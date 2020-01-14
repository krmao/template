package com.smart.library.flutter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.annotation.NonNull
import com.idlefish.flutterboost.*
import com.idlefish.flutterboost.interfaces.IContainerRecord
import com.idlefish.flutterboost.interfaces.IFlutterEngineProvider
import com.smart.library.util.bus.STBusManager
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.plugin.common.PluginRegistry
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

        FlutterBoost.init(
                object : Platform() {

                    override fun registerPlugins(registry: PluginRegistry?) {
                        super.registerPlugins(registry)
                        BoostChannel.registerWith(registry?.registrarFor("flutter_boost_channel"))
                    }

                    override fun engineProvider(): IFlutterEngineProvider {
                        return object : BoostEngineProvider() {
                            override fun createEngine(context: Context): BoostFlutterEngine {
                                return BoostFlutterEngine(
                                        context,
                                        DartExecutor.DartEntrypoint(
                                                context.resources.assets,
                                                FlutterMain.findAppBundlePath(context),
                                                "main"
                                        ),
                                        "/")
                            }
                        };
                    }

                    override fun whenEngineStart(): Int = ANY_ACTIVITY_CREATED

                    override fun openContainer(context: Context?, url: String?, urlParams: MutableMap<String, Any>?, requestCode: Int, exts: MutableMap<String, Any>?) {
                        FlutterRouter.openContainer(context, url, urlParams, requestCode, exts)
                    }

                    override fun closeContainer(containerRecord: IContainerRecord?, result: MutableMap<String, Any>?, exts: MutableMap<String, Any>?) {
                        containerRecord?.container?.finishContainer(result)
                    }

                    override fun getApplication(): Application = application

                    override fun isDebug(): Boolean = isDebug
                }
        )

        FlutterMain.startInitialization(application)
    }

}