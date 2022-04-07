import Foundation
import RxCocoa
import RxSwift

class CXHybirdUtil {

    static func getRootDir(_ moduleName: String) -> File {
        let root = File(CXHybird.localRootDir, moduleName)
        //CXLogUtil.v("rootDir=\(root)")
        return root
    }

    static func getZipFile(_ config: CXHybirdModuleConfigModel) -> File {
        let parent = getRootDir(config.moduleName)
        let name = "\(config.moduleName)-\(config.moduleVersion)\(CXHybird.bundleSuffix)"
        let zipFile = File(parent, name)
        //CXLogUtil.v("zipFile=\(zipFile)")
        return zipFile
    }

    static func getUnzipDir(_ config: CXHybirdModuleConfigModel) -> File {
        let unzipDir = File(getRootDir(config.moduleName), config.moduleVersion)
        //CXLogUtil.v("unzipDir=\(unzipDir)")
        return unzipDir.makeDirs()
    }

    /**
     * 校验配置信息
     *
     * 规则
     * 1: 文件夹校验成功则成功，zip包的校验不影响结果，如果zip包校验失败则删除zip包
     * 注意文件夹如果检验失败压缩包校验成功,则立即重新解压且返回校验成功,重新解压正确的压缩包不用重复校验文件夹内的各个文件
     */
    static func isLocalFilesValid(_ config: CXHybirdModuleConfigModel?) -> Bool {
        CXLogUtil.v("<><><><><><><>文件校验 开始: 模块名称=\(config?.moduleName ?? "")")
        var success = false
        let start = System.currentTimeMillis()
        if (config != nil) {
            let zipFile = getZipFile(config!)
            let unzipDir = getUnzipDir(config!)
            if (!verifyLocalFiles(unzipDir, config!.moduleFilesMd5, config!.moduleName ?? "")) {
                if (!verifyZip(zipFile, config!.moduleZipMd5, config!.moduleName ?? "")) {
                    success = false
                } else {
                    success = unzipToLocal(zipFile, unzipDir)//解压后的文件夹校验失败，但是zip包校验成功，则重新解压即可
                }
            } else {
                success = true
            }
        }
        CXLogUtil.v("<><><><><><><>文件校验 文件校验结束: 校验 \(success ? "成功" : "失败") , 模块版本=\(config?.moduleVersion ?? "nil") , 耗时: \(System.currentTimeMillis() - start)ms")
        return success
    }

    static internal func unzipToLocal(_ zipFile: File?, _ unZipDir: File?) -> Bool {
        var success = false
        CXFileUtil.deleteDirectory(unZipDir?.path)
        do {
            try CXZipUtil.unzip(zipFile?.path, unZipDir?.path)
            success = true
        } catch {
        }
        return success
    }

    /**
     * 校验失败删除压缩包
     */
    static internal func verifyZip(_ zipFile: File?, _ moduleZipMd5: String?, _ logTag: String? = CXHybird.TAG) -> Bool {
        var success = false
        if (zipFile != nil && !TextUtils.isEmpty(moduleZipMd5)) {
            let zipFileExists = zipFile!.exists()

            let zipFileMd5 = CXChecksumUtil.genMD5Checksum(zipFile)
            let isZipFileMd5Valid = zipFileMd5 == moduleZipMd5
            success = zipFileExists && isZipFileMd5Valid

            CXLogUtil.v("-- 文件校验 校验本地 zip 压缩包:\(success ? "成功" : "失败") localMD5:\(zipFileMd5 ?? "nil") , rightMD5:\(moduleZipMd5 ?? "nil")")
        } else {
            CXLogUtil.e("-- 文件校验 校验本地 zip 压缩包:失败, zipFile is nil or moduleZipMd5 is empty")
        }

        if (!success) {
            CXFileUtil.deleteFile(zipFile)
        }
        return success
    }

