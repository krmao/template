package com.smart.template.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smart.library.base.STBaseFragment
import com.smart.library.util.STLogUtil
import com.smart.library.util.STRouteManager
import com.smart.template.R
import com.smart.template.home.tab.FinalHomeTabActivity
import kotlinx.android.synthetic.main.final_hybird_fragment.*

class FinalHybirdFragment : STBaseFragment() {

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.final_hybird_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hybirdPage.setOnClickListener {
            STRouteManager.goToActivity(activity, "com.smart.library.hybird.HybirdActivity") {
                STLogUtil.w("krmao", it.toString())
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.w(FinalHomeTabActivity.TAG, "HybirdFragment:onStart");
    }

    override fun onStop() {
        super.onStop()
        Log.w(FinalHomeTabActivity.TAG, "HybirdFragment:onStop");
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.w(FinalHomeTabActivity.TAG, "HybirdFragment:onDestroy");
    }

}
