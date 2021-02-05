import 'bridge.dart';

class URL extends Bridge {
  static Future<T> openURL<T>(String url, {Map<dynamic, dynamic> urlParams, Map<dynamic, dynamic> exts}) async {
    return await Bridge.callNativeStatic("URL", "openURL", {"url": url, "urlParams": urlParams, 'exts': exts});
  }

  @override
  String getPluginName() {
    return "URL";
  }
}
