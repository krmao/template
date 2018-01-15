package com.smart.library.bundle.util

import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.webkit.WebResourceResponse
import android.webkit.WebView
import com.smart.library.base.HKBaseApplication
import com.smart.library.bundle.HKHybird
import com.smart.library.bundle.manager.HKHybirdBundleInfoManager
import com.smart.library.bundle.manager.HKHybirdDownloadManager
import com.smart.library.bundle.manager.HKHybirdLifecycleManager
import com.smart.library.bundle.manager.HKHybirdModuleManager
import com.smart.library.bundle.model.HKHybirdModuleConfigModel
import com.smart.library.util.*
import com.smart.library.util.hybird.HKHybirdBridge
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.util.*
import java.util.zip.ZipException

@Suppress("MemberVisibilityCanPrivate", "unused")
object HKHybirdUtil {

    @JvmStatic
    fun getRootDir(moduleName: String): File = File(HKHybird.localRootDir, moduleName)

    @JvmStatic
    fun getZipFile(config: HKHybirdModuleConfigModel): File = File(getRootDir(config.moduleName), "${config.moduleName}-${config.moduleVersion}${HKHybird.bundleSuffix}")

    @JvmStatic
    fun getUnzipDir(config: HKHybirdModuleConfigModel): File? = File(getRootDir(config.moduleName), config.moduleVersion)

    /**
     * 校验配置信息
     *
     * 规则
     * 1: 文件夹校验成功则成功，zip包的校验不影响结果，如果zip包校验失败则删除zip包
     * 注意文件夹如果检验失败压缩包校验成功,则立即重新解压且返回校验成功,重新解压正确的压缩包不用重复校验文件夹内的各个文件
     */
    @JvmStatic
    fun isLocalFilesValid(config: HKHybirdModuleConfigModel?): Boolean {
        HKLogUtil.v(HKHybird.TAG + ":" + config?.moduleName, "<><><><><><><>文件校验 开始: 模块名称=${config?.moduleName} , 模块版本=${config?.moduleVersion}")
        var success = false
        val start = System.currentTimeMillis()
        if (config != null) {
            val zipFile = getZipFile(config)
            val unzipDir = getUnzipDir(config)
            if (!verifyLocalFiles(unzipDir, config.moduleFilesMd5, config.moduleName)) {
                if (!verifyZip(zipFile, config.moduleZipMd5, config.moduleName)) {
                    success = false
                } else {
                    success = unzipToLocal(zipFile, unzipDir)//解压后的文件夹校验失败，但是zip包校验成功，则重新解压即可
                }
            } else {
                success = true
            }
        }
        HKLogUtil.v(HKHybird.TAG + ":" + config?.moduleName, "<><><><><><><>文件校验 文件校验结束: 校验 ${if (success) "成功" else "失败"} , 模块名称=${config?.moduleName} , 模块版本=${config?.moduleVersion} , 耗时: ${System.currentTimeMillis() - start}ms")
        return success
    }

    @JvmStatic
    @Synchronized
    internal fun unzipToLocal(zipFile: File, unZipDir: File?): Boolean {
        var success = false
        HKFileUtil.deleteDirectory(unZipDir)
        try {
            HKZipUtil.unzip(zipFile, unZipDir)
            success = true
        } catch (exception: FileNotFoundException) {
            HKLogUtil.e(HKHybird.TAG, "--------[unzipToLocal]: 解压失败:文件不存在", exception)
        } catch (exception: IOException) {
            HKLogUtil.e(HKHybird.TAG, "--------[unzipToLocal]: 解压失败:文件读写错误", exception)
        } catch (exception: ZipException) {
            HKLogUtil.e(HKHybird.TAG, "--------[unzipToLocal]: 解压失败:压缩包错误", exception)
        }
        return success
    }

    /**
     * 校验失败删除压缩包
     */
    @JvmStatic
    internal fun verifyZip(zipFile: File?, moduleZipMd5: String?, logTag: String? = HKHybird.TAG): Boolean {
        var success = false
        if (zipFile != null && !TextUtils.isEmpty(moduleZipMd5)) {
            val zipFileExists = zipFile.exists()
            val zipFileMd5 = HKChecksumUtil.genMD5Checksum(zipFile)
            val isZipFileMd5Valid = zipFileMd5 == moduleZipMd5
            success = zipFileExists && isZipFileMd5Valid
        }
        HKLogUtil.v(HKHybird.TAG + ":" + logTag, "-- 文件校验 校验本地 zip 压缩包:${if (success) "成功" else "失败"}")
        if (!success) HKFileUtil.deleteFile(zipFile)
        return success
    }

