package com.smart.template.module.hybird

import android.util.Log
import com.smart.template.repository.CXRepository
import com.smart.template.repository.remote.core.CXOkHttpManager
import com.smart.library.base.CXBaseApplication
import com.smart.library.bundle.CXHybird
import com.smart.library.bundle.model.CXHybirdModuleConfigModel
import com.smart.library.bundle.strategy.CXHybirdInitStrategy
import com.smart.library.util.CXFileUtil
import com.smart.library.util.CXJsonUtil
import com.smart.library.util.CXLogUtil
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
        val allConfiger = { allConfigUrl: String, callback: (configList: MutableList<CXHybirdModuleConfigModel>?) -> Unit? ->
            CXRepository.downloadHybirdAllModuleConfigurations(allConfigUrl)
                .observeOn(Schedulers.io()) //下载成功后也是异步处理，防止回滚等好性能操作阻塞UI
                .subscribe(
                    { configList: MutableList<CXHybirdModuleConfigModel>? ->
                        callback.invoke(configList)
                    },
                    { error: Throwable ->
                        CXLogUtil.e("HybirdApplication", "总配置信息下载出错", error)
                        callback.invoke(null)
                    }
                )
            Unit
        }

        //异步 配置文件下载器
        val configer = { configUrl: String, callback: (CXHybirdModuleConfigModel?) -> Unit? ->
            CXOkHttpManager.doGet(configUrl, readTimeoutMS = 500, connectTimeoutMS = 500) {
                callback.invoke(CXJsonUtil.fromJson(it, CXHybirdModuleConfigModel::class.java))
            }
        }

        //异步 更新包下载器
        val downloader = { downloadUrl: String, file: File?, callback: (File?) -> Unit? ->
            CXRepository.downloadFile(downloadUrl)
                .observeOn(Schedulers.io()) //下载成功后也是异步处理，防止回滚等好性能操作阻塞UI
                .subscribe({ content: InputStream ->
                    if (file != null) {
                        CXFileUtil.copy(content, file)
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
        CXHybird.init(CXBaseApplication.DEBUG, CXHybirdInitStrategy.DOWNLOAD, allConfigUrl, allConfiger, configer, downloader)
    }
}
