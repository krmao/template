package com.smart.library.bundle

import com.smart.library.base.HKBaseApplication
import com.smart.library.util.*
import com.smart.library.util.cache.HKCacheManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.util.zip.ZipException

@Suppress("MemberVisibilityCanPrivate", "unused", "UNCHECKED_CAST", "PrivatePropertyName")
class HKHybirdModuleManager(val moduleFullName: String) {

    private val TAG = HKHybirdModuleManager::class.java.simpleName + ":" + moduleFullName
    private val KEY_CONFIGURATION = "key-$moduleFullName"

    private val updateManager: HKHybirdModuleUpdateManager = HKHybirdModuleUpdateManager()
    private val localRootDir = File(HKCacheManager.getChildCacheDir("hybird"), moduleFullName)
    private var localUnzipDir: File = File(localRootDir, "src")
    private var localZipFile: File = File(localRootDir, "$moduleFullName.zip")

    /**
     * zip准备好后保存到本地, verifyLocalZip==false 时从本地删除
     */
    var localConfiguration: HKHybirdModuleConfiguration? = HKPreferencesUtil.getEntity(KEY_CONFIGURATION, HKHybirdModuleConfiguration::class.java)
        set(value) {
            HKPreferencesUtil.putEntity(KEY_CONFIGURATION, value)
            field = value
        }

    fun verifySync() {
        HKLogUtil.w(TAG, "verify start")
        val start = System.currentTimeMillis()
        if (!verifyLocalFiles()) {
            if (!verifyLocalZip()) {
                localConfiguration = null
                copyZipFromAssets()
            }
            unzipToLocal()
        }
        updateManager.checkUpdate()
        HKLogUtil.w(TAG, "verify end , 耗时:${System.currentTimeMillis() - start}ms")
    }

    fun verify(callback: ((localUnzipDir: File, configuration: HKHybirdModuleConfiguration?) -> Unit)? = null) {
        HKLogUtil.w(TAG, "verify start")
        var success = false
        val start = System.currentTimeMillis()
        Observable.fromCallable {
            if (!verifyLocalFiles()) {
                if (!verifyLocalZip()) {
                    localConfiguration = null
                    copyZipFromAssets()
                }
                success = unzipToLocal()
            }
            updateManager.checkUpdate()
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                HKLogUtil.w(TAG, "verify: $success , 耗时:${System.currentTimeMillis() - start}ms")
                callback?.invoke(localUnzipDir, localConfiguration)
            }
        HKLogUtil.w(TAG, "verify async-progressing , 耗时:${System.currentTimeMillis() - start}ms")
    }

    private fun getConfigurationFromAssets(): HKHybirdModuleConfiguration? {
        HKLogUtil.d(TAG, "getConfigurationFromAssets start")
        val start = System.currentTimeMillis()
        var configuration: HKHybirdModuleConfiguration? = null
        try {
            configuration = HKJsonUtil.fromJson(HKFileUtil.readTextFromFile(HKBaseApplication.INSTANCE.assets.open("hybird/$moduleFullName.json")), HKHybirdModuleConfiguration::class.java)
            HKLogUtil.d(TAG, "getConfigurationFromAssets success , 耗时:${System.currentTimeMillis() - start}ms \nconfiguration:$configuration")
        } catch (exception: FileNotFoundException) {
            HKLogUtil.e(TAG, "getConfigurationFromAssets failure , 耗时:${System.currentTimeMillis() - start}ms", exception)
        } catch (exception: IOException) {
            HKLogUtil.e(TAG, "getConfigurationFromAssets failure , 耗时:${System.currentTimeMillis() - start}ms", exception)
        }
        return configuration
    }

    private fun copyZipFromAssets(): Boolean {
        HKLogUtil.d(TAG, "copyZipFromAssets start")
        var success = false
        val start = System.currentTimeMillis()
        HKFileUtil.deleteFile(localZipFile)
        HKFileUtil.deleteDirectory(localUnzipDir)
        try {
            HKFileUtil.copy(HKBaseApplication.INSTANCE.assets.open("hybird/$moduleFullName.zip"), localZipFile)
            localConfiguration = getConfigurationFromAssets()
            success = true
            HKLogUtil.d(TAG, "copyZipFromAssets success ,localZipFile.exists?${localZipFile.exists()} , 耗时:${System.currentTimeMillis() - start}ms")
        } catch (exception: FileNotFoundException) {
            HKLogUtil.e(TAG, "copyZipFromAssets failure , 耗时:${System.currentTimeMillis() - start}ms", exception)
        } catch (exception: IOException) {
            HKLogUtil.e(TAG, "copyZipFromAssets failure , 耗时:${System.currentTimeMillis() - start}ms", exception)
        }
        return success
    }

    private fun unzipToLocal(): Boolean {
        HKLogUtil.d(TAG, "unzipToLocal start")
        var success = false
        val start = System.currentTimeMillis()
        HKFileUtil.deleteDirectory(localUnzipDir)
        try {
            HKZipUtil.unzip(localZipFile, localUnzipDir)
            success = true
            HKLogUtil.d(TAG, "unzipToLocal success , 耗时:${System.currentTimeMillis() - start}ms")
        } catch (exception: FileNotFoundException) {
            HKLogUtil.e(TAG, "unzipToLocal failure , 耗时:${System.currentTimeMillis() - start}ms", exception)
        } catch (exception: IOException) {
            HKLogUtil.e(TAG, "unzipToLocal failure , 耗时:${System.currentTimeMillis() - start}ms", exception)
        } catch (exception: ZipException) {
            HKLogUtil.e(TAG, "unzipToLocal failure , 耗时:${System.currentTimeMillis() - start}ms", exception)
        }
        return success
    }

    private fun verifyLocalZip(): Boolean {
        HKLogUtil.d(TAG, "verifyLocalZip start")
        val start = System.currentTimeMillis()
        val zipFileExists = localZipFile.exists()
        val zipFileMd5 = HKChecksumUtil.genMD5Checksum(localZipFile)
        val rightMd5 = localConfiguration?.moduleZipMd5
        val isZipFileMd5Valid = zipFileMd5 == rightMd5
        val success = zipFileExists && isZipFileMd5Valid
        HKLogUtil.d(TAG, "verifyLocalZip:${if (success) "success" else "failure"}, zip文件是否存在:$zipFileExists, MD5是否正确:$isZipFileMd5Valid, 耗时:${System.currentTimeMillis() - start}ms")
        return success
    }

    private fun verifyLocalFiles(): Boolean {
        HKLogUtil.d(TAG, "verifyLocalFiles start")
        val start = System.currentTimeMillis()
        val localUnzipDirExists = localUnzipDir.exists()
        val localIndexExists = File(localUnzipDir, "index.shtml").exists()
        var invalidFilesNum = 0
        if (localIndexExists && localIndexExists) {
            HKFileUtil.getFileList(localUnzipDir).forEach {
                val fileMd5 = HKChecksumUtil.genMD5Checksum(it)
                val rightMd5 = localConfiguration?.moduleFilesMd5?.get(it.path)
                val isFileMd5Valid = fileMd5 == rightMd5
                if (!isFileMd5Valid)
                    invalidFilesNum++
            }
        }
        val success = invalidFilesNum == 0 && localUnzipDirExists && localIndexExists
        HKLogUtil.d(TAG, "verifyLocalFiles:${if (success) "success" else "failure"},invalidFilesNum:$invalidFilesNum ,localUnzipDirExists:$localUnzipDirExists ,localIndexExists:$localIndexExists ,耗时:${System.currentTimeMillis() - start}ms")
        return success
    }
}
