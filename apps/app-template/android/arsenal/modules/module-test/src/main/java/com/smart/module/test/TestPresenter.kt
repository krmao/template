package com.smart.module.test

import android.util.Log
import com.smart.template.repository.CXRepository
import com.smart.template.repository.remote.model.request.CityRequestModel
import io.reactivex.disposables.CompositeDisposable

class TestPresenter(private val view: TestContract.View) : TestContract.Presenter {
    private val subscriptions: CompositeDisposable = CompositeDisposable()

    override fun loadData(forceUpdate: Boolean) {
        view.showLoading()
        subscriptions.add(CXRepository.getCityList(CityRequestModel(null)).subscribe({ cityList ->
            Log.w("krmao", "request success: cityList=$cityList")
            view.showData(cityList)
            view.hideLoading()
        }, { throwable ->
            Log.w("krmao", "request failure: throwable=$throwable")
            view.showException(throwable.message ?: "net error")
        }))
    }

    override fun subscribe() {
        subscriptions.clear()
        loadData(false)
    }

    override fun unSubscribe() {
        subscriptions.clear()
    }
}
