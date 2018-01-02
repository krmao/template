package com.smart.library.bundle

import android.text.TextUtils
import android.webkit.WebViewClient
import com.smart.library.base.HKActivityLifecycleCallbacks
import com.smart.library.base.HKBaseApplication
import com.smart.library.util.HKLogUtil
import com.smart.library.util.cache.HKCacheManager
import com.smart.library.util.rx.RxBus
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.util.concurrent.ConcurrentHashMap

/**
 * 面向对象
 * =============================================
 * 1: 适用于本地加速策略，某些网页过大或者希望对网页进行加速
 * 2: 适用于完全本地版本的网页，可以动态调整更新替换
 * 3: 完全线上版本也不会有影响
 *
 *
 * 测试步骤
 * =============================================
 * 设置moduleDebug=true / false 决定是开发机器还是所有用户机器进行下载更新
 * 明智的步骤为:
 * a: moduleDebug=true 进行开发测试
 * b: 测试成功后 修改 moduleDebug=false 进行全局发布
 *
 *
 * =============================================
 * 用到的一些策略
 * =============================================
 *
 * 健康体检策略
 * @see com.smart.library.bundle.HKHybirdManager.checkHealth         健康体检接口
 * @see com.smart.library.widget.webview.client.HKWebViewClient.init 浏览器每次启动时
 * =============================================
 * 1: 每次启动浏览器 执行异步健康检查
 *    每次程序启动时，执行全部模块的健康体检
 *
 * 2: 每次程序启动时，所有模块执行一次健康体检 checkHealth
 * 3: 检测到本地文件校验失败是尝试校验zip，zip校验成功则重新解压，失败则删除当前最新版本
 *    并更新配置信息，还原到上一个最近有效版本，如果上一版本也无效，则会递归直到找到有效版本位置，
 *    如果全部无效，会从原始安装包重新拷贝解压
 *    如果本地所有版本接无效，则重新copy 原始安装包中的版本并且重新解压，此时无需校验
 *
 *
 * 检查更新策略
 * @see com.smart.library.bundle.HKHybirdUpdateManager.checkUpdate
 * =============================================
 * 1: 每次启动模块主URL，执行检查更新  checkUpdate, 目前是所有模块都会检查更新，是否需要只检查当前url对应的模块 todo
 * 2: 每次程序启动时，所有模块执行一次检查更新 checkUpdate
 * 3: 更新策略分 测试机与正式用户机，测试机方便调试，参数为 moduleDebug = true/false
 * 4: 如果检查到更新是最新版本，但是本地文件无效(也许是物理删除)，此过程放在健康体检中，检测无效
 *    则删除当前配置文件自动回退到上一次最近有效版本，所以这里无需判断或者而检测，各司其职
 * 5: 当前粒度是按照整个模块的更新,并且每次是加载主url的时候检查更新，android也只能拦截到主url，
 *    '#' 后面的内部路由无法检测到,
 *    won't do [暂时保留更细粒度的按内部url进行检查更新，暂时不支持这个 moduleRoutesUpdateStrategy 字段]
 * 6: 模块可控粒度 moduleUpdateStrategy                                     todo
 *    0 每次启动模块主URL/每次程序启动时
 * 7: 检查更新是异步的，检测到版本变化时会实时切换 onlineModel 所以无需同步等待
 * 8: onlineMode 的安全性
 *    a: 假如 加载 html 是本地的页面，切换线上后，线上的所有js/css 是基于修改后的 线上 html(线上html有可能已经大变样，js也是)
 *       此时内存中的 html 结构已经不支持显示正常的页面了
 *       此时有两种方案
 *       0: 加载 html 之前 同步检测更新
 *       1: 以全部在线的方式 reload 当前页面
 *       2: 全部离线的方式加载本地缓存页面                                    todo
 *       3: 浏览器回退时如何处理                                             todo
 *
 *
 *
 *       此时应该做好页面级的沙盒操作                                         todo
 *
 *
 * 检查更新策略--回滚策略-实时切换策略
 * @see com.smart.library.bundle.HKHybirdUpdateManager.completeDownloadSuccess
 * =============================================
 * 1: 实施切换的安全性有待验证，可能需要提示用户重启应用，或者重启所有浏览器
 * 2: 实时切换成功后 应广播事件提示所有浏览器刷新当前页面                        todo 通知事件还是自己直接刷新
 * 3: 线上版本是否完备？是否所有的js html css 线上都有，因此可以安全切换在线模式   todo 确保所有文件线上都有
 * 4: 回滚期间会切换到在线状态，回滚后还原上一次是否在线的状态
 * 5: 回滚操作是异步的，对回滚的方法做了同步处理，防止多线程错乱问题
 *   @see com.smart.housekeeper.module.hybird.HybirdApplication.onCreate()
 *
 *
 * 检查更新策略--回滚策略
 * @see com.smart.library.bundle.HKHybirdUpdateManager.rollback
 * =============================================
 * 1: 更新策略增加回滚策略，如要撤销某次更新
 *    a: 服务器删除当前最新配置信息，还原成上一个配置信息，版本号 -1
 *    b: 客户端检测到版本比本地低的时候，则删除本地配置信息及所有文件(一定包括zip,不过以后再发布新的版本号+1，zip MD5也不一样)
 *    c: 删除本地版本时注意适时切换的策略
 *    d: 远程回滚操作已经支持，只要把最新的配置文件修改为旧版本的即可，本地会自动删除高于本版本的所有相关信息即文件，操作为异步线程安全的操作
 *
 *
 * 检查更新策略--下载策略
 * @see com.smart.library.bundle.HKHybirdUpdateManager.download
 * =============================================
 * 1: 如果即将下载的版本 本地解压包存在切校验正确,zip即使不存在也无需下载
 * 2: 如果即将下载的版本 本地解压包不存在或者校验失败,执行下载zip
 *
 *
 **/