    /**
     * 校验失败删除文件夹
     */
    static internal func verifyLocalFiles(_ unZipDir: File?, _ moduleFilesMd5: HashMap<String, String>?, _ logTag: String? = CXHybird.TAG) -> Bool {
        var success = false

        if (unZipDir != nil && moduleFilesMd5 != nil) {
            let localUnzipDirExists = unZipDir!.exists()

            let rightFilesCount = moduleFilesMd5!.size
            var validFilesCount = 0
            if (localUnzipDirExists) {
                CXFileUtil.getFileList(unZipDir).forEach { (it: File) in
                    let fileMd5 = CXChecksumUtil.genMD5Checksum(it)
                    let remotePath = it.absolutePath.replace(unZipDir!.absolutePath, "")
                    let rightMd5 = moduleFilesMd5![remotePath]
                    let isFileMd5Valid = fileMd5 == rightMd5
                    if (isFileMd5Valid) {
                        validFilesCount = validFilesCount + 1
                    }
                    CXLogUtil.v("-- 文件校验 校验文件(\(it.name)) : \(isFileMd5Valid ? "成功" : "失败") , fileMd5:\(fileMd5 ?? "") , rightMd5:\(rightMd5 ?? "") , localPath:\(it.path) ,remotePath:\(remotePath)")
                }
            }
            success = rightFilesCount == validFilesCount && localUnzipDirExists
        }
        CXLogUtil.v("-- 文件校验 校验本地 zip 解压后的文件夹:\(success ? "成功" : "失败"), path=\(unZipDir)")
        if (!success) {
            CXFileUtil.deleteDirectory(unZipDir)
        }
        return success
    }

    static func getLocalFile(_ config: CXHybirdModuleConfigModel?, _ url: String?) -> File? {
        var localFile: File? = nil

        if (config != nil && url?.isNotBlank() == true) {
            if (url!.startsWith("http")) {
                var tmpPath: String? = url!.substringAfter(config!.moduleMainUrl)
                if (tmpPath != nil && tmpPath!.isNotBlank()) {
                    if (tmpPath!.contains("#/")) {
                        //  #/index?userInfo=
                        //  index.shtml#/index?userInfo=
                        tmpPath = "index.shtml"
                    } else {
                        //  css/app.ae44e2d0f77af623eb2bcac61ceb2626.css
                        //  js/manifest.d01e227b32f52bdd60bd.js
                        //  js/app.6aff061a2dc3ffbfb27c.js
                        //  png/xxx
                    }
                    localFile = File(getUnzipDir(config!), "\(tmpPath!)")
                }
            }
        } else {
            CXLogUtil.e("params error ! config=\(config) , url=\(url)")
        }
        let exists = localFile?.exists() == true
        CXLogUtil.v("检测到本地文件\(!exists ? " 不存在 " : " 存在"), 文件路径:\(localFile?.absolutePath ?? "") url:\(url ?? "")")
        if (!exists) {
            localFile = nil
        }

        return localFile
    }

    static func copyModuleZipFromAssets(_ moduleName: String, _ primaryConfig: CXHybirdModuleConfigModel?) -> Bool {
        CXLogUtil.d("--------[copyModuleZipFromAssets]:  start")
        var success = false
        if (primaryConfig != nil) {
            let zipFile = getZipFile(primaryConfig!)
            let unzipDir = getUnzipDir(primaryConfig!)
            do {
                if (zipFile != nil && zipFile.exists()) {
                    try CXFileUtil.deleteFile(zipFile)
                }
                if (unzipDir.exists()) {
                    try CXFileUtil.deleteDirectory(unzipDir)
                }
                try CXFileUtil.copy(Bundle.main.path(forResource: "\(moduleName)-\(primaryConfig!.moduleVersion)", ofType: "zip")!, zipFile)
                success = true
            } catch {
                CXLogUtil.e("--------[copyModuleZipFromAssets]: 文件不存在", error)
            }
        }
        CXLogUtil.d("--------[copyModuleZipFromAssets]:  end success:\(success)")
        return success
    }

