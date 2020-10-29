package com.smart.flutter.template

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.smart.library.flutter.FlutterRouter

import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity() {

    companion object {
        var sRef: WeakReference<MainActivity>? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sRef = WeakReference(this)

        setContentView(R.layout.main_activity)
        
        findViewById<View>(R.id.openMine).setOnClickListener {
            FlutterRouter.URL_MINE.goTo(this, hashMapOf(), 0,null)
        }

        findViewById<View>(R.id.openOrder).setOnClickListener {
            FlutterRouter.URL_ORDER.goTo(this, hashMapOf(), 0,null)
        }

        findViewById<View>(R.id.openSettings).setOnClickListener {
            FlutterRouter.URL_SETTINGS.goTo(this, hashMapOf(), 0,null)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sRef?.clear()
        sRef = null
    }

}
