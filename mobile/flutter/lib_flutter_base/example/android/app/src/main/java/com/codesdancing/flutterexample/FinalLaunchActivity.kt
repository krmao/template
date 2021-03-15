package com.codesdancing.flutterexample

import android.annotation.SuppressLint
import android.os.Bundle
import com.codesdancing.flutter.STFlutterUtils
import com.codesdancing.flutter.multiple.STFlutterMultipleHomeActivity
import com.smart.library.STInitializer
import com.smart.library.base.STBaseLaunchActivity
import com.smart.library.util.STLogUtil
import com.smart.library.util.rx.RxBus
import io.reactivex.disposables.Disposable

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
                    goToFlutterHome()
                }
                //endregion
            }
        }
    }

    private var disposable: Disposable? = null

    @SuppressLint("CheckResult")
    private fun goToFlutterHome() {
        disposable = RxBus.toObservable(STFlutterMultipleHomeActivity.OnFlutterUiDisplayedEvent::class.java).subscribe {
            STLogUtil.d(TAG, "goToFlutterHome receive OnFlutterUiDisplayedEvent, ${Thread.currentThread().name}")
            if (!isFinishing) finish()
            STFlutterMultipleHomeActivity.needNotifyOnFlutterUiDisplayedEvent = false
            disposable?.dispose() // unsubscribe
        }
        STFlutterMultipleHomeActivity.needNotifyOnFlutterUiDisplayedEvent = true
        STFlutterUtils.openNewFlutterHomeActivityByName(this, "FlutterBridge")
    }
}
