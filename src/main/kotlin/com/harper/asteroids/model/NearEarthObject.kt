package com.harper.asteroids.model

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Definition for Neo - Near Earth Object
 */
class NearEarthObject {
    @JsonProperty("id")
    val id: String? = null

    @JsonProperty("name")
    val name: String? = null

    @JsonProperty("nasa_jpl_url")
    val nplUrl: String? = null

    @JsonProperty("absolute_magnitude_h")
    val absoluteMagnitude: Double = 0.0

    @JsonProperty("is_potentially_hazardous_asteroid")
    val isPotentiallyHazardous: Boolean = false

    @JsonProperty("close_approach_data")
    var closeApproachData: List<CloseApproachData>? = null

    @JsonProperty("is_sentry_object")
    val isSentryObject: Boolean = false
}