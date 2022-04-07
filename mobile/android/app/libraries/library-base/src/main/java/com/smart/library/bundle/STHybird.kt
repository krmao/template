package com.smart.library.bundle

import android.annotation.SuppressLint
import android.util.Log
import android.webkit.WebViewClient
import androidx.annotation.Keep
import com.smart.library.base.STApplicationVisibleChangedEvent
import com.smart.library.bundle.manager.STHybirdBundleInfoManager
import com.smart.library.bundle.manager.STHybirdDownloadManager
import com.smart.library.bundle.manager.STHybirdLifecycleManager
import com.smart.library.bundle.manager.STHybirdModuleManager
import com.smart.library.bundle.model.STHybirdModuleBundleModel
import com.smart.library.bundle.model.STHybirdModuleConfigModel
import com.smart.library.bundle.strategy.STHybirdInitStrategy
import com.smart.library.bundle.strategy.STHybirdUpdateStrategy
import com.smart.library.bundle.util.STHybirdUtil
import com.smart.library.util.STJsonUtil
import com.smart.library.util.STLogUtil
import com.smart.library.util.STTimeUtil
import com.smart.library.util.cache.STCacheManager
import com.smart.library.util.rx.RxBus
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Suppress("MemberVisibilityCanPrivate", "unused", "KDocUnresolvedReference", "MemberVisibilityCanBePrivate")
//@Keep
object STHybird {

    @JvmStatic
    internal val TAG: String = STHybird::class.java.simpleName

    @JvmStatic
    var debug = true
        private set

    @JvmStatic
    var enable = false
        private set

    @JvmStatic
    var enableCheckUpdate = false
        private set

    @JvmStatic
    val assetsDirName = "hybird"

    @JvmStatic
    var indexPath = "index.shtml"
        private set

    @JvmStatic
    val bundleSuffix = ".zip"

    @JvmStatic
    val configSuffix = ".json"

    @JvmStatic
    val localRootDir = STCacheManager.getFilesHotPatchChildDir(assetsDirName)

    @JvmStatic
    var initStrategy = STHybirdInitStrategy.LOCAL
        internal set

    @JvmStatic
    var downloader: ((downloadUrl: String, file: File?, callback: (File?) -> Unit?) -> Unit?)? = null
        internal set

    @JvmStatic
    var configer: ((configUrl: String, callback: (STHybirdModuleConfigModel?) -> Unit?) -> Unit?)? = null
        internal set

    @JvmStatic
    var allConfiger: ((allConfigUrl: String, callback: (configList: MutableList<STHybirdModuleConfigModel>?) -> Unit?) -> Unit?)? = null
        internal set

    @JvmStatic
    var allConfigUrl: String = ""
        internal set

    @JvmStatic
    var modules: ConcurrentHashMap<String, STHybirdModuleManager> = ConcurrentHashMap()
        internal set

    @JvmStatic
    fun removeModule(moduleName: String?) {
        if (!enable) {
            return
        }

        if (moduleName != null && moduleName.isNotBlank()) {
            STHybirdUtil.removeIntercept(modules[moduleName]?.currentConfig)
            modules.remove(moduleName)
        }
    }

