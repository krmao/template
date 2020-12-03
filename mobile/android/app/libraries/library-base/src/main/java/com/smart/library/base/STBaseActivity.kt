package com.smart.library.base

import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import com.gyf.barlibrary.ImmersionBar
import io.reactivex.disposables.CompositeDisposable

/**
 * 默认沉浸式布局
 *
 * 1: activity/fragment 手动设置根布局背景色即可影响状态栏背景色
 *    即-> 需要设置 activity/fragment 根布局 去影响状态栏背景色
 *
 * 2: fragment 默认设置了根布局 fitsSystemWindows = true , 所以开发人员无需处理
 *
 * 3: 如果activity 没有包含fragment, 则需要手动设置 根布局 fitsSystemWindows = true
 *    即-> 没有包含fragment 的 activity 无需做任何处理需要设置根布局 fitsSystemWindows = true
 *
 * 4: 如果activity    包含fragment, 则不需要设置 根布局 fitsSystemWindows = true, 因为步骤2 fragment 以及默认设置了
 *    即-> 包含fragment 的 activity 无需做任何处理
 *
 * 5: 总结: fragment/activity 的根布局 不可同时设置 fitsSystemWindows = true , 否则没有效果(即内容显示在状态栏的后面)
 *    注意: 以上方案 在 根布局不是 CoordinatorLayout 时 可行
 *
 * sdk >= 4.4 纯透明
 * sdk >= 4.1 < 4.4 则不起任何作用,不影响工程的使用
 */
@Suppress("MemberVisibilityCanBePrivate")
open class STBaseActivity : AppCompatActivity(), STBaseActivityDelegate {

    protected open val delegate: STBaseActivityDelegate by lazy { STBaseActivityDelegateImpl(this) }

    override fun callback(): ((bundle: Bundle?) -> Unit?)? = delegate.callback()
    override fun disposables(): CompositeDisposable = delegate.disposables()
    override fun statusBar(): ImmersionBar? = delegate.statusBar()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        delegate.onCreate(savedInstanceState)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        delegate.onPostCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        delegate.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        delegate.onDestroy()
    }

    override fun onBackPressedIntercept(): Boolean = delegate.onBackPressedIntercept()

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (delegate.onKeyDown(keyCode, event)) {
            true
        } else {
            super.onKeyDown(keyCode, event)
        }
    }

    override fun enableSwipeBack(): Boolean = delegate.enableSwipeBack()
    override fun enableSwipeBack(enable: Boolean) = delegate.enableSwipeBack(enable)
    override fun enableImmersionStatusBar(): Boolean = delegate.enableImmersionStatusBar()
    override fun enableImmersionStatusBar(enable: Boolean) = delegate.enableImmersionStatusBar(enable)
    override fun enableImmersionStatusBarWithDarkFont(): Boolean = delegate.enableImmersionStatusBarWithDarkFont()
    override fun enableImmersionStatusBarWithDarkFont(enable: Boolean) = delegate.enableImmersionStatusBarWithDarkFont(enable)
    override fun enableExitWithDoubleBackPressed(): Boolean = delegate.enableExitWithDoubleBackPressed()
    override fun enableExitWithDoubleBackPressed(enable: Boolean) = delegate.enableExitWithDoubleBackPressed(enable)
    override fun quitApplication() = delegate.quitApplication()
}
