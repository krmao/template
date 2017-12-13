package com.smart.library.bundle

import android.text.TextUtils
import com.smart.library.base.HKBaseApplication
import com.smart.library.util.HKLogUtil
import com.smart.library.util.cache.HKCacheManager
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.ConcurrentHashMap

/**
 * 面向对象
 * =============================================
 * 1: 适用于本地加速策略，某些网页过大或者希望对网页进行加速
 * 2: 适用于完全本地版本的网页，可以动态调整更新替换
 * 3: 完全线上版本也不会有影响
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

 * 检查更新策略
 * @see com.smart.library.bundle.HKHybirdUpdateManager.checkUpdate
 * =============================================
 * 1: 每次启动模块主URL，执行检查更新  checkUpdate
 * 2: 每次程序启动时，所有模块执行一次检查更新 checkUpdate
 * 3: 更新策略分 测试机与正式用户机，测试机方便调试，参数为 moduleDebug = true/false
 * 4: 如果检查到更新是最新版本，但是本地文件无效(也许是物理删除)，此过程放在健康体检中，检测无效
 *    则删除当前配置文件自动回退到上一次最近有效版本，所以这里无需判断或者而检测，各司其职
 * 5: 当前粒度是按照整个模块的更新,并且每次是加载主url的时候检查更新，android也只能拦截到主url，
 *    '#' 后面的内部路由无法检测到,
 *    won't do [暂时保留更细粒度的按内部url进行检查更新，暂时不支持这个 moduleRoutesUpdateStrategy 字段]
 * 6: 模块可控粒度 moduleUpdateStrategy  todo
 *    0 每次启动模块主URL/每次程序启动时
 * 7: 检查更新是异步的，检测到版本变化时会实时切换 onlineModel 所以无需同步等待
 *
 *
 *
 * 检查更新策略--回滚策略-实时切换策略
 * @see com.smart.library.bundle.HKHybirdUpdateManager.completeUpdating
 * =============================================
 * 1: 实施切换的安全性有待验证，可能需要提示用户重启应用，或者重启所有浏览器
 * 2: 实时切换成功后 应广播事件提示所有浏览器刷新当前页面 todo 通知事件还是自己直接刷新
 * 3: 线上版本是否完备？是否所有的js html css 线上都有，因此可以安全切换在线模式 todo 确保所有文件线上都有
 * 4: 回滚期间会切换到在线状态，回滚后还原上一次是否在线的状态
 * 5: 回滚操作是异步的，对回滚的方法做了同步处理，防止多线程错乱问题
 *   @see com.smart.housekeeper.module.hybird.HybirdApplication.onCreate()
 *
 * 检查更新策略--回滚策略
 * @see com.smart.library.bundle.HKHybirdUpdateManager.rollback
 * =============================================
 * 1: 更新策略增加回滚策略，如要撤销某次更新
 *    a: 服务器删除当前最新配置信息，还原成上一个配置信息，版本号 -1
 *    b: 客户端检测到版本比本地低的时候，则删除本地配置信息及所有文件(一定包括zip,不过以后再发布新的版本号+1，zip MD5也不一样)
 *    c: 删除本地版本时注意适时切换的策略
 * todo 暂时不增加远程控制回滚策略，避免回滚过程中再次发版本的逻辑问题
 *
 * 检查更新策略--下载策略
 * @see com.smart.library.bundle.HKHybirdUpdateManager.download
 * =============================================
 * 1: 如果即将下载的版本 本地解压包存在切校验正确,zip即使不存在也无需下载
 * 2: 如果即将下载的版本 本地解压包不存在或者校验失败,执行下载zip
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
