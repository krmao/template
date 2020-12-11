package com.smart.library.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable

open class STBaseFragment : Fragment() {

    private val disposables: CompositeDisposable by lazy { CompositeDisposable() }

    fun disposables(): CompositeDisposable = this.disposables

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.fitsSystemWindows = true
        super.onViewCreated(view, savedInstanceState)
    }

    interface OnBackPressedListener {
        /**
         *  @return true表示事件不再传播，false表示事件继续传播
         */
        fun onBackPressed(): Boolean
    }

}