    /**
     * 校验失败删除文件夹
     */
    @JvmStatic
    internal fun verifyLocalFiles(unZipDir: File?, moduleFilesMd5: HashMap<String, String>?, logTag: String? = HKHybird.TAG): Boolean {
        var success = false
        if (unZipDir != null && moduleFilesMd5 != null) {
            val localUnzipDirExists = unZipDir.exists()

            val rightFilesCount = moduleFilesMd5.size
            var validFilesCount = 0
            if (localUnzipDirExists) {
                HKFileUtil.getFileList(unZipDir).forEach {
                    val fileMd5 = HKChecksumUtil.genMD5Checksum(it)
                    val remotePath = it.absolutePath.replace(unZipDir.absolutePath, "")
                    val rightMd5 = moduleFilesMd5[remotePath]
                    val isFileMd5Valid = fileMd5 == rightMd5
                    if (isFileMd5Valid)
                        validFilesCount++
                    HKLogUtil.v(HKHybird.TAG + ":" + logTag, "-- 文件校验 校验文件(${it.name}) : ${if (isFileMd5Valid) "成功" else "失败"} , fileMd5:$fileMd5 , rightMd5:$rightMd5 , localPath:${it.path} ,remotePath:$remotePath")
                }
            }
            success = rightFilesCount == validFilesCount && localUnzipDirExists
        }
        HKLogUtil.v(HKHybird.TAG + ":" + logTag, "-- 文件校验 校验本地 zip 解压后的文件夹:${if (success) "成功" else "失败"}, path=$unZipDir")
        if (!success) HKFileUtil.deleteDirectory(unZipDir)
        return success
    }

    @JvmStatic
    fun getLocalFile(config: HKHybirdModuleConfigModel?, url: String?): File? {
        var localFile: File? = null
        if (config != null && url?.isNotBlank() == true) {
            val scheme = Uri.parse(url)?.scheme?.trim()
            if ("http".equals(scheme, true) || "https".equals(scheme, true)) {

                var tmpPath = url.substringAfter(config.moduleMainUrl, "")

                if (tmpPath.isNotBlank()) {

                    if (tmpPath.contains("#/")) {
                        //  #/index?userInfo=
                        //  index.shtml#/index?userInfo=
                        tmpPath = "index.shtml"
                    } else {
                        //  css/app.ae44e2d0f77af623eb2bcac61ceb2626.css
                        //  js/manifest.d01e227b32f52bdd60bd.js
                        //  js/app.6aff061a2dc3ffbfb27c.js
                        //  png/xxx
                    }
                    localFile = File(getUnzipDir(config)?.absolutePath + "/$tmpPath")
                }
            }
        } else {
            HKLogUtil.e(HKHybird.TAG + ":" + config?.moduleName, "params error ! config=$config , url=$url")
        }
        val exists = localFile?.exists() == true
        HKLogUtil.v(HKHybird.TAG + ":" + config?.moduleName, "检测到本地文件${if (!exists) " 不存在 " else " 存在"}, 文件路径:${localFile?.absolutePath}")
        if (!exists) localFile = null
        return localFile
    }

    @JvmStatic
    @Synchronized
    fun copyModuleZipFromAssets(moduleName: String, primaryConfig: HKHybirdModuleConfigModel?): Boolean {
        var success = false
        if (primaryConfig != null) {
            val zipFile = getZipFile(primaryConfig)
            val unzipDir = getUnzipDir(primaryConfig)
            try {
                HKFileUtil.deleteFile(zipFile)
                HKFileUtil.deleteDirectory(unzipDir)
                HKFileUtil.copy(HKBaseApplication.INSTANCE.assets.open("${HKHybird.assetsDirName}/$moduleName-${primaryConfig.moduleVersion}${HKHybird.bundleSuffix}"), zipFile)
                success = true
            } catch (exception: FileNotFoundException) {
                HKLogUtil.e(HKHybird.TAG + ":" + moduleName, "--------[copyModuleZipFromAssets]: 文件不存在", exception)
            } catch (exception: IOException) {
                HKLogUtil.e(HKHybird.TAG + ":" + moduleName, "--------[copyModuleZipFromAssets]: 文件读写错误", exception)
            }
        }
        return success
    }

