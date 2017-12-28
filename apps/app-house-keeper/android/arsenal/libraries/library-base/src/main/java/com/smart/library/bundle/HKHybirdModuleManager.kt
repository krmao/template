package com.smart.library.bundle

import android.app.Activity
import android.net.Uri
import android.os.StrictMode
import android.text.TextUtils
import android.util.Log
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import com.smart.library.base.HKBaseApplication
import com.smart.library.util.*
import com.smart.library.util.hybird.HKHybirdBridge
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.util.zip.ZipException

/**
 * 全责范围
 *
 * 1: 负责对本模块各版本的健康体检，自动删除无效版本，删除后自动还原到上一个最新版本，如果没有则还原到原始安装包版本
 * 2: 不负责下载更新升级等操作，这些操作放在 HKHybirdUpdateManager
 */
@Suppress("MemberVisibilityCanPrivate", "unused", "UNCHECKED_CAST", "PrivatePropertyName")
class HKHybirdModuleManager(val moduleFullName: String) {

    internal val TAG = HKHybirdModuleManager::class.java.simpleName + ":" + moduleFullName

    internal val configManager: HKHybirdConfigManager = HKHybirdConfigManager(this)
    internal val updateManager: HKHybirdUpdateManager = HKHybirdUpdateManager(this)

    internal val rootDir = File(HKHybirdManager.LOCAL_ROOT_DIR, moduleFullName)

    /**
     * 1: 检测到有新的版本变化,开始异步下载更新,立即切换为在线模式
     * 2: 更新时机必须在浏览器加载本模块之前,如果本模块并没有在浏览器中打开,则直接更新,如果已经打开,则一直使用在线模式
     * 3: 如果已经是在线模式,则不执行重复检查更新,不执行重复的健康体检
     * 4: 重启app或者执行正式更新替换之前,检查更新包的完整性
     */
    var onLineModel: Boolean = false

    fun getZipFile(configuration: HKHybirdConfigModel?): File = File(rootDir, "${configuration?.moduleName}-${configuration?.moduleVersion}${HKHybirdManager.BUNDLE_SUFFIX}")
    fun getUnzipDir(configuration: HKHybirdConfigModel?): File? = File(rootDir, configuration?.moduleVersion)

    /**
     * 健康体检，检查模块完整性
     * 每次打开浏览器时执行
     *
     * 由于 initLocalConfiguration 是同步的，所以在任何地方都可以调用  checkHealth 而不会重复校验,是序列化的
     */
    @Synchronized
    fun checkHealth(callback: ((localUnzipDir: File?, configuration: HKHybirdConfigModel?) -> Unit)? = null) {
        HKLogUtil.w(TAG, "checkHealth >>>>>>>>>>===============>>>>>>>>>>")
        val start = System.currentTimeMillis()
        if (configManager.currentConfig == null || !isLocalFilesValid(configManager.currentConfig)) {

            Observable.fromCallable {
                initLocalConfiguration()
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    callback?.invoke(getUnzipDir(configManager.currentConfig), configManager.currentConfig)
                    HKLogUtil.e(TAG, "checkHealth complete , 耗时: ${System.currentTimeMillis() - start}ms")
                    HKLogUtil.w(TAG, "checkHealth <<<<<<<<<<===============<<<<<<<<<<")
                }
            HKLogUtil.w(TAG, "checkHealth async-progressing , 耗时: ${System.currentTimeMillis() - start}ms")
        } else {
            callback?.invoke(getUnzipDir(configManager.currentConfig), configManager.currentConfig)
            HKLogUtil.e(TAG, "checkHealth complete , 耗时: ${System.currentTimeMillis() - start}ms")
            HKLogUtil.w(TAG, "checkHealth <<<<<<<<<<===============<<<<<<<<<<")
        }
    }

