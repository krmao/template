import 'bridge.dart';

class Env extends Bridge {
  static Future<String> getEnv() async {
    Map envData = await Bridge.callNativeStatic("Env", "getEnv", {});
    return envData["envType"];
  }

  static Future<String> getNetworkType() async {
    Map envData = await Bridge.callNativeStatic("Env", "getNetworkType", {});
    return envData["networkType"];
  }

  @override
  String getPluginName() {
    return "Env";
  }
}
