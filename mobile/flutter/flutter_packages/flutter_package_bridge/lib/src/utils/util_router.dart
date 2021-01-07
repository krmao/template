import 'package:flutter/widgets.dart';
import 'package:flutter_boost/flutter_boost.dart';

class RouterUtil {
  static Map<String, PageBuilder> wrapRouters(Map<String, Map<Object, PageBuilder>> namedRouters) {
    Map<String, PageBuilder> routers = {};
    namedRouters.values.forEach((element) {
      element.forEach((key, value) {
        if (key is PageConfigItem) {
          routers[key.routerPath] = value;
        } else {
          routers[key.toString()] = value;
        }
      });
    });
    return routers;
  }

  static Future<Map<String, dynamic>> open(String url, {Map<String, dynamic> urlParams, Map<String, dynamic> exts}) {
    return FlutterBoost.singleton.open(url, urlParams: urlParams, exts: exts);
  }

  /// 有 bug, 暂时不起作用
  // ignore: unused_element
  static Future<Map<String, dynamic>> _openInCurrentContainer(String url, {Map<String, dynamic> urlParams, Map<String, dynamic> exts}) {
    return FlutterBoost.singleton.openInCurrentContainer(url, urlParams: urlParams, exts: exts);
  }

  static Future<bool> close(String id, {Map<String, dynamic> result, Map<String, dynamic> exts}) {
    return FlutterBoost.singleton.close(id, result: result, exts: exts);
  }

  static bool closeInCurrentContainer<T extends Object>(T result) {
    return FlutterBoost.singleton.closeInCurrentContainer(result);
  }

  static Future<bool> closeCurrent({Map<String, dynamic> result, Map<String, dynamic> exts}) {
    return FlutterBoost.singleton.closeCurrent(result: result, exts: exts);
  }

  static Future<bool> closeByContext(BuildContext context, {Map<String, dynamic> result, Map<String, dynamic> exts}) {
    return FlutterBoost.singleton.closeByContext(context, result: result, exts: exts);
  }
}

class PageConfigItem {
  String desc;
  String routerPath;
  String url;

  PageConfigItem(this.desc, this.routerPath, {this.url = ""});
}
