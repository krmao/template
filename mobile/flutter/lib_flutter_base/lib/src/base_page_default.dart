import 'package:flutter/cupertino.dart';
import 'package:lib_flutter_base/src/base_state_default.dart';

import 'base_page.dart';
import 'base_page_variables.dart';

// ignore: must_be_immutable
class BasePageDefault extends BasePage with BasePageVariables {
  State state;

  BasePageDefault(
      {child,
      state,
      statusBarColor,
      loadingWidget,
      enableTitleBar = false,
      enableLoading = false,
      titleBarWidget,
      keepAlive = false,
      uniqueId,
      argumentsJsonString})
      : this.initWithChild(
          child,
          state: state,
          statusBarColor: statusBarColor,
          loadingWidget: loadingWidget,
          enableTitleBar: enableTitleBar,
          enableLoading: enableLoading,
          titleBarWidget: titleBarWidget,
          keepAlive: keepAlive,
          uniqueId: uniqueId,
          argumentsJsonString: argumentsJsonString,
        );

  BasePageDefault.initWithChild(child,
      {statusBarColor,
      state,
      loadingWidget,
      enableTitleBar = false,
      enableLoading = false,
      titleBarWidget,
      keepAlive = false,
      uniqueId,
      argumentsJsonString})
      : super(uniqueId: uniqueId, argumentsJsonString: argumentsJsonString) {
    this.child = child;
    this.statusBarColor = statusBarColor;
    this.state = state;
    this.loadingWidget = loadingWidget;
    this.enableTitleBar = enableTitleBar;
    this.enableLoading = enableLoading;
    this.titleBarWidget = titleBarWidget;
    this.keepAlive = keepAlive;
  }

  @override
  State createState() {
    if (this.state == null) {
      this.state = BaseStateDefault(
        child: this.child,
        statusBarColor: this.statusBarColor,
        loadingWidget: this.loadingWidget,
        titleBarWidget: this.titleBarWidget,
        keepAlive: this.keepAlive,
        uniqueId: this.uniqueId,
        argumentsJsonString: this.argumentsJsonString,
      );
    }
    return this.state;
  }
}
