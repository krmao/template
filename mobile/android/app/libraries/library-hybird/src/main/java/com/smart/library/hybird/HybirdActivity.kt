package com.smart.library.hybird

import android.os.Bundle
import android.widget.FrameLayout
import com.smart.library.base.STBaseActivity
import com.smart.library.widget.webview.STWebFragment

@Suppress("unused")
class HybirdActivity : STBaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        enableSwipeBack = false
        enableExitWithDoubleBackPressed = true
        super.onCreate(savedInstanceState)
        setContentView(FrameLayout(this))

        Thread(Runnable {
            HybirdApplication.init {

                runOnUiThread {
                    val fragment = STWebFragment()
                    val arguments = Bundle()
//                    arguments.putString("url", "http://10.32.33.16:5388/#/")
//                    arguments.putString("url", "http://192.168.1.5:5388/#/")
                    arguments.putString("url", "http://192.168.1.5:5388/cx/cxj/cxjappweb/template/index.html")
                    arguments.putBoolean("hideTitleBar", true)
                    arguments.putBoolean("fullScreenAndBehindStatusBar", true)

                    fragment.arguments = arguments

                    supportFragmentManager.beginTransaction().add(android.R.id.content, fragment, STWebFragment::javaClass.name).commitAllowingStateLoss()
                }
            }
        }).start()
    }
}
