package com.harper.asteroids.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Definition for Neo - Near Earth Object
 */

@Serializable
data class NearEarthObject(
    @SerialName("id")
    val id: String?,

    @SerialName("name")
    val name: String?,

    @SerialName("nasa_jpl_url")
    val nplUrl: String?,

    @SerialName("absolute_magnitude_h")
    val absoluteMagnitude: Double,

    @SerialName("is_potentially_hazardous_asteroid")
    val isPotentiallyHazardous: Boolean,

    @SerialName("close_approach_data")
    val closeApproachData: List<CloseApproachData>?,

    @SerialName("is_sentry_object")
    val isSentryObject: Boolean,
) {
    fun getCloseApproachDataWithinNextWeek(): List<CloseApproachData> {
        if (closeApproachData.isNullOrEmpty()) return listOf()
        return closeApproachData.filter { it.isCloseApproachDateWithinNextWeek() }
    }
}
