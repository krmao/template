package com.smart.library.widget.debug

import android.app.Activity
import android.view.Gravity
import android.widget.Toast
import com.smart.library.base.CXBaseApplication
import com.smart.library.base.CXConfig
import com.smart.library.util.CXToastUtil
import com.smart.library.util.CXViewUtil

object CXDebugManager {

    /**
     * 显示调试信息
     * 显示当前页面是否包含 webView, 是否包含 react view
     * 只有调试包起作用
     */
    @JvmStatic
    fun showActivityInfo(activity: Activity?) {
        if (CXBaseApplication.DEBUG && CXConfig.ENABLE_ACTIVITY_INFO_DEBUG) {
            CXToastUtil.show("${activity?.javaClass?.simpleName}\n\n" +
                    "包含 webview    :  ${if (CXViewUtil.isHaveWebView(activity)) "是" else "否"}\n" +
                    "包含 react view :  ${if (CXViewUtil.isHaveReactView(activity)) "是" else "否"}",
                    Gravity.TOP, Toast.LENGTH_LONG)
        }
    }

}