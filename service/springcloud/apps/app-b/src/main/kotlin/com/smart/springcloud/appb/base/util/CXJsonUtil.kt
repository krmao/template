package com.smart.springcloud.appb.base.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue

@Suppress("unused")
object CXJsonUtil {

    @JvmStatic
    val JSON: ObjectMapper = ObjectMapper().registerModule(KotlinModule()).disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)

    @JvmStatic
    fun toJSON(value: Any): String = JSON.writeValueAsString(value)

    @JvmStatic
    inline fun <reified T : Any> parse(content: String): T = JSON.readValue(content)
}
