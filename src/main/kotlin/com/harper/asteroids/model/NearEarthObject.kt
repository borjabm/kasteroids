package com.bbm.com.harper.asteroids.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Definition for Neo - Near Earth Object
 *
 * TODO: why the h*** must I add this annotation to ignore unknown properties when I set it on ObjectMapper?
 */
@JsonIgnoreProperties(ignoreUnknown = true)
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
    val closeApproachData: List<CloseApproachData>? = null

    @JsonProperty("is_sentry_object")
    val isSentryObject: Boolean = false
}