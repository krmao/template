package com.smart.springcloud.appb.base.util

import java.lang.reflect.Field
import java.lang.reflect.InvocationTargetException
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.declaredFunctions

@Suppress("unused")
object CXReflectUtil {
    private val TAG = "[reflect]"

    /**
     * 根据方法的名字调用方法，适合 object 定义的单例静态方法
     */
    @Throws(RuntimeException::class, IllegalAccessException::class, IllegalArgumentException::class, InvocationTargetException::class, NullPointerException::class, ExceptionInInitializerError::class)
    fun invoke(clazz: KClass<*>?, methodName: String?, vararg params: Any?): Any? {
        if (clazz == null || methodName.isNullOrBlank()) throw RuntimeException("${TAG} clazz:$clazz or methodName:$methodName is null")
        val methods = clazz.java.kotlin.companionObject?.declaredFunctions?.filter { it.name == methodName && it.parameters.size - 1 == params.size }
        if (methods?.size ?: 0 <= 0) throw RuntimeException("[callNativeMethod] the invoked method dose not exist :$methodName")
        return methods!![0].call(clazz.companionObjectInstance, *params)
    }

    fun getFields(objectClass: Class<*>?): MutableList<Field> {
        var fieldList: MutableList<Field>? = null
        try {
            fieldList = objectClass?.declaredFields?.toMutableList()
            objectClass?.superclass?.declaredFields.let { if (it != null) fieldList?.addAll(it) }
        } catch (ignore: Exception) {
        }
        return fieldList ?: arrayListOf()
    }

    fun getValue(field: Field?, fromObject: Any?): Any? = try {
        field?.isAccessible = true
        field?.get(fromObject)
    } catch (ignore: Exception) {
        null
    }
}
