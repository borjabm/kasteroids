package com.harper.asteroids

import com.harper.asteroids.model.NearEarthObject
import com.harper.asteroids.utils.NasaClient
import io.ktor.client.call.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.datetime.LocalDate
import java.io.IOException

/**
 * Receives a set of neo ids and rates them after earth proximity.
 * Retrieves the approach data for them and sorts to the n closest.
 * https://api.nasa.gov/neo/rest/v1/neo/
 * Alerts if someone is possibly hazardous.
 */
class ApproachDetector(
    private val client: NasaClient,
    private val today: LocalDate,
    private val nearEarthObjectIds: List<String>
) {
    /**
     * Get the n closest approaches in this period
     * @param limit - n
     */
    suspend fun getClosestApproaches(limit: Int): List<NearEarthObject> {
        val neos = coroutineScope {
            nearEarthObjectIds.map { id ->
                async {
                    try {
                        println("Check passing of object $id")

                        val response = client.getNeo(id)
                        val neo: NearEarthObject = response.body()

                        println("Check passing of object $id - fetched")

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

    /**
     * Get the closest passing.
     * @param neos the NearEarthObjects
     * @param limit
     * @return
     */
    fun getClosest(today: LocalDate, neos: List<NearEarthObject>, limit: Int): List<NearEarthObject> =
        neos.filter { it.closeApproachData.isNotEmpty() }
            .sortedWith(VicinityComparator(today))
            .take(limit)
}