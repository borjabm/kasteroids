package com.harper.asteroids

import com.harper.asteroids.model.Feed
import com.harper.asteroids.model.NearEarthObject
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import java.time.LocalDate

class NeoApiFacade(private val config: Config, private val httpClient: HttpClient) {
    companion object {
        private const val BASE_URL = "https://api.nasa.gov/neo/rest/v1";
        private const val NEO_FEED_URL = "${BASE_URL}/feed"
        private const val NEO_URL = "${BASE_URL}/neo/"
    }

    suspend fun getTodayFeed(): Feed {
        val today = LocalDate.now()
        val response = httpClient.get(NEO_FEED_URL) {
            parameter("start_date", today.toString())
            parameter("end_date", today.toString())
            parameter("api_key", config.getNeoApiKey())
            contentType(ContentType.Application.Json)
        }

        if (response.status != HttpStatusCode.OK) {
            throw IllegalStateException("Failed querying feed, got ${response.status.value}")
        }

        return response.body()
    }

    suspend fun getNeoDetails(id: String): NearEarthObject {
        val response = httpClient.get(NEO_URL + id) {
            parameter("api_key", config.getNeoApiKey())
            contentType(ContentType.Application.Json)
        }

        if (response.status != HttpStatusCode.OK) {
            throw IllegalStateException("Failed querying neo details, got ${response.status.value}")
        }

        return response.body()
    }
}

object NeoApiJsonParser {
    val json = Json {
        explicitNulls = false
        isLenient = true
        ignoreUnknownKeys = true
    }
}