    /**
     * @return 返回拷贝zip成功 并且解压zip到文件夹也成功的 configList
     */
    @JvmStatic
    @Synchronized
    fun getConfigListFromAssetsWithCopyAndUnzip(callback: (configList: MutableList<HKHybirdModuleConfigModel>) -> Unit) {
        val start = System.currentTimeMillis()
        HKLogUtil.v(HKHybird.TAG, "--------[getConfigListFromAssetsWithCopyAndUnzip: 开始]-----------------------------------------------------------------------------------")
        HKLogUtil.v(HKHybird.TAG, "--------[getConfigListFromAssetsWithCopyAndUnzip: 开始], 当前线程:${Thread.currentThread().name}, 当前时间:${HKTimeUtil.yMdHmsS(Date(start))}")
        var allConfigList: MutableList<HKHybirdModuleConfigModel> = mutableListOf()

        Observable.fromCallable {
            val allConfigJsonString = HKFileUtil.readTextFromFile(HKBaseApplication.INSTANCE.assets.open("${HKHybird.assetsDirName}/all${HKHybird.configSuffix}"))
            HKLogUtil.j(Log.VERBOSE, HKHybird.TAG, allConfigJsonString)

            try {
                allConfigList = HKJsonUtil.fromJson(allConfigJsonString, Array<HKHybirdModuleConfigModel>::class.java) ?: mutableListOf()
            } catch (exception: FileNotFoundException) {
                HKLogUtil.e(HKHybird.TAG, "--------[getConfigListFromAssetsWithCopyAndUnzip: 开始], 文件不存在, 当前线程:${Thread.currentThread().name}, 当前时间:${HKTimeUtil.yMdHmsS(Date(start))}", exception)
            } catch (exception: IOException) {
                HKLogUtil.e(HKHybird.TAG, "--------[getConfigListFromAssetsWithCopyAndUnzip: 开始], 文件读写错误, 当前线程:${Thread.currentThread().name}, 当前时间:${HKTimeUtil.yMdHmsS(Date(start))}", exception)
            }

            if (allConfigList.isNotEmpty()) {
                val iterator = allConfigList.iterator()
                while (iterator.hasNext()) {
                    val config = iterator.next()
                    val zipFile = getZipFile(config)
                    val unzipDir = getUnzipDir(config)

                    val copyStart = System.currentTimeMillis()
                    val copyPrimaryZipFromAssetsSuccess = copyModuleZipFromAssets(config.moduleName, config)
                    val copyTime = System.currentTimeMillis() - copyStart

                    if (copyPrimaryZipFromAssetsSuccess) {
                        val unzipStart = System.currentTimeMillis()
                        val unzipToLocalSuccess = unzipToLocal(zipFile, unzipDir)
                        val unzipTime = System.currentTimeMillis() - unzipStart

                        HKLogUtil.v(HKHybird.TAG, "--------[getConfigListFromAssetsWithCopyAndUnzip: 从 assets 拷贝 ${config.moduleName}.zip 成功, 解压${if (unzipToLocalSuccess) "成功" else "失败"}, 拷贝耗时:$copyTime ms, 解压耗时:$unzipTime ms, 当前线程:${Thread.currentThread().name}, 当前时间:${HKTimeUtil.yMdHmsS(Date(start))}")
                        if (!unzipToLocalSuccess) {
                            iterator.remove()
                            HKLogUtil.v(HKHybird.TAG, "--------[getConfigListFromAssetsWithCopyAndUnzip: 解压${config.moduleName}.zip 到文件夹失败, 从列表中删除 ${config.moduleName}, 当前线程:${Thread.currentThread().name}, 当前时间:${HKTimeUtil.yMdHmsS(Date(start))}")
                        }
                    } else {
                        HKLogUtil.v(HKHybird.TAG, "--------[getConfigListFromAssetsWithCopyAndUnzip: 从 assets 拷贝 ${config.moduleName}.zip 失败, 拷贝耗时:$copyTime ms, 当前线程:${Thread.currentThread().name}, 当前时间:${HKTimeUtil.yMdHmsS(Date(start))}")
                    }
                }
            }

        }.subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe {
            HKLogUtil.v(HKHybird.TAG, "--------[getConfigListFromAssetsWithCopyAndUnzip: 返回解压成功的 allConfigList.size=${allConfigList.size}], 当前线程:${Thread.currentThread().name}, 当前时间:${HKTimeUtil.yMdHmsS(Date(start))}")
            HKLogUtil.v(HKHybird.TAG, "--------[getConfigListFromAssetsWithCopyAndUnzip: 结束]-----------------------------------------------------------------------------------")
            HKLogUtil.v(HKHybird.TAG, "--------[getConfigListFromAssetsWithCopyAndUnzip: 结束], 当前线程:${Thread.currentThread().name}, 当前时间:${HKTimeUtil.yMdHmsS(Date())} , 一共耗时:${System.currentTimeMillis() - start}ms")
            callback.invoke(allConfigList)
        }
    }

