package com.smart.template.module.hybird

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.smart.library.base.CXBaseFragment
import com.smart.library.bundle.CXHybird
import com.smart.library.util.CXRandomUtil
import com.smart.library.widget.webview.CXWebFragment
import kotlinx.android.synthetic.main.hybird_fragment.*

@Suppress("unused")
class HybirdFragment : CXBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.hybird_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CXHybird.modules.forEach { entry ->
            val textView = TextView(context).apply {
                text = entry.key; textSize = 24f; gravity = Gravity.CENTER
                setTextColor(Color.BLACK)
                setBackgroundColor(CXRandomUtil.randomColor)
                setPadding(20, 20, 20, 20)
            }

            textView.setOnClickListener {
                if (entry.key == "buyMealCard") {
                    CXWebFragment.goTo(activity, "https://h.jia.chexiangpre.com" + entry.value.currentConfig.moduleMainUrl + "index.shtml#/cardList")
                } else if (entry.key == "welfare") {
                    CXWebFragment.goTo(activity, "https://cxjapp.chexiang.com/service/dispacher/0?cityCode=310100&id=CXJ_445&longitude=121.433353&latitude=31.204572&storeId=&userToken=MTAwMDVhNGY0MmUzNWFjNWU5ZTM4NjY0MDMxAu7noHxv7lSlaLt_rGS8oedQqJo&cityName=上海市")
                }
            }

            containerLayout.addView(textView)
        }

        title_bar.right0BgView.setOnClickListener {
            CXWebFragment.goTo(activity, "file:///android_asset/index.html")
        }
    }
}