    /**
     * debug true  的话代表是测试机,可以拉取动态更新的测试版本
     *       false 代表是已发布的正式的去打包，不能拉取动态更新的测试版本
     */
    @SuppressLint("CheckResult")
    @JvmStatic
    @JvmOverloads
    @Synchronized
    fun init(enable: Boolean = false, enableCheckUpdate: Boolean = false, debug: Boolean = true, initStrategy: STHybirdInitStrategy = STHybird.initStrategy, indexPath: String = STHybird.indexPath, allConfigUrl: String = "", allConfiger: ((configUrl: String, callback: (configList: MutableList<STHybirdModuleConfigModel>?) -> Unit?) -> Unit?)? = null, configer: ((configUrl: String, callback: (STHybirdModuleConfigModel?) -> Unit?) -> Unit?)? = null, downloader: ((downloadUrl: String, file: File?, callback: (File?) -> Unit?) -> Unit?)? = null, callback: ((configList: MutableList<STHybirdModuleConfigModel>?) -> Unit)? = null) {
        STHybird.enable = enable

        if (!enable) {
            callback?.invoke(null)
            return
        }

        val start = System.currentTimeMillis()
        STLogUtil.w(TAG, ">>>>----------------------------------------------------------------------")
        STLogUtil.w(TAG, ">>>>----初始化HYBIRD模块 开始")
        STLogUtil.w(TAG, ">>>>----------------------------------------------------------------------")


        STHybird.debug = debug
        STHybird.enableCheckUpdate = enableCheckUpdate
        STHybird.indexPath = indexPath
        STHybird.initStrategy = initStrategy

        if (configer != null) STHybird.configer = configer
        if (downloader != null) STHybird.downloader = downloader
        if (allConfiger != null) STHybird.allConfiger = allConfiger
        if (allConfigUrl.isNotBlank()) STHybird.allConfigUrl = allConfigUrl

        STLogUtil.w(TAG, ">>>>----enableLoadMore=${STHybird.enable}")
        STLogUtil.w(TAG, ">>>>----debug=${STHybird.debug}")
        STLogUtil.w(TAG, ">>>>----initStrategy=${STHybird.initStrategy}")
        STLogUtil.w(TAG, ">>>>----allConfigUrl=$allConfigUrl")
        STLogUtil.w(TAG, ">>>>----------------------------------------------------------------------")
        STLogUtil.w(TAG, ">>>>----------------------------------------------------------------------")

        /**
         * 关于保存配置信息的时机
         * 1: 为空的时候的第一次初始化
         */
        val bundles: MutableMap<String, STHybirdModuleBundleModel> = STHybirdBundleInfoManager.getBundles()
        val bundleNames: MutableSet<String> = bundles.keys

        STLogUtil.w(TAG, ">>>>----检测到当前初始化策略为: initStrategy=${STHybird.initStrategy}, 检测本地是否存在缓存配置信息: bundleNames=$bundleNames")
        if (bundleNames.isEmpty()) {
            if (STHybird.initStrategy == STHybirdInitStrategy.DOWNLOAD) {
                STLogUtil.w(TAG, ">>>>----由于未检测到缓存配置信息, 开始执行远程下载总配置信息进行初始化")
                if (STHybird.allConfiger != null && STHybird.allConfigUrl.isNotBlank()) {
                    STLogUtil.w(TAG, ">>>>----初始化策略为在线下载初始化, 开始下载总的配置文件, allConfigUrl=${STHybird.allConfigUrl}")
                    STHybird.allConfiger?.invoke(STHybird.allConfigUrl) { remoteConfigList: MutableList<STHybirdModuleConfigModel>? ->
                        STLogUtil.w(TAG, ">>>>----总的配置文件下载 ${if (remoteConfigList == null) "失败" else "成功"}")
                        STLogUtil.w(TAG, ">>>>----remoteConfigList->")
                        STLogUtil.j(Log.WARN, TAG, STJsonUtil.toJson(remoteConfigList))

                        downloadAndInitModules(remoteConfigList, callback)
                    }
                } else {
                    STLogUtil.w(TAG, ">>>>----检测到下载器 allConfiger==null?${STHybird.allConfiger == null} 尚未设置 或者 总配置信息的 allConfigUrl=$allConfigUrl 没有设置,return")
                }
            } else {
                STLogUtil.w(TAG, ">>>>----由于未检测到缓存配置信息, 开始执行从 assets 读取总配置信息进行初始化")
                STHybirdUtil.getConfigListFromAssetsWithCopyAndUnzip { configList ->
                    initAllModules(configList, true, callback)
                }
            }
        } else {
            STLogUtil.w(TAG, ">>>>----检测本地有缓存配置信息, 根据缓存配置信息初始化")
            initAllModules(bundles.values.mapNotNull { it.moduleConfigList.firstOrNull() }.toMutableList(), true, callback)
        }

        //应用程序前后台切换的时候执行一次异步检查更新
        RxBus.toObservable(STApplicationVisibleChangedEvent::class.java).subscribe { changeEvent ->
            //从前台切换到后台时
            if (!changeEvent.isApplicationVisible) {
                STLogUtil.e(TAG, ">>>>----系统监测到应用程序从前台切换到后台,执行一次异步的健康体检和检查更新")
                checkAllUpdate()
            }
        }

        STLogUtil.w(TAG, ">>>>----------------------------------------------------------------------")
        STLogUtil.w(TAG, ">>>>----初始化HYBIRD模块 结束  耗时: ${System.currentTimeMillis() - start}ms")
        STLogUtil.w(TAG, ">>>>----------------------------------------------------------------------")
    }

