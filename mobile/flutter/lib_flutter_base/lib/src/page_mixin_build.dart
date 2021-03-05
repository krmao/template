import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:lib_flutter_base/lib_flutter_base.dart';

import 'page_mixin_variables.dart';
import 'widgets/widget_loading.dart';
import 'widgets/widget_titlebar.dart';

mixin PageMixinBuild on PageMixinVariables {
  //region build

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
      loadingWidget = Loading();
      children.add(loadingWidget);
    }
    if (enableTitleBar && titleBarWidget == null) {
      titleBarWidget = TitleBar(onBackPressed: () => pop(scaffoldContext));
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

}
