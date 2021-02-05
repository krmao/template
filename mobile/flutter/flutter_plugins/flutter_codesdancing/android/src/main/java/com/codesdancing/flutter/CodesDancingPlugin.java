package com.codesdancing.flutter;

import com.idlefish.flutterboost.FlutterBoostPlugin;

import io.flutter.plugin.common.PluginRegistry;

public class CodesDancingPlugin {
    public static void registerWith(PluginRegistry.Registrar registrar) {
        FlutterBoostPlugin.registerWith(registrar);
    }
}
