package com.smart.template

import com.smart.library.base.STBaseApplication

@Suppress("unused")
class FinalApplication : STBaseApplication() {

    override fun onCreate() {
        super.onCreate()
        if (isGodEyeEnabled()) {
            if (cn.hikyson.godeye.core.utils.ProcessUtils.isMainProcess(this)) {
                cn.hikyson.godeye.core.GodEye.instance().init(this)
                cn.hikyson.godeye.core.GodEye.instance().install(cn.hikyson.godeye.core.GodEyeConfig.fromAssets("config/config_godeye.xml"))
                cn.hikyson.godeye.monitor.GodEyeMonitor.work(this, 5388) // adb forward tcp:5388 tcp:5388 http://localhost:5388/index.html
            }
        }
    }

    private fun isGodEyeEnabled(): Boolean {
        try {
            Class.forName("cn.hikyson.godeye.core.GodEye")
            return true
        } catch (e: Exception) {
        }
        return false
    }

    fun exitApplication() {
        if (isGodEyeEnabled()) {
            cn.hikyson.godeye.monitor.GodEyeMonitor.shutDown()
            cn.hikyson.godeye.core.GodEye.instance().uninstall()
        }
    }

}
