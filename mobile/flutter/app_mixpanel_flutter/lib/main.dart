import 'package:flutter_codesdancing/flutter_codesdancing.dart';
import 'package:flutter/rendering.dart';
import 'dart:ui' as ui;

import 'settings/router/flutter_router.dart';

void main() {
  // https://flutter.dev/docs/development/add-to-app/debugging
  // https://flutter.cn/docs/development/add-to-app/debugging
  // flutter attach
  // flutter attach -d deviceId # AKC7N19118000852
  // flutter attach --isolate-filter='debug'
  ui.window.setIsolateDebugName("debug isolate");

  debugPaintSizeEnabled = false;
  Map<String, Map<Object, PageBuilder>> namedRouters = {};
  namedRouters["业务A"] = FlutterRouter.getRouters1();
  namedRouters["业务B"] = FlutterRouter.getRouters2();

  appRun(PageNotFoundHomePage(), routerProvider: () => RouterUtil.wrapRouters(namedRouters));
}