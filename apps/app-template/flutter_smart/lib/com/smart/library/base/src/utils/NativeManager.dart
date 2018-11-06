import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class NativeManager {
  // 开启flutter native 混合模式
  static var enableNative = true;
  static const platform = const MethodChannel('smart.flutter.io/methods');

  static void initialize() {
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

  static Future<dynamic> goTo(String pageName, String arguments) async {
    try {
      var result = await platform.invokeMethod('goTo', {pageName: pageName, arguments: arguments});
      print("[NativeManager] goTo result:$result");
      return result;
    } on PlatformException catch (e) {
      print("[NativeManager] goTo failure:${e.message}");
    }
  }

  static Future<dynamic> finish(dynamic arguments) async {
    try {
      var result = await platform.invokeMethod('finish', {arguments: arguments});
      print("[NativeManager] finish result:$result");
      return result;
    } on PlatformException catch (e) {
      print("[NativeManager] finish failure:${e.message}");
    }
  }

  static Future pop(BuildContext context, [dynamic result]) {
    print("flutter --> pop will pop with param:$result");
    var popSuccess = Navigator.pop(context, result);
    print("flutter --> pop popSuccess:$popSuccess");
    return NativeManager.finish("hehe").then((result) {
      print("flutter --> pop finish success result:$result");
    }).catchError((error) {
      print("flutter --> pop finish failure error:$error");
    });
  }
}
