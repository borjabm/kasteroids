package com.harper.asteroids.utils

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonBuilder

val jsonParser = Json(
    builderAction =
        fun JsonBuilder.() {
            explicitNulls = false
            ignoreUnknownKeys = true
            isLenient = true
        })