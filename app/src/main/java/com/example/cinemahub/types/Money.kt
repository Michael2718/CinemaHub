package com.example.cinemahub.types

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.postgresql.util.PGmoney

object PGMoneySerializer : KSerializer<PGmoney> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("PGmoney") {
        element<String>("money")
    }

    override fun serialize(encoder: Encoder, value: PGmoney) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): PGmoney {
        try {
            val moneyString = decoder.decodeString()
            return PGmoney(moneyString)
        } catch (e: Exception) {
            throw SerializationException("Error parsing money: ${e.message}")
        }
    }
}

//fun parsePGMoney(moneyString: String): PGmoney {
//    val json = Json { ignoreUnknownKeys = true }
//    return json.decodeFromString(PGMoneySerializer, moneyString)
//}
