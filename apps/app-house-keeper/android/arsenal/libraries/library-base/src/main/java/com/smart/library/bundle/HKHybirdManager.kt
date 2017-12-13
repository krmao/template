package com.smart.library.bundle

import android.text.TextUtils
import com.smart.library.base.HKBaseApplication
import com.smart.library.util.HKLogUtil
import com.smart.library.util.cache.HKCacheManager
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.ConcurrentHashMap

/**
 * 检查策略
 * =============================================
 * 1: 每次启动浏览器 执行异步健康检查  checkHealth，每次程序启动时，执行全部模块的健康体检
 * @see com.smart.library.widget.webview.client.HKWebViewClient#init
 *
 * 2: 每次启动模块主URL，执行检查更新  checkUpdate
 * 3: 每次程序启动时，a:所有模块执行一次检查更新 checkUpdate，b:所有模块执行一次健康体检 checkHealth
 *
 * 回滚策略
 * =============================================
 * todo
 *
 * online/offline 切换策略
 * =============================================
 * 1: 实施切换的安全性有待验证，可能需要提示用户重启应用，或者重启所有浏览器
 * 2: 实时切换成功后 应广播事件提示所有浏览器刷新当前页面
 * 3: 线上版本是否完备？是否所有的js html css 线上都有，因此可以安全切换在线模式 todo
 *
 */
@Suppress("MemberVisibilityCanPrivate", "unused")
object HKHybirdManager {
    private val TAG = HKHybirdManager::class.java.simpleName

    var EVN = "pre"
        private set
    var DEBUG = true
        private set

    val ASSETS_DIR_NAME = "hybird"
    val BUNDLE_SUFFIX = ".zip"
    val CONFIG_SUFFIX = ".json"
    val LOCAL_ROOT_DIR = HKCacheManager.getChildCacheDir(ASSETS_DIR_NAME)

    val MODULES: Lazy<ConcurrentHashMap<String, HKHybirdModuleManager>> =
        lazy {
            val tmpMap = ConcurrentHashMap<String, HKHybirdModuleManager>()
            HKBaseApplication.INSTANCE.assets
                .list(ASSETS_DIR_NAME)
                .filter { !TextUtils.isEmpty(it) && it.endsWith(CONFIG_SUFFIX) }
                .map { it.replace(CONFIG_SUFFIX, "") }
                .forEach { tmpMap[it] = HKHybirdModuleManager(it) }

            tmpMap
        }

    /**
     * debug true  的话代表是测试机,可以拉取动态更新的测试版本
     *       false 代表是已发布的正式的去打包，不能拉取动态更新的测试版本
     */
    @JvmStatic
    @JvmOverloads
    fun init(debug: Boolean = true, env: String? = null) {
        DEBUG = debug
        EVN = if (env?.isNotBlank() == true) env else EVN
        checkHealth()
    }

    /**
     * 健康体检，检查模块完整性
     * 每次打开浏览器时执行
     *
     * 由于 initLocalConfiguration 是同步的，所以在任何地方都可以调用  checkHealth 而不会重复校验,是序列化的
     */
    @JvmStatic
    @Synchronized
    fun checkHealth() {
        HKLogUtil.d(TAG, ">>>>>>>>>>>>>>>>>>>>====================>>>>>>>>>>>>>>>>>>>>")
        HKLogUtil.e(TAG, "**  CHECK HEALTH START")
        HKLogUtil.d(TAG, ">>>>>>>>>>>>>>>>>>>>====================>>>>>>>>>>>>>>>>>>>>")
        val start = System.currentTimeMillis()
        Observable.fromCallable {
            MODULES.value.forEach {
                HKLogUtil.w(TAG, "**  --> MODULE CHECK HEALTH START: " + it.key)
                it.value.checkHealth()
            }

            HKLogUtil.w(TAG, "<<<<<<<<<<<<<<<<<<<<====================<<<<<<<<<<<<<<<<<<<<")
            HKLogUtil.e(TAG, "**  CHECK HEALTH COMPLETE  耗时: ${System.currentTimeMillis() - start}ms")
            HKLogUtil.w(TAG, "<<<<<<<<<<<<<<<<<<<<====================<<<<<<<<<<<<<<<<<<<<")
        }.subscribeOn(Schedulers.newThread()).subscribe()
        HKLogUtil.d(TAG, ">>>>>>>>>>>>>>>>>>>>====================<<<<<<<<<<<<<<<<<<<<")
        HKLogUtil.e(TAG, "**  CHECK HEALTH ING  耗时: ${System.currentTimeMillis() - start}ms")
        HKLogUtil.d(TAG, ">>>>>>>>>>>>>>>>>>>>====================<<<<<<<<<<<<<<<<<<<<")
    }
}
