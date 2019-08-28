package com.smart.library.flutter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.support.annotation.NonNull
import com.smart.library.flutter.utils.FlutterRouter
import com.smart.library.flutter.views.NativeMainActivity
import com.taobao.idlefish.flutterboost.Debuger
import com.taobao.idlefish.flutterboost.interfaces.IPlatform
import io.flutter.view.FlutterMain

@Suppress("UNCHECKED_CAST", "MemberVisibilityCanBePrivate")
@SuppressLint("StaticFieldLeak")
object FlutterManager {

    const val CHANNEL_METHOD = "smart.flutter.io/methods"
    const val KEY_FLUTTER_STRING_RESULT = "KEY_FLUTTER_STRING_RESULT"

    var currentActivity: Activity? = null
        internal set
    var application: Application? = null
        private set

    @JvmStatic
    fun startInitialization(@NonNull application: Application) {
        FlutterManager.application = application

        FlutterBundle.init(object : IPlatform {
            override fun getApplication(): Application {
                return application
            }

            /**
             * 获取应用入口的Activity,这个Activity在应用交互期间应该是一直在栈底的
             */
            override fun getMainActivity(): Activity? {
                return if (NativeMainActivity.sRef != null) NativeMainActivity.sRef.get() else null
            }

            override fun isDebug(): Boolean {
                return true
            }

            /**
             * 如果flutter想打开一个本地页面，将会回调这个方法，页面参数将会拼接在url中
             * 例如：sample://nativePage?aaa=bbb
             * 参数就是类似 aaa=bbb 这样的键值对
             */
            override fun startActivity(context: Context, url: String, requestCode: Int): Boolean {
                Debuger.log("startActivity url=$url")
                return FlutterRouter.openPageByUrl(contex t, url, requestCode)
            }

            override fun getSettings(): Map<*, *>? {
                return null
            }
        })

        FlutterMain.startInitialization(application)
    }
    
}