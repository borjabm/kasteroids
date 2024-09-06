package com.harper.asteroids.model

import com.harper.asteroids.utils.NasaDateSerializer
import com.harper.asteroids.utils.NasaDateTimeSerializer
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class CloseApproachData(
    @SerialName("close_approach_date")
    @Serializable(with = NasaDateSerializer::class)
    val closeApproachDate: LocalDate? = null,

    @SerialName("close_approach_date_full")
    @Serializable(with = NasaDateTimeSerializer::class)
    val closeApproachDateTime: LocalDateTime? = null,

    @SerialName("epoch_date_close_approach")
    val closeApproachEpochDate: Long = 0,

    @SerialName("relative_velocity")
    val relativeVelocity: Velocities? = null,

    @SerialName("miss_distance")
    val missDistance: Distances? = null,

    @SerialName("orbiting_body")
    val orbitingBody: String? = null
)
