import 'base_bridge.dart';

class PageBridge extends BaseBridge {
  static Future<T> enableExitWithDoubleBackPressed<T>(bool enable) async {
    return await BaseBridge.callNativeStatic(
        "Page", "enableExitWithDoubleBackPressed", {"enable": enable});
  }

  @override
  String getPluginName() {
    return "Page";
  }
}
