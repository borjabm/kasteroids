package com.harper.asteroids.model

import com.fasterxml.jackson.annotation.JsonProperty

class Distances : Comparable<Distances?> {
    @JsonProperty("astronomical")
    val astronomical: Double? = null

    @JsonProperty("lunar")
    val lunar: Double? = null

    @JsonProperty("kilometers")
    val kilometers: Double? = null

    @JsonProperty("miles")
    val miles: Double? = null


    override fun compareTo(other: Distances?): Int {
        return kilometers!!.compareTo(other!!.kilometers!!)
    }
}