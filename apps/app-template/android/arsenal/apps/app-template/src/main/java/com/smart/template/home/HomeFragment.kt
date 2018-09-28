package com.smart.template.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smart.library.base.CXActivity
import com.smart.library.base.CXBaseFragment
import com.smart.library.util.CXToastUtil
import com.smart.template.R
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : CXBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.home_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        text1.setOnClickListener {
            CXActivity.start(activity, Test1Fragment::class.java)
        }
        text2.setOnClickListener {
            CXActivity.start(activity, Test2Fragment::class.java)
        }
        text3.setOnClickListener {
            CXToastUtil.show("Hello World")
            CXToastUtil.show("system toast")
        }
    }
}
