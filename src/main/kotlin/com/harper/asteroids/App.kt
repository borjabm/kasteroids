package com.harper.asteroids

import com.harper.asteroids.model.CloseApproachData
import com.harper.asteroids.model.Feed
import com.harper.asteroids.model.NearEarthObject
import com.harper.asteroids.utils.NasaClient
import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import java.io.IOException
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
    /**
     * Scan space for asteroids close to earth
     */
    private suspend fun checkForAsteroids(client: NasaClient) {
        val today = LocalDate.now()
        val response: HttpResponse = client.getFeed(today, today);

        println("Got response: $response")
        if (response.status == HttpStatusCode.OK) {
            try {
                val neoFeed: Feed = response.body()
                val approachDetector: ApproachDetector = ApproachDetector(client, today, neoFeed.allObjectIds)

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
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            NasaClient().use {
                App().checkForAsteroids(it)
            }
        }
    }
}