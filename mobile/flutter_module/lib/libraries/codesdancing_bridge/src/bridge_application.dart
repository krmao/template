import 'bridge.dart';

class Application extends Bridge {
  static bool debug = true;

  static String versionName;
  static String versionCode;

  static Map deviceInfo;
  static Map configInfo;

  static Future<Map> getApplicationConstants() async {
    Map applicationConstants = await Bridge.callNativeStatic("Application", "getApplicationConstants", {});
    if (applicationConstants.containsKey("applicationInfo")) {
      Map applicationInfo = applicationConstants["applicationInfo"];
      Application.debug = applicationInfo["debug"];
      Application.versionCode = applicationInfo["versionCode"];
      Application.versionName = applicationInfo["versionName"];
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
