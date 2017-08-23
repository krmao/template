package com.xixi.module.test

import com.xixi.fruitshop.repository.remote.model.response.citylist.City
import com.xixi.library.android.base.mvp.CXBasePresenter
import com.xixi.library.android.base.mvp.CXBaseView

interface TestContract {

    interface View : CXBaseView<Presenter> {
        fun showData(cityList: List<City>)
        fun showException(message: String)
    }

    interface Presenter : CXBasePresenter {
        fun loadData(forceUpdate: Boolean)
    }
}
