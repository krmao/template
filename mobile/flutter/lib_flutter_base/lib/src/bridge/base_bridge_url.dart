import 'base_bridge.dart';

class URL extends BaseBridge {
  static Future<T> openURL<T>(String url,
      {Map<dynamic, dynamic> urlParams, Map<dynamic, dynamic> exts}) async {
    return await BaseBridge.callNativeStatic(
        "URL", "openURL", {"url": url, "urlParams": urlParams, 'exts': exts});
  }

  @override
  String getPluginName() {
    return "URL";
  }
}
