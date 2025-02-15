package com.harper.asteroids

import com.harper.asteroids.client.NasaApiClient
import com.harper.asteroids.model.NearEarthObject
import org.slf4j.LoggerFactory
import java.util.stream.Collectors

/**
 * Receives a set of neo ids and rates them after earth proximity. Retrieves the approach data for
 * them and sorts to the n closest. https://api.nasa.gov/neo/rest/v1/neo/ Alerts if someone is
 * possibly hazardous.
 */
class ApproachDetector(private val nearEarthObjectIds: List<Int>, private val nasaApiClient: NasaApiClient = NasaApiClient()) {
    private val log = LoggerFactory.getLogger(javaClass)
    
    /**
     * Get the n closest approaches in this period
     *
     * @param limit - n
     */
    fun getClosestApproaches(limit: Int): List<NearEarthObject> {
        val neoList = nasaApiClient.lookupByIds(nearEarthObjectIds)
        log.info("Received " + neoList.size + " neos, now sorting")
        return getClosest(neoList, limit)
    }

    companion object {
        /**
         * Get the closest passing.
         *
         * @param neos the NearEarthObjects
         * @param limit
         * @return
         */
        fun getClosest(neos: List<NearEarthObject>, limit: Int): List<NearEarthObject> {
            // TODO: Should ignore the passes that are not today/this week.
            return neos
                .stream()
                .filter { neo: NearEarthObject -> !neo.closeApproachData.isNullOrEmpty() }
                .sorted(VicinityComparator())
                .limit(limit.toLong())
                .collect(Collectors.toList())
        }
    }
}
