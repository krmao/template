import 'package:flutter_codesdancing/flutter_codesdancing.dart';
import 'package:flutter/material.dart';
import 'package:flutter_module_template/modules/bridge/bridge_widget.dart';
import 'package:flutter_module_template/modules/order/order_widget.dart';
import 'package:flutter_module_template/modules/player/player_tab_widget_state.dart';
import 'package:flutter_module_template/modules/settings/settings_widget.dart';

class FlutterRouter {
  static const String URL_NATIVE_MINE = "native_mine";
  static const String URL_FLUTTER_ORDER = "flutter_order";
  static const String URL_FLUTTER_SETTINGS = "flutter_settings";
  static const String URL_FLUTTER_PLAYER = "flutter_player";
  static const String URL_FLUTTER_BRIDGE = "flutter_bridge";

  static Map<PageConfigItem, PageBuilder> getRouters1() {
    Map<PageConfigItem, PageBuilder> routers = {
      PageConfigItem("设置页面", FlutterRouter.URL_FLUTTER_SETTINGS): (pageName, params, _) {
        return PageRouteObserverProvider(data: params, child: PageWidget(state: SettingsState(params)));
      },
      PageConfigItem("订单页面", FlutterRouter.URL_FLUTTER_ORDER): (pageName, params, _) {
        return PageRouteObserverProvider(data: params, child: OrderWidget());
      }
    };
    return routers;
  }

  static Map<PageConfigItem, PageBuilder> getRouters2() {
    Map<PageConfigItem, PageBuilder> routers = {
      PageConfigItem("玩家页面", FlutterRouter.URL_FLUTTER_PLAYER): (pageName, params, _) {
        return PageRouteObserverProvider(data: params, child: PageWidget(state: MainTabWidgetState()));
      },
      PageConfigItem("桥接页面", FlutterRouter.URL_FLUTTER_BRIDGE): (pageName, params, _) {
        return PageRouteObserverProvider(data: params, child: PageWidget(state: BridgeWidgetState()));
      }
    };
    return routers;
  }
}
