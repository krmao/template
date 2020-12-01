import '../../settings/imports/flutter_imports_material.dart';

class FlutterRouter {
  static const String URL_MINE = "native_mine";
  static const String URL_ORDER = "flutter_order";
  static const String URL_SETTINGS = "flutter_settings";

  static bool _isInitialized = false;

  static initialize() {
    if (_isInitialized) return;
    FlutterBoost.singleton.registerPageBuilders({FlutterRouter.URL_SETTINGS: (pageName, params, _) => SettingsWidget(params), FlutterRouter.URL_ORDER: (pageName, params, _) => OrderWidget()});
    _isInitialized = true;
  }

  static Future<Map<String, dynamic>> open(String url, {Map<String, dynamic> urlParams, Map<String, dynamic> exts}) {
    return FlutterBoost.singleton.open(url, urlParams: urlParams, exts: exts);
  }

  static Future<Map<String, dynamic>> openInCurrentContainer(String url, {Map<String, dynamic> urlParams, Map<String, dynamic> exts}) {
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
