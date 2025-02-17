package com.harper.asteroids

import com.harper.asteroids.model.NearEarthObject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.time.LocalDate

/**
 * Receives a set of neo ids and rates them after earth proximity. Retrieves the approach data for
 * them and sorts to the n closest. https://api.nasa.gov/neo/rest/v1/neo/ Alerts if someone is
 * possibly hazardous.
 */
class ApproachDetector(private val nearEarthObjectIds: MutableList<Any>?, private val neoApiFacade: NeoApiFacade) {

    /**
     * Get the n closest approaches in this period
     *
     * @param limit - n
     */
    suspend fun getClosestApproaches(limit: Int): List<NearEarthObject> {
        val neos = coroutineScope {
            nearEarthObjectIds?.map { id -> async { neoApiFacade.getNeoDetails(id as String) } }
        }?.awaitAll() ?: emptyList()

        println("Received " + neos.size + " neos, now sorting")

        return getClosest(neos, limit)
    }

    companion object {

        /**
         * Get the closest passing.
         *
         * @param neos the NearEarthObjects
         * @param limit
         * @return
         */
        fun getClosest(
            neos: List<NearEarthObject>,
            limit: Int,
            now: LocalDate = LocalDate.now()
        ): List<NearEarthObject> {
            return neos
                .filter { it.closestApproachInWeek(now) != null }
                .sortedWith(VicinityComparator(now))
                .take(limit)
        }
    }
}
