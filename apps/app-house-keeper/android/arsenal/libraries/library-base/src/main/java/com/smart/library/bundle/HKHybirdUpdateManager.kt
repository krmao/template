package com.smart.library.bundle

import com.smart.library.util.HKLogUtil
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File


/**
 * 负责检查更新和下载
 *
 * 注意: 当下载到一半或者执行解压校验一半忽然杀死App,则下次启动重新执行更行步骤，app启动时加载最新版本的文件夹目录，
 * 校验失败则删除并加载上一版本目录，如果没有了，则执行从安装包中解压初始包
 *
 */
@Suppress("MemberVisibilityCanPrivate", "PrivatePropertyName")
class HKHybirdUpdateManager(val moduleManager: HKHybirdModuleManager) {

    /**
     * 升级策略
     */
    enum class UpdateStrategy {
        ONLINE,         // 检测到更新理解切换在线
        OFFLINE,        // 检测到更新仍然使用本地
    }

    internal var configer: ((configUrl: String, callback: (HKHybirdConfigModel?) -> Boolean?) -> Boolean?)? = null
    internal var downloader: ((downloadUrl: String, file: File?, callback: (File?) -> Unit?) -> Unit?)? = null
    private var isDownloading = false

    /**
     * 检查更新-异步
     */
    fun checkUpdate() {
        val start = System.currentTimeMillis()
        HKLogUtil.v(moduleManager.moduleName, "系统检测更新(异步) 开始 ,当前线程:${Thread.currentThread().name}")
        Observable.fromCallable {
            val needUpdate = checkUpdateSync()
            HKLogUtil.e(moduleManager.moduleName, "检查更新(同步) 结束 ,当前线程:${Thread.currentThread().name} , ${if (needUpdate) "检测到需要更新,已经切换为在线状态,访问在线资源" else "未检测到更新,访问本地资源"} 耗时: ${System.currentTimeMillis() - start}ms")
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe()
    }

    /**
     * 检查更新-同步(加载本模块URL的时候)
     * 注意: 此处无需处理 模块第一次加载 然后合并 下次启动生效的配置文件  操作, 因为既然打开了本网页,前提是已经checkHealth, 而 checkHealth 已经包含了 fitNextAndFitLocalIfNeedConfigsInfo
     *       所以,良好的设计是此时无需关注合版本合并信息,只关注自己的责任,检查/下载更新
     *
     * A:   进来发现  当前已经是在线状态    说明时间段为  正在下载 到 应用成功之前 这个时间段内
     *
     * 不执行重复网络请求,return true, 说明正在处理更新操作
     * ----
     * B:   进来发现  当前不是在线状态
     *
     * 1: 执行检查更新
     *
     * 2: 如果检测到 有更新或者回滚指令 则异步下载更新包(这里需要检测本地是否已经下载,已经解压,只待切换),并解压到本地,校验OK后,将状态保存为下一次模块启动替换
     *
     *      调用时机:   系统启动时,浏览器启动时,模块主url被拦截到时,前后台切换时
     *
     *                 其中 系统启动时         --> 检测模块是否已经被打开,如果没有且更细胞准备充分,则同步更新替换本地文件结束再执行放行
     *                 其中 模块主url被拦截到时 --> 检测模块是否已经被打开,如果没有且更细胞准备充分,则同步更新替换本地文件结束再执行放行
     *
     *      a-> 回滚/升级, 更新策略为在线:  则立即 切换为在线状态(onlineModel=true),    访问在线资源     ,当检测到模块没有被浏览器加载的时候,执行回滚操作
     *      b-> 回滚/升级, 更新策略为离线:  则依然 使用本地文件  (onlineModel=false),    访问本地资源     ,当检测到模块没有被浏览器加载的时候,执行回滚操作
     *
     *
     * 3: 如果没有更新 return false
     *
     *
     * 返回 true  直到更新包尚未应用成功
     * 返回 false 代表没有更新,或者更新包已经应用成功
     */
    @Synchronized
    fun checkUpdateSync(): Boolean {
        val start = System.currentTimeMillis()
        HKLogUtil.v(moduleManager.moduleName, "系统检测更新(同步) 开始 ,当前线程:${Thread.currentThread().name}")

        if (isDownloading) {
            HKLogUtil.e(moduleManager.moduleName, "系统检测到当前正在下载更新中, return true")
            return true
        }

        if (moduleManager.onLineModel) {
            HKLogUtil.e(moduleManager.moduleName, "系统检测到当前已经是在线状态了,无需重复检测 return true")
            return true
        }

        if (configer == null) {
            HKLogUtil.e(moduleManager.moduleName, "系统检测到尚未配置 config 下载器，请先设置 config 下载器, return false")
            return false
        }

        val moduleConfigUrl = moduleManager.currentConfig?.moduleConfigUrl ?: ""
        HKLogUtil.v(moduleManager.moduleName, "下载配置文件 开始: $moduleConfigUrl")
        val needUpdate = configer?.invoke(moduleConfigUrl) { remoteConfig: HKHybirdConfigModel? ->
            HKLogUtil.v(moduleManager.moduleName, "下载配置文件 ${if (remoteConfig == null) "失败" else "成功"} , remoteConfig = $remoteConfig")
            if (remoteConfig != null) {
                //1:正式包，所有机器可以拉取
                //2:测试包，只要测试机器可以拉取
                if (!remoteConfig.moduleDebug || (remoteConfig.moduleDebug && HKHybirdManager.DEBUG)) {
                    HKLogUtil.e(moduleManager.moduleName, "检测到该版本为正式版 或者当前为测试版本并且本机是测试机,可以执行更新操作")
                    val remoteVersion = remoteConfig.moduleVersion.toFloatOrNull()
                    val localVersion = moduleManager.currentConfig?.moduleVersion?.toFloatOrNull()
                    HKLogUtil.v("${moduleManager.moduleName} 当前版本:$localVersion   远程版本:$remoteVersion")
                    if (remoteVersion != null && localVersion != null) {
                        //版本号相等时不做任何处理，避免不必要的麻烦
                        if (remoteVersion > localVersion) {
                            HKLogUtil.v("系统检测到有新版本")

                            if (remoteConfig.moduleUpdateMode == UpdateStrategy.ONLINE) {
                                moduleManager.onLineModel = true
                                HKLogUtil.e("立即切换为在线模式")
                            } else {
                                HKLogUtil.e("无需切换为在线模式")
                            }
                            download(remoteConfig)
                            return@invoke true
                        } else if (remoteVersion < localVersion) {
                            HKLogUtil.v("系统检测到需要回滚")

                            if (remoteConfig.moduleUpdateMode == UpdateStrategy.ONLINE) {
                                moduleManager.onLineModel = true
                                HKLogUtil.e("立即切换为在线模式")
                            } else {
                                HKLogUtil.e("无需切换为在线模式")
                            }
                            download(remoteConfig)
                            return@invoke true
                        } else {
                            HKLogUtil.v("系统检测到 remoteVersion:$remoteVersion 或者 localVersion:$localVersion 相等, 无需更新")
                        }
                    } else {
                        HKLogUtil.e("系统检测到 remoteVersion:$remoteVersion 或者 localVersion:$localVersion 为空, 无法判断需要更新,默认不需要更新")
                    }
                } else {
                    HKLogUtil.e(moduleManager.moduleName, "检测到该版本为调试版本且本机不是测试机,不执行更新操作 return false")
                }
            }
            return@invoke false
        } == true
        HKLogUtil.v(moduleManager.moduleName, "检查更新(同步) 结束 ,当前线程:${Thread.currentThread().name} , ${if (needUpdate) "检测到需要更新,已经切换为在线状态,访问在线资源" else "未检测到更新,访问本地资源"} 耗时: ${System.currentTimeMillis() - start}ms")
        return needUpdate
    }

    @Synchronized
    private fun completeDownloadSuccess(remoteConfig: HKHybirdConfigModel) {
        val start = System.currentTimeMillis()
        HKLogUtil.e(moduleManager.moduleName, "下载任务成功-后期处理 开始: 目标配置文件为=$remoteConfig")
        moduleManager.configManager.saveConfigNext(remoteConfig)
        isDownloading = false
        HKLogUtil.e(moduleManager.moduleName, "下载任务成功-后期处理 结束  耗时:${System.currentTimeMillis() - start}ms ")
    }

    @Synchronized
    private fun completeDownloadFailure(remoteConfig: HKHybirdConfigModel) {
        val start = System.currentTimeMillis()
        HKLogUtil.e(moduleManager.moduleName, "下载任务失败-后期处理 开始: 目标配置文件为=$remoteConfig")
        isDownloading = false
        HKLogUtil.e(moduleManager.moduleName, "下载任务失败-后期处理 结束  耗时:${System.currentTimeMillis() - start}ms ")
    }

    private fun download(remoteConfig: HKHybirdConfigModel) {
        HKLogUtil.v(moduleManager.moduleName, "下载更新包 开始: ${remoteConfig.moduleDownloadUrl}")

        val nextConfigStack = moduleManager.configManager.getNextConfigStack()
        if (nextConfigStack.contains(remoteConfig)) {
            if (nextConfigStack.peek() != remoteConfig) {
                HKLogUtil.w(moduleManager.moduleName, "系统检测到 当前任务已经加载好并且已经保存在下次启动生效的对战里面, 但是不在栈顶 , nextConfigStack=$nextConfigStack , 即将执行重置堆栈")
                moduleManager.configManager.saveConfigNext(remoteConfig)
            } else {
                HKLogUtil.w(moduleManager.moduleName, "系统检测到 当前任务已经加载好并且已经保存在下次启动生效的对战里面 , 无需重复下载 return , nextConfigStack=$nextConfigStack")
            }
            return
        }

        val zipFile = HKHybirdModuleManager.getZipFile(remoteConfig)

        HKLogUtil.e(moduleManager.moduleName, "率先校验本地是否存在 目标版本的已解压的校验完整的 更新包解压后的文件夹,有无zip包不重要,如果有则无需重复执行下载任务")
        //1: 如果即将下载的版本 本地解压包存在切校验正确,zip即使不存在也无需下载
        if (HKHybirdModuleManager.isLocalFilesValid(remoteConfig)) {
            HKLogUtil.e(moduleManager.moduleName, "系统检测到 解压后的文件夹 校验成功,无需执行下载, 开始执行后续操作")
            completeDownloadSuccess(remoteConfig)
            return
        }

        if (downloader == null) {
            isDownloading = false
            HKLogUtil.e(moduleManager.moduleName, "系统检测到 尚未配置zip下载器，请先设置模块的zip下载器, return")
            return
        }

        isDownloading = true
        HKLogUtil.v(moduleManager.moduleName, "开始进行网络下载 ...")
        //如果即将下载的版本 本地解压包不存在或者校验失败,执行下载zip
        downloader?.invoke(remoteConfig.moduleDownloadUrl, zipFile) { file: File? ->
            HKLogUtil.e(moduleManager.moduleName, "下载更新包结束 :${file?.path}")

            if (HKHybirdModuleManager.isLocalFilesValid(remoteConfig)) {
                HKLogUtil.e(moduleManager.moduleName, "更新包 & 解压后的文件夹 校验成功, 开始执行后续操作")
                completeDownloadSuccess(remoteConfig)
            } else {
                HKLogUtil.e(moduleManager.moduleName, "更新包 & 解压后的文件夹 校验失败, 请检查 更新包已经配置信息是否完善,完整,MD5值是否一一对应 ! 本次更新失败,操作结束 !!!")
                HKLogUtil.e(moduleManager.moduleName, "注意 虽然本次操作结束, 但是仍然是在线模式, 因为服务端仍然会返回新版本,避免重复操作! 只有当本模块在浏览器中全部退出的时候,重置 onLineMode 为 false !!!")
                completeDownloadFailure(remoteConfig)
            }
        }
    }
}
