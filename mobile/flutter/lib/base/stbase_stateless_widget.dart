import 'dart:async';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:smart/base/stbase_constants.dart';
import 'package:smart/base/utils/stbase_native_manager.dart';

import 'widgets/stbase_loading_widget.dart';
import 'widgets/stbase_titlebar_widget.dart';

// ignore: must_be_immutable
class STBaseStatelessWidget extends StatelessWidget with WidgetsBindingObserver {
  String tag;
  BuildContext scaffoldContext;
  STBaseLoadingWidget loadingWidget;
  STBaseTitleBarWidget titleBarWidget;
  WidgetBuildFunction child;
  bool keepAlive = false;
  bool enableSafeArea = true;
  bool enableSafeAreaTop = true;
  bool enableSafeAreaBottom = true;
  bool enableSafeAreaLeft = true;
  bool enableSafeAreaRight = true;
  bool enableTitleBar = true;
  Color statusBarColor;
  State state;

  STBaseStatelessWidget({this.child, this.statusBarColor, this.loadingWidget, this.titleBarWidget, this.keepAlive, this.state, this.enableSafeArea, this.enableSafeAreaTop, this.enableSafeAreaBottom, this.enableSafeAreaLeft, this.enableSafeAreaRight});

  STBaseStatelessWidget.initWithChild(WidgetBuildFunction child, { this.statusBarColor, this.loadingWidget, this.titleBarWidget, this.keepAlive, this.enableSafeArea, this.enableSafeAreaTop, this.enableSafeAreaBottom, this.enableSafeAreaLeft, this.enableSafeAreaRight}){
    this.child = child;
  }

  STBaseStatelessWidget.initWithState(State state){
    this.state = state;
  }

  @override
  Widget build(BuildContext context) {
    tag = this.toStringShort();
    print("[$tag] build");

    if (loadingWidget == null) loadingWidget = STBaseLoadingWidget();
    if (titleBarWidget == null) titleBarWidget = STBaseTitleBarWidget(onBackPressed: () => STBaseNativeManager.enableNative ? STBaseNativeManager.finish() : Navigator.pop(context));
    if (statusBarColor == null) statusBarColor = STBaseConstants.DEFAULT_STATUS_BAR_COLOR;
    if (child == null) child = () => Container();

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
  void didChangeAppLifecycleState(AppLifecycleState state) {
    super.didChangeAppLifecycleState(state);
    print("[$tag] didChangeAppLifecycleState state=${state.toString()}");
  }


  @override
  Future<bool> didPopRoute() {
    debugPrint("$tag didPopRoute");
    return Future.value(false);
  }

}