    /**
     * 安全的删除所有大于 @param latestConfiguration 的版本的 临时配置信息以及与之相关的本地文件，包括压缩包和解压文件夹以及sharedPreference里面的配置信息
     */
    @Deprecated("下次启动生效")
    @Synchronized //同步，确保所有其他操作在此期间都是等待状态，最好这里是原子性的, @Synchronized注解锁住的是对该类对象的访问操作
    fun completeRemoveAllGTLatestConfigSafely(latestConfiguration: HKHybirdConfigModel?) {
        HKLogUtil.e(TAG, "completeRemoveAllGTLatestConfigSafely start")
        val start = System.currentTimeMillis()
        if (latestConfiguration != null) {
            val latestVersion = latestConfiguration.moduleVersion.toFloatOrNull()
            if (latestVersion != null) {
                val configurationList = configManager.getConfigList()

                val iterate = configurationList.listIterator()
                while (iterate.hasNext()) {
                    val tmpConfiguration = iterate.next()
                    val zipFile = getZipFile(tmpConfiguration)
                    val unzipDir = getUnzipDir(tmpConfiguration)
                    val tmpVersion = tmpConfiguration.moduleVersion.toFloatOrNull()
                    if (tmpVersion == null || tmpVersion > latestVersion) {
                        iterate.remove()                            //删除在list中的位置
                        HKFileUtil.deleteFile(zipFile)              //删除 zip
                        HKFileUtil.deleteDirectory(unzipDir)        //删除 unzipDir
                    }
                }
                configManager.saveConfig(configurationList)  //彻底删除配置信息，至此已经删除了所有与本版本相关的信息
                initLocalConfiguration()                            //重置 currentConfig ，并且list为空自动重新拷贝原始安装包
            }
        }
        HKLogUtil.e(TAG, "completeRemoveAllGTLatestConfigSafely   end  耗时:${System.currentTimeMillis() - start}ms")
    }

    fun checkUpdate() {
        updateManager.checkUpdate()
    }

    fun checkUpdateSync(): Boolean {
        return updateManager.checkUpdateSync()
    }

    fun setDownloader(downloader: (downloadUrl: String, file: File?, callback: (File?) -> Unit?) -> Unit?) {
        updateManager.downloader = downloader
    }

    fun setConfiger(configer: (configUrl: String, callback: (HKHybirdConfigModel?) -> Boolean?) -> Boolean?) {
        updateManager.configer = configer
    }

    /**
     * 赋值最新版本信息
     * 做到可以回滚
     * 只校验有配置信息的，校验失败可以删除，但是没有校验信息的本地文件夹不要删除，下次下载好配置信息好先检测本地zip包是否已经有了，避免重复下载，如果有解压后的文件夹但是没有zip包，不删除，可以直接
     */
    @Synchronized
    internal fun initLocalConfiguration() {
        HKLogUtil.e(TAG, "initLocalConfiguration start")
        val start = System.currentTimeMillis()
        val configurationList = configManager.getConfigList()//读取配置信息
        HKLogUtil.w(TAG, "sorted list : ${configurationList.map { it.moduleVersion }}")
        if (configurationList.isNotEmpty()) {
            HKLogUtil.w(TAG, "configurationList not empty")

            //直到找到有效的版本，如果全部无效，则在后续步骤中重新解压原始版本
            val iterate = configurationList.listIterator()
            while (iterate.hasNext()) {
                if (!isLocalFilesValid(iterate.next())) {
                    iterate.remove() // mind ConcurrentModificationException
                }
            }
        }

        //删除无效的配置信息,删除后会重新判断空,如果是空会获取原始安装包
        if (configurationList.isEmpty()) {
            HKLogUtil.w(TAG, "configurationList is empty")
            getConfigurationFromAssets()?.let { configurationList.add(it) } //如果为空则添加原始配置信息
        }
        //如果有删除的或则新加的原始配置信息，则需要重新保存下
        configManager.saveConfig(configurationList)

        configManager.currentConfig = null
        configurationList.firstOrNull()?.let { configManager.currentConfig = it }
        HKLogUtil.e(TAG, "initLocalConfiguration reset currentConfig:${configManager.currentConfig}")
        HKLogUtil.e(TAG, "initLocalConfiguration   end  耗时:${System.currentTimeMillis() - start}ms")
        return
    }

