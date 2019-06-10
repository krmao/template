package com.smart.library.hybird

import android.util.Log
import com.smart.template.repository.STRepository
import com.smart.library.util.okhttp.STOkHttpManager
import com.smart.library.base.STBaseApplication
import com.smart.library.bundle.STHybird
import com.smart.library.bundle.model.STHybirdModuleConfigModel
import com.smart.library.bundle.strategy.STHybirdInitStrategy
import com.smart.library.util.STFileUtil
import com.smart.library.util.STJsonUtil
import com.smart.library.util.STLogUtil
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.InputStream

/**
 * 仅仅用于第一次安装bundle时执行的初始化代码，不能用作上下文application
 */
object HybirdApplication {

    fun init(initCallback: ((configList: MutableList<STHybirdModuleConfigModel>?) -> Unit)? = null) {
        Log.v("HybirdApplication", "HybirdApplication:init")

        //异步 所有模块配置文件下载器
        val allConfiger = { allConfigUrl: String, callback: (configList: MutableList<STHybirdModuleConfigModel>?) -> Unit? ->
            STRepository.downloadHybirdAllModuleConfigurations(allConfigUrl)
                .observeOn(Schedulers.io()) //下载成功后也是异步处理，防止回滚等好性能操作阻塞UI
                .subscribe(
                    { configList: MutableList<STHybirdModuleConfigModel>? ->
                        callback.invoke(configList)
                    },
                    { error: Throwable ->
                        STLogUtil.e("HybirdApplication", "总配置信息下载出错", error)
                        callback.invoke(null)
                    }
                )
            Unit
        }

        //异步 配置文件下载器
        val configer = { configUrl: String, callback: (STHybirdModuleConfigModel?) -> Unit? ->
            STOkHttpManager.doGet(configUrl, readTimeoutMS = 500, connectTimeoutMS = 500) {
                callback.invoke(STJsonUtil.fromJson(it, STHybirdModuleConfigModel::class.java))
            }
        }

        //异步 更新包下载器
        val downloader = { downloadUrl: String, file: File?, callback: (File?) -> Unit? ->
            STRepository.downloadFile(downloadUrl)
                .observeOn(Schedulers.io()) //下载成功后也是异步处理，防止回滚等好性能操作阻塞UI
                .subscribe({ content: InputStream ->
                    if (file != null) {
                        STFileUtil.copy(content, file)
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
        STHybird.init(true, false, STBaseApplication.DEBUG, STHybirdInitStrategy.LOCAL, "index.html", allConfigUrl, allConfiger, configer, downloader, initCallback)
    }
}
