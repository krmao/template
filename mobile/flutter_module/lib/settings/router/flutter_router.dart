import 'package:flutter_module/modules/bridge/bridge_widget.dart';

import '../../settings/imports/flutter_imports_material.dart';

class FlutterRouter {
  static const String URL_NATIVE_MINE = "native_mine";
  static const String URL_FLUTTER_ORDER = "flutter_order";
  static const String URL_FLUTTER_SETTINGS = "flutter_settings";
  static const String URL_FLUTTER_PLAYER = "flutter_player";
  static const String URL_FLUTTER_BRIDGE = "flutter_bridge";

  static bool _isInitialized = false;

  static initialize() {
    if (_isInitialized) return;
    FlutterBoost.singleton.registerPageBuilders({
      FlutterRouter.URL_FLUTTER_SETTINGS: (pageName, params, _) => SettingsWidget(params),
      FlutterRouter.URL_FLUTTER_ORDER: (pageName, params, _) => OrderWidget(),
      FlutterRouter.URL_FLUTTER_PLAYER: (pageName, params, _) => STBaseStatefulWidget(state: MainTabWidgetState()),
      FlutterRouter.URL_FLUTTER_BRIDGE: (pageName, params, _) => BridgeWidget()
    });
    FlutterBoost.singleton.registerRouteSettingsBuilder((url, {exts, urlParams}) => BoostRouteSettings(uniqueId: '${url}_${DateTime.now().millisecondsSinceEpoch}', name: url, params: urlParams));
    FlutterBoost.singleton.registerDefaultPageBuilder((pageName, params, uniqueId) {
      return Scaffold(
          backgroundColor: Colors.blue,
          appBar: AppBar(title: Text('DEFAULT FLUTTER PAGE"')),
          body: new Column(mainAxisAlignment: MainAxisAlignment.center, children: <Widget>[
            new GestureDetector(
              onTap: () => closeCurrent(),
              child: new Container(
                padding: const EdgeInsets.all(10.0),
                color: Colors.blue,
                child: const Text("BACK", style: TextStyle(fontSize: 28.0, color: Colors.white)),
              ),
            )
          ]));
    });

    FlutterBoost.singleton.addBoostNavigatorObserver(BoostNavigatorObserver());
    FlutterBoost.singleton.addContainerObserver((ContainerOperation operation, BoostContainerSettings settings) {});

    FlutterBoostAPI.singleton.routeSettingsBuilder = (String url, {Map<String, dynamic> urlParams, Map<String, dynamic> exts}) => BoostRouteSettings(
          uniqueId: '${url}_${DateTime.now().millisecondsSinceEpoch}',
          name: url,
          params: urlParams,
        );

    _isInitialized = true;
  }

  static Future<Map<String, dynamic>> open(String url, {Map<String, dynamic> urlParams, Map<String, dynamic> exts}) {
    return FlutterBoost.singleton.open(url, urlParams: urlParams, exts: exts);
  }

  /**
   * 有 bug, 暂时不起作用
   */
  static Future<Map<String, dynamic>> _openInCurrentContainer(String url, {Map<String, dynamic> urlParams, Map<String, dynamic> exts}) {
    return FlutterBoost.singleton.openInCurrentContainer(url, urlParams: urlParams, exts: exts);
  }

  static Future<bool> close(String id, {Map<String, dynamic> result, Map<String, dynamic> exts}) {
    return FlutterBoost.singleton.close(id, result: result, exts: exts);
  }

  static bool closeInCurrentContainer<T extends Object>(T result) {
    return FlutterBoost.singleton.closeInCurrentContainer(result);
  }

  static Future<bool> closeCurrent({Map<String, dynamic> result, Map<String, dynamic> exts}) {
    return FlutterBoost.singleton.closeCurrent(result: result, exts: exts);
  }

  static Future<bool> closeByContext(BuildContext context, {Map<String, dynamic> result, Map<String, dynamic> exts}) {
    return FlutterBoost.singleton.closeByContext(context, result: result, exts: exts);
  }
}

class BoostNavigatorObserver extends ContainerNavigatorObserver {
  void didPush(Route<dynamic> route, Route<dynamic> previousRoute) {
    //新页面已经push完成
    print("flutterboost#didPush");
  }

  void didPop(Route<dynamic> route, Route<dynamic> previousRoute) {
    print("flutterboost#didPop");
  }

  void didRemove(Route<dynamic> route, Route<dynamic> previousRoute) {
    print("flutterboost#didRemove");
  }

  void didReplace({Route<dynamic> newRoute, Route<dynamic> oldRoute}) {
    print("flutterboost#didReplace");
  }

  void willPush(Route<dynamic> route, Route<dynamic> previousRoute) {
    print("flutterboost#willPush");
  }
}
