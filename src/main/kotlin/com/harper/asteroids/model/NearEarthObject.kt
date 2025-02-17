package com.harper.asteroids.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Definition for Neo - Near Earth Object
 */
@Serializable
class NearEarthObject {

    @SerialName("name") val name: String? = null

    @SerialName("is_potentially_hazardous_asteroid") val isPotentiallyHazardous: Boolean = false

    @SerialName("close_approach_data") val closeApproachData: List<CloseApproachData>? = null
}
