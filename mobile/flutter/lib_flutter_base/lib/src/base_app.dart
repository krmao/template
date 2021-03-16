//import 'package:flutter/cupertino.dart';

import 'dart:async';
import 'dart:convert';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:lib_flutter_base/lib_flutter_base.dart';

import 'base_app_constants.dart';
import 'base_state_default.dart';
import 'bridge/base_bridge_application.dart';
import 'bridge/base_bridge_url.dart';

typedef void OnInitStateCallback();

class BaseApp extends StatefulWidget {
  final Widget child;
  final OnInitStateCallback onInitStateCallback;

  BaseApp({Key key, @required this.child, this.onInitStateCallback})
      : super(key: key);

  @override
  State<StatefulWidget> createState() => BaseAppState(
      child: this.child, onInitStateCallback: this.onInitStateCallback);
}

class BaseAppState extends State<BaseApp> {
  final Widget child;
  final Color statusBarColor;
  final OnInitStateCallback onInitStateCallback;

  BaseAppState(
      {@required this.child,
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
        home: this.child,
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

// ignore: unused_element
class _NoOverScrollBehavior extends ScrollBehavior {
  @override
  Widget buildViewportChrome(
      BuildContext context, Widget child, AxisDirection axisDirection) {
    return child;
  }
}

//region 阻止控制台打印太多无效信息
class _GlobalErrorStatus {
  // ignore: non_constant_identifier_names
  static int GLOBAL_ERROR_BOOM_MAX_COUNT = 3;

  String _errorStr;
  String _stackTraceStr;
  int _count = 0;

  bool isGlobalErrorBoom(Object error, StackTrace stackTrace) {
    String newErrorStr = error?.toString();
    String newStackTraceStr = stackTrace?.toString();
    if (newErrorStr == _errorStr && newStackTraceStr == _stackTraceStr) {
      _count++;
    } else {
      _count = 0;
    }
    _errorStr = newErrorStr;
    _stackTraceStr = newStackTraceStr;
    return _count >= GLOBAL_ERROR_BOOM_MAX_COUNT;
  }
}

_GlobalErrorStatus _globalErrorStatus = _GlobalErrorStatus();

/// 全局错误捕获
void _onZoneGlobalError(Object error, StackTrace stackTrace) {
  // log console
  print(error);
  print(stackTrace);

  if (_globalErrorStatus.isGlobalErrorBoom(error, stackTrace)) {
    print("error count boom, just skip");
    return;
  }

  // send log to UBT
  var errorMsg = error.toString();
  var stackTraceStr = stackTrace.toString();
  if (BaseBridgeApplication.debug) {
    // if (BoostNavigator.of().getTopPageInfo().pageName == '_error_page') {
    //   BoostNavigator.of().pop();
    // }

    // show error page
    URL.openURL<dynamic>('smart://template/flutter?page=_error_page&params=' +
        json.encode({
          "error": errorMsg,
          "stacktrace": stackTraceStr,
        }));
  }
}
//endregion

// ignore: must_be_immutable
class ErrorApp extends StatelessWidget {
  dynamic errorStack;
  dynamic error;
  dynamic containerInfoMap;

  ErrorApp(this.error, this.errorStack, this.containerInfoMap);

  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
        title: 'Flutter Error',
        theme: new ThemeData(primarySwatch: Colors.red),
        home: Scaffold(
            body: Container(
                color: Colors.red,
                width: double.infinity,
                height: double.infinity,
                padding: EdgeInsets.all(15),
                child: SingleChildScrollView(
                    child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        mainAxisSize: MainAxisSize.max,
                        children: <Widget>[
                      Row(children: <Widget>[
                        Text("ErrorMessage",
                            style: TextStyle(
                              fontSize: 16,
                              fontStyle: FontStyle.italic,
                              fontWeight: FontWeight.bold,
                              color: Colors.white,
                            ),
                            textAlign: TextAlign.left)
                      ]),
                      Text(error.toString(),
                          style: TextStyle(
                              fontSize: 16,
                              color: Colors.white,
                              fontWeight: FontWeight.bold)),
                      Padding(
                          padding: EdgeInsets.only(top: 15),
                          child: Text("ContainerInfo",
                              style: TextStyle(
                                fontSize: 16,
                                fontStyle: FontStyle.italic,
                                fontWeight: FontWeight.bold,
                                color: Colors.white,
                              ),
                              textAlign: TextAlign.left)),
                      Text(containerInfoMap?.toString() ?? "EMPTY",
                          style: TextStyle(fontSize: 14, color: Colors.white)),
                      Padding(
                          padding: EdgeInsets.only(top: 15),
                          child: Text("StackTrace",
                              style: TextStyle(
                                fontSize: 16,
                                fontStyle: FontStyle.italic,
                                fontWeight: FontWeight.bold,
                                color: Colors.white,
                              ),
                              textAlign: TextAlign.left)),
                      Text(errorStack.toString(),
                          style: TextStyle(fontSize: 14, color: Colors.white))
                    ])))));
  }
}

class PageNotFoundHomePage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => _PageNotFoundHomeState();
}

class _PageNotFoundHomeState extends BaseStateDefault<PageNotFoundHomePage> {
  _PageNotFoundHomeState();

  @override
  Widget buildBaseChild(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('页面不存在')),
      body: Center(
          child: Container(
        padding: EdgeInsets.fromLTRB(0, 130, 0, 0),
        child: Column(
          children: <Widget>[
            Container(
              margin: EdgeInsets.fromLTRB(0, 10, 0, 0),
              child: RaisedButton(
                child: const Text('页面不存在, 点击返回'),
                color: Colors.blue,
                textColor: Colors.white,
                onPressed: () {
                  PageBridge.popPage();
                },
              ),
            )
          ],
        ),
      )),
    );
  }
}

void appRun(app, {OnInitStateCallback onInitStateCallback}) {
  FlutterError.onError = (FlutterErrorDetails details) async {
    Zone.current.handleUncaughtError(details.exception, details.stack);
  };
  /*runZoned(
        () => runApp(
          App(
            enableSafeArea: false,
            statusBarColor: Constants.DEFAULT_STATUS_BAR_COLOR,
            child: app,
            onInitStateCallback: () {
              onInitStateCallback();
              TopRouterProvider finalRouterProvider = _mixGlobalRouters(routerProvider);
              if (finalRouterProvider != null) {
                FlutterBoost.singleton.registerPageBuilders(finalRouterProvider());
              }
            },
          ),
    ),
    zoneSpecification: ZoneSpecification(
      print: (Zone self, ZoneDelegate parent, Zone zone, String line) {
        try {
          if (Application.debug) {
            parent.print(zone, line);
          }
        } catch (e, stack) {
          parent.print(zone, e);
        }
      },
    ),
    onError: (Object error, StackTrace stackTrace) {
      _onZoneGlobalError(error, stackTrace);
    },
  );*/

  runZoned<Future<Null>>(() async {
    WidgetsBinding widgetsBinding = WidgetsFlutterBinding.ensureInitialized();
    BaseBridgeApplication.getApplicationConstants().then((value) {
      // ignore: invalid_use_of_protected_member
      widgetsBinding.scheduleAttachRootWidget(BaseApp(
        child: app,
        onInitStateCallback: () {
          if (onInitStateCallback != null) onInitStateCallback();
        },
      ));
      widgetsBinding.scheduleWarmUpFrame();
    });
  }, onError: (error, stackTrace) async {
    _onZoneGlobalError(error, stackTrace);
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
