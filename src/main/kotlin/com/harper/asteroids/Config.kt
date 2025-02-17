package com.harper.asteroids

class Config(private val env: Map<String, String> = System.getenv()) {
    fun getNeoApiKey(): String {
        return env["API_KEY"] ?: "DEMO_KEY"
    }

    val neosLimit: Int
        get() = 10
}