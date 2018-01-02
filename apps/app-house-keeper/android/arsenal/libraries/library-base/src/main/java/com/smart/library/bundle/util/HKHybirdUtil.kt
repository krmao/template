package com.smart.library.bundle.util

import android.net.Uri
import android.text.TextUtils
import com.smart.library.base.HKBaseApplication
import com.smart.library.bundle.model.HKHybirdConfigModel
import com.smart.library.bundle.HKHybird
import com.smart.library.util.*
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.util.zip.ZipException

@Suppress("MemberVisibilityCanPrivate", "unused")
object HKHybirdUtil {

    private val TAG = HKHybirdUtil::class.java.simpleName

    @JvmStatic
    fun getRootDir(moduleName: String?): File = File(HKHybird.LOCAL_ROOT_DIR, moduleName)

    @JvmStatic
    fun getZipFile(config: HKHybirdConfigModel?): File = File(getRootDir(config?.moduleName), "${config?.moduleName}-${config?.moduleVersion}${HKHybird.BUNDLE_SUFFIX}")

    @JvmStatic
    fun getUnzipDir(config: HKHybirdConfigModel?): File? = File(getRootDir(config?.moduleName), config?.moduleVersion)

    /**
     * 校验配置信息
     *
     * 规则
     * 1: 文件夹校验成功则成功，zip包的校验不影响结果，如果zip包校验失败则删除zip包
     * 注意文件夹如果检验失败压缩包校验成功,则立即重新解压且返回校验成功,重新解压正确的压缩包不用重复校验文件夹内的各个文件
     */
    @JvmStatic
    fun isLocalFilesValid(config: HKHybirdConfigModel?): Boolean {
        HKLogUtil.v(config?.moduleName, "文件校验开始(注意文件夹如果检验失败压缩包校验成功,则立即重新解压且返回校验成功,重新解压正确的压缩包不用重复校验文件夹内的各个文件): 模块名称=${config?.moduleName} , 模块版本=${config?.moduleVersion}")
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
                }
            } else {
                success = true
            }
        }
        HKLogUtil.v(config?.moduleName, "文件校验结束: 校验 ${if (success) "成功" else "失败"} , 模块名称=${config?.moduleName} , 模块版本=${config?.moduleVersion} , 耗时: ${System.currentTimeMillis() - start}ms")
        return success
    }

    @JvmStatic
    @Synchronized
    internal fun unzipToLocal(zipFile: File, unZipDir: File?): Boolean {
        var success = false
        val start = System.currentTimeMillis()
        HKFileUtil.deleteDirectory(unZipDir)
        try {
            HKZipUtil.unzip(zipFile, unZipDir)
            success = true
            HKLogUtil.v(TAG, "unzipToLocal success , 耗时: ${System.currentTimeMillis() - start}ms")
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
    @JvmStatic
    internal fun verifyZip(zipFile: File?, moduleZipMd5: String?): Boolean {
        val start = System.currentTimeMillis()
        var success = false
        if (zipFile != null && !TextUtils.isEmpty(moduleZipMd5)) {
            val zipFileExists = zipFile.exists()
            val zipFileMd5 = HKChecksumUtil.genMD5Checksum(zipFile)
            val isZipFileMd5Valid = zipFileMd5 == moduleZipMd5
            success = zipFileExists && isZipFileMd5Valid
            HKLogUtil.v(TAG, "verifyZip (${zipFile.name}) : ${if (success) "success" else "failure"}, zip文件是否存在:$zipFileExists, MD5是否正确:$isZipFileMd5Valid, 耗时: ${System.currentTimeMillis() - start}ms")
        } else {
            HKLogUtil.e(TAG, "verifyZip (${zipFile?.name}) : ${if (success) "success" else "failure"}, zipFile or moduleZipMd5 is null, 耗时: ${System.currentTimeMillis() - start}ms")
        }
        if (!success) HKFileUtil.deleteFile(zipFile)
        return success
    }

    /**
     * 校验失败删除文件夹
     */
    @JvmStatic
    internal fun verifyLocalFiles(unZipDir: File?, moduleFilesMd5: HashMap<String, String>?, logTag: String? = TAG): Boolean {
        val start = System.currentTimeMillis()
        HKLogUtil.v(logTag, "校验本地文件夹 开始")

        var success = false
        if (unZipDir != null && moduleFilesMd5 != null) {
            val localUnzipDirExists = unZipDir.exists()

            val rightFilesCount = moduleFilesMd5.size
            var validFilesCount = 0
            HKLogUtil.v(logTag, "校验本地文件夹(${unZipDir.name}) : 检测到本地文件夹 ${if (localUnzipDirExists) "存在" else "不存在"}")
            if (localUnzipDirExists) {
                HKFileUtil.getFileList(unZipDir).forEach {
                    val fileMd5 = HKChecksumUtil.genMD5Checksum(it)
                    val remotePath = it.absolutePath.replace(unZipDir.absolutePath, "")
                    val rightMd5 = moduleFilesMd5[remotePath]
                    val isFileMd5Valid = fileMd5 == rightMd5
                    if (isFileMd5Valid)
                        validFilesCount++

                    HKLogUtil.v(logTag, "---- 校验文件 -(${it.name}) : ${if (isFileMd5Valid) "成功" else "失败"} , fileMd5:$fileMd5 , rightMd5:$rightMd5 , localPath:${it.path} ,remotePath:$remotePath")
                }
            }

            success = rightFilesCount == validFilesCount && localUnzipDirExists

            HKLogUtil.v(logTag, "校验本地文件夹(${unZipDir.name}) :  ${if (success) "成功" else "失败"}")
        } else {
            HKLogUtil.e(logTag, "校验本地文件夹 (${unZipDir?.name}) :  ${if (success) "成功" else "失败"}, unZipDir:$unZipDir or moduleFilesMd5:$moduleFilesMd5 is null")
        }
        if (!success) HKFileUtil.deleteDirectory(unZipDir)

        HKLogUtil.v(logTag, "校验本地文件夹 结束 ,校验结果:$success  ,耗时:${System.currentTimeMillis() - start}ms")
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
    @JvmStatic
    fun getLocalFile(config: HKHybirdConfigModel?, url: String?): File? {
        return getLocalHtmlFile(config, url) ?: getLocalScriptFile(config, url)
    }

    @JvmStatic
    fun getLocalHtmlFile(config: HKHybirdConfigModel?, url: String?): File? {
        var localFile: File? = null
        if (url?.isNotBlank() == true) {
            val scheme = Uri.parse(url)?.scheme?.trim()
            if ("http".equals(scheme, true) || "https".equals(scheme, true)) {
                val mainBaseUrl = config?.moduleMainUrl?.get(HKHybird.EVN)
                if (mainBaseUrl?.isNotBlank() == true && url.startsWith(mainBaseUrl, true)) {
                    localFile = File(getUnzipDir(config)?.absolutePath + "/" + url.substringBefore("#").split("/").last()).takeIf {
                        it.exists()
                    }
                }
            }
        }
        HKLogUtil.v(config?.moduleName, "检测到本地文件(${if (localFile == null) " 不存在 " else " 存在 , path=${localFile.absolutePath}"})")
        return localFile
    }

    @JvmStatic
    fun getLocalScriptFile(config: HKHybirdConfigModel?, url: String?): File? {
        var localFile: File? = null
        if (url?.isNotBlank() == true) {
            val scheme = Uri.parse(url)?.scheme?.trim()
            if ("http".equals(scheme, true) || "https".equals(scheme, true)) {
                val mainBaseUrl = config?.moduleScriptUrl?.get(HKHybird.EVN)
                if (mainBaseUrl?.isNotBlank() == true && url.startsWith(mainBaseUrl, true)) {
                    localFile = File(getUnzipDir(config)?.absolutePath + "/" + url.substringAfter(mainBaseUrl)).takeIf {
                        HKLogUtil.v(config.moduleName, "检测到本地文件路径=${it.absolutePath}")
                        it.exists()
                    }
                }
            }
        }
        HKLogUtil.v(config?.moduleName, "检测到本地文件${if (localFile == null) " 不存在 " else " 存在 "}")
        return localFile
    }

    @JvmStatic
    @Synchronized
    fun copyPrimaryZipFromAssets(moduleName: String, primaryConfig: HKHybirdConfigModel?): Boolean {
        HKLogUtil.v(moduleName, "从 assets 拷贝/解压原始包 开始")
        var success = false
        val start = System.currentTimeMillis()
        val zipFile = HKHybirdUtil.getZipFile(primaryConfig)
        val unzipDir = HKHybirdUtil.getUnzipDir(primaryConfig)
        try {
            HKFileUtil.deleteFile(zipFile)
            HKFileUtil.deleteDirectory(unzipDir)
            HKFileUtil.copy(HKBaseApplication.INSTANCE.assets.open("${HKHybird.ASSETS_DIR_NAME}/$moduleName-${primaryConfig?.moduleVersion}${HKHybird.BUNDLE_SUFFIX}"), zipFile)
            success = true
        } catch (exception: FileNotFoundException) {
            HKLogUtil.e(moduleName, "文件不存在", exception)
        } catch (exception: IOException) {
            HKLogUtil.e(moduleName, "文件读写错误", exception)
        }
        HKLogUtil.v(moduleName, "从 assets 拷贝/解压原始包 结束 , 是否拷贝成功:$success , 目标文件是否存在:${zipFile.exists()} , 耗时:${System.currentTimeMillis() - start}ms")
        return success
    }

    @JvmStatic
    @Synchronized
    fun getPrimaryConfigFromAssets(moduleName: String): HKHybirdConfigModel? {
        HKLogUtil.v(moduleName, "从 assets 获取最原始配置信息 开始")
        val start = System.currentTimeMillis()
        var primaryConfig: HKHybirdConfigModel? = null
        try {
            primaryConfig = HKJsonUtil.fromJson(HKFileUtil.readTextFromFile(HKBaseApplication.INSTANCE.assets.open("${HKHybird.ASSETS_DIR_NAME}/$moduleName${HKHybird.CONFIG_SUFFIX}")), HKHybirdConfigModel::class.java)

            val zipFile = HKHybirdUtil.getZipFile(primaryConfig)
            val unzipDir = HKHybirdUtil.getUnzipDir(primaryConfig)
            if (HKHybirdUtil.copyPrimaryZipFromAssets(moduleName, primaryConfig)) {
                HKHybirdUtil.unzipToLocal(zipFile, unzipDir)
            }
        } catch (exception: FileNotFoundException) {
            HKLogUtil.e(moduleName, "文件不存在", exception)
        } catch (exception: IOException) {
            HKLogUtil.e(moduleName, "文件读写错误", exception)
        }
        HKLogUtil.v(moduleName, "从 assets 获取最原始配置信息 结束 , 耗时: ${System.currentTimeMillis() - start}ms , 原始配置信息为:$primaryConfig")
        return primaryConfig
    }

}
