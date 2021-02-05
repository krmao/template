import 'package:flutter/material.dart';
import 'flutter_boost/flutter_boost.dart';

import 'bridge.dart';

class PageBridge extends Bridge {
  static Future<bool> close({Map<String, dynamic> result, Map<String, dynamic> exts}) {
    return FlutterBoost.singleton.closeCurrent(result: result, exts: exts);
  }

  static String getContainerId(BuildContext context) {
    String containerId = BoostContainer.tryOf(context)?.uniqueId;
    return containerId ?? "";
  }

  static Future<T> enableExitWithDoubleBackPressed<T>(bool enable) async {
    return await Bridge.callNativeStatic("Page", "enableExitWithDoubleBackPressed", {"enable": enable});
  }

  @override
  String getPluginName() {
    return "Page";
  }
}
