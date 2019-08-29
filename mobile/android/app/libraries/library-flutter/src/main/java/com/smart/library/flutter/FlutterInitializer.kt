package com.smart.library.flutter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.support.annotation.NonNull
import com.smart.library.util.bus.STBusManager
import com.taobao.idlefish.flutterboost.FlutterBoostPlugin
import com.taobao.idlefish.flutterboost.interfaces.IPlatform
import io.flutter.view.FlutterMain

@Suppress("UNCHECKED_CAST", "MemberVisibilityCanBePrivate")
@SuppressLint("StaticFieldLeak")
object FlutterInitializer {

    var application: Application? = null
        private set

    val homeActivity: Activity?
        get() = STBusManager.homeActivity?.get()

    /**
     * @param mainActivity 获取应用入口的Activity,这个Activity在应用交互期间应该是一直在栈底的
     * @param startActivity
     *      如果flutter想打开一个本地页面，将会回调这个方法，页面参数将会拼接在url中
     *      例如：sample://nativePage?aaa=bbb
     *      参数就是类似 aaa=bbb 这样的键值对
     */
    @JvmStatic
    fun startInitialization(@NonNull application: Application, mainActivity: Activity? = null, isDebug: Boolean = true, settings: Map<*, *>? = null, startActivity: (context: Context, url: String, requestCode: Int) -> Boolean) {
        FlutterInitializer.application = application

        FlutterBoostPlugin.init(
                object : IPlatform {
                    override fun getApplication(): Application = application

                    /**
                     * 获取应用入口的Activity,这个Activity在应用交互期间应该是一直在栈底的
                     */
                    override fun getMainActivity(): Activity? = mainActivity ?: homeActivity

                    override fun isDebug(): Boolean = isDebug

                    override fun startActivity(context: Context, url: String, requestCode: Int): Boolean = startActivity(context, url, requestCode)

                    override fun getSettings(): Map<*, *>? = settings
                }
        )

        FlutterMain.startInitialization(application)
    }

}