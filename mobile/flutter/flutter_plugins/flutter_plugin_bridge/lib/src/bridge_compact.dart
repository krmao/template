import 'dart:convert';

import 'bridge.dart';

class BridgeCompact extends Bridge {
  static Future<dynamic> open(String url) async {
    return await _callNativeStatic("open", {"url": url});
  }

  static Future<dynamic> close(dynamic result) async {
    return await _callNativeStatic("close", {"result": result});
  }

  static Future<dynamic> showToast(String message) async {
    return await _callNativeStatic("showToast", {"message": message});
  }

  static Future<dynamic> put(String key, dynamic value) async {
    return await _callNativeStatic("put", {"key": key, "value": value});
  }

  static Future<dynamic> get(String key) async {
    return await _callNativeStatic("get", {"key": key});
  }

  static Future<dynamic> getUserInfo() async {
    return await _callNativeStatic("getUserInfo", {});
  }

  static Future<dynamic> getLocationInfo() async {
    return await _callNativeStatic("getLocationInfo", {});
  }

  static Future<dynamic> getDeviceInfo() async {
    return await _callNativeStatic("getDeviceInfo", {});
  }

  static Future<dynamic> _callNativeStatic(String functionName, Object paramsJsonObject) async {
    return await Bridge.callNativeStatic("BridgeCompact", "callNative", {"functionName": functionName, "params": jsonEncode(paramsJsonObject)});
  }

  @override
  String getPluginName() {
    return "BridgeCompact";
  }
}
