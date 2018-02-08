import RxCocoa
import RxSwift
import Foundation

class CXHybird: NSObject {


    //internal let TAG: String = CXHybird::class.java.simpleName


    static var debug = true

    static let assetsDirName = "hybird"

    static let bundleSuffix = ".zip"

    static let configSuffix = ".json"

    static let localRootDir = CXCacheManager.getChildCacheDir(assetsDirName)


    static var initStrategy = CXHybirdInitStrategy.LOCAL


    static var downloader: ((_ downloadUrl: String, _ file: File?, _ callback: @escaping  (File?) -> Void?) -> Void?)? = nil


    static var configer: ((_ configUrl: String, _ callback: @escaping  (CXHybirdModuleConfigModel?) -> Void?) -> Void?)? = nil


    static var allConfiger: ((_ allConfigUrl: String, _ callback: @escaping (_ configList: MutableList<CXHybirdModuleConfigModel>?) -> Void?) -> Void?)? = nil


    static var allConfigUrl: String = ""


    static var modules: ConcurrentHashMap<String, CXHybirdModuleManager> = ConcurrentHashMap<String, CXHybirdModuleManager>()


    static func removeModule(_ moduleName: String?) {
        if (moduleName != nil && moduleName?.isNotBlank() == true) {
            CXHybirdUtil.removeIntercept(modules[moduleName!]?.currentConfig)
            modules.remove(moduleName)
        }

    }

    /**
     * debug true  的话代表是测试机,可以拉取动态更新的测试版本
     *       false 代表是已发布的正式的去打包，不能拉取动态更新的测试版本
     */
    static func initialize(
            debug: Bool = true,
            initStrategy: CXHybirdInitStrategy = CXHybird.initStrategy,
            allConfigUrl: String = "",
            allConfiger: ((_ configUrl: String, _ callback: @escaping (_ configList: MutableList<CXHybirdModuleConfigModel>?) -> Void?) -> Void?)? = nil,
            configer: ((_ configUrl: String, _ callback: @escaping  (CXHybirdModuleConfigModel?) -> Void?) -> Void?)? = nil,
            downloader: ((_ downloadUrl: String, _ file: File?, _ callback: @escaping  (File?) -> Void?) -> Void?)? = nil,
            callback: ((_ configList: MutableList<CXHybirdModuleConfigModel>?) -> Void)? = nil
    ) {


        objc_sync_enter(self)

        let start = System.currentTimeMillis()
        CXHybird.debug = debug
        CXHybird.initStrategy = initStrategy

        if (configer != nil) {
            CXHybird.configer = configer
        }
        if (downloader != nil) {
            CXHybird.downloader = downloader
        }
        if (allConfiger != nil) {
            CXHybird.allConfiger = allConfiger
        }
        if (allConfigUrl.isNotBlank()) {
            CXHybird.allConfigUrl = allConfigUrl
        }

        CXLogUtil.w(TAG, ">>>>----------------------------------------------------------------------")
        CXLogUtil.w(TAG, """

        >>>>
        >>>>
        >>>>---- 初始化HYBIRD模块 开始
        >>>>---- debug=\(CXHybird.debug)
        >>>>---- initStrategy=\(CXHybird.initStrategy)
        >>>>---- allConfigUrl=\(allConfigUrl)
        >>>>
        >>>>
        """)
        CXLogUtil.w(TAG, ">>>>----------------------------------------------------------------------")


        /**
         * 关于保存配置信息的时机
         * 1: 为空的时候的第一次初始化
         */
        let bundles: MutableMap<String, CXHybirdModuleBundleModel> = CXHybirdBundleInfoManager.getBundles()
        let bundleNames: MutableList<String> = bundles.keys()
        //let bundleNames: MutableList<String> = MutableList<String>()

        CXLogUtil.w(TAG, ">>>>----检测到当前初始化策略为: initStrategy=\(CXHybird.initStrategy), 检测本地是否存在缓存配置信息: bundleNames=\(bundleNames)")
        if (bundleNames.isEmpty()) {
            if (CXHybird.initStrategy == CXHybirdInitStrategy.DOWNLOAD) {
                CXLogUtil.w(TAG, ">>>>----由于未检测到缓存配置信息, 开始执行远程下载总配置信息进行初始化")
                if (CXHybird.allConfiger != nil && CXHybird.allConfigUrl.isNotBlank()) {
                    CXLogUtil.w(TAG, ">>>>----初始化策略为在线下载初始化, 开始下载总的配置文件, allConfigUrl=\(CXHybird.allConfigUrl)")
                    CXHybird.allConfiger?(CXHybird.allConfigUrl) { remoteConfigList in
                        CXLogUtil.w(TAG, ">>>>----总的配置文件下载 \(remoteConfigList == nil ? "失败" : "成功")")
                        CXLogUtil.w(TAG, ">>>>----remoteConfigList->")
                        CXLogUtil.j(CXLogUtil.WARN, CXJsonUtil.toJson(remoteConfigList))

                        downloadAndInitModules(remoteConfigList, callback)

                        return Void()
                    }
                } else {
                    CXLogUtil.w(TAG, ">>>>----检测到下载器 allConfiger==nil?\(CXHybird.allConfiger == nil) 尚未设置 或者 总配置信息的 allConfigUrl=\(allConfigUrl) 没有设置,return")
                }
            } else {
                CXLogUtil.w(TAG, ">>>>----由于未检测到缓存配置信息, 开始执行从 assets 读取总配置信息进行初始化")

                CXHybirdUtil.getConfigListFromAssetsWithCopyAndUnzip { configList in
                    CXHybird.initAllModules(configList, true, callback)
                }
            }
        } else {
            CXLogUtil.w(TAG, ">>>>----检测本地有缓存配置信息, 根据缓存配置信息初始化")
            initAllModules(bundles.values.mapNotNull {
                $0.moduleConfigList.firstOrNull()
            }, true, callback)
        }

        //应用程序前后台切换的时候执行一次异步检查更新 todo
        /*RxBus.toObservable(CXApplicationVisibleChangedEvent::class.java).subscribe {
            changeEvent ->
            //从前台切换到后台时
            if (!changeEvent.isApplicationVisible) {
                CXLogUtil.e(TAG, ">>>>----系统监测到应用程序从前台切换到后台,执行一次异步的健康体检和检查更新")
                checkAllUpdate()
            }
        }*/

        CXLogUtil.w(TAG, ">>>>----------------------------------------------------------------------")
        CXLogUtil.w(TAG, ">>>>----初始化HYBIRD模块 结束  耗时: \(System.currentTimeMillis() - start)ms")
        CXLogUtil.w(TAG, ">>>>----------------------------------------------------------------------")

        objc_sync_exit(self)
    }

