package com.smart.housekeeper.module.hybird

import android.app.Application
import android.util.Log
import com.smart.housekeeper.repository.HKRepository
import com.smart.housekeeper.repository.remote.core.HKOkHttpManager
import com.smart.library.base.HKBaseApplication
import com.smart.library.bundle.model.HKHybirdConfigModel
import com.smart.library.bundle.HKHybird
import com.smart.library.util.HKFileUtil
import com.smart.library.util.HKJsonUtil
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.InputStream

/**
 * 仅仅用于第一次安装bundle时执行的初始化代码，不能用作上下文application
 */
class HybirdApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.v("HybirdApplication", "HybirdApplication:onCreate")

        //异步 更新包下载器
        HKHybird.setDownloader { downloadUrl: String, file: File?, callback: (File?) -> Unit? ->
            HKRepository.downloadFile(downloadUrl)
                .observeOn(Schedulers.io()) //下载成功后也是异步处理，防止回滚等好性能操作阻塞UI
                .subscribe({ content: InputStream ->
                    if (file != null) {
                        HKFileUtil.copy(content, file)
                    }
                    callback.invoke(file)
                }, { _: Throwable ->
                    callback.invoke(null)
                }
                )
            Unit
        }
        //同步 配置文件下载器
        HKHybird.setConfiger { configUrl: String, callback: (HKHybirdConfigModel?) -> Boolean? ->
            callback.invoke(HKJsonUtil.fromJson(HKOkHttpManager.doGetSync(configUrl, readTimeoutMS = 200, connectTimeoutMS = 200), HKHybirdConfigModel::class.java))
        }
        //初始化开始
        HKHybird.init(HKBaseApplication.DEBUG, "pre")
    }
}
