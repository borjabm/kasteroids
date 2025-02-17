package com.harper.asteroids.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Distances : Comparable<Distances?> {
    @SerialName("kilometers") val kilometers: Double? = null

    override fun compareTo(other: Distances?): Int {
        return kilometers!!.compareTo(other!!.kilometers!!)
    }
}
