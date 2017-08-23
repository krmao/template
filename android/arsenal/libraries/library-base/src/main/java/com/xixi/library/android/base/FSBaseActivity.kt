package com.xixi.library.android.base

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import com.jude.swipbackhelper.SwipeBackHelper

open class CXBaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SwipeBackHelper.onCreate(this)
        SwipeBackHelper.getCurrentPage(this)//get current instance
                .setSwipeBackEnable(true)//on-off
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
        super.onDestroy()
        SwipeBackHelper.onDestroy(this)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            window.decorView.clearAnimation()
            val fragments = supportFragmentManager.fragments
            if (fragments != null && fragments.size > 0) {
                val fragment = fragments[0]
                if (fragment is CXBaseFragment.OnBackPressedListener) {
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
                finish()
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
}
