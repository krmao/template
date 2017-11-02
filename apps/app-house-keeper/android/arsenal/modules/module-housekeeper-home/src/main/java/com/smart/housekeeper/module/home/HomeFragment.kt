package com.smart.housekeeper.module.home

import android.graphics.Color
import android.os.Bundle
import android.taobao.atlas.bundleInfo.AtlasBundleInfoManager
import android.taobao.atlas.framework.Atlas
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.smart.housekeeper.repository.HKRepository
import com.smart.library.base.HKBaseFragment
import com.smart.library.util.HKBigDecimalUtil
import com.smart.library.util.HKFileUtil
import com.smart.library.util.HKLogUtil
import com.smart.library.util.cache.HKCacheManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import java.io.File

class HomeFragment : HKBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val textView = TextView(context)
        textView.text = "home"
        textView.setBackgroundColor(Color.LTGRAY)
        textView.setOnClickListener {
            HKRepository.download("http://10.47.18.39:8080/server-house-keeper/static/files/test.json", { current, total ->
                HKLogUtil.d("download:progress", "current:$current/total:$total==${HKBigDecimalUtil.formatValue((current.toFloat() / total.toFloat() * 100).toDouble(), 2)}%")
            })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnNext {
                    HKFileUtil.copy(it.byteStream(), File(HKCacheManager.getCacheDir(), "test.zip").path) { current, total ->
                        HKLogUtil.w("copy:progress", "current:$current/total:$total==${HKBigDecimalUtil.formatValue((current.toFloat() / total.toFloat() * 100).toDouble(), 2)}%")
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { _: ResponseBody ->
                        HKLogUtil.w("progress", "onNext")
                    },
                    { error: Throwable ->
                        HKLogUtil.w("progress", "onError", error)
                    },
                    {
                        HKLogUtil.w("progress", "onComplete")
                    }
                )
        }
        HKLogUtil.w("krmao", "所有的Bundles:" + AtlasBundleInfoManager.instance().bundleInfo.bundles.toString())
        HKLogUtil.e("krmao", "当前已安装:" + Atlas.getInstance().bundles.toString())
        return textView
    }

    override fun onStart() {
        super.onStart()
        Log.w("krmao", "HomeFragment:onStart")
    }

    override fun onStop() {
        super.onStop()
        Log.w("krmao", "HomeFragment:onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.w("krmao", "HomeFragment:onDestroy")
    }
}
