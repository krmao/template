package com.smart.library.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.smart.library.base.HKBaseApplication
import com.smart.library.base.HKConfig
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

object HKPreferencesUtil {

    private var mSharedPreferences: SharedPreferences = HKBaseApplication.INSTANCE.getSharedPreferences(HKConfig.NAME_SHARED_PREFERENCES, Context.MODE_PRIVATE)
    //private var objectMapper: ObjectMapper = ObjectMapper().registerKotlinModule()
    private var mGson: Gson = Gson()

    fun getBoolean(name: String, bDefault: Boolean): Boolean =
        mSharedPreferences.getBoolean(name, bDefault)

    fun putBoolean(name: String, value: Boolean): Boolean {
        val editor = mSharedPreferences.edit()
        editor.putBoolean(name, value)
        return editor.commit()
    }

    fun putString(key: String, value: String): Boolean {
        val editor = mSharedPreferences.edit()
        editor.putString(key, value)
        return editor.commit()
    }

    fun getString(key: String): String = mSharedPreferences.getString(key, "")

    fun getString(key: String, default: String? = null): String =
        mSharedPreferences.getString(key, default)

    fun putInt(key: String, value: Int): Boolean {
        val editor = mSharedPreferences.edit()
        editor.putInt(key, value)
        return editor.commit()
    }

    @SuppressLint("ApplySharedPref")
    fun putEntity(key: String, value: Any?): Boolean = try {
        val editor = mSharedPreferences.edit()
        editor.putString(key, mGson.toJson(value))
        //editor.putString(key, objectMapper.writeValueAsString(value))
        editor.commit()
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }

    fun <T> getEntity(key: String, classOfT: Class<T>): T? {
        var entity: T? = null
        try {
            entity = mGson.fromJson(mSharedPreferences.getString(key, null), classOfT)
            //entity = objectMapper.readValue(mSharedPreferences.getString(key, null), classOfT)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return entity
    }

    fun putList(key: String, list: List<*>): Boolean = try {
        val editor = mSharedPreferences.edit()
        editor.putString(key, mGson.toJson(list))
        //editor.putString(key, objectMapper.writeValueAsString(list))
        editor.commit()
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }

    fun <T : Any> getList(key: String, classOfT: Class<T>?): MutableList<T> {
        val list = ArrayList<T>()
        try {
            mGson.fromJson<ArrayList<JsonElement>>(mSharedPreferences.getString(key, null), object : TypeToken<ArrayList<JsonElement>>() {}.type)?.mapNotNullTo(list) { mGson.fromJson(it, classOfT) }
        } catch (jsonParseException: JsonParseException) {
            jsonParseException.printStackTrace()
        } catch (jsonSyntaxException: JsonSyntaxException) {
            jsonSyntaxException.printStackTrace()
        }
        return list

        /* for jackson
        var list: MutableList<T> = ArrayList()
        try {
            list = objectMapper.readValue(mSharedPreferences.getString(key, null), TypeFactory.defaultInstance().constructCollectionType(ArrayList::class.java, classOfT))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list*/
    }

    fun getInt(key: String, iDefault: Int): Int = mSharedPreferences.getInt(key, iDefault)

    fun putLong(key: String, value: Long): Boolean {
        val editor = mSharedPreferences.edit()
        editor.putLong(key, value)
        return editor.commit()
    }

    fun getLong(key: String, iDefault: Long): Long = mSharedPreferences.getLong(key, iDefault)
}
