package com.smart.library.util

import com.google.gson.Gson

object CXJsonUtil {
    private val GSON: Gson = Gson()

    @JvmStatic
    fun <T> fromJson(jsonString: String?, classOfT: Class<T>): T? {
        var entity: T? = null
        if (jsonString?.isNotBlank() == true) {
            try {
                entity = GSON.fromJson(jsonString, classOfT)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return entity
    }

    /**
     * val allConfigList = CXJsonUtil.fromJson(allConfigJsonString, Array<CXHybirdModuleConfigModel>::class.java) ?: mutableListOf()
     */
    @JvmStatic
    fun <T> fromJson(jsonString: String, clazz: Class<Array<T>>): MutableList<T>? {
        return GSON.fromJson(jsonString, clazz).toMutableList()
    }

    @JvmStatic
    fun toJson(any: Any?): String {
        return if (any != null) GSON.toJson(any) else ""
    }
}
