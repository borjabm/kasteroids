package com.harper.asteroids.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneOffset.UTC

/**
 * Definition for Neo - Near Earth Object
 */
@Serializable
class NearEarthObject {

    @SerialName("name")
    val name: String? = null

    @SerialName("is_potentially_hazardous_asteroid")
    val isPotentiallyHazardous: Boolean = false

    @SerialName("close_approach_data")
    val closeApproachData: List<CloseApproachData>? = null

    // can be as well an extension function, just wanted to demonstrate more domain driven approach
    fun closestApproachInWeek(now: LocalDate): CloseApproachData? {
        val currentWeek = now.with(DayOfWeek.MONDAY)..now.with(DayOfWeek.SUNDAY)
        return this.closeApproachData?.filter {
            it.closeApproachDateTime?.let { dateTime -> LocalDate.ofInstant(dateTime, UTC) in currentWeek } ?: false
        }?.minByOrNull { it.missDistance?.kilometers ?: Double.MAX_VALUE }
    }
}
