package com.smart.library.flutter.test

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.smart.flutter.template.MainActivity
import com.smart.flutter.template.R
import com.smart.library.flutter.FlutterRouter

class MineActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.mine_activity)

        findViewById<View>(R.id.openHome).setOnClickListener {
        }

        findViewById<View>(R.id.openOrder).setOnClickListener {
            FlutterRouter.URL_ORDER.goTo(this, hashMapOf(), 0)
        }

        findViewById<View>(R.id.openSettings).setOnClickListener {
            FlutterRouter.URL_SETTINGS.goTo(this, hashMapOf(), 0)
        }
    }

}
