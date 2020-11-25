package com.smart.template.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smart.library.base.STBaseFragment
import com.smart.library.util.bus.STBusManager
import com.smart.template.R
import kotlinx.android.synthetic.main.final_flutter_fragment.*

class FinalFlutterFragment : STBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.final_flutter_fragment, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        flutterPageBridge.setOnClickListener {
            STBusManager.call(context, "flutter/open", "smart://template/flutter?page=bridge&params=")
        }
        flutterPageDemo.setOnClickListener {
            STBusManager.call(context, "flutter/demo")
        }
        flutterPageOrder.setOnClickListener {
            STBusManager.call(context, "flutter/order")
        }
        flutterPageNotFound.setOnClickListener {
            STBusManager.call(context, "flutter/not_found")
        }
    }

    override fun onStart() {
        super.onStart()
        Log.w("krmao", "FlutterFragment:onStart");
    }

    override fun onStop() {
        super.onStop()
        Log.w("krmao", "FlutterFragment:onStop");
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.w("krmao", "FlutterFragment:onDestroy");
    }
}
