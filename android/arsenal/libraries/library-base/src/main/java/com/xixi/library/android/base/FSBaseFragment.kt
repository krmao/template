package com.xixi.library.android.base

import android.os.Bundle
import android.support.v4.app.Fragment
import com.xixi.library.android.util.FSRouteManager

open class FSBaseFragment : Fragment() {

    interface OnBackPressedListener {
        /**
         *  @return true表示事件不再传播，false表示事件继续传播
         */
        fun onBackPressed(): Boolean
    }

    /**
     * 通过 FSRouteManager 跳转到 activity | fragment 可以添加此回调方法，方便跨组件传递参数(atlas)
     * 在 onDestroy 里会自动清除
     */
    val callback: ((bundle: Bundle?) -> Unit?)? by lazy { FSRouteManager.getCallback(this) }

    override fun onDestroy() {
        FSRouteManager.removeCallback(this)
        super.onDestroy()
    }
}
