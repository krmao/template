import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';

import 'widgets/widget_loading.dart';
import 'widgets/widget_titlebar.dart';

typedef WidgetBuildFunction = Widget Function(BuildContext context);

mixin PageMixinVariables {
  WidgetBuildFunction child;
  bool keepAlive = false;
  Loading loadingWidget;
  TitleBar titleBarWidget;
  Color statusBarColor;
  BuildContext context;
  BuildContext scaffoldContext;

  bool enableSafeArea = true;
  bool enableSafeAreaTop = true;
  bool enableSafeAreaBottom = true;
  bool enableSafeAreaLeft = true;
  bool enableSafeAreaRight = true;
  bool enableTitleBar = false;
  bool enableLoading = false;
}
