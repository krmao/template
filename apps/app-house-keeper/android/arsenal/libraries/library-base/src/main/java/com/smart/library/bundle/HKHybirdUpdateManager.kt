package com.smart.library.bundle

import android.support.v7.app.AlertDialog
import com.smart.library.base.HKBaseApplication
import com.smart.library.util.HKLogUtil
import java.io.File
import kotlin.system.exitProcess


/**
 * 负责检查更新和下载
 *
 * 注意: 当下载到一半或者执行解压校验一半忽然杀死App,则下次启动重新执行更行步骤，app启动时加载最新版本的文件夹目录，
 * 校验失败则删除并加载上一版本目录，如果没有了，则执行从安装包中解压初始包
 *
 */
@Suppress("MemberVisibilityCanPrivate", "PrivatePropertyName")
class HKHybirdUpdateManager(val moduleManager: HKHybirdModuleManager) {
    private val TAG = HKHybirdUpdateManager::class.java.simpleName

    /**
     * 可配置的更新升级模式
     */
    enum class Mode {
        REMIND,         // 弹框提示是否立即生效（是）（否），是:立即重启app或者退出所有webView,否:则下次重启app生效
        RESTART,        // 下次app重启生效
        IMMEDIATELY;    // 实时生效
    }

    var configer: ((configUrl: String, callback: (HKHybirdConfigModel?) -> Boolean?) -> Boolean?)? = null
    var downloader: ((downloadUrl: String, file: File?, callback: (File?) -> Unit?) -> Unit?)? = null
    private var isDownloading = false

    @Synchronized
    fun checkUpdate() {
        HKLogUtil.e(TAG, "checkUpdate start")

        if (configer == null) {
            HKLogUtil.e(TAG, "尚未配置config下载器，请先设置config下载器")
            return
        }

        if (isDownloading) {
            HKLogUtil.e(TAG, "正在下载更新中，在更新安装成功前不能下载其他更新 return")
            return
        }

        val moduleConfigUrl = moduleManager.configManager.currentConfig?.moduleConfigUrl ?: ""
        HKLogUtil.v(TAG, "开始下载配置文件 $moduleConfigUrl")
        configer?.invoke(moduleConfigUrl) { remoteConfig: HKHybirdConfigModel? ->
            HKLogUtil.v(TAG, "配置文件下载成功: $remoteConfig")
            if (remoteConfig != null) {
                //1:正式包，所有机器可以拉取
                //2:测试包，只要测试机器可以拉取
                if (!remoteConfig.moduleDebug || (remoteConfig.moduleDebug && HKHybirdManager.DEBUG)) {
                    startUpdating(remoteConfig)
                }
            }
            true
        }
    }

    /**
     * 是否需要切换在线状态
     *
     * 返回 true  代表有更新,需要更新,并且切换为在线状态, 直到应用更新成功之前,都是在线,意味着即使更新下载成功,依然代表需要更新,依然使用在线状态
     * 返回 false 代表没有更新,使用本地文件
     */
    @Synchronized
    fun checkUpdateSync(): Boolean {
        val start = System.currentTimeMillis()
        HKLogUtil.v(TAG, "开始检查更新")

        if (moduleManager.onLineModel) {
            HKLogUtil.e(TAG, "已经是在线状态了,无需重复检测更新")
            return true
        }

        if (configer == null) {
            HKLogUtil.e(TAG, "尚未配置config下载器，请先设置config下载器,无需切换为在线状态")
            return false
        }

        if (isDownloading) {
            HKLogUtil.e(TAG, "正在下载更新中，需要切换为在线状态")
            return true
        }

        val moduleConfigUrl = moduleManager.configManager.currentConfig?.moduleConfigUrl ?: ""
        HKLogUtil.v(TAG, "开始下载配置文件 $moduleConfigUrl")

        val needUpdate = configer?.invoke(moduleConfigUrl) { remoteConfig: HKHybirdConfigModel? ->
            HKLogUtil.v(TAG, "配置文件下载成功: $remoteConfig")
            if (remoteConfig != null) {
                //1:正式包，所有机器可以拉取
                //2:测试包，只要测试机器可以拉取
                if (!remoteConfig.moduleDebug || (remoteConfig.moduleDebug && HKHybirdManager.DEBUG)) {
                    val remoteVersion = remoteConfig.moduleVersion.toFloatOrNull()
                    val localVersion = moduleManager.configManager.currentConfig?.moduleVersion?.toFloatOrNull()
                    HKLogUtil.v("${moduleManager.moduleFullName} 当前版本:$localVersion   远程版本:$remoteVersion")
                    if (remoteVersion != null && localVersion != null) {
                        //版本号相等时不做任何处理，避免不必要的麻烦
                        if (remoteVersion > localVersion) {
                            HKLogUtil.v("检测到有新版本")
                            return@invoke true
                        } else if (remoteVersion < localVersion) {
                            HKLogUtil.v("检测到需要回滚")
                            return@invoke true
                        }
                    }
                }
            }
            return@invoke false
        } == true
        HKLogUtil.v(TAG, "检查更新结束: ${if (needUpdate) "检测到需要更新,请立即切换为在线状态" else "未检测到更新, 耗时: ${System.currentTimeMillis() - start}"}ms")
        return needUpdate
    }

