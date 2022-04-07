package com.smart.library.widget.debug

import android.app.Activity
import android.view.Gravity
import android.widget.Toast
import androidx.annotation.Keep
import com.smart.library.STInitializer
import com.smart.library.util.STToastUtil
import com.smart.library.util.STViewUtil

//@Keep
object STDebugManager {

    /**
     * 显示调试信息
     * 显示当前页面是否包含 webView, 是否包含 react view
     * 只有调试包起作用
     */
    @JvmStatic
    fun showActivityInfo(activity: Activity?) {
        if (STInitializer.debug() && STDebugFragment.ENABLE_ACTIVITY_INFO_DEBUG) {
            STToastUtil.show("${activity?.javaClass?.simpleName}\n\n" +
                    "包含 webview    :  ${if (STViewUtil.isHaveWebView(activity)) "是" else "否"}\n" +
                    "包含 react view :  ${if (STViewUtil.isHaveReactView(activity)) "是" else "否"}",
                    Gravity.TOP, Toast.LENGTH_LONG)
        }
    }

}