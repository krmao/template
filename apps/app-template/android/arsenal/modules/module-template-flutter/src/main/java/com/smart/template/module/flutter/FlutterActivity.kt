package com.smart.template.module.flutter

import android.os.Bundle
import android.support.v4.app.Fragment
import android.widget.FrameLayout
import com.smart.library.base.CXBaseActivity
import io.flutter.facade.Flutter

class FlutterActivity : CXBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(FrameLayout(this))

        val fragment: Fragment = Flutter.createFragment("route1")
        val fragmentClassName: String = fragment.javaClass.name
        supportFragmentManager.beginTransaction().replace(android.R.id.content, fragment, fragmentClassName).commitAllowingStateLoss()
    }

}
