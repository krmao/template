package com.smart.library.deploy

import com.smart.library.base.CXApplicationVisibleChangedEvent
import com.smart.library.deploy.client.impl.CXDeployClientForReactNative
import com.smart.library.deploy.model.CXDeployType
import com.smart.library.deploy.model.bundle.CXBundleInfo
import com.smart.library.deploy.model.bundle.CXPatchInfo
import com.smart.library.util.CXLogUtil
import com.smart.library.util.cache.CXCacheManager
import com.smart.library.util.rx.RxBus
import org.jetbrains.anko.async
import java.io.File

/**
 * 动态部署管理器
 *
 *
 * android              hotfix + dynamic deployment(contain hotfix)
 * hybird               hotfix + dynamic deployment(contain hotfix)
 * react-native         hotfix + dynamic deployment(contain hotfix)
 *
 */
object CXDeployManager {
    const val TAG = "[rn-deploy]"

    @JvmStatic
    var debug = true
        private set

    const val URI_SCHEME_ASSETS = "asset://"

    private val supportTypes: MutableSet<CXDeployType> = mutableSetOf()

    @JvmStatic
    fun initialize(supportTypes: MutableSet<CXDeployType>, initCallback: (indexBundleFile: File?) -> Unit?,
                   checkHandler: ((bundleInfo: CXBundleInfo?, patchInfo: CXPatchInfo?, downloadUrl: String?, isPatch: Boolean) -> Unit?) -> Unit?,
                   downloadHandler: (patchDownloadUrl: String?, toFile: File, callback: (file: File) -> Unit) -> Unit?,

                   reloadHandler: (indexBundleFileInSdcard: File) -> Boolean, isRNOpenedHandler: () -> Boolean) {
        this.supportTypes.addAll(supportTypes)

        val rnCXIDeployClient = CXDeployClientForReactNative(
                CXBundleInfo(1),
                CXCacheManager.getFilesHotPatchReactNativeDir(),
                "bundle-rn.zip",
                checkHandler,
                downloadHandler,
                isRNOpenedHandler
        )

        rnCXIDeployClient.initialize({
            CXLogUtil.e(CXDeployManager.TAG, "initialize end, indexBundleFile=${it?.absolutePath}")
            initCallback.invoke(it)
        }, reloadHandler)

        rnCXIDeployClient.apply()

        RxBus.toObservable(CXApplicationVisibleChangedEvent::class.java).subscribe {
            if (!it.isApplicationVisible) {
                rnCXIDeployClient.check()
            }
        }

        async {
            Thread.sleep(5000)
            rnCXIDeployClient.check()
        }

    }

}
