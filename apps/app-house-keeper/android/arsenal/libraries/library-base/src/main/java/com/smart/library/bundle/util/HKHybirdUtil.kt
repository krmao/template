package com.smart.library.bundle.util

import android.net.Uri
import android.text.TextUtils
import android.webkit.WebResourceResponse
import android.webkit.WebView
import com.smart.library.base.HKBaseApplication
import com.smart.library.bundle.HKHybird
import com.smart.library.bundle.manager.HKHybirdDownloadManager
import com.smart.library.bundle.model.HKHybirdModuleConfigModel
import com.smart.library.util.*
import com.smart.library.util.hybird.HKHybirdBridge
import io.reactivex.Observable
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
    fun getRootDir(moduleName: String): File = File(HKHybird.LOCAL_ROOT_DIR, moduleName)

    @JvmStatic
    fun getZipFile(config: HKHybirdModuleConfigModel): File = File(getRootDir(config.moduleName), "${config.moduleName}-${config.moduleVersion}${HKHybird.BUNDLE_SUFFIX}")

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
        HKLogUtil.v(HKHybird.TAG + ":" + config?.moduleName, "文件校验开始(注意文件夹如果检验失败压缩包校验成功,则立即重新解压且返回校验成功,重新解压正确的压缩包不用重复校验文件夹内的各个文件): 模块名称=${config?.moduleName} , 模块版本=${config?.moduleVersion}")
        var success = false
        val start = System.currentTimeMillis()
        if (config != null) {
            val zipFile = getZipFile(config)
            val unzipDir = getUnzipDir(config)
            if (!verifyLocalFiles(unzipDir, config.moduleFilesMd5, config.moduleName)) {
                if (!verifyZip(zipFile, config.moduleZipMd5)) {
                    success = false
                } else {
                    success = unzipToLocal(zipFile, unzipDir)//解压后的文件夹校验失败，但是zip包校验成功，则重新解压即可

                    HKLogUtil.e(HKHybird.TAG + ":" + config.moduleName, "由于 zip文件 校验成功, 此时直接解压zip, 得到默认校验成功文件夹 , 解压文件夹成功?$success")
                }
            } else {
                success = true
            }
        }
        HKLogUtil.v(HKHybird.TAG + ":" + config?.moduleName, "文件校验结束: 校验 ${if (success) "成功" else "失败"} , 模块名称=${config?.moduleName} , 模块版本=${config?.moduleVersion} , 耗时: ${System.currentTimeMillis() - start}ms")
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
            HKLogUtil.e(HKHybird.TAG, "解压失败:文件不存在", exception)
        } catch (exception: IOException) {
            HKLogUtil.e(HKHybird.TAG, "解压失败:文件读写错误", exception)
        } catch (exception: ZipException) {
            HKLogUtil.e(HKHybird.TAG, "解压失败:压缩包错误", exception)
        }
        return success
    }

    /**
     * 校验失败删除压缩包
     */
    @JvmStatic
    internal fun verifyZip(zipFile: File?, moduleZipMd5: String?): Boolean {
        var success = false
        if (zipFile != null && !TextUtils.isEmpty(moduleZipMd5)) {
            val zipFileExists = zipFile.exists()
            val zipFileMd5 = HKChecksumUtil.genMD5Checksum(zipFile)
            val isZipFileMd5Valid = zipFileMd5 == moduleZipMd5
            success = zipFileExists && isZipFileMd5Valid
        }
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
                    HKLogUtil.v(HKHybird.TAG + ":" + logTag, "-------------- 校验文件 -(${it.name}) : ${if (isFileMd5Valid) "成功" else "失败"} , fileMd5:$fileMd5 , rightMd5:$rightMd5 , localPath:${it.path} ,remotePath:$remotePath")
                }
            }
            success = rightFilesCount == validFilesCount && localUnzipDirExists
        }
        HKLogUtil.v(HKHybird.TAG + ":" + logTag, "校验本地文件夹(${unZipDir?.name}):${if (success) "成功" else "失败"}, unZipDir:$unZipDir ")
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
    fun copyPrimaryZipFromAssets(moduleName: String, primaryConfig: HKHybirdModuleConfigModel?): Boolean {
        var success = false
        if (primaryConfig != null) {
            val zipFile = getZipFile(primaryConfig)
            val unzipDir = getUnzipDir(primaryConfig)
            try {
                HKFileUtil.deleteFile(zipFile)
                HKFileUtil.deleteDirectory(unzipDir)
                HKFileUtil.copy(HKBaseApplication.INSTANCE.assets.open("${HKHybird.ASSETS_DIR_NAME}/$moduleName-${primaryConfig.moduleVersion}${HKHybird.BUNDLE_SUFFIX}"), zipFile)
                success = true
            } catch (exception: FileNotFoundException) {
                HKLogUtil.e(HKHybird.TAG + ":" + moduleName, "文件不存在", exception)
            } catch (exception: IOException) {
                HKLogUtil.e(HKHybird.TAG + ":" + moduleName, "文件读写错误", exception)
            }
        }
        return success
    }

    @JvmStatic
    @Synchronized
    fun getPrimaryConfigFromAssets(moduleName: String): HKHybirdModuleConfigModel? {
        var primaryConfig: HKHybirdModuleConfigModel? = null
        try {
            primaryConfig = HKJsonUtil.fromJson(HKFileUtil.readTextFromFile(HKBaseApplication.INSTANCE.assets.open("${HKHybird.ASSETS_DIR_NAME}/$moduleName${HKHybird.CONFIG_SUFFIX}")), HKHybirdModuleConfigModel::class.java)
            if (primaryConfig != null) {
                val zipFile = getZipFile(primaryConfig)
                val unzipDir = getUnzipDir(primaryConfig)
                if (copyPrimaryZipFromAssets(moduleName, primaryConfig)) {
                    unzipToLocal(zipFile, unzipDir)
                }
            }
        } catch (exception: FileNotFoundException) {
            HKLogUtil.e(HKHybird.TAG + ":" + moduleName, "文件不存在", exception)
        } catch (exception: IOException) {
            HKLogUtil.e(HKHybird.TAG + ":" + moduleName, "文件读写错误", exception)
        }
        return primaryConfig
    }

    @JvmStatic
    @Synchronized
    fun getConfigListFromAssets(callback: (configList: MutableList<HKHybirdModuleConfigModel>) -> Unit) {
        var start = System.currentTimeMillis()
        HKLogUtil.v(HKHybird.TAG, "--------[getConfigNamesFromAssets:开始]-----------------------------------------------------------------------------------")
        HKLogUtil.v(HKHybird.TAG, "--------[getConfigNamesFromAssets:开始], 当前线程:${Thread.currentThread().name}, 当前时间:${HKTimeUtil.yMdHmsS(Date(start))}")

        val assetsBundleNames = HKBaseApplication.INSTANCE.assets.list(HKHybird.ASSETS_DIR_NAME).filter { !TextUtils.isEmpty(it) && it.endsWith(HKHybird.CONFIG_SUFFIX) }.map { it.replace(HKHybird.CONFIG_SUFFIX, "") }

        HKLogUtil.v(HKHybird.TAG, "--------[getConfigNamesFromAssets:结束]-----------------------------------------------------------------------------------")
        HKLogUtil.v(HKHybird.TAG, "--------[getConfigNamesFromAssets:结束], 当前线程:${Thread.currentThread().name}, 当前时间:${HKTimeUtil.yMdHmsS(Date())} , 一共耗时:${System.currentTimeMillis() - start}ms")


        start = System.currentTimeMillis()
        HKLogUtil.w(HKHybird.TAG, "--------[getConfigListFromAssets:开始]-----------------------------------------------------------------------------------")
        HKLogUtil.w(HKHybird.TAG, "--------[getConfigListFromAssets:开始], 当前线程:${Thread.currentThread().name}, 当前时间:${HKTimeUtil.yMdHmsS(Date(start))}")
        Observable.zip(
            assetsBundleNames.map {
                Observable.fromCallable {
                    val _start = System.currentTimeMillis()
                    HKLogUtil.d(HKHybird.TAG, "--------[getConfigListFromAssets:$it:开始], 当前线程:${Thread.currentThread().name}, 当前时间:${HKTimeUtil.yMdHmsS(Date(_start))}")
                    val config: HKHybirdModuleConfigModel? = HKHybirdUtil.getPrimaryConfigFromAssets(it)
                    HKLogUtil.d(HKHybird.TAG, "--------[getConfigListFromAssets:$it:结束], 当前线程:${Thread.currentThread().name}, 当前时间:${HKTimeUtil.yMdHmsS(Date())}, 耗时:${System.currentTimeMillis() - _start}ms, config=$config")
                    config
                }.subscribeOn(Schedulers.io())
            }
            ,
            ({
                it.mapNotNull { it as?  HKHybirdModuleConfigModel }.toMutableList()
            })
        ).subscribe {
            HKLogUtil.w(HKHybird.TAG, "--------[getConfigListFromAssets:结束]-----------------------------------------------------------------------------------")
            HKLogUtil.w(HKHybird.TAG, "--------[getConfigListFromAssets:结束], 当前线程:${Thread.currentThread().name}, 当前时间:${HKTimeUtil.yMdHmsS(Date())} ,最终成功初始化的模块:${HKHybird.MODULES.map { it.key }} , 一共耗时:${System.currentTimeMillis() - start}ms")
            callback.invoke(it)
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
            HKLogUtil.e(HKHybird.TAG, "--[downloadAllModules:全部下载结束], 当前线程:${Thread.currentThread().name}, 当前时间:${HKTimeUtil.yMdHmsS(Date())} ,最终成功初始化的模块:${HKHybird.MODULES.map { it.key }} , 一共耗时:${System.currentTimeMillis() - start}ms")
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
        HKLogUtil.v(HKHybird.TAG + ":" + config?.moduleName, "设置拦截器开始: ${config?.moduleVersion}")

        if (config == null) {
            HKLogUtil.v(HKHybird.TAG + ":" + config?.moduleName, "检测到 配置文件为空 , 取消设置拦截器 return")
            return
        }

        val interceptUrl = config.moduleMainUrl

        if (interceptUrl.isBlank()) {
            HKLogUtil.e(HKHybird.TAG + ":" + config.moduleName, "检测到 interceptMainUrl == null return")
            return
        }

        HKHybirdBridge.removeScheme(interceptUrl)
        HKHybirdBridge.removeRequest(interceptUrl)

        /**
         * webView.loadUrl 不会触发此回调,放到 HKHybirdBridge.addRequest(interceptMainUrl) 里面处理
         * http://www.jianshu.com/p/3474cb8096da
         */
        /*HKLogUtil.v(config.HKHybird.TAG +":" + config?.moduleName, "增加 URL 拦截 , 匹配 -> interceptMainUrl : $interceptUrl")
        HKHybirdBridge.addScheme(interceptUrl) { _: WebView?, webViewClient: WebViewClient?, url: String?, callback: (() -> Unit?)? ->
            lifecycleManager.onWebViewOpenPage(webViewClient, url)

            HKLogUtil.e(config.HKHybird.TAG +":" + config?.moduleName, "系统拦截到模块URL请求: url=$url ,匹配到 检测内容为 '$interceptUrl' 的拦截器, 由于当前模块的策略为 '$updateStrategy' , ${if (updateStrategy == HKHybirdUpdateStrategy.ONLINE) "需要检测更新,开始更新" else "不需要检查更新,不拦截 return"}")

            //仅仅更新策略为 ONLINE 时,才会执行此步骤
            if (updateStrategy == HKHybirdUpdateStrategy.ONLINE) {
                checkUpdate(synchronized = false, switchToOnlineModeIfRemoteVersionChanged = true, callback = callback)
            }
            true
        }*/

        HKLogUtil.v(HKHybird.TAG + ":" + config.moduleName, "增加 资源 拦截 , 匹配 -> interceptUrl : $interceptUrl")
        HKHybirdBridge.addRequest(interceptUrl) { _: WebView?, url: String? ->
            HKLogUtil.v(HKHybird.TAG + ":" + config.moduleName, "shouldInterceptRequest: $url ,匹配拦截器:$interceptUrl")
            var resourceResponse: WebResourceResponse? = null
            if (HKHybird.MODULES[config.moduleName]?.onlineModel != true) {
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
                        HKLogUtil.v(HKHybird.TAG + ":" + config.moduleName, "执行伪造本地资源")
                        resourceResponse = WebResourceResponse(mimeType, "UTF-8", FileInputStream(localFile))
                    } catch (e: Exception) {
                        HKLogUtil.e(HKHybird.TAG + ":" + config.moduleName, "伪造本地资源出错", e)
                    }
                } else {
                    HKLogUtil.v(HKHybird.TAG + ":" + config.moduleName, "系统检测到本地文件不存在,访问在线资源")
                }
            } else {
                HKLogUtil.v(HKHybird.TAG + ":" + config.moduleName, "系统检测到当前为在线模式,访问在线资源")
            }
            resourceResponse
        }
        HKLogUtil.v(HKHybird.TAG + ":" + config.moduleName, "设置拦截器结束")
    }

}
