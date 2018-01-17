package com.smart.library.bundle

import android.util.Log
import android.webkit.WebViewClient
import com.smart.library.base.CXApplicationVisibleChangedEvent
import com.smart.library.bundle.manager.CXHybirdBundleInfoManager
import com.smart.library.bundle.manager.CXHybirdDownloadManager
import com.smart.library.bundle.manager.CXHybirdLifecycleManager
import com.smart.library.bundle.manager.CXHybirdModuleManager
import com.smart.library.bundle.model.CXHybirdModuleBundleModel
import com.smart.library.bundle.model.CXHybirdModuleConfigModel
import com.smart.library.bundle.strategy.CXHybirdInitStrategy
import com.smart.library.bundle.strategy.CXHybirdUpdateStrategy
import com.smart.library.bundle.util.CXHybirdUtil
import com.smart.library.util.CXJsonUtil
import com.smart.library.util.CXLogUtil
import com.smart.library.util.CXTimeUtil
import com.smart.library.util.cache.CXCacheManager
import com.smart.library.util.rx.RxBus
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Suppress("MemberVisibilityCanPrivate", "unused", "KDocUnresolvedReference")
object CXHybird {

    @JvmStatic
    internal val TAG: String = CXHybird::class.java.simpleName

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
    val localRootDir = CXCacheManager.getChildCacheDir(assetsDirName)

    @JvmStatic
    var initStrategy = CXHybirdInitStrategy.LOCAL
        internal set

    @JvmStatic
    var downloader: ((downloadUrl: String, file: File?, callback: (File?) -> Unit?) -> Unit?)? = null
        internal set
    @JvmStatic
    var configer: ((configUrl: String, callback: (CXHybirdModuleConfigModel?) -> Unit?) -> Unit?)? = null
        internal set
    @JvmStatic
    var allConfiger: ((allConfigUrl: String, callback: (configList: MutableList<CXHybirdModuleConfigModel>?) -> Unit?) -> Unit?)? = null
        internal set
    @JvmStatic
    var allConfigUrl: String = ""
        internal set

    @JvmStatic
    var modules: ConcurrentHashMap<String, CXHybirdModuleManager> = ConcurrentHashMap()
        internal set

    @JvmStatic
    fun removeModule(moduleName: String?) {
        if (moduleName != null && moduleName.isNotBlank()) {
            CXHybirdUtil.removeIntercept(CXHybird.modules[moduleName]?.currentConfig)
            CXHybird.modules.remove(moduleName)
        }
    }

