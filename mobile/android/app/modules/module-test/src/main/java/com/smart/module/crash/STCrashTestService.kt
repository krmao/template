package com.smart.module.crash

import android.app.Service
import android.content.Intent
import android.os.IBinder
import xcrash.XCrash

class STCrashTestService : Service() {
    override fun onCreate() {}
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val type = intent.getStringExtra("type")
        if (type != null) {
            if (type == "native") {
                XCrash.testNativeCrash(false)
            } else if (type == "java") {
                XCrash.testJavaCrash(false)
            }
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}