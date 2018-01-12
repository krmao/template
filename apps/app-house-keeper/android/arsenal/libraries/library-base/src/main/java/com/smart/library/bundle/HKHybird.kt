package com.smart.library.bundle

import android.webkit.WebViewClient
import com.smart.library.base.HKApplicationVisibleChangedEvent
import com.smart.library.bundle.manager.HKHybirdBundleInfoManager
import com.smart.library.bundle.manager.HKHybirdDownloadManager
import com.smart.library.bundle.manager.HKHybirdLifecycleManager
import com.smart.library.bundle.manager.HKHybirdModuleManager
import com.smart.library.bundle.model.HKHybirdModuleBundleModel
import com.smart.library.bundle.model.HKHybirdModuleConfigModel
import com.smart.library.bundle.strategy.HKHybirdInitStrategy
import com.smart.library.bundle.strategy.HKHybirdUpdateStrategy
import com.smart.library.bundle.util.HKHybirdUtil
import com.smart.library.util.HKJsonUtil
import com.smart.library.util.HKLogUtil
import com.smart.library.util.HKTimeUtil
import com.smart.library.util.cache.HKCacheManager
import com.smart.library.util.rx.RxBus
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.util.*
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
 * @see com.smart.library.bundle.HKHybird.checkHealth         健康体检接口
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
object HKHybird {

    @JvmStatic
    val TAG: String = HKHybird::class.java.simpleName

    @JvmStatic
    var EVN = "pre"
        private set

    @JvmStatic
    var DEBUG = true
        private set

    @JvmStatic
    val ASSETS_DIR_NAME = "hybird"
    @JvmStatic
    val BUNDLE_SUFFIX = ".zip"
    @JvmStatic
    val CONFIG_SUFFIX = ".json"
    @JvmStatic
    val LOCAL_ROOT_DIR = HKCacheManager.getChildCacheDir(ASSETS_DIR_NAME)

    /**
     * 初始化策略
     */
    @JvmStatic
    var INIT_STRATEGY = HKHybirdInitStrategy.LOCAL
        internal set

    @JvmStatic
    var downloader: ((downloadUrl: String, file: File?, callback: (File?) -> Unit?) -> Unit?)? = null
        internal set
    @JvmStatic
    var configer: ((configUrl: String, callback: (HKHybirdModuleConfigModel?) -> Unit?) -> Unit?)? = null
        internal set
    @JvmStatic
    var allConfiger: ((allConfigUrl: String, callback: (configList: MutableList<HKHybirdModuleConfigModel>?) -> Unit?) -> Unit?)? = null
        internal set
    @JvmStatic
    var allConfigUrl: String = ""
        internal set

    @JvmStatic
    var MODULES: ConcurrentHashMap<String, HKHybirdModuleManager> = ConcurrentHashMap()
        internal set

