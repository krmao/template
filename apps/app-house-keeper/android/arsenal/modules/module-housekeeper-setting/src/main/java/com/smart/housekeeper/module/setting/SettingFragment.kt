package com.smart.housekeeper.module.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smart.library.base.HKBaseFragment

class SettingFragment : HKBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val bundle = arguments ?: Bundle()
        bundle.putString("name", "michael mao")
        callback?.invoke(bundle)
        callback?.invoke(bundle)
        return inflater.inflate(R.layout.setting_fragment, container, false)
    }
}
