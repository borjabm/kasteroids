package com.harper.asteroids

import com.harper.asteroids.model.CloseApproachData
import com.harper.asteroids.model.Feed
import com.harper.asteroids.model.NearEarthObject
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import java.io.IOException
import java.time.LocalDate
import java.util.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonBuilder

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
    private val NEO_FEED_URL = "https://api.nasa.gov/neo/rest/v1/feed"
    private val httpClient: HttpClient

    init {
        val apiKey: String? = System.getenv("API_KEY")

        if (!apiKey.isNullOrEmpty()) {
            API_KEY = apiKey
        }
        val json =
            Json(
                builderAction =
                    fun JsonBuilder.() {
                        explicitNulls = false
                        ignoreUnknownKeys = true
                        isLenient = true
                    })

        httpClient = HttpClient(CIO.create()) { install(ContentNegotiation) { json(json = json) } }
    }

    /** Scan space for asteroids close to earth */
    suspend fun checkForAsteroids() {
        val today = LocalDate.now()

        val respK: HttpResponse =
            httpClient.get(NEO_FEED_URL) {
                parameter("start_date", today.toString())
                parameter("end_date", today.toString())
                parameter("api_key", API_KEY)
                contentType(ContentType.Application.Json)
            }

        if (respK.status == HttpStatusCode.OK) {
            val json = Json {
                explicitNulls = false
                isLenient = true
            }

            val bodyAsText = respK.bodyAsText()

            try {
                val neoFeedK: Feed = json.decodeFromString<Feed>(bodyAsText)
                val approachDetector = ApproachDetector(neoFeedK.allObjectIds)

                val closest: MutableList<NearEarthObject>? =
                    approachDetector.getClosestApproaches(10)
                println("Hazard?   Distance(km)    When                             Name")
                println("----------------------------------------------------------------------")
                for (neo in closest!!) {
                    val closestPass: Optional<CloseApproachData> =
                        neo.closeApproachData!!
                            .stream()
                            .min(Comparator.comparing(CloseApproachData::missDistance))

                    if (closestPass.isEmpty) continue

                    println(
                        java.lang.String.format(
                            "%s       %12.3f  %s    %s",
                            (if (neo.isPotentiallyHazardous) "!!!" else " - "),
                            closestPass.get().missDistance!!.kilometers,
                            closestPass.get().closeApproachDateTime,
                            neo.name))
                }
            } catch (e: IOException) {
                println("Failed scanning for asteroids: $e")
            }
        } else {
            println(("Failed querying feed, got " + respK.status.value) + " " + respK.status)
        }
    }

    companion object {
        var API_KEY: String = "DEMO_KEY"
    }
}

suspend fun main() {
    App().checkForAsteroids()
}
