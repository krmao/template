package com.codesdancing.flutter.plugins

import io.flutter.embedding.engine.FlutterEngine

object STFlutterPluginRegistrant {
    @JvmStatic
    fun registerWith(flutterEngine: FlutterEngine) {
        flutterEngine.plugins.add(STFlutterBridgeChannel.INSTANCE())
    }
}