package com.smart.library.flutter

import android.os.Bundle

/**
 * Flutter 启动页
 * https://flutter.cn/docs/development/ui/splash-screen/android-splash-screen
 */
@Suppress("unused", "PrivatePropertyName")
class STFlutterFragmentLaunchActivity : STFlutterBoostActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        intent = withNewEngine()
            .url("flutter://flutter/settings")
            .params(hashMapOf<String, Any?>())
            .backgroundMode(BackgroundMode.transparent)
            .build(context)

        super.onCreate(savedInstanceState)
    }

}
