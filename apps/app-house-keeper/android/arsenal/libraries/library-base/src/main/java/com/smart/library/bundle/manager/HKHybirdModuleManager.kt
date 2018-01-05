package com.smart.library.bundle.manager

import android.os.StrictMode
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import com.smart.library.bundle.HKHybird
import com.smart.library.bundle.model.HKHybirdConfigModel
import com.smart.library.bundle.strategy.HKHybirdCheckStrategy
import com.smart.library.bundle.strategy.HKHybirdUpdateStrategy
import com.smart.library.bundle.util.HKHybirdUtil
import com.smart.library.util.HKFileUtil
import com.smart.library.util.HKLogUtil
import com.smart.library.util.hybird.HKHybirdBridge
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileInputStream

/**
 * 负责责范围
 *
 * 1: 负责对本模块各版本的健康体检，自动删除无效版本，删除后自动还原到上一个最新版本，如果没有则还原到原始安装包版本
 * 2: 不负责下载更新升级等操作，这些操作放在 HKHybirdUpdateManager
 *
 * *** 注意: 确保这里的  moduleName 与 配置文件里面的 moduleName 一致 ***
 *
 */
@Suppress("MemberVisibilityCanPrivate", "unused", "UNCHECKED_CAST", "PrivatePropertyName")
class HKHybirdModuleManager(val moduleName: String) {

    internal val configManager: HKHybirdConfigManager = HKHybirdConfigManager(this)
    internal val updateManager: HKHybirdUpdateManager = HKHybirdUpdateManager(this)
    internal val lifecycleManager: HKHybirdLifecycleManager = HKHybirdLifecycleManager(this)

    internal var currentConfig: HKHybirdConfigModel? = null
        set(value) {
            field = value
            //当设置新的当前生效的配置信息时，更新拦截请求配置
            if (value != null)
                setIntercept(value)
        }

    internal val rootDir = File(HKHybird.LOCAL_ROOT_DIR, moduleName)

    /**
     * 1: 检测到有新的版本变化,开始异步下载更新,立即切换为在线模式
     * 2: 更新时机必须在浏览器加载本模块之前,如果本模块并没有在浏览器中打开,则直接更新,如果已经打开,则一直使用在线模式
     * 3: 如果已经是在线模式,则不执行重复检查更新,不执行重复的健康体检
     * 4: 重启app或者执行正式更新替换之前,检查更新包的完整性
     */
    var onlineModel: Boolean = false

    var updateStrategy = HKHybirdUpdateStrategy.ONLINE

    /**
     * 异步进行初始化操作
     */
    @Synchronized
    fun init(configer: ((configUrl: String, callback: (HKHybirdConfigModel?) -> Boolean?) -> Boolean?)? = null, downloader: ((downloadUrl: String, file: File?, callback: (File?) -> Unit?) -> Unit?)? = null, callback: ((localUnzipDir: File?, config: HKHybirdConfigModel?) -> Unit)? = null) {
        if (configer != null) setConfiger(configer)
        if (downloader != null) setDownloader(downloader)

        checkHealth(HKHybirdCheckStrategy.ALL, false, callback)
    }

    /**
     *
     * 健康体检，检查模块完整性
     *
     * 注意: 执行一次本方法,才能添加资源拦截器,不然无法使用本地资源,无法拦截URL,所以万无一失的做法是每次都调用下面提到的案例方式加载URL,能确保条件充分
     * 注意: 本方法会在合适的情况下处理 下次启动生效的配置信息
     *
     * 执行时机:
     *
     * 1: 程序启动的时候
     * 2: 每次手动加载本模块的时候, 详见下面的  案例
     * 3: url被拦截到的时候,进行检查更新的同事,检测本地文件完整性,由于这是同步耗时操作,暂定 TODO 待定
     *
     * 案例:
     *      checkHealth { _, _ ->
     *          HybirdWebFragment.goTo(activity, "file:///android_asset/index.html")
     *      }
     */
    @Synchronized
    fun checkHealth(strategy: HKHybirdCheckStrategy = HKHybirdCheckStrategy.READY, synchronized: Boolean = true, callback: ((localUnzipDir: File?, config: HKHybirdConfigModel?) -> Unit)? = null) {
        if (strategy == HKHybirdCheckStrategy.READY && currentConfig != null) {
            HKLogUtil.v(moduleName, "系统监测到当前模块已经准备充分,当前策略不需要检验本地文件,可以加载URL   检查策略:$strategy")
            callback?.invoke(HKHybirdUtil.getUnzipDir(currentConfig), currentConfig)
            return
        }
        HKLogUtil.v(moduleName, "开始校验本模块文件 检查策略:$strategy")
        if (synchronized) {
            checkHealthSync()
            callback?.invoke(HKHybirdUtil.getUnzipDir(currentConfig), currentConfig)
        } else {
            Observable.fromCallable { fitConfigsInfoSync() }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe { callback?.invoke(HKHybirdUtil.getUnzipDir(currentConfig), currentConfig) }
        }
    }

