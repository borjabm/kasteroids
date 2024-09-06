package com.harper.asteroids.model

import kotlinx.serialization.Serializable


@Serializable
class EstimatedDiameter(
    val kilometers: DiameterEstimation? = null,
    val meters: DiameterEstimation? = null,
    val miles: DiameterEstimation? = null,
    val feet: DiameterEstimation? = null
)
