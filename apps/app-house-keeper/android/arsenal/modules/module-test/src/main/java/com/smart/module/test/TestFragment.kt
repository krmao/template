package com.smart.module.test

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView
import com.smart.housekeeper.repository.remote.model.response.citylist.City
import com.smart.library.base.CXActivity
import com.smart.library.base.CXBaseFragment
import com.smart.library.util.rx.RxBus
import com.smart.library.util.rx.RxRouteEvent
import com.smart.library.widget.loading.CXFrameLoadingLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.test_fragment_test.*
import java.util.concurrent.TimeUnit

class TestFragment : CXBaseFragment(), TestContract.View {

    companion object {
        fun goTo(activity: Activity) {
            CXActivity.start(activity, TestFragment::class.java)
        }
    }

    private val presenter: TestContract.Presenter by lazy {
        TestPresenter(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.test_fragment_test, null, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        RxView.clicks(fab_test).concatWith(RxView.clicks(btn_test)).debounce(5000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe {
            Log.w("TestRouteManager", "start from self")
            RxBus.post(RxRouteEvent().apply {
                activity = activity
                targetUrl = "test://detail"
                bundle = null
                id = javaClass.canonicalName
            })
        }
        RxView.clicks(btn_test).debounce(300, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe {
            tv_city_list.text = null
            presenter.loadData(true)
        }
        loading_view.setOnRefreshClickListener(View.OnClickListener {
            presenter.loadData(true)
        })
        presenter.subscribe()
    }

    override fun showException(message: String) {
        loading_view.showView(CXFrameLoadingLayout.ViewType.NETWORK_EXCEPTION,message,true)
    }

    override fun showLoading() {
        loading_view.showView(CXFrameLoadingLayout.ViewType.LOADING)
    }

    override fun hideLoading() {
        loading_view.hideAll()
    }

    override fun showData(cityList: List<City>) {
        tv_city_list.text = cityList.toString()
    }

    override fun onDestroy() {
        presenter.unSubscribe()
        super.onDestroy()
    }
}
