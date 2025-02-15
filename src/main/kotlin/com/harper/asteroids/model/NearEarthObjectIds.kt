package com.harper.asteroids.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class NearEarthObjectIds(
    @SerialName("id") val id: String? = null,
    @SerialName("name") val name: String? = null
)
