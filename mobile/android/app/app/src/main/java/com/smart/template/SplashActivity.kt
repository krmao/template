package com.smart.template

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.smart.library.util.STEventManager
import com.smart.library.util.STLogUtil
import com.smart.library.util.STPreferencesUtil
import com.smart.template.home.tab.HomeTabActivity
import com.smart.template.library.STBridgeCommunication

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(null)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        if (Build.VERSION.SDK_INT >= 28) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            val layoutParams: WindowManager.LayoutParams = window.attributes
            layoutParams.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            window.attributes = layoutParams
        }

        if (!isTaskRoot) {
            val intent: Intent? = intent
            val action: String? = intent?.action
            if (intent?.hasCategory(Intent.CATEGORY_LAUNCHER) == true && action == Intent.ACTION_MAIN) {
                finish()
                return
            }
        }

        // setContentView(R.layout.home_splash)

        FinalApplicationInitManager.initialize { key: String, success: Boolean ->
            STLogUtil.w(
                "FinalApplicationInitManager",
                "initialize end isFinishing=$isFinishing, $key=$key, success=$success, thread=${Thread.currentThread().name}"
            )

            if (!isFinishing && key == "reactnative" && success) {
                //region schema
                val url: String? = intent.data?.toString()
                STLogUtil.w("schema", "url=$url")
                if (url?.startsWith("smart://template") == true) {
                    STBridgeCommunication.handleBridgeOpenSchema(this@SplashActivity, url)
                    finish()
                    return@initialize
                }
                //endregion
            }
        }

        // 程序运行黑屏或白屏的问题 https://www.jianshu.com/p/23f4bbb372c8

        // 监听 react native 首屏渲染事件, 此处可以关闭引导页
        val eventId: Any = this
        STEventManager.register(eventId, "react-native-inited") { eventKey: String, value: Any? ->
            STEventManager.unregisterAll(eventId)
            if ("react-native-inited" == eventKey) {
                if ("renderSuccess" == value) {
                    STPreferencesUtil.putBoolean("react-native-inited", true)
                    /*if (!isFinishing) {
                        STLogUtil.w("splash", "finish")
                        finish()
                    }*/
                }
            }
        }

        // test
        // startActivity(Intent(this, STLocalPicturesActivity::class.java))
        // startActivity(Intent(this, STPictureViewerExamplesActivity::class.java))
        startActivity(Intent(this, HomeTabActivity::class.java))
        finish()
    }
}
