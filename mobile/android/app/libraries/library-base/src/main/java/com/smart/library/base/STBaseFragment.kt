package com.smart.library.base

import android.os.Bundle
import  androidx.fragment.app.Fragment
import android.view.View
import com.smart.library.util.STRouteManager

open class STBaseFragment : Fragment() {

    interface OnBackPressedListener {
        /**
         *  @return true表示事件不再传播，false表示事件继续传播
         */
        fun onBackPressed(): Boolean
    }

    /**
     * 通过 STRouteManager 跳转到 activity | fragment 可以添加此回调方法，方便跨组件传递参数(atlas)
     * 在 onDestroy 里会自动清除
     */
    val callback: ((bundle: Bundle?) -> Unit?)? by lazy { STRouteManager.getCallback(this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.fitsSystemWindows = true
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        STRouteManager.removeCallback(this)
        super.onDestroy()
    }
}
