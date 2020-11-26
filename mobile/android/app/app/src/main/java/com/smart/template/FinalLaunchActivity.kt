package com.smart.template

import android.content.Intent
import android.os.Bundle
import com.smart.library.STInitializer
import com.smart.library.base.STBaseLaunchActivity
import com.smart.library.util.STLogUtil
import com.smart.template.home.tab.FinalHomeTabActivity

class FinalLaunchActivity : STBaseLaunchActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)

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
    }

    private fun goToHome() {
        startActivity(Intent(this, FinalHomeTabActivity::class.java))
        finish()
    }
}
