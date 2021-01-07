import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import 'bridge_page.dart';
import 'page_aware.dart';
import 'page_mixin_build.dart';
import 'page_mixin_variables.dart';
import 'widgets/widget_loading.dart';
import 'widgets/widget_titlebar.dart';

class PageState<T extends StatefulWidget> extends State<T>
    with
        AutomaticKeepAliveClientMixin<T>,
        WidgetsBindingObserver,
        WidgetsBindingObserver,
        PageAware,
        PageMixinVariables,
        PageMixinBuild {
  @override
  bool get wantKeepAlive => this.keepAlive;

  String containerId;

  PageState(
      {child,
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

  PageState.initWithChild(child,
      {statusBarColor,
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

  String getPageId() {
    return '';
  }

  String getPageName() {
    return '';
  }

  //region build

  @override
  Widget build(BuildContext context) {
    super.build(context);
    print("[page] $runtimeType - build context=$context");

    if (statusBarColor == null) statusBarColor = Colors.blueGrey;

    //region common body
    if (child == null) child = (context) => Container();
    List<Widget> children = <Widget>[];
    children.add(Container(
        child: buildBaseChild(context),
        margin:
            EdgeInsets.only(top: enableTitleBar ? titleBarWidget.height : 0)));
    if (enableLoading && loadingWidget == null) {
      loadingWidget = Loading();
      children.add(loadingWidget);
    }
    if (enableTitleBar && titleBarWidget == null) {
      titleBarWidget = TitleBar(onBackPressed: () => pop(scaffoldContext));
      children.add(titleBarWidget);
    }
    //endregion

    return buildBase(context);
  }

  //endregion

  @override
  @mustCallSuper
  void initState() {
    super.initState();
    containerId = PageBridge.getContainerId(context);
    print("[page] $runtimeType - initState ${this.toStringShort()}, containerId=$containerId");
    WidgetsBinding.instance.addObserver(this);
  }

  //region did change

  @override
  @mustCallSuper
  void didChangeDependencies() {
    super.didChangeDependencies();
    print("[page] $runtimeType - didChangeDependencies");
  }

  //region did change

  @override
  @mustCallSuper
  void didChangeMetrics() {
    super.didChangeMetrics();
    print("[page] $runtimeType - didChangeMetrics");
  }

  @override
  @mustCallSuper
  void didChangeAppLifecycleState(AppLifecycleState state) {
    super.didChangeAppLifecycleState(state);
    print(
        "[page] $runtimeType - didChangeAppLifecycleState state=${state.toString()}");
  }

  @override
  @mustCallSuper
  void didChangeTextScaleFactor() {
    super.didChangeTextScaleFactor();
    print("[page] $runtimeType - didChangeTextScaleFactor");
  }

  @override
  @mustCallSuper
  void didChangePlatformBrightness() {
    super.didChangePlatformBrightness();
    print("[page] $runtimeType - didChangePlatformBrightness");
  }

  @override
  @mustCallSuper
  void didChangeLocales(List<Locale> locale) {
    super.didChangeLocales(locale);
    print("[page] $runtimeType - didChangeLocales");
  }

  @override
  @mustCallSuper
  void didHaveMemoryPressure() {
    super.didHaveMemoryPressure();
    print("[page] $runtimeType - didHaveMemoryPressure");
  }

  @override
  @mustCallSuper
  void didChangeAccessibilityFeatures() {
    super.didChangeAccessibilityFeatures();
    print("[page] $runtimeType - didChangeAccessibilityFeatures");
  }

  @override
  Future<bool> didPopRoute() {
    debugPrint("[page] $runtimeType - didPopRoute");
    return Future.value(false);
  }

  //endregion

  @override
  @mustCallSuper
  void didUpdateWidget(Widget oldWidget) {
    super.didUpdateWidget(oldWidget);
    print("[page] $runtimeType - didUpdateWidget");
  }

  @override
  @mustCallSuper
  void reassemble() {
    super.reassemble();
    print("[page] $runtimeType - reassemble");
  }

  //当State对象从树中被移除时，会调用此回调
  @override
  @mustCallSuper
  void deactivate() {
    super.deactivate();
    print("[page] $runtimeType - deactivate");
  }

  @override
  @mustCallSuper
  void dispose() {
    WidgetsBinding.instance.removeObserver(this);
    super.dispose();
    print("[page] $runtimeType - dispose");
  }

//endregion

  Future<T> push<T extends Object>(BuildContext context, Route<T> route) {
    return Navigator.of(context).push(route);
  }

  Future<T> pushNamed<T extends Object>(
    BuildContext context,
    String routeName, {
    Object arguments,
  }) {
    return Navigator.of(context).pushNamed<T>(routeName, arguments: arguments);
  }

  dynamic pop<T extends Object>(BuildContext context, [T result]) {
    final ModalRoute<dynamic> parentRoute = ModalRoute.of(context);
    final bool canPop = parentRoute?.canPop ?? false;
    if (canPop) {
      Navigator.pop<T>(context, result);
      pageDidDisappear();
    } else {
      return PageBridge.close();
    }
  }
}