    private static func downloadAndInitModules(_ configList: MutableList<CXHybirdModuleConfigModel>?, _ callback: ((_ configList: MutableList<CXHybirdModuleConfigModel>?) -> Void)? = nil) {
        CXLogUtil.w(TAG, ">>>>----需要下载初始化的模块有 \(configList?.map { $0.moduleName })")
        CXHybirdUtil.downloadAllModules(configList) { validConfigList in
            initAllModules(validConfigList, false, callback)
        }
    }

    private static func downloadAndInitModule(config: CXHybirdModuleConfigModel?) {
        if (config != nil) {
            let start = System.currentTimeMillis()
            CXLogUtil.d(TAG, "**********[downloadAndInitModule:单模块开始")

            CXLogUtil.d(TAG, "**********[downloadAndInitModule:单模块下载:\(config!.moduleName):开始], 当前线程:\(Thread.currentThread()), 当前时间:\(CXTimeUtil.yMdHmsS(Date(start)))")
            CXLogUtil.d(TAG, "**********[downloadAndInitModule:单模块下载:\(config!.moduleName):开始], \(config!.moduleDownloadUrl)")

            CXHybirdDownloadManager.download(config!) { isLocalFilesValid in
                CXLogUtil.d(TAG, "**********[downloadAndInitModule:单模块下载:\(config!.moduleName):结束], isLocalFilesValid:\(isLocalFilesValid)")
                CXLogUtil.d(TAG, "**********[downloadAndInitModule:单模块下载:\(config!.moduleName):结束], 当前线程:\(Thread.currentThread()), 当前时间:\(CXTimeUtil.yMdHmsS(Date())), 耗时:\(System.currentTimeMillis() - start)ms, config=\(config)")
                if (isLocalFilesValid) {
                    initModule(config)
                }
                return Void()
            }
        }
    }

    private static func initModule(_ config: CXHybirdModuleConfigModel?, _ callback: ((_ config: CXHybirdModuleConfigModel?) -> Void)? = nil) {
        let start = System.currentTimeMillis()
        CXLogUtil.e(TAG, "--[initModule:\(config?.moduleName)初始化开始]-----------------------------------------------------------------------------------")
        CXLogUtil.e(TAG, "--[initModule:\(config?.moduleName)初始化开始], 当前线程:\(Thread.currentThread()), 当前时间:\(CXTimeUtil.yMdHmsS(Date(start)))")
        if (config == nil) {
            CXLogUtil.e(TAG, "--[initModule:\(config?.moduleName)初始化结束], 没有模块需要初始化")
            CXLogUtil.e(TAG, "--[initModule:\(config?.moduleName)初始化结束], 当前线程:\(Thread.currentThread()), 当前时间:\(CXTimeUtil.yMdHmsS(Date())) ,最终成功初始化的模块:\(modules.map { $0.key }) , 一共耗时:\(System.currentTimeMillis() - start)ms")
            CXLogUtil.e(TAG, "--[initModule:\(config?.moduleName)初始化结束]-----------------------------------------------------------------------------------")
            callback?(nil)
        } else {
            Observable.from {
                        initModuleManager(config!)
                    }
                    .subscribeOn(ConcurrentDispatchQueueScheduler(qos: .background))
                    .subscribe(
                            onNext: { it in
                                CXLogUtil.e(TAG, "--[initModule:\(config!.moduleName)初始化结束], 当前线程:\(Thread.currentThread()), 当前时间:\(CXTimeUtil.yMdHmsS(Date())) ,最终成功初始化的模块:\(modules.map { $0.key }) , 一共耗时:\(System.currentTimeMillis() - start)ms")
                                CXLogUtil.e(TAG, "--[initModule:\(config!.moduleName)初始化结束]-----------------------------------------------------------------------------------")
                                callback?(config!)
                            }
                    )
        }
    }

    private static func initModuleManager(_ config: CXHybirdModuleConfigModel?) {
        let _start = System.currentTimeMillis()
        CXLogUtil.w(TAG, "----------[initModule:\(config?.moduleName ?? "nil")](开始), 当前线程:\(Thread.currentThread()), 当前时间:\(CXTimeUtil.yMdHmsS(Date(_start)))")

        if (config != nil) {
            let moduleManager = CXHybirdModuleManager.create(config!)
            if (moduleManager != nil) {
                modules[config!.moduleName] = moduleManager
                CXHybirdBundleInfoManager.saveConfigToBundleByName(config!.moduleName, config!)
            }
        }
        CXLogUtil.w(TAG, "----------[initModule:\(config?.moduleName ?? "nil")](结束), 当前线程:\(Thread.currentThread()), 当前时间:\(CXTimeUtil.yMdHmsS(Date())) , 一共耗时:\(System.currentTimeMillis() - _start)ms")
    }