    /**
     * debug true  的话代表是测试机,可以拉取动态更新的测试版本
     *       false 代表是已发布的正式的去打包，不能拉取动态更新的测试版本
     */
    @JvmStatic
    @JvmOverloads
    @Synchronized
    fun init(debug: Boolean = true, env: String? = null, initStrategy: HKHybirdInitStrategy = HKHybird.INIT_STRATEGY, allConfigUrl: String = "", allConfiger: ((configUrl: String, callback: (configList: MutableList<HKHybirdModuleConfigModel>?) -> Unit?) -> Unit?)? = null, configer: ((configUrl: String, callback: (HKHybirdModuleConfigModel?) -> Unit?) -> Unit?)? = null, downloader: ((downloadUrl: String, file: File?, callback: (File?) -> Unit?) -> Unit?)? = null) {
        val start = System.currentTimeMillis()
        HKLogUtil.w(TAG, ">>>>----------------------------------------------------------------------")
        HKLogUtil.w(TAG, ">>>>----初始化HYBIRD模块 开始")
        HKLogUtil.w(TAG, ">>>>----------------------------------------------------------------------")

        DEBUG = debug
        EVN = if (env?.isNotBlank() == true) env else EVN
        INIT_STRATEGY = initStrategy

        if (configer != null) HKHybird.configer = configer
        if (downloader != null) HKHybird.downloader = downloader
        if (allConfiger != null) HKHybird.allConfiger = allConfiger
        if (allConfigUrl.isNotBlank()) HKHybird.allConfigUrl = allConfigUrl

        HKLogUtil.w(TAG, ">>>>----DEBUG=$DEBUG")
        HKLogUtil.w(TAG, ">>>>----EVN=$EVN")
        HKLogUtil.w(TAG, ">>>>----INIT_STRATEGY=$INIT_STRATEGY")
        HKLogUtil.w(TAG, ">>>>----allConfigUrl=$allConfigUrl")
        HKLogUtil.w(TAG, ">>>>----------------------------------------------------------------------")
        HKLogUtil.w(TAG, ">>>>----------------------------------------------------------------------")

        /**
         * 关于保存配置信息的时机
         * 1: 为空的时候的第一次初始化
         */
        val bundles: MutableMap<String, HKHybirdModuleBundleModel> = HKHybirdBundleInfoManager.getBundles()
        val bundleNames: MutableSet<String> = bundles.keys

        HKLogUtil.w(TAG, ">>>>----检测到当前初始化策略为: INIT_STRATEGY=$INIT_STRATEGY, 检测本地是否存在缓存配置信息: bundleNames=$bundleNames")
        if (bundleNames.isEmpty()) {
            if (INIT_STRATEGY == HKHybirdInitStrategy.DOWNLOAD) {
                HKLogUtil.w(TAG, ">>>>----由于未检测到缓存配置信息, 开始执行远程下载总配置信息进行初始化")
                if (HKHybird.allConfiger != null && HKHybird.allConfigUrl.isNotBlank()) {
                    HKLogUtil.w(TAG, ">>>>----初始化策略为在线下载初始化, 开始下载总的配置文件, allConfigUrl=${HKHybird.allConfigUrl}")
                    HKHybird.allConfiger?.invoke(HKHybird.allConfigUrl) { remoteConfigList: MutableList<HKHybirdModuleConfigModel>? ->
                        HKLogUtil.w(TAG, ">>>>----总的配置文件下载 ${if (remoteConfigList == null) "失败" else "成功"}")
                        HKLogUtil.j(TAG, HKJsonUtil.toJson(remoteConfigList))

                        downloadAndInitModules(remoteConfigList)
                    }
                } else {
                    HKLogUtil.w(TAG, ">>>>----检测到下载器 allConfiger==null?${HKHybird.allConfiger == null} 尚未设置 或者 总配置信息的 allConfigUrl=$allConfigUrl 没有设置,return")
                }
            } else {
                HKLogUtil.w(TAG, ">>>>----由于未检测到缓存配置信息, 开始执行从 assets 读取总配置信息进行初始化")
                HKHybirdUtil.getConfigListFromAssets { configList ->
                    initAllModules(configList, true)
                }
            }
        } else {
            HKLogUtil.w(TAG, ">>>>----检测本地有缓存配置信息, 根据缓存配置信息初始化")
            initAllModules(bundles.values.mapNotNull { it.moduleConfigList.firstOrNull() }.toMutableList(), true)
        }

        //应用程序前后台切换的时候执行一次异步检查更新
        RxBus.toObservable(HKApplicationVisibleChangedEvent::class.java).subscribe { changeEvent ->
            //从前台切换到后台时
            if (!changeEvent.isApplicationVisible) {
                HKLogUtil.e(TAG, ">>>>----系统监测到应用程序从前台切换到后台,执行一次异步的健康体检和检查更新")
                checkAllUpdate()
            }
        }

        HKLogUtil.w(TAG, ">>>>----------------------------------------------------------------------")
        HKLogUtil.w(TAG, ">>>>----初始化HYBIRD模块 结束  耗时: ${System.currentTimeMillis() - start}ms")
        HKLogUtil.w(TAG, ">>>>----------------------------------------------------------------------")
    }

    fun downloadAndInitModules(configList: MutableList<HKHybirdModuleConfigModel>?) {
        HKHybirdUtil.downloadAllModules(configList) { validConfigList: MutableList<HKHybirdModuleConfigModel>? ->
            initAllModules(validConfigList, false)
        }
    }

