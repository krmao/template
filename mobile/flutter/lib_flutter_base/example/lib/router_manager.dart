import 'package:flutter/material.dart';
import 'package:lib_flutter_base/lib_flutter_base.dart';

import 'custom/modules/bridge/bridge_widget.dart';
import 'custom/settings/router/flutter_router.dart';

class RouterManager {
    Map<String, WidgetBuilder> routeMap = new Map<String, WidgetBuilder>();

    RouterManager._privateConstructor() {
        routeMap.putIfAbsent("/", () => (BuildContext context) => Container(color: Colors.blue));
        routeMap.addAll(FlutterRouter.getRouters3());
        routeMap.addAll(FlutterRouter.getRouters4());
    }

    static final RouterManager instance = RouterManager._privateConstructor();
}
