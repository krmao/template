import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class NativeManager {
  // 开启flutter native 混合模式
  static const TAG = "[flutter]";
  static var enableNative = true;
  static const platform = const MethodChannel('smart.flutter.io/methods');

  static void resetMethodCallHandler(BuildContext context) {
    platform.setMethodCallHandler((MethodCall call) {
      switch (call.method) {
        case "pop":
          {
            return finish(call.arguments).then((result) {});
          }
        default:
          {
            return Future.error(null);
          }
      }
    });
  }

  static Future<dynamic> beforeGoTo() async {
    try {
      print("${NativeManager.TAG} will beforeGoTo");
      var result = await platform.invokeMethod('beforeGoTo');
      print("${NativeManager.TAG} did beforeGoTo with result:$result");
      return result;
    } on PlatformException catch (error) {
      print("${NativeManager.TAG} beforeGoTo failure with error:$error");
    }
  }

  static Future<dynamic> goTo(BuildContext context, String pageName, String arguments) async {
    try {
      print("${NativeManager.TAG} will goTo with arguments:$arguments");
      var result = await platform.invokeMethod('goTo', {pageName: pageName, arguments: arguments});
      print("${NativeManager.TAG} did goTo with result:$result");
      return result;
    } on PlatformException catch (error) {
      print("${NativeManager.TAG} goTo failure with error:$error");
    }
  }

  static Future<Null> willFinish() async {
    try {
      print("${NativeManager.TAG} will willFinish");
      var finishResult = await platform.invokeMethod('willFinish');
      print("${NativeManager.TAG} did willFinish with finishResult:$finishResult");
    } on PlatformException catch (error) {
      print("${NativeManager.TAG} finish willFinish with error:$error");
    }
  }

  static Future<Null> finish(dynamic arguments) async {
    try {
      print("${NativeManager.TAG} will finish with arguments:$arguments");
      var finishResult = await platform.invokeMethod('finish', {arguments: arguments});
      print("${NativeManager.TAG} did finish with finishResult:$finishResult");
    } on PlatformException catch (error) {
      print("${NativeManager.TAG} finish failure with error:$error");
    }
  }

  static Future<Null> pop(BuildContext context, [dynamic arguments]) {
    return willFinish().then((value) {
      print("${NativeManager.TAG} will pop with arguments:$arguments");
      var popSuccess = Navigator.pop(context, arguments);
      print("${NativeManager.TAG} pop did success?$popSuccess");
      if (popSuccess) {
        NativeManager.finish("hehe").then((result) {
          print("${NativeManager.TAG} finish success with result:$result");
        }).catchError((error) {
          print("${NativeManager.TAG} finish failure with error:$error");
        });
      }
    });
  }
}
