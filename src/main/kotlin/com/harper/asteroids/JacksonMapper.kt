package com.harper.asteroids

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper

object JacksonMapper {

    val instance: ObjectMapper = ObjectMapper()

    init {
        instance.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }
}