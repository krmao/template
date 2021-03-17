//import 'package:flutter/cupertino.dart';

import 'dart:async';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:lib_flutter_base/lib_flutter_base.dart';

import 'base_app_constants.dart';
import 'bridge/base_bridge_application.dart';

typedef void OnInitStateCallback();

class BaseApp extends StatefulWidget {
  final OnInitStateCallback onInitStateCallback;
  final Map<String, WidgetBuilder> routes;

  BaseApp({Key key, @required this.routes, this.onInitStateCallback})
      : super(key: key);

  @override
  State<StatefulWidget> createState() => BaseAppState(
      routes: this.routes, onInitStateCallback: this.onInitStateCallback);
}

class BaseAppState extends State<BaseApp> {
  final Map<String, WidgetBuilder> routes;
  final Color statusBarColor;
  final OnInitStateCallback onInitStateCallback;

  BaseAppState(
      {@required this.routes,
      this.statusBarColor = BaseAppConstants.DEFAULT_STATUS_BAR_COLOR,
      this.onInitStateCallback})
      : super();

  @override
  void initState() {
    super.initState();
    if (this.onInitStateCallback != null) this.onInitStateCallback();
  }

  /// flutter_boost no need config about onUnknownRoute/initialRoute
  @override
  Widget build(BuildContext context) {
    SystemChrome.setSystemUIOverlayStyle(SystemUiOverlayStyle(
      statusBarColor: Colors.transparent, // android >= M
      statusBarBrightness: Brightness.dark, // ios
      statusBarIconBrightness: Brightness.light, // android >= M
    ));
    return MaterialApp(
        localizationsDelegates: [DefaultMaterialLocalizations.delegate],
        routes: this.routes,
        theme: ThemeData(
            // This is the theme of your application.
            //
            // Try running your application with "flutter run". You'll see the
            // application has a blue toolbar. Then, without quitting the app, try
            // changing the primarySwatch below to Colors.green and then invoke
            // "hot reload" (press "r" in the console where you ran "flutter run",
            // or simply save your changes to "hot reload" in a Flutter IDE).
            // Notice that the counter didn't reset back to zero; the application
            // is not restarted.
            primarySwatch: Colors.blue,
            // This makes the visual density adapt to the platform that you run
            // the app on. For desktop platforms, the controls will be smaller and
            // closer together (more dense) than on mobile platforms.
            visualDensity: VisualDensity.adaptivePlatformDensity,
            primaryColor: BaseAppConstants.PRIMARY_COLOR,
            accentColor: BaseAppConstants.ACCENT_COLOR,
            primaryColorBrightness: Brightness.light,
            hintColor: BaseAppConstants.HINT_COLOR,
            highlightColor: BaseAppConstants.HIGHLIGHT_COLOR,
            inputDecorationTheme: InputDecorationTheme(
                labelStyle: TextStyle(
                    color: BaseAppConstants.INPUT_DECORATION_COLOR))));
  }
}

typedef RoutesBuilder = Map<String, WidgetBuilder> Function(
    String uniqueId, String argumentsJsonString);

void appRun(RoutesBuilder routesBuilder,
    {OnInitStateCallback onInitStateCallback}) {
  FlutterError.onError = (FlutterErrorDetails details) async {
    Zone.current.handleUncaughtError(details.exception, details.stack);
  };

  runZoned<Future<Null>>(() async {
    WidgetsBinding widgetsBinding = WidgetsFlutterBinding.ensureInitialized();
    BaseBridgeApplication.getApplicationConstants().then((value) {
      String argumentsJsonString = value['argumentsJsonString'];
      String uniqueId = value['uniqueId'];

      print(
          "[page] runZoned-getApplicationConstants value=$value, argumentsJsonString=$argumentsJsonString");
      // ignore: invalid_use_of_protected_member
      widgetsBinding.scheduleAttachRootWidget(BaseApp(
        routes: routesBuilder(uniqueId, argumentsJsonString),
        onInitStateCallback: () {
          if (onInitStateCallback != null) onInitStateCallback();
        },
      ));
      widgetsBinding.scheduleWarmUpFrame();
    });
    // ignore: deprecated_member_use
  }, onError: (error, stackTrace) async {
    print(error);
    print(stackTrace);
  }, zoneSpecification: ZoneSpecification(
      print: (Zone self, ZoneDelegate parent, Zone zone, String line) {
    try {
      if (BaseBridgeApplication.debug) {
        parent.print(zone, line);
      }
    } catch (e, _) {
      parent.print(zone, e);
    }
  }));
}
