package com.harper.asteroids.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

/**
 * Definition for Neo - Near Earth Object
 *
 * TODO: why the h*** must I add this annotation to ignore unknown properties when I set it on Json
 *   parser?
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
class NearEarthObject {
    @SerialName("id") val id: String? = null

    @SerialName("name") val name: String? = null

    @SerialName("nasa_jpl_url") val nplUrl: String? = null

    @SerialName("absolute_magnitude_h") val absoluteMagnitude: Double = 0.0

    @SerialName("is_potentially_hazardous_asteroid") val isPotentiallyHazardous: Boolean = false

    @SerialName("close_approach_data") val closeApproachData: List<CloseApproachData>? = null

    @SerialName("is_sentry_object") val isSentryObject: Boolean = false
}
