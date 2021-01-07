import 'package:codesdancing_bridge/codesdancing_bridge.dart';
import 'package:flutter/rendering.dart';

import 'settings/router/flutter_router.dart';

void main() {
  debugPaintSizeEnabled = false;
  Map<String, Map<Object, PageBuilder>> namedRouters = {};
  namedRouters["业务A"] = FlutterRouter.getRouters1();
  namedRouters["业务B"] = FlutterRouter.getRouters2();

  appRun(PageNotFoundHomePage(), routerProvider: () => RouterUtil.wrapRouters(namedRouters));
}