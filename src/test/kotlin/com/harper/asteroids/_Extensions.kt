package com.harper.asteroids

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json
import java.io.InputStreamReader

val json = Json { ignoreUnknownKeys = true }

inline fun <reified T> decodeFromResourceFile(fileName: String): T =
    json.decodeFromString<T>(readResourceFile(fileName))

fun readResourceFile(fileName: String): String {
    return {}
        .javaClass.classLoader.getResourceAsStream(fileName)
        ?.let { InputStreamReader(it).readText() }
        ?: throw IllegalArgumentException("File $fileName not found in resources")
}

fun LocalDate.Companion.now() =
    Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