    @JvmStatic
    @Synchronized
    fun downloadAllModules(configList: MutableList<HKHybirdModuleConfigModel>?, callback: ((validConfigList: MutableList<HKHybirdModuleConfigModel>?) -> Unit)? = null) {
        val start = System.currentTimeMillis()
        HKLogUtil.e(HKHybird.TAG, "--[downloadAllModules:全部下载开始]-----------------------------------------------------------------------------------")
        HKLogUtil.e(HKHybird.TAG, "--[downloadAllModules:全部下载开始]:${configList?.map { it.moduleName }}")
        HKLogUtil.e(HKHybird.TAG, "--[downloadAllModules:全部下载开始], 当前线程:${Thread.currentThread().name}, 当前时间:${HKTimeUtil.yMdHmsS(Date(start))}")
        if (configList == null || configList.isEmpty()) {
            HKLogUtil.e(HKHybird.TAG, "--[downloadAllModules:全部下载结束], 没有模块需要下载")
            HKLogUtil.e(HKHybird.TAG, "--[downloadAllModules:全部下载结束], 当前线程:${Thread.currentThread().name}, 当前时间:${HKTimeUtil.yMdHmsS(Date())} ,最终成功初始化的模块:${HKHybird.modules.map { it.key }} , 一共耗时:${System.currentTimeMillis() - start}ms")
            HKLogUtil.e(HKHybird.TAG, "--[downloadAllModules:全部下载结束]-----------------------------------------------------------------------------------")
            callback?.invoke(null)
        } else {
            Observable.zip(
                configList.map { config ->
                    Observable.create<HKHybirdModuleConfigModel> {
                        //下载好所有准备都充分后, 再进行初始化
                        HKLogUtil.e(HKHybird.TAG, "----开始下载子模块: ${config.moduleDownloadUrl}")
                        val _start = System.currentTimeMillis()
                        HKLogUtil.d(HKHybird.TAG, "--------[downloadAllModules:单模块下载:${config.moduleName}:开始], 当前线程:${Thread.currentThread().name}, 当前时间:${HKTimeUtil.yMdHmsS(Date(_start))}")
                        HKLogUtil.d(HKHybird.TAG, "--------[downloadAllModules:单模块下载:${config.moduleName}:开始], ${config.moduleDownloadUrl}")
                        HKHybirdDownloadManager.download(config) { isLocalFilesValid: Boolean ->

                            HKLogUtil.d(HKHybird.TAG, "--------[downloadAllModules:单模块下载:${config.moduleName}:结束], isLocalFilesValid:$isLocalFilesValid")
                            HKLogUtil.d(HKHybird.TAG, "--------[downloadAllModules:单模块下载:${config.moduleName}:结束], 当前线程:${Thread.currentThread().name}, 当前时间:${HKTimeUtil.yMdHmsS(Date())}, 耗时:${System.currentTimeMillis() - _start}ms, config=$config")
                            if (isLocalFilesValid) {
                                it.onNext(config)
                            } else {
                                it.onError(FileNotFoundException("--------[downloadAllModules:单模块下载:${config.moduleName}:校验失败] isLocalFilesValid=$isLocalFilesValid"))
                            }
                        }
                    }.onErrorReturnItem(HKHybirdModuleConfigModel.invalidConfigModel)
                }
                ,
                ({
                    it.map { it as HKHybirdModuleConfigModel }.filter { it != HKHybirdModuleConfigModel.invalidConfigModel }.toMutableList()
                })
            )
                .subscribe(
                    { validConfigList: MutableList<HKHybirdModuleConfigModel> ->
                        HKLogUtil.e(HKHybird.TAG, "--[downloadAllModules:全部下载结束]:success, 经校验有效的可以保存到 sharedPreference 的 validConfigList=${validConfigList.map { it.moduleName }}")
                        HKLogUtil.e(HKHybird.TAG, "--[downloadAllModules:全部下载结束]:success, 当前线程:${Thread.currentThread().name}, 当前时间:${HKTimeUtil.yMdHmsS(Date())}, 耗时:${System.currentTimeMillis() - start}ms")
                        HKLogUtil.e(HKHybird.TAG, "--[downloadAllModules:全部下载结束]-----------------------------------------------------------------------------------")
                        callback?.invoke(validConfigList)
                    }
                )

        }
    }

