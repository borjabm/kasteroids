package com.harper.asteroids

import com.harper.asteroids.model.NearEarthObject
import com.harper.asteroids.utils.NasaObjectMapper
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.io.IOException
import java.time.LocalDate
import java.util.function.Predicate
import java.util.stream.Collectors

/**
 * Receives a set of neo ids and rates them after earth proximity.
 * Retrieves the approach data for them and sorts to the n closest.
 * https://api.nasa.gov/neo/rest/v1/neo/
 * Alerts if someone is possibly hazardous.
 */
class ApproachDetector(
    private val today: LocalDate,
    private val nearEarthObjectIds: MutableList<Any>?
) {

    private val client = HttpClient(CIO) {
        install(HttpTimeout) {
            requestTimeoutMillis = 10000
        }
    }

    private val mapper = NasaObjectMapper()

    /**
     * Get the n closest approaches in this period
     * @param limit - n
     */
    suspend fun getClosestApproaches(limit: Int): MutableList<NearEarthObject>? {
        val neos = coroutineScope {
            nearEarthObjectIds!!.map { id ->
                async {
                    try {
                        println("Check passing of object $id")
                        val response = client.get(NEO_URL + id) {
                            parameter("api_key", App.API_KEY)
                            accept(ContentType.Application.Json)
                        }

                        val neo: NearEarthObject = mapper.readValue(
                            response.bodyAsText(),
                            NearEarthObject::class.java
                        )

                        println("Check passing of object $id - done")
                        neo
                    } catch (e: IOException) {
                        println("Failed scanning for asteroids: $e")
                        throw e
                    }
                }
            }.awaitAll()
        }

        println("Received " + neos.size + " neos, now sorting")

        return getClosest(today, neos, limit)
    }

    companion object {
        private const val NEO_URL = "https://api.nasa.gov/neo/rest/v1/neo/"

        /**
         * Get the closest passing.
         * @param neos the NearEarthObjects
         * @param limit
         * @return
         */
        fun getClosest(today: LocalDate, neos: List<NearEarthObject>, limit: Int): MutableList<NearEarthObject>? {
            return neos.stream()
                .filter(Predicate<NearEarthObject> { neo: NearEarthObject ->
                    neo.closeApproachData != null && !neo.closeApproachData.isEmpty()
                })
                .sorted(VicinityComparator(today))
                .limit(limit.toLong())
                .collect(Collectors.toList<NearEarthObject>())
        }
    }
}