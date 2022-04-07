package com.smart.module.test

import com.smart.library.base.STMvpContract

interface TestContract {

    interface View : STMvpContract.View {
        fun showData(cityList: List<Any>)
        fun showException(message: String)
        fun showLoading()
        fun hideLoading()
    }

    interface Presenter : STMvpContract.Presenter {
        fun loadData(forceUpdate: Boolean)
        fun subscribe()
        fun unSubscribe()
    }
}
