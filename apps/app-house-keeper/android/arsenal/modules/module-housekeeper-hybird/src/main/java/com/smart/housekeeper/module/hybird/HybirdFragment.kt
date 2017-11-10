package com.smart.housekeeper.module.hybird

import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceResponse
import android.webkit.WebView
import com.smart.library.base.HKBaseFragment
import com.smart.library.bundle.HKHybirdManager
import com.smart.library.bundle.HKHybirdModuleConfiguration
import com.smart.library.util.HKLogUtil
import com.smart.library.util.HKToastUtil
import com.smart.library.util.hybird.HKHybirdBridge
import kotlinx.android.synthetic.main.hybird_fragment.*
import java.io.File
import java.io.FileInputStream

class HybirdFragment : HKBaseFragment() {
    private val TAG = HybirdFragment::class.java.simpleName

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater?.inflate(R.layout.hybird_fragment, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        @Suppress("UNUSED_ANONYMOUS_PARAMETER")
        text.setOnClickListener {
            HKHybirdManager.Module.BASE.manager.verify { localUnzipDir, configuration ->
                addRequestIntercept(localUnzipDir, configuration)
                val indexPath = configuration?.moduleSchemeUrls?.get("sit") + "/index.shtml"
                HybirdWebFragment.goTo(activity, indexPath)
                HKLogUtil.d("hybird", "加载成功")
                HKToastUtil.show("加载成功")
            }
        }
    }

    private fun addRequestIntercept(localUnzipDir: File, configuration: HKHybirdModuleConfiguration?) {
        val interceptUrl = configuration?.moduleSchemeUrls?.get("sit") ?: return

        HKHybirdBridge.addRequest(interceptUrl) { _: WebView?, url: String? ->
            var resourceResponse: WebResourceResponse? = null
            if (!TextUtils.isEmpty(url)) {
                val requestUrl = Uri.parse(url)
                val scheme = requestUrl?.scheme?.trim()
                if (requestUrl != null && ("http".equals(scheme, true) || "https".equals(scheme, true))) {
                    if (url!!.contains(interceptUrl, true)) {
                        val tmpPath = requestUrl.toString()
                            .replace(interceptUrl, "")
                            .replace("https://", "")
                            .replace("http://", "")

                        val mimeType = when {
                            requestUrl.toString().contains(".css") -> "text/css"
                            requestUrl.toString().contains(".png") -> "image/png"
                            requestUrl.toString().contains(".js") -> "application/x-javascript"
                            requestUrl.toString().contains(".woff") -> "application/x-font-woff"
                            else -> "text/html"
                        }

                        val localPath = localUnzipDir.absolutePath + tmpPath
                        val localFileExists = File(localPath).exists()
                        HKLogUtil.v(TAG, "shouldInterceptRequest:<do intercept?$localFileExists(localFile.exists?$localFileExists)>, [originPath: " + requestUrl.toString() + "], [localPath: $localPath]")
                        if (localFileExists) {
                            try {
                                resourceResponse = WebResourceResponse(mimeType, "UTF-8", FileInputStream(localPath))
                            } catch (e: Exception) {
                                HKLogUtil.e(TAG, "shouldInterceptRequest:<intercept error>: ", e)
                            }
                        }
                    }
                } else {
                    HKLogUtil.v(TAG, "shouldInterceptRequest:<do intercept?false>, [originPath: $url], [interceptHost: $interceptUrl]")
                }
            } else {
                HKLogUtil.v(TAG, "shouldInterceptRequest:<do intercept?false>, [originPath: $url], [interceptHost: $interceptUrl]")
            }
            resourceResponse
        }
    }
}
