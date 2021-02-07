package com.codesdancing.flutterexample

import android.app.Activity
import android.app.Application
import android.content.Context
import com.idlefish.flutterboost.FlutterBoost
import com.idlefish.flutterboost.FlutterBoost.BoostLifecycleListener
import com.idlefish.flutterboost.Utils
import com.idlefish.flutterboost.interfaces.INativeRouter
import com.smart.library.STInitializer
import com.smart.library.util.STReflectUtil
import com.taobao.idlefish.flutterboostexample.PageRouter
import com.taobao.idlefish.flutterboostexample.TextPlatformViewFactory
import io.flutter.embedding.android.FlutterView
import io.flutter.plugin.common.StandardMessageCodec

class MyApplication : Application() {
    public override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        STInitializer.attachApplicationBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()

        /*STInitializer.initialApplication(
                STInitializer.Config(
                        application = this,
                        appDebug = BuildConfig.DEBUG,
                        configBundle = STInitializer.ConfigBundle(
                                bundleBusHandlerClassMap = hashMapOf(
                                        "flutter" to "com.codesdancing.flutter.FlutterBusHandler"
                                )
                        )
                )
        )*/

        val router = INativeRouter { context, url, urlParams, requestCode, exts ->
            val assembleUrl = Utils.assembleUrl(url, urlParams)
            PageRouter.openPageByUrl(context, assembleUrl, urlParams)
        }
        val boostLifecycleListener: BoostLifecycleListener = object : BoostLifecycleListener {
            override fun beforeCreateEngine() {}
            override fun onEngineCreated() {}
            override fun onPluginsRegistered() {}
            override fun onEngineDestroy() {}
        }

        //
        // AndroidManifest.xml 中必须要添加 flutterEmbedding 版本设置
        //
        //   <meta-data android:name="flutterEmbedding"
        //               android:value="2">
        //    </meta-data>
        // GeneratedPluginRegistrant 会自动生成 新的插件方式　
        //
        // 插件注册方式请使用
        // FlutterBoost.instance().engineProvider().getPlugins().add(new FlutterPlugin());
        // GeneratedPluginRegistrant.registerWith()，是在engine 创建后马上执行，放射形式调用
        //
        val platform = FlutterBoost.ConfigBuilder(this, router)
                .isDebug(true)
                .whenEngineStart(FlutterBoost.ConfigBuilder.IMMEDIATELY)
                .renderMode(FlutterView.RenderMode.texture)
                .lifecycleListener(boostLifecycleListener)
                .build()
        FlutterBoost.instance().init(platform)

        // whenEngineStart(FlutterBoost.ConfigBuilder.IMMEDIATELY) 时候，engine才初始化好。
        if (FlutterBoost.instance().engineProvider() != null) {
            val registry = FlutterBoost.instance().engineProvider().platformViewsController.registry
            registry.registerViewFactory("plugins.test/view", TextPlatformViewFactory(StandardMessageCodec.INSTANCE))
        }
    }
}