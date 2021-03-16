package com.codesdancing.flutter;

import com.codesdancing.flutter.plugins.STFlutterBridgeChannel;

import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.plugins.FlutterPlugin;

public class LibFlutterBaseMultiplePlugin extends STFlutterBridgeChannel implements FlutterPlugin {

    @SuppressWarnings("unchecked")
    public static LibFlutterBaseMultiplePlugin getFlutterBaseMultiplePlugin(FlutterEngine engine) {
        if (engine != null) {
            try {
                Class<? extends FlutterPlugin> pluginClass = (Class<? extends FlutterPlugin>) Class.forName("com.codesdancing.flutter.LibFlutterBaseMultiplePlugin");
                return (LibFlutterBaseMultiplePlugin) engine.getPlugins().get(pluginClass);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}