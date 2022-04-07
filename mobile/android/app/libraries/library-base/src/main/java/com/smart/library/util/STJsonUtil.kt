package com.smart.library.util

import androidx.annotation.Keep
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

//@Keep
@Suppress("unused", "MemberVisibilityCanBePrivate", "LiftReturnOrAssignment")
object STJsonUtil {
    private val gson: Gson by lazy { Gson() }

    @JvmStatic
    fun <T> fromJson(jsonString: String?, classOfT: Class<T>): T? {
        var entity: T? = null
        if (jsonString?.isNotBlank() == true) {
            entity = try {
                gson.fromJson(jsonString, classOfT)
            } catch (e: Exception) {
                STLogUtil.e("[JSON]", "fromJson failure", e)
                null
            }
        }
        return entity
    }

    fun toMap(jsonObject: JSONObject?): Map<String, Any> {
        val map: MutableMap<String, Any> = HashMap()
        if (jsonObject == null) {
            return map
        }
        val keys = jsonObject.keys()
        while (keys.hasNext()) {
            val key = keys.next()
            var value = jsonObject[key]
            if (value is JSONArray) {
                value = toList(value)
            } else if (value is JSONObject) {
                value = toMap(value)
            }
            map[key] = value
        }
        return map
    }

    fun toList(array: JSONArray?): List<Any> {
        val list: MutableList<Any> = ArrayList()
        if (array == null) {
            return list
        }
        for (i in 0 until array.length()) {
            var value = array[i]
            if (value is JSONArray) {
                value = toList(value)
            } else if (value is JSONObject) {
                value = toMap(value)
            }
            list.add(value)
        }
        return list
    }

    @JvmStatic
    fun toMapOrNull(jsonString: String?): HashMap<String, Any>? {
        if (jsonString.isNullOrBlank()) {
            return null
        } else {
            try {
                return gson.fromJson(jsonString, object : TypeToken<HashMap<String, Any>>() {}.type)
            } catch (e: Exception) {
                STLogUtil.e("[JSON]", "toMapOrNull failure", e)
                return null
            }
        }
    }

    @JvmStatic
    fun toJSONObjectOrNull(jsonString: String?): JSONObject? {
        return if (jsonString.isNullOrBlank()) {
            null
        } else {
            try {
                JSONObject(jsonString)
            } catch (e: Exception) {
                STLogUtil.e("[JSON]", "toJSONObjectOrNull failure", e)
                null
            }
        }
    }

    @JvmStatic
    fun toJsonElementOrNull(value: Any?): JsonElement? {
        return if (value == null) {
            null
        } else {
            try {
                JsonParser().parse(toJson(value)).asJsonObject
            } catch (e: Exception) {
                STLogUtil.e("[JSON]", "toJsonElementOrNull failure", e)
                null
            }
        }
    }

    @JvmStatic
    fun toJsonObjectOrNull(jsonString: String?): JsonObject? {
        return if (jsonString.isNullOrBlank()) {
            null
        } else {
            try {
                JsonParser().parse(jsonString).asJsonObject
            } catch (e: Exception) {
                STLogUtil.e("[JSON]", "toJsonObjectOrNull failure", e)
                null
            }
        }
    }

    @JvmStatic
    fun toJsonArrayOrNull(jsonString: String?): JsonArray? {
        return if (jsonString.isNullOrBlank()) {
            null
        } else {
            try {
                JsonParser().parse(jsonString).asJsonArray
            } catch (e: Exception) {
                STLogUtil.e("[JSON]", "toJsonArrayOrNull failure", e)
                null
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
                STLogUtil.e("[JSON]", "toJSONArrayOrNull failure", e)
                return null
            }
        }
    }

    /**
     * val allConfigList = HKJsonUtil.fromJson(allConfigJsonString, Array<HKHybirdModuleConfigModel>::class.java) ?: mutableListOf()
     */
    @JvmStatic
    fun <T> fromJson(jsonString: String, clazz: Class<Array<T>>): MutableList<T>? {
        return gson.fromJson(jsonString, clazz).toMutableList()
    }

    @JvmStatic
    fun toJson(any: Any?): String {
        return if (any != null) (any as? String ?: gson.toJson(any)) else ""
    }
}