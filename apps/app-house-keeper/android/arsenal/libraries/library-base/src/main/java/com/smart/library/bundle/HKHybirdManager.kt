package com.smart.library.bundle

import android.text.TextUtils
import com.smart.library.base.HKBaseApplication
import com.smart.library.util.HKLogUtil
import com.smart.library.util.cache.HKCacheManager
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.ConcurrentHashMap

@Suppress("MemberVisibilityCanPrivate", "unused")
object HKHybirdManager {
    private val TAG = HKHybirdManager::class.java.simpleName

    var EVN = "pre"
        private set
    val ASSETS_DIR_NAME = "hybird"
    val BUNDLE_SUFFIX = ".zip"
    val CONFIG_SUFFIX = ".json"
    val LOCAL_ROOT_DIR = HKCacheManager.getChildCacheDir(ASSETS_DIR_NAME)

    val MODULES: Lazy<ConcurrentHashMap<String, HKHybirdModuleManager>> =
        lazy {
            val tmpMap = ConcurrentHashMap<String, HKHybirdModuleManager>()
            HKBaseApplication.INSTANCE.assets.list(ASSETS_DIR_NAME).filter { !TextUtils.isEmpty(it) && it.endsWith(CONFIG_SUFFIX) }.map { it.replace(CONFIG_SUFFIX, "") }.forEach {
                tmpMap[it] = HKHybirdModuleManager(it)
                tmpMap[it]?.verify()
            }
            tmpMap
        }

    fun init(env: String? = null) {
        if (!TextUtils.isEmpty(env)) EVN = env!!
        HKLogUtil.d(TAG, ">>>>>>>>>>>>>>>>>>>>====================>>>>>>>>>>>>>>>>>>>>")
        HKLogUtil.w(TAG, "**  INIT START")
        HKLogUtil.d(TAG, ">>>>>>>>>>>>>>>>>>>>====================>>>>>>>>>>>>>>>>>>>>")
        val start = System.currentTimeMillis()
        Observable.fromCallable {
            HKLogUtil.e(TAG, "**  ALL MODULES : " + MODULES.value.keys.toString())
            HKLogUtil.w(TAG, "<<<<<<<<<<<<<<<<<<<<====================<<<<<<<<<<<<<<<<<<<<")
            HKLogUtil.e(TAG, "**  INIT COMPLETE  耗时: ${System.currentTimeMillis() - start}ms")
            HKLogUtil.w(TAG, "<<<<<<<<<<<<<<<<<<<<====================<<<<<<<<<<<<<<<<<<<<")
        }.subscribeOn(Schedulers.newThread()).subscribe()
        HKLogUtil.d(TAG, ">>>>>>>>>>>>>>>>>>>>====================<<<<<<<<<<<<<<<<<<<<")
        HKLogUtil.w(TAG, "**  INIT ING  耗时: ${System.currentTimeMillis() - start}ms")
        HKLogUtil.d(TAG, ">>>>>>>>>>>>>>>>>>>>====================<<<<<<<<<<<<<<<<<<<<")
    }
}
