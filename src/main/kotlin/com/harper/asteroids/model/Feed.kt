package com.harper.asteroids.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.function.Function
import java.util.stream.Collectors

/**
 * Response for a feed query of Neos.
 */
@Serializable
class Feed(
    @SerialName("element_count")
    val elementCount: Int = 0,

    @SerialName("near_earth_objects")
    private val nearEarthObjects: Map<String, List<NearEarthObjectIds?>>? = null,
) {
    fun getNearEarthObjects(): Map<String, List<NearEarthObjectIds?>>? {
        return nearEarthObjects
    }

    val allObjectIds: MutableList<Any>?
        get() = nearEarthObjects!!.values.stream()
            .flatMap<NearEarthObjectIds?>({ l: List<NearEarthObjectIds?> -> l.stream() })
            .map<Any>(Function<NearEarthObjectIds?, Any> { n: NearEarthObjectIds? -> n!!.id })
            .collect(Collectors.toList<Any>())
}