    /**
     * 校验配置信息
     *
     * 规则
     * 1: 文件夹校验成功则成功，zip包的校验不影响结果，如果zip包校验失败则删除zip包
     */
    fun isLocalFilesValid(configuration: HKHybirdConfigModel?): Boolean {
        HKLogUtil.w(TAG, "verify start ${configuration?.moduleName}:${configuration?.moduleVersion}")
        val success: Boolean
        val start = System.currentTimeMillis()
        val zipFile = getZipFile(configuration)
        val unzipDir = getUnzipDir(configuration)
        if (!verifyLocalFiles(unzipDir, configuration?.moduleFilesMd5)) {
            if (!verifyZip(zipFile, configuration?.moduleZipMd5)) {
                success = false
            } else {
                success = unzipToLocal(zipFile, unzipDir)//解压后的文件夹校验失败，但是zip包校验成功，则重新解压即可
            }
        } else {
            success = true
        }
        HKLogUtil.w(TAG, "verify   end ${configuration?.moduleName}:${configuration?.moduleVersion} , verify ${if (success) "success" else "failure"} , 耗时: ${System.currentTimeMillis() - start}ms")
        return success
    }

    @Synchronized
    internal fun getConfigurationFromAssets(): HKHybirdConfigModel? {
        val start = System.currentTimeMillis()
        var configuration: HKHybirdConfigModel? = null
        try {
            configuration = HKJsonUtil.fromJson(HKFileUtil.readTextFromFile(HKBaseApplication.INSTANCE.assets.open("${HKHybirdManager.ASSETS_DIR_NAME}/$moduleFullName${HKHybirdManager.CONFIG_SUFFIX}")), HKHybirdConfigModel::class.java)
            HKLogUtil.w(TAG, "getConfigurationFromAssets success , 耗时: ${System.currentTimeMillis() - start}ms \nconfiguration:$configuration")

            val zipFile = getZipFile(configuration)
            val unzipDir = getUnzipDir(configuration)
            if (copyPrimaryZipFromAssets(configuration)) {
                unzipToLocal(zipFile, unzipDir)
            }
        } catch (exception: FileNotFoundException) {
            HKLogUtil.e(TAG, "getConfigurationFromAssets failure , 耗时: ${System.currentTimeMillis() - start}ms", exception)
        } catch (exception: IOException) {
            HKLogUtil.e(TAG, "getConfigurationFromAssets failure , 耗时: ${System.currentTimeMillis() - start}ms", exception)
        }
        return configuration
    }

    @Synchronized
    internal fun copyPrimaryZipFromAssets(primaryConfiguration: HKHybirdConfigModel?): Boolean {
        var success = false
        val start = System.currentTimeMillis()
        try {
            val zipFile = getZipFile(primaryConfiguration)
            val unzipDir = getUnzipDir(primaryConfiguration)

            HKFileUtil.deleteFile(zipFile)
            HKFileUtil.deleteDirectory(unzipDir)
            HKFileUtil.copy(HKBaseApplication.INSTANCE.assets.open("${HKHybirdManager.ASSETS_DIR_NAME}/$moduleFullName-${primaryConfiguration?.moduleVersion}${HKHybirdManager.BUNDLE_SUFFIX}"), zipFile)
            success = true
            HKLogUtil.w(TAG, "copyPrimaryZipFromAssets success ,localZipFile.exists?${zipFile.exists()} , 耗时: ${System.currentTimeMillis() - start}ms")
        } catch (exception: FileNotFoundException) {
            HKLogUtil.e(TAG, "copyPrimaryZipFromAssets failure , 耗时: ${System.currentTimeMillis() - start}ms", exception)
        } catch (exception: IOException) {
            HKLogUtil.e(TAG, "copyPrimaryZipFromAssets failure , 耗时: ${System.currentTimeMillis() - start}ms", exception)
        }
        return success
    }

