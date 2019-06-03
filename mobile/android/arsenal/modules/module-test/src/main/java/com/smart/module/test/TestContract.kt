package com.smart.module.test

import com.smart.library.base.mvp.STBasePresenter
import com.smart.library.base.mvp.STBaseView

interface TestContract {

    interface View : STBaseView<Presenter> {
        fun showData(cityList: List<Any>)
        fun showException(message: String)
    }

    interface Presenter : STBasePresenter {
        fun loadData(forceUpdate: Boolean)
    }
}