    fun downloadAndInitModule(config: HKHybirdModuleConfigModel?) {
        if (config != null) {
            val start = System.currentTimeMillis()
            HKLogUtil.d(HKHybird.TAG, "**********[downloadAndInitModule:单模块开始")

            HKLogUtil.d(HKHybird.TAG, "**********[downloadAndInitModule:单模块下载:${config.moduleName}:开始], 当前线程:${Thread.currentThread().name}, 当前时间:${HKTimeUtil.yMdHmsS(Date(start))}")
            HKLogUtil.d(HKHybird.TAG, "**********[downloadAndInitModule:单模块下载:${config.moduleName}:开始], ${config.moduleDownloadUrl}")

            HKHybirdDownloadManager.download(config) { isLocalFilesValid: Boolean ->
                HKLogUtil.d(HKHybird.TAG, "**********[downloadAndInitModule:单模块下载:${config.moduleName}:结束], isLocalFilesValid:$isLocalFilesValid")
                HKLogUtil.d(HKHybird.TAG, "**********[downloadAndInitModule:单模块下载:${config.moduleName}:结束], 当前线程:${Thread.currentThread().name}, 当前时间:${HKTimeUtil.yMdHmsS(Date())}, 耗时:${System.currentTimeMillis() - start}ms, config=$config")
                if (isLocalFilesValid) {
                    initModule(config)
                }
            }
        }
    }

    fun initModule(config: HKHybirdModuleConfigModel?, callback: ((config: HKHybirdModuleConfigModel?) -> Unit)? = null) {
        val start = System.currentTimeMillis()
        HKLogUtil.e(TAG, "--[initModule:${config?.moduleName}初始化开始]-----------------------------------------------------------------------------------")
        HKLogUtil.e(TAG, "--[initModule:${config?.moduleName}初始化开始], 当前线程:${Thread.currentThread().name}, 当前时间:${HKTimeUtil.yMdHmsS(Date(start))}")
        if (config == null) {
            HKLogUtil.e(HKHybird.TAG, "--[initModule:${config?.moduleName}初始化结束], 没有模块需要初始化")
            HKLogUtil.e(HKHybird.TAG, "--[initModule:${config?.moduleName}初始化结束], 当前线程:${Thread.currentThread().name}, 当前时间:${HKTimeUtil.yMdHmsS(Date())} ,最终成功初始化的模块:${HKHybird.MODULES.map { it.key }} , 一共耗时:${System.currentTimeMillis() - start}ms")
            HKLogUtil.e(HKHybird.TAG, "--[initModule:${config?.moduleName}初始化结束]-----------------------------------------------------------------------------------")
            callback?.invoke(null)
        } else {
            Observable.fromCallable { initModuleManager(config) }
                .subscribeOn(Schedulers.io())
                .subscribe {
                    HKLogUtil.e(TAG, "--[initModule:${config.moduleName}初始化结束], 当前线程:${Thread.currentThread().name}, 当前时间:${HKTimeUtil.yMdHmsS(Date())} ,最终成功初始化的模块:${MODULES.map { it.key }} , 一共耗时:${System.currentTimeMillis() - start}ms")
                    HKLogUtil.e(TAG, "--[initModule:${config.moduleName}初始化结束]-----------------------------------------------------------------------------------")
                    callback?.invoke(config)
                }
        }
    }

    private fun initModuleManager(config: HKHybirdModuleConfigModel?) {
        val _start = System.currentTimeMillis()
        HKLogUtil.w(TAG, "--[initModule:${config?.moduleName}](开始), 当前线程:${Thread.currentThread().name}, 当前时间:${HKTimeUtil.yMdHmsS(Date(_start))}")

        if (config != null) {
            val moduleManager = HKHybirdModuleManager.create(config)
            if (moduleManager != null) {
                MODULES[config.moduleName] = moduleManager
                HKHybirdBundleInfoManager.saveConfigToBundleByName(config.moduleName, config)
            }
        }
        HKLogUtil.w(TAG, "--[initModule:${config?.moduleName}](结束), 当前线程:${Thread.currentThread().name}, 当前时间:${HKTimeUtil.yMdHmsS(Date())} , 一共耗时:${System.currentTimeMillis() - _start}ms")
    }