    @Synchronized
    internal fun unzipToLocal(zipFile: File, unZipDir: File?): Boolean {
        var success = false
        val start = System.currentTimeMillis()
        HKFileUtil.deleteDirectory(unZipDir)
        try {
            HKZipUtil.unzip(zipFile, unZipDir)
            success = true
            HKLogUtil.w(TAG, "unzipToLocal success , 耗时: ${System.currentTimeMillis() - start}ms")
        } catch (exception: FileNotFoundException) {
            HKLogUtil.e(TAG, "unzipToLocal failure , 耗时: ${System.currentTimeMillis() - start}ms", exception)
        } catch (exception: IOException) {
            HKLogUtil.e(TAG, "unzipToLocal failure , 耗时: ${System.currentTimeMillis() - start}ms", exception)
        } catch (exception: ZipException) {
            HKLogUtil.e(TAG, "unzipToLocal failure , 耗时: ${System.currentTimeMillis() - start}ms", exception)
        }
        return success
    }

    /**
     * 校验失败删除压缩包
     */
    internal fun verifyZip(zipFile: File?, moduleZipMd5: String?): Boolean {
        val start = System.currentTimeMillis()
        var success = false
        if (zipFile != null && !TextUtils.isEmpty(moduleZipMd5)) {
            val zipFileExists = zipFile.exists()
            val zipFileMd5 = HKChecksumUtil.genMD5Checksum(zipFile)
            val isZipFileMd5Valid = zipFileMd5 == moduleZipMd5
            success = zipFileExists && isZipFileMd5Valid
            HKLogUtil.w(TAG, "verifyZip (${zipFile.name}) : ${if (success) "success" else "failure"}, zip文件是否存在:$zipFileExists, MD5是否正确:$isZipFileMd5Valid, 耗时: ${System.currentTimeMillis() - start}ms")
        } else {
            HKLogUtil.e(TAG, "verifyZip (${zipFile?.name}) : ${if (success) "success" else "failure"}, zipFile or moduleZipMd5 is null, 耗时: ${System.currentTimeMillis() - start}ms")
        }
        if (!success) HKFileUtil.deleteFile(zipFile)
        return success
    }

    /**
     * 校验失败删除文件夹
     */
    internal fun verifyLocalFiles(unZipDir: File?, moduleFilesMd5: HashMap<String, String>?): Boolean {
        val start = System.currentTimeMillis()
        var success = false
        if (unZipDir != null && moduleFilesMd5 != null) {
            val localUnzipDirExists = unZipDir.exists()
            val localIndexExists = File(unZipDir, "index.shtml").exists()
            var invalidFilesNum = 0
            if (localIndexExists && localIndexExists) {
                HKFileUtil.getFileList(unZipDir).forEach {
                    val fileMd5 = HKChecksumUtil.genMD5Checksum(it)
                    val remotePath = it.absolutePath.replace(unZipDir.absolutePath, "")
                    val rightMd5 = moduleFilesMd5[remotePath]
                    val isFileMd5Valid = fileMd5 == rightMd5
                    if (!isFileMd5Valid)
                        invalidFilesNum++
                    HKLogUtil.w(TAG, "verifyLocalFiles : isFileMd5Valid:$isFileMd5Valid , fileMd5:$fileMd5 , rightMd5:$rightMd5 , localPath:${it.path} ,remotePath:$remotePath")
                }
                success = invalidFilesNum == 0 && localUnzipDirExists && localIndexExists
            }
            HKLogUtil.w(TAG, "verifyLocalFiles(${unZipDir.name}) : ${if (success) "success" else "failure"}, invalidFilesNum:$invalidFilesNum, localUnzipDirExists:$localUnzipDirExists, localIndexExists:$localIndexExists, 耗时: ${System.currentTimeMillis() - start}ms")
        } else {
            HKLogUtil.e(TAG, "verifyLocalFiles(${unZipDir?.name}) : ${if (success) "success" else "failure"}, unZipDir or moduleFilesMd5 is null, 耗时: ${System.currentTimeMillis() - start}ms")
        }
        if (!success) HKFileUtil.deleteDirectory(unZipDir)
        return success
    }

