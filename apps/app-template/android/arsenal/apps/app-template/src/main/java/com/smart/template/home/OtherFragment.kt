package com.smart.template.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.smart.library.base.CXBaseApplication
import com.smart.library.base.CXBaseFragment
import com.smart.library.util.CXToast
import com.smart.library.util.CXToastUtil
import com.smart.template.R

class OtherFragment : CXBaseFragment() {

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val textView: TextView = TextView(activity)
        textView.text = "other"
        @Suppress("DEPRECATION")
        textView.setTextColor(resources.getColor(R.color.orange))
        textView.setBackgroundColor(Color.DKGRAY)
        textView.setOnClickListener {
            CXToastUtil.show("custom toast main")
            Thread { CXToastUtil.show("custom toast other") }.start()
        }
        return textView
    }
}
