package com.harper.asteroids.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

@Serializable
class CloseApproachData {

    @SerialName("close_approach_date_full")
    @Serializable(with = InstantSerializer::class)
    val closeApproachDateTime: Instant? = null

    @SerialName("miss_distance") val missDistance: Distances? = null
}

object InstantSerializer : KSerializer<Instant> {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MMM-dd HH:mm", Locale.ENGLISH)

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Instant", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Instant) {
        val formatted = formatter.format(value.atZone(ZoneOffset.UTC).toLocalDateTime())
        encoder.encodeString(formatted)
    }

    override fun deserialize(decoder: Decoder): Instant {
        val string = decoder.decodeString()
        val localDateTime = LocalDateTime.parse(string, formatter)
        return localDateTime.atZone(ZoneOffset.UTC).toInstant()
    }
}