    /**
     * return file or null
     *
     * "https://h.jia.chexiangpre.com/cx/cxj/cxjappweb/base/example.shtml"
     * "https://h.jia.chexiangpre.com/cx/cxj/cxjappweb/base/example.shtml#route"
     * "https://h.jia.chexiangpre.com/cx/cxj/cxjappweb/base/example.shtml#route/2"
     * "https://h.jia.chexiangpre.com/cx/cxj/cxjappweb/base/example.shtml#route/child/3"
     *
     *
     *
     * interceptMainUrl == "https://h.jia.chexiangpre.com/cx/cxj/cxjappweb/base"
     * localFilePath    == unzipDir/example.shtml
     *
     */
    fun getLocalFile(url: String?): File? {
        return getLocalHtmlFile(url) ?: getLocalScriptFile(url)
    }

    fun getLocalHtmlFile(url: String?): File? {
        var localFile: File? = null
        if (url?.isNotBlank() == true) {
            val scheme = Uri.parse(url)?.scheme?.trim()
            if ("http".equals(scheme, true) || "https".equals(scheme, true)) {
                val mainBaseUrl = configManager.currentConfig?.moduleMainUrl?.get(HKHybirdManager.EVN)
                if (mainBaseUrl?.isNotBlank() == true && url.startsWith(mainBaseUrl, true)) {
                    localFile = File(getUnzipDir(configManager.currentConfig)?.absolutePath + "/" + url.substringBefore("#").split("/").last()).takeIf {
                        HKLogUtil.v(TAG, "检测到本地文件路径=${it.absolutePath}")
                        it.exists()
                    }
                }
            }
        }
        HKLogUtil.v(TAG, "检测到本地文件${if (localFile == null) " 不存在 " else " 存在 "}")
        return localFile
    }

    fun getLocalScriptFile(url: String?): File? {
        var localFile: File? = null
        if (url?.isNotBlank() == true) {
            val scheme = Uri.parse(url)?.scheme?.trim()
            if ("http".equals(scheme, true) || "https".equals(scheme, true)) {
                val mainBaseUrl = configManager.currentConfig?.moduleScriptUrl?.get(HKHybirdManager.EVN)
                if (mainBaseUrl?.isNotBlank() == true && url.startsWith(mainBaseUrl, true)) {
                    localFile = File(getUnzipDir(configManager.currentConfig)?.absolutePath + "/" + url.substringAfter(mainBaseUrl)).takeIf {
                        HKLogUtil.v(TAG, "检测到本地文件路径=${it.absolutePath}")
                        it.exists()
                    }
                }
            }
        }
        HKLogUtil.v(TAG, "检测到本地文件${if (localFile == null) " 不存在 " else " 存在 "}")
        return localFile
    }

    private val webViewClientSet: MutableSet<WebViewClient?> = mutableSetOf()

    fun isModuleOpenNow(): Boolean {
        HKLogUtil.e(TAG, "webViewClientSet.size=${webViewClientSet.size}")
        return !webViewClientSet.isEmpty()
    }

    fun onWebViewOpenPage(webViewClient: WebViewClient?) {
        if (webViewClient != null)
            webViewClientSet.add(webViewClient)
        HKLogUtil.e(TAG, "webViewClientSet.size=${webViewClientSet.size}")
    }

    fun onWebViewClose(webViewClient: WebViewClient?) {
        if (webViewClient != null)
            webViewClientSet.remove(webViewClient)
        HKLogUtil.e(TAG, "webViewClientSet.size=${webViewClientSet.size}")
    }