    fun removeIntercept(config: HKHybirdModuleConfigModel?) {
        if (config != null) {
            HKHybirdBridge.removeScheme(config.moduleMainUrl)
            HKHybirdBridge.removeRequest(config.moduleMainUrl)
        }
    }

    /**
     * 设置资源拦截器,URL拦截器
     */
    fun setIntercept(config: HKHybirdModuleConfigModel?) {
        val moduleName: String = config?.moduleName ?: ""
        val tag: String = HKHybird.TAG + ":" + moduleName

        HKLogUtil.v(tag, "======>> 设置拦截器开始: ${config?.moduleVersion}")

        if (config == null) {
            HKLogUtil.v(tag, "======>> 检测到 配置文件为空 , 取消设置拦截器 return")
            return
        }

        val interceptUrl = config.moduleMainUrl

        if (interceptUrl.isBlank()) {
            HKLogUtil.e(tag, "======>> 检测到 interceptMainUrl == null return")
            return
        }

        HKHybirdBridge.removeScheme(interceptUrl)
        HKHybirdBridge.removeRequest(interceptUrl)

        /**
         * webView.loadUrl 不会触发此回调,放到 HKHybirdBridge.addRequest(interceptMainUrl) 里面处理
         * http://www.jianshu.com/p/3474cb8096da
         */
        /*HKLogUtil.v(config.HKHybird.TAG +":" + moduleName, "增加 URL 拦截 , 匹配 -> interceptMainUrl : $interceptUrl")
        HKHybirdBridge.addScheme(interceptUrl) { _: WebView?, webViewClient: WebViewClient?, url: String?, callback: (() -> Unit?)? ->
            lifecycleManager.onWebViewOpenPage(webViewClient, url)

            HKLogUtil.e(config.HKHybird.TAG +":" + moduleName, "系统拦截到模块URL请求: url=$url ,匹配到 检测内容为 '$interceptUrl' 的拦截器, 由于当前模块的策略为 '$updateStrategy' , ${if (updateStrategy == HKHybirdUpdateStrategy.ONLINE) "需要检测更新,开始更新" else "不需要检查更新,不拦截 return"}")

            //仅仅更新策略为 ONLINE 时,才会执行此步骤
            if (updateStrategy == HKHybirdUpdateStrategy.ONLINE) {
                checkUpdate(synchronized = false, switchToOnlineModeIfRemoteVersionChanged = true, callback = callback)
            }
            true
        }*/

        HKLogUtil.v(tag, "======>> 增加 资源 拦截 , 匹配 -> interceptUrl : $interceptUrl")
        HKHybirdBridge.addRequest(interceptUrl) { _: WebView?, url: String? ->
            HKLogUtil.v(tag, "======>> shouldInterceptRequest: $url ,匹配拦截器:$interceptUrl")
            var resourceResponse: WebResourceResponse? = null
            if (HKHybird.modules[moduleName]?.onlineModel != true) {
                val localFile = HKHybirdUtil.getLocalFile(config, url)
                if (url != null && localFile?.exists() == true) {
                    val mimeType: String = when {
                        url.contains(".css") -> "text/css"
                        url.contains(".png") -> "image/png"
                        url.contains(".js") -> "application/x-javascript"
                        url.contains(".woff") -> "application/x-font-woff"
                        url.contains(".html") -> "text/html"
                        url.contains(".shtml") -> "text/html"
                        else -> "text/html"
                    }
                    try {
                        HKLogUtil.v(tag, "======>> 执行伪造本地资源")
                        resourceResponse = WebResourceResponse(mimeType, "UTF-8", FileInputStream(localFile))
                    } catch (e: Exception) {
                        HKLogUtil.e(tag, "======>> 伪造本地资源出错", e)
                    }
                } else {
                    HKLogUtil.v(tag, "======>> 系统检测到本地文件不存在,访问在线资源")
                }
            } else {
                HKLogUtil.v(tag, "======>> 系统检测到当前为在线模式,访问在线资源")
            }
            resourceResponse
        }
        HKLogUtil.v(tag, "======>> 设置拦截器结束")
    }