    /**
     * @param callback 在全部模块成功初始化结束以后,检查全部更新之前回调, configList 返回空,代表没有模块被成功初始化, 属于初始化失败的标志
     */
    private fun initAllModules(configList: MutableList<HKHybirdModuleConfigModel>?, isNeedCheckAllUpdateAfterAllModulesSuccessInit: Boolean = true, callback: ((configList: MutableList<HKHybirdModuleConfigModel>?) -> Unit)? = null) {
        val start = System.currentTimeMillis()
        HKLogUtil.e(TAG, "--[initAllModules:全部初始化开始]-----------------------------------------------------------------------------------")
        HKLogUtil.e(TAG, "--[initAllModules:全部初始化开始], 当前线程:${Thread.currentThread().name}, 当前时间:${HKTimeUtil.yMdHmsS(Date(start))}  ,isNeedCheckAllUpdateAfterAllModulesSuccessInit=$isNeedCheckAllUpdateAfterAllModulesSuccessInit")
        if (configList == null || configList.isEmpty()) {
            HKLogUtil.e(HKHybird.TAG, "--[initAllModules:全部初始化结束], 没有模块需要初始化")
            HKLogUtil.e(HKHybird.TAG, "--[initAllModules:全部初始化结束], 当前线程:${Thread.currentThread().name}, 当前时间:${HKTimeUtil.yMdHmsS(Date())} ,最终成功初始化的模块:${HKHybird.MODULES.map { it.key }} , 一共耗时:${System.currentTimeMillis() - start}ms")
            HKLogUtil.e(HKHybird.TAG, "--[initAllModules:全部初始化结束]-----------------------------------------------------------------------------------")
            callback?.invoke(null)
        } else {
            Observable.zip(
                configList.map { config ->
                    Observable.fromCallable { initModuleManager(config) }.subscribeOn(Schedulers.io())
                }
                ,
                ({
                })
            ).subscribe {


                HKLogUtil.e(TAG, "--[initAllModules:全部初始化结束], 当前线程:${Thread.currentThread().name}, 当前时间:${HKTimeUtil.yMdHmsS(Date())} ,最终成功初始化的模块:${MODULES.map { it.key }} , 一共耗时:${System.currentTimeMillis() - start}ms")
                HKLogUtil.e(TAG, "--[initAllModules:全部初始化结束]-----------------------------------------------------------------------------------")

                callback?.invoke(configList)

                if (isNeedCheckAllUpdateAfterAllModulesSuccessInit) {
                    checkAllUpdate()
                }
            }
        }
    }

    /**
     * 只有各模块初始化成功后可以执行该方法, 因为会用到 MODULES
     */
    fun checkAllUpdate() {
        val start = System.currentTimeMillis()
        HKLogUtil.e(TAG, ">>>>>>>检查更新:下载开始, 当前线程:${Thread.currentThread().name}, 当前时间:${HKTimeUtil.yMdHmsS(Date(start))}")
        HKHybird.allConfiger?.invoke(HKHybird.allConfigUrl) { remoteConfigList: MutableList<HKHybirdModuleConfigModel>? ->
            HKLogUtil.w(TAG, ">>>>----检查更新:下载${if (remoteConfigList == null) "失败" else "成功"}, 当前时间:${HKTimeUtil.yMdHmsS(Date())}, 耗时:${System.currentTimeMillis() - start}ms")
            HKLogUtil.j(TAG, HKJsonUtil.toJson(remoteConfigList))

            val needDownloadAndInitConfigList: MutableList<HKHybirdModuleConfigModel> = mutableListOf()

            remoteConfigList?.forEach {
                if (MODULES.containsKey(it.moduleName)) {
                    checkUpdate(it)
                } else {
                    needDownloadAndInitConfigList.add(it)
                }
            }

            if (needDownloadAndInitConfigList.isNotEmpty()) {
                downloadAndInitModules(needDownloadAndInitConfigList)
            }

            HKLogUtil.w(TAG, ">>>>----检查更新:结束, 当前时间:${HKTimeUtil.yMdHmsS(Date())}, 耗时:${System.currentTimeMillis() - start}ms")
        }
    }

