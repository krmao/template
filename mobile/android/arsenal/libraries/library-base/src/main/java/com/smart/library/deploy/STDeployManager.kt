package com.smart.library.deploy

import android.app.Activity
import android.support.annotation.UiThread
import com.smart.library.base.STBaseApplication
import com.smart.library.deploy.client.STIDeployClient
import com.smart.library.deploy.client.impl.STDeployClientForReactNative
import com.smart.library.deploy.model.STDeployApplyType
import com.smart.library.deploy.model.STDeployCheckUpdateType
import com.smart.library.deploy.model.STDeployConfigModel
import com.smart.library.deploy.model.STIDeployCheckUpdateCallback
import com.smart.library.util.STLogUtil
import com.smart.library.util.STValueUtil
import com.smart.library.util.cache.STCacheManager
import java.io.File

@Suppress("MemberVisibilityCanBePrivate", "unused", "PropertyName")
enum class STDeployManager(private var debug: Boolean, private var rootDir: File, private val checkUpdateTypes: MutableSet<STDeployCheckUpdateType>, private val applyTypes: MutableSet<STDeployApplyType>) : STIDeployClient {

    ANDROID(STBaseApplication.DEBUG, STCacheManager.getFilesHotPatchAndroidDir(), mutableSetOf(), mutableSetOf()),
    HYBIRD(STBaseApplication.DEBUG, STCacheManager.getFilesHotPatchHybirdDir(), mutableSetOf(), mutableSetOf()),
    REACT_NATIVE(STBaseApplication.DEBUG, STCacheManager.getFilesHotPatchReactNativeDir(), mutableSetOf(), mutableSetOf());

    val TAG = "[deploy-${this.name}]"

    val preferenceManager by lazy { STDeployPreferenceManager(this) }

    private var deployClient: STIDeployClient? = null

    fun isDebug(): Boolean = debug

    @Volatile
    private var startedCount: Int = 0
        set(value) {
            STLogUtil.w(TAG, "$name detect page started count changed from $field to $value")
            field = value
        }

    /**
     * call on Activity or Fragment create lifecycle
     * should call at main thread
     */
    fun onCreate(activity: Activity?, checkUpdateCallback: STIDeployCheckUpdateCallback) {
        beforePageOpeningListener(isAllPagesClosed(), object : STIDeployCheckUpdateCallback {
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
                if (STValueUtil.isValid(activity)) {
                    startedCount++
                    checkUpdateCallback.onApplyCallback(applySuccess)
                } else {
                    STLogUtil.e(TAG, "activity is invalid, cancel init react view")
                }
            }
        })
    }

    /**
     * call on Activity or Fragment destroy lifecycle
     * should call at main thread
     */
    fun onDestroy() {
        startedCount--

        afterPageClosedListener(isAllPagesClosed())
    }

    fun isAtLeastOnePageOpened(): Boolean = startedCount > 0

    fun isAllPagesClosed(): Boolean = startedCount == 0

    private fun beforePageOpeningListener(isFirstPageOpening: Boolean, checkUpdateCallback: STIDeployCheckUpdateCallback) {
        STLogUtil.w(TAG, "beforePageOpeningListener-> isFirstPageOpening:$isFirstPageOpening")
        if (isFirstPageOpening && checkUpdateTypes.contains(STDeployCheckUpdateType.APP_OPEN_FIRST_PAGE)) {
            checkUpdate(checkUpdateCallback)// 首次打开同步更新并应用后继续
            return
        }

        checkUpdateCallback.onCheckUpdateCallback(false)
        checkUpdateCallback.onDownloadCallback(false)
        checkUpdateCallback.onMergePatchCallback(false)
        checkUpdateCallback.onApplyCallback(false)

        if (checkUpdateTypes.contains(STDeployCheckUpdateType.APP_OPEN_EVERY_PAGE)) {
            checkUpdate()// 非首次打开 异步更新
        }
    }

    private fun afterPageClosedListener(isAllPagesClosed: Boolean) {
        if (isAllPagesClosed) {
            if (applyTypes.contains(STDeployApplyType.APP_CLOSE_ALL_PAGES)) tryApply()
        }
    }

    /**
     * @param deployConfig if is null, may custom init by @see resetClient
     */
    @JvmOverloads
    fun initialize(debug: Boolean = this.debug, rootDir: File = this.rootDir, deployConfig: STDeployConfigModel?, checkUpdateTypes: MutableSet<STDeployCheckUpdateType>, applyTypes: MutableSet<STDeployApplyType>) {
        this.debug = debug
        this.rootDir = rootDir
        this.checkUpdateTypes.clear()
        this.checkUpdateTypes.addAll(checkUpdateTypes)
        this.applyTypes.clear()
        this.applyTypes.addAll(applyTypes)

        when (this) {
            STDeployManager.ANDROID -> {
            }
            STDeployManager.HYBIRD -> {
            }
            STDeployManager.REACT_NATIVE -> {
                deployConfig?.let { this.deployClient = STDeployClientForReactNative(this.debug, this.rootDir, it, this.TAG) }
            }
        }
    }

    /**
     * only available when deployConfig param in initialize is null
     */
    fun resetClient(deployClient: STIDeployClient) {
        if (this.deployClient == null) this.deployClient = deployClient
    }

    override fun checkUpdate() {
        deployClient?.checkUpdate()
    }

    override fun checkUpdate(checkUpdateCallback: STIDeployCheckUpdateCallback?) {
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
