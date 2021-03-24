import 'base_bridge.dart';

class BaseBridgeEnv extends BaseBridge {
  static Future<String> getEnv() async {
    Map envData = await BaseBridge.callNativeStatic("Env", "getEnv", {});
    return envData["envType"];
  }

  static Future<String> getNetworkType() async {
    Map envData =
        await BaseBridge.callNativeStatic("Env", "getNetworkType", {});
    return envData["networkType"];
  }

  @override
  String getPluginName() {
    return "Env";
  }
}
