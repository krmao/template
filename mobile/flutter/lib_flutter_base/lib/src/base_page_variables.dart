import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';

import 'widgets/base_widget_loading.dart';
import 'widgets/base_widget_title_bar.dart';

typedef WidgetBuildFunction = Widget Function(BuildContext context);

mixin BasePageVariables {
  WidgetBuildFunction child;
  bool keepAlive = false;
  BaseWidgetLoading loadingWidget;
  BaseWidgetTitleBar titleBarWidget;
  Color statusBarColor;

  // BuildContext context;
  BuildContext scaffoldContext;

  bool enableTitleBar = false;
  bool enableLoading = false;
}
