package com.harper.asteroids.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NearEarthObjectIds(
    @SerialName("id") val id: String? = null,
    @SerialName("name") val name: String? = null
)
