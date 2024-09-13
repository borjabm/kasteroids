package com.harper.asteroids.clients

import com.fasterxml.jackson.databind.DeserializationFeature
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.utils.io.core.*

private object HttpRoutes {
    const val NEO_URL = "/neo/rest/v1/neo"
}

class NeoWsClient : Closeable {

    private val client = HttpClient(CIO) {
        // throws exception on status other than 2XX
        expectSuccess = true

        install(ContentNegotiation) {
            jackson {
                configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            }
        }

        install(DefaultRequest) {
            url {
                protocol = URLProtocol.HTTPS
                host = "api.nasa.gov"
                parameters.append("api_key", getApiKey())
            }
            accept(ContentType.Application.Json)
        }
    }

    suspend fun getNeo(id: String): HttpResponse {
        return client.get("${HttpRoutes.NEO_URL}/$id")
    }

    private fun getApiKey(): String {
        val apiKey = System.getenv("API_KEY")

        if (apiKey.isNullOrBlank()) {
            return DEFAULT_API_KEY
        }

        return apiKey
    }

    override fun close() {
        client.close()
    }

    companion object {
        const val DEFAULT_API_KEY = "DEMO_KEY"
    }
}