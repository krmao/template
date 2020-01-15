package com.smart.library.util

import android.text.TextUtils
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.declaredFunctions

@Suppress("unused")
object STReflectUtil {
    private const val TAG = "[reflect]"

    /**
     * 调用私有方法
     */
    @JvmStatic
    @JvmOverloads
    fun invokeDeclaredMethod(instance: Any?, functionName: String, parameterTypes: Array<Class<out Any>> = arrayOf(), params: Array<Any?> = arrayOf()): Any? {
        var result: Any? = null
        try {
            val clazz: Class<Any>? = instance?.javaClass
            val method: Method? = clazz?.getDeclaredMethod(functionName, *parameterTypes)
            method?.isAccessible = true
            STLogUtil.w(TAG, "invoke start ${method?.toGenericString()}")
            result = method?.invoke(instance, *params)
            STLogUtil.w(TAG, "invoke end $result")
        } catch (e: InstantiationException) {
            STLogUtil.e(TAG, "invoke failure", e)
        } catch (e: IllegalAccessException) {
            STLogUtil.e(TAG, "invoke failure", e)
        } catch (e: IllegalArgumentException) {
            STLogUtil.e(TAG, "invoke failure", e)
        } catch (e: InvocationTargetException) {
            STLogUtil.e(TAG, "invoke failure", e)
        } catch (e: NoSuchMethodException) {
            STLogUtil.e(TAG, "invoke failure", e)
        } catch (e: SecurityException) {
            STLogUtil.e(TAG, "invoke failure", e)
        } catch (e: ClassNotFoundException) {
            STLogUtil.e(TAG, "invoke failure", e)
        }
        return result
    }

    /**
     * 根据方法的名字调用方法，适合 object 定义的单例静态方法
     */
    @Throws(RuntimeException::class, IllegalAccessException::class, IllegalArgumentException::class, InvocationTargetException::class, NullPointerException::class, ExceptionInInitializerError::class)
    fun invokeKotlinCompanionObjectDeclaredFunctions(clazz: KClass<*>?, methodName: String?, vararg params: Any?): Any? {
        if (clazz == null || TextUtils.isEmpty(methodName)) throw RuntimeException("$TAG clazz:$clazz or methodName:$methodName is null")
        val methods = clazz.java.kotlin.companionObject?.declaredFunctions?.filter { it.name == methodName && it.parameters.size - 1 == params.size }
        if (methods?.size ?: 0 <= 0) throw RuntimeException("[callNativeMethod] the invoked method dose not exist :$methodName")
        return methods!![0].call(clazz.companionObjectInstance, *params)
    }

    @JvmStatic
    fun setFieldValue(obj: Any?, fieldName: String?, value: Any?) {
        STLogUtil.e(TAG, "do set ${obj?.javaClass?.simpleName}.$fieldName = $value start ...")
        try {
            val filed = obj?.javaClass?.getDeclaredField(fieldName ?: "")
            filed?.isAccessible = true
            filed?.set(obj, value)
            STLogUtil.e(TAG, "do set ${obj?.javaClass?.simpleName}.$fieldName = $value success !!!")
        } catch (e: NoSuchFieldException) {
            STLogUtil.e(TAG, "set failure with NoSuchFieldException!", e)
        } catch (e: NullPointerException) {
            STLogUtil.e(TAG, "set failure with NullPointerException !", e)
        } catch (e: SecurityException) {
            STLogUtil.e(TAG, "set failure with SecurityException !", e)
        } catch (e: IllegalAccessException) {
            STLogUtil.e(TAG, "set failure with IllegalAccessException !", e)
        } catch (e: IllegalArgumentException) {
            STLogUtil.e(TAG, "set failure with IllegalArgumentException !", e)
        } catch (e: ExceptionInInitializerError) {
            STLogUtil.e(TAG, "set failure with ExceptionInInitializerError !", e)
        } catch (e: Exception) {
            STLogUtil.e(TAG, "set failure with Exception !", e)
        }
    }

    @JvmStatic
    fun getFieldValue(obj: Any?, fieldName: String?): Any? {
        STLogUtil.e(TAG, "do get ${obj?.javaClass?.simpleName}.$fieldName start ...")
        try {
            val filed = obj?.javaClass?.getDeclaredField(fieldName ?: "")
            filed?.isAccessible = true
            val value = filed?.get(obj)
            STLogUtil.e(TAG, "do get ${obj?.javaClass?.simpleName}.$fieldName = $value success !!!")
            return value
        } catch (e: NoSuchFieldException) {
            STLogUtil.e(TAG, "get failure with NoSuchFieldException!", e)
        } catch (e: NullPointerException) {
            STLogUtil.e(TAG, "get failure with NullPointerException !", e)
        } catch (e: SecurityException) {
            STLogUtil.e(TAG, "get failure with SecurityException !", e)
        } catch (e: IllegalAccessException) {
            STLogUtil.e(TAG, "get failure with IllegalAccessException !", e)
        } catch (e: IllegalArgumentException) {
            STLogUtil.e(TAG, "get failure with IllegalArgumentException !", e)
        } catch (e: ExceptionInInitializerError) {
            STLogUtil.e(TAG, "get failure with ExceptionInInitializerError !", e)
        } catch (e: Exception) {
            STLogUtil.e(TAG, "get failure with Exception !", e)
        }
        return null
    }
}
