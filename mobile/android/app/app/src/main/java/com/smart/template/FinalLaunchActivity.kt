package com.smart.template

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.smart.library.R
import com.smart.library.STInitializer
import com.smart.library.base.STBaseLaunchActivity
import com.smart.library.flutter.STFlutterRouter
import com.smart.library.util.STLogUtil
import com.smart.library.util.STSystemUtil
import com.smart.library.util.STToastUtil
import com.smart.template.home.tab.FinalHomeTabActivity

class FinalLaunchActivity : STBaseLaunchActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)

        // window?.decorView?.post { STInitializer.initialApplication() }

        STInitializer.state.ensureBusInitialized {
            STLogUtil.w(TAG, "ensureBusInitialized isFinishing=$isFinishing, thread=${Thread.currentThread().name}")

            if (!isFinishing) {
                //region schema
                val url: String? = intent.data?.toString()
                STLogUtil.w(TAG, "schema url=$url")
                if (url?.startsWith("smart://template") == true) {
                    STInitializer.openSchema(this@FinalLaunchActivity, url)
                    finish()
                    return@ensureBusInitialized
                } else {
                    // goToFlutterHome()
                    goToHome()
                }
                //endregion
            }
        }

        STInitializer.state.ensureRNFirstScreenAttached { attached: Boolean ->
            STLogUtil.w(TAG, "ensureRNFirstScreenAttached attached=$attached")
            /*if (!isFinishing) {
                STLogUtil.w("splash", "finish")
                finish()
            }*/
        }

        STToastUtil.show(STSystemUtil.getScreenInfo())
    }

    private fun goToFlutterHome() {
        STFlutterRouter.openHomeByName(this, "flutter_bridge")
        Handler().postDelayed({ finish() }, 1000)
    }

    private fun goToHome() {
        startActivity(Intent(this, FinalHomeTabActivity::class.java))
        finish()
    }
}
