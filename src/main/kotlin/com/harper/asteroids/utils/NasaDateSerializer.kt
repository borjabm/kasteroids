package com.harper.asteroids.utils

import kotlinx.datetime.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.format.DateTimeFormatter
import java.util.*


object NasaDateSerializer : KSerializer<LocalDate> {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.US)

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(javaClass.simpleName, PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDate) {
        val formatted = value.toJavaLocalDate().format(formatter)
        encoder.encodeString(formatted)
    }

    override fun deserialize(decoder: Decoder): LocalDate {
        val dateString = decoder.decodeString()
        val dateTime = java.time.LocalDate.parse(dateString, formatter)
        return dateTime.toKotlinLocalDate()
    }
}
