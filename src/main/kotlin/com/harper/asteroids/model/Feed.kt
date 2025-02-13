package com.harper.asteroids.model

import java.util.stream.Collectors
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

/** Response for a feed query of Neos. */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
class Feed {
    @SerialName("element_count") val elementCount: Int = 0

    @SerialName("near_earth_objects")
    private val nearEarthObjects: Map<String, List<NearEarthObjectIds?>>? = null

    fun getNearEarthObjects(): Map<String, List<NearEarthObjectIds?>>? {
        return nearEarthObjects
    }

    val allObjectIds: MutableList<Any>?
        get() =
            nearEarthObjects!!
                .values
                .stream()
                .flatMap<NearEarthObjectIds?> { l: List<NearEarthObjectIds?> -> l.stream() }
                .map { n: NearEarthObjectIds? -> n!!.id }
                .collect(Collectors.toList())
}
