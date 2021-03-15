import 'package:flutter/material.dart';
import 'package:lib_flutter_base/lib_flutter_base.dart';

import '../modules/bridge/bridge_widget.dart';
import '../modules/order/order_widget.dart';
import '../modules/player/player_tab_widget_state.dart';
import '../modules/settings/settings_widget.dart';

class FlutterRouter {
  static Map<String, WidgetBuilder> getRouters3() {
    Map<String, WidgetBuilder> routers = {
      "FlutterSettings": (BuildContext context) =>
          BasePageDefault(state: SettingsState({})),
      "FlutterOrder": (BuildContext context) => OrderWidget()
    };
    return routers;
  }

  static Map<String, WidgetBuilder> getRouters4() {
    Map<String, WidgetBuilder> routers = {
      "FlutterPlayer": (BuildContext context) =>
          BasePageDefault(state: MainTabWidgetState()),
      "FlutterBridge": (BuildContext context) =>
          BasePageDefault(state: BridgeWidgetState())
    };
    return routers;
  }

  static Map<String, FlutterBoostRouteFactory> getRouters1() {
    Map<String, FlutterBoostRouteFactory> routers = {
      "FlutterSettings": (settings, uniqueId) {
        return PageRouteBuilder<dynamic>(
            settings: settings,
            pageBuilder: (_, __, ___) =>
                BasePageDefault(state: SettingsState({})));
      },
      "FlutterOrder": (settings, uniqueId) {
        return PageRouteBuilder<dynamic>(
            settings: settings, pageBuilder: (_, __, ___) => OrderWidget());
      },
    };
    return routers;
  }

  static Map<String, FlutterBoostRouteFactory> getRouters2() {
    Map<String, FlutterBoostRouteFactory> routers = {
      "FlutterPlayer": (settings, uniqueId) {
        return PageRouteBuilder<dynamic>(
            settings: settings,
            pageBuilder: (_, __, ___) =>
                BasePageDefault(state: MainTabWidgetState()));
      },
      "FlutterBridge": (settings, uniqueId) {
        return PageRouteBuilder<dynamic>(
            settings: settings,
            pageBuilder: (_, __, ___) =>
                BasePageDefault(state: BridgeWidgetState()));
      },
    };
    return routers;
  }
}
