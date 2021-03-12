import 'dart:ui' as ui;

import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'package:lib_flutter_base/lib_flutter_base.dart';

import 'router_manager.dart';

/// https://flutter.dev/docs/development/add-to-app/debugging
/// https://flutter.cn/docs/development/add-to-app/debugging
/// flutter attach
/// flutter attach -d deviceId # AKC7N19118000852
/// flutter attach --isolate-filter='debug'
void initAndRunApp(String initialRoute) {
  ui.window.setIsolateDebugName("debug isolate");

  debugPaintSizeEnabled = false;
  debugPaintBaselinesEnabled = false;
  debugPrintLayouts = false;
  debugPaintLayerBordersEnabled = false;
  debugPaintPointersEnabled = false;
  debugRepaintRainbowEnabled = false;
  debugRepaintTextRainbowEnabled = false;

  appRun(MaterialApp(routes: {'/': RouterManager.instance.routeMap[initialRoute]}));
}

//region dartEntrypointFunctionName 仅比 initialRoute 多一个 main 前缀, 方便以后灵活的切换 '多引擎单路由' / '单引擎多路由'
@pragma('vm:entry-point') void mainFlutterBridge() => initAndRunApp("FlutterBridge");
@pragma('vm:entry-point') void mainFlutterSettings() => initAndRunApp("FlutterSettings");
@pragma('vm:entry-point') void mainFlutterOrder() => initAndRunApp("FlutterOrder");
@pragma('vm:entry-point') void mainFlutterPlayer() => initAndRunApp("FlutterPlayer");
//endregion

void main() => initAndRunApp("/");
