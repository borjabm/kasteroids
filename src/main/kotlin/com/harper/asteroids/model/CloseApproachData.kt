package com.harper.asteroids.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
class CloseApproachData {
    @JsonProperty("close_approach_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    val closeApproachDate: Date? = null

    @JsonProperty("close_approach_date_full")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MMM-dd hh:mm", locale = "en_GB")
    val closeApproachDateTime: Date? = null

    @JsonProperty("epoch_date_close_approach")
    val closeApproachEpochDate: Long = 0

    @JsonProperty("relative_velocity")
    val relativeVelocity: Velocities? = null

    @JsonProperty("miss_distance")
    val missDistance: Distances? = null

    @JsonProperty("orbiting_body")
    val orbitingBody: String? = null
}