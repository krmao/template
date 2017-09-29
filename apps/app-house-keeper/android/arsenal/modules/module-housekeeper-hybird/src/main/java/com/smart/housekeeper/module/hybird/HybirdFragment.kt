package com.smart.housekeeper.module.hybird

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smart.library.base.HKBaseFragment
import com.smart.library.util.HKFileUtil
import com.smart.library.util.HKLogUtil
import com.smart.library.util.HKToastUtil
import com.smart.library.util.HKZipUtil
import com.smart.library.util.cache.HKCacheManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.hybird_fragment.*
import java.io.File

class HybirdFragment : HKBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater?.inflate(R.layout.hybird_fragment, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundleZipName = "bundle.zip"
        val bundleUnZipDirName = "hybird"
        val bundleDirInSdcardPath: String = HKCacheManager.getCacheDir().path + "/"
        val bundleZipInSdcardPath: String = bundleDirInSdcardPath + bundleUnZipDirName + ".zip"
        val indexPath = "file://$bundleDirInSdcardPath$bundleUnZipDirName/index.html"

        text.setOnClickListener {
            HKToastUtil.show("开始解压")
            Observable.fromCallable {
                HKLogUtil.d("hybird", "开始解压")

                HKLogUtil.w("hybird", "bundleZipName:" + bundleZipName)
                HKLogUtil.w("hybird", "bundleDirInSdcardPath:" + bundleDirInSdcardPath)
                HKLogUtil.w("hybird", "bundleZipInSdcardPath:" + bundleZipInSdcardPath)
                HKLogUtil.w("hybird", "indexPath:" + indexPath)

                HKFileUtil.deleteFile(bundleZipInSdcardPath)
                HKFileUtil.deleteDirectory(bundleDirInSdcardPath + bundleUnZipDirName + "/")

                HKFileUtil.copy(activity.assets.open(bundleZipName), bundleZipInSdcardPath)
                HKLogUtil.d("hybird", "copy to sdcard success ! bundleZipInSdcardExists === " + File(bundleZipInSdcardPath).exists())
                HKLogUtil.d("hybird", "start unzip now ...")
                HKZipUtil.unzip(bundleZipInSdcardPath, bundleDirInSdcardPath)
                HKLogUtil.d("hybird", "unzip success !")
            }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        HKToastUtil.show("解压成功")
                        HKLogUtil.d("hybird", "解压成功,执行跳转:" + indexPath)
                        HybirdWebFragment.goTo(activity, indexPath)
                    }, { error ->
                        HKToastUtil.show("解压失败")
                        HKLogUtil.e("hybird", "开始解压", error)
                    })
        }
    }
}
