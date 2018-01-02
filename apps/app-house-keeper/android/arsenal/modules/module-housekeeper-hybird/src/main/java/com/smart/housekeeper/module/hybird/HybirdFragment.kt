package com.smart.housekeeper.module.hybird

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.smart.library.base.HKBaseFragment
import com.smart.library.bundle.HKHybirdConfigModel
import com.smart.library.bundle.HKHybirdManager
import com.smart.library.bundle.HKHybirdModuleManager
import com.smart.library.util.HKRandomUtil
import com.smart.library.widget.webview.HKWebFragment
import kotlinx.android.synthetic.main.hybird_fragment.*

@Suppress("unused")
class HybirdFragment : HKBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater?.inflate(R.layout.hybird_fragment, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        HKHybirdManager.MODULES.value.forEach { entry ->
            val textView = TextView(context).apply {
                text = entry.key; textSize = 24f; gravity = Gravity.CENTER
                setTextColor(Color.BLACK)
                setBackgroundColor(HKRandomUtil.randomColor)
                setPadding(20, 20, 20, 20)
            }

            textView.setOnClickListener {

                entry.value.checkHealth(strategy = HKHybirdModuleManager.CheckStrategy.READY, synchronized = false) { _, config: HKHybirdConfigModel? ->
                    HybirdWebFragment.goTo(activity, config?.moduleMainUrl?.get(HKHybirdManager.EVN) + "index.shtml#/cardList")
                    //HybirdWebFragment.goTo(activity, "file:///android_asset/index.html")
                }
            }

            containerLayout.addView(textView)
        }
    }
}
