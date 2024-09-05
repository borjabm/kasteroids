package com.bbm.com.harper.asteroids.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class NearEarthObjectIds {
    @JsonProperty("id")
    val id: String? = null

    @JsonProperty("name")
    val name: String? = null
}