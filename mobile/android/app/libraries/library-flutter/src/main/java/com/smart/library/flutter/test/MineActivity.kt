package com.smart.library.flutter.test

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.smart.library.flutter.FlutterInitializer
import com.smart.library.flutter.FlutterRouter
import com.smart.library.flutter.R

class MineActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.mine_activity)

        findViewById<View>(R.id.openHome).setOnClickListener {
            FlutterInitializer.homeActivity?.let { homeActivity ->
                startActivity(Intent(this@MineActivity, homeActivity.javaClass))
            }
        }

        findViewById<View>(R.id.openOrder).setOnClickListener {
            FlutterRouter.URL_ORDER.goTo(this, hashMapOf(), 0,null)
        }

        findViewById<View>(R.id.openSettings).setOnClickListener {
            FlutterRouter.URL_SETTINGS.goTo(this, hashMapOf(), 0,null)
        }
    }

}
