package com.smart.template.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smart.library.base.STBaseFragment
import com.smart.library.reactnative.ReactJumper
import com.smart.template.R
import kotlinx.android.synthetic.main.home_react_native_fragment.*

class ReactNativeFragment : STBaseFragment() {

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_react_native_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        home.setOnClickListener {
            ReactJumper.goTo(context, pageName = "home", component = "cc-rn")
        }
        bridge.setOnClickListener {
            ReactJumper.goTo(context, pageName = "bridge", component = "cc-rn")
        }
    }

    override fun onStart() {
        super.onStart()
        Log.w("krmao", "ReactNativeFragment:onStart");
    }

    override fun onStop() {
        super.onStop()
        Log.w("krmao", "ReactNativeFragment:onStop");
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.w("krmao", "ReactNativeFragment:onDestroy");
    }
}
