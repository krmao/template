import 'base_bridge.dart';

class BaseBridgeAppInfo extends BaseBridge {
  static BaseAppInfo appInfo;

  static Future<BaseAppInfo> getAppInfo() async {
    Map appInfoMap =
        await BaseBridge.callNativeStatic("AppInfo", "getAppInfo", {});

    BaseAppInfo appInfo = BaseAppInfo();

    if (appInfoMap.containsKey("debug")) {
      appInfo.debug = appInfoMap['debug'];
    }
    if (appInfoMap.containsKey("deviceType")) {
      appInfo.deviceType = appInfoMap['deviceType'];
    }
    if (appInfoMap.containsKey("deviceName")) {
      appInfo.deviceName = appInfoMap['deviceName'];
    }
    if (appInfoMap.containsKey("versionName")) {
      appInfo.versionName = appInfoMap['versionName'];
    }
    if (appInfoMap.containsKey("pageInfo")) {
      Map pageInfoMap = appInfoMap['pageInfo'];

      if (pageInfoMap.containsKey("uniqueId")) {
        appInfo.pageInfo.uniqueId = pageInfoMap['uniqueId'];
      }
      if (pageInfoMap.containsKey("paramsJsonObjectString")) {
        appInfo.pageInfo.paramsJsonObjectString =
            pageInfoMap['paramsJsonObjectString'];
      }
    }
    BaseBridgeAppInfo.appInfo = appInfo;
    return appInfo;
  }

  @override
  String getPluginName() {
    return "AppInfo";
  }
}

class BaseAppInfo {
  BaseAppInfo();

  bool debug = true;

  String osVersion = "";
  String deviceType = "";
  String deviceName = "";
  String versionName = "";

  BasePageInfo pageInfo = BasePageInfo();
}

class BasePageInfo {
  BasePageInfo();

  String uniqueId;
  String paramsJsonObjectString;
}
