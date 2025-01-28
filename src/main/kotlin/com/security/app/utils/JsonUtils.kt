package com.security.app.utils

import com.nimbusds.jose.shaded.gson.Gson
import org.springframework.stereotype.Component

@Component
class JsonUtils {
    private final val gson = Gson()

    fun toJson(obj: Any): String {
        return gson.toJson(obj)
    }

    fun <T> fromJson(json: String, clazz: Class<T>): T {
        return gson.fromJson(json, clazz)
    }
}