    fun checkUpdate(remoteConfig: HKHybirdModuleConfigModel?) {
        HKLogUtil.e(TAG, ">>>>>>>======检查子模块更新 开始:${remoteConfig?.moduleName}, 当前时间:${HKTimeUtil.yMdHmsS(Date())}")

        if (remoteConfig != null) {
            val moduleManager = MODULES[remoteConfig.moduleName]
            if (moduleManager != null) {
                HKLogUtil.e(TAG, ">>>>>>>======检查子模块本地已经有以前的版本信息,进行比较操作")
                //1:正式包，所有机器可以拉取
                //2:测试包，只要测试机器可以拉取
                if (!remoteConfig.moduleDebug || (remoteConfig.moduleDebug && HKHybird.DEBUG)) {
                    HKLogUtil.e(remoteConfig.moduleName, "检测到该版本为正式版 或者当前为测试版本并且本机是测试机,可以执行更新操作")
                    val remoteVersion = remoteConfig.moduleVersion.toFloatOrNull()
                    val localVersion = moduleManager.currentConfig?.moduleVersion?.toFloatOrNull()
                    HKLogUtil.v(TAG, ">>>>>>>======${remoteConfig.moduleName} 当前版本:$localVersion   远程版本:$remoteVersion")
                    if (remoteVersion != null && localVersion != null) {
                        //版本号相等时不做任何处理，避免不必要的麻烦
                        if (remoteVersion != localVersion) {
                            HKLogUtil.v(TAG, ">>>>>>>======${remoteConfig.moduleName} 系统检测到有新版本")

                            if (remoteConfig.moduleUpdateStrategy == HKHybirdUpdateStrategy.ONLINE) {
                                moduleManager.onlineModel = true
                            } else {
                                HKLogUtil.e(TAG, ">>>>>>>======${remoteConfig.moduleName} 无需切换为在线模式")
                            }
                            HKHybirdDownloadManager.download(remoteConfig)
                        } else {
                            HKLogUtil.v(TAG, ">>>>>>>======${remoteConfig.moduleName} 系统检测到 remoteVersion:$remoteVersion 或者 localVersion:$localVersion 相等, 无需更新")
                        }
                    } else {
                        HKLogUtil.e(TAG, ">>>>>>>======${remoteConfig.moduleName} 系统检测到 remoteVersion:$remoteVersion 或者 localVersion:$localVersion 为空, 无法判断需要更新,默认不需要更新")
                    }
                } else {
                    HKLogUtil.e(TAG, ">>>>>>>======${remoteConfig.moduleName} 检测到该版本为调试版本且本机不是测试机,不执行更新操作 return false")
                }
            }
        }
        HKLogUtil.e(TAG, ">>>>>>>======检查子模块结束 结束:${remoteConfig?.moduleName}, 当前时间:${HKTimeUtil.yMdHmsS(Date())}")
    }

