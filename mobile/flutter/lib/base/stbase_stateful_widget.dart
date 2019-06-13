import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:smart/base/Constants.dart';
import 'package:smart/base/stbase_stateful_widget_state.dart';

import 'widgets/LoadingWidget.dart';
import 'widgets/TitleBarWidget.dart';

// ignore: must_be_immutable
class STBaseStatefulWidget extends StatefulWidget {

  final LoadingWidget loadingWidget;
  final TitleBarWidget titleBarWidget;
  final WidgetBuildFunction child;
  bool keepAlive = false;
  bool enableSafeArea = true;
  bool enableSafeAreaTop = true;
  bool enableSafeAreaBottom = true;
  bool enableSafeAreaLeft = true;
  bool enableSafeAreaRight = true;
  Color statusBarColor;
  State state;

  STBaseStatefulWidget({this.child, this.statusBarColor, this.loadingWidget, this.titleBarWidget, this.keepAlive, this.state, this.enableSafeArea, this.enableSafeAreaTop, this.enableSafeAreaBottom, this.enableSafeAreaLeft, this.enableSafeAreaRight});

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
