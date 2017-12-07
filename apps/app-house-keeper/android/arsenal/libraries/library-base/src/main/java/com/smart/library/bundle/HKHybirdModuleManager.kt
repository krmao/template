package com.smart.library.bundle

import android.net.Uri
import android.text.TextUtils
import android.webkit.WebResourceResponse
import android.webkit.WebView
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

@Suppress("MemberVisibilityCanPrivate", "unused", "UNCHECKED_CAST", "PrivatePropertyName")
class HKHybirdModuleManager(val moduleFullName: String) {

    private val TAG = HKHybirdModuleManager::class.java.simpleName + ":" + moduleFullName
    private val KEY_CONFIGURATION = "key-$moduleFullName"

    /**
     * zip准备好后保存到本地, verify Local Zip==false 时从本地删除
     */
    //TODO 新版本文件破坏，读取原始版本，但是原始版本本地存在，则校验成功？
    //TODO 本地文件存在，只是配置丢失，注意此时情况
    var localConfiguration: HKHybirdModuleConfiguration? = HKPreferencesUtil.getEntity(KEY_CONFIGURATION, HKHybirdModuleConfiguration::class.java) ?: getConfigurationFromAssets()
        set(value) {
            HKPreferencesUtil.putEntity(KEY_CONFIGURATION, value)
            field = value
        }


    private val localRootDir = File(HKHybirdManager.LOCAL_ROOT_DIR, moduleFullName)
    private var localZipFile: File = getLocalZipFile(localConfiguration?.moduleVersion)
    private var localUnzipDir: File = getLocalUnZipDir(localConfiguration?.moduleVersion)
    private var verifySuccess: Boolean = false
    private val updateManager: HKHybirdUpdateManager = HKHybirdUpdateManager(this)

    fun getLocalZipFile(moduleVersion: String?): File = File(localRootDir, "${localConfiguration?.moduleName}-$moduleVersion${HKHybirdManager.BUNDLE_SUFFIX}")
    fun getLocalUnZipDir(moduleVersion: String?): File = File(localRootDir, moduleVersion)

    private fun verifySync(): Boolean {
        synchronized(this) {
            verifySuccess = false
            val start = System.currentTimeMillis()
            if (!verifySuccess) {
                if (!verifyLocalFiles(localUnzipDir, localConfiguration?.moduleFilesMd5)) {
                    if (!verifyZip(localZipFile, localConfiguration?.moduleZipMd5)) {
                        localConfiguration = null
                        copyZipFromAssets()
                    }
                    verifySuccess = unzipToLocal(localZipFile, localUnzipDir)
                } else {
                    verifySuccess = true
                }
                updateManager.checkUpdate()
                HKLogUtil.e(TAG, "verify $verifySuccess , 耗时: ${System.currentTimeMillis() - start}ms")
            } else {
                HKLogUtil.e(TAG, "verify $verifySuccess , 耗时: ${System.currentTimeMillis() - start}ms , [无需重复校验,直接返回 success]")
            }
            addRequestIntercept(localUnzipDir, localConfiguration)
        }
        return verifySuccess
    }

