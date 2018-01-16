package com.smart.template.module.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smart.library.base.CXBaseFragment

class SettingFragment : CXBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val bundle = arguments ?: Bundle()
        bundle.putString("name", "michael mao")
        callback?.invoke(bundle)
        callback?.invoke(bundle)
        return inflater.inflate(R.layout.setting_fragment, container, false)
    }
}
