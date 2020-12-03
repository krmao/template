package com.smart.library.base

import android.os.Bundle
import android.view.KeyEvent
import com.gyf.barlibrary.ImmersionBar
import io.reactivex.disposables.CompositeDisposable

interface STBaseActivityDelegate {

    /**
     * 通过 STRouteManager 跳转到 activity | fragment 可以添加此回调方法，方便跨组件传递参数(atlas)
     * 在 onDestroy 里会自动清除
     */
    fun callback(): ((bundle: Bundle?) -> Unit?)?

    fun disposables(): CompositeDisposable

    fun statusBar(): ImmersionBar?

    fun onCreate(savedInstanceState: Bundle?)

    fun onPostCreate(savedInstanceState: Bundle?)

    fun onResume()

    fun onDestroy()

    fun onBackPressedIntercept(): Boolean = false

    fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean

    fun enableSwipeBack(): Boolean
    fun enableSwipeBack(enable:Boolean)
    fun enableImmersionStatusBar(): Boolean
    fun enableImmersionStatusBar(enable:Boolean)
    fun enableImmersionStatusBarWithDarkFont(): Boolean
    fun enableImmersionStatusBarWithDarkFont(enable:Boolean)
    fun enableExitWithDoubleBackPressed(): Boolean
    fun enableExitWithDoubleBackPressed(enable:Boolean)
    fun quitApplication()
}
