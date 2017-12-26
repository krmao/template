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
                entry.value.checkHealth { _, configuration ->
                    //                    var url = configuration?.moduleMainUrl?.get(HKHybirdManager.EVN)!!
//                    url += "index.shtml"
//                    HKWebFragment.goTo(activity, url)
//                    url += "#/cardList"
//                    HKWebFragment.goTo(activity, "http://www.chexiang.com")
//                    HKWebFragment.goTo(activity, "https://h.jia.chexiangpre.com/cx/cxj/cxjappweb/buyMealCard/index.shtml#/cardList")
//                    HKWebFragment.goTo(activity, "https://www.baidu.com")

                    HybirdWebFragment.goTo(activity, "file:///android_asset/index.html")
                    //HKWebFragment.goTo(activity, "https://h.jia.chexiangpre.com/cx/cxj/cxjappweb/buyMealCard/index.shtml#/cardList")
                }
            }

            containerLayout.addView(textView)
        }
    }
}
