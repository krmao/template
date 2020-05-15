import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class STBaseNativeManager {
  // 开启flutter native 混合模式
  static const TAG = "[flutter]";

  static final List<WidgetsBackPressedEvent> onBackPressedEventList = <WidgetsBackPressedEvent>[];
  static var enableNative = true;
  static const methodChannel = MethodChannel('smart.flutter.io/methods');
  static NavigatorState navigatorState;

  // init at widget which parent state is MaterialApp
  static void initialize(BuildContext context) {
    if (navigatorState == null) {
      debugPrint("$TAG initialize context=$context");

      try {
        debugPrint("$TAG Navigator.of(context, rootNavigator=true, nullOk=false)=" + (Navigator.of(context, rootNavigator: true, nullOk: false)).toString());
        if (navigatorState == null) navigatorState = Navigator.of(context, rootNavigator: true, nullOk: false);
      } catch (e) {
        debugPrint("$TAG error1 " + e.toString());
      }
      try {
        if (navigatorState == null) {
          debugPrint("$TAG Navigator.of(context, rootNavigator=false, nullOk=false)=" + (Navigator.of(context, rootNavigator: false, nullOk: false)).toString());
          navigatorState = Navigator.of(context, rootNavigator: false, nullOk: false);
        }
      } catch (e) {
        debugPrint("$TAG error2 " + e.toString());
      }
      try {
        if (navigatorState == null) {
          debugPrint("$TAG Navigator.of(context, rootNavigator=false, nullOk=true)=" + (Navigator.of(context, rootNavigator: false, nullOk: true)).toString());
          navigatorState = Navigator.of(context, rootNavigator: false, nullOk: true);
        }
      } catch (e) {
        debugPrint("$TAG error3 " + e.toString());
      }

      navigatorState = Navigator.of(context);
      methodChannel.setMethodCallHandler((MethodCall call) {
        debugPrint("$TAG onMethodCall ${call.method} ${call.arguments}");
        switch (call.method) {
          case "pop":
            {
              return STBaseNativeManager.finish(call.arguments).then((result) {});
            }
          case "backPressed":
            {
              handlePopRoute();
              break;
            }
          default:
            {
              return Future.error(null);
            }
        }
      });
    }

    if (navigatorState == null) {
      throw Exception("must be init success at first time and right place");
    }
  }

  static void handlePopRoute() {
    if (!onBackPressedEventList.last.onBackPressed()) {
      debugPrint("$TAG handlePopRoute SystemNavigator.pop()");
      // SystemNavigator.pop();
      finish();
    } else {
      debugPrint("$TAG handlePopRoute onBackPressed intercept");
    }
  }

  static Future<dynamic> invokeNativeBeforeGoTo() async {
    try {
      debugPrint("${STBaseNativeManager.TAG} will beforeGoTo methodChannel==null?${methodChannel == null}");
      var result = await methodChannel.invokeMethod('beforeGoTo');
      debugPrint("${STBaseNativeManager.TAG} did beforeGoTo with result:$result");
      return result;
    } on PlatformException catch (error) {
      debugPrint("${STBaseNativeManager.TAG} beforeGoTo failure with error:$error");
    }
  }

  static Future<dynamic> invokeNativeGoTo(String pageName, String arguments) async {
    try {
      debugPrint("${STBaseNativeManager.TAG} will goTo with arguments:$arguments methodChannel==null?${methodChannel == null}");
      var result = await methodChannel.invokeMethod('goTo', {pageName: pageName, arguments: arguments});
      debugPrint("${STBaseNativeManager.TAG} did goTo with result:$result");
      return result;
    } on PlatformException catch (error) {
      debugPrint("${STBaseNativeManager.TAG} goTo failure with error:$error");
    }
  }

  static Future<dynamic> invokeNativeGoToNative(String pageName, String arguments) async {
    try {
      debugPrint("${STBaseNativeManager.TAG} will goTo with arguments:$arguments methodChannel==null?${methodChannel == null}");
      var result = await methodChannel.invokeMethod('goToNative', {"pageName": pageName, "arguments": arguments});
      debugPrint("${STBaseNativeManager.TAG} did goTo with result:$result");
      return result;
    } on PlatformException catch (error) {
      debugPrint("${STBaseNativeManager.TAG} goTo failure with error:$error");
    }
  }

  /*static Future goTo(Widget toPage, {bool ensureLogin = false, bool animation = true}) {
    var navigator = navigatorState;
    print("${NativeManager.TAG} goTo navigator=$navigator");
    */ /*if (ensureLogin) {
      UserManager.ensureLogin(context).then((userModel) {
        Navigator.push(context, animation ? CupertinoPageRoute(builder: (_) => toPage) : NoAnimationRoute(builder: (_) => toPage));
      }).catchError((error) {});
    } else {*/ /*
    // Navigator.push(context, animation ? CupertinoPageRoute(builder: (_) => toPage) : NoAnimationRoute(builder: (_) => toPage));
    return NativeManager.invokeNativeBeforeGoTo().then((value) {
      print("${NativeManager.TAG} goTo will push ${toPage.toStringShort()}");
      navigator.push(NoAnimationRoute(builder: (_) => toPage));
      print("${NativeManager.TAG} goTo did push ${toPage.toStringShort()}");
      print("${NativeManager.TAG} goTo will start new activity");
      NativeManager.invokeNativeGoTo(toPage.toStringShort(), "haha").then((result) {
        print("${NativeManager.TAG} goTo did start new activity with result:$result");
      }).catchError((error) {
        print("${NativeManager.TAG} goTo start new activity failure with error:$error");
      });
    });
  }*/

  static Future goTo(Widget toPage, {bool ensureLogin = false, bool animation = true}) {
    var navigator = navigatorState;

    if (!enableNative) {
      navigator.push(NoAnimationRoute(builder: (_) => toPage));
      return Future.value(null);
    }

    print("${STBaseNativeManager.TAG} goTo navigator=$navigator");
    return STBaseNativeManager.invokeNativeBeforeGoTo().then((value) {
      print("${STBaseNativeManager.TAG} goTo will push ${toPage.toStringShort()}");
      navigator.push(NoAnimationRoute(builder: (_) => toPage));
      print("${STBaseNativeManager.TAG} goTo did push ${toPage.toStringShort()}");
      print("${STBaseNativeManager.TAG} goTo will start new activity");
      STBaseNativeManager.invokeNativeGoTo(toPage.toStringShort(), "haha").then((result) {
        print("${STBaseNativeManager.TAG} goTo did start new activity with result:$result");
      }).catchError((error) {
        print("${STBaseNativeManager.TAG} goTo start new activity failure with error:$error");
      });
    });
  }

  static Future<Null> invokeNativeWillFinish() async {
    try {
      debugPrint("${STBaseNativeManager.TAG} will willFinish methodChannel==null?${methodChannel == null}");
      var finishResult = await methodChannel.invokeMethod('willFinish');
      debugPrint("${STBaseNativeManager.TAG} did willFinish with finishResult:$finishResult");
    } on PlatformException catch (error) {
      debugPrint("${STBaseNativeManager.TAG} finish willFinish with error:$error");
    }
  }

  static Future<Null> invokeNativeFinish(dynamic arguments) async {
    try {
      debugPrint("${STBaseNativeManager.TAG} will finish with arguments:$arguments methodChannel==null?${methodChannel == null}");
      var finishResult = await methodChannel.invokeMethod('finish', {arguments: arguments});
      debugPrint("${STBaseNativeManager.TAG} did finish with finishResult:$finishResult");
    } on PlatformException catch (error) {
      debugPrint("${STBaseNativeManager.TAG} finish failure with error:$error");
    }
  }

  static Future<Null> finish([dynamic arguments]) {
    var navigator = navigatorState;

    if (!enableNative) {
      navigator.pop(arguments);
      return Future.value(null);
    }

    debugPrint("${STBaseNativeManager.TAG} do willFinish... methodChannel==null?${methodChannel == null} navigator==null?${navigator == null}");
    return invokeNativeWillFinish().then((value) {
      debugPrint("${STBaseNativeManager.TAG} after will finish and check canPop ... methodChannel==null?${methodChannel == null} navigator==null?${navigator == null}");
      var canPop = navigator.canPop();
      debugPrint("${STBaseNativeManager.TAG} canPop?$canPop, arguments:$arguments navigator==null?${navigator == null}");
      if (canPop) {
        debugPrint("${STBaseNativeManager.TAG} will pop navigator==null?${navigator == null}");
        navigatorState.pop(arguments);
        debugPrint("${STBaseNativeManager.TAG} pop did success navigator==null?${navigator == null}");
      }
      /*if (popSuccess) {*/
      STBaseNativeManager.invokeNativeFinish("hehe").then((result) {
        debugPrint("${STBaseNativeManager.TAG} finish success with result:$result navigator==null?${navigator == null}");
      }).catchError((error) {
        debugPrint("${STBaseNativeManager.TAG} finish failure with error:$error navigator==null?${navigator == null}");
      });
      /*}*/
      /*} else {
        debugPrint("${NativeManager.TAG} pop failure, canPop=false navigator==null?${navigator == null}");
      }*/
    });
  }
}

class NoAnimationRoute<T> extends MaterialPageRoute<T> {
  NoAnimationRoute({WidgetBuilder builder, RouteSettings settings}) : super(builder: builder, settings: settings);

  @override
  TickerFuture didPush() {
    return super.didPush();
  }

  @override
  Widget buildTransitions(BuildContext context, Animation<double> animation, Animation<double> secondaryAnimation, Widget child) => child; // FadeTransition(opacity: animation, child: child)
}

abstract class WidgetsBackPressedEvent {
  bool onBackPressed();
}