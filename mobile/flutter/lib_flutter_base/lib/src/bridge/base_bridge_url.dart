import 'base_bridge.dart';

class URL extends BaseBridge {
  static Future<T> openURL<T>(String url) async {
    return await BaseBridge.callNativeStatic("URL", "openURL", {"url": url});
  }

  @override
  String getPluginName() {
    return "URL";
  }
}