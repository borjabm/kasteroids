package com.harper.asteroids

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
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
class App {
    private val neoApiFacade: NeoApiFacade
    private val config = Config()

    init {
        val httpClient = HttpClient(CIO.create()) { install(ContentNegotiation) { json(json = NeoApiJsonParser.json) } }
        neoApiFacade = NeoApiFacade(config, httpClient)
    }

    /** Scan space for asteroids close to earth */
    suspend fun checkForAsteroids() {
        val feed = neoApiFacade.getTodayFeed()
        val approachDetector = ApproachDetector(feed.allObjectIds, neoApiFacade)

        val closest = approachDetector.getClosestApproaches(config.neosLimit)
        println("Hazard?   Distance(km)    When                             Name")
        println("----------------------------------------------------------------------")
        val now = LocalDate.now()
        closest.forEach {
            println(
                java.lang.String.format(
                    "%s       %12.3f  %s    %s",
                    (if (it.isPotentiallyHazardous) "!!!" else " - "),
                    it.closestApproachInWeek(now)?.missDistance!!.kilometers,
                    it.closestApproachInWeek(now)?.closeApproachDateTime,
                    it.name
                )
            )
        }
    }
}

suspend fun main() {
    App().checkForAsteroids()
}