    fun verify(callback: ((localUnzipDir: File, configuration: HKHybirdModuleConfiguration?) -> Unit)? = null) {
        HKLogUtil.w(TAG, "verify >>>>>>>>>>===============>>>>>>>>>>")
        val start = System.currentTimeMillis()
        Observable.fromCallable { verifySync() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { callback?.invoke(localUnzipDir, localConfiguration) }
        HKLogUtil.w(TAG, "verify async-progressing , 耗时: ${System.currentTimeMillis() - start}ms")
        HKLogUtil.w(TAG, "verify <<<<<<<<<<===============<<<<<<<<<<")
    }

    private fun getConfigurationFromAssets(): HKHybirdModuleConfiguration? {
        val start = System.currentTimeMillis()
        var configuration: HKHybirdModuleConfiguration? = null
        try {
            configuration = HKJsonUtil.fromJson(HKFileUtil.readTextFromFile(HKBaseApplication.INSTANCE.assets.open("${HKHybirdManager.ASSETS_DIR_NAME}/$moduleFullName${HKHybirdManager.CONFIG_SUFFIX}")), HKHybirdModuleConfiguration::class.java)
            HKLogUtil.d(TAG, "getConfigurationFromAssets success , 耗时: ${System.currentTimeMillis() - start}ms \nconfiguration:$configuration")
        } catch (exception: FileNotFoundException) {
            HKLogUtil.e(TAG, "getConfigurationFromAssets failure , 耗时: ${System.currentTimeMillis() - start}ms", exception)
        } catch (exception: IOException) {
            HKLogUtil.e(TAG, "getConfigurationFromAssets failure , 耗时: ${System.currentTimeMillis() - start}ms", exception)
        }
        return configuration
    }

    private fun copyZipFromAssets(): Boolean {
        var success = false
        val start = System.currentTimeMillis()
        HKFileUtil.deleteFile(localZipFile)
        HKFileUtil.deleteDirectory(localUnzipDir)
        try {
            localConfiguration = getConfigurationFromAssets()
            HKFileUtil.copy(HKBaseApplication.INSTANCE.assets.open("${HKHybirdManager.ASSETS_DIR_NAME}/$moduleFullName-${localConfiguration?.moduleVersion}${HKHybirdManager.BUNDLE_SUFFIX}"), localZipFile)
            success = true
            HKLogUtil.d(TAG, "copyZipFromAssets success ,localZipFile.exists?${localZipFile.exists()} , 耗时: ${System.currentTimeMillis() - start}ms")
        } catch (exception: FileNotFoundException) {
            HKLogUtil.e(TAG, "copyZipFromAssets failure , 耗时: ${System.currentTimeMillis() - start}ms", exception)
        } catch (exception: IOException) {
            HKLogUtil.e(TAG, "copyZipFromAssets failure , 耗时: ${System.currentTimeMillis() - start}ms", exception)
        }
        return success
    }

    internal fun unzipToLocal(zipFile: File, unZipDir: File): Boolean {
        var success = false
        val start = System.currentTimeMillis()
        HKFileUtil.deleteDirectory(unZipDir)
        try {
            HKZipUtil.unzip(zipFile, unZipDir)
            success = true
            HKLogUtil.d(TAG, "unzipToLocal success , 耗时: ${System.currentTimeMillis() - start}ms")
        } catch (exception: FileNotFoundException) {
            HKLogUtil.e(TAG, "unzipToLocal failure , 耗时: ${System.currentTimeMillis() - start}ms", exception)
        } catch (exception: IOException) {
            HKLogUtil.e(TAG, "unzipToLocal failure , 耗时: ${System.currentTimeMillis() - start}ms", exception)
        } catch (exception: ZipException) {
            HKLogUtil.e(TAG, "unzipToLocal failure , 耗时: ${System.currentTimeMillis() - start}ms", exception)
        }
        return success
    }

    internal fun verifyZip(zipFile: File?, moduleZipMd5: String?): Boolean {
        val start = System.currentTimeMillis()
        var success = false
        if (zipFile != null && !TextUtils.isEmpty(moduleZipMd5)) {
            val zipFileExists = zipFile.exists()
            val zipFileMd5 = HKChecksumUtil.genMD5Checksum(zipFile)
            val isZipFileMd5Valid = zipFileMd5 == moduleZipMd5
            success = zipFileExists && isZipFileMd5Valid
            HKLogUtil.d(TAG, "verifyZip (${zipFile.name}) : ${if (success) "success" else "failure"}, zip文件是否存在:$zipFileExists, MD5是否正确:$isZipFileMd5Valid, 耗时: ${System.currentTimeMillis() - start}ms")
        } else {
            HKLogUtil.e(TAG, "verifyZip (${zipFile?.name}) : ${if (success) "success" else "failure"}, zipFile or moduleZipMd5 is null, 耗时: ${System.currentTimeMillis() - start}ms")
        }
        return false
    }

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
                    val rightMd5 = moduleFilesMd5.get(remotePath)
                    val isFileMd5Valid = fileMd5 == rightMd5
                    if (!isFileMd5Valid)
                        invalidFilesNum++
                    HKLogUtil.d(TAG, "verifyLocalFiles : isFileMd5Valid:$isFileMd5Valid , fileMd5:$fileMd5 , rightMd5:$rightMd5 , localPath:${it.path} ,remotePath:$remotePath")
                }
                success = invalidFilesNum == 0 && localUnzipDirExists && localIndexExists
            }
            HKLogUtil.w(TAG, "verifyLocalFiles(${unZipDir.name}) : ${if (success) "success" else "failure"}, invalidFilesNum:$invalidFilesNum, localUnzipDirExists:$localUnzipDirExists, localIndexExists:$localIndexExists, 耗时: ${System.currentTimeMillis() - start}ms")
        } else {
            HKLogUtil.e(TAG, "verifyLocalFiles(${unZipDir?.name}) : ${if (success) "success" else "failure"}, unZipDir or moduleFilesMd5 is null, 耗时: ${System.currentTimeMillis() - start}ms")
        }
        return false
    }

    private fun addRequestIntercept(localUnzipDir: File, configuration: HKHybirdModuleConfiguration?) {
        val interceptScriptUrl = configuration?.moduleScriptUrl?.get(HKHybirdManager.EVN) ?: return
        val interceptMainUrl = configuration.moduleMainUrl[HKHybirdManager.EVN] ?: return

        //main url
        HKHybirdBridge.addScheme(interceptMainUrl) { webView: WebView?, url: String? ->
            checkUpdate()
            false
        }

        //html
        HKHybirdBridge.addRequest(interceptMainUrl) { _: WebView?, url: String? ->
            var resourceResponse: WebResourceResponse? = null
            if (!TextUtils.isEmpty(url)) {
                val requestUrl = Uri.parse(url)
                val scheme = requestUrl?.scheme?.trim()
                if (requestUrl != null && ("http".equals(scheme, true) || "https".equals(scheme, true))) {
                    val requestUrlString = requestUrl.toString()
                    if (interceptMainUrl == requestUrlString) {
                        val mimeType = "text/html"
                        val localPath = localUnzipDir.absolutePath + "/" + requestUrlString.substringBefore("#", requestUrlString).split("/").last()
                        val localFileExists = File(localPath).exists()
                        HKLogUtil.e(TAG, "**** do intercept request ? $localFileExists **** [originPath: " + requestUrl.toString() + "], [localPath: $localPath]")
                        if (localFileExists) {
                            try {
                                resourceResponse = WebResourceResponse(mimeType, "UTF-8", FileInputStream(localPath))
                            } catch (e: Exception) {
                                HKLogUtil.e(TAG, "**** do intercept request ? false **** ", e)
                            }
                        } else {
                            HKLogUtil.e(TAG, "**** do intercept request ? false **** [originPath: $url], [interceptHost: $interceptScriptUrl], [localPath(exist?$localFileExists): $localPath]")
                        }
                    } else {
                        HKLogUtil.e(TAG, "**** do intercept request ? false **** [originPath: $url], [interceptHost: $interceptScriptUrl], interceptMainUrl != requestUrlString")
                    }
                } else {
                    HKLogUtil.e(TAG, "**** do intercept request ? false **** [originPath: $url], [interceptHost: $interceptScriptUrl], requestUrl ==null || scheme != http && scheme != https")
                }
            } else {
                HKLogUtil.e(TAG, "**** do intercept request ? false **** [originPath: $url], [interceptHost: $interceptScriptUrl], url is empty")
            }
            resourceResponse
        }

        //css,js,image
        HKHybirdBridge.addRequest(interceptScriptUrl) { _: WebView?, url: String? ->
            var resourceResponse: WebResourceResponse? = null
            if (!TextUtils.isEmpty(url)) {
                val requestUrl = Uri.parse(url)
                val scheme = requestUrl?.scheme?.trim()
                if (requestUrl != null && ("http".equals(scheme, true) || "https".equals(scheme, true))) {
                    if (url!!.contains(interceptScriptUrl, true)) {

                        val requestUrlString = requestUrl.toString()

                        val mimeType: String = when {
                            requestUrlString.contains(".css") -> "text/css"
                            requestUrlString.contains(".png") -> "image/png"
                            requestUrlString.contains(".js") -> "application/x-javascript"
                            requestUrlString.contains(".woff") -> "application/x-font-woff"
                            requestUrlString.contains(".html") -> "text/html"
                            requestUrlString.contains(".shtml") -> "text/html"
                            else -> "text/html"
                        }
                        val tmpPath = requestUrlString
                            .replace(interceptScriptUrl, "")
                            .replace("https://", "")
                            .replace("http://", "")
                        val localPath = localUnzipDir.absolutePath + tmpPath

                        val localFileExists = File(localPath).exists()
                        HKLogUtil.e(TAG, "**** do intercept request ? $localFileExists **** [originPath: $requestUrlString], [localPath: $localPath]")
                        if (localFileExists) {
                            try {
                                resourceResponse = WebResourceResponse(mimeType, "UTF-8", FileInputStream(localPath))
                            } catch (e: Exception) {
                                HKLogUtil.e(TAG, "**** do intercept request ? false **** ", e)
                            }
                        } else {
                            HKLogUtil.e(TAG, "**** do intercept request ? false **** [originPath: $url], [interceptHost: $interceptScriptUrl], [localPath(exist?$localFileExists): $localPath]")
                        }
                    } else {
                        HKLogUtil.e(TAG, "**** do intercept request ? false **** [originPath: $url], [interceptHost: $interceptScriptUrl], interceptMainUrl != requestUrlString")
                    }
                } else {
                    HKLogUtil.e(TAG, "**** do intercept request ? false **** [originPath: $url], [interceptHost: $interceptScriptUrl], requestUrl ==null || scheme != http && scheme != https")
                }
            } else {
                HKLogUtil.e(TAG, "**** do intercept request ? false **** [originPath: $url], [interceptHost: $interceptScriptUrl], url is empty")
            }
            resourceResponse
        }
    }

    //TODO
    fun checkUpdate() = updateManager.checkUpdate()

    fun setDownloader(downloader: (downloadUrl: String, file: File?, callback: (File?) -> Unit?) -> Unit?) {
        updateManager.downloader = downloader
    }

    fun setConfiger(configer: (configUrl: String, callback: (HKHybirdModuleConfiguration?) -> Unit?) -> Unit?) {
        updateManager.configer = configer
    }
}
