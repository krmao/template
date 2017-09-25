package com.xixi.fruitshop.android.module.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xixi.library.android.base.FSBaseFragment

class SettingFragment : FSBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val bundle = arguments ?: Bundle()
        bundle.putString("name", "michael mao")
        callback?.invoke(bundle)
        callback?.invoke(bundle)
        return inflater?.inflate(R.layout.setting_fragment, container, false)
    }
}
