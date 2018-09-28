package com.smart.template.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.smart.library.base.CXBaseFragment
import com.smart.library.util.CXToast
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
            CXToast.makeText("Hello World").show()
            CXToast.makeText("飞流直下三千尺").show()

            // CXToastUtil.show("Hello World")
            // Thread { CXToastUtil.show("飞流直下三千尺") }.start()

        }
        return textView
    }
}
