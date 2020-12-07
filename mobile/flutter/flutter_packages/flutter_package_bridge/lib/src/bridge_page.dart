import 'package:flutter/material.dart';
import 'package:flutter_boost/flutter_boost.dart';

import 'bridge.dart';

class PageBridge extends Bridge {
  static Future<bool> close({Map<String, dynamic> result, Map<String, dynamic> exts}) {
    return FlutterBoost.singleton.closeCurrent(result: result, exts: exts);
  }

  static String getContainerId(BuildContext context) {
    String containerId = BoostContainer.tryOf(context)?.uniqueId;
    return containerId ?? "";
  }

  @override
  String getPluginName() {
    return "Page";
  }
}