    /**
     * @return 返回拷贝zip成功 并且解压zip到文件夹也成功的 configList
     */
    static func getConfigListFromAssetsWithCopyAndUnzip(_ callback: @escaping (_ configList: MutableList<CXHybirdModuleConfigModel>) -> Void) {
        let start = System.currentTimeMillis()
        CXLogUtil.v("--------[getConfigListFromAssetsWithCopyAndUnzip: 开始]-----------------------------------------------------------------------------------")
        CXLogUtil.v("--------[getConfigListFromAssetsWithCopyAndUnzip: 开始], 当前线程:\(Thread.currentThread()), 当前时间:\(CXTimeUtil.yMdHmsS(Date(start)))")
        var allConfigList: MutableList<CXHybirdModuleConfigModel> = MutableList<CXHybirdModuleConfigModel>()
        Observable<Any>.create { observer in
                    do {
                        let path = Bundle.main.path(forResource: "all", ofType: "json")
                        let allConfigJsonString = CXFileUtil.readTextFromFile(path)
                        CXLogUtil.j(CXLogUtil.VERBOSE, allConfigJsonString)

                        allConfigList = CXJsonUtil.parseArray(allConfigJsonString) ?? MutableList<CXHybirdModuleConfigModel>()
                        CXLogUtil.d("--------[getConfigListFromAssetsWithCopyAndUnzip:  allConfigList.size = \(allConfigList.size)")

                        if (allConfigList.isNotEmpty()) {
                            for (index, config) in allConfigList.enumerated().reversed(){
                                let zipFile = getZipFile(config)
                                CXLogUtil.d("copyPrimaryZipFromAssetsSuccess zipFile:\(zipFile) exists=\(zipFile.exists())")
                                let unzipDir = getUnzipDir(config)
                                CXLogUtil.d("copyPrimaryZipFromAssetsSuccess unzipDir:\(unzipDir)")

                                let copyStart = System.currentTimeMillis()
                                let copyPrimaryZipFromAssetsSuccess = copyModuleZipFromAssets(config.moduleName, config)
                                let copyTime = System.currentTimeMillis() - copyStart
                                CXLogUtil.d("copyPrimaryZipFromAssetsSuccess --> \(copyPrimaryZipFromAssetsSuccess), zipFile.exists=\(zipFile.exists())")

                                if (copyPrimaryZipFromAssetsSuccess) {
                                    let unzipStart = System.currentTimeMillis()
                                    let unzipToLocalSuccess = unzipToLocal(zipFile, unzipDir)
                                    let unzipTime = System.currentTimeMillis() - unzipStart

                                    CXLogUtil.v("--------[getConfigListFromAssetsWithCopyAndUnzip: 从 assets 拷贝 \(config.moduleName).zip 成功, 解压\(unzipToLocalSuccess ? "成功" : "失败"), 拷贝耗时:\(copyTime) ms, 解压耗时:\(unzipTime) ms, 当前线程:\(Thread.currentThread()), 当前时间:\(CXTimeUtil.yMdHmsS(Date(start)))")
                                    if (!unzipToLocalSuccess) {
                                        allConfigList.remove(at: index)
                                        CXLogUtil.v("--------[getConfigListFromAssetsWithCopyAndUnzip: 解压\(config.moduleName).zip 到文件夹失败, 从列表中删除 \(config.moduleName), 当前线程:\(Thread.currentThread()), 当前时间:\(CXTimeUtil.yMdHmsS(Date(start)))")
                                    }
                                } else {
                                    CXLogUtil.v("--------[getConfigListFromAssetsWithCopyAndUnzip: 从 assets 拷贝 \(config.moduleName).zip 失败, 拷贝耗时:\(copyTime) ms, 当前线程:\(Thread.currentThread()), 当前时间:\(CXTimeUtil.yMdHmsS(Date(start)))")
                                }
                            }
                        }
                        observer.onNext(0)
                    } catch {
                        CXLogUtil.e("--------[getConfigListFromAssetsWithCopyAndUnzip: 开始], 文件不存在, 当前线程:\(Thread.currentThread()), 当前时间:\(CXTimeUtil.yMdHmsS(Date(start)))", error)
                        observer.onError(error)
                    }
                    return Disposables.create()
                }
                .subscribeOn(ConcurrentDispatchQueueScheduler(qos: .background))
                .observeOn(ConcurrentDispatchQueueScheduler(qos: .background))
                .subscribe(onNext: { it in
                    CXLogUtil.v("--------[getConfigListFromAssetsWithCopyAndUnzip: 返回解压成功的 allConfigList.size=\(allConfigList.size)], 当前线程:\(Thread.currentThread()), 当前时间:\(CXTimeUtil.yMdHmsS(Date(start)))")
                    CXLogUtil.v("--------[getConfigListFromAssetsWithCopyAndUnzip: 结束]-----------------------------------------------------------------------------------")
                    CXLogUtil.v("--------[getConfigListFromAssetsWithCopyAndUnzip: 结束], 当前线程:\(Thread.currentThread()), 当前时间:\(CXTimeUtil.yMdHmsS(Date())), 一共耗时:\(System.currentTimeMillis() - start)ms")

                    //CXFileUtil.printDirs(CXHybird.localRootDir)
                    callback(allConfigList)
                })
    }

    static func downloadAllModules(_ configList: MutableList<CXHybirdModuleConfigModel>?, callback: ((_ validConfigList: MutableList<CXHybirdModuleConfigModel>?) -> Void)? = nil) {
        let start = System.currentTimeMillis()
        CXLogUtil.e("--[downloadAllModules:全部下载开始]-----------------------------------------------------------------------------------")
        CXLogUtil.e("--[downloadAllModules:全部下载开始]:\(configList?.map { $0.moduleName })")
        CXLogUtil.e("--[downloadAllModules:全部下载开始], 当前线程:\(Thread.currentThread()), 当前时间:\(CXTimeUtil.yMdHmsS(Date(start)))")

        if (configList == nil || configList!.isEmpty()) {
            CXLogUtil.e("--[downloadAllModules:全部下载结束], 没有模块需要下载")
            CXLogUtil.e("--[downloadAllModules:全部下载结束], 当前线程:\(Thread.currentThread()), 当前时间:\(CXTimeUtil.yMdHmsS(Date())) ,最终成功初始化的模块:\(CXHybird.modules.map { $0.key }) , 一共耗时:\(System.currentTimeMillis() - start)ms")
            CXLogUtil.e("--[downloadAllModules:全部下载结束]-----------------------------------------------------------------------------------")
            callback?(nil)
        } else {

            Observable.zip(
                            configList!.map { config in
                                Observable<CXHybirdModuleConfigModel>.create { observer in
                                    //下载好所有准备都充分后, 再进行初始化
                                    CXLogUtil.e("----开始下载子模块: \(config.moduleDownloadUrl)")
                                    let _start = System.currentTimeMillis()
                                    CXLogUtil.d("--------[downloadAllModules:单模块下载:\(config.moduleName):开始], 当前线程:\(Thread.currentThread()), 当前时间:\(CXTimeUtil.yMdHmsS(Date(_start)))")
                                    CXLogUtil.d("--------[downloadAllModules:单模块下载:\(config.moduleName):开始], \(config.moduleDownloadUrl)")
                                    CXHybirdDownloadManager.download(config) { isLocalFilesValid in
                                        CXLogUtil.d("--------[downloadAllModules:单模块下载:\(config.moduleName):结束], isLocalFilesValid:\(isLocalFilesValid)")
                                        CXLogUtil.d("--------[downloadAllModules:单模块下载:\(config.moduleName):结束], 当前线程:\(Thread.currentThread()), 当前时间:\(CXTimeUtil.yMdHmsS(Date())), 耗时:\(System.currentTimeMillis() - _start)ms, config=\(config)")
                                        if (isLocalFilesValid) {
                                            observer.onNext(config)
                                        } else {
                                            observer.onError(FileNotFoundException("--------[downloadAllModules:单模块下载:\(config.moduleName):校验失败] isLocalFilesValid=\(isLocalFilesValid)"))
                                        }
                                        return Void()
                                    }
                                    return Disposables.create()
                                }.catchErrorJustReturn(CXHybirdModuleConfigModel.invalidConfigModel)
                            },
                            ({
                                CXLogUtil.e("result selector == ")
                                return $0.map {
                                    $0 as CXHybirdModuleConfigModel
                                }.filter { it in
                                    it != CXHybirdModuleConfigModel.invalidConfigModel
                                }.toMutableList()
                            })
                    )
                    .subscribe(
                            onNext: { (validConfigList: MutableList<CXHybirdModuleConfigModel>) in

                                CXLogUtil.e("--[downloadAllModules:全部下载结束]:success, 经校验有效的可以保存到 sharedPreference 的 validConfigList=\(validConfigList.map { $0.moduleName })")
                                CXLogUtil.e("--[downloadAllModules:全部下载结束]:success, 当前线程:\(Thread.currentThread()), 当前时间:\(CXTimeUtil.yMdHmsS(Date())), 耗时:\(System.currentTimeMillis() - start)ms")
                                CXLogUtil.e("--[downloadAllModules:全部下载结束]-----------------------------------------------------------------------------------")
                                callback?(validConfigList)
                            }
                    )

        }
    }