    /**
     * debug true  的话代表是测试机,可以拉取动态更新的测试版本
     *       false 代表是已发布的正式的去打包，不能拉取动态更新的测试版本
     */
    @JvmStatic
    @JvmOverloads
    @Synchronized
    fun init(debug: Boolean = true, initStrategy: CXHybirdInitStrategy = CXHybird.initStrategy, allConfigUrl: String = "", allConfiger: ((configUrl: String, callback: (configList: MutableList<CXHybirdModuleConfigModel>?) -> Unit?) -> Unit?)? = null, configer: ((configUrl: String, callback: (CXHybirdModuleConfigModel?) -> Unit?) -> Unit?)? = null, downloader: ((downloadUrl: String, file: File?, callback: (File?) -> Unit?) -> Unit?)? = null) {
        val start = System.currentTimeMillis()
        CXLogUtil.w(TAG, ">>>>----------------------------------------------------------------------")
        CXLogUtil.w(TAG, ">>>>----初始化HYBIRD模块 开始")
        CXLogUtil.w(TAG, ">>>>----------------------------------------------------------------------")

        this.debug = debug
        this.initStrategy = initStrategy

        if (configer != null) CXHybird.configer = configer
        if (downloader != null) CXHybird.downloader = downloader
        if (allConfiger != null) CXHybird.allConfiger = allConfiger
        if (allConfigUrl.isNotBlank()) CXHybird.allConfigUrl = allConfigUrl

        CXLogUtil.w(TAG, ">>>>----debug=${this.debug}")
        CXLogUtil.w(TAG, ">>>>----initStrategy=${this.initStrategy}")
        CXLogUtil.w(TAG, ">>>>----allConfigUrl=$allConfigUrl")
        CXLogUtil.w(TAG, ">>>>----------------------------------------------------------------------")
        CXLogUtil.w(TAG, ">>>>----------------------------------------------------------------------")

        /**
         * 关于保存配置信息的时机
         * 1: 为空的时候的第一次初始化
         */
        val bundles: MutableMap<String, CXHybirdModuleBundleModel> = CXHybirdBundleInfoManager.getBundles()
        val bundleNames: MutableSet<String> = bundles.keys

        CXLogUtil.w(TAG, ">>>>----检测到当前初始化策略为: initStrategy=${this.initStrategy}, 检测本地是否存在缓存配置信息: bundleNames=$bundleNames")
        if (bundleNames.isEmpty()) {
            if (this.initStrategy == CXHybirdInitStrategy.DOWNLOAD) {
                CXLogUtil.w(TAG, ">>>>----由于未检测到缓存配置信息, 开始执行远程下载总配置信息进行初始化")
                if (CXHybird.allConfiger != null && CXHybird.allConfigUrl.isNotBlank()) {
                    CXLogUtil.w(TAG, ">>>>----初始化策略为在线下载初始化, 开始下载总的配置文件, allConfigUrl=${CXHybird.allConfigUrl}")
                    CXHybird.allConfiger?.invoke(CXHybird.allConfigUrl) { remoteConfigList: MutableList<CXHybirdModuleConfigModel>? ->
                        CXLogUtil.w(TAG, ">>>>----总的配置文件下载 ${if (remoteConfigList == null) "失败" else "成功"}")
                        CXLogUtil.w(TAG, ">>>>----remoteConfigList->")
                        CXLogUtil.j(Log.WARN, TAG, CXJsonUtil.toJson(remoteConfigList))

                        downloadAndInitModules(remoteConfigList)
                    }
                } else {
                    CXLogUtil.w(TAG, ">>>>----检测到下载器 allConfiger==null?${CXHybird.allConfiger == null} 尚未设置 或者 总配置信息的 allConfigUrl=$allConfigUrl 没有设置,return")
                }
            } else {
                CXLogUtil.w(TAG, ">>>>----由于未检测到缓存配置信息, 开始执行从 assets 读取总配置信息进行初始化")
                CXHybirdUtil.getConfigListFromAssetsWithCopyAndUnzip { configList ->
                    initAllModules(configList, true)
                }
            }
        } else {
            CXLogUtil.w(TAG, ">>>>----检测本地有缓存配置信息, 根据缓存配置信息初始化")
            initAllModules(bundles.values.mapNotNull { it.moduleConfigList.firstOrNull() }.toMutableList(), true)
        }

        //应用程序前后台切换的时候执行一次异步检查更新
        RxBus.toObservable(CXApplicationVisibleChangedEvent::class.java).subscribe { changeEvent ->
            //从前台切换到后台时
            if (!changeEvent.isApplicationVisible) {
                CXLogUtil.e(TAG, ">>>>----系统监测到应用程序从前台切换到后台,执行一次异步的健康体检和检查更新")
                checkAllUpdate()
            }
        }

        CXLogUtil.w(TAG, ">>>>----------------------------------------------------------------------")
        CXLogUtil.w(TAG, ">>>>----初始化HYBIRD模块 结束  耗时: ${System.currentTimeMillis() - start}ms")
        CXLogUtil.w(TAG, ">>>>----------------------------------------------------------------------")
    }

    private fun downloadAndInitModules(configList: MutableList<CXHybirdModuleConfigModel>?) {
        CXLogUtil.w(TAG, ">>>>----需要下载初始化的模块有 ${configList?.map { it.moduleName }}")
        CXHybirdUtil.downloadAllModules(configList) { validConfigList: MutableList<CXHybirdModuleConfigModel>? ->
            initAllModules(validConfigList, false)
        }
    }

