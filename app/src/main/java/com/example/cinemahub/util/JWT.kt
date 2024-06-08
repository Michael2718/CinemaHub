package com.example.cinemahub.util

import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
fun extractAudienceFromJwt(jwtToken: String): String? {
    val parts = jwtToken.split(".")
    if (parts.size != 3) {
        return null
    }

    val decodedString = String(Base64.decode(parts[1]))

    val jsonObject = Gson().fromJson(decodedString, JsonObject::class.java)

    return jsonObject["aud"]?.asString
}
