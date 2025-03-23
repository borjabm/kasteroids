package com.harper.asteroids

import com.harper.asteroids.App.Companion.API_KEY
import com.harper.asteroids.model.NearEarthObject
import com.harper.asteroids.utils.jsonParser
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

/**
 * Receives a set of neo ids and rates them after earth proximity. Retrieves the approach data for
 * them and sorts to the n closest. https://api.nasa.gov/neo/rest/v1/neo/ Alerts if someone is
 * possibly hazardous.
 */
class ApproachDetector(private val nearEarthObjectIds: MutableList<Any>?) {

    private val httpClient: HttpClient =
        HttpClient(CIO.create()) { install(ContentNegotiation) { json(json = jsonParser) } }

    /**
     * Get the n closest approaches in this period
     *
     * @param limit - n
     */
    suspend fun getClosestApproaches(limit: Int): MutableList<NearEarthObject> {
        val neos = coroutineScope {
            val neos = nearEarthObjectIds!!.map {
                async {
                    println("Check passing of object $it")
                    val respK: HttpResponse =
                        httpClient.get(NEO_URL + it) {
                            parameter("api_key", API_KEY)
                            contentType(ContentType.Application.Json)
                        }
                    return@async jsonParser.decodeFromString<NearEarthObject>(respK.bodyAsText())
                }
            }.awaitAll()
            return@coroutineScope neos
        }
        println("Received " + neos.size + " neos, now sorting")

        return getClosest(neos, limit)
    }

    companion object {
        private const val NEO_URL = "https://api.nasa.gov/neo/rest/v1/neo/"

        /**
         * Get the closest passing.
         *
         * @param neos the NearEarthObjects
         * @param limit
         * @return
         */
        fun getClosest(neos: List<NearEarthObject>, limit: Int): MutableList<NearEarthObject> {
            // TODO: Should ignore the passes that are not today/this week.
            return neos.asSequence()
                .map { it.copy(closeApproachData = it.getCloseApproachDataWithinNextWeek()) }
                .filterNot { it.closeApproachData.isNullOrEmpty() }
                .sortedWith(VicinityComparator())
                .take(limit)
                .toMutableList()
        }
    }
}
