package com.smart.library.util.cache

import android.os.Environment
import android.text.TextUtils
import com.smart.library.base.CXBaseApplication
import com.smart.library.util.CXSystemUtil
import java.io.File
import java.util.concurrent.ConcurrentHashMap

/**
 * 管理应用程序全局的 entity cache
 */
@Suppress("MemberVisibilityCanPrivate", "unused")
object CXCacheManager {

    interface Callback<in T> {
        fun onSuccess(successObject: T?)

        fun onFailure(failureObject: T?)
    }

    private val allModuleCacheMap = ConcurrentHashMap<String, ConcurrentHashMap<String, Any>>()

    // 荣耀6 会有很多警告
    fun getPackageDir(): File {
        val cacheDir = if (CXSystemUtil.isSdCardExist) {
            File(Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" + CXBaseApplication.INSTANCE.packageName)
        } else {
            File(CXBaseApplication.INSTANCE.filesDir.absolutePath)
        }
        if (!cacheDir.exists())
            cacheDir.mkdirs()
        return cacheDir
    }

    fun getCacheDir(): File {
        val cacheDir = File(getPackageDir(), "cache")
        if (!cacheDir.exists())
            cacheDir.mkdirs()
        return cacheDir
    }

    fun getChildCacheDir(childDir: String): File {
        if (TextUtils.isEmpty(childDir))
            return getCacheDir()
        val cacheDir = File(getCacheDir(), childDir)
        if (!cacheDir.exists())
            cacheDir.mkdirs()
        return cacheDir
    }

    fun put(module: String, key: String, value: Any) {
        if (TextUtils.isEmpty(module) || TextUtils.isEmpty(key)) {
            return
        }
        val subModuleCacheMap: ConcurrentHashMap<String, Any> = allModuleCacheMap[module] ?: ConcurrentHashMap()
        subModuleCacheMap.put(key, value)
        allModuleCacheMap.put(module, subModuleCacheMap)
    }

    fun <T> get(module: String, key: String, default: T? = null): T? {
        if (TextUtils.isEmpty(module) || TextUtils.isEmpty(key)) {
            return default
        }
        val subModuleCacheMap: ConcurrentHashMap<String, Any>? = allModuleCacheMap[module]
        if (subModuleCacheMap != null) {
            try {
                @Suppress("UNCHECKED_CAST")
                return subModuleCacheMap[key] as T
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return default
    }

    fun remove(module: String) {
        if (TextUtils.isEmpty(module)) {
            return
        }
        allModuleCacheMap.remove(module)
    }

    fun remove(module: String, key: String) {
        if (TextUtils.isEmpty(module) || TextUtils.isEmpty(key)) {
            return
        }
        val subModuleCacheMap: ConcurrentHashMap<String, Any> = allModuleCacheMap[module] ?: ConcurrentHashMap()
        subModuleCacheMap.remove(key)
        allModuleCacheMap.put(module, subModuleCacheMap)
    }
}
