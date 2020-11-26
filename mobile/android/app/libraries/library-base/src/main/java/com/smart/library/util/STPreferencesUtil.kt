package com.smart.library.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.smart.library.STInitializer
import java.util.*

@Suppress("unused")
/**
 * <pre>
 * author : smart
 * e-mail : smart@smart.com
 * desc   : SharedPreferences 工具，可以方便的保存于读取数组
 * </pre>
 *
 * for jackson
 * import com.fasterxml.jackson.databind.ObjectMapper
 * import com.fasterxml.jackson.databind.type.TypeFactory
 * import com.fasterxml.jackson.module.kotlin.registerKotlinModule
 */
object STPreferencesUtil {

    private val sharedPreferences: SharedPreferences? by lazy { STInitializer.application()?.getSharedPreferences(STInitializer.config?.configName?.appSPName ?: "com.codesdancing.shared_preferences", Context.MODE_PRIVATE) }

    //private var objectMapper: ObjectMapper = ObjectMapper().registerKotlinModule()
    private var gson: Gson = Gson()

    fun getBoolean(name: String, bDefault: Boolean): Boolean? = sharedPreferences?.getBoolean(name, bDefault)

    fun putBoolean(name: String, value: Boolean): Boolean? {
        val editor = sharedPreferences?.edit()
        editor?.putBoolean(name, value)
        return editor?.commit()
    }

    fun putString(key: String, value: String?): Boolean? {
        val editor = sharedPreferences?.edit()
        editor?.putString(key, value)
        return editor?.commit()
    }

    fun putJsonString(key: String, value: Any): Boolean? {
        val editor = sharedPreferences?.edit()
        editor?.putString(key, gson.toJson(value))
        return editor?.commit()
    }

    fun getString(key: String): String? = sharedPreferences?.getString(key, "")

    fun getString(key: String, default: String = ""): String = sharedPreferences?.getString(key, default) ?: default

    fun putInt(key: String, value: Int): Boolean? {
        val editor = sharedPreferences?.edit()
        editor?.putInt(key, value)
        return editor?.commit()
    }

    @SuppressLint("ApplySharedPref")
    fun putEntity(key: String, value: Any?): Boolean? = try {
        val editor = sharedPreferences?.edit()
        editor?.putString(key, gson.toJson(value))
        //editor?.putString(key, objectMapper.writeValueAsString(value))
        editor?.commit()
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }

    fun <T> getEntity(key: String, classOfT: Class<T>): T? {
        var entity: T? = null
        try {
            entity = gson.fromJson(sharedPreferences?.getString(key, null), classOfT)
            //entity = objectMapper.readValue(mSharedPreferences?.getString(key, null), classOfT)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return entity
    }

    @SuppressLint("ApplySharedPref")
    fun putList(key: String, list: List<*>): Boolean? = try {
        val editor = sharedPreferences?.edit()
        editor?.putString(key, gson.toJson(list))
        //editor?.putString(key, objectMapper.writeValueAsString(list))
        editor?.commit()
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }

    @SuppressLint("ApplySharedPref")
    fun putMap(key: String, map: Map<String, Any>): Boolean {
        var success = false
        try {
            val editor = sharedPreferences?.edit()
            editor?.putString(key, gson.toJson(map))
            editor?.commit()
            success = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return success
    }

    fun <T : Any> getMap(key: String, classOfT: Class<T>?): MutableMap<String, T> {
        val map: MutableMap<String, T> = mutableMapOf()
        val mapJsonString = sharedPreferences?.getString(key, null)
        if (!mapJsonString.isNullOrBlank()) {
            try {
                gson.fromJson<MutableMap<String, JsonElement>>(mapJsonString, object : TypeToken<MutableMap<String, JsonElement>>() {}.type)?.map {
                    map[it.key] = gson.fromJson(it.value, classOfT)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return map
    }

    fun <T : Any> getList(key: String, classOfT: Class<T>?): MutableList<T> {
        val list: ArrayList<T> = ArrayList()
        try {
            gson.fromJson<ArrayList<JsonElement>>(sharedPreferences?.getString(key, null), object : TypeToken<ArrayList<JsonElement>>() {}.type)?.mapNotNullTo(list) { gson.fromJson(it, classOfT) }
        } catch (jsonParseException: JsonParseException) {
            jsonParseException.printStackTrace()
        } catch (jsonSyntaxException: JsonSyntaxException) {
            jsonSyntaxException.printStackTrace()
        }
        return list

        /* for jackson
        var list: MutableList<T> = ArrayList()
        try {
            list = objectMapper.readValue(mSharedPreferences?.getString(key, null), TypeFactory.defaultInstance().constructCollectionType(ArrayList::class.java, classOfT))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list*/
    }

    fun <T : Any> getStack(key: String, classOfT: Class<T>?): Stack<T> {
        val stack = Stack<T>()
        try {
            gson.fromJson<Stack<JsonElement>>(sharedPreferences?.getString(key, null), object : TypeToken<Stack<JsonElement>>() {}.type)?.mapNotNullTo(stack) { gson.fromJson(it, classOfT) }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
        return stack
    }

    fun getInt(key: String, iDefault: Int): Int? = sharedPreferences?.getInt(key, iDefault)

    fun putLong(key: String, value: Long): Boolean? {
        val editor = sharedPreferences?.edit()
        editor?.putLong(key, value)
        return editor?.commit()
    }

    fun getLong(key: String, iDefault: Long): Long? = sharedPreferences?.getLong(key, iDefault)
}
