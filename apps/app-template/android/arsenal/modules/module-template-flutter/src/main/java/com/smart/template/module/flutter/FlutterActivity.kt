package com.smart.template.module.flutter

import android.os.Bundle
import android.widget.FrameLayout
import com.smart.library.base.CXBaseActivity

class FlutterActivity : CXBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableSwipeBack = false
        setContentView(FrameLayout(this))

        val fragment: CXFlutterFragment = CXFlutterFragment.createFragment("route1")
        val fragmentClassName: String = fragment.javaClass.name
        supportFragmentManager.beginTransaction().replace(android.R.id.content, fragment, fragmentClassName).commitAllowingStateLoss()
    }

}
