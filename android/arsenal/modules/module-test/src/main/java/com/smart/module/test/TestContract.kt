package com.smart.module.test

import com.smart.housekeeper.repository.remote.model.response.citylist.City
import com.smart.library.base.mvp.CXBasePresenter
import com.smart.library.base.mvp.CXBaseView

interface TestContract {

    interface View : CXBaseView<Presenter> {
        fun showData(cityList: List<City>)
        fun showException(message: String)
    }

    interface Presenter : CXBasePresenter {
        fun loadData(forceUpdate: Boolean)
    }
}