    @Synchronized
    private fun startUpdating(remoteConfig: HKHybirdConfigModel) {
        HKLogUtil.v("开始准备下载更新的ZIP包")
        if (moduleManager.configManager.isContainedInNextConfigStack(remoteConfig)) {
            HKLogUtil.w("${moduleManager.moduleFullName} 不用重复下载，已经在下次启动生效的队列中 -->")
            return
        }

        val remoteVersion = remoteConfig.moduleVersion.toFloatOrNull()
        val localVersion = moduleManager.configManager.currentConfig?.moduleVersion?.toFloatOrNull()
        HKLogUtil.e("${moduleManager.moduleFullName} 当前版本:$localVersion   远程版本:$remoteVersion")
        if (remoteVersion != null && localVersion != null) {

            //版本号相等时不做任何处理，避免不必要的麻烦
            if (remoteVersion > localVersion) {
                HKLogUtil.v("${moduleManager.moduleFullName} 开始升级 -->")
                HKLogUtil.v(TAG, "切换在线模式并且立即下载")
                download(remoteConfig)       //然后立即下载
            } else if (remoteVersion < localVersion) {
                HKLogUtil.v("${moduleManager.moduleFullName} 开始回滚 -->")

                moduleManager.configManager.saveConfigNext(remoteConfig)
                //回滚
                //moduleManager.completeRemoveAllGTLatestConfigSafely(remoteConfig)//delete 即时生效
                HKLogUtil.e(TAG, "切换在线模式并且立即下载")
                download(remoteConfig)       //然后立即下载
            }
        } else {
            HKLogUtil.e(TAG, "无需下载 remoteVersion:$remoteVersion or localVersion:$localVersion is null !")
        }
    }

    /**
     * todo 安全性有待验证
     * 锁住 moduleManager 确保升级期间不会有乱入操作，导致数据混乱
     */
    @Synchronized
    private fun completeUpdating(remoteConfig: HKHybirdConfigModel) {
        val start = System.currentTimeMillis()
        HKLogUtil.e(TAG, "completeUpdating start :updateMode=${remoteConfig.moduleUpdateMode}")

        when (remoteConfig.moduleUpdateMode) {
            Mode.IMMEDIATELY -> {
                completeImmediately(remoteConfig)
            }
            HKHybirdUpdateManager.Mode.RESTART -> {
                completeValidNextRestart(remoteConfig)
            }
            HKHybirdUpdateManager.Mode.REMIND -> {
                completeRemind(remoteConfig)
            }
        }
        HKLogUtil.e(TAG, "completeUpdating end  耗时:${System.currentTimeMillis() - start}ms ")
    }

    @Synchronized
    fun completeImmediately(remoteConfig: HKHybirdConfigModel) {
        synchronized(moduleManager) {
            moduleManager.configManager.currentConfig = remoteConfig
            moduleManager.configManager.saveConfig(remoteConfig)
        }
        isDownloading = false
    }

    @Synchronized
    fun completeRemind(remoteConfig: HKHybirdConfigModel) {
        AlertDialog.Builder(HKBaseApplication.INSTANCE)
            .setTitle("更新")
            .setMessage("检测到应用有新的更新,是否立即生效？")
            .setNegativeButton("否") { _, _ ->
                completeValidNextRestart(remoteConfig)
            }
            .setPositiveButton("是") { _, _ ->
                moduleManager.configManager.currentConfig = remoteConfig
                moduleManager.configManager.saveConfig(remoteConfig)
                isDownloading = false
                exitProcess(0)
            }
            .show()
    }

    @Synchronized
    fun completeValidNextRestart(remoteConfig: HKHybirdConfigModel) {
        moduleManager.configManager.saveConfigNext(remoteConfig)
        isDownloading = false
    }

    private fun download(remoteConfig: HKHybirdConfigModel) {
        HKLogUtil.e(TAG, "do download zip start --> ${remoteConfig.moduleDownloadUrl}")

        val zipFile = moduleManager.getZipFile(remoteConfig)

        //1: 如果即将下载的版本 本地解压包存在切校验正确,zip即使不存在也无需下载
        if (moduleManager.isLocalFilesValid(remoteConfig)) {
            HKLogUtil.e(TAG, "do download zip end <-- local file valid")
            completeUpdating(remoteConfig)
            return
        }

        if (downloader == null) {
            isDownloading = false
            HKLogUtil.e(TAG, "尚未配置zip下载器，请先设置模块的zip下载器")
            return
        }

        isDownloading = true
        //如果即将下载的版本 本地解压包不存在或者校验失败,执行下载zip
        downloader?.invoke(remoteConfig.moduleDownloadUrl, zipFile) { file: File? ->
            HKLogUtil.e(TAG, "do download zip end <-- file:${file?.path}")

            if (moduleManager.isLocalFilesValid(remoteConfig)) {
                HKLogUtil.e(TAG, "do download zip end <-- local file valid")
                completeUpdating(remoteConfig)
            } else {
                isDownloading = false
                //moduleManager.onLineMode = false //todo 线上版本是否完备？
                HKLogUtil.e(TAG, "do download zip end <-- local file not valid !")
            }
        }
    }
}