    internal fun setIntercept(configuration: HKHybirdConfigModel?) {
        HKLogUtil.v("设置拦截器开始: $configuration")

        if (configuration == null) return

        val interceptScriptUrl = configuration.moduleScriptUrl[HKHybirdManager.EVN]
        if (interceptScriptUrl == null || interceptScriptUrl.isBlank()) {
            HKLogUtil.e(TAG, "interceptScriptUrl == null")
            return
        }

        val interceptMainUrl = configuration.moduleMainUrl[HKHybirdManager.EVN]

        if (interceptMainUrl == null || interceptMainUrl.isBlank()) {
            HKLogUtil.e(TAG, "interceptMainUrl == null")
            return
        }

        HKHybirdBridge.removeScheme(interceptMainUrl)
        HKHybirdBridge.removeRequest(interceptMainUrl)
        HKHybirdBridge.removeRequest(interceptScriptUrl)

        /**
         * webView.loadUrl 不会触发此回调,放到 HKHybirdBridge.addRequest(interceptMainUrl) 里面处理
         * http://www.jianshu.com/p/3474cb8096da
         */
        HKLogUtil.w(TAG, "addScheme interceptMainUrl : $interceptMainUrl")
        HKHybirdBridge.addScheme(interceptMainUrl) { _: WebView?, webViewClient: WebViewClient?, url: String? ->
            //            checkUpdate()
            onWebViewOpenPage(webViewClient)

            //======================================================================================
            // 暂时修改系统策略
            //======================================================================================
            val oldThreadPolicy = StrictMode.getThreadPolicy()
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())
            //======================================================================================

            val needUpdate = checkUpdateSync()
            HKLogUtil.v(TAG, "needUpdate:$needUpdate")

            //======================================================================================
            // 还原系统策略
            //======================================================================================
            StrictMode.setThreadPolicy(oldThreadPolicy)
            //======================================================================================

            false
        }

        //html
        HKLogUtil.v(TAG, "addRequest interceptMainUrl : $interceptMainUrl")
        HKHybirdBridge.addRequest(interceptMainUrl) { _: WebView?, url: String? ->
            HKLogUtil.v(TAG, "系统检测到资源访问请求: $url")
            var resourceResponse: WebResourceResponse? = null
            if (!onLineModel) {
                val localFile = getLocalHtmlFile(url)
                if (localFile?.exists() == true) {
                    try {
                        HKLogUtil.v(TAG, "执行伪造本地资源")
                        resourceResponse = WebResourceResponse("text/html", "UTF-8", FileInputStream(localFile.absolutePath))
                    } catch (e: Exception) {
                        HKLogUtil.e(TAG, "伪造本地资源出错", e)
                    }
                } else {
                    HKLogUtil.v(TAG, "系统检测到本地文件不存在,访问在线资源")
                }
            } else {
                HKLogUtil.v(TAG, "系统检测到当前为在线模式,访问在线资源")
            }
            resourceResponse
        }

        HKLogUtil.w(TAG, "interceptScriptUrl : $interceptScriptUrl")
        //css,js,image
        HKHybirdBridge.addRequest(interceptScriptUrl) { _: WebView?, url: String? ->
            HKLogUtil.v(TAG, "系统检测到资源访问请求: $url")
            var resourceResponse: WebResourceResponse? = null
            if (!onLineModel) {
                val localFile = getLocalScriptFile(url)
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
                        HKLogUtil.v(TAG, "执行伪造本地资源")
                        resourceResponse = WebResourceResponse(mimeType, "UTF-8", FileInputStream(localFile))
                    } catch (e: Exception) {
                        HKLogUtil.e(TAG, "伪造本地资源出错", e)
                    }
                } else {
                    HKLogUtil.v(TAG, "系统检测到本地文件不存在,访问在线资源")
                }
            } else {
                HKLogUtil.v(TAG, "系统检测到当前为在线模式,访问在线资源")
            }
            resourceResponse
        }
        HKLogUtil.v("设置拦截器结束")
    }

}
