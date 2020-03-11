package com.smart.template

import cn.hikyson.godeye.core.GodEye
import cn.hikyson.godeye.core.GodEyeConfig
import cn.hikyson.godeye.core.utils.ProcessUtils
import cn.hikyson.godeye.monitor.GodEyeMonitor
import com.smart.library.base.STBaseApplication

@Suppress("unused")
class FinalApplication : STBaseApplication() {

    override fun onCreate() {
        super.onCreate()

        if (ProcessUtils.isMainProcess(this)) {
            GodEye.instance().init(this)
            GodEye.instance().install(GodEyeConfig.fromAssets("config/config_godeye.xml"))
            GodEyeMonitor.work(this, 5388) // adb forward tcp:5388 tcp:5388 http://localhost:5388/index.html
        }

    }

    fun exitApplication() {
        GodEyeMonitor.shutDown()
        GodEye.instance().uninstall()
    }

}
