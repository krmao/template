import 'base_bridge.dart';

class BaseBridgeApplication extends BaseBridge {
  static bool debug = true;

  static String versionName;
  static String versionCode;

  static Map deviceInfo;
  static Map configInfo;

  static Future<Map> getApplicationConstants() async {
    Map applicationConstants = await BaseBridge.callNativeStatic(
        "Application", "getApplicationConstants", {});
    if (applicationConstants.containsKey("applicationInfo")) {
      Map applicationInfo = applicationConstants["applicationInfo"];
      BaseBridgeApplication.debug = applicationInfo["debug"];
      BaseBridgeApplication.versionCode = applicationInfo["versionCode"];
      BaseBridgeApplication.versionName = applicationInfo["versionName"];
    }

    if (applicationConstants.containsKey("deviceInfo")) {
      deviceInfo = applicationConstants['deviceInfo'];
    }

    if (applicationConstants.containsKey("configInfo")) {
      configInfo = applicationConstants['configInfo'];
    }
    return applicationConstants;
  }

  @override
  String getPluginName() {
    return "Application";
  }
}
