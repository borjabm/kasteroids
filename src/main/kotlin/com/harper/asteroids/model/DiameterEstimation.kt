package com.bbm.com.harper.asteroids.model

import com.fasterxml.jackson.annotation.JsonProperty

class DiameterEstimation {
    @JsonProperty("estimated_diameter_min")
    private val min: Double? = null

    @JsonProperty("estimated_diameter_max")
    private val max: Double? = null
}