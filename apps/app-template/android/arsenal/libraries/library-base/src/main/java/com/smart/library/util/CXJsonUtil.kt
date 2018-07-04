package com.smart.library.util

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import org.json.JSONObject

@Suppress("unused")
object CXJsonUtil {
    private val GSON: Gson = Gson()

    @JvmStatic
    fun <T> fromJson(jsonString: String?, classOfT: Class<T>): T? {
        var entity: T? = null
        if (jsonString?.isNotBlank() == true) {
            try {
                entity = GSON.fromJson(jsonString, classOfT)
            } catch (e: Exception) {
                CXLogUtil.e("[JSON]", "fromJson failure", e)
                entity = null
            }
        }
        return entity
    }

    @JvmStatic
    fun toMapOrNull(jsonString: String?): HashMap<String, Any>? {
        if (jsonString.isNullOrBlank()) {
            return null
        } else {
            try {
                return GSON.fromJson(jsonString, object : TypeToken<HashMap<String, Any>>() {}.type)
            } catch (e: Exception) {
                CXLogUtil.e("[JSON]", "toMapOrNull failure", e)
                return null
            }
        }
    }

    @JvmStatic
    fun toJSONObjectOrNull(jsonString: String?): JSONObject? {
        if (jsonString.isNullOrBlank()) {
            return null
        } else {
            try {
                return JSONObject(jsonString)
            } catch (e: Exception) {
                CXLogUtil.e("[JSON]", "toJSONObjectOrNull failure", e)
                return null
            }
        }
    }

    @JvmStatic
    fun toJsonElementOrNull(value: Any?): JsonElement? {
        if (value == null) {
            return null
        } else {
            try {
                return JsonParser().parse(toJson(value)).asJsonObject
            } catch (e: Exception) {
                CXLogUtil.e("[JSON]", "toJsonElementOrNull failure", e)
                return null
            }
        }
    }

    @JvmStatic
    fun toJsonObjectOrNull(jsonString: String?): JsonObject? {
        if (jsonString.isNullOrBlank()) {
            return null
        } else {
            try {
                return JsonParser().parse(jsonString).asJsonObject
            } catch (e: Exception) {
                CXLogUtil.e("[JSON]", "toJsonObjectOrNull failure", e)
                return null
            }
        }
    }

    @JvmStatic
    fun toJsonArrayOrNull(jsonString: String?): JsonArray? {
        if (jsonString.isNullOrBlank()) {
            return null
        } else {
            try {
                return JsonParser().parse(jsonString).asJsonArray
            } catch (e: Exception) {
                CXLogUtil.e("[JSON]", "toJsonArrayOrNull failure", e)
                return null
            }
        }
    }

    @JvmStatic
    fun toJSONArrayOrNull(jsonString: String?): JSONArray? {
        if (jsonString.isNullOrBlank()) {
            return null
        } else {
            try {
                return JSONArray(jsonString)
            } catch (e: Exception) {
                CXLogUtil.e("[JSON]", "toJSONArrayOrNull failure", e)
                return null
            }
        }
    }

    /**
     * val allConfigList = HKJsonUtil.fromJson(allConfigJsonString, Array<HKHybirdModuleConfigModel>::class.java) ?: mutableListOf()
     */
    @JvmStatic
    fun <T> fromJson(jsonString: String, clazz: Class<Array<T>>): MutableList<T>? {
        return GSON.fromJson(jsonString, clazz).toMutableList()
    }

    @JvmStatic
    fun toJson(any: Any?): String {
        return if (any != null) (any as? String ?: GSON.toJson(any)) else ""
    }
}