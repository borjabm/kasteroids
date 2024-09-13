package com.harper.asteroids

import com.harper.asteroids.clients.NeoWsClient
import com.harper.asteroids.model.CloseApproachData
import com.harper.asteroids.model.NearEarthObject
import io.ktor.client.call.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.time.Instant
import java.time.temporal.ChronoUnit

/**
 * Receives a set of neo ids and rates them after earth proximity.
 * Retrieves the approach data for them and sorts to the n closest.
 * https://api.nasa.gov/neo/rest/v1/neo/
 * Alerts if someone is possibly hazardous.
 */
class ApproachDetector(private val nearEarthObjectIds: MutableList<Any>?) {
    private val httpClient = NeoWsClient()

    /**
     * Get the n closest approaches in this period
     * @param limit - n
     */
    suspend fun getClosestApproaches(limit: Int): List<NearEarthObject> {
        val deferred: MutableList<Deferred<NearEarthObject>> = ArrayList()

        coroutineScope {
            for (id in nearEarthObjectIds!!) {
                println("Check passing of object $id")

                val result: Deferred<NearEarthObject> = async { httpClient.getNeo(id.toString()).body() }
                deferred.add(result)
            }
        }

        val neos: List<NearEarthObject> = deferred.awaitAll()
        println("Received " + neos.size + " neos, now sorting")

        val closest: List<NearEarthObject> = getClosest(neos, limit)
        return closest
    }

    companion object {

        /**
         * Get the closest passing.
         * @param neos the NearEarthObjects
         * @param limit
         * @return
         */
        fun getClosest(neos: List<NearEarthObject>, limit: Int): List<NearEarthObject> {
            val startDate: Instant = Instant.now()
            val endDate: Instant = startDate.plus(7, ChronoUnit.DAYS)

            return neos
                .map { neo ->
                    neo.closeApproachData = getClosestInRange(startDate, endDate, neo.closeApproachData!!)
                    neo
                }
                .filter { it.closeApproachData!!.isNotEmpty() }
                .sortedWith(Comparator.nullsFirst(VicinityComparator(startDate, endDate)))
                .take(limit)
        }

        private fun getClosestInRange(
            startDate: Instant,
            endDate: Instant,
            closeApproachData: List<CloseApproachData>
        ): List<CloseApproachData> {

            return closeApproachData.filter {
                it.closeApproachDateTime!!.toInstant() in startDate..endDate
            }
        }
    }
}