    static func removeIntercept(_ config: CXHybirdModuleConfigModel?) {
//        if (config != nil) {
//            CXHybirdBridge.removeScheme(config.moduleMainUrl)
//            CXHybirdBridge.removeRequest(config.moduleMainUrl)
//        }
    }

/**
 * 设置资源拦截器,URL拦截器
 */
    static func setIntercept(_ config: CXHybirdModuleConfigModel?) {
        /*let moduleName: String = config?.moduleName ?? ""
        let tag: String = CXHybird.TAG + ":" + moduleName

        CXLogUtil.v(tag, "======>> 设置拦截器开始: \(config?.moduleVersion)")

        if (config == nil) {
            CXLogUtil.v(tag, "======>> 检测到 配置文件为空 , 取消设置拦截器 return")
            return
        }

        let interceptUrl = config!.moduleMainUrl

        if (interceptUrl.isEmpty()) {
            CXLogUtil.e(tag, "======>> 检测到 interceptMainUrl == nil return")
            return
        }

        CXHybirdBridge.removeScheme(interceptUrl)*/
//        CXHybirdBridge.removeRequest(interceptUrl)

        /**
         * webView.loadUrl 不会触发此回调,放到 CXHybirdBridge.addRequest(interceptMainUrl) 里面处理
         * http://www.jianshu.com/p/3474cb8096da
         */
        /*CXLogUtil.v(config.CXHybird.TAG +":" + moduleName, "增加 URL 拦截 , 匹配 -> interceptMainUrl : \(interceptUrl)")
        CXHybirdBridge.addScheme(interceptUrl) { _: WebView?, webViewClient: WebViewClient?, url: String?, callback: (() -> Void?)? ->
            lifecycleManager.onWebViewOpenPage(webViewClient, url)

            CXLogUtil.e(config.CXHybird.TAG +":" + moduleName, "系统拦截到模块URL请求: url=\(url) ,匹配到 检测内容为 '\(interceptUrl)' 的拦截器, 由于当前模块的策略为 '\(updateStrategy)' , \(updateStrategy == CXHybirdUpdateStrategy.ONLINE) "需要检测更新,开始更新" else "不需要检查更新,不拦截 return")")

            //仅仅更新策略为 ONLINE 时,才会执行此步骤
            if (updateStrategy == CXHybirdUpdateStrategy.ONLINE) {
                checkUpdate(synchronized = false, switchToOnlineModeIfRemoteVersionChanged = true, callback = callback)
            }
            true
        }*////

        //CXLogUtil.v(tag, "======>> 增加 资源 拦截 , 匹配 -> interceptUrl : \(interceptUrl)")
        /*CXHybirdBridge.addRequest(interceptUrl) {
            (_: WebView?, url: String?) ->
                    CXLogUtil.v(tag, "======>> shouldInterceptRequest: \(url) ,匹配拦截器:\(interceptUrl)")
            var resourceResponse: WebResourceResponse? = nil
            if (CXHybird.modules[moduleName]?.onlineModel != true) {
                let localFile = CXHybirdUtil.getLocalFile(config, url)
                if (url != nil && localFile?.exists() == true) {
                    do {
                        CXLogUtil.v(tag, "======>> 执行伪造本地资源")
//                        try resourceResponse = WebResourceResponse(mimeType, "UTF-8", FileInputStream(localFile))
                    } catch (e:Error) {
                        CXLogUtil.e(tag, "======>> 伪造本地资源出错", e)
                    }
                } else {
                    CXLogUtil.v(tag, "======>> 系统检测到本地文件不存在,访问在线资源")
                }
            } else {
                CXLogUtil.v(tag, "======>> 系统检测到当前为在线模式,访问在线资源")
            }
            resourceResponse
        }*/
        //CXLogUtil.v(tag, "======>> 设置拦截器结束")
    }