    /**
     * @param callback 在全部模块成功初始化结束以后,检查全部更新之前回调, configList 返回空,代表没有模块被成功初始化, 属于初始化失败的标志
     */
    private static func initAllModules(_ configList: MutableList<CXHybirdModuleConfigModel>?, _ isNeedCheckAllUpdateAfterAllModulesSuccessInit: Bool = true, _ callback: ((_ configList: MutableList<CXHybirdModuleConfigModel>?) -> Void)? = nil) {
        let start = System.currentTimeMillis()
        CXLogUtil.e(TAG, "--[initAllModules:全部初始化开始]-----------------------------------------------------------------------------------")
        CXLogUtil.e(TAG, "--[initAllModules:全部初始化开始], 当前线程:\(Thread.currentThread()), 当前时间:\(CXTimeUtil.yMdHmsS(Date(start)))  ,isNeedCheckAllUpdateAfterAllModulesSuccessInit=\(isNeedCheckAllUpdateAfterAllModulesSuccessInit)")
        if (configList == nil || configList!.isEmpty()) {
            CXLogUtil.e(TAG, "--[initAllModules:全部初始化结束], 没有模块需要初始化")
            CXLogUtil.e(TAG, "--[initAllModules:全部初始化结束], 当前线程:\(Thread.currentThread()), 当前时间:\(CXTimeUtil.yMdHmsS(Date())) ,最终成功初始化的模块:\(modules.map { $0.key }) , 一共耗时:\(System.currentTimeMillis() - start)ms")
            CXLogUtil.e(TAG, "--[initAllModules:全部初始化结束]-----------------------------------------------------------------------------------")
            callback?(nil)
        } else {

            CXLogUtil.d("zip-> size=\(configList!.size) \(Thread.currentThread())")
            Observable.zip(
                            configList!.map { config -> Observable<CXHybirdModuleConfigModel> in
                                CXLogUtil.i("zip-> \(config.moduleName) size=\(configList!.size) \(Thread.currentThread())")

                                return Observable<CXHybirdModuleConfigModel>.just(config).map { element -> CXHybirdModuleConfigModel in
                                    CXLogUtil.d("zip-> \(config.moduleName) size=\(configList!.size) \(Thread.currentThread())")
                                    initModuleManager(element)
                                    return element
                                }.subscribeOn(ConcurrentDispatchQueueScheduler(qos: .background))
                            }
                    )
                    .observeOn(MainScheduler.instance)
                    .subscribe(onNext: { it in
                        CXLogUtil.e(TAG, "--[initAllModules:全部初始化结束], 当前线程:\(Thread.currentThread()), 当前时间:\(CXTimeUtil.yMdHmsS(Date())) ,最终成功初始化的模块:\(modules.map { $0.key }) , 一共耗时:\(System.currentTimeMillis() - start)ms")
                        CXLogUtil.e(TAG, "--[initAllModules:全部初始化结束]-----------------------------------------------------------------------------------")

                        callback?(configList!)

                        if (isNeedCheckAllUpdateAfterAllModulesSuccessInit) {
                            checkAllUpdate()
                        }
                    })
        }
    }

    /**
     * 只有各模块初始化成功后可以执行该方法, 因为会用到 modules
     */
    static func checkAllUpdate() {
        let start = System.currentTimeMillis()
        CXLogUtil.w(TAG, ">>>>----检查更新:下载开始, 当前线程:\(Thread.currentThread()), 当前时间:\(CXTimeUtil.yMdHmsS(Date(start)))")
        if (allConfiger == nil) {
            CXLogUtil.w(TAG, ">>>>----检查更新:结束,allConfiger == nil !!! 当前时间:\(CXTimeUtil.yMdHmsS(Date())), 耗时:\(System.currentTimeMillis() - start)ms")
        } else {
            allConfiger!(allConfigUrl) { (remoteConfigList: MutableList<CXHybirdModuleConfigModel>?) in
                CXLogUtil.w(TAG, ">>>>----检查更新:下载\(remoteConfigList == nil ? "失败" : "成功"), 当前时间:\(CXTimeUtil.yMdHmsS(Date())), 耗时:\(System.currentTimeMillis() - start)ms")
                CXLogUtil.w(TAG, ">>>>----remoteConfigList->")
                //CXLogUtil.j(CXLogUtil.WARN, CXJsonUtil.toJson(remoteConfigList))

                var needDownloadAndInitConfigList: MutableList<CXHybirdModuleConfigModel> = MutableList<CXHybirdModuleConfigModel>()


                remoteConfigList?.forEach { it in
                    if (modules.containsKey(it.moduleName)) {
                        CXLogUtil.w(TAG, ">>>>----检测模块\(it.moduleName)已经被初始化过, 走更新流程")
                        checkUpdate(it)
                    } else if (initStrategy == CXHybirdInitStrategy.DOWNLOAD) {
                        CXLogUtil.w(TAG, ">>>>----检测模块\(it.moduleName)已经尚未被初始化过, 且当前初始化策略为在线下载初始化, 走下载初始化流程")
                        needDownloadAndInitConfigList.add(it)
                    }

                }

                if (needDownloadAndInitConfigList.isNotEmpty()) {
                    downloadAndInitModules(needDownloadAndInitConfigList)
                }

                CXLogUtil.w(TAG, ">>>>----检查更新:结束, 当前时间:\(CXTimeUtil.yMdHmsS(Date())), 耗时:\(System.currentTimeMillis() - start)ms")
                return Void()
            }
        }
    }

