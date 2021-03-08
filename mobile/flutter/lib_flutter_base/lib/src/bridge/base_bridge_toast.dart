import 'base_bridge.dart';

class Toast extends BaseBridge {
  static Future<T> show<T>(String message) async {
    return await BaseBridge.callNativeStatic(
        "Toast", "show", {"message": message});
  }

  @override
  String getPluginName() {
    return "Toast";
  }
}