    /**
     * 校验所有版本信息,如果无效则删除该版本相关的所有文件/配置
     * 重置当前版本指向
     * 如果本模块尚未被打开,则下一次启动生效的更新/回滚配置 在此一并兼容
     */
    static func fitLocalConfigsInfoSync(_ moduleName: String?) {
        /* objc_sync_enter(self)

         if (moduleName == nil || moduleName!.isNullOrBlank()) {
             return
         }
         let moduleManager: CXHybirdModuleManager? = CXHybird.modules[moduleName!]
         if (moduleManager == nil) {
             return
         }

         let tag = CXHybird.TAG + ":" + moduleName!


         CXLogUtil.e(tag, "||||||||=====>>>>> 一次检验本地可用配置信息的完整性(同步) 开始 , 当前线程:\(Thread.currentThread()) , (如果本模块没有被浏览器加载,则优先合并 下次启动生效的任务, 当前 onLineMode = \(moduleManager.onlineModel))")
         let start = System.currentTimeMillis()
         let configList = CXHybirdBundleInfoManager.getConfigListFromBundleByName(moduleName!)
         CXLogUtil.e(tag, "||||||||=====>>>>> 当前最新配置信息为: \(configList.map { $0.moduleVersion })")
         if (configList.isNotEmpty()) {
             CXLogUtil.v(tag, "||||||||=====>>>>> 检测到本地配置信息不为空,开始过滤/清理无效的本地配置信息")

             //直到找到有效的版本，如果全部无效，则在后续步骤中重新解压原始版本
             let iterate = configList.listIterator()
             while (iterate.hasNext()) {
                 if (!CXHybirdUtil.isLocalFilesValid(iterate.next())) {
                     iterate.remove() // mind ConcurrentModificationException
                 }
             }
         }

         //删除无效的配置信息,删除后会重新判断空,如果是空会获取原始安装包
         if (configList.isEmpty()) {
             CXLogUtil.w(tag, "||||||||=====>>>>> 检测到本地配置信息为空,从全局你变量 modules 中删除")
             CXHybird.removeModule(moduleName)
             CXLogUtil.e(tag, "||||||||=====>>>>> 清空 本模块 结束, 当前本地可操作模块: \(CXHybird.modules.keys)")
         } else {
             //如果有删除的或则新加的原始配置信息，则需要重新保存下
             CXHybirdBundleInfoManager.saveConfigListToBundleByName(moduleName, configList)
             configList.firstOrNull()?.let { it in
                 moduleManager.currentConfig = it
             }
             CXLogUtil.e(tag, "||||||||=====>>>>> 重置当前 本地配置头 为:\(moduleManager.currentConfig.moduleVersion)")

         }
         CXLogUtil.e(tag, "||||||||=====>>>>> 一次检验本地所有可用配置信息(不包含 next)的完整性(同步) 结束 , 当前线程:\(Thread.currentThread()) , (如果本模块没有被浏览器加载,则优先合并 下次启动生效的任务, 当前 onLineMode = \(moduleManager.onlineModel) ,  耗时: \(System.currentTimeMillis() - start)ms")
         objc_sync_exit(self)*/
    }

    /**
     * 当检测到模块完全没有被浏览器加载的时候,可以调用此方法是否有 下次加载生效的配置信息 ,并异步处理本地文件,重置当前模块的 版本头信息
     *
     * 调用时机
     * 1: 当前模块退出浏览器的时候(异步处理)
     * 2: 当前模块第一次被浏览器加载的时候(同步处理)
     */
    static internal func fitNextAndFitLocalIfNeedConfigsInfo(_ moduleName: String?) {
        Observable.from {
            fitNextAndFitLocalIfNeedConfigsInfoSync(moduleName)
        }.subscribeOn(ConcurrentDispatchQueueScheduler(qos: .background)).observeOn(MainScheduler.instance).subscribe()
    }