    static func checkUpdate(_ remoteConfig: CXHybirdModuleConfigModel?) {
        CXLogUtil.e(TAG, ">>>>>>>======检查子模块更新 开始:\(remoteConfig?.moduleName), 当前时间:\(CXTimeUtil.yMdHmsS(Date()))")

        if (remoteConfig != nil) {
            let moduleManager = modules[remoteConfig!.moduleName]
            if (moduleManager != nil) {
                CXLogUtil.e(TAG, ">>>>>>>======检查子模块本地已经有以前的版本信息,进行比较操作")
                //1:正式包，所有机器可以拉取
                //2:测试包，只要测试机器可以拉取
                if (!remoteConfig!.moduleDebug || (remoteConfig!.moduleDebug && debug)) {
                    CXLogUtil.e(remoteConfig!.moduleName, "检测到该版本为正式版 或者当前为测试版本并且本机是测试机,可以执行更新操作")
                    let remoteVersion = remoteConfig!.moduleVersion.toFloatOrNull()
                    let localVersion = moduleManager!.currentConfig.moduleVersion.toFloatOrNull()
                    CXLogUtil.v(TAG, ">>>>>>>======\(remoteConfig!.moduleName) 当前版本:\(localVersion)   远程版本:\(remoteVersion)")
                    if (remoteVersion != nil && localVersion != nil) {
                        //版本号相等时不做任何处理，避免不必要的麻烦
                        if (remoteVersion != localVersion) {
                            CXLogUtil.v(TAG, ">>>>>>>======\(remoteConfig!.moduleName) 系统检测到有新版本")

                            if (remoteConfig!.moduleUpdateStrategy == CXHybirdUpdateStrategy.ONLINE) {
                                moduleManager!.onlineModel = true
                            } else {
                                CXLogUtil.e(TAG, ">>>>>>>======\(remoteConfig!.moduleName) 无需切换为在线模式")
                            }
//                            CXHybirdDownloadManager.download(remoteConfig) {
//                                (isLocalFilesletid)->
//                            }
                        } else {
                            CXLogUtil.v(TAG, ">>>>>>>======\(remoteConfig!.moduleName) 系统检测到 remoteVersion:\(remoteVersion) 或者 localVersion:\(localVersion) 相等, 无需更新")
                        }
                    } else {
                        CXLogUtil.e(TAG, ">>>>>>>======\(remoteConfig!.moduleName) 系统检测到 remoteVersion:\(remoteVersion) 或者 localVersion:\(localVersion) 为空, 无法判断需要更新,默认不需要更新")
                    }
                } else {
                    CXLogUtil.e(TAG, ">>>>>>>======\(remoteConfig!.moduleName) 检测到该版本为调试版本且本机不是测试机,不执行更新操作 return false")
                }
            }
        }
        CXLogUtil.e(TAG, ">>>>>>>======检查子模块结束 结束:\(remoteConfig?.moduleName), 当前时间:\(CXTimeUtil.yMdHmsS(Date()))")
    }


    static func checkUpdate(_ url: String?, _ callback: (() -> Void?)? = nil) {
        let moduleManager = getModule(url)
        if (moduleManager == nil) {
            callback?()
        } else {
            checkUpdate(moduleManager, callback)
        }
    }


