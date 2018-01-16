package com.smart.library.bundle

import android.util.Log
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

@Suppress("MemberVisibilityCanPrivate", "unused", "KDocUnresolvedReference")
object HKHybird {

    @JvmStatic
    internal val TAG: String = HKHybird::class.java.simpleName

    @JvmStatic
    var debug = true
        private set

    @JvmStatic
    val assetsDirName = "hybird"
    @JvmStatic
    val bundleSuffix = ".zip"
    @JvmStatic
    val configSuffix = ".json"
    @JvmStatic
    val localRootDir = HKCacheManager.getChildCacheDir(assetsDirName)

    @JvmStatic
    var initStrategy = HKHybirdInitStrategy.LOCAL
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
    var modules: ConcurrentHashMap<String, HKHybirdModuleManager> = ConcurrentHashMap()
        internal set

    @JvmStatic
    fun removeModule(moduleName: String?) {
        if (moduleName != null && moduleName.isNotBlank()) {
            HKHybirdUtil.removeIntercept(HKHybird.modules[moduleName]?.currentConfig)
            HKHybird.modules.remove(moduleName)
        }
    }

    /**
     * debug true  的话代表是测试机,可以拉取动态更新的测试版本
     *       false 代表是已发布的正式的去打包，不能拉取动态更新的测试版本
     */
    @JvmStatic
    @JvmOverloads
    @Synchronized
    fun init(debug: Boolean = true, initStrategy: HKHybirdInitStrategy = HKHybird.initStrategy, allConfigUrl: String = "", allConfiger: ((configUrl: String, callback: (configList: MutableList<HKHybirdModuleConfigModel>?) -> Unit?) -> Unit?)? = null, configer: ((configUrl: String, callback: (HKHybirdModuleConfigModel?) -> Unit?) -> Unit?)? = null, downloader: ((downloadUrl: String, file: File?, callback: (File?) -> Unit?) -> Unit?)? = null) {
        val start = System.currentTimeMillis()
        HKLogUtil.w(TAG, ">>>>----------------------------------------------------------------------")
        HKLogUtil.w(TAG, ">>>>----初始化HYBIRD模块 开始")
        HKLogUtil.w(TAG, ">>>>----------------------------------------------------------------------")

        this.debug = debug
        this.initStrategy = initStrategy

        if (configer != null) HKHybird.configer = configer
        if (downloader != null) HKHybird.downloader = downloader
        if (allConfiger != null) HKHybird.allConfiger = allConfiger
        if (allConfigUrl.isNotBlank()) HKHybird.allConfigUrl = allConfigUrl

        HKLogUtil.w(TAG, ">>>>----debug=${this.debug}")
        HKLogUtil.w(TAG, ">>>>----initStrategy=${this.initStrategy}")
        HKLogUtil.w(TAG, ">>>>----allConfigUrl=$allConfigUrl")
        HKLogUtil.w(TAG, ">>>>----------------------------------------------------------------------")
        HKLogUtil.w(TAG, ">>>>----------------------------------------------------------------------")

        /**
         * 关于保存配置信息的时机
         * 1: 为空的时候的第一次初始化
         */
        val bundles: MutableMap<String, HKHybirdModuleBundleModel> = HKHybirdBundleInfoManager.getBundles()
        val bundleNames: MutableSet<String> = bundles.keys

        HKLogUtil.w(TAG, ">>>>----检测到当前初始化策略为: initStrategy=${this.initStrategy}, 检测本地是否存在缓存配置信息: bundleNames=$bundleNames")
        if (bundleNames.isEmpty()) {
            if (this.initStrategy == HKHybirdInitStrategy.DOWNLOAD) {
                HKLogUtil.w(TAG, ">>>>----由于未检测到缓存配置信息, 开始执行远程下载总配置信息进行初始化")
                if (HKHybird.allConfiger != null && HKHybird.allConfigUrl.isNotBlank()) {
                    HKLogUtil.w(TAG, ">>>>----初始化策略为在线下载初始化, 开始下载总的配置文件, allConfigUrl=${HKHybird.allConfigUrl}")
                    HKHybird.allConfiger?.invoke(HKHybird.allConfigUrl) { remoteConfigList: MutableList<HKHybirdModuleConfigModel>? ->
                        HKLogUtil.w(TAG, ">>>>----总的配置文件下载 ${if (remoteConfigList == null) "失败" else "成功"}")
                        HKLogUtil.w(TAG, ">>>>----remoteConfigList->")
                        HKLogUtil.j(Log.WARN, TAG, HKJsonUtil.toJson(remoteConfigList))

                        downloadAndInitModules(remoteConfigList)
                    }
                } else {
                    HKLogUtil.w(TAG, ">>>>----检测到下载器 allConfiger==null?${HKHybird.allConfiger == null} 尚未设置 或者 总配置信息的 allConfigUrl=$allConfigUrl 没有设置,return")
                }
            } else {
                HKLogUtil.w(TAG, ">>>>----由于未检测到缓存配置信息, 开始执行从 assets 读取总配置信息进行初始化")
                HKHybirdUtil.getConfigListFromAssetsWithCopyAndUnzip { configList ->
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

    private fun downloadAndInitModules(configList: MutableList<HKHybirdModuleConfigModel>?) {
        HKHybirdUtil.downloadAllModules(configList) { validConfigList: MutableList<HKHybirdModuleConfigModel>? ->
            initAllModules(validConfigList, false)
        }
    }

    private fun downloadAndInitModule(config: HKHybirdModuleConfigModel?) {
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

    private fun initModule(config: HKHybirdModuleConfigModel?, callback: ((config: HKHybirdModuleConfigModel?) -> Unit)? = null) {
        val start = System.currentTimeMillis()
        HKLogUtil.e(TAG, "--[initModule:${config?.moduleName}初始化开始]-----------------------------------------------------------------------------------")
        HKLogUtil.e(TAG, "--[initModule:${config?.moduleName}初始化开始], 当前线程:${Thread.currentThread().name}, 当前时间:${HKTimeUtil.yMdHmsS(Date(start))}")
        if (config == null) {
            HKLogUtil.e(HKHybird.TAG, "--[initModule:${config?.moduleName}初始化结束], 没有模块需要初始化")
            HKLogUtil.e(HKHybird.TAG, "--[initModule:${config?.moduleName}初始化结束], 当前线程:${Thread.currentThread().name}, 当前时间:${HKTimeUtil.yMdHmsS(Date())} ,最终成功初始化的模块:${HKHybird.modules.map { it.key }} , 一共耗时:${System.currentTimeMillis() - start}ms")
            HKLogUtil.e(HKHybird.TAG, "--[initModule:${config?.moduleName}初始化结束]-----------------------------------------------------------------------------------")
            callback?.invoke(null)
        } else {
            Observable.fromCallable { initModuleManager(config) }
                .subscribeOn(Schedulers.io())
                .subscribe {
                    HKLogUtil.e(TAG, "--[initModule:${config.moduleName}初始化结束], 当前线程:${Thread.currentThread().name}, 当前时间:${HKTimeUtil.yMdHmsS(Date())} ,最终成功初始化的模块:${modules.map { it.key }} , 一共耗时:${System.currentTimeMillis() - start}ms")
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
                modules[config.moduleName] = moduleManager
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
            HKLogUtil.e(HKHybird.TAG, "--[initAllModules:全部初始化结束], 当前线程:${Thread.currentThread().name}, 当前时间:${HKTimeUtil.yMdHmsS(Date())} ,最终成功初始化的模块:${HKHybird.modules.map { it.key }} , 一共耗时:${System.currentTimeMillis() - start}ms")
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


                HKLogUtil.e(TAG, "--[initAllModules:全部初始化结束], 当前线程:${Thread.currentThread().name}, 当前时间:${HKTimeUtil.yMdHmsS(Date())} ,最终成功初始化的模块:${modules.map { it.key }} , 一共耗时:${System.currentTimeMillis() - start}ms")
                HKLogUtil.e(TAG, "--[initAllModules:全部初始化结束]-----------------------------------------------------------------------------------")

                callback?.invoke(configList)

                if (isNeedCheckAllUpdateAfterAllModulesSuccessInit) {
                    checkAllUpdate()
                }
            }
        }
    }

    /**
     * 只有各模块初始化成功后可以执行该方法, 因为会用到 modules
     */
    fun checkAllUpdate() {
        val start = System.currentTimeMillis()
        HKLogUtil.e(TAG, ">>>>----检查更新:下载开始, 当前线程:${Thread.currentThread().name}, 当前时间:${HKTimeUtil.yMdHmsS(Date(start))}")
        HKHybird.allConfiger?.invoke(HKHybird.allConfigUrl) { remoteConfigList: MutableList<HKHybirdModuleConfigModel>? ->
            HKLogUtil.w(TAG, ">>>>----检查更新:下载${if (remoteConfigList == null) "失败" else "成功"}, 当前时间:${HKTimeUtil.yMdHmsS(Date())}, 耗时:${System.currentTimeMillis() - start}ms")
            HKLogUtil.w(TAG, ">>>>----remoteConfigList->")
            HKLogUtil.j(Log.WARN, TAG, HKJsonUtil.toJson(remoteConfigList))

            val needDownloadAndInitConfigList: MutableList<HKHybirdModuleConfigModel> = mutableListOf()

            remoteConfigList?.forEach {
                if (modules.containsKey(it.moduleName)) {
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
            val moduleManager = modules[remoteConfig.moduleName]
            if (moduleManager != null) {
                HKLogUtil.e(TAG, ">>>>>>>======检查子模块本地已经有以前的版本信息,进行比较操作")
                //1:正式包，所有机器可以拉取
                //2:测试包，只要测试机器可以拉取
                if (!remoteConfig.moduleDebug || (remoteConfig.moduleDebug && HKHybird.debug)) {
                    HKLogUtil.e(remoteConfig.moduleName, "检测到该版本为正式版 或者当前为测试版本并且本机是测试机,可以执行更新操作")
                    val remoteVersion = remoteConfig.moduleVersion.toFloatOrNull()
                    val localVersion = moduleManager.currentConfig.moduleVersion.toFloatOrNull()
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
                            HKHybirdDownloadManager.download(remoteConfig) { isLocalFilesValid ->

                            }
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
            val moduleName = moduleManager.currentConfig.moduleName
            val start = System.currentTimeMillis()
            HKLogUtil.v(HKHybird.TAG + ":" + moduleName, "系统检测更新(同步) 开始 当前版本=${moduleManager.currentConfig.moduleVersion},当前线程:${Thread.currentThread().name}")
            HKLogUtil.v(HKHybird.TAG + ":" + moduleName, "当前配置=${moduleManager.currentConfig}")

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

            val moduleConfigUrl = moduleManager.currentConfig.moduleConfigUrl
            HKLogUtil.v(HKHybird.TAG + ":" + moduleName, "下载配置文件 开始 当前版本=${moduleManager.currentConfig.moduleVersion}: $moduleConfigUrl , 当前线程:${Thread.currentThread().name}")
            HKHybird.configer?.invoke(moduleConfigUrl) { remoteConfig: HKHybirdModuleConfigModel? ->
                HKLogUtil.v(HKHybird.TAG + ":" + moduleName, "下载配置文件 ${if (remoteConfig == null) "失败" else "成功"} , 当前线程:${Thread.currentThread().name}")
                HKLogUtil.v(HKHybird.TAG + ":" + moduleName, "remoteConfig->")
                HKLogUtil.j(HKHybird.TAG + ":" + moduleName, HKJsonUtil.toJson(remoteConfig))
                if (remoteConfig != null) {
                    //1:正式包，所有机器可以拉取
                    //2:测试包，只要测试机器可以拉取
                    if (!remoteConfig.moduleDebug || (remoteConfig.moduleDebug && HKHybird.debug)) {
                        HKLogUtil.e(HKHybird.TAG + ":" + moduleName, "检测到该版本为正式版 或者当前为测试版本并且本机是测试机,可以执行更新操作")
                        val remoteVersion = remoteConfig.moduleVersion.toFloatOrNull()
                        val localVersion = moduleManager.currentConfig.moduleVersion.toFloatOrNull()
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
        return if (url.isNullOrBlank()) null else HKHybird.modules.values.firstOrNull { isMemberOfModule(it.currentConfig, url) }
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
