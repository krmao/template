package com.smart.library.base

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import com.jude.swipbackhelper.SwipeBackHelper
import com.smart.library.util.HKRouteManager
import com.smart.library.util.HKToastUtil


open class HKBaseActivity : AppCompatActivity() {

    /**
     * 通过 HKRouteManager 跳转到 activity | fragment 可以添加此回调方法，方便跨组件传递参数(atlas)
     * 在 onDestroy 里会自动清除
     */
    val callback: ((bundle: Bundle?) -> Unit?)? by lazy { HKRouteManager.getCallback(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        SwipeBackHelper.onPostCreate(this)
    }

    override fun onDestroy() {
        HKRouteManager.removeCallback(this)
        SwipeBackHelper.onDestroy(this)
        super.onDestroy()
    }

    open fun onBackPress(): Boolean = false

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (onBackPress())
                return true

            window.decorView.clearAnimation()
            val fragments = supportFragmentManager.fragments
            if (fragments != null && fragments.size > 0) {
                val fragment = fragments[0]
                if (fragment is HKBaseFragment.OnBackPressedListener) {
                    val canPropagate = fragment.onBackPressed()
                    if (canPropagate) {
                        try {
                            supportFragmentManager.popBackStackImmediate()
                        } catch (ignored: Exception) {
                        }
                        return true
                    }
                }
            }
            if (supportFragmentManager.backStackEntryCount == 0) {
                if (enableExitWithDoubleBackPressed) {
                    exitApp()
                } else {
                    finish()
                }
            } else {
                try {
                    supportFragmentManager.popBackStackImmediate()
                } catch (ignored: Exception) {
                }
            }
            return true
        } else {
            return super.onKeyDown(keyCode, event)
        }
    }

    protected var enableExitWithDoubleBackPressed = false
    protected var enableSwipeBack = true
    private var exitTime: Long = 0

    private fun exitApp() = if (System.currentTimeMillis() - exitTime > 2000) {
        HKToastUtil.show("再按一次退出程序")
        exitTime = System.currentTimeMillis()
    } else {
        finish()
        System.exit(0)
    }
}
