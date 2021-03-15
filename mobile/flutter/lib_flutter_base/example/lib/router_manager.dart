import 'package:flutter/material.dart';

import 'router/flutter_router.dart';

class RouterManager {
  Map<String, WidgetBuilder> routeMap = new Map<String, WidgetBuilder>();

  RouterManager._privateConstructor() {
    routeMap.putIfAbsent(
        "/", () => (BuildContext context) => Container(color: Colors.blue));
    routeMap.addAll(FlutterRouter.getRouters3());
    routeMap.addAll(FlutterRouter.getRouters4());
  }

  static final RouterManager instance = RouterManager._privateConstructor();
}
