import 'bridge.dart';

class PageBridge extends Bridge {
  static Future<T> enableExitWithDoubleBackPressed<T>(bool enable) async {
    return await Bridge.callNativeStatic(
        "Page", "enableExitWithDoubleBackPressed", {"enable": enable});
  }

  @override
  String getPluginName() {
    return "Page";
  }
}
