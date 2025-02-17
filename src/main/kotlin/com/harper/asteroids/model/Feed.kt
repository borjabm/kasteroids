package com.harper.asteroids.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.stream.Collectors

/** Response for a feed query of Neos. */
@Serializable
class Feed {
    @SerialName("near_earth_objects")
    private val nearEarthObjects: Map<String, List<NearEarthObjectIds?>>? = null

    val allObjectIds: MutableList<Any>?
        get() =
            nearEarthObjects!!
                .values
                .stream()
                .flatMap<NearEarthObjectIds?> { l: List<NearEarthObjectIds?> -> l.stream() }
                .map { n: NearEarthObjectIds? -> n!!.id }
                .collect(Collectors.toList())
}
