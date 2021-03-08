import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:lib_flutter_base/lib_flutter_base.dart';

import 'base_page_variables.dart';
import 'base_state.dart';
import 'widgets/base_widget_loading.dart';
import 'widgets/base_widget_title_bar.dart';

class BaseStateDefault<T extends StatefulWidget> extends BaseState<T>
    with
        AutomaticKeepAliveClientMixin<T>,
        WidgetsBindingObserver,
        WidgetsBindingObserver,
        BasePageVariables {
  @override
  bool get wantKeepAlive => this.keepAlive;

  BaseStateDefault(
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

  BaseStateDefault.initWithChild(child,
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

  //region build

  @override
  Widget build(BuildContext context) {
    super.build(context);
    print("[page] $runtimeType - build context=$context");

    if (statusBarColor == null) statusBarColor = Colors.blueGrey;

    //endregion

    return buildBase(context);
  }

  Widget buildBase(BuildContext context) {
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
      loadingWidget = BaseWidgetLoading();
      children.add(loadingWidget);
    }
    if (enableTitleBar && titleBarWidget == null) {
      titleBarWidget =
          BaseWidgetTitleBar(onBackPressed: () => pop(scaffoldContext));
      children.add(titleBarWidget);
    }
    //endregion
    print(
        "[page] enableSafeArea && enableSafeAreaTop = ${enableSafeArea && enableSafeAreaTop}");
    return Scaffold(
      backgroundColor: statusBarColor,
      body: Builder(
        builder: (BuildContext context) {
          scaffoldContext = context;
          print("[page] $runtimeType - build scaffoldContext=$scaffoldContext");
          // bug 如果 android backgroundMode(BoostFlutterActivity.BackgroundMode.transparent) 则 SafeArea 不起作用
          // https://github.com/flutter/flutter/issues/46060
          return SafeArea(
            top: enableSafeArea && enableSafeAreaTop,
            left: enableSafeArea && enableSafeAreaLeft,
            right: enableSafeArea && enableSafeAreaRight,
            bottom: enableSafeArea && enableSafeAreaBottom,
            child: Container(
              color: statusBarColor,
              width: double.infinity,
              height: double.infinity,
              child: Stack(children: children),
            ),
          );
        },
      ),
    );
  }

  @protected
  Widget buildBaseChild(BuildContext context) => child(context);

  //endregion

  ///region push and pop

  Future<T> push<T extends Object>(BuildContext context, Route<T> route) {
    print("[page] $runtimeType - push");
    return Navigator.of(context).push(route);
  }

  Future<T> pushNamed<T extends Object>(BuildContext context, String routeName,
      {Object arguments}) {
    print("[page] $runtimeType - pushNamed $routeName");
    return Navigator.of(context).pushNamed<T>(routeName, arguments: arguments);
  }

  dynamic pop<T extends Object>(BuildContext context, [T result]) {
    print("[page] $runtimeType - pop");
    final ModalRoute<dynamic> parentRoute = ModalRoute.of(context);
    final bool canPop = parentRoute?.canPop ?? false;
    if (canPop) {
      Navigator.pop<T>(context, result);
    } else {
      return BoostNavigator.of().pop([result]);
    }
  }

  ///endregion

  //endregion

  @override
  @mustCallSuper
  void initState() {
    super.initState();
    print("[page] $runtimeType - initState ${this.toStringShort()}");
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
}