    private fun downloadAndInitModule(config: CXHybirdModuleConfigModel?) {
        if (config != null) {
            val start = System.currentTimeMillis()
            CXLogUtil.d(CXHybird.TAG, "**********[downloadAndInitModule:单模块开始")

            CXLogUtil.d(CXHybird.TAG, "**********[downloadAndInitModule:单模块下载:${config.moduleName}:开始], 当前线程:${Thread.currentThread().name}, 当前时间:${CXTimeUtil.yMdHmsS(Date(start))}")
            CXLogUtil.d(CXHybird.TAG, "**********[downloadAndInitModule:单模块下载:${config.moduleName}:开始], ${config.moduleDownloadUrl}")

            CXHybirdDownloadManager.download(config) { isLocalFilesValid: Boolean ->
                CXLogUtil.d(CXHybird.TAG, "**********[downloadAndInitModule:单模块下载:${config.moduleName}:结束], isLocalFilesValid:$isLocalFilesValid")
                CXLogUtil.d(CXHybird.TAG, "**********[downloadAndInitModule:单模块下载:${config.moduleName}:结束], 当前线程:${Thread.currentThread().name}, 当前时间:${CXTimeUtil.yMdHmsS(Date())}, 耗时:${System.currentTimeMillis() - start}ms, config=$config")
                if (isLocalFilesValid) {
                    initModule(config)
                }
            }
        }
    }

    private fun initModule(config: CXHybirdModuleConfigModel?, callback: ((config: CXHybirdModuleConfigModel?) -> Unit)? = null) {
        val start = System.currentTimeMillis()
        CXLogUtil.e(TAG, "--[initModule:${config?.moduleName}初始化开始]-----------------------------------------------------------------------------------")
        CXLogUtil.e(TAG, "--[initModule:${config?.moduleName}初始化开始], 当前线程:${Thread.currentThread().name}, 当前时间:${CXTimeUtil.yMdHmsS(Date(start))}")
        if (config == null) {
            CXLogUtil.e(CXHybird.TAG, "--[initModule:${config?.moduleName}初始化结束], 没有模块需要初始化")
            CXLogUtil.e(CXHybird.TAG, "--[initModule:${config?.moduleName}初始化结束], 当前线程:${Thread.currentThread().name}, 当前时间:${CXTimeUtil.yMdHmsS(Date())} ,最终成功初始化的模块:${CXHybird.modules.map { it.key }} , 一共耗时:${System.currentTimeMillis() - start}ms")
            CXLogUtil.e(CXHybird.TAG, "--[initModule:${config?.moduleName}初始化结束]-----------------------------------------------------------------------------------")
            callback?.invoke(null)
        } else {
            Observable.fromCallable { initModuleManager(config) }
                .subscribeOn(Schedulers.io())
                .subscribe {
                    CXLogUtil.e(TAG, "--[initModule:${config.moduleName}初始化结束], 当前线程:${Thread.currentThread().name}, 当前时间:${CXTimeUtil.yMdHmsS(Date())} ,最终成功初始化的模块:${modules.map { it.key }} , 一共耗时:${System.currentTimeMillis() - start}ms")
                    CXLogUtil.e(TAG, "--[initModule:${config.moduleName}初始化结束]-----------------------------------------------------------------------------------")
                    callback?.invoke(config)
                }
        }
    }

    private fun initModuleManager(config: CXHybirdModuleConfigModel?) {
        val _start = System.currentTimeMillis()
        CXLogUtil.w(TAG, "--[initModule:${config?.moduleName}](开始), 当前线程:${Thread.currentThread().name}, 当前时间:${CXTimeUtil.yMdHmsS(Date(_start))}")

        if (config != null) {
            val moduleManager = CXHybirdModuleManager.create(config)
            if (moduleManager != null) {
                modules[config.moduleName] = moduleManager
                CXHybirdBundleInfoManager.saveConfigToBundleByName(config.moduleName, config)
            }
        }
        CXLogUtil.w(TAG, "--[initModule:${config?.moduleName}](结束), 当前线程:${Thread.currentThread().name}, 当前时间:${CXTimeUtil.yMdHmsS(Date())} , 一共耗时:${System.currentTimeMillis() - _start}ms")
    }

