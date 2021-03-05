import 'bridge.dart';

class Toast extends Bridge {
  static Future<T> show<T>(String message) async {
    return await Bridge.callNativeStatic("Toast", "show", {"message": message});
  }

  @override
  String getPluginName() {
    return "Toast";
  }
}
