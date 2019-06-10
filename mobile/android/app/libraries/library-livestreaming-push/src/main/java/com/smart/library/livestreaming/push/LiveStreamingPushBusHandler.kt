package com.smart.library.livestreaming.push

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Context
import com.smart.library.util.STLogUtil
import com.smart.library.util.STToastUtil
import com.smart.library.util.bus.STBusManager
import com.smart.library.util.rx.permission.RxPermissions

@Suppress("unused", "PrivatePropertyName")
class LiveStreamingPushBusHandler : STBusManager.IBusHandler {

    override fun onInitOnce(application: Application) {
    }

    override fun onUpgradeOnce(application: Application) {

    }

    override fun onCall(context: Context?, busFunctionName: String, vararg params: Any) {
        when (busFunctionName) {
            "livestreamingpush/push" -> {
                (context as? Activity)?.apply {
                    RxPermissions(this).requestEachCombined(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO).subscribe {
                        STLogUtil.e(RxPermissions.TAG, "request permissions callback -> $it")
                        if (it.granted) {
                            val url = params.firstOrNull() as? String
                            STVideoPushActivity.intentTo(context, url)
                        } else {
                            STToastUtil.show("相机需要权限")
                        }
                    }
                }
            }
        }

    }
}