    private fun downloadAndInitModules(configList: MutableList<STHybirdModuleConfigModel>?, callback: ((configList: MutableList<STHybirdModuleConfigModel>?) -> Unit)? = null) {
        if (!enable) {
            callback?.invoke(null)
            return
        }

        STLogUtil.w(TAG, ">>>>----需要下载初始化的模块有 ${configList?.map { it.moduleName }}")
        STHybirdUtil.downloadAllModules(configList) { validConfigList: MutableList<STHybirdModuleConfigModel>? ->
            initAllModules(validConfigList, false, callback)
        }
    }

    private fun downloadAndInitModule(config: STHybirdModuleConfigModel?) {
        if (!enable) {
            return
        }

        if (config != null) {
            val start = System.currentTimeMillis()
            STLogUtil.d(TAG, "**********[downloadAndInitModule:单模块开始")

            STLogUtil.d(TAG, "**********[downloadAndInitModule:单模块下载:${config.moduleName}:开始], 当前线程:${Thread.currentThread().name}, 当前时间:${STTimeUtil.yMdHmsS(Date(start))}")
            STLogUtil.d(TAG, "**********[downloadAndInitModule:单模块下载:${config.moduleName}:开始], ${config.moduleDownloadUrl}")

            STHybirdDownloadManager.download(config) { isLocalFilesValid: Boolean ->
                STLogUtil.d(TAG, "**********[downloadAndInitModule:单模块下载:${config.moduleName}:结束], isLocalFilesValid:$isLocalFilesValid")
                STLogUtil.d(TAG, "**********[downloadAndInitModule:单模块下载:${config.moduleName}:结束], 当前线程:${Thread.currentThread().name}, 当前时间:${STTimeUtil.yMdHmsS(Date())}, 耗时:${System.currentTimeMillis() - start}ms, config=$config")
                if (isLocalFilesValid) {
                    initModule(config)
                }
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun initModule(config: STHybirdModuleConfigModel?, callback: ((config: STHybirdModuleConfigModel?) -> Unit)? = null) {
        if (!enable) {
            callback?.invoke(null)
            return
        }

        val start = System.currentTimeMillis()
        STLogUtil.e(TAG, "--[initModule:${config?.moduleName}初始化开始]-----------------------------------------------------------------------------------")
        STLogUtil.e(TAG, "--[initModule:${config?.moduleName}初始化开始], 当前线程:${Thread.currentThread().name}, 当前时间:${STTimeUtil.yMdHmsS(Date(start))}")
        if (config == null) {
            STLogUtil.e(TAG, "--[initModule:${config?.moduleName}初始化结束], 没有模块需要初始化")
            STLogUtil.e(TAG, "--[initModule:${config?.moduleName}初始化结束], 当前线程:${Thread.currentThread().name}, 当前时间:${STTimeUtil.yMdHmsS(Date())} ,最终成功初始化的模块:${modules.map { it.key }} , 一共耗时:${System.currentTimeMillis() - start}ms")
            STLogUtil.e(TAG, "--[initModule:${config?.moduleName}初始化结束]-----------------------------------------------------------------------------------")
            callback?.invoke(null)
        } else {
            Observable.fromCallable { initModuleManager(config) }
                .subscribeOn(Schedulers.io())
                .subscribe {
                    STLogUtil.e(TAG, "--[initModule:${config.moduleName}初始化结束], 当前线程:${Thread.currentThread().name}, 当前时间:${STTimeUtil.yMdHmsS(Date())} ,最终成功初始化的模块:${modules.map { it.key }} , 一共耗时:${System.currentTimeMillis() - start}ms")
                    STLogUtil.e(TAG, "--[initModule:${config.moduleName}初始化结束]-----------------------------------------------------------------------------------")
                    callback?.invoke(config)
                }
        }
    }

    private fun initModuleManager(config: STHybirdModuleConfigModel?) {
        if (!enable) {
            return
        }

        val start = System.currentTimeMillis()
        STLogUtil.w(TAG, "--[initModule:${config?.moduleName}](开始), 当前线程:${Thread.currentThread().name}, 当前时间:${STTimeUtil.yMdHmsS(Date(start))}")

        if (config != null) {
            val moduleManager = STHybirdModuleManager.create(config)
            if (moduleManager != null) {
                modules[config.moduleName] = moduleManager
                STHybirdBundleInfoManager.saveConfigToBundleByName(config.moduleName, config)
            }
        }
        STLogUtil.w(TAG, "--[initModule:${config?.moduleName}](结束), 当前线程:${Thread.currentThread().name}, 当前时间:${STTimeUtil.yMdHmsS(Date())} , 一共耗时:${System.currentTimeMillis() - start}ms")
    }

    /**
     * @param callback 在全部模块成功初始化结束以后,检查全部更新之前回调, configList 返回空,代表没有模块被成功初始化, 属于初始化失败的标志
     */
    @SuppressLint("CheckResult")
    private fun initAllModules(configList: MutableList<STHybirdModuleConfigModel>?, isNeedCheckAllUpdateAfterAllModulesSuccessInit: Boolean = true, callback: ((configList: MutableList<STHybirdModuleConfigModel>?) -> Unit)? = null) {
        if (!enable) {
            callback?.invoke(null)
            return
        }

        val start = System.currentTimeMillis()
        STLogUtil.e(TAG, "--[initAllModules:全部初始化开始]-----------------------------------------------------------------------------------")
        STLogUtil.e(TAG, "--[initAllModules:全部初始化开始], 当前线程:${Thread.currentThread().name}, 当前时间:${STTimeUtil.yMdHmsS(Date(start))}  ,isNeedCheckAllUpdateAfterAllModulesSuccessInit=$isNeedCheckAllUpdateAfterAllModulesSuccessInit")
        if (configList == null || configList.isEmpty()) {
            STLogUtil.e(TAG, "--[initAllModules:全部初始化结束], 没有模块需要初始化")
            STLogUtil.e(TAG, "--[initAllModules:全部初始化结束], 当前线程:${Thread.currentThread().name}, 当前时间:${STTimeUtil.yMdHmsS(Date())} ,最终成功初始化的模块:${modules.map { it.key }} , 一共耗时:${System.currentTimeMillis() - start}ms")
            STLogUtil.e(TAG, "--[initAllModules:全部初始化结束]-----------------------------------------------------------------------------------")
            callback?.invoke(null)
        } else {
            Observable.zip(
                configList.map { config ->
                    Observable.fromCallable { initModuleManager(config) }.subscribeOn(Schedulers.io())
                },
                ({
                })
            ).subscribe {
                STLogUtil.e(TAG, "--[initAllModules:全部初始化结束], 当前线程:${Thread.currentThread().name}, 当前时间:${STTimeUtil.yMdHmsS(Date())} ,最终成功初始化的模块:${modules.map { it.key }} , 一共耗时:${System.currentTimeMillis() - start}ms")
                STLogUtil.e(TAG, "--[initAllModules:全部初始化结束]-----------------------------------------------------------------------------------")

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
    private fun checkAllUpdate() {
        if (!enable || !enableCheckUpdate) {
            return
        }

        val start = System.currentTimeMillis()
        STLogUtil.e(TAG, ">>>>----检查更新:下载开始, 当前线程:${Thread.currentThread().name}, 当前时间:${STTimeUtil.yMdHmsS(Date(start))}")
        allConfiger?.invoke(allConfigUrl) { remoteConfigList: MutableList<STHybirdModuleConfigModel>? ->
            STLogUtil.w(TAG, ">>>>----检查更新:下载${if (remoteConfigList == null) "失败" else "成功"}, 当前时间:${STTimeUtil.yMdHmsS(Date())}, 耗时:${System.currentTimeMillis() - start}ms")
            STLogUtil.w(TAG, ">>>>----remoteConfigList->")
            STLogUtil.j(Log.WARN, TAG, STJsonUtil.toJson(remoteConfigList))

            val needDownloadAndInitConfigList: MutableList<STHybirdModuleConfigModel> = mutableListOf()

            remoteConfigList?.forEach {
                if (modules.containsKey(it.moduleName)) {
                    STLogUtil.w(TAG, ">>>>----检测模块${it.moduleName}已经被初始化过, 走更新流程")
                    checkUpdate(it)
                } else if (initStrategy == STHybirdInitStrategy.DOWNLOAD) {
                    STLogUtil.w(TAG, ">>>>----检测模块${it.moduleName}已经尚未被初始化过, 且当前初始化策略为在线下载初始化, 走下载初始化流程")
                    needDownloadAndInitConfigList.add(it)
                }
            }

            if (needDownloadAndInitConfigList.isNotEmpty()) {
                downloadAndInitModules(needDownloadAndInitConfigList)
            }

            STLogUtil.w(TAG, ">>>>----检查更新:结束, 当前时间:${STTimeUtil.yMdHmsS(Date())}, 耗时:${System.currentTimeMillis() - start}ms")
        }
    }

    private fun checkUpdate(remoteConfig: STHybirdModuleConfigModel?) {
        if (!enable || !enableCheckUpdate) {
            return
        }

        STLogUtil.e(TAG, ">>>>>>>======检查子模块更新 开始:${remoteConfig?.moduleName}, 当前时间:${STTimeUtil.yMdHmsS(Date())}")

        if (remoteConfig != null) {
            val moduleManager = modules[remoteConfig.moduleName]
            if (moduleManager != null) {
                STLogUtil.e(TAG, ">>>>>>>======检查子模块本地已经有以前的版本信息,进行比较操作")
                //1:正式包，所有机器可以拉取
                //2:测试包，只要测试机器可以拉取
                if (!remoteConfig.moduleDebug || (remoteConfig.moduleDebug && debug)) {
                    STLogUtil.e(remoteConfig.moduleName, "检测到该版本为正式版 或者当前为测试版本并且本机是测试机,可以执行更新操作")
                    val remoteVersion = remoteConfig.moduleVersion.toFloatOrNull()
                    val localVersion = moduleManager.currentConfig.moduleVersion.toFloatOrNull()
                    STLogUtil.v(TAG, ">>>>>>>======${remoteConfig.moduleName} 当前版本:$localVersion   远程版本:$remoteVersion")
                    if (remoteVersion != null && localVersion != null) {
                        //版本号相等时不做任何处理，避免不必要的麻烦
                        if (remoteVersion != localVersion) {
                            STLogUtil.v(TAG, ">>>>>>>======${remoteConfig.moduleName} 系统检测到有新版本")

                            if (remoteConfig.moduleUpdateStrategy == STHybirdUpdateStrategy.ONLINE) {
                                moduleManager.onlineModel = true
                            } else {
                                STLogUtil.e(TAG, ">>>>>>>======${remoteConfig.moduleName} 无需切换为在线模式")
                            }
                            STHybirdDownloadManager.download(remoteConfig) {
                            }
                        } else {
                            STLogUtil.v(TAG, ">>>>>>>======${remoteConfig.moduleName} 系统检测到 remoteVersion:$remoteVersion 或者 localVersion:$localVersion 相等, 无需更新")
                        }
                    } else {
                        STLogUtil.e(TAG, ">>>>>>>======${remoteConfig.moduleName} 系统检测到 remoteVersion:$remoteVersion 或者 localVersion:$localVersion 为空, 无法判断需要更新,默认不需要更新")
                    }
                } else {
                    STLogUtil.e(TAG, ">>>>>>>======${remoteConfig.moduleName} 检测到该版本为调试版本且本机不是测试机,不执行更新操作 return false")
                }
            }
        }
        STLogUtil.e(TAG, ">>>>>>>======检查子模块结束 结束:${remoteConfig?.moduleName}, 当前时间:${STTimeUtil.yMdHmsS(Date())}")
    }

    @JvmStatic
    fun checkUpdate(url: String?, callback: (() -> Unit?)? = null) {
        if (!enable || !enableCheckUpdate) {
            callback?.invoke()
            return
        }

        val moduleManager = getModule(url)
        if (moduleManager == null) {
            callback?.invoke()
        } else {
            checkUpdate(moduleManager, callback)
        }
    }

    @JvmStatic
    private fun checkUpdate(moduleManager: STHybirdModuleManager?, callback: (() -> Unit?)? = null) {
        if (!enable || !enableCheckUpdate) {
            callback?.invoke()
            return
        }

        if (moduleManager != null) {
            val moduleName = moduleManager.currentConfig.moduleName
            val start = System.currentTimeMillis()
            STLogUtil.v("$TAG:$moduleName", "系统检测更新(同步) 开始 当前版本=${moduleManager.currentConfig.moduleVersion},当前线程:${Thread.currentThread().name}")
            STLogUtil.v("$TAG:$moduleName", "当前配置=${moduleManager.currentConfig}")

            if (configer == null) {
                STLogUtil.e("$TAG:$moduleName", "系统检测到尚未配置 config 下载器，请先设置 config 下载器, return")
                callback?.invoke()
                return
            }

            if (STHybirdDownloadManager.isDownloading(moduleManager.currentConfig)) {
                STLogUtil.e("$TAG:$moduleName", "系统检测到当前正在下载更新中, return")
                callback?.invoke()
                return
            }

            if (moduleManager.onlineModel) {
                STLogUtil.e("$TAG:$moduleName", "系统检测到当前已经是在线状态了,无需重复检测 return")
                callback?.invoke()
                return
            }

            val moduleConfigUrl = moduleManager.currentConfig.moduleConfigUrl
            STLogUtil.v("$TAG:$moduleName", "下载配置文件 开始 当前版本=${moduleManager.currentConfig.moduleVersion}: $moduleConfigUrl , 当前线程:${Thread.currentThread().name}")
            configer?.invoke(moduleConfigUrl) { remoteConfig: STHybirdModuleConfigModel? ->
                STLogUtil.v("$TAG:$moduleName", "下载配置文件 ${if (remoteConfig == null) "失败" else "成功"} , 当前线程:${Thread.currentThread().name}")
                STLogUtil.v("$TAG:$moduleName", "remoteConfig->")
                STLogUtil.j("$TAG:$moduleName", STJsonUtil.toJson(remoteConfig))
                if (remoteConfig != null) {
                    //1:正式包，所有机器可以拉取
                    //2:测试包，只要测试机器可以拉取
                    if (!remoteConfig.moduleDebug || (remoteConfig.moduleDebug && debug)) {
                        STLogUtil.e("$TAG:$moduleName", "检测到该版本为正式版 或者当前为测试版本并且本机是测试机,可以执行更新操作")
                        val remoteVersion = remoteConfig.moduleVersion.toFloatOrNull()
                        val localVersion = moduleManager.currentConfig.moduleVersion.toFloatOrNull()
                        STLogUtil.v("${"$TAG:$moduleName"} 当前版本:$localVersion   远程版本:$remoteVersion")
                        if (remoteVersion != null && localVersion != null) {
                            //版本号相等时不做任何处理，避免不必要的麻烦
                            if (remoteVersion != localVersion) {
                                STLogUtil.v("系统检测到有新版本")
                                if (remoteConfig.moduleUpdateStrategy == STHybirdUpdateStrategy.ONLINE) {
                                    moduleManager.onlineModel = true
                                } else {
                                    STLogUtil.e("无需切换为在线模式")
                                }
                                STHybirdDownloadManager.download(remoteConfig)
                            } else {
                                STLogUtil.v("系统检测到 remoteVersion:$remoteVersion 或者 localVersion:$localVersion 相等, 无需更新")
                            }
                        } else {
                            STLogUtil.e("系统检测到 remoteVersion:$remoteVersion 或者 localVersion:$localVersion 为空, 无法判断需要更新,默认不需要更新")
                        }
                    } else {
                        STLogUtil.e("$TAG:$moduleName", "检测到该版本为调试版本且本机不是测试机,不执行更新操作 return false")
                    }
                }
                callback?.invoke()
            }
            STLogUtil.v("$TAG:$moduleName", "检查更新 结束, 当前线程:${Thread.currentThread().name}, 耗时: ${System.currentTimeMillis() - start}ms")
        }
    }

    fun getModule(url: String?): STHybirdModuleManager? {
        if (!enable) {
            return null
        }

        return if (url.isNullOrBlank()) null else modules.values.firstOrNull { isMemberOfModule(it.currentConfig, url) }
    }

    @JvmStatic
    fun onWebViewClose(webViewClient: WebViewClient?) {
        if (!enable) {
            return
        }

        STHybirdLifecycleManager.onWebViewClose(webViewClient)
    }

    @JvmStatic
    fun onWebViewOpenPage(webViewClient: WebViewClient?, url: String?) {
        if (!enable) {
            return
        }

        STHybirdLifecycleManager.onWebViewOpenPage(webViewClient, url)
    }

    @JvmStatic
    fun isMemberOfModule(config: STHybirdModuleConfigModel?, url: String?): Boolean {
        if (!enable) {
            return false
        }

        return url?.contains(config?.moduleMainUrl ?: "") == true
    }

    @JvmStatic
    fun isModuleOpened(moduleName: String?): Boolean {
        if (!enable) {
            return false
        }

        return STHybirdLifecycleManager.isModuleOpened(moduleName)
    }
}
