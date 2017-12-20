package com.smart.library.util

import com.google.gson.Gson

object HKJsonUtil {
    private val GSON: Gson = Gson()

    fun <T> fromJson(jsonString: String?, classOfT: Class<T>): T? {
        var entity: T? = null
        if (jsonString?.isNotBlank() == true) {
            try {
                entity = GSON.fromJson(jsonString, classOfT)
                //entity = objectMapper.readValue(mSharedPreferences.getString(key, null), classOfT)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return entity
    }
}
