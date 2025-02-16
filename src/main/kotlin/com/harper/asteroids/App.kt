package com.harper.asteroids

import com.harper.asteroids.client.NasaApiClient
import com.harper.asteroids.model.Feed
import com.harper.asteroids.model.NearEarthObject
import java.time.LocalDate

/**
 * Main app. Gets the list of closest asteroids from NASA at
 * https://api.nasa.gov/neo/rest/v1/feed?start_date=START_DATE&end_date=END_DATE&api_key=API_KEY See
 * documentation on the Asteroids - NeoWs API at https://api.nasa.gov/
 *
 * Prints the 10 closest
 *
 * Risk of getting throttled if we don't sign up for own key on https://api.nasa.gov/ Set
 * environment variable 'API_KEY' to override.
 */
class App(private val nasaApiClient: NasaApiClient = NasaApiClient()) {
    
    /** Scan space for asteroids close to earth */
    fun checkForAsteroids() {
        val today = LocalDate.now()
        val neoFeedK: Feed = nasaApiClient.getNeoFeed(today, today)
        val approachDetector = ApproachDetector(neoFeedK.allObjectIds)
        
        val closest: List<NearEarthObject> = approachDetector.getClosestApproaches(10)
        println("Hazard?   Distance(km)    When                             Name")
        println("----------------------------------------------------------------------")
        
        closest.forEach { neo ->
            neo.closeApproachData
                .filter { it.missDistance?.kilometers != null }
                .minByOrNull { it.missDistance?.kilometers ?: 0.0 }?.let { closestPass ->
                    println(
                        String.format(
                            "%s       %12.3f  %s    %s",
                            (if (neo.isPotentiallyHazardous) "!!!" else " - "),
                            closestPass.missDistance!!.kilometers,
                            closestPass.closeApproachDateTime,
                            neo.name
                        )
                    )
                }
        }
    }
}

fun main() {
    App().checkForAsteroids()
}
