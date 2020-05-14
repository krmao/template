package com.smart.library.flutter.boost.test

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import com.smart.library.flutter.boost.STFlutterInitializer
import com.smart.library.flutter.boost.STFlutterRouter
import com.smart.library.flutter.R

class MineActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.mine_activity)

        findViewById<View>(R.id.openHome).setOnClickListener {
            STFlutterInitializer.homeActivity?.let { homeActivity ->
                startActivity(Intent(this@MineActivity, homeActivity.javaClass))
            }
        }

        findViewById<View>(R.id.openOrder).setOnClickListener {
            STFlutterRouter.URL_ORDER.goTo(this, hashMapOf(), 0,null)
        }

        findViewById<View>(R.id.openSettings).setOnClickListener {
            STFlutterRouter.URL_SETTINGS.goTo(this, hashMapOf(), 0,null)
        }
    }

}
