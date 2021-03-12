import 'dart:ui' as ui;

import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'package:lib_flutter_base/lib_flutter_base.dart';

import './case/flutter_to_flutter_sample.dart';
import './case/image_pick.dart';
import './case/media_query.dart';
import './case/return_data.dart';
import './case/willpop.dart';
import './flutter_page.dart';
import './simple_page_widgets.dart';
import './tab/simple_widget.dart';
import 'custom/settings/router/flutter_router.dart';


RouteObserver<PageRoute> routeObserver = RouteObserver<PageRoute>();

void main() {
  // https://flutter.dev/docs/development/add-to-app/debugging
  // https://flutter.cn/docs/development/add-to-app/debugging
  // flutter attach
  // flutter attach -d deviceId # AKC7N19118000852
  // flutter attach --isolate-filter='debug'
  ui.window.setIsolateDebugName("debug isolate");

  debugPaintSizeEnabled = false;
  debugPaintBaselinesEnabled = false;
  debugPrintLayouts = false;
  debugPaintLayerBordersEnabled = false;
  debugPaintPointersEnabled = false;
  debugRepaintRainbowEnabled = false;
  debugRepaintTextRainbowEnabled = false;

  Map<String, FlutterBoostRouteFactory> routerMap = {
    '/': (settings, uniqueId) {
      return PageRouteBuilder<dynamic>(
          settings: settings, pageBuilder: (_, __, ___) => Container(
        color: Colors.blue,
      ));
    },
    'embedded':  (settings, uniqueId) => PageRouteBuilder<dynamic>(settings: settings, pageBuilder: (_, __, ___) => EmbeddedFirstRouteWidget()),
    'presentFlutterPage': (settings, uniqueId) {
      return PageRouteBuilder<dynamic>(
          settings: settings,
          pageBuilder: (_, __, ___) => FlutterRouteWidget(
            params: settings.arguments,
            uniqueId: uniqueId,
          ));
    },
    'imagepick': (settings, uniqueId) {
      return PageRouteBuilder<dynamic>(
          settings: settings,
          pageBuilder: (_, __, ___) =>
              ImagePickerPage(title: "xxx", uniqueId: uniqueId));
    },
    'firstFirst': (settings, uniqueId) {
      return PageRouteBuilder<dynamic>(
          settings: settings,
          pageBuilder: (_, __, ___) => FirstFirstRouteWidget());
    },
    'willPop': (settings, uniqueId) {
      return PageRouteBuilder<dynamic>(
          settings: settings, pageBuilder: (_, __, ___) => WillPopRoute());
    },
    'returnData': (settings, uniqueId) {
      return PageRouteBuilder<dynamic>(
          settings: settings, pageBuilder: (_, __, ___) => ReturnDataWidget());
    },
    'secondStateful': (settings, uniqueId) {
      return PageRouteBuilder<dynamic>(
          settings: settings,
          pageBuilder: (_, __, ___) => SecondStatefulRouteWidget());
    },
    'platformView': (settings, uniqueId) {
      return PageRouteBuilder<dynamic>(
          settings: settings,
          pageBuilder: (_, __, ___) => PlatformRouteWidget());
    },

    ///可以在native层通过 getContainerParams 来传递参数
    'flutterPage': (settings, uniqueId) {
      print('flutterPage settings:$settings, uniqueId:$uniqueId');
      return PageRouteBuilder<dynamic>(
        settings: settings,
        pageBuilder: (_, __, ___) => FlutterRouteWidget(
          params: settings.arguments,
          uniqueId: uniqueId,
        ),
        // transitionsBuilder: (BuildContext context, Animation<double> animation,
        //     Animation<double> secondaryAnimation, Widget child) {
        //   return SlideTransition(
        //     position: Tween<Offset>(
        //       begin: const Offset(1.0, 0),
        //       end: Offset.zero,
        //     ).animate(animation),
        //     child: SlideTransition(
        //       position: Tween<Offset>(
        //         begin: Offset.zero,
        //         end: const Offset(-1.0, 0),
        //       ).animate(secondaryAnimation),
        //       child: child,
        //     ),
        //   );
        // },
      );
    },
    'tab_friend': (settings, uniqueId) {
      return PageRouteBuilder<dynamic>(
          settings: settings,
          pageBuilder: (_, __, ___) => SimpleWidget(
              uniqueId, settings.arguments, "This is a flutter fragment"));
    },
    'tab_message': (settings, uniqueId) {
      return PageRouteBuilder<dynamic>(
          settings: settings,
          pageBuilder: (_, __, ___) => SimpleWidget(
              uniqueId, settings.arguments, "This is a flutter fragment"));
    },
    'tab_flutter1': (settings, uniqueId) {
      return PageRouteBuilder<dynamic>(
          settings: settings,
          pageBuilder: (_, __, ___) => SimpleWidget(
              uniqueId, settings.arguments, "This is a custom FlutterView"));
    },
    'tab_flutter2': (settings, uniqueId) {
      return PageRouteBuilder<dynamic>(
          settings: settings,
          pageBuilder: (_, __, ___) => SimpleWidget(
              uniqueId, settings.arguments, "This is a custom FlutterView"));
    },

    'f2f_first': (settings, uniqueId) {
      return PageRouteBuilder<dynamic>(
          settings: settings, pageBuilder: (_, __, ___) => F2FFirstPage());
    },
    'f2f_second': (settings, uniqueId) {
      return PageRouteBuilder<dynamic>(
          settings: settings, pageBuilder: (_, __, ___) => F2FSecondPage());
    },
    'mediaquery': (settings, uniqueId) {
      return PageRouteBuilder<dynamic>(
          settings: settings,
          pageBuilder: (_, __, ___) => MediaQueryRouteWidget(
            params: settings.arguments,
            uniqueId: uniqueId,
          ));
    },
  };
  routerMap.addAll(FlutterRouter.getRouters1());
  routerMap.addAll(FlutterRouter.getRouters2());
  Widget appBuilder(Widget home) {
      return MaterialApp(
          home: home,
      );
  }
  Widget appBuilder2(Widget home) {
    return BaseApp(
      child: home,
    );
  }
  appRun(FlutterBoostApp(
    (RouteSettings settings, String uniqueId) {
      FlutterBoostRouteFactory func = routerMap[settings.name];
      if (func == null) {
        return null;
      }
      return func(settings, uniqueId);
    },
    observers: [routeObserver],
    appBuilder: (Widget home) {
      return appBuilder2(home);
    },
    initialRoute: "/",
  ));
}