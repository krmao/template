package com.smart.library.flutter;

import com.taobao.idlefish.flutterboost.FlutterBoostPlugin;
import com.taobao.idlefish.flutterboost.interfaces.IPlatform;

@SuppressWarnings("WeakerAccess")
public class FlutterBundle {

    public static void init(IPlatform platform) {
        FlutterBoostPlugin.init(platform);
    }

}
