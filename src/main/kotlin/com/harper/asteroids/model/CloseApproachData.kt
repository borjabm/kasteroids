package com.harper.asteroids.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.text.SimpleDateFormat
import java.util.*

@Serializable
class CloseApproachData {
    @SerialName("close_approach_date")
    @Serializable(with = SimpleDateSerializer::class)
    val closeApproachDate: Date? = null

    @SerialName("close_approach_date_full")
    @Serializable(with = DateSerializer::class)
    val closeApproachDateTime: Date? = null

    @SerialName("epoch_date_close_approach")
    val closeApproachEpochDate: Long = 0

    @SerialName("relative_velocity")
    val relativeVelocity: Velocities? = null

    @SerialName("miss_distance")
    val missDistance: Distances? = null

    @SerialName("orbiting_body")
    val orbitingBody: String? = null
}

object DateSerializer : KSerializer<Date> {
    private val formatter = SimpleDateFormat("yyyy-MMM-dd hh:mm", Locale.ENGLISH)

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Date) {
        encoder.encodeString(formatter.format(value))
    }

    override fun deserialize(decoder: Decoder): Date {
        val string = decoder.decodeString()
        return formatter.parse(string)
    }
}

object SimpleDateSerializer : KSerializer<Date> {
    private val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Date) {
        encoder.encodeString(formatter.format(value))
    }

    override fun deserialize(decoder: Decoder): Date {
        val string = decoder.decodeString()
        return formatter.parse(string)
    }
}