@Suppress("MemberVisibilityCanPrivate", "unused", "KDocUnresolvedReference")
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
    @Synchronized
    fun init(debug: Boolean = true, env: String? = null, configer: ((configUrl: String, callback: (HKHybirdConfigModel?) -> Boolean?) -> Boolean?)? = null, downloader: ((downloadUrl: String, file: File?, callback: (File?) -> Unit?) -> Unit?)? = null) {
        val start = System.currentTimeMillis()
        HKLogUtil.w(TAG, ">>>>>>>>>>>>>>>>>>>>====================>>>>>>>>>>>>>>>>>>>>")
        HKLogUtil.w(TAG, ">>>>>>>>>>>>>>>>>>>>====================>>>>>>>>>>>>>>>>>>>>")
        HKLogUtil.w(TAG, ">>>>>>>>>>>>>>>>>>>>====================>>>>>>>>>>>>>>>>>>>>")
        HKLogUtil.w(TAG, "初始化HYBIRD模块 开始")
        HKLogUtil.w(TAG, ">>>>>>>>>>>>>>>>>>>>====================>>>>>>>>>>>>>>>>>>>>")
        HKLogUtil.w(TAG, ">>>>>>>>>>>>>>>>>>>>====================>>>>>>>>>>>>>>>>>>>>")
        HKLogUtil.w(TAG, ">>>>>>>>>>>>>>>>>>>>====================>>>>>>>>>>>>>>>>>>>>")

        DEBUG = debug
        EVN = if (env?.isNotBlank() == true) env else EVN

        MODULES.value.forEach {
            //初始化本模块
            it.value.init(configer, downloader) { _, _ ->
                //检查更新1: 应用程序第一次启动的时候执行一次一步检查更新
                it.value.checkUpdate(synchronized = false, switchToOnlineModeIfRemoteVersionChanged = false)
            }
        }

        //应用程序前后台切换的时候执行一次异步检查更新
        RxBus.toObservable(HKActivityLifecycleCallbacks.ApplicationVisibleChangedEvent::class.java).subscribe { changeEvent ->
            //从前台切换到后台时
            if (!changeEvent.isApplicationVisible) {
                HKLogUtil.e(TAG, "系统监测到应用程序从前台切换到后台,执行一次异步的健康体检和检查更新")
                MODULES.value.forEach {
                    //初始化本模块
                    it.value.checkHealth { _, _ ->
                        //检查更新2: 应用程序第一次启动的时候执行一次一步检查更新
                        it.value.checkUpdate(synchronized = false, switchToOnlineModeIfRemoteVersionChanged = false)
                    }
                }
            }
        }

        HKLogUtil.w(TAG, "<<<<<<<<<<<<<<<<<<<<====================<<<<<<<<<<<<<<<<<<<<")
        HKLogUtil.w(TAG, "<<<<<<<<<<<<<<<<<<<<====================<<<<<<<<<<<<<<<<<<<<")
        HKLogUtil.w(TAG, "<<<<<<<<<<<<<<<<<<<<====================<<<<<<<<<<<<<<<<<<<<")
        HKLogUtil.w(TAG, "初始化HYBIRD模块 结束  耗时: ${System.currentTimeMillis() - start}ms")
        HKLogUtil.w(TAG, "<<<<<<<<<<<<<<<<<<<<====================<<<<<<<<<<<<<<<<<<<<")
        HKLogUtil.w(TAG, "<<<<<<<<<<<<<<<<<<<<====================<<<<<<<<<<<<<<<<<<<<")
        HKLogUtil.w(TAG, "<<<<<<<<<<<<<<<<<<<<====================<<<<<<<<<<<<<<<<<<<<")
    }

    fun getModule(url: String): HKHybirdModuleManager? {
        return MODULES.value.filter { HKHybirdModuleManager.getLocalFile(it.value.currentConfig, url)?.exists() == true }.values.firstOrNull()
    }

    /**
     * 健康体检，检查模块完整性
     * 每次打开浏览器时执行
     *
     * 由于  是同步的，所以在任何地方都可以调用  checkHealth 而不会重复校验,是序列化的
     */
    @JvmStatic
    @Synchronized
    fun checkHealth() {
        HKLogUtil.w(TAG, ">>>>>>>>>>>>>>>>>>>>====================>>>>>>>>>>>>>>>>>>>>")
        HKLogUtil.w(TAG, ">>>>>>>>>>>>>>>>>>>>====================>>>>>>>>>>>>>>>>>>>>")
        HKLogUtil.w(TAG, ">>>>>>>>>>>>>>>>>>>>====================>>>>>>>>>>>>>>>>>>>>")
        HKLogUtil.w(TAG, "所有模块执行一次健康体检 开始")
        HKLogUtil.w(TAG, ">>>>>>>>>>>>>>>>>>>>====================>>>>>>>>>>>>>>>>>>>>")
        HKLogUtil.w(TAG, ">>>>>>>>>>>>>>>>>>>>====================>>>>>>>>>>>>>>>>>>>>")
        HKLogUtil.w(TAG, ">>>>>>>>>>>>>>>>>>>>====================>>>>>>>>>>>>>>>>>>>>")
        val start = System.currentTimeMillis()
        Observable.fromCallable {
            MODULES.value.forEach { it.value.checkHealth(synchronized = false) }

            HKLogUtil.w(TAG, "<<<<<<<<<<<<<<<<<<<<====================<<<<<<<<<<<<<<<<<<<<")
            HKLogUtil.w(TAG, "<<<<<<<<<<<<<<<<<<<<====================<<<<<<<<<<<<<<<<<<<<")
            HKLogUtil.w(TAG, "<<<<<<<<<<<<<<<<<<<<====================<<<<<<<<<<<<<<<<<<<<")
            HKLogUtil.w(TAG, "所有模块执行一次健康体检 结束  耗时: ${System.currentTimeMillis() - start}ms")
            HKLogUtil.w(TAG, "<<<<<<<<<<<<<<<<<<<<====================<<<<<<<<<<<<<<<<<<<<")
            HKLogUtil.w(TAG, "<<<<<<<<<<<<<<<<<<<<====================<<<<<<<<<<<<<<<<<<<<")
            HKLogUtil.w(TAG, "<<<<<<<<<<<<<<<<<<<<====================<<<<<<<<<<<<<<<<<<<<")
        }.subscribeOn(Schedulers.newThread()).subscribe()
    }

    fun onWebViewClose(webViewClient: WebViewClient?) {
        MODULES.value.forEach {
            it.value.onWebViewClose(webViewClient)
        }
    }

    fun setDownloader(downloader: (downloadUrl: String, file: File?, callback: (File?) -> Unit?) -> Unit?) {
        MODULES.value.forEach { it.value.setDownloader(downloader) }
    }

    fun setConfiger(configer: (configUrl: String, callback: (HKHybirdConfigModel?) -> Boolean?) -> Boolean?) {
        MODULES.value.forEach { it.value.setConfiger(configer) }
    }
}
