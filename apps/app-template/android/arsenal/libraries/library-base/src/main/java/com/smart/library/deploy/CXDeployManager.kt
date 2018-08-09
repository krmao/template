package com.smart.library.deploy

import com.smart.library.base.CXBaseApplication
import com.smart.library.deploy.client.CXIDeployClient
import com.smart.library.deploy.client.impl.CXDeployClientForReactNative
import com.smart.library.deploy.model.CXDeployApplyType
import com.smart.library.deploy.model.CXDeployCheckUpdateType
import com.smart.library.deploy.model.CXDeployConfigModel
import com.smart.library.deploy.model.CXIDeployCheckUpdateCallback
import com.smart.library.util.CXLogUtil
import com.smart.library.util.cache.CXCacheManager
import java.io.File

@Suppress("MemberVisibilityCanBePrivate", "unused", "PropertyName")
enum class CXDeployManager(private var debug: Boolean, private var rootDir: File, private val checkUpdateTypes: MutableSet<CXDeployCheckUpdateType>, private val applyTypes: MutableSet<CXDeployApplyType>) : CXIDeployClient {

    ANDROID(CXBaseApplication.DEBUG, CXCacheManager.getFilesHotPatchAndroidDir(), mutableSetOf(), mutableSetOf()),
    HYBIRD(CXBaseApplication.DEBUG, CXCacheManager.getFilesHotPatchHybirdDir(), mutableSetOf(), mutableSetOf()),
    REACT_NATIVE(CXBaseApplication.DEBUG, CXCacheManager.getFilesHotPatchReactNativeDir(), mutableSetOf(), mutableSetOf());

    val TAG = "[deploy-${this.name}]"

    val preferenceManager by lazy { CXDeployPreferenceManager(this) }

    private var deployClient: CXIDeployClient? = null

    fun isDebug(): Boolean = debug

    @Volatile
    private var startedCount: Int = 0
        set(value) {
            CXLogUtil.w(TAG, "$name detect page started count changed from $field to $value")
            field = value
        }

    /**
     * call on Activity or Fragment create lifecycle
     */
    fun onCreate(checkUpdateCallback: CXIDeployCheckUpdateCallback) {
        beforePageOpeningListener(isAllPagesClosed(), object : CXIDeployCheckUpdateCallback {
            override fun onCheckUpdateCallback(isHaveNewVersion: Boolean) {
                checkUpdateCallback.onCheckUpdateCallback(isHaveNewVersion)
            }

            override fun onDownloadCallback(downloadSuccess: Boolean) {
                checkUpdateCallback.onDownloadCallback(downloadSuccess)
            }

            override fun onMergePatchCallback(mergeSuccess: Boolean) {
                checkUpdateCallback.onMergePatchCallback(mergeSuccess)
            }

            override fun onApplyCallback(applySuccess: Boolean) {
                startedCount++
                checkUpdateCallback.onApplyCallback(applySuccess)
            }
        })
    }

    /**
     * call on Activity or Fragment destroy lifecycle
     */
    fun onDestroy() {
        startedCount--

        afterPageClosedListener(isAllPagesClosed())
    }

    fun isAtLeastOnePageOpened(): Boolean = startedCount > 0

    fun isAllPagesClosed(): Boolean = startedCount == 0

    private fun beforePageOpeningListener(isFirstPageOpening: Boolean, checkUpdateCallback: CXIDeployCheckUpdateCallback) {
        CXLogUtil.w(TAG, "beforePageOpeningListener-> isFirstPageOpening:$isFirstPageOpening")
        if (isFirstPageOpening && checkUpdateTypes.contains(CXDeployCheckUpdateType.APP_OPEN_FIRST_PAGE)) {
            checkUpdate(checkUpdateCallback)// 首次打开同步更新并应用后继续
            return
        }

        checkUpdateCallback.onCheckUpdateCallback(false)
        checkUpdateCallback.onDownloadCallback(false)
        checkUpdateCallback.onMergePatchCallback(false)
        checkUpdateCallback.onApplyCallback(false)

        if (checkUpdateTypes.contains(CXDeployCheckUpdateType.APP_OPEN_EVERY_PAGE)) {
            checkUpdate()// 非首次打开 异步更新
        }
    }

    private fun afterPageClosedListener(isAllPagesClosed: Boolean) {
        if (isAllPagesClosed) {
            if (applyTypes.contains(CXDeployApplyType.APP_CLOSE_ALL_PAGES)) tryApply()
        }
    }

    /**
     * @param deployConfig if is null, may custom init by @see resetClient
     */
    @JvmOverloads
    fun initialize(debug: Boolean = this.debug, rootDir: File = this.rootDir, deployConfig: CXDeployConfigModel?, checkUpdateTypes: MutableSet<CXDeployCheckUpdateType>, applyTypes: MutableSet<CXDeployApplyType>) {
        this.debug = debug
        this.rootDir = rootDir
        this.checkUpdateTypes.clear()
        this.checkUpdateTypes.addAll(checkUpdateTypes)
        this.applyTypes.clear()
        this.applyTypes.addAll(applyTypes)

        when (this) {
            CXDeployManager.ANDROID -> {
            }
            CXDeployManager.HYBIRD -> {
            }
            CXDeployManager.REACT_NATIVE -> {
                deployConfig?.let { this.deployClient = CXDeployClientForReactNative(this.debug, this.rootDir, it, this.TAG) }
            }
        }
    }

    /**
     * only available when deployConfig param in initialize is null
     */
    fun resetClient(deployClient: CXIDeployClient) {
        if (this.deployClient == null) this.deployClient = deployClient
    }

    override fun checkUpdate() {
        deployClient?.checkUpdate()
    }

    override fun checkUpdate(checkUpdateCallback: CXIDeployCheckUpdateCallback?) {
        if (deployClient == null) {
            checkUpdateCallback?.onCheckUpdateCallback(false)
            checkUpdateCallback?.onDownloadCallback(false)
            checkUpdateCallback?.onMergePatchCallback(false)
            checkUpdateCallback?.onApplyCallback(false)
        } else {
            deployClient?.checkUpdate(checkUpdateCallback)
        }
    }

    override fun getRootDir(): File = this.rootDir

    override fun tryApply() {
        deployClient?.tryApply()
    }

    override fun tryApply(applyCallback: ((applySuccess: Boolean) -> Unit?)?) {
        deployClient?.tryApply(applyCallback)
    }
}
