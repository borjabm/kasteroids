package com.harper.asteroids.client

import com.harper.asteroids.model.Feed
import com.harper.asteroids.model.NearEarthObject
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import java.time.LocalDate

class NasaApiClient(
    private val httpClient: HttpClient = NasaApiConfig.httpClient,
    private val apiKey: String = NasaApiConfig.apiKey
) {
    private val log = LoggerFactory.getLogger(javaClass)
    private val API_URL = "https://api.nasa.gov"
    private val NEO_FEED_URL = "$API_URL/neo/rest/v1/feed"
    private val NEO_LOOKUP_URL = "$API_URL/neo/rest/v1/neo"
    
    fun getNeoFeed(startDate: LocalDate, endDate: LocalDate): Feed = runBlocking {
        log.info("Getting NEO feed from $startDate till $endDate")
        httpClient.get(NEO_FEED_URL) {
            parameter("start_date", startDate)
            parameter("end_date", endDate)
            parameter("api_key", apiKey)
        }.body()
    }
    
    fun lookupByIds(asteroidIds: List<Int>): List<NearEarthObject> = runBlocking {
        asteroidIds
            .map { asteroidId ->
                async { lookupById(asteroidId) } }
            .toList()
            .awaitAll()
    }
    
    private suspend fun lookupById(asteroidId: Int): NearEarthObject {
        log.info("Check passing of object $asteroidId")
        return httpClient.get("$NEO_LOOKUP_URL/$asteroidId") {
            parameter("api_key", apiKey)
        }.body()
    }
}