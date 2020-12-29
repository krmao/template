package com.smart.library.base

import android.content.res.Resources
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import com.gyf.barlibrary.ImmersionBar
import io.reactivex.disposables.CompositeDisposable

/**
 * 沉浸式布局
 *
 * 1: activity 包含 fragment (列如 STActivity) 手动设置 fragment 根布局背景色即可影响状态栏背景色
 *      > STBaseFragment 默认设置了根布局 fitsSystemWindows = true , 所以开发人员无需处理, 如果是自定义 Fragment 需要自己设置 fitsSystemWindows = true
 *
 * 2: 如果 activity 没有包含 fragment, 则需要手动设置 activity 根布局 fitsSystemWindows = true
 *
 * 总结: fragment/activity 的根布局 不可同时设置 fitsSystemWindows = true, 否则没有效果(即内容显示在状态栏的后面)
 * 注意: 以上方案 在 根布局不是 CoordinatorLayout 时 可行
 *
 * sdk >= 4.4 纯透明
 * sdk >= 4.1 < 4.4 则不起任何作用,不影响工程的使用
 */
@Suppress("MemberVisibilityCanBePrivate")
open class STBaseActivity : AppCompatActivity(), STActivityDelegate {

    protected open val delegate: STActivityDelegate by lazy { STBaseActivityDelegateImpl(this) }

    override fun disposables(): CompositeDisposable = delegate.disposables()
    override fun statusBar(): ImmersionBar? = delegate.statusBar()

    override fun onCreate(savedInstanceState: Bundle?) {
        onCreateBefore(savedInstanceState)
        super.onCreate(savedInstanceState)
        onCreateAfter(savedInstanceState)
    }

    override fun onCreateBefore(savedInstanceState: Bundle?) {
        delegate.onCreateBefore(savedInstanceState)
    }

    override fun onCreateAfter(savedInstanceState: Bundle?) {
        delegate.onCreateAfter(savedInstanceState)
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
        javaClass.canonicalName
    }

    override fun finish() {
        super.finish()
        finishAfter()
    }

    override fun finishAfter() {
        delegate.finishAfter()
    }

    override fun getResources(): Resources {
        return getResources(super.getResources())
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
    override fun enableFinishIfIsNotTaskRoot(): Boolean = delegate.enableFinishIfIsNotTaskRoot()
    override fun enableFinishIfIsNotTaskRoot(enable: Boolean) = delegate.enableFinishIfIsNotTaskRoot(enable)
    override fun enableActivityFullScreenAndExpandLayout(): Boolean = delegate.enableActivityFullScreenAndExpandLayout()
    override fun enableActivityFullScreenAndExpandLayout(enable: Boolean) = delegate.enableActivityFullScreenAndExpandLayout(enable)
    override fun enableActivityFeatureNoTitle(): Boolean = delegate.enableActivityFeatureNoTitle()
    override fun enableActivityFeatureNoTitle(enable: Boolean) = delegate.enableActivityFeatureNoTitle(enable)
    override fun activityDecorViewBackgroundResource(): Int = delegate.activityDecorViewBackgroundResource()
    override fun activityDecorViewBackgroundResource(drawableResource: Int) = delegate.activityDecorViewBackgroundResource(drawableResource)
    override fun activityCloseEnterAnimation(): Int = delegate.activityCloseEnterAnimation()
    override fun activityCloseEnterAnimation(animation: Int) = delegate.activityCloseEnterAnimation(animation)
    override fun activityCloseExitAnimation(): Int = delegate.activityCloseExitAnimation()
    override fun activityCloseExitAnimation(animation: Int) = delegate.activityCloseExitAnimation(animation)
    override fun activityTheme(): Int = delegate.activityTheme()
    override fun activityTheme(activityTheme: Int) = delegate.activityTheme(activityTheme)
    override fun adapterDesignWidth(designWidth: Int) = delegate.adapterDesignWidth(designWidth)
    override fun adapterDesignHeight(designHeight: Int) = delegate.adapterDesignHeight(designHeight)
    override fun enableAdapterDesign(enable: Boolean) = delegate.enableAdapterDesign(enable)

    override fun getResources(resources: Resources): Resources = delegate.getResources(resources)
    override fun quitApplication() = delegate.quitApplication()
}
