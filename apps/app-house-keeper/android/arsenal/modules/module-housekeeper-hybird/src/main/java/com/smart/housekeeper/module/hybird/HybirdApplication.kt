package com.smart.housekeeper.module.hybird

import android.util.Log
import com.smart.housekeeper.repository.HKRepository
import com.smart.housekeeper.repository.remote.core.HKOkHttpManager
import com.smart.library.base.HKBaseApplication
import com.smart.library.bundle.HKHybird
import com.smart.library.bundle.model.HKHybirdModuleConfigModel
import com.smart.library.bundle.strategy.HKHybirdInitStrategy
import com.smart.library.util.HKFileUtil
import com.smart.library.util.HKJsonUtil
import com.smart.library.util.HKLogUtil
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.InputStream

/**
 * 仅仅用于第一次安装bundle时执行的初始化代码，不能用作上下文application
 */
object HybirdApplication {

    fun init() {
        Log.v("HybirdApplication", "HybirdApplication:init")

        //异步 所有模块配置文件下载器
        val allConfiger = { allConfigUrl: String, callback: (configList: MutableList<HKHybirdModuleConfigModel>?) -> Unit? ->
            HKRepository.downloadHybirdAllModuleConfigurations(allConfigUrl)
                .observeOn(Schedulers.io()) //下载成功后也是异步处理，防止回滚等好性能操作阻塞UI
                .subscribe(
                    { configList: MutableList<HKHybirdModuleConfigModel>? ->
                        callback.invoke(configList)
                    },
                    { error: Throwable ->
                        HKLogUtil.e("HybirdApplication", "总配置信息下载出错", error)
                        callback.invoke(null)
                    }
                )
            Unit
        }

        //同步 配置文件下载器
        val configer = { configUrl: String, callback: (HKHybirdModuleConfigModel?) -> Unit? ->
            callback.invoke(HKJsonUtil.fromJson(HKOkHttpManager.doGetSync(configUrl, readTimeoutMS = 2000, connectTimeoutMS = 2000), HKHybirdModuleConfigModel::class.java))
        }

        //异步 更新包下载器
        val downloader = { downloadUrl: String, file: File?, callback: (File?) -> Unit? ->
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

        val allConfigUrl = "http://10.47.58.14:8080/background/files/all.json"
        //初始化开始
        HKHybird.init(HKBaseApplication.DEBUG, "pre", HKHybirdInitStrategy.DOWNLOAD, allConfigUrl, allConfiger, configer, downloader)
    }
}
