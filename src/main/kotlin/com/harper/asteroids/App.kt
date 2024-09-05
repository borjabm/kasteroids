package com.harper.asteroids

import com.harper.asteroids.model.CloseApproachData
import com.harper.asteroids.model.Feed
import com.harper.asteroids.model.NearEarthObject
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import org.glassfish.jersey.client.ClientConfig
import java.io.IOException
import java.time.LocalDate
import java.util.*
import javax.ws.rs.client.Client
import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * Main app. Gets the list of closest asteroids from NASA at
 * https://api.nasa.gov/neo/rest/v1/feed?start_date=START_DATE&end_date=END_DATE&api_key=API_KEY
 * See documentation on the Asteroids - NeoWs API at https://api.nasa.gov/
 *
 * Prints the 10 closest
 *
 * Risk of getting throttled if we don't sign up for own key on https://api.nasa.gov/
 * Set environment variable 'API_KEY' to override.
 */
class App {
    private val client: Client

    private val mapper: ObjectMapper = ObjectMapper()

    init {
        val configuration: ClientConfig = ClientConfig()
        client = ClientBuilder.newClient(configuration)
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }

    /**
     * Scan space for asteroids close to earth
     */
    private fun checkForAsteroids() {
        val today = LocalDate.now()
        val response: Response = client
            .target(NEO_FEED_URL)
            .queryParam("start_date", today.toString())
            .queryParam("end_date", today.toString())
            .queryParam("api_key", API_KEY)
            .request(MediaType.APPLICATION_JSON)
            .get()
        println("Got response: $response")
        if (response.getStatus() === Response.Status.OK.getStatusCode()) {
            val mapper: ObjectMapper = ObjectMapper()
            val content: String = response.readEntity(String::class.java)


            try {
                val neoFeed: Feed = mapper.readValue(content, Feed::class.java)
                val approachDetector: ApproachDetector = ApproachDetector(neoFeed.allObjectIds)

                val closest: MutableList<NearEarthObject>? = approachDetector.getClosestApproaches(10)
                println("Hazard?   Distance(km)    When                             Name")
                println("----------------------------------------------------------------------")
                for (neo in closest!!) {
                    val closestPass: Optional<CloseApproachData> = neo.closeApproachData!!.stream()
                        .min(Comparator.comparing(CloseApproachData::missDistance))

                    if (closestPass.isEmpty()) continue

                    println(
                        java.lang.String.format(
                            "%s       %12.3f  %s    %s",
                            (if (neo.isPotentiallyHazardous) "!!!" else " - "),
                            closestPass.get().missDistance!!.kilometers,
                            closestPass.get().closeApproachDateTime,
                            neo.name
                        )
                    )
                }
            } catch (e: IOException) {
                println("Failed scanning for asteroids: $e")
            }
        } else {
            println(("Failed querying feed, got " + response.getStatus()).toString() + " " + response.getStatusInfo())
        }
    }


    companion object {
        private const val NEO_FEED_URL = "https://api.nasa.gov/neo/rest/v1/feed"

        var API_KEY: String = "DEMO_KEY"

        @JvmStatic
        fun main(args: Array<String>) {
            val apiKey = System.getenv("API_KEY")
            if (apiKey != null && !apiKey.isBlank()) {
                API_KEY = apiKey
            }
            App().checkForAsteroids()
        }
    }
}