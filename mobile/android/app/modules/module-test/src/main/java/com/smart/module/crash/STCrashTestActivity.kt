package com.smart.module.crash

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import xcrash.XCrash

class STCrashTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(LinearLayout(this))
        val intent = intent
        val type = intent.getStringExtra("type")
        if (type != null) {
            if (type == "native") {
                XCrash.testNativeCrash(false)
            } else if (type == "java") {
                XCrash.testJavaCrash(false)
            }
        }
    }
}