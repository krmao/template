package com.smart.module.test

import com.smart.library.base.mvp.CXBasePresenter
import com.smart.library.base.mvp.CXBaseView

interface TestContract {

    interface View : CXBaseView<Presenter> {
        fun showData(cityList: List<Any>)
        fun showException(message: String)
    }

    interface Presenter : CXBasePresenter {
        fun loadData(forceUpdate: Boolean)
    }
}
