package com.smart.library.util.cache

import android.support.annotation.NonNull
import android.text.TextUtils
import com.smart.library.base.CXBaseApplication
import com.smart.library.base.md5
import com.smart.library.util.CXSystemUtil
import java.io.File
import java.util.concurrent.ConcurrentHashMap

/**
 * 管理应用程序全局的 entity cache
 */
@Suppress("MemberVisibilityCanPrivate", "unused", "MemberVisibilityCanBePrivate")
object CXCacheManager {

    @JvmStatic
    val DEFAULT_MODULE: String by lazy { "GLOBAL".md5() }

    interface Callback<in T> {
        fun onSuccess(successObject: T?)

        fun onFailure(failureObject: T?)
    }

    interface Call<Long, in T> {
        fun onSuccess(requestCode: Long, successObject: T?)

        fun onFailure(requestCode: Long, failureObject: T?)
    }

    private val allModuleCacheMap = ConcurrentHashMap<String, ConcurrentHashMap<String, Any>>()

    @JvmStatic
    fun getChildDir(@NonNull parent: File, @NonNull childDirName: String): File = File(parent, childDirName).apply { if (!this.exists()) this.mkdirs() }

    /**
     * @see https://stackoverflow.com/a/40741881/4348530
     */
    @JvmStatic
    fun getFilesDir(): File {
        val file = if (CXSystemUtil.isSdCardExist) CXBaseApplication.INSTANCE.getExternalFilesDir(null) else CXBaseApplication.INSTANCE.filesDir
        return file ?: CXBaseApplication.INSTANCE.filesDir
    }

    @JvmStatic
    fun getFilesHotPatchDir(): File = getChildDir(getFilesDir(), "hot-patch")

    @JvmStatic
    fun getFilesHotPatchChildDir(childDirName: String): File = getChildDir(getFilesHotPatchDir(), childDirName)

    @JvmStatic
    fun getFilesHotPatchAndroidDir(): File = getFilesHotPatchChildDir("android".md5(CXBaseApplication.DEBUG))

    @JvmStatic
    fun getFilesHotPatchHybirdDir(): File = getFilesHotPatchChildDir("hybird".md5(CXBaseApplication.DEBUG))

    @JvmStatic
    fun getFilesHotPatchReactNativeDir(): File = getFilesHotPatchChildDir("react-native".md5(CXBaseApplication.DEBUG))

    /**
     * @see https://stackoverflow.com/a/40741881/4348530
     */
    @JvmStatic
    fun getCacheDir(): File {
        val file = if (CXSystemUtil.isSdCardExist) CXBaseApplication.INSTANCE.externalCacheDir else CXBaseApplication.INSTANCE.cacheDir
        return file ?: CXBaseApplication.INSTANCE.cacheDir
    }

    @JvmStatic
    fun getCacheChildDir(childDir: String): File = getChildDir(getCacheDir(), childDir)

    @JvmStatic
    fun getCacheCrashDir(): File = getCacheChildDir("crash")

    @JvmStatic
    fun getCacheMediaDir(): File = getCacheChildDir("media".md5(CXBaseApplication.DEBUG))

    @JvmStatic
    fun getCacheMediaChildDir(childDirName: String): File = getChildDir(getCacheMediaDir(), childDirName)

    @JvmStatic
    fun getCacheMusicDir(): File = getChildDir(getCacheMediaDir(), "music".md5(CXBaseApplication.DEBUG))

    @JvmStatic
    fun getCacheVideoDir(): File = getChildDir(getCacheMediaDir(), "video".md5(CXBaseApplication.DEBUG))

    @JvmStatic
    fun getCacheImageDir(): File = getChildDir(getCacheMediaDir(), "image".md5(CXBaseApplication.DEBUG))

    @JvmStatic
    fun getCacheImageChildDir(childDirName: String): File = getChildDir(getCacheImageDir(), childDirName)

    @JvmStatic
    fun put(module: String, key: String, value: Any) {
        if (!TextUtils.isEmpty(module) && !TextUtils.isEmpty(key)) {
            val subModuleCacheMap: ConcurrentHashMap<String, Any> = allModuleCacheMap[module]
                    ?: ConcurrentHashMap()
            subModuleCacheMap[key] = value
            allModuleCacheMap[module] = subModuleCacheMap
        }
    }

    @JvmOverloads
    @JvmStatic
    fun <T> get(module: String, key: String, default: T? = null): T? {
        if (!TextUtils.isEmpty(module) && !TextUtils.isEmpty(key)) {
            val subModuleCacheMap: ConcurrentHashMap<String, Any>? = allModuleCacheMap[module]
            if (subModuleCacheMap != null) {
                try {
                    @Suppress("UNCHECKED_CAST")
                    return subModuleCacheMap[key] as T
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        return default
    }

    @JvmStatic
    fun remove(module: String) {
        if (!TextUtils.isEmpty(module)) allModuleCacheMap.remove(module)
    }

    @JvmStatic
    fun remove(module: String, key: String) {
        if (!TextUtils.isEmpty(module) && !TextUtils.isEmpty(key)) {
            val subModuleCacheMap: ConcurrentHashMap<String, Any> = allModuleCacheMap[module]
                    ?: ConcurrentHashMap()
            subModuleCacheMap.remove(key)
            allModuleCacheMap[module] = subModuleCacheMap
        }
    }
}
