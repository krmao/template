package com.smart.template

import android.graphics.Color
import com.smart.library.base.CXBaseApplication
import com.smart.library.base.CXConfig
import com.smart.library.deploy.CXDeployManager
import com.smart.library.deploy.model.CXDeployType
import com.smart.library.util.CXFileUtil
import com.smart.library.util.CXLogUtil
import com.smart.library.util.image.CXImageManager
import com.smart.library.util.image.impl.CXImageFrescoHandler
import com.smart.library.widget.debug.CXDebugFragment
import com.smart.library.widget.titlebar.CXTitleBar
import com.smart.template.handlers.OnRNCallNativeHandler
import com.smart.template.library.R
import com.smart.template.module.rn.ReactActivity
import com.smart.template.module.rn.ReactManager
import com.smart.template.module.rn.dev.ReactDevSettingsView
import com.smart.template.repository.CXRepository
import com.smart.template.repository.remote.core.CXOkHttpManager
import com.smart.template.tab.HomeTabActivity
import java.io.File

class FinalApplication : CXBaseApplication() {

    override fun onCreate() {
        // init before application onCreate
        CXConfig.NOTIFICATION_ICON_SMALL = R.mipmap.ic_notification
        CXConfig.CLASS_ACTIVITY_MAIN = HomeTabActivity::class.java

        CXTitleBar.DEFAULT_BACKGROUND_COLOR = Color.BLACK
        CXTitleBar.DEFAULT_TEXT_COLOR = Color.WHITE
        CXTitleBar.DEFAULT_TEXT_SIZE = 16f

        super.onCreate()

        //if (CXBaseApplication.DEBUG) {
        Thread.setDefaultUncaughtExceptionHandler { t, e -> CXFileUtil.saveUncaughtException(t, e) }
        //}

        // init global location
        // CXLocationManager.initialize(CXLocationClientForAMap())

        // init global repository
        CXRepository.init()

        CXDebugFragment.childViewList.add(ReactDevSettingsView::class.java)

        // image manager with fresco and react native init together
        val frescoConfig = CXImageFrescoHandler.getConfigBuilder(CXBaseApplication.DEBUG, CXOkHttpManager.client).build()
        CXImageManager.initialize(CXImageFrescoHandler(frescoConfig))

        CXDeployManager.initialize(
                mutableSetOf(
                        CXDeployType.REACT_NATIVE.apply {
                            this.supportCheckTypes.addAll(listOf(
                                    CXDeployType.CheckType.APP_START,
                                    CXDeployType.CheckType.APP_FORGROUND_TO_BACKGROUND,
                                    CXDeployType.CheckType.APP_CLOSE_ALL_PAGES

                            ))
                        }
                ),
                { indexBundleFile: File? ->
                    // 无论 copy 成功还是失败, 都应该初始化, 方便远程加载
                    ReactManager.init(this, CXBaseApplication.DEBUG, indexBundleFile, frescoConfig, OnRNCallNativeHandler())
                },
                {
                    if (ReactManager.instanceManager != null) {
                        CXLogUtil.e(CXDeployManager.TAG, "recreateReactContextInBackground start")
                        ReactManager.reloadBundleFromSdcard(it)
                        true
                    } else {
                        ReactManager.indexBundleFileInSdcard = it
                        CXLogUtil.e(CXDeployManager.TAG, "recreateReactContextInBackground failure, instanceManager is null")
                        false
                    }
                },
                {
                    ReactActivity.isAtLeastOnePageOpened()
                }
        )
    }
}
