import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_module/base/stbase_constants.dart';
import 'package:flutter_module/base/stbase_stateful_widget_state.dart';

import 'widgets/stbase_loading_widget.dart';
import 'widgets/stbase_titlebar_widget.dart';

// ignore: must_be_immutable
class STBaseStatefulWidget extends StatefulWidget {
  STBaseLoadingWidget loadingWidget;
  STBaseTitleBarWidget titleBarWidget;
  WidgetBuildFunction child;
  bool keepAlive = false;
  bool enableSafeArea = false;
  bool enableSafeAreaTop = false;
  bool enableSafeAreaBottom = false;
  bool enableSafeAreaLeft = false;
  bool enableSafeAreaRight = false;
  Color statusBarColor;
  State state;

  STBaseStatefulWidget({this.child, this.statusBarColor, this.loadingWidget, this.titleBarWidget, this.keepAlive, this.state, this.enableSafeArea, this.enableSafeAreaTop, this.enableSafeAreaBottom, this.enableSafeAreaLeft, this.enableSafeAreaRight});

  STBaseStatefulWidget.initWithChild(WidgetBuildFunction child, {this.statusBarColor, this.loadingWidget, this.titleBarWidget, this.keepAlive, this.enableSafeArea, this.enableSafeAreaTop, this.enableSafeAreaBottom, this.enableSafeAreaLeft, this.enableSafeAreaRight}) {
    this.child = child;
  }

  STBaseStatefulWidget.initWithState(State state) {
    this.state = state;
  }

  @override
  State createState() {
    if (this.state == null) {
      this.state = STBaseStatefulWidgetState(
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