    /**
     * 同步 处理下次生效的配置信息以及处理完后 如果需要的话(即确实有下次生效的配置信息的情况下,避免检查下次配置信息的时候多做一次处理本地配置信息的操作)紧接着处理本地配置信息
     */
    static internal func fitNextAndFitLocalIfNeedConfigsInfoSync(_ moduleName: String?, _ mustFitLocal: Bool = false) {
        /* if (moduleName == nil || moduleName!.isNullOrBlank()) {
             return
         }
         let moduleManager: CXHybirdModuleManager? = CXHybird.modules[moduleName!]
         if (moduleManager == nil) {
             return
         }

         let tag = CXHybird.TAG + ":" + moduleName!

         CXLogUtil.v(tag, "---->>>> 检测是否有下次启动生效的配置文件需要处理 开始, 当前线程:\(Thread.currentThread())")

         var configList = CXHybirdBundleInfoManager.getConfigListFromBundleByName(moduleManager!.currentConfig.moduleName)
         CXLogUtil.v(tag, "---->>>> configList->")
         CXLogUtil.j(CXLogUtil.VERBOSE, tag, CXJsonUtil.toJson(configList))

         let isModuleOpened = CXHybirdLifecycleManager.isModuleOpened(moduleName!)
         if (!isModuleOpened) {
             CXLogUtil.v(tag, "---->>>> 检测当前模块未被浏览器加载,可以处理")

             let nextConfig = CXHybirdBundleInfoManager.getNextConfigFromBundleByName(moduleManager!.currentConfig.moduleName)

             if (nextConfig != nil) {
                 if (isLocalFilesValid(nextConfig!)) {
                     CXLogUtil.v(tag, "---->>>> 检测下次生效的配置信息不为空, 且本地文件夹校验成功, 开始处理")

                     let destVersion = nextConfig!.moduleVersion.toFloatOrNull()
                     if (destVersion != nil) {
                         if (configList.isNotEmpty()) {
                             let iterate = configList.listIterator()
                             while (iterate.hasNext()) {
                                 let tmpConfig = iterate.next()
                                 let tmpVersion = tmpConfig.moduleVersion.toFloatOrNull()
                                 //版本号为空 或者 这是回滚操作,则清空大于等于该版本的所有文件/配置
                                 if (tmpVersion == nil || tmpVersion >= destVersion) {
                                     CXLogUtil.v(tag, "---->>>> 清空版本号为\(tmpVersion)(升级/回滚的目标版本为\(destVersion)) 的所有本地文件以及配置信息")
                                     iterate.remove()                                                                //删除在list中的位置
                                     CXFileUtil.deleteFile(CXHybirdUtil.getZipFile(tmpConfig))              //删除 zip
                                     CXFileUtil.deleteDirectory(CXHybirdUtil.getUnzipDir(tmpConfig))        //删除 unzipDir
                                 }
                             }
                         }
                         configList.add(0, nextConfig!)
                         CXHybirdBundleInfoManager.saveConfigListToBundleByName(moduleManager!.currentConfig.moduleName, configList)
                         moduleManager!.currentConfig = nextConfig!
                         CXLogUtil.e(tag, "---->>>> 重置当前 本地配置头 为:\(moduleManager!.currentConfig.moduleVersion)")
                     }
                 } else {
                     CXLogUtil.v(tag, "---->>>> 检测下次生效的配置信息不为空, 但是本地文件夹校验失败, 不做处理, 清除下次生效的该配置文件")
                 }


                 CXHybirdBundleInfoManager.removeNextConfigBundleByName(moduleManager!.currentConfig.moduleName)
             } else {
                 CXLogUtil.v(tag, "---->>>> 检测下次生效的配置信息为空,无需处理")
             }

             //ifNeed 体现在此处
             if (nextConfig != nil || mustFitLocal) {
                 CXHybirdUtil.fitLocalConfigsInfoSync(moduleName!)
             }
         } else {
             CXLogUtil.v(tag, "---->>>> 检测当前模块正在被浏览器加载,不能处理")
         }

         CXLogUtil.v(tag, "---->>>>  检测是否有下次启动生效的配置文件需要处理 结束, 当前线程:\(Thread.currentThread())")*/
    }

}
