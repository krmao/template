package com.smart.housekeeper.module.hybird

import android.app.Application
import android.util.Log
import com.smart.housekeeper.repository.HKRepository
import com.smart.library.base.HKBaseApplication
import com.smart.library.bundle.HKHybirdManager
import com.smart.library.bundle.HKHybirdModuleConfiguration
import com.smart.library.bundle.HKHybirdModuleManager
import com.smart.library.util.HKBigDecimalUtil
import com.smart.library.util.HKFileUtil
import com.smart.library.util.HKLogUtil
import java.io.InputStream

/**
 * 仅仅用于第一次安装bundle时执行的初始化代码，不能用作上下文application
 */
class HybirdApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.v("krmao", "HybirdApplication:onCreate")

        //set downloader and configure
        HKHybirdManager.MODULES.value.forEach {
            val moduleManager: HKHybirdModuleManager? = it.value
            moduleManager?.setDownloader { downloadUrl, file, callback ->
                HKRepository.downloadFile(downloadUrl,
                    { current, total ->
                        if (total.toFloat() > 0)
                            HKLogUtil.d(moduleManager.moduleFullName, "current:$current/total:$total==${HKBigDecimalUtil.formatValue((current.toFloat() / total.toFloat() * 100).toDouble(), 2)}%")
                        else
                            HKLogUtil.w(moduleManager.moduleFullName, "current:$current/total:$total")
                    }
                ).subscribe(
                    { content: InputStream ->
                        HKLogUtil.w(moduleManager.moduleFullName, "download success result :$content")
                        if (file != null) HKFileUtil.copy(content, file)
                        callback.invoke(file)
                    },
                    { error: Throwable ->
                        HKLogUtil.w(moduleManager.moduleFullName, "download failure", error)
                        callback.invoke(null)
                    }
                )

                Unit
            }
            moduleManager?.setConfiger { configUrl: String, callback: (HKHybirdModuleConfiguration?) -> Unit? ->
                HKRepository.downloadHybirdModuleConfiguration(configUrl).subscribe(
                    { content: HKHybirdModuleConfiguration ->
                        HKLogUtil.w(moduleManager.moduleFullName, "download success result :$content")
                        callback.invoke(content)
                    },
                    { error: Throwable ->
                        HKLogUtil.w(moduleManager.moduleFullName, "download failure", error)
                        callback.invoke(null)
                    }
                )
                Unit
            }

            //3: 每次程序启动时，a:所有模块执行一次检查更新 checkUpdate，b:所有模块执行一次健康体检 checkHealth
            moduleManager?.checkUpdate()
        }

        //all config before call init
        HKHybirdManager.init(HKBaseApplication.DEBUG, "pre")
    }
}
