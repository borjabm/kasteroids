package com.harper.asteroids

import com.fasterxml.jackson.databind.ObjectMapper
import com.harper.asteroids.model.CloseApproachData
import com.harper.asteroids.model.Feed
import com.harper.asteroids.model.NearEarthObject
import com.harper.asteroids.utils.NasaObjectMapper
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import java.io.IOException
import java.time.LocalDate
import java.util.*

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
    private val client = HttpClient(CIO) {
        install(HttpTimeout) {
            requestTimeoutMillis = 10000
        }
    }

    private val mapper: ObjectMapper = NasaObjectMapper()

    /**
     * Scan space for asteroids close to earth
     */
    private suspend fun checkForAsteroids() {
        val today = LocalDate.now()
        val response = client.get(NEO_FEED_URL) {
            parameter("api_key", App.API_KEY)
            parameter("start_date", today.toString())
            parameter("end_date", today.toString())
            accept(ContentType.Application.Json)
        }

        println("Got response: $response")
        if (response.status == HttpStatusCode.OK) {
            val content: String = response.bodyAsText()

            try {
                val neoFeed: Feed = mapper.readValue(content, Feed::class.java)
                val approachDetector: ApproachDetector = ApproachDetector(today, neoFeed.allObjectIds)

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
            println("Failed querying feed, got " + response.status + " " + response.status.description)
        }
    }


    companion object {
        private const val NEO_FEED_URL = "https://api.nasa.gov/neo/rest/v1/feed"

        var API_KEY: String = "DEMO_KEY"

        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            val apiKey = System.getenv("API_KEY")
            if (apiKey != null && !apiKey.isBlank()) {
                API_KEY = apiKey
            }
            App().checkForAsteroids()
        }
    }
}