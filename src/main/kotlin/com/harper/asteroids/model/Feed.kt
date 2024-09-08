package com.harper.asteroids.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Response for a feed query of Neos.
 */
@Serializable
class Feed(
    @SerialName("element_count")
    val elementCount: Int = 0,

    @SerialName("near_earth_objects")
    private val nearEarthObjects: Map<String, List<NearEarthObjectIds?>> = mapOf(),
) {
    fun getNearEarthObjects(): Map<String, List<NearEarthObjectIds?>> {
        return nearEarthObjects
    }

    val allObjectIds: List<String>
        get() = nearEarthObjects.values
            .flatten()
            .mapNotNull { it?.id }
            .toList()
}
