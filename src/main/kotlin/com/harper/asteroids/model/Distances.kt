package com.harper.asteroids.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
class Distances(
    @SerialName("astronomical")
    val astronomical: Double? = null,

    @SerialName("lunar")
    val lunar: Double? = null,

    @SerialName("kilometers")
    val kilometers: Double? = null,

    @SerialName("miles")
    val miles: Double? = null
) : Comparable<Distances> {
    override fun compareTo(other: Distances): Int {
        return kilometers!!.compareTo(other.kilometers!!) //TODO: Think what to do if kilometers is missing (instead of NPE)
    }
}