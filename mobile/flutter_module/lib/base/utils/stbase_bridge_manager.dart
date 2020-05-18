import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class STBaseBridgeManager {
  static const TAG = "[FLUTTER BRIDGE]";

  static const CHANNEL_METHOD = const MethodChannel("smart.template.flutter/method");

  static void setMethodCallHandler(Future<dynamic> handler(MethodCall call)) {
    CHANNEL_METHOD.setMethodCallHandler(handler);
  }

  static Future<dynamic> invokeNativeMethod(String method, [dynamic arguments]) async {
    try {
      debugPrint("$TAG invokeNativeMethod start method=$method, arguments=$arguments");
      var result = await CHANNEL_METHOD.invokeMethod(method, arguments);
      debugPrint("$TAG invokeNativeMethod end result:$result");
      return result;
    } on PlatformException catch (error) {
      debugPrint("$TAG invokeNativeMethod failure error:$error");
    }
  }

  static Future<dynamic> open(String url) async {
    try {
      debugPrint("$TAG open start url=$url");
      var result = await invokeNativeMethod('open', {"url": url});
      debugPrint("$TAG open end result:$result");
      return result;
    } on PlatformException catch (error) {
      debugPrint("$TAG open failure error:$error");
    }
  }

  static Future<dynamic> close(String params) async {
    try {
      debugPrint("$TAG close start params=$params");
      var result = await invokeNativeMethod('close', params);
      debugPrint("$TAG close end result:$result");
      return result;
    } on PlatformException catch (error) {
      debugPrint("$TAG close failure error:$error");
    }
  }

  static Future<dynamic> showToast(String message) async {
    try {
      debugPrint("$TAG showToast start message=$message");
      var result = await invokeNativeMethod('showToast', {"message": message});
      debugPrint("$TAG showToast end result:$result");
      return result;
    } on PlatformException catch (error) {
      debugPrint("$TAG showToast failure error:$error");
    }
  }

  static Future<dynamic> put(String key, String value) async {
    try {
      debugPrint("$TAG put start key=$key value=$value");
      var result = await invokeNativeMethod('put', {"key": key, "value": value});
      debugPrint("$TAG put end result:$result");
      return result;
    } on PlatformException catch (error) {
      debugPrint("$TAG put failure error:$error");
    }
  }

  static Future<dynamic> get(String key) async {
    try {
      debugPrint("$TAG get start key=$key");
      var result = await invokeNativeMethod('get', {"key": key});
      debugPrint("$TAG get end result:$result");
      return result;
    } on PlatformException catch (error) {
      debugPrint("$TAG get failure error:$error");
    }
  }

  static Future<dynamic> getUserInfo() async {
    try {
      debugPrint("$TAG getUserInfo start");
      var result = await invokeNativeMethod('getUserInfo');
      debugPrint("$TAG getUserInfo end result:$result");
      return result;
    } on PlatformException catch (error) {
      debugPrint("$TAG getUserInfo failure error:$error");
    }
  }

  static Future<dynamic> getLocationInfo() async {
    try {
      debugPrint("$TAG getLocationInfo start");
      var result = await invokeNativeMethod('getLocationInfo');
      debugPrint("$TAG getLocationInfo end result:$result");
      return result;
    } on PlatformException catch (error) {
      debugPrint("$TAG getLocationInfo failure error:$error");
    }
  }

  static Future<dynamic> getDeviceInfo() async {
    try {
      debugPrint("$TAG getDeviceInfo start");
      var result = await invokeNativeMethod('getDeviceInfo');
      debugPrint("$TAG getDeviceInfo end result:$result");
      return result;
    } on PlatformException catch (error) {
      debugPrint("$TAG getDeviceInfo failure error:$error");
    }
  }
}
