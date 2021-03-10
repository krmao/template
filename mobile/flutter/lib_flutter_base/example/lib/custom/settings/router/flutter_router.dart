import 'package:flutter/material.dart';
import 'package:lib_flutter_base/lib_flutter_base.dart';

import '../../modules/bridge/bridge_widget.dart';
import '../../modules/order/order_widget.dart';
import '../../modules/player/player_tab_widget_state.dart';
import '../../modules/settings/settings_widget.dart';

class FlutterRouter {
  static const String URL_NATIVE_MINE = "native_mine";
  static const String URL_FLUTTER_ORDER = "flutter_order";
  static const String URL_FLUTTER_SETTINGS = "flutter_settings";
  static const String URL_FLUTTER_PLAYER = "flutter_player";
  static const String URL_FLUTTER_BRIDGE = "flutter_bridge";

  static Map<String, FlutterBoostRouteFactory> getRouters1() {
    Map<String, FlutterBoostRouteFactory> routers = {
      FlutterRouter.URL_FLUTTER_SETTINGS: (settings, uniqueId) {
        return PageRouteBuilder<dynamic>(
            settings: settings,
            pageBuilder: (_, __, ___) =>
                BasePageDefault(state: SettingsState({})));
      },
      FlutterRouter.URL_FLUTTER_ORDER: (settings, uniqueId) {
        return PageRouteBuilder<dynamic>(
            settings: settings, pageBuilder: (_, __, ___) => OrderWidget());
      },
    };
    return routers;
  }

  static Map<String, FlutterBoostRouteFactory> getRouters2() {
    Map<String, FlutterBoostRouteFactory> routers = {
      FlutterRouter.URL_FLUTTER_PLAYER: (settings, uniqueId) {
        return PageRouteBuilder<dynamic>(
            settings: settings,
            pageBuilder: (_, __, ___) =>
                BasePageDefault(state: MainTabWidgetState()));
      },
      FlutterRouter.URL_FLUTTER_BRIDGE: (settings, uniqueId) {
        return PageRouteBuilder<dynamic>(
            settings: settings,
            pageBuilder: (_, __, ___) =>
                BasePageDefault(state: BridgeWidgetState()));
      },
    };
    return routers;
  }
}
