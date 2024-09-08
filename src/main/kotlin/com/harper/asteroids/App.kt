package com.harper.asteroids

import com.harper.asteroids.model.Feed
import com.harper.asteroids.utils.NasaClient
import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import java.io.IOException

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
fun main() = runBlocking {
    NasaClient().use {
        checkForAsteroids(it)
    }
}

private suspend fun checkForAsteroids(client: NasaClient) {
    val limit = 10
    val today = LocalDate.now()
    val response: HttpResponse = client.getFeed(today, today);

    println("Got response: $response")

    if (response.status == HttpStatusCode.OK) {
        try {
            val neoFeed: Feed = response.body()
            val approachDetector = ApproachDetector(client, today, neoFeed.allObjectIds)

            val closest = approachDetector.getClosestApproaches(limit)
            println("Hazard?   Distance(km)    When                             Name")
            println("----------------------------------------------------------------------")
            for (neo in closest) {
                val closestPass = neo.closeApproachData.minByOrNull { it.missDistance!! } //TODO: Think what if no missDistance (instead of NPE)

                if (closestPass == null) continue

                val hazardMarker = if (neo.isPotentiallyHazardous) "!!!" else " - "
                val kilometers = String.format("%12.3f", closestPass.missDistance!!.kilometers)
                val dateTime = closestPass.closeApproachDateTime
                val name = neo.name

                println("$hazardMarker       $kilometers  $dateTime    $name")
            }
        } catch (e: IOException) {
            println("Failed scanning for asteroids: $e")
        }
    } else {
        println("Failed querying feed, got " + response.status + " " + response.status.description)
    }
}