    /**
     * 本操作会修改 onLineMode 状态
     *
     * 检查更新-同步(加载本模块URL的时候)
     * 注意: 此处无需处理 模块第一次加载 然后合并 下次启动生效的配置文件  操作, 因为既然打开了本网页,前提是已经checkHealth, 而 checkHealth 已经包含了 fitNextAndFitLocalIfNeedConfigsInfo
     *       所以,良好的设计是此时无需关注合版本合并信息,只关注自己的责任,检查/下载更新
     *
     * A:   进来发现  当前已经是在线状态    说明时间段为  正在下载 到 应用成功之前 这个时间段内
     *
     * 不执行重复网络请求,return true, 说明正在处理更新操作
     * ----
     * B:   进来发现  当前不是在线状态
     *
     * 1: 执行检查更新
     *
     * 2: 如果检测到 有更新或者回滚指令 则异步下载更新包(这里需要检测本地是否已经下载,已经解压,只待切换),并解压到本地,校验OK后,将状态保存为下一次模块启动替换
     *
     *      调用时机:   系统启动时,浏览器启动时,模块主url被拦截到时,前后台切换时
     *
     *                 其中 系统启动时         --> 检测模块是否已经被打开,如果没有且更细胞准备充分,则同步更新替换本地文件结束再执行放行
     *                 其中 模块主url被拦截到时 --> 检测模块是否已经被打开,如果没有且更细胞准备充分,则同步更新替换本地文件结束再执行放行
     *
     *      a-> 回滚/升级, 更新策略为在线:  则立即 切换为在线状态(onlineModel=true),    访问在线资源     ,当检测到模块没有被浏览器加载的时候,执行回滚操作
     *      b-> 回滚/升级, 更新策略为离线:  则依然 使用本地文件  (onlineModel=false),    访问本地资源     ,当检测到模块没有被浏览器加载的时候,执行回滚操作
     *
     *
     * 3: 如果没有更新 return false
     *
     *
     * 返回 true  直到更新包尚未应用成功
     * 返回 false 代表没有更新,或者更新包已经应用成功
     */
    /**
     * 检查更新一共有三个地方
     *
     * 更新策略为ONLINE 时,  1:程序启动,2:前后台切换,3:webView加载模块
     * 更新策略为OFFLINE 时,  1:程序启动,2:前后台切换
     */
    @JvmStatic
    fun checkUpdate(url: String?, callback: (() -> Unit?)? = null) {
        val moduleManager = getModule(url)
        if (moduleManager == null) {
            callback?.invoke()
        } else {
            checkUpdate(moduleManager, callback)
        }
    }

    @JvmStatic
    fun checkUpdate(moduleManager: HKHybirdModuleManager?, callback: (() -> Unit?)? = null) {
        if (moduleManager != null) {
            val moduleName = moduleManager.currentConfig?.moduleName
            val start = System.currentTimeMillis()
            HKLogUtil.v(HKHybird.TAG + ":" + moduleName, "系统检测更新(同步) 开始 当前版本=${moduleManager.currentConfig?.moduleVersion},当前线程:${Thread.currentThread().name}")
            HKLogUtil.v(HKHybird.TAG + ":" + moduleName, "当前配置=${moduleManager.currentConfig}")
            if (moduleManager.currentConfig == null) {
                HKLogUtil.e(HKHybird.TAG + ":" + moduleName, "系统检测到当前模块尚未被初始化,必须执行初始化操作")
                moduleManager.checkHealth(synchronized = true)
            } else {
                HKLogUtil.e(HKHybird.TAG + ":" + moduleName, "系统检测到当前模块已被初始化过,可以执行检查更新")
            }

            if (HKHybird.configer == null) {
                HKLogUtil.e(HKHybird.TAG + ":" + moduleName, "系统检测到尚未配置 config 下载器，请先设置 config 下载器, return")
                callback?.invoke()
                return
            }

            if (HKHybirdDownloadManager.isDownloading(moduleManager.currentConfig)) {
                HKLogUtil.e(HKHybird.TAG + ":" + moduleName, "系统检测到当前正在下载更新中, return")
                callback?.invoke()
                return
            }

            if (moduleManager.onlineModel) {
                HKLogUtil.e(HKHybird.TAG + ":" + moduleName, "系统检测到当前已经是在线状态了,无需重复检测 return")
                callback?.invoke()
                return
            }

            val moduleConfigUrl = moduleManager.currentConfig?.moduleConfigUrl ?: ""
            HKLogUtil.v(HKHybird.TAG + ":" + moduleName, "下载配置文件 开始 当前版本=${moduleManager.currentConfig?.moduleVersion}: $moduleConfigUrl , 当前线程:${Thread.currentThread().name}")
            HKHybird.configer?.invoke(moduleConfigUrl) { remoteConfig: HKHybirdModuleConfigModel? ->
                HKLogUtil.v(HKHybird.TAG + ":" + moduleName, "下载配置文件 ${if (remoteConfig == null) "失败" else "成功"} , 当前线程:${Thread.currentThread().name}")
                HKLogUtil.j(HKHybird.TAG + ":" + moduleName, HKJsonUtil.toJson(remoteConfig))
                if (remoteConfig != null) {
                    //1:正式包，所有机器可以拉取
                    //2:测试包，只要测试机器可以拉取
                    if (!remoteConfig.moduleDebug || (remoteConfig.moduleDebug && HKHybird.DEBUG)) {
                        HKLogUtil.e(HKHybird.TAG + ":" + moduleName, "检测到该版本为正式版 或者当前为测试版本并且本机是测试机,可以执行更新操作")
                        val remoteVersion = remoteConfig.moduleVersion.toFloatOrNull()
                        val localVersion = moduleManager.currentConfig?.moduleVersion?.toFloatOrNull()
                        HKLogUtil.v("${HKHybird.TAG + ":" + moduleName} 当前版本:$localVersion   远程版本:$remoteVersion")
                        if (remoteVersion != null && localVersion != null) {
                            //版本号相等时不做任何处理，避免不必要的麻烦
                            if (remoteVersion != localVersion) {
                                HKLogUtil.v("系统检测到有新版本")

                                if (remoteConfig.moduleUpdateStrategy == HKHybirdUpdateStrategy.ONLINE) {
                                    moduleManager.onlineModel = true
                                } else {
                                    HKLogUtil.e("无需切换为在线模式")
                                }
                                HKHybirdDownloadManager.download(remoteConfig)
                            } else {
                                HKLogUtil.v("系统检测到 remoteVersion:$remoteVersion 或者 localVersion:$localVersion 相等, 无需更新")
                            }
                        } else {
                            HKLogUtil.e("系统检测到 remoteVersion:$remoteVersion 或者 localVersion:$localVersion 为空, 无法判断需要更新,默认不需要更新")
                        }
                    } else {
                        HKLogUtil.e(HKHybird.TAG + ":" + moduleName, "检测到该版本为调试版本且本机不是测试机,不执行更新操作 return false")
                    }
                }
                callback?.invoke()
            }
            HKLogUtil.v(HKHybird.TAG + ":" + moduleName, "检查更新 结束, 当前线程:${Thread.currentThread().name}, 耗时: ${System.currentTimeMillis() - start}ms")
        }
    }

