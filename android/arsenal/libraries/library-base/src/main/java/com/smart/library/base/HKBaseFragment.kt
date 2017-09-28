package com.smart.library.base

import android.os.Bundle
import android.support.v4.app.Fragment
import com.smart.library.util.HKRouteManager

open class HKBaseFragment : Fragment() {

    interface OnBackPressedListener {
        /**
         *  @return true表示事件不再传播，false表示事件继续传播
         */
        fun onBackPressed(): Boolean
    }

    /**
     * 通过 HKRouteManager 跳转到 activity | fragment 可以添加此回调方法，方便跨组件传递参数(atlas)
     * 在 onDestroy 里会自动清除
     */
    val callback: ((bundle: Bundle?) -> Unit?)? by lazy { HKRouteManager.getCallback(this) }

    override fun onDestroy() {
        HKRouteManager.removeCallback(this)
        super.onDestroy()
    }
}
