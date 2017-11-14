package com.smart.housekeeper.module.hybird

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.smart.library.base.HKBaseFragment
import com.smart.library.bundle.HKHybirdManager
import com.smart.library.util.HKRandomUtil
import kotlinx.android.synthetic.main.hybird_fragment.*

@Suppress("unused")
class HybirdFragment : HKBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater?.inflate(R.layout.hybird_fragment, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        HKHybirdManager.Module.values().forEach {
            val textView = TextView(context)
            textView.text = it.name
            textView.setTextColor(Color.BLACK)
            textView.setBackgroundColor(HKRandomUtil.randomColor)
            textView.textSize = 24f
            textView.gravity = Gravity.CENTER
            textView.setPadding(20, 20, 20, 20)

            textView.setOnClickListener {
                HKHybirdManager.Module.valueOf(textView.text.toString()).manager.verify { localUnzipDir, configuration ->
                    HybirdWebFragment.goTo(activity, configuration?.moduleMainUrl?.get(HKHybirdManager.EVN))
                }
            }

            containerLayout.addView(textView)
        }
    }
}
