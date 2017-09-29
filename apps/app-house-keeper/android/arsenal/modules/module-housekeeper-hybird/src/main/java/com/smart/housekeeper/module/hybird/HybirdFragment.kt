package com.smart.housekeeper.module.hybird

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.common.io.Files
import com.smart.library.base.HKBaseFragment
import com.smart.library.util.HKFileUtil
import com.smart.library.util.HKLogUtil
import com.smart.library.util.HKToastUtil
import com.smart.library.util.HKZipUtil
import com.smart.library.util.cache.HKCacheManager
import com.smart.library.util.rx.RxBus
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.hybird_fragment.*

class HybirdFragment : HKBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater?.inflate(R.layout.hybird_fragment, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val hybirdDirPath: String = HKCacheManager.getCacheDir().path + "/hybird/"
        val hybirdFilePath: String = hybirdDirPath + "bundle.zip"
        text.setOnClickListener {
            HKToastUtil.show("开始解压")
            Observable.fromCallable {
                HKLogUtil.d("hybird", "开始解压")

//                HKFileUtil.copy(activity.assets.open("bundle.zip"), hybirdFilePath)
//                HKZipUtil.unzip(hybirdFilePath, hybirdDirPath)

            }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
                HKToastUtil.show("解压成功")
                val htmlPath=hybirdDirPath + "bundle/html/index.html"
                HKLogUtil.d("hybird", "解压成功,执行跳转:"+htmlPath)
                HybirdWebFragment.goTo(activity,htmlPath )
            }, { error ->
                HKToastUtil.show("解压失败")
                HKLogUtil.e("hybird", "开始解压", error)
            })
        }
    }
}
