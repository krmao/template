package com.smart.library.deploy

import com.smart.library.base.CXApplicationVisibleChangedEvent
import com.smart.library.deploy.client.impl.CXDeployClientForReactNative
import com.smart.library.deploy.model.CXDeployType
import com.smart.library.deploy.model.bundle.CXBundleInfo
import com.smart.library.deploy.model.bundle.CXPatchInfo
import com.smart.library.util.CXFileUtil
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

    val URI_SCHEME_ASSETS = "asset://"

    private val supportTypes: MutableSet<CXDeployType> = mutableSetOf()

    @JvmStatic
    fun initialize(supportTypes: MutableSet<CXDeployType>, initCallback: (indexBundleFile: File?) -> Unit?, reloadHandler: () -> Unit?, isRNOpenedHandler: () -> Boolean) {
        this.supportTypes.addAll(supportTypes)

        val rnCXIDeployClient = CXDeployClientForReactNative(
                CXBundleInfo(1),
                CXCacheManager.getFilesHotPatchReactNativeDir(),
                "bundle-rn.zip",
                {
                    CXLogUtil.d(CXDeployManager.TAG, "check start")
                    async {
                        Thread.sleep(1000)
                        CXLogUtil.d(CXDeployManager.TAG, "check end")
                        it.invoke(null, CXPatchInfo(1, 2), "${URI_SCHEME_ASSETS}app50-rn1-rn2.patch", true)
                    }

                    Unit
                },
                { patchDownloadUrl: String?, file: File, downloadCallback: (file: File) -> Unit ->
                    CXLogUtil.d(CXDeployManager.TAG, "download start")
                    async {
                        Thread.sleep(1000)
                        CXLogUtil.d(CXDeployManager.TAG, "download end")

                        if (patchDownloadUrl?.startsWith(URI_SCHEME_ASSETS) == true) {
                            CXFileUtil.copyFromAssets(patchDownloadUrl.substringAfter(URI_SCHEME_ASSETS), file)
                        }

                        downloadCallback.invoke(file)
                    }

                    Unit
                },
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
