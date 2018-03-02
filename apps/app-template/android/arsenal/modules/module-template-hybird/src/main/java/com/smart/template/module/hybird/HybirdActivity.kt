package com.smart.template.module.hybird

import android.os.Bundle
import android.widget.FrameLayout
import com.smart.library.base.CXBaseActivity
import com.smart.library.widget.webview.CXWebFragment

@Suppress("unused")
class HybirdActivity : CXBaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        enableSwipeBack = false
        enableExitWithDoubleBackPressed = true
        super.onCreate(savedInstanceState)
        setContentView(FrameLayout(this))

        Thread(Runnable {
            HybirdApplication.init {

                runOnUiThread {
                    val fragment = CXWebFragment()
                    val arguments = Bundle()
                    arguments.putString("url", "http://wx.hfgjia.cn/wx/index.html")
                    arguments.putBoolean("hideTitleBar", true)
                    arguments.putBoolean("fullScreenAndBehindStatusBar", true)

                    fragment.arguments = arguments

                    supportFragmentManager.beginTransaction().add(android.R.id.content, fragment, CXWebFragment::javaClass.name).commitAllowingStateLoss()
                }
            }
        }).start()
    }
}
