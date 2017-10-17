package com.smart.library.bundle.imp

import com.smart.library.base.HKBaseApplication
import com.smart.library.bundle.HKIBundleManager
import com.smart.library.util.*
import com.smart.library.util.cache.HKCacheManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File

/*
   上传参数: bundleKey: versionCode_versionName

   下发参数: {
                bundleKey: versionCode_versionName,
                patchList: [
                    {
                        patchUrl="https://www.ctrip.com/com.smart.modules.device.ios_1.patch",
                        packageName:"com.smart.modules.device.ios"
                        patchVersion:1
                        patchMd5:""
                        syntheticMd5:""
                    },
                    {
                        patchUrl="https://www.ctrip.com/com.smart.modules.device.android_1.patch",
                        packageName:"com.smart.modules.device.android"
                        patchVersion:1
                        patchMd5:""
                        syntheticMd5:""
                    },
                ]
            }
   本地目录: /hotpatch
            ........./app.version.1.1(bundleKey)/
            ......................../com.smart.modules.device.ios/
            ......................................./patch.version.1
            ......................................................./com.smart.modules.device.ios.patch //下载的差分文件
            ......................................................./com.smart.modules.device.ios.zip    //合成的目标文件
            ......................................................./com.smart.modules.device.ios.dex   //加载一次后生成的dex文件，如果存在可以直接加载这个(optimize)
            ......................................./patch.version.2
            ......................................................./com.smart.modules.device.ios.patch
            ......................................................./com.smart.modules.device.ios.zip
            ........./app.version.2.2(bundleKey)/
*/
@Suppress("MemberVisibilityCanPrivate", "unused")
object HKBundleManager : HKIBundleManager {
    private val TAG = HKBundleManager::class.java.name
    private val KEY_HYBIRD_VERSION = "[HybirdLocalVersion]"

    private val suffix = ".zip"

    private val nameInAssets = "bundle"
    private val nameForUnZipDir = "hybird"
    private val pathForLocalRootDir: String = HKCacheManager.getCacheDir().path + "/$nameForUnZipDir/"

    private val pathForHybirdDir: String = "$pathForLocalRootDir$nameForUnZipDir/"

    private val pathForLocalFile: String = "$pathForHybirdDir$nameForUnZipDir$suffix"

    val HYBIRD_URL_PREFIX = "file://"

    val HYBIRD_DIR: String = pathForHybirdDir

    private val listeners: java.util.ArrayList<(success: Boolean, hybirdDir: String) -> Unit> = ArrayList()

    private var hybirdLocalVersion: String = HKPreferencesUtil.getString(KEY_HYBIRD_VERSION)
        set(value) {
            HKPreferencesUtil.putString(KEY_HYBIRD_VERSION, value)
            field = value
        }

    @Synchronized
    override fun installWithVerify(callback: ((success: Boolean, hybirdDir: String) -> Unit)?) {
        if (!verify()) {
            Observable.fromCallable {
                HKLogUtil.d(TAG, "start clean now ...")
                HKFileUtil.deleteFile(pathForLocalFile)
                HKFileUtil.deleteDirectory(pathForHybirdDir)
                HKLogUtil.d(TAG, "clean success")
                HKLogUtil.d(TAG, "start copy now ...")
                HKFileUtil.copy(HKBaseApplication.INSTANCE.assets.open(nameInAssets + suffix), pathForLocalFile)
                HKLogUtil.d(TAG, "copy success ! isFileExistsInSdcard:${File(pathForLocalFile).exists()}")
                HKLogUtil.d(TAG, "start unzip now ...")
                HKZipUtil.unzip(pathForLocalFile, pathForLocalRootDir)
                HKLogUtil.d(TAG, "unzip success")

                hybirdLocalVersion = "${HKSystemUtil.versionCode}_${HKSystemUtil.versionName}"
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    HKLogUtil.d(TAG, "copyToLocal success")
                    callback?.invoke(true, HYBIRD_DIR)
                    listeners.forEach { it(true, HYBIRD_DIR) }

                }, { error ->
                    HKLogUtil.e(TAG, "copyToLocal failure", error)
                    callback?.invoke(false, HYBIRD_DIR)
                    listeners.forEach { it(false, HYBIRD_DIR) }
                })
        } else {
            HKLogUtil.w(TAG, "no need to copyToLocal")
            callback?.invoke(true, HYBIRD_DIR)
        }
    }

    override fun verify(): Boolean {
        val versionCurrentApp = "${HKSystemUtil.versionCode}_${HKSystemUtil.versionName}"
        val verify = versionCurrentApp == hybirdLocalVersion
        HKLogUtil.d(TAG, "versionCurrentApp:$versionCurrentApp == hybirdLocalVersion:${hybirdLocalVersion} ? $verify")
        return verify
    }
}
