package com.bbm.com.harper.asteroids.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.function.Function
import java.util.stream.Collectors
import java.util.stream.Stream

/**
 * Response for a feed query of Neos.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class Feed() {
    @JsonProperty("element_count")
    val elementCount: Int = 0

    @JsonProperty("near_earth_objects")
    private val nearEarthObjects: Map<String, List<NearEarthObjectIds?>>? = null

    fun getNearEarthObjects(): Map<String, List<NearEarthObjectIds?>>? {
        return nearEarthObjects
    }

    val allObjectIds: MutableList<Any>?
        get() = nearEarthObjects!!.values.stream()
            .flatMap<NearEarthObjectIds?>({ l: List<NearEarthObjectIds?> -> l.stream() })
            .map<Any>(Function<NearEarthObjectIds?, Any> { n: NearEarthObjectIds? -> n!!.id })
            .collect(Collectors.toList<Any>())
}