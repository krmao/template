package com.smart.library.util

import android.text.TextUtils
import java.lang.reflect.InvocationTargetException
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.declaredFunctions

@Suppress("unused")
object HKReflectUtil {
    private val TAG = "[reflect]"

    /**
     * 根据方法的名字调用方法，适合 object 定义的单例静态方法
     */
    @Throws(RuntimeException::class, IllegalAccessException::class, IllegalArgumentException::class, InvocationTargetException::class, NullPointerException::class, ExceptionInInitializerError::class)
    fun invoke(clazz: KClass<*>?, methodName: String?, vararg params: Any?): Any? {
        if (clazz == null || TextUtils.isEmpty(methodName)) throw RuntimeException("$TAG clazz:$clazz or methodName:$methodName is null")
        val methods = clazz.java.kotlin.companionObject?.declaredFunctions?.filter { it.name == methodName && it.parameters.size - 1 == params.size }
        if (methods?.size ?: 0 <= 0) throw RuntimeException("[callNativeMethod] the invoked method dose not exist :$methodName")
        return methods!![0].call(clazz.companionObjectInstance, *params)
    }

    @JvmStatic
    fun set(obj: Any?, fieldName: String?, value: Any?) {
        HKLogUtil.e(TAG, "do set ${obj?.javaClass?.simpleName}.$fieldName = $value start ...")
        try {
            val filed = obj?.javaClass?.getDeclaredField(fieldName)
            filed?.isAccessible = true
            filed?.set(obj, value)
            HKLogUtil.e(TAG, "do set ${obj?.javaClass?.simpleName}.$fieldName = $value success !!!")
        } catch (e: NoSuchFieldException) {
            HKLogUtil.e(TAG, "set failure with NoSuchFieldException!", e)
        } catch (e: NullPointerException) {
            HKLogUtil.e(TAG, "set failure with NullPointerException !", e)
        } catch (e: SecurityException) {
            HKLogUtil.e(TAG, "set failure with SecurityException !", e)
        } catch (e: IllegalAccessException) {
            HKLogUtil.e(TAG, "set failure with IllegalAccessException !", e)
        } catch (e: IllegalArgumentException) {
            HKLogUtil.e(TAG, "set failure with IllegalArgumentException !", e)
        } catch (e: ExceptionInInitializerError) {
            HKLogUtil.e(TAG, "set failure with ExceptionInInitializerError !", e)
        } catch (e: Exception) {
            HKLogUtil.e(TAG, "set failure with Exception !", e)
        }
    }

    @JvmStatic
    fun get(obj: Any?, fieldName: String?): Any? {
        HKLogUtil.e(TAG, "do get ${obj?.javaClass?.simpleName}.$fieldName start ...")
        try {
            val filed = obj?.javaClass?.getDeclaredField(fieldName)
            filed?.isAccessible = true
            val value = filed?.get(obj)
            HKLogUtil.e(TAG, "do get ${obj?.javaClass?.simpleName}.$fieldName = $value success !!!")
            return value
        } catch (e: NoSuchFieldException) {
            HKLogUtil.e(TAG, "get failure with NoSuchFieldException!", e)
        } catch (e: NullPointerException) {
            HKLogUtil.e(TAG, "get failure with NullPointerException !", e)
        } catch (e: SecurityException) {
            HKLogUtil.e(TAG, "get failure with SecurityException !", e)
        } catch (e: IllegalAccessException) {
            HKLogUtil.e(TAG, "get failure with IllegalAccessException !", e)
        } catch (e: IllegalArgumentException) {
            HKLogUtil.e(TAG, "get failure with IllegalArgumentException !", e)
        } catch (e: ExceptionInInitializerError) {
            HKLogUtil.e(TAG, "get failure with ExceptionInInitializerError !", e)
        } catch (e: Exception) {
            HKLogUtil.e(TAG, "get failure with Exception !", e)
        }
        return null
    }
}
