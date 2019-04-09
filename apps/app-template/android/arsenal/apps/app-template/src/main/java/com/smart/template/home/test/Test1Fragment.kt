package com.smart.template.home.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smart.library.base.CXBaseFragment
import com.smart.template.R

class Test1Fragment : CXBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.home_fragment_test_1, container, false)
}
