package com.harper.asteroids.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Response for a feed query of Neos. */
@Serializable
data class Feed(
    @SerialName("element_count")
    val elementCount: Int = 0,
    @SerialName("near_earth_objects")
    val nearEarthObjects: Map<String, List<NearEarthObjectIds>> = mapOf()
) {
    val allObjectIds: List<Int> = nearEarthObjects.flatMap { entry ->
        entry.value.map { neo -> neo.id?.toIntOrNull() }
    }.filterNotNull()
}
