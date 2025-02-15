package com.harper.asteroids.client

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory

object NasaApiConfig {
    
    private val log = LoggerFactory.getLogger(javaClass)
    
    val apiKey: String = System.getenv("API_KEY") ?: let {
        log.warn("API_KEY environment variable is not present. Using hardcoded API Key which might not work in the future")
        "1F9CYkWnvYv2nvNeVhmTWswDTQbotIE9DIFLOPuq"
    }
    
    val httpClient: HttpClient = HttpClient(CIO) {
        val json = Json {
            explicitNulls = false
            ignoreUnknownKeys = true
            isLenient = true
        }
        expectSuccess = true
        install(Logging) {
            // Change if needed for debugging purposes
            level = LogLevel.NONE
        }
        install(ContentNegotiation) { json(json) }
        
        defaultRequest {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            header(HttpHeaders.Accept, ContentType.Application.Json)
        }
    }
}