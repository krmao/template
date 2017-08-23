package com.xixi.library.android.base

import android.support.v4.app.Fragment

open class CXBaseFragment : Fragment() {

    interface OnBackPressedListener {
        /**
         * @return true表示事件不再传播，false表示事件继续传播
         */
        fun onBackPressed(): Boolean
    }

}