    /**
     * @param callback 在全部模块成功初始化结束以后,检查全部更新之前回调, configList 返回空,代表没有模块被成功初始化, 属于初始化失败的标志
     */
    private fun initAllModules(configList: MutableList<CXHybirdModuleConfigModel>?, isNeedCheckAllUpdateAfterAllModulesSuccessInit: Boolean = true, callback: ((configList: MutableList<CXHybirdModuleConfigModel>?) -> Unit)? = null) {
        val start = System.currentTimeMillis()
        CXLogUtil.e(TAG, "--[initAllModules:全部初始化开始]-----------------------------------------------------------------------------------")
        CXLogUtil.e(TAG, "--[initAllModules:全部初始化开始], 当前线程:${Thread.currentThread().name}, 当前时间:${CXTimeUtil.yMdHmsS(Date(start))}  ,isNeedCheckAllUpdateAfterAllModulesSuccessInit=$isNeedCheckAllUpdateAfterAllModulesSuccessInit")
        if (configList == null || configList.isEmpty()) {
            CXLogUtil.e(CXHybird.TAG, "--[initAllModules:全部初始化结束], 没有模块需要初始化")
            CXLogUtil.e(CXHybird.TAG, "--[initAllModules:全部初始化结束], 当前线程:${Thread.currentThread().name}, 当前时间:${CXTimeUtil.yMdHmsS(Date())} ,最终成功初始化的模块:${CXHybird.modules.map { it.key }} , 一共耗时:${System.currentTimeMillis() - start}ms")
            CXLogUtil.e(CXHybird.TAG, "--[initAllModules:全部初始化结束]-----------------------------------------------------------------------------------")
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


                CXLogUtil.e(TAG, "--[initAllModules:全部初始化结束], 当前线程:${Thread.currentThread().name}, 当前时间:${CXTimeUtil.yMdHmsS(Date())} ,最终成功初始化的模块:${modules.map { it.key }} , 一共耗时:${System.currentTimeMillis() - start}ms")
                CXLogUtil.e(TAG, "--[initAllModules:全部初始化结束]-----------------------------------------------------------------------------------")

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
        CXLogUtil.e(TAG, ">>>>----检查更新:下载开始, 当前线程:${Thread.currentThread().name}, 当前时间:${CXTimeUtil.yMdHmsS(Date(start))}")
        CXHybird.allConfiger?.invoke(CXHybird.allConfigUrl) { remoteConfigList: MutableList<CXHybirdModuleConfigModel>? ->
            CXLogUtil.w(TAG, ">>>>----检查更新:下载${if (remoteConfigList == null) "失败" else "成功"}, 当前时间:${CXTimeUtil.yMdHmsS(Date())}, 耗时:${System.currentTimeMillis() - start}ms")
            CXLogUtil.w(TAG, ">>>>----remoteConfigList->")
            CXLogUtil.j(Log.WARN, TAG, CXJsonUtil.toJson(remoteConfigList))

            val needDownloadAndInitConfigList: MutableList<CXHybirdModuleConfigModel> = mutableListOf()

            remoteConfigList?.forEach {
                if (modules.containsKey(it.moduleName)) {
                    CXLogUtil.w(TAG, ">>>>----检测模块${it.moduleName}已经被初始化过, 走更新流程")
                    checkUpdate(it)
                } else if (initStrategy == CXHybirdInitStrategy.DOWNLOAD) {
                    CXLogUtil.w(TAG, ">>>>----检测模块${it.moduleName}已经尚未被初始化过, 且当前初始化策略为在线下载初始化, 走下载初始化流程")
                    needDownloadAndInitConfigList.add(it)
                }
            }

            if (needDownloadAndInitConfigList.isNotEmpty()) {
                downloadAndInitModules(needDownloadAndInitConfigList)
            }

            CXLogUtil.w(TAG, ">>>>----检查更新:结束, 当前时间:${CXTimeUtil.yMdHmsS(Date())}, 耗时:${System.currentTimeMillis() - start}ms")
        }
    }

    fun checkUpdate(remoteConfig: CXHybirdModuleConfigModel?) {
        CXLogUtil.e(TAG, ">>>>>>>======检查子模块更新 开始:${remoteConfig?.moduleName}, 当前时间:${CXTimeUtil.yMdHmsS(Date())}")

        if (remoteConfig != null) {
            val moduleManager = modules[remoteConfig.moduleName]
            if (moduleManager != null) {
                CXLogUtil.e(TAG, ">>>>>>>======检查子模块本地已经有以前的版本信息,进行比较操作")
                //1:正式包，所有机器可以拉取
                //2:测试包，只要测试机器可以拉取
                if (!remoteConfig.moduleDebug || (remoteConfig.moduleDebug && CXHybird.debug)) {
                    CXLogUtil.e(remoteConfig.moduleName, "检测到该版本为正式版 或者当前为测试版本并且本机是测试机,可以执行更新操作")
                    val remoteVersion = remoteConfig.moduleVersion.toFloatOrNull()
                    val localVersion = moduleManager.currentConfig.moduleVersion.toFloatOrNull()
                    CXLogUtil.v(TAG, ">>>>>>>======${remoteConfig.moduleName} 当前版本:$localVersion   远程版本:$remoteVersion")
                    if (remoteVersion != null && localVersion != null) {
                        //版本号相等时不做任何处理，避免不必要的麻烦
                        if (remoteVersion != localVersion) {
                            CXLogUtil.v(TAG, ">>>>>>>======${remoteConfig.moduleName} 系统检测到有新版本")

                            if (remoteConfig.moduleUpdateStrategy == CXHybirdUpdateStrategy.ONLINE) {
                                moduleManager.onlineModel = true
                            } else {
                                CXLogUtil.e(TAG, ">>>>>>>======${remoteConfig.moduleName} 无需切换为在线模式")
                            }
                            CXHybirdDownloadManager.download(remoteConfig) { isLocalFilesValid ->

                            }
                        } else {
                            CXLogUtil.v(TAG, ">>>>>>>======${remoteConfig.moduleName} 系统检测到 remoteVersion:$remoteVersion 或者 localVersion:$localVersion 相等, 无需更新")
                        }
                    } else {
                        CXLogUtil.e(TAG, ">>>>>>>======${remoteConfig.moduleName} 系统检测到 remoteVersion:$remoteVersion 或者 localVersion:$localVersion 为空, 无法判断需要更新,默认不需要更新")
                    }
                } else {
                    CXLogUtil.e(TAG, ">>>>>>>======${remoteConfig.moduleName} 检测到该版本为调试版本且本机不是测试机,不执行更新操作 return false")
                }
            }
        }
        CXLogUtil.e(TAG, ">>>>>>>======检查子模块结束 结束:${remoteConfig?.moduleName}, 当前时间:${CXTimeUtil.yMdHmsS(Date())}")
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
    fun checkUpdate(moduleManager: CXHybirdModuleManager?, callback: (() -> Unit?)? = null) {
        if (moduleManager != null) {
            val moduleName = moduleManager.currentConfig.moduleName
            val start = System.currentTimeMillis()
            CXLogUtil.v(CXHybird.TAG + ":" + moduleName, "系统检测更新(同步) 开始 当前版本=${moduleManager.currentConfig.moduleVersion},当前线程:${Thread.currentThread().name}")
            CXLogUtil.v(CXHybird.TAG + ":" + moduleName, "当前配置=${moduleManager.currentConfig}")

            if (CXHybird.configer == null) {
                CXLogUtil.e(CXHybird.TAG + ":" + moduleName, "系统检测到尚未配置 config 下载器，请先设置 config 下载器, return")
                callback?.invoke()
                return
            }

            if (CXHybirdDownloadManager.isDownloading(moduleManager.currentConfig)) {
                CXLogUtil.e(CXHybird.TAG + ":" + moduleName, "系统检测到当前正在下载更新中, return")
                callback?.invoke()
                return
            }

            if (moduleManager.onlineModel) {
                CXLogUtil.e(CXHybird.TAG + ":" + moduleName, "系统检测到当前已经是在线状态了,无需重复检测 return")
                callback?.invoke()
                return
            }

            val moduleConfigUrl = moduleManager.currentConfig.moduleConfigUrl
            CXLogUtil.v(CXHybird.TAG + ":" + moduleName, "下载配置文件 开始 当前版本=${moduleManager.currentConfig.moduleVersion}: $moduleConfigUrl , 当前线程:${Thread.currentThread().name}")
            CXHybird.configer?.invoke(moduleConfigUrl) { remoteConfig: CXHybirdModuleConfigModel? ->
                CXLogUtil.v(CXHybird.TAG + ":" + moduleName, "下载配置文件 ${if (remoteConfig == null) "失败" else "成功"} , 当前线程:${Thread.currentThread().name}")
                CXLogUtil.v(CXHybird.TAG + ":" + moduleName, "remoteConfig->")
                CXLogUtil.j(CXHybird.TAG + ":" + moduleName, CXJsonUtil.toJson(remoteConfig))
                if (remoteConfig != null) {
                    //1:正式包，所有机器可以拉取
                    //2:测试包，只要测试机器可以拉取
                    if (!remoteConfig.moduleDebug || (remoteConfig.moduleDebug && CXHybird.debug)) {
                        CXLogUtil.e(CXHybird.TAG + ":" + moduleName, "检测到该版本为正式版 或者当前为测试版本并且本机是测试机,可以执行更新操作")
                        val remoteVersion = remoteConfig.moduleVersion.toFloatOrNull()
                        val localVersion = moduleManager.currentConfig.moduleVersion.toFloatOrNull()
                        CXLogUtil.v("${CXHybird.TAG + ":" + moduleName} 当前版本:$localVersion   远程版本:$remoteVersion")
                        if (remoteVersion != null && localVersion != null) {
                            //版本号相等时不做任何处理，避免不必要的麻烦
                            if (remoteVersion != localVersion) {
                                CXLogUtil.v("系统检测到有新版本")
                                if (remoteConfig.moduleUpdateStrategy == CXHybirdUpdateStrategy.ONLINE) {
                                    moduleManager.onlineModel = true
                                } else {
                                    CXLogUtil.e("无需切换为在线模式")
                                }
                                CXHybirdDownloadManager.download(remoteConfig)
                            } else {
                                CXLogUtil.v("系统检测到 remoteVersion:$remoteVersion 或者 localVersion:$localVersion 相等, 无需更新")
                            }
                        } else {
                            CXLogUtil.e("系统检测到 remoteVersion:$remoteVersion 或者 localVersion:$localVersion 为空, 无法判断需要更新,默认不需要更新")
                        }
                    } else {
                        CXLogUtil.e(CXHybird.TAG + ":" + moduleName, "检测到该版本为调试版本且本机不是测试机,不执行更新操作 return false")
                    }
                }
                callback?.invoke()
            }
            CXLogUtil.v(CXHybird.TAG + ":" + moduleName, "检查更新 结束, 当前线程:${Thread.currentThread().name}, 耗时: ${System.currentTimeMillis() - start}ms")
        }
    }

    fun getModule(url: String?): CXHybirdModuleManager? {
        return if (url.isNullOrBlank()) null else CXHybird.modules.values.firstOrNull { isMemberOfModule(it.currentConfig, url) }
    }

    @JvmStatic
    fun onWebViewClose(webViewClient: WebViewClient?) {
        CXHybirdLifecycleManager.onWebViewClose(webViewClient)
    }

    @JvmStatic
    fun onWebViewOpenPage(webViewClient: WebViewClient?, url: String?) {
        CXHybirdLifecycleManager.onWebViewOpenPage(webViewClient, url)
    }

    @JvmStatic
    fun isMemberOfModule(config: CXHybirdModuleConfigModel?, url: String?): Boolean {
        return url?.contains(config?.moduleMainUrl ?: "") == true
    }

    @JvmStatic
    fun isModuleOpened(moduleName: String?): Boolean {
        return CXHybirdLifecycleManager.isModuleOpened(moduleName)
    }

}
