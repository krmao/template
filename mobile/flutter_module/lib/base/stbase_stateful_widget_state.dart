import 'dart:async';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_module/base/stbase_constants.dart';
import 'package:flutter_module/base/utils/stbase_widget_util.dart';
import 'package:flutter_module/settings/imports/_flutter_imports_common.dart';

import 'widgets/stbase_loading_widget.dart';
import 'widgets/stbase_titlebar_widget.dart';

class STBaseStatefulWidgetState<T extends StatefulWidget> extends State<T> with AutomaticKeepAliveClientMixin<T>, WidgetsBindingObserver {
  String tag;
  WidgetBuildFunction child;
  bool keepAlive = false;
  STBaseLoadingWidget loadingWidget;
  STBaseTitleBarWidget titleBarWidget;
  Color statusBarColor;
  BuildContext scaffoldContext;

  // MethodChannel methodChannel;

  bool enableSafeArea = true;
  bool enableSafeAreaTop = true;
  bool enableSafeAreaBottom = true;
  bool enableSafeAreaLeft = true;
  bool enableSafeAreaRight = true;
  bool enableTitleBar = false;
  bool enableExitWithDouble = false;
  bool enableStatusBarTransparent = false;

  @override
  bool get wantKeepAlive => this.keepAlive;

  STBaseStatefulWidgetState({this.child, this.statusBarColor, this.loadingWidget, this.enableTitleBar = false, this.titleBarWidget, this.keepAlive = false, this.enableSafeArea = true, this.enableSafeAreaTop = true, this.enableSafeAreaBottom = true, this.enableSafeAreaLeft = true, this.enableSafeAreaRight = true, this.enableExitWithDouble = false, this.enableStatusBarTransparent = false});

  STBaseStatefulWidgetState.initWithChild(WidgetBuildFunction child, {this.statusBarColor, this.loadingWidget, this.enableTitleBar = false, this.titleBarWidget, this.keepAlive = false, this.enableSafeArea = true, this.enableSafeAreaTop = true, this.enableSafeAreaBottom = true, this.enableSafeAreaLeft = true, this.enableSafeAreaRight = true, this.enableExitWithDouble = false, this.enableStatusBarTransparent = false}) {
    this.child = child;
  }

  @override
  void initState() {
    super.initState();
    tag = this.toStringShort();
    print("[$tag] initSatte");

    if (loadingWidget == null) loadingWidget = STBaseLoadingWidget();
    if (titleBarWidget == null) titleBarWidget = STBaseTitleBarWidget(onBackPressed: () => Navigator.pop(context));
    if (statusBarColor == null) statusBarColor = STBaseConstants.DEFAULT_STATUS_BAR_COLOR;
    if (child == null) child = () => Container();
  }

  @override
  void didChangeAppLifecycleState(AppLifecycleState state) {
    super.didChangeAppLifecycleState(state);
    print("[$tag] didChangeAppLifecycleState state=${state.toString()}");
  }

  @override
  Widget build(BuildContext context) {
    super.build(context);

    if (this.enableStatusBarTransparent) {
      print("[$tag] 设置状态栏完全透明");
      SystemChrome.setSystemUIOverlayStyle(SystemUiOverlayStyle(
        statusBarColor: Colors.transparent, // android >= M
        statusBarBrightness: Brightness.dark, // ios
        statusBarIconBrightness: Brightness.light, // android >= M
      ));
    }

    print("[$tag] build context=$context");
    return MaterialApp(
        home: buildRoot(),
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
            primarySwatch: Colors.red,
            primaryColor: STBaseConstants.PRIMARY_COLOR,
            accentColor: STBaseConstants.ACCENT_COLOR,
            primaryColorBrightness: Brightness.light,
            hintColor: STBaseConstants.HINT_COLOR,
            highlightColor: STBaseConstants.HIGHLIGHT_COLOR,
            inputDecorationTheme: InputDecorationTheme(labelStyle: TextStyle(color: STBaseConstants.INPUT_DECORATION_COLOR))));
  }

  Widget buildRoot() {
    print("[$tag] buildRoot");
    return Scaffold(
        backgroundColor: statusBarColor,
        body: Builder(builder: (BuildContext context) {
          scaffoldContext = context;
          print("[$tag] buildRoot scaffoldContext=$scaffoldContext");
          return SafeArea(
              top: enableSafeArea && enableSafeAreaTop,
              left: enableSafeArea && enableSafeAreaLeft,
              right: enableSafeArea && enableSafeAreaRight,
              bottom: enableSafeArea && enableSafeAreaBottom,
              child: WillPopScope(
                  child: Container(
                      color: statusBarColor,
                      width: double.infinity,
                      height: double.infinity,
                      child: Stack(children: <Widget>[
                        Container(
                          child: buildBody(),
                          margin: EdgeInsets.only(top: enableTitleBar ? titleBarWidget.height : 0),
                        ),
                        enableTitleBar ? titleBarWidget : Container(),
                        loadingWidget
                      ])),
                  onWillPop: () => _processExit(context)));
        }));
  }

  var _lastTime = 0;

  Future<bool> _processExit(BuildContext context) {
    if (!this.enableExitWithDouble) {
      return Future.value(true);
    }
    int now = DateTime.now().millisecondsSinceEpoch;
    var duration = now - _lastTime;
    print("_processExit -> now:$now, _lastTime:$_lastTime, duration:$duration");
    _lastTime = now;
    if (duration > 1500) {
      print("_processExit -> context==null?${context == null}");
      Scaffold.of(context).showSnackBar(SnackBar(content: Text("再按一次退出"), duration: Duration(milliseconds: 2000)));
      return Future.value(false);
    } else {
      return Future.value(true);
    }
  }

  Widget buildBody() => child();

  @override
  Future<bool> didPopRoute() {
    debugPrint("$tag didPopRoute");
    return Future.value(false);
  }

  @override
  void dispose() {
    print("[$tag] dispose");
    super.dispose();
  }

  // -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
  // -- common functions definition
  // -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --

  void showSnackBar(String msg) {
    if (scaffoldContext != null && msg?.trim()?.isNotEmpty == true) {
      Scaffold.of(scaffoldContext).showSnackBar(SnackBar(content: Text(msg), duration: Duration(milliseconds: 2000)));
    }
  }

  Widget getVerticalLine({Color lineColor = STBaseConstants.DEFAULT_LINE_COLOR, double height = double.infinity, double width = 1.0, EdgeInsetsGeometry margin, EdgeInsetsGeometry padding}) => STBaseWidgetUtils.getVerticalLine(lineColor: lineColor, height: height, width: width, margin: margin, padding: padding);

  Widget getHorizontalLine({Color lineColor = STBaseConstants.DEFAULT_LINE_COLOR, double height = 1.0, double width = double.infinity, EdgeInsetsGeometry margin, EdgeInsetsGeometry padding}) => STBaseWidgetUtils.getHorizontalLine(lineColor: lineColor, height: height, width: width, margin: margin, padding: padding);

  Widget getOnTapWidget(Widget child, GestureTapCallback onTap) => STBaseWidgetUtils.getOnTapWidget(child, onTap);

  Widget getOnDoubleTapWidget(Widget child, GestureTapCallback onDoubleTap) => STBaseWidgetUtils.getOnDoubleTapWidget(child, onDoubleTap);

  Widget getOnLongPressWidget(Widget child, GestureLongPressCallback onLongPress) => STBaseWidgetUtils.getOnLongPressWidget(child, onLongPress);
}