    /**
     * 检查更新一共有三个地方
     *
     * 更新策略为ONLINE 时,  1:程序启动,2:前后台切换,3:webView加载模块
     * 更新策略为OFFLINE 时,  1:程序启动,2:前后台切换
     */
    fun checkUpdate(synchronized: Boolean = true, switchToOnlineModeIfRemoteVersionChanged: Boolean = false) {

        //======================================================================================
        // 暂时修改系统策略(因为网络请求不能再主线程执行)
        //======================================================================================
        val oldThreadPolicy = StrictMode.getThreadPolicy()
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())
        //======================================================================================

        updateManager.checkUpdate(synchronized, switchToOnlineModeIfRemoteVersionChanged)

        //======================================================================================
        // 还原系统策略
        //======================================================================================
        StrictMode.setThreadPolicy(oldThreadPolicy)
        //======================================================================================
    }

    @Synchronized
    private fun checkHealthSync() {
        HKLogUtil.w(moduleName, "健康体检(同步:${Thread.currentThread().name}) 开始")
        val start = System.currentTimeMillis()

        val isConfigNull = currentConfig == null
        val isLocalFilesValid = HKHybirdUtil.isLocalFilesValid(currentConfig)
        if (isConfigNull || !isLocalFilesValid) {
            HKLogUtil.w(moduleName, "健康体检(同步:${Thread.currentThread().name}) 检测到当前配置信息文件为空或者文件校验失败,开始执行修复操作 , isConfigNull==$isConfigNull , isLocalFilesValid==$isLocalFilesValid")
            fitConfigsInfoSync()
        } else {
            HKLogUtil.v(moduleName, "健康体检(同步:${Thread.currentThread().name}) 检测到当前配置信息文件完善且文件校验成功,非常健康!")
        }
        HKLogUtil.w(moduleName, "健康体检(同步:${Thread.currentThread().name}) 结束 , 耗时: ${System.currentTimeMillis() - start}ms")
    }

    /**
     * 同步 处理本地配置信息,如果当前模块没有被打开,则会优先处理下次生效的配置信息然后立即处理本地配置信息
     */
    @Synchronized
    private fun fitConfigsInfoSync() {
        if (lifecycleManager.isModuleOpenNow()) {
            fitLocalConfigsInfoSync()
        } else {
            fitNextAndLocalConfigsInfoSync()
        }
    }

    /**
     * 同步 处理下次生效的配置信息以及处理完后紧接着处理本地配置信息
     */
    @Synchronized
    private fun fitNextAndLocalConfigsInfoSync() {
        fitNextAndFitLocalIfNeedConfigsInfoSync(true)
    }

    /**
     * 当检测到模块完全没有被浏览器加载的时候,可以调用此方法是否有 下次加载生效的配置信息 ,并异步处理本地文件,重置当前模块的 版本头信息
     *
     * 调用时机
     * 1: 当前模块退出浏览器的时候(异步处理)
     * 2: 当前模块第一次被浏览器加载的时候(同步处理)
     */
    internal fun fitNextAndFitLocalIfNeedConfigsInfo() {
        val start = System.currentTimeMillis()
        HKLogUtil.v(moduleName, "检测是否有 下次启动生效的配置文件 需要处理(异步) 开始 , 当前线程:${Thread.currentThread().name} , (如果本模块没有被浏览器加载,则优先合并 下次启动生效的任务, 当前 onLineMode = $onlineModel)")
        Observable.fromCallable {
            fitNextAndFitLocalIfNeedConfigsInfoSync()
            HKLogUtil.v(moduleName, "检测是否有 下次启动生效的配置文件 需要处理(异步) 结束 , 当前线程:${Thread.currentThread().name} , (如果本模块没有被浏览器加载,则优先合并 下次启动生效的任务, 当前 onLineMode = $onlineModel) ,  耗时: ${System.currentTimeMillis() - start}ms")
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe()
    }

    /**
     * 同步 处理下次生效的配置信息以及处理完后 如果需要的话(即确实有下次生效的配置信息的情况下,避免检查下次配置信息的时候多做一次处理本地配置信息的操作)紧接着处理本地配置信息
     */
    @Synchronized
    private fun fitNextAndFitLocalIfNeedConfigsInfoSync(mustFitLocal: Boolean = false) {
        HKLogUtil.v(moduleName, "检测是否有 下次启动生效的配置文件 需要处理(同步) 开始 ,当前线程:${Thread.currentThread().name}")

        if (onlineModel || lifecycleManager.isModuleOpenNow()) {
            HKLogUtil.v(moduleName, "检测到 当前为在线模式 onlineModel=$onlineModel 或者当前模块正在被浏览器使用 isModuleOpenNow=${lifecycleManager.isModuleOpenNow()} ,不能执行本操作 return")
            return
        }

        val configList = configManager.getConfigList() //版本号降序排序

        if (!onlineModel) {
            HKLogUtil.v(moduleName, "检测当前模块未被浏览器加载,可以处理")

            val nextConfigStack = configManager.getNextConfigStack()
            if (!nextConfigStack.empty()) {
                HKLogUtil.v(moduleName, "检测下次生效的配置信息不为空,开始处理")
                val destConfig = nextConfigStack.pop()
                val destVersion = destConfig.moduleVersion.toFloatOrNull()
                if (destVersion != null) {
                    if (configList.isNotEmpty()) {
                        val iterate = configList.listIterator()
                        while (iterate.hasNext()) {
                            val tmpConfig = iterate.next()
                            val tmpVersion = tmpConfig.moduleVersion.toFloatOrNull()
                            //版本号为空 或者 这是回滚操作,则清空大于等于该版本的所有文件/配置
                            if (tmpVersion == null || tmpVersion >= destVersion) {
                                HKLogUtil.v(moduleName, "清空版本号为$tmpVersion(升级/回滚的目标版本为$destVersion) 的所有本地文件以及配置信息")
                                iterate.remove()                                                                //删除在list中的位置
                                HKFileUtil.deleteFile(HKHybirdUtil.getZipFile(tmpConfig))              //删除 zip
                                HKFileUtil.deleteDirectory(HKHybirdUtil.getUnzipDir(tmpConfig))        //删除 unzipDir
                            }
                        }
                    }
                    configList.add(0, destConfig)

                    configManager.saveConfig(configList)  //彻底删除配置信息，至此已经删除了所有与本版本相关的信息
                    currentConfig = destConfig
                    HKLogUtil.e(moduleName, "重置当前 本地配置头 为:${currentConfig?.moduleVersion}")
                }
                configManager.clearConfigNext()
            } else {
                HKLogUtil.v(moduleName, "检测下次生效的配置信息为空,无需处理")
            }

            //ifNeed 体现在此处
            if (nextConfigStack.isNotEmpty() || mustFitLocal) fitLocalConfigsInfoSync()
        } else {
            HKLogUtil.v(moduleName, "检测当前模块正在被浏览器加载,不能处理")
        }

        HKLogUtil.v(moduleName, "检测是否有 下次启动生效的配置文件 需要处理(同步) 结束 ,当前线程:${Thread.currentThread().name}")
    }

    /**
     * 与 nextConfig 互不相关
     */
    private fun fitLocalConfigsInfo() {
        val start = System.currentTimeMillis()
        HKLogUtil.v(moduleName, "一次检验本地所有可用配置信息的完整性(异步) 开始 , 当前线程:${Thread.currentThread().name} , (如果本模块没有被浏览器加载,则优先合并 下次启动生效的任务, 当前 onLineMode = $onlineModel)")
        Observable.fromCallable {
            fitLocalConfigsInfoSync()
            HKLogUtil.v(moduleName, "一次检验本地所有可用配置信息的完整性(异步) 结束 , 当前线程:${Thread.currentThread().name} , (如果本模块没有被浏览器加载,则优先合并 下次启动生效的任务, 当前 onLineMode = $onlineModel) ,  耗时: ${System.currentTimeMillis() - start}ms")
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe()
    }

    /**
     * 校验所有版本信息,如果无效则删除该版本相关的所有文件/配置
     * 重置当前版本指向
     * 如果本模块尚未被打开,则下一次启动生效的更新/回滚配置 在此一并兼容
     */
    @Synchronized
    private fun fitLocalConfigsInfoSync() {
        HKLogUtil.e(moduleName, "一次检验本地可用配置信息的完整性(同步) 开始 , 当前线程:${Thread.currentThread().name} , (如果本模块没有被浏览器加载,则优先合并 下次启动生效的任务, 当前 onLineMode = $onlineModel)")
        val start = System.currentTimeMillis()
        val configList = configManager.getConfigList()
        HKLogUtil.e(moduleName, "当前最新配置信息为: ${configList.map { it.moduleVersion }}")
        if (configList.isNotEmpty()) {
            HKLogUtil.v(moduleName, "检测到本地配置信息不为空,开始过滤/清理无效的本地配置信息")

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
            HKLogUtil.w(moduleName, "检测到本地配置信息为空,重新从原始包拷贝")
            HKHybirdUtil.getPrimaryConfigFromAssets(moduleName)?.let { configList.add(it) } //如果为空则添加原始配置信息
        }
        //如果有删除的或则新加的原始配置信息，则需要重新保存下
        configManager.saveConfig(configList)

        currentConfig = null
        configList.firstOrNull()?.let { currentConfig = it }
        HKLogUtil.e(moduleName, "重置当前 本地配置头 为:${currentConfig?.moduleVersion}")
        HKLogUtil.e(moduleName, "一次检验本地所有可用配置信息(不包含 next)的完整性(同步) 结束 , 当前线程:${Thread.currentThread().name} , (如果本模块没有被浏览器加载,则优先合并 下次启动生效的任务, 当前 onLineMode = $onlineModel) ,  耗时: ${System.currentTimeMillis() - start}ms")
    }

    /**
     * 设置资源拦截器,URL拦截器
     */
    private fun setIntercept(currentConfig: HKHybirdConfigModel?) {
        HKLogUtil.v(currentConfig?.moduleName, "设置拦截器开始: ${currentConfig?.moduleVersion}")

        if (currentConfig == null) {
            HKLogUtil.v(currentConfig?.moduleName, "检测到 配置文件为空 , 取消设置拦截器 return")
            return
        }

        val interceptUrl = currentConfig.moduleMainUrl

        if (interceptUrl.isBlank()) {
            HKLogUtil.e(currentConfig.moduleName, "检测到 interceptMainUrl == null return")
            return
        }

        HKHybirdBridge.removeScheme(interceptUrl)
        HKHybirdBridge.removeRequest(interceptUrl)

        /**
         * webView.loadUrl 不会触发此回调,放到 HKHybirdBridge.addRequest(interceptMainUrl) 里面处理
         * http://www.jianshu.com/p/3474cb8096da
         */
        HKLogUtil.v(currentConfig.moduleName, "增加 URL 拦截 , 匹配 -> interceptMainUrl : $interceptUrl")
        HKHybirdBridge.addScheme(interceptUrl) { _: WebView?, webViewClient: WebViewClient?, url: String? ->
            lifecycleManager.onWebViewOpenPage(webViewClient, url)

            HKLogUtil.e(currentConfig.moduleName, "系统拦截到模块URL请求: url=$url ,匹配到 检测内容为 '$interceptUrl' 的拦截器, 由于当前模块的策略为 '$updateStrategy' , ${if (updateStrategy == HKHybirdUpdateStrategy.ONLINE) "需要检测更新,开始更新" else "不需要检查更新,不拦截 return"}")

            //仅仅更新策略为 ONLINE 时,才会执行此步骤
            if (updateStrategy == HKHybirdUpdateStrategy.ONLINE) {
                checkUpdate(synchronized = true, switchToOnlineModeIfRemoteVersionChanged = true)
            }
            false
        }

        HKLogUtil.v(currentConfig.moduleName, "增加 资源 拦截 , 匹配 -> interceptUrl : $interceptUrl")
        HKHybirdBridge.addRequest(interceptUrl) { _: WebView?, url: String? ->
            HKLogUtil.v(currentConfig.moduleName, "shouldInterceptRequest: $url ,匹配拦截器:$interceptUrl")
            var resourceResponse: WebResourceResponse? = null
            if (!onlineModel) {
                val localFile = HKHybirdUtil.getLocalFile(currentConfig, url)
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
                        HKLogUtil.v(currentConfig.moduleName, "执行伪造本地资源")
                        resourceResponse = WebResourceResponse(mimeType, "UTF-8", FileInputStream(localFile))
                    } catch (e: Exception) {
                        HKLogUtil.e(currentConfig.moduleName, "伪造本地资源出错", e)
                    }
                } else {
                    HKLogUtil.v(currentConfig.moduleName, "系统检测到本地文件不存在,访问在线资源")
                }
            } else {
                HKLogUtil.v(currentConfig.moduleName, "系统检测到当前为在线模式,访问在线资源")
            }
            resourceResponse
        }
        HKLogUtil.v(currentConfig.moduleName, "设置拦截器结束")
    }

    fun setDownloader(downloader: (downloadUrl: String, file: File?, callback: (File?) -> Unit?) -> Unit?) {
        updateManager.downloader = downloader
    }

    fun setConfiger(configer: (configUrl: String, callback: (HKHybirdConfigModel?) -> Boolean?) -> Boolean?) {
        updateManager.configer = configer
    }
}