    /**
     * 校验所有版本信息,如果无效则删除该版本相关的所有文件/配置
     * 重置当前版本指向
     * 如果本模块尚未被打开,则下一次启动生效的更新/回滚配置 在此一并兼容
     */
    @JvmStatic
    @Synchronized
    fun fitLocalConfigsInfoSync(moduleName: String?) {
        if (moduleName.isNullOrBlank()) {
            return
        }
        val moduleManager: HKHybirdModuleManager = HKHybird.modules[moduleName] ?: return

        val tag = HKHybird.TAG + ":" + moduleName


        HKLogUtil.e(tag, "||||||||=====>>>>> 一次检验本地可用配置信息的完整性(同步) 开始 , 当前线程:${Thread.currentThread().name} , (如果本模块没有被浏览器加载,则优先合并 下次启动生效的任务, 当前 onLineMode = ${moduleManager.onlineModel})")
        val start = System.currentTimeMillis()
        val configList = HKHybirdBundleInfoManager.getConfigListFromBundleByName(moduleName)
        HKLogUtil.e(tag, "||||||||=====>>>>> 当前最新配置信息为: ${configList.map { it.moduleVersion }}")
        if (configList.isNotEmpty()) {
            HKLogUtil.v(tag, "||||||||=====>>>>> 检测到本地配置信息不为空,开始过滤/清理无效的本地配置信息")

            //直到找到有效的版本，如果全部无效，则在后续步骤中重新解压原始版本
            val iterate = configList.listIterator()
            while (iterate.hasNext()) {
                if (!HKHybirdUtil.isLocalFilesValid(iterate.next())) {
                    iterate.remove() // mind ConcurrentModificationException
                }
            }
        }

        //删除无效的配置信息,删除后会重新判断空,如果是空会获取原始安装包
        if (configList.isEmpty()) {
            HKLogUtil.w(tag, "||||||||=====>>>>> 检测到本地配置信息为空,从全局你变量 modules 中删除")
            HKHybird.removeModule(moduleName)
            HKLogUtil.e(tag, "||||||||=====>>>>> 清空 本模块 结束, 当前本地可操作模块: ${HKHybird.modules.keys}")
        } else {
            //如果有删除的或则新加的原始配置信息，则需要重新保存下
            HKHybirdBundleInfoManager.saveConfigListToBundleByName(moduleName, configList)
            configList.firstOrNull()?.let { moduleManager.currentConfig = it }
            HKLogUtil.e(tag, "||||||||=====>>>>> 重置当前 本地配置头 为:${moduleManager.currentConfig.moduleVersion}")

        }
        HKLogUtil.e(tag, "||||||||=====>>>>> 一次检验本地所有可用配置信息(不包含 next)的完整性(同步) 结束 , 当前线程:${Thread.currentThread().name} , (如果本模块没有被浏览器加载,则优先合并 下次启动生效的任务, 当前 onLineMode = ${moduleManager.onlineModel}) ,  耗时: ${System.currentTimeMillis() - start}ms")
    }


