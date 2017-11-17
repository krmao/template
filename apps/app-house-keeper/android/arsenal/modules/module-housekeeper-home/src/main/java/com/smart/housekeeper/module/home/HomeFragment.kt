package com.smart.housekeeper.module.home

import android.os.Bundle
import android.taobao.atlas.bundleInfo.AtlasBundleInfoManager
import android.taobao.atlas.framework.Atlas
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smart.housekeeper.repository.HKRepository
import com.smart.library.base.HKBaseFragment
import com.smart.library.util.HKBigDecimalUtil
import com.smart.library.util.HKFileUtil
import com.smart.library.util.HKLogUtil
import com.smart.library.util.cache.HKCacheManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.home_fragment.*
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File

class HomeFragment : HKBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater?.inflate(R.layout.home_fragment, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        text1.setOnClickListener {
            val path = "http://10.47.18.39:7777/download/v1/test2"
            HKRepository.download(path, { current, total ->
                HKLogUtil.d("download:progress", "current:$current/total:$total==${HKBigDecimalUtil.formatValue((current.toFloat() / total.toFloat() * 100).toDouble(), 2)}%")
            })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnNext { response: Response<ResponseBody> ->
                    val headers = response.headers()
                    System.out.println("-----------")
                    System.out.println("Content-Type:" + headers.get("Content-Type"))
                    val eTag = headers.get("ETag")?.toLongOrNull()
                    System.out.println("ETag:" + eTag)
                    val lastModified = headers.getDate("Last-Modified")?.time
                    System.out.println("Last-Modified:" + headers.getDate("Last-Modified"))
                    System.out.println("Date:" + headers.getDate("Date"))
                    System.out.println("Expires:" + headers.getDate("Expires"))
                    System.out.println("Cache-Control:" + headers.get("Cache-Control"))
                    System.out.println("max-age:" + headers.get("Cache-Control")?.split("=")?.getOrNull(1))
                    System.out.println("Content-Disposition:" + headers.get("Content-Disposition"))
                    val fileName = headers.get("Content-Disposition")?.split("=")?.getOrNull(1) ?: "temp_file"
                    System.out.println("filename:" + fileName)
                    System.out.println("-----------")
                    val file = File(HKCacheManager.getCacheDir(), fileName)
                    if (response.body() != null)
                        HKFileUtil.copy(response.body()?.byteStream(), file.path) { current, total ->
                            HKLogUtil.w("copy:progress", "current:$current/total:$total==${HKBigDecimalUtil.formatValue((current.toFloat() / total.toFloat() * 100).toDouble(), 2)}%")
                        }
                    System.out.println("file:lastModified:" + file.lastModified())
                    val tmpModified = eTag ?: lastModified ?: file.lastModified()
                    val setSuccess = file.setLastModified(tmpModified) //can't set, bug is here: https://issuetracker.google.com/issues/36930892
                    System.out.println("file:tmpModified:$tmpModified , setSuccess:$setSuccess")
                    System.out.println("file:lastModified:" + file.lastModified())
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { _: Response<ResponseBody> ->
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
        text2.setOnClickListener {
            HKRepository.getBundleConfig()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { _: String ->
                        HKLogUtil.w("bundleConfig", "onNext")
                    },
                    { error: Throwable ->
                        HKLogUtil.w("bundleConfig", "onError", error)
                    },
                    {
                        HKLogUtil.w("bundleConfig", "onComplete")
                    }
                )
        }
    }

    override fun onStart() {
        super.onStart()
        Log.w("krmao", "HomeFragment:onStart")
        HKLogUtil.w("krmao", "所有的Bundles:" + AtlasBundleInfoManager.instance().bundleInfo.bundles.toString())
        HKLogUtil.e("krmao", "当前已安装:" + Atlas.getInstance().bundles.toString())
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
