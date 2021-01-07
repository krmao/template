//import 'package:flutter/cupertino.dart';

import 'dart:async';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_boost/flutter_boost.dart';

import '../codesdancing_bridge.dart';
import 'app_constants.dart';
import 'bridge_application.dart';
import 'page_route_observer.dart';

typedef void OnInitStateCallback();
typedef Map<String, PageBuilder> TopRouterProvider();

class App extends StatefulWidget {
  final Widget child;
  final Color statusBarColor;
  final OnInitStateCallback onInitStateCallback;
  final bool enableSafeArea,
      enableSafeAreaTop,
      enableSafeAreaBottom,
      enableSafeAreaLeft,
      enableSafeAreaRight;

  App(
      {Key key,
      @required this.child,
      this.statusBarColor = Constants.DEFAULT_STATUS_BAR_COLOR,
      this.enableSafeArea = true,
      this.enableSafeAreaTop = true,
      this.enableSafeAreaBottom = true,
      this.enableSafeAreaLeft = true,
      this.enableSafeAreaRight = true,
      this.onInitStateCallback})
      : super(key: key);

  @override
  State<StatefulWidget> createState() => AppState(
      child: this.child,
      statusBarColor: this.statusBarColor,
      enableSafeArea: this.enableSafeArea,
      enableSafeAreaTop: this.enableSafeAreaTop,
      enableSafeAreaBottom: this.enableSafeAreaBottom,
      enableSafeAreaLeft: this.enableSafeAreaLeft,
      enableSafeAreaRight: this.enableSafeAreaRight,
      onInitStateCallback: this.onInitStateCallback);
}

class AppState extends State<App> {
  final Widget child;
  final Color statusBarColor;
  final OnInitStateCallback onInitStateCallback;
  final bool enableSafeArea,
      enableSafeAreaTop,
      enableSafeAreaBottom,
      enableSafeAreaLeft,
      enableSafeAreaRight;

  AppState(
      {@required this.child,
      this.statusBarColor = Constants.DEFAULT_STATUS_BAR_COLOR,
      this.enableSafeArea = true,
      this.enableSafeAreaTop = true,
      this.enableSafeAreaBottom = true,
      this.enableSafeAreaLeft = true,
      this.enableSafeAreaRight = true,
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
        builder: FlutterBoost.init(postPush: (String pageName, String uniqueId,
            Map params, Route route, Future _) {
          print(
              "onRoutePushed -> pageName:$pageName, uniqueId:$uniqueId, params:$params");
        }),
        navigatorObservers: [PageRouteObserver.singleton],
        home: Scaffold(
            backgroundColor: this
                .statusBarColor, // android status bar and iphone X top and bottom edges color
            body: Builder(builder: (BuildContext context) {
              return SafeArea(
                  top: this.enableSafeArea && this.enableSafeAreaTop,
                  left: this.enableSafeArea && this.enableSafeAreaLeft,
                  right: this.enableSafeArea && this.enableSafeAreaRight,
                  bottom: this.enableSafeArea && this.enableSafeAreaBottom,
                  child: WillPopScope(
                      child: this.child,
                      onWillPop: () => _processExit(context)));
            })),
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
            primaryColor: Constants.PRIMARY_COLOR,
            accentColor: Constants.ACCENT_COLOR,
            primaryColorBrightness: Brightness.light,
            hintColor: Constants.HINT_COLOR,
            highlightColor: Constants.HIGHLIGHT_COLOR,
            inputDecorationTheme: InputDecorationTheme(
                labelStyle:
                    TextStyle(color: Constants.INPUT_DECORATION_COLOR))));
  }
}

// ignore: unused_element
class _NoOverScrollBehavior extends ScrollBehavior {
  @override
  Widget buildViewportChrome(BuildContext context, Widget child, AxisDirection axisDirection) {
    return child;
  }
}

var _lastTime = 0;

