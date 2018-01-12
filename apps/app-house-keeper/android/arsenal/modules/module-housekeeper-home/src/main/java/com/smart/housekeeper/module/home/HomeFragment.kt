package com.smart.housekeeper.module.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smart.library.base.HKActivity
import com.smart.library.base.HKBaseFragment
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : HKBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.home_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        text1.setOnClickListener {
            HKActivity.start(activity, Test1Fragment::class.java)
        }
        text2.setOnClickListener {
            HKActivity.start(activity, Test2Fragment::class.java)
        }
    }
}
