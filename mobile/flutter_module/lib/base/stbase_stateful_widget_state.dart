import 'dart:async';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import '../base/stbase_constants.dart';
import '../base/utils/stbase_widget_util.dart';

import 'utils/stbase_native_manager.dart';
import 'widgets/stbase_loading_widget.dart';
import 'widgets/stbase_titlebar_widget.dart';

class STBaseStatefulWidgetState<T extends StatefulWidget> extends State<T> with AutomaticKeepAliveClientMixin<T>, WidgetsBindingObserver, WidgetsBackPressedEvent {
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

  @override
  bool get wantKeepAlive => this.keepAlive;

  STBaseStatefulWidgetState({ this.child, this.statusBarColor, this.loadingWidget, this.enableTitleBar = false, this.titleBarWidget, this.keepAlive = false, this.enableSafeArea = true, this.enableSafeAreaTop = true, this.enableSafeAreaBottom = true, this.enableSafeAreaLeft = true, this.enableSafeAreaRight = true});

  STBaseStatefulWidgetState.initWithChild(WidgetBuildFunction child, { this.statusBarColor, this.loadingWidget, this.enableTitleBar = false, this.titleBarWidget, this.keepAlive = false, this.enableSafeArea = true, this.enableSafeAreaTop = true, this.enableSafeAreaBottom = true, this.enableSafeAreaLeft = true, this.enableSafeAreaRight = true}){
    this.child = child;
  }

  @override
  void initState() {
    super.initState();
    tag = this.toStringShort();
    print("[$tag] initSatte");
    // WidgetsBinding.instance.addObserver(this);
    STBaseNativeManager.onBackPressedEventList.add(this);

    if (loadingWidget == null) loadingWidget = STBaseLoadingWidget();
    if (titleBarWidget == null) titleBarWidget = STBaseTitleBarWidget(onBackPressed: () => STBaseNativeManager.enableNative ? STBaseNativeManager.finish() : Navigator.pop(context));
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
    print("[$tag] build context=$context");
    // methodChannel = NativeManager.createMethodChannel(context, titleBarWidget);
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
    STBaseNativeManager.onBackPressedEventList.remove(this);
    super.dispose();
  }

  // -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
  // -- common functions definition
  // -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --

  void showSnackBar(String msg) {
    if (scaffoldContext != null && msg
        ?.trim()
        ?.isNotEmpty == true) {
      Scaffold.of(scaffoldContext).showSnackBar(SnackBar(content: Text(msg), duration: Duration(milliseconds: 2000)));
    }
  }

  Widget getVerticalLine({Color lineColor = STBaseConstants.DEFAULT_LINE_COLOR, double height = double.infinity, double width = 1.0, EdgeInsetsGeometry margin, EdgeInsetsGeometry padding}) =>
      STBaseWidgetUtils.getVerticalLine(lineColor: lineColor,
          height: height,
          width: width,
          margin: margin,
          padding: padding);

  Widget getHorizontalLine({Color lineColor = STBaseConstants.DEFAULT_LINE_COLOR, double height = 1.0, double width = double.infinity, EdgeInsetsGeometry margin, EdgeInsetsGeometry padding}) =>
      STBaseWidgetUtils.getHorizontalLine(lineColor: lineColor,
          height: height,
          width: width,
          margin: margin,
          padding: padding);

  Widget getOnTapWidget(Widget child, GestureTapCallback onTap) => STBaseWidgetUtils.getOnTapWidget(child, onTap);

  Widget getOnDoubleTapWidget(Widget child, GestureTapCallback onDoubleTap) => STBaseWidgetUtils.getOnDoubleTapWidget(child, onDoubleTap);

  Widget getOnLongPressWidget(Widget child, GestureLongPressCallback onLongPress) => STBaseWidgetUtils.getOnLongPressWidget(child, onLongPress);
}