Future<bool> _processExit(BuildContext context) {
  int now = DateTime.now().millisecondsSinceEpoch;
  var duration = now - _lastTime;
  print("_processExit -> now:$now, _lastTime:$_lastTime, duration:$duration");
  _lastTime = now;
  if (duration > 1500) {
    print("_processExit -> context==null?${context == null}");
    Scaffold.of(context).showSnackBar(SnackBar(
        content: Text("再按一次退出"), duration: Duration(milliseconds: 2000)));
    return Future.value(false);
  } else {
    return Future.value(true);
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

BoostContainerSettings getCurrentContainer() {
    return FlutterBoost.containerManager?.onstageSettings;
}

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
    if (Application.debug) {
        if (getCurrentContainer()?.name == '_error_page') {
            FlutterBoost.singleton.closeCurrent();
        }

        // show error page
        URL.openURL<dynamic>(
            'smart://template/flutter?page=_error_page', urlParams: {
            "error": errorMsg,
            "stacktrace": stackTraceStr,
        });
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

// ignore: must_be_immutable
class PageRouteObserverProvider extends InheritedWidget {
  dynamic data;
  PageRouteObserverProvider({
    Key key,
    this.data,
    @required Widget child,
  }) : super(key: key, child: child);

  @override
  bool updateShouldNotify(InheritedWidget oldWidget) {
    return false;
  }

  static PageRouteObserverProvider of(BuildContext context) {
    // return context.inheritFromWidgetOfExactType(PageRouteObserverProvider);
    return context
        .dependOnInheritedWidgetOfExactType<PageRouteObserverProvider>();
  }
}

/// 全局路由处理
TopRouterProvider _mixGlobalRouters(TopRouterProvider outRouterProvider) {
  // ErrorRouters
  Map<String, PageBuilder> globalRouters = {
    '_error_page': (pageName, params, _) => PageRouteObserverProvider(
        data: params,
        child: ErrorApp(
            params["error"], params["stacktrace"], params["userInfoMap"]))
  };
  globalRouters.addAll(outRouterProvider());
  return () {
    return globalRouters;
  };
}

class PageNotFoundHomePage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => _PageNotFoundHomeState();
}

class _PageNotFoundHomeState extends PageState<PageNotFoundHomePage> {
  _PageNotFoundHomeState();

  @override
  String getPageId() {
    return 'PageNotFound';
  }

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
                  FlutterBoost.singleton.closeCurrent();
                },
              ),
            )
          ],
        ),
      )),
    );
  }
}

void appRun(app, {TopRouterProvider routerProvider, OnInitStateCallback onInitStateCallback}) {
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
    Application.getApplicationConstants().then((value) {
      // ignore: invalid_use_of_protected_member
      widgetsBinding.scheduleAttachRootWidget(App(
        enableSafeArea: false,
        statusBarColor: Constants.DEFAULT_STATUS_BAR_COLOR,
        child: app,
        onInitStateCallback: () {
          TopRouterProvider finalRouterProvider = _mixGlobalRouters(routerProvider);
          if (finalRouterProvider != null) {
            FlutterBoost.singleton.registerPageBuilders(finalRouterProvider());
            FlutterBoost.singleton.registerRouteSettingsBuilder(
                (url, {exts, urlParams}) => BoostRouteSettings(
                    uniqueId: '${url}_${DateTime.now().millisecondsSinceEpoch}',
                    name: url,
                    params: urlParams));
            FlutterBoost.singleton
                .registerDefaultPageBuilder((pageName, params, uniqueId) {
              return Scaffold(
                  backgroundColor: Colors.blue,
                  appBar: AppBar(title: Text('DEFAULT FLUTTER PAGE"')),
                  body: new Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: <Widget>[
                        new GestureDetector(
                          onTap: () => FlutterBoost.singleton.closeCurrent(),
                          child: new Container(
                            padding: const EdgeInsets.all(10.0),
                            color: Colors.blue,
                            child: const Text("BACK",
                                style: TextStyle(
                                    fontSize: 28.0, color: Colors.white)),
                          ),
                        )
                      ]));
            });

            FlutterBoost.singleton
                .addBoostNavigatorObserver(PageRouteObserver.singleton);
            FlutterBoost.singleton.addContainerObserver(
                (ContainerOperation operation,
                    BoostContainerSettings settings) {
              print("flutter boostContainerObserver");
            });

            FlutterBoost.singleton
                .addBoostContainerLifeCycleObserver((state, settings) {
              if (state == ContainerLifeCycle.Appear) {
                PageRouteObserver.singleton
                    .pageDidAppear(settings.name, settings.uniqueId);
              } else if (state == ContainerLifeCycle.Disappear) {
                PageRouteObserver.singleton
                    .pageDidDisappear(settings.name, settings.uniqueId);
              } else if (state == ContainerLifeCycle.Destroy) {
                PageRouteObserver.singleton
                    .pageDidDestroy(settings.name, settings.uniqueId);
              }
            });

            FlutterBoostAPI.singleton.routeSettingsBuilder = (String url,
                    {Map<String, dynamic> urlParams,
                    Map<String, dynamic> exts}) =>
                BoostRouteSettings(
                    uniqueId: '${url}_${DateTime.now().millisecondsSinceEpoch}',
                    name: url,
                    params: urlParams);
          }

          if(onInitStateCallback!=null) onInitStateCallback();
        },
      ));
      widgetsBinding.scheduleWarmUpFrame();
    });
  }, onError: (error, stackTrace) async {
    _onZoneGlobalError(error, stackTrace);
  }, zoneSpecification: ZoneSpecification(
      print: (Zone self, ZoneDelegate parent, Zone zone, String line) {
    try {
      if (Application.debug) {
        parent.print(zone, line);
      }
    } catch (e, _) {
      parent.print(zone, e);
    }
  }));
}
