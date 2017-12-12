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
    var DEBUG = true

    val MODULES: Lazy<ConcurrentHashMap<String, HKHybirdModuleManager>> =
        lazy {
            val tmpMap = ConcurrentHashMap<String, HKHybirdModuleManager>()
            HKBaseApplication.INSTANCE.assets.list(ASSETS_DIR_NAME).filter { !TextUtils.isEmpty(it) && it.endsWith(CONFIG_SUFFIX) }.map { it.replace(CONFIG_SUFFIX, "") }.forEach {
                tmpMap[it] = HKHybirdModuleManager(it)
                moduleInitBeforeVerifyListener?.invoke(tmpMap[it])
                tmpMap[it]?.verify()
            }
            tmpMap
        }

    var moduleInitBeforeVerifyListener: ((moduleManager: HKHybirdModuleManager?) -> Unit?)? = null

    /**
     * debug true  的话代表是测试机,可以拉取动态更新的测试版本
     *       false 代表是已发布的正式的去打包，不能拉取动态更新的测试版本
     */
    @JvmStatic
    @JvmOverloads
    fun init(debug: Boolean = true, env: String? = null, moduleInitBeforeVerifyListener: ((moduleManager: HKHybirdModuleManager?) -> Unit?)? = null) {
        DEBUG = debug

        if (!TextUtils.isEmpty(env)) EVN = env!!
        this.moduleInitBeforeVerifyListener = moduleInitBeforeVerifyListener
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
