package com.smart.template

import com.smart.library.base.CXBaseApplication
import com.smart.template.module.rn.ReactManager


class FinalApplication : CXBaseApplication() {

    override fun onCreate() {
        super.onCreate()

        ReactManager.init(this, CXBaseApplication.DEBUG)
    }
}
