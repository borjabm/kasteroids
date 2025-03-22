package com.harper.asteroids.model

import com.harper.asteroids.utils.DateSerializer
import com.harper.asteroids.utils.SimpleDateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalDateTime

@Serializable
class CloseApproachData {
    @SerialName("close_approach_date")
    @Serializable(with = SimpleDateSerializer::class)
    val closeApproachDate: LocalDate? = null

    @SerialName("close_approach_date_full")
    @Serializable(with = DateSerializer::class)
    val closeApproachDateTime: LocalDateTime? = null

    @SerialName("epoch_date_close_approach")
    val closeApproachEpochDate: Long = 0

    @SerialName("relative_velocity")
    val relativeVelocity: Velocities? = null

    @SerialName("miss_distance")
    val missDistance: Distances? = null

    @SerialName("orbiting_body")
    val orbitingBody: String? = null

    fun isCloseApproachDateWithinNextWeek(): Boolean {
        if (closeApproachDate == null) return false
        val today = LocalDate.now()
        val nextSevenDays = today.plusDays(7)
        return closeApproachDate in today..nextSevenDays
    }
}

