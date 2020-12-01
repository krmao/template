package com.smart.library.flutter.test

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.smart.library.flutter.FlutterBusHandler
import com.smart.library.flutter.R
import com.smart.library.flutter.STFlutterRouter

class MineActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.mine_activity)

        findViewById<View>(R.id.openOrder).setOnClickListener {
            STFlutterRouter.URL_ORDER.goTo(this, hashMapOf(), 0, null)
        }

        findViewById<View>(R.id.openSettings).setOnClickListener {
            STFlutterRouter.URL_SETTINGS.goTo(this, hashMapOf(), 0, null)
        }
    }

}
