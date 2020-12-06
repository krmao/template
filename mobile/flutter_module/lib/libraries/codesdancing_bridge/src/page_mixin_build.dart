import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_module/libraries/codesdancing_bridge/src/page_mixin_variables.dart';
import 'package:flutter_module/settings/router/flutter_router.dart';

import '../codesdancing_bridge.dart';
import 'page_aware.dart';

mixin PageMixinBuild on PageAware, PageMixinVariables {
  //region build

  Widget buildBase(BuildContext context) {
    print("[page] $runtimeType - build context=$context");
    this.context = context;

    if (statusBarColor == null) statusBarColor = Colors.blueGrey;

    //region common body
    if (child == null) child = (context) => Container();
    List<Widget> children = <Widget>[];
    children.add(Container(child: buildBaseChild(context), margin: EdgeInsets.only(top: enableTitleBar ? titleBarWidget.height : 0)));
    if (enableLoading && loadingWidget == null) {
      loadingWidget = Loading();
      children.add(loadingWidget);
    }
    if (enableTitleBar && titleBarWidget == null) {
      titleBarWidget = TitleBar(onBackPressed: () => pop(scaffoldContext));
      children.add(titleBarWidget);
    }
    //endregion

    return Scaffold(
      backgroundColor: statusBarColor,
      body: Builder(
        builder: (BuildContext context) {
          scaffoldContext = context;
          print("[page] $runtimeType - build scaffoldContext=$scaffoldContext");
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

  //region custom page lifecycle

  @override
  @mustCallSuper
  void pageDidAppear() {
    super.pageDidAppear();

    final ModalRoute<dynamic> parentRoute = ModalRoute.of(context);
    final bool canPop = parentRoute?.canPop ?? false;

    print("[page] $runtimeType - pageDidAppear canPop=$canPop");
  }

  @override
  @mustCallSuper
  void pageDidDisappear() {
    super.pageDidDisappear();
    print("[page] $runtimeType - pageDidDisappear");
  }

//endregion

  ///region push and pop

  Future<T> push<T extends Object>(BuildContext context, Route<T> route) {
    print("[page] $runtimeType - push");
    return Navigator.of(context).push(route);
  }

  Future<T> pushNamed<T extends Object>(BuildContext context, String routeName, {Object arguments}) {
    print("[page] $runtimeType - pushNamed $routeName");
    return Navigator.of(context).pushNamed<T>(routeName, arguments: arguments);
  }

  dynamic pop<T extends Object>(BuildContext context, [T result]) {
    print("[page] $runtimeType - pop");
    final ModalRoute<dynamic> parentRoute = ModalRoute.of(context);
    final bool canPop = parentRoute?.canPop ?? false;
    if (canPop) {
      Navigator.pop<T>(context, result);
      pageDidDisappear();
    } else {
      return FlutterRouter.closeCurrent();
    }
  }

  ///endregion

}
