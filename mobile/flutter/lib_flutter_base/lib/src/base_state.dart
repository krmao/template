import 'dart:convert';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:lib_flutter_base/lib_flutter_base.dart';
import 'package:uuid/uuid.dart';

abstract class BaseState<T extends StatefulWidget> extends State<T> {
  final String pageUniqueId = Uuid().v1();

  @override
  @mustCallSuper
  void initState() {
    super.initState();
    print(
        "[page] pageUniqueId=$pageUniqueId, $runtimeType - initState ${this.toStringShort()}");

    BaseBridge.registerMethodCallBack("$pageUniqueId-listen-page-result",
        (methodName, arguments) {
      print(
          "[page] pageUniqueId=$pageUniqueId, $runtimeType - onMethodCallBack methodName=$methodName, arguments=$arguments");

      if (methodName == pageUniqueId) {
        dynamic eventData = json.decode(arguments);
        onPageResult(eventData);
      }
    });
  }

  @mustCallSuper
  void onPageResult(dynamic data) {
    print(
        "[page] pageUniqueId=$pageUniqueId, $runtimeType - onPageResult data=$data");
  }

  //region did change

  @override
  @mustCallSuper
  void didChangeDependencies() {
    super.didChangeDependencies();
    print(
        "[page] pageUniqueId=$pageUniqueId, $runtimeType - didChangeDependencies");
  }

  //endregion

  @override
  @mustCallSuper
  void didUpdateWidget(Widget oldWidget) {
    super.didUpdateWidget(oldWidget);
    print("[page] pageUniqueId=$pageUniqueId, $runtimeType - didUpdateWidget");
  }

  @override
  @mustCallSuper
  void reassemble() {
    super.reassemble();
    print("[page] pageUniqueId=$pageUniqueId, $runtimeType - reassemble");
  }

  //当State对象从树中被移除时，会调用此回调
  @override
  @mustCallSuper
  void deactivate() {
    super.deactivate();
    print("[page] pageUniqueId=$pageUniqueId, $runtimeType - deactivate");
  }

  @override
  @mustCallSuper
  void dispose() {
    super.dispose();
    BaseBridge.unregisterMethodCallBack("$pageUniqueId-listen-page-result");
    print("[page] pageUniqueId=$pageUniqueId, $runtimeType - dispose");
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
      print("[page] pageUniqueId=$pageUniqueId, pop, call pageDidDisappear");
    } else {
      return BoostNavigator.of().pop([result]);
    }
  }
}
