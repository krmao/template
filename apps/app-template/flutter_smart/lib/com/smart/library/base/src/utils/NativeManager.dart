import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class NativeManager {
  // 开启flutter native 混合模式
  static const TAG = "[flutter]";

  static var enableNative = true;

  static MethodChannel createMethodChannel(BuildContext context) {
    const methodChannel = MethodChannel('smart.flutter.io/methods');

    methodChannel.setMethodCallHandler((MethodCall call) {
      switch (call.method) {
        case "pop":
          {
            return pop(methodChannel, context, call.arguments).then((result) {});
          }
        default:
          {
            return Future.error(null);
          }
      }
    });
    return methodChannel;
  }

  static Future<dynamic> beforeGoTo(MethodChannel methodChannel) async {
    try {
      print("${NativeManager.TAG} will beforeGoTo");
      var result = await methodChannel.invokeMethod('beforeGoTo');
      print("${NativeManager.TAG} did beforeGoTo with result:$result");
      return result;
    } on PlatformException catch (error) {
      print("${NativeManager.TAG} beforeGoTo failure with error:$error");
    }
  }

  static Future<dynamic> goTo(MethodChannel methodChannel, BuildContext context, String pageName, String arguments) async {
    try {
      print("${NativeManager.TAG} will goTo with arguments:$arguments");
      var result = await methodChannel.invokeMethod('goTo', {pageName: pageName, arguments: arguments});
      print("${NativeManager.TAG} did goTo with result:$result");
      return result;
    } on PlatformException catch (error) {
      print("${NativeManager.TAG} goTo failure with error:$error");
    }
  }

  static Future<Null> willFinish(MethodChannel methodChannel) async {
    try {
      print("${NativeManager.TAG} will willFinish");
      var finishResult = await methodChannel.invokeMethod('willFinish');
      print("${NativeManager.TAG} did willFinish with finishResult:$finishResult");
    } on PlatformException catch (error) {
      print("${NativeManager.TAG} finish willFinish with error:$error");
    }
  }

  static Future<Null> finish(MethodChannel methodChannel, dynamic arguments) async {
    try {
      print("${NativeManager.TAG} will finish with arguments:$arguments");
      var finishResult = await methodChannel.invokeMethod('finish', {arguments: arguments});
      print("${NativeManager.TAG} did finish with finishResult:$finishResult");
    } on PlatformException catch (error) {
      print("${NativeManager.TAG} finish failure with error:$error");
    }
  }

  static Future<Null> pop(MethodChannel methodChannel, BuildContext context, [dynamic arguments]) {
    return willFinish(methodChannel).then((value) {
      var canPop = Navigator.canPop(context);
      print("${NativeManager.TAG} canPop?$canPop, arguments:$arguments, context:$context");
      if (canPop) {
        print("${NativeManager.TAG} will pop");
        var popSuccess = Navigator.pop(context, arguments);
        print("${NativeManager.TAG} pop did success?$popSuccess");
        if (popSuccess) {
          NativeManager.finish(methodChannel, "hehe").then((result) {
            print("${NativeManager.TAG} finish success with result:$result");
          }).catchError((error) {
            print("${NativeManager.TAG} finish failure with error:$error");
          });
        }
      } else {
        print("${NativeManager.TAG} pop failure, canPop=false");
      }
    });
  }
}
