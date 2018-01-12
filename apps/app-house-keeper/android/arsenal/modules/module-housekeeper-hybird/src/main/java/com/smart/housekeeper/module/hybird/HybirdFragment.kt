package com.smart.housekeeper.module.hybird

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.smart.library.base.HKBaseFragment
import com.smart.library.bundle.model.HKHybirdModuleConfigModel
import com.smart.library.bundle.HKHybird
import com.smart.library.bundle.strategy.HKHybirdCheckStrategy
import com.smart.library.util.HKRandomUtil
import com.smart.library.widget.webview.HKWebFragment
import kotlinx.android.synthetic.main.hybird_fragment.*

@Suppress("unused")
class HybirdFragment : HKBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.hybird_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        HKHybird.MODULES.forEach { entry ->
            val textView = TextView(context).apply {
                text = entry.key; textSize = 24f; gravity = Gravity.CENTER
                setTextColor(Color.BLACK)
                setBackgroundColor(HKRandomUtil.randomColor)
                setPadding(20, 20, 20, 20)
            }

            textView.setOnClickListener {
                entry.value.checkHealth(strategy = HKHybirdCheckStrategy.READY, synchronized = false) { _, _: HKHybirdModuleConfigModel? ->
                    //HybirdWebFragment.goTo(activity, "https://h.jia.chexiangpre.com" + config?.moduleMainUrl + "index.shtml#/cardList")
                    HKWebFragment.goTo(activity, "https://cxjapp.chexiang.com/service/dispacher/0?cityCode=310100&id=CXJ_445&longitude=121.433353&latitude=31.204572&storeId=&userToken=MTAwMDVhNGY0MmUzNWFjNWU5ZTM4NjY0MDMxAu7noHxv7lSlaLt_rGS8oedQqJo&cityName=上海市")
                }
            }

            containerLayout.addView(textView)
        }

        title_bar.right0BgView.setOnClickListener {
            HKWebFragment.goTo(activity, "file:///android_asset/index.html")
        }
    }
}
