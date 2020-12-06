import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_module/libraries/codesdancing_bridge/src/page_mixin_variables.dart';
import 'package:flutter_module/libraries/codesdancing_bridge/src/page_state.dart';

// ignore: must_be_immutable
class PageWidget extends StatefulWidget with PageMixinVariables {
  State state;

  PageWidget(
      {child,
      state,
      statusBarColor,
      loadingWidget,
      enableTitleBar = false,
      enableLoading = false,
      titleBarWidget,
      keepAlive = false,
      enableSafeArea = true,
      enableSafeAreaTop = true,
      enableSafeAreaBottom = true,
      enableSafeAreaLeft = true,
      enableSafeAreaRight = true})
      : this.initWithChild(child,
            state: state,
            statusBarColor: statusBarColor,
            loadingWidget: loadingWidget,
            enableTitleBar: enableTitleBar,
            enableLoading: enableLoading,
            titleBarWidget: titleBarWidget,
            keepAlive: keepAlive,
            enableSafeArea: enableSafeArea,
            enableSafeAreaTop: enableSafeAreaTop,
            enableSafeAreaBottom: enableSafeAreaBottom,
            enableSafeAreaLeft: enableSafeAreaLeft,
            enableSafeAreaRight: enableSafeAreaRight);

  PageWidget.initWithChild(child,
      {statusBarColor,
      state,
      loadingWidget,
      enableTitleBar = false,
      enableLoading = false,
      titleBarWidget,
      keepAlive = false,
      enableSafeArea = true,
      enableSafeAreaTop = true,
      enableSafeAreaBottom = true,
      enableSafeAreaLeft = true,
      enableSafeAreaRight = true}) {
    this.child = child;
    this.statusBarColor = statusBarColor;
    this.state = state;
    this.loadingWidget = loadingWidget;
    this.enableTitleBar = enableTitleBar;
    this.enableLoading = enableLoading;
    this.titleBarWidget = titleBarWidget;
    this.keepAlive = keepAlive;
    this.enableSafeArea = enableSafeArea;
    this.enableSafeAreaTop = enableSafeAreaTop;
    this.enableSafeAreaBottom = enableSafeAreaBottom;
    this.enableSafeAreaLeft = enableSafeAreaLeft;
    this.enableSafeAreaRight = enableSafeAreaRight;
  }

  @override
  State createState() {
    if (this.state == null) {
      this.state = PageState(
        child: this.child,
        statusBarColor: this.statusBarColor,
        loadingWidget: this.loadingWidget,
        titleBarWidget: this.titleBarWidget,
        keepAlive: this.keepAlive,
        enableSafeArea: this.enableSafeArea,
        enableSafeAreaTop: this.enableSafeAreaTop,
        enableSafeAreaBottom: this.enableSafeAreaBottom,
        enableSafeAreaLeft: this.enableSafeAreaLeft,
        enableSafeAreaRight: this.enableSafeAreaRight,
      );
    }
    return this.state;
  }
}
