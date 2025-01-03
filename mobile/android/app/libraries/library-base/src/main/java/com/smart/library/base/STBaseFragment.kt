package com.smart.library.base

import android.os.Bundle
import android.view.View
import androidx.annotation.Keep
import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable

@Suppress("unused")
//@Keep
open class STBaseFragment : Fragment() {

    private val disposables: CompositeDisposable by lazy { CompositeDisposable() }

    fun disposables(): CompositeDisposable = this.disposables

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.fitsSystemWindows = true
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables().dispose()
    }

    //@Keep
    interface OnBackPressedListener {
        /**
         *  @return true表示事件不再传播，false表示事件继续传播
         */
        fun onBackPressed(): Boolean
    }
}
