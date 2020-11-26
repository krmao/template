package com.smart.library.base

import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import com.gyf.barlibrary.ImmersionBar
import com.jude.swipbackhelper.SwipeBackHelper
import com.smart.library.STInitializer
import com.smart.library.util.STRouteManager
import com.smart.library.util.STToastUtil
import com.smart.library.widget.debug.STDebugFragment
import com.smart.library.widget.debug.STDebugManager
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
open class STBaseActivity : AppCompatActivity() {

    /**
     * 通过 STRouteManager 跳转到 activity | fragment 可以添加此回调方法，方便跨组件传递参数(atlas)
     * 在 onDestroy 里会自动清除
     */
    val callback: ((bundle: Bundle?) -> Unit?)? by lazy { STRouteManager.getCallback(this) }

    open val disposables: CompositeDisposable = CompositeDisposable()

    open var statusBar: ImmersionBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (enableImmersionStatusBar) {
            //navigationBarEnable=true 华为荣耀6 4.4.2 手机会出现导航栏错乱问题
            statusBar = ImmersionBar.with(this)
                .transparentStatusBar()
                .statusBarColorInt(Color.TRANSPARENT)
                .navigationBarEnable(false)
                .statusBarDarkFont(enableImmersionStatusBarWithDarkFont, if (enableImmersionStatusBarWithDarkFont) 0.2f else 0f)
            statusBar?.init()
        }

        if (enableSwipeBack) {
            SwipeBackHelper.onCreate(this)
            SwipeBackHelper.getCurrentPage(this)//get current instance
                .setSwipeBackEnable(enableSwipeBack)//on-off
                //.setSwipeEdge(200)//set the touch area。200 mean only the left 200px of screen can touch to begin swipe.
                .setSwipeEdgePercent(0.1f)//0.2 mean left 20% of screen can touch to begin swipe.
                .setSwipeSensitivity(0.7f)//sensitiveness of the gesture。0:slow  1:sensitive
                .setScrimColor(Color.parseColor("#EE000000"))//color of Scrim below the activity
                .setClosePercent(0.7f)//close activity when swipe over this
                .setSwipeRelateEnable(true)//if should move together with the following Activity
                .setSwipeRelateOffset(500)//the Offset of following Activity when setSwipeRelateEnable(true)
                .setDisallowInterceptTouchEvent(false)//your view can hand the events first.default false;
            /*.addListener(object : SwipeListener {
            override fun onScrollToClose() {
            }

            override fun onEdgeTouch() {
            }

            override fun onScroll(p0: Float, p1: Int) {
            }
        })*/

        }

    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        if (enableSwipeBack) SwipeBackHelper.onPostCreate(this)
    }

    override fun onResume() {
        super.onResume()
        STDebugManager.showActivityInfo(this)
    }

    override fun onDestroy() {
        disposables.dispose()
        statusBar?.destroy()
        if (enableSwipeBack) SwipeBackHelper.onDestroy(this)
        STRouteManager.removeCallback(this)
        super.onDestroy()
    }

    open fun onBackPressedIntercept(): Boolean = false

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (onBackPressedIntercept()) return true

            window.decorView.clearAnimation()
            supportFragmentManager.fragments.firstOrNull()?.let {
                if (it is STBaseFragment.OnBackPressedListener) {
                    if (it.onBackPressed()) {
                        supportFragmentManager.popBackStackImmediate()
                        return true
                    }
                }
            }

            if (supportFragmentManager.backStackEntryCount == 0) {
                if (enableExitWithDoubleBackPressed) exitApp() else finish()
            } else supportFragmentManager.popBackStackImmediate()
            return true
        } else {
            return super.onKeyDown(keyCode, event)
        }
    }

    protected var exitTime: Long = 0
    protected var enableSwipeBack = true
    protected var enableImmersionStatusBar = true
    protected var enableImmersionStatusBarWithDarkFont = false
    protected var enableExitWithDoubleBackPressed = false

    protected fun exitApp() = if (System.currentTimeMillis() - exitTime > 2000) {
        STToastUtil.show("再按一次退出程序")
        exitTime = System.currentTimeMillis()
    } else {
        if (STInitializer.debug()) STDebugFragment.cancelDebugNotification()
        STInitializer.quitApplication()
    }
}
