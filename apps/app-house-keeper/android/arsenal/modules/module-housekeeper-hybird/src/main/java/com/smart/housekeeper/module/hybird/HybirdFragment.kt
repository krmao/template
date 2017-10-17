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
import com.smart.library.bundle.imp.HKBundleManager
import com.smart.library.util.HKLogUtil
import com.smart.library.util.HKToastUtil
import com.smart.library.util.hybird.HKHybirdManager
import kotlinx.android.synthetic.main.hybird_fragment.*
import java.io.FileInputStream

class HybirdFragment : HKBaseFragment() {
    companion object {
        val HOST = "www.smarttemplate.com"
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater?.inflate(R.layout.hybird_fragment, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        @Suppress("UNUSED_ANONYMOUS_PARAMETER")
        text.setOnClickListener {
            HKBundleManager.installWithVerify { success, roodDir ->
                // val indexPath = "file://${roodDir}index.html"
                val indexPath = "https://$HOST/index.html"
                if (success) {
                    HybirdWebFragment.goTo(activity, indexPath)

                    HKLogUtil.d("hybird", "加载成功")
                    HKToastUtil.show("加载成功")
                } else {
                    HKLogUtil.e("hybird", "加载失败,请重新安装程序")
                    HKToastUtil.show("加载失败,请重新安装程序")
                }
            }
        }

        val interceptHost = HybirdFragment.HOST
        HKHybirdManager.addRequest(interceptHost) { _: WebView?, url: String? ->
            var resourceResponse: WebResourceResponse? = null

            if (!TextUtils.isEmpty(url)) {
                val requestUrl = Uri.parse(url)
                val scheme = requestUrl?.scheme?.trim()

                if (requestUrl != null && ("http".equals(scheme, true) || "https".equals(scheme, true))) {
                    if (interceptHost.equals(requestUrl.host, true)) {

                        HKLogUtil.v(HKHybirdManager.TAG, "shouldInterceptRequest: intercept originPath: " + requestUrl.toString())

                        try {
                            val tmpPath = requestUrl.toString()
                                .replace("https://", "")
                                .replace("http://", "")
                                .replace(interceptHost + "/", "")
                                .replace(interceptHost, "")
                            //.replace("/#!.*".toRegex(), "")
                            //.replace("/index.html", "")

                            HKLogUtil.w(HKHybirdManager.TAG, "shouldInterceptRequest: intercept tmpPath: $tmpPath")

                            val mimeType = when {
                                requestUrl.toString().contains(".css") -> "text/css"
                                requestUrl.toString().contains(".png") -> "image/png"
                                requestUrl.toString().contains(".js") -> "application/x-javascript"
                                requestUrl.toString().contains(".woff") -> "application/x-font-woff"
                                else -> "text/html"
                            }

                            val fullPath = HKBundleManager.HYBIRD_DIR + tmpPath

                            HKLogUtil.w(HKHybirdManager.TAG, "shouldInterceptRequest: intercept fullPath: $fullPath")

                            resourceResponse = WebResourceResponse(mimeType, "UTF-8", FileInputStream(fullPath))
                        } catch (e: Exception) {
                            HKLogUtil.e(HKHybirdManager.TAG, "shouldInterceptRequest: intercept error: ", e)
                        }
                    }
                }
            }
            resourceResponse
        }
    }
}
