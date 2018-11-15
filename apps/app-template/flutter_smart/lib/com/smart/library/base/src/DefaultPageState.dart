import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:smart/com/smart/library/base/src/utils/WidgetUtils.dart';

import 'Constants.dart';
import 'utils/NativeManager.dart';
import 'widgets/LoadingWidget.dart';
import 'widgets/TitleBarWidget.dart';

class DefaultPageState<T extends StatefulWidget> extends State<T> with AutomaticKeepAliveClientMixin<T>, WidgetsBindingObserver, WidgetsBackPressedEvent {
  String tag;
  WidgetBuildFunction child;
  bool keepAlive = false;
  LoadingWidget loadingWidget;
  TitleBarWidget titleBarWidget;
  Color statusBarColor;
  BuildContext scaffoldContext;

  // MethodChannel methodChannel;

  bool enableSafeArea = true;
  bool enableSafeAreaTop = true;
  bool enableSafeAreaBottom = true;
  bool enableSafeAreaLeft = true;
  bool enableSafeAreaRight = true;
  bool enableTitleBar = true;

  @override
  bool get wantKeepAlive => this.keepAlive;

  DefaultPageState(
      {this.statusBarColor, this.loadingWidget, this.enableTitleBar = true, this.titleBarWidget, this.child, this.keepAlive = false, this.enableSafeArea = true, this.enableSafeAreaTop = true, this.enableSafeAreaBottom = true, this.enableSafeAreaLeft = true, this.enableSafeAreaRight = true});

  @override
  void initState() {
    super.initState();
    tag = this.toStringShort();
    print("[$tag] initSatte");
    // WidgetsBinding.instance.addObserver(this);
    NativeManager.onBackPressedEventList.add(this);

    if (loadingWidget == null) loadingWidget = LoadingWidget();
    if (titleBarWidget == null) titleBarWidget = TitleBarWidget();
    if (statusBarColor == null) statusBarColor = Constants.DEFAULT_STATUS_BAR_COLOR;
    if (child == null) child = () => Container();
  }

  @override
  void didChangeAppLifecycleState(AppLifecycleState state) {
    super.didChangeAppLifecycleState(state);
    print("[$tag] didChangeAppLifecycleState state=${state.toString()}");
  }

  @override
  Widget build(BuildContext context) {
    print("[$tag] build context=$context");
    // methodChannel = NativeManager.createMethodChannel(context, titleBarWidget);
    titleBarWidget.onBackPressed = () => NativeManager.enableNative ? NativeManager.finish() : Navigator.pop(context);

    return buildRoot();
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
                  ])));
        }));
  }

  Widget buildBody() => child();

  @override
  Future<bool> didPopRoute() {
    debugPrint("$tag didPopRoute");
    return Future.value(false);
  }

  @override
  bool onBackPressed() {
    return false;
  }

  @override
  void dispose() {
    print("[$tag] dispose");
    // WidgetsBinding.instance.removeObserver(this);
    NativeManager.onBackPressedEventList.remove(this);
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

  Widget getVerticalLine({Color lineColor = Constants.DEFAULT_LINE_COLOR, double height = double.infinity, double width = 1.0, EdgeInsetsGeometry margin, EdgeInsetsGeometry padding}) => WidgetUtils.getVerticalLine(lineColor: lineColor, height: height, width: width, margin: margin, padding: padding);

  Widget getHorizontalLine({Color lineColor = Constants.DEFAULT_LINE_COLOR, double height = 1.0, double width = double.infinity, EdgeInsetsGeometry margin, EdgeInsetsGeometry padding}) =>
      WidgetUtils.getHorizontalLine(lineColor: lineColor, height: height, width: width, margin: margin, padding: padding);

  Widget getOnTapWidget(Widget child, GestureTapCallback onTap) => WidgetUtils.getOnTapWidget(child, onTap);

  Widget getOnDoubleTapWidget(Widget child, GestureTapCallback onDoubleTap) => WidgetUtils.getOnDoubleTapWidget(child, onDoubleTap);

  Widget getOnLongPressWidget(Widget child, GestureLongPressCallback onLongPress) => WidgetUtils.getOnLongPressWidget(child, onLongPress);
}
