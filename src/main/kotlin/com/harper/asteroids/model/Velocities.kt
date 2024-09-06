package com.harper.asteroids.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
class Velocities(
    @SerialName("kilometers_per_second")
    var kilometersPerSecond: Double = 0.0,

    @SerialName("kilometers_per_hour")
    var kilometersPerHour: Double = 0.0,

    @SerialName("miles_per_hour")
    var milesPerHour: Double = 0.0
)