package com.smart.library.deploy

import com.smart.library.deploy.client.impl.CXDeployClientForReactNative
import com.smart.library.deploy.model.CXDeployType
import com.smart.library.deploy.model.bundle.CXBundleInfo
import com.smart.library.util.CXLogUtil
import com.smart.library.util.cache.CXCacheManager
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

    private val supportTypes: MutableSet<CXDeployType> = mutableSetOf()

    @JvmStatic
    fun initialize(supportTypes: MutableSet<CXDeployType>, callback: (indexBundleFile: File?) -> Unit) {
        this.supportTypes.addAll(supportTypes)


        val rnCXIDeployClient = CXDeployClientForReactNative(
                CXBundleInfo("bundle-rn.zip", 1),
                CXCacheManager.getFilesHotPatchReactNativeDir(),
                {
                    CXLogUtil.d(CXDeployManager.TAG, "check start")
                    async {
                        Thread.sleep(1000)
                        CXLogUtil.d(CXDeployManager.TAG, "check end")
                        it.invoke(null, null)
                    }

                    Unit
                },
                { patchDownloadUrl: String?, downloadCallback: (file: File?) -> Unit ->
                    CXLogUtil.d(CXDeployManager.TAG, "download start")
                    async {
                        Thread.sleep(1000)
                        CXLogUtil.d(CXDeployManager.TAG, "download end")
                        downloadCallback.invoke(null)
                    }

                    Unit
                }
        )

        rnCXIDeployClient.initialize {
            CXLogUtil.e(CXDeployManager.TAG, "initialize end, indexBundleFile=${it?.absolutePath}")
            callback.invoke(it)
        }
    }

}
