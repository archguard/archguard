package com.thoughtworks.archguard.common

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

object JsonUtils {
    val objectMapper = ObjectMapper()

    fun obj2json(obj: Any): String {
        return objectMapper.writeValueAsString(obj)
    }


    inline fun <reified T> json2obj(jsonString: String): T {
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
        return objectMapper.readValue(jsonString)
    }

}
