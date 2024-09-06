package com.harper.asteroids.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class DiameterEstimation(
    @SerialName("estimated_diameter_min")
    private val min: Double? = null,

    @SerialName("estimated_diameter_max")
    private val max: Double? = null
)
