package com.harper.asteroids.model

import com.fasterxml.jackson.annotation.JsonProperty

class Velocities {
    @JsonProperty("kilometers_per_second")
    var kilometersPerSecond: Double = 0.0

    @JsonProperty("kilometers_per_hour")
    var kilometersPerHour: Double = 0.0

    @JsonProperty("miles_per_hour")
    var milesPerHour: Double = 0.0
}