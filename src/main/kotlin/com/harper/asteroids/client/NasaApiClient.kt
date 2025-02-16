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
    private val neoApiUrl = "https://api.nasa.gov/neo/rest/v1"
    private val feedUrl = "$neoApiUrl/feed"
    private val lookupUrl = "$neoApiUrl/neo"
    private val concurrentRequestsCount = 8
    
    fun getNeoFeed(startDate: LocalDate, endDate: LocalDate): Feed = runBlocking {
        log.info("Getting NEO feed from $startDate till $endDate")
        httpClient.get(feedUrl) {
            parameter("start_date", startDate)
            parameter("end_date", endDate)
            parameter("api_key", apiKey)
        }.body()
    }
    
    fun lookupByIds(asteroidIds: List<Int>): List<NearEarthObject> = runBlocking {
        log.info("Looking up ${asteroidIds.size} asteroids in concurrent chunks of size $concurrentRequestsCount")
        asteroidIds.chunked(concurrentRequestsCount)
            .mapIndexed{ idx, asteroidIdsChunk ->
                log.info("Fetching chunk $idx for ids: $asteroidIdsChunk")
                asteroidIdsChunk
                    .map { asteroidId -> async { lookupById(asteroidId) } }
                    .toList()
                    .awaitAll()
            }.flatten()
    }
    
    private suspend fun lookupById(asteroidId: Int): NearEarthObject {
        log.debug("Check passing of object $asteroidId")
        return httpClient.get("$lookupUrl/$asteroidId") {
            parameter("api_key", apiKey)
        }.body()
    }
}