    fun getModule(url: String?): HKHybirdModuleManager? {
        return if (url.isNullOrBlank()) null else HKHybird.MODULES.values.firstOrNull { isMemberOfModule(it.currentConfig, url) }
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
        HKLogUtil.w(TAG, "所有模块执行一次健康体检 开始")
        HKLogUtil.w(TAG, ">>>>>>>>>>>>>>>>>>>>====================>>>>>>>>>>>>>>>>>>>>")
        val start = System.currentTimeMillis()
        Observable.fromCallable {
            MODULES.forEach { it.value.checkHealth(synchronized = false) }

            HKLogUtil.w(TAG, "<<<<<<<<<<<<<<<<<<<<====================<<<<<<<<<<<<<<<<<<<<")
            HKLogUtil.w(TAG, "所有模块执行一次健康体检 结束  耗时: ${System.currentTimeMillis() - start}ms")
            HKLogUtil.w(TAG, "<<<<<<<<<<<<<<<<<<<<====================<<<<<<<<<<<<<<<<<<<<")
        }.subscribeOn(Schedulers.newThread()).subscribe()
    }

    @JvmStatic
    fun onWebViewClose(webViewClient: WebViewClient?) {
        HKHybirdLifecycleManager.onWebViewClose(webViewClient)
    }

    @JvmStatic
    fun onWebViewOpenPage(webViewClient: WebViewClient?, url: String?) {
        HKHybirdLifecycleManager.onWebViewOpenPage(webViewClient, url)
    }

    @JvmStatic
    fun isMemberOfModule(config: HKHybirdModuleConfigModel?, url: String?): Boolean {
        return url?.contains(config?.moduleMainUrl ?: "") == true
    }

    @JvmStatic
    fun isModuleOpened(moduleName: String?): Boolean {
        return HKHybirdLifecycleManager.isModuleOpened(moduleName)
    }

}
