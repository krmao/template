package com.xixi.fruitshop.android.module.hybird

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xixi.library.android.base.FSBaseFragment
import com.xixi.library.android.widget.webview.FSWebFragmentV2
import kotlinx.android.synthetic.main.hybird_fragment.*

class HybirdFragment : FSBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.hybird_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        text.setOnClickListener {
            FSWebFragmentV2.goTo(activity, "https://www.baidu.com")
        }
    }
}
