package com.smart.template

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.smart.library.STInitializer
import com.smart.library.util.STLogUtil
import com.smart.template.home.tab.FinalHomeTabActivity

class FinalSplashActivity : AppCompatActivity() {

    companion object {
        const val TAG: String = "SplashActivity"
    }

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

        // setContentView(R.layout.final_splash)

        STInitializer.ensureBusInitialized {
            STLogUtil.w(TAG, "ensureBusInitialized isFinishing=$isFinishing, thread=${Thread.currentThread().name}")

            if (!isFinishing) {
                //region schema
                val url: String? = intent.data?.toString()
                STLogUtil.w(TAG, "schema url=$url")
                if (url?.startsWith("smart://template") == true) {
                    STInitializer.openSchema(this@FinalSplashActivity, url)
                    finish()
                    return@ensureBusInitialized
                }
                //endregion
            }
        }

        STInitializer.ensureRNFirstScreenAttached { attached: Boolean ->
            STLogUtil.w(TAG, "ensureRNFirstScreenAttached attached=$attached")
            /*if (!isFinishing) {
                STLogUtil.w("splash", "finish")
                finish()
            }*/
        }

        startActivity(Intent(this, FinalHomeTabActivity::class.java))
        finish()
    }
}