    /**
     * 当检测到模块完全没有被浏览器加载的时候,可以调用此方法是否有 下次加载生效的配置信息 ,并异步处理本地文件,重置当前模块的 版本头信息
     *
     * 调用时机
     * 1: 当前模块退出浏览器的时候(异步处理)
     * 2: 当前模块第一次被浏览器加载的时候(同步处理)
     */
    @JvmStatic
    internal fun fitNextAndFitLocalIfNeedConfigsInfo(moduleName: String?) {
        Observable.fromCallable { fitNextAndFitLocalIfNeedConfigsInfoSync(moduleName) }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe()
    }

    /**
     * 同步 处理下次生效的配置信息以及处理完后 如果需要的话(即确实有下次生效的配置信息的情况下,避免检查下次配置信息的时候多做一次处理本地配置信息的操作)紧接着处理本地配置信息
     */
    @JvmStatic
    @Synchronized
    internal fun fitNextAndFitLocalIfNeedConfigsInfoSync(moduleName: String?, mustFitLocal: Boolean = false) {
        if (moduleName.isNullOrBlank()) {
            return
        }
        val moduleManager: HKHybirdModuleManager = HKHybird.modules[moduleName] ?: return

        val tag = HKHybird.TAG + ":" + moduleName

        HKLogUtil.v(tag, "---->>>> 检测是否有下次启动生效的配置文件需要处理 开始, 当前线程:${Thread.currentThread().name}")

        val configList = HKHybirdBundleInfoManager.getConfigListFromBundleByName(moduleManager.currentConfig.moduleName)

        val isModuleOpened = HKHybirdLifecycleManager.isModuleOpened(moduleName)
        if (!isModuleOpened) {
            HKLogUtil.v(tag, "---->>>> 检测当前模块未被浏览器加载,可以处理")

            val nextConfigStack = HKHybirdBundleInfoManager.getNextConfigFromBundleByName(moduleManager.currentConfig.moduleName)
            if (nextConfigStack != null) {
                HKLogUtil.v(tag, "---->>>> 检测下次生效的配置信息不为空,开始处理")
                val destVersion = nextConfigStack.moduleVersion.toFloatOrNull()
                if (destVersion != null) {
                    if (configList.isNotEmpty()) {
                        val iterate = configList.listIterator()
                        while (iterate.hasNext()) {
                            val tmpConfig = iterate.next()
                            val tmpVersion = tmpConfig.moduleVersion.toFloatOrNull()
                            //版本号为空 或者 这是回滚操作,则清空大于等于该版本的所有文件/配置
                            if (tmpVersion == null || tmpVersion >= destVersion) {
                                HKLogUtil.v(tag, "---->>>> 清空版本号为$tmpVersion(升级/回滚的目标版本为$destVersion) 的所有本地文件以及配置信息")
                                iterate.remove()                                                                //删除在list中的位置
                                HKFileUtil.deleteFile(HKHybirdUtil.getZipFile(tmpConfig))              //删除 zip
                                HKFileUtil.deleteDirectory(HKHybirdUtil.getUnzipDir(tmpConfig))        //删除 unzipDir
                            }
                        }
                    }
                    configList.add(0, nextConfigStack)
                    HKHybirdBundleInfoManager.saveConfigListToBundleByName(moduleManager.currentConfig.moduleName, configList)
                    moduleManager.currentConfig = nextConfigStack
                    HKLogUtil.e(tag, "---->>>> 重置当前 本地配置头 为:${moduleManager.currentConfig.moduleVersion}")
                }
                HKHybirdBundleInfoManager.removeNextConfigBundleByName(moduleManager.currentConfig.moduleName)
            } else {
                HKLogUtil.v(tag, "---->>>> 检测下次生效的配置信息为空,无需处理")
            }

            //ifNeed 体现在此处
            if (nextConfigStack != null || mustFitLocal) HKHybirdUtil.fitLocalConfigsInfoSync(moduleName)
        } else {
            HKLogUtil.v(tag, "---->>>> 检测当前模块正在被浏览器加载,不能处理")
        }

        HKLogUtil.v(tag, "---->>>>  检测是否有下次启动生效的配置文件需要处理 结束, 当前线程:${Thread.currentThread().name}")
    }

}