    static func checkUpdate(_ moduleManager: CXHybirdModuleManager?, _ callback: (() -> Void?)? = nil) {
        if (moduleManager != nil) {
            let moduleName = moduleManager!.currentConfig.moduleName
            let start = System.currentTimeMillis()
            CXLogUtil.v(TAG + ":" + moduleName, "系统检测更新(同步) 开始 当前版本=\(moduleManager!.currentConfig.moduleVersion),当前线程:\(Thread.currentThread())")
            CXLogUtil.v(TAG + ":" + moduleName, "当前配置=\(moduleManager!.currentConfig)")

            if (configer == nil) {
                CXLogUtil.e(TAG + ":" + moduleName, "系统检测到尚未配置 config 下载器，请先设置 config 下载器, return")
                callback?()
                return
            }

            if (CXHybirdDownloadManager.isDownloading(moduleManager!.currentConfig)) {
                CXLogUtil.e(TAG + ":" + moduleName, "系统检测到当前正在下载更新中, return")
                callback?()
                return
            }

            if (moduleManager!.onlineModel) {
                CXLogUtil.e(TAG + ":" + moduleName, "系统检测到当前已经是在线状态了,无需重复检测 return")
                callback?()
                return
            }

            let moduleConfigUrl = moduleManager!.currentConfig.moduleConfigUrl
            CXLogUtil.v(TAG + ":" + moduleName, "下载配置文件 开始 当前版本=\(moduleManager!.currentConfig.moduleVersion): \(moduleConfigUrl) , 当前线程:\(Thread.currentThread())")
            configer?(moduleConfigUrl) { remoteConfig in
                CXLogUtil.v(TAG + ":" + moduleName, "下载配置文件 \(remoteConfig == nil ? "失败" : "成功") , 当前线程:\(Thread.currentThread())")
                CXLogUtil.v(TAG + ":" + moduleName, "remoteConfig->")
                CXLogUtil.j(CXLogUtil.INFO, CXJsonUtil.toJson(remoteConfig))
                if (remoteConfig != nil) {
                    //1:正式包，所有机器可以拉取
                    //2:测试包，只要测试机器可以拉取
                    if (!remoteConfig!.moduleDebug || (remoteConfig!.moduleDebug && debug)) {
                        CXLogUtil.e(TAG + ":" + moduleName, "检测到该版本为正式版 或者当前为测试版本并且本机是测试机,可以执行更新操作")
                        let remoteVersion = remoteConfig!.moduleVersion.toFloatOrNull()
                        let localVersion = moduleManager!.currentConfig.moduleVersion.toFloatOrNull()
                        CXLogUtil.v("\(TAG + ":" + moduleName) 当前版本:\(localVersion)   远程版本:\(remoteVersion)")
                        if (remoteVersion != nil && localVersion != nil) {
                            //版本号相等时不做任何处理，避免不必要的麻烦
                            if (remoteVersion != localVersion) {
                                CXLogUtil.v("系统检测到有新版本")
                                if (remoteConfig!.moduleUpdateStrategy == CXHybirdUpdateStrategy.ONLINE) {
                                    moduleManager!.onlineModel = true
                                } else {
                                    CXLogUtil.e("无需切换为在线模式")
                                }
                                CXHybirdDownloadManager.download(remoteConfig!)
                            } else {
                                CXLogUtil.v("系统检测到 remoteVersion:\(remoteVersion) 或者 localVersion:\(localVersion) 相等, 无需更新")
                            }
                        } else {
                            CXLogUtil.e("系统检测到 remoteVersion:\(remoteVersion) 或者 localVersion:\(localVersion) 为空, 无法判断需要更新,默认不需要更新")
                        }
                    } else {
                        CXLogUtil.e(TAG + ":" + moduleName, "检测到该版本为调试版本且本机不是测试机,不执行更新操作 return false")
                    }
                }
                callback?()
                return Void()
            }
            CXLogUtil.v(TAG + ":" + moduleName, "检查更新 结束, 当前线程:\(Thread.currentThread()), 耗时: \(System.currentTimeMillis() - start)ms")
        }
    }

    static func getModule(_ url: String?) -> CXHybirdModuleManager? {
        if (url == nil || url!.isNullOrBlank()) {
            return nil
        }

        return modules.values.firstOrNull {
            isMemberOfModule($0.currentConfig, url)
        }
    }


    static func onWebViewClose(_ webViewClient: WebViewClient?) {
        CXHybirdLifecycleManager.onWebViewClose(webViewClient)
    }


    static func onWebViewOpenPage(_ webViewClient: WebViewClient?, _ url: String?) {
        CXHybirdLifecycleManager.onWebViewOpenPage(webViewClient, url)
    }


    static func isMemberOfModule(_ config: CXHybirdModuleConfigModel?, _ url: String?) -> Bool {
        return url?.contains(config?.moduleMainUrl ?? "") == true
    }


    static func isModuleOpened(_ moduleName: String?) -> Bool {
        return CXHybirdLifecycleManager.isModuleOpened(moduleName)
    }

}
