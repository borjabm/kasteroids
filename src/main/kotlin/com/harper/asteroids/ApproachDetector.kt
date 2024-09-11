package com.harper.asteroids

import com.harper.asteroids.model.NearEarthObject
import java.io.IOException
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.ws.rs.client.Client
import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.core.MediaType

/**
 * Receives a set of neo ids and rates them after earth proximity.
 * Retrieves the approach data for them and sorts to the n closest.
 * https://api.nasa.gov/neo/rest/v1/neo/
 * Alerts if someone is possibly hazardous.
 */
class ApproachDetector(private val nearEarthObjectIds: MutableList<Any>?) {
    private val client: Client = ClientBuilder.newClient()

    /**
     * Get the n closest approaches in this period
     * @param limit - n
     */
    fun getClosestApproaches(limit: Int): List<NearEarthObject> {
        val neos: MutableList<NearEarthObject> = ArrayList(limit)
        for (id in nearEarthObjectIds!!) {
            try {
                println("Check passing of object $id")
                val response = client
                    .target(NEO_URL + id)
                    .queryParam("api_key", App.API_KEY)
                    .request(MediaType.APPLICATION_JSON)
                    .get()

                val neo: NearEarthObject = JacksonMapper.instance.readValue(
                    response.readEntity(String::class.java),
                    NearEarthObject::class.java
                )
                neos.add(neo)
            } catch (e: IOException) {
                println("Failed scanning for asteroids: $e")
            }
        }
        println("Received " + neos.size + " neos, now sorting")

        return getClosest(neos, limit)
    }

    companion object {
        private const val NEO_URL = "https://api.nasa.gov/neo/rest/v1/neo/"

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
                .filter { !it.closeApproachData.isNullOrEmpty() }
                .sortedWith(VicinityComparator(startDate, endDate))
                .take(limit)
        }
    }
}