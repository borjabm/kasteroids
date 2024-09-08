package com.harper.asteroids.utils

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json
import java.io.Closeable

private const val DEFAULT_API_KEY = "DEMO_KEY"

class NasaClient: Closeable {

    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 10000
        }
        defaultRequest {
            accept(ContentType.Application.Json)
            url("https", "api.nasa.gov") {
                parameters.append("api_key", getApiKey())
            }
        }
    }

    private fun getApiKey(): String =
        System.getenv("API_KEY")
            ?.let { it.ifBlank { null } }
            ?: DEFAULT_API_KEY

    override fun close() {
        httpClient.close()
    }

    suspend fun getNeo(id: String): HttpResponse =
        httpClient.get("neo/rest/v1/neo/$id")

    suspend fun getFeed(startDate: LocalDate, endDate: LocalDate): HttpResponse =
        httpClient.get("neo/rest/v1/feed") {
            parameter("start_date", startDate.toString())
            parameter("end_date", endDate.toString())
        }
}