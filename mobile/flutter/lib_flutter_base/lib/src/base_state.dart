import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:lib_flutter_base/lib_flutter_base.dart';
import 'package:uuid/uuid.dart';

abstract class BaseState<T extends StatefulWidget> extends State<T> {
  final String pageUniqueId = Uuid().v1();
  // ignore: non_constant_identifier_names
  final String KEY_ARGUMENTS_JSON_STRING = "KEY_ARGUMENTS_JSON_STRING";

  @override
  @mustCallSuper
  void initState() {
    super.initState();
    print(
        "[page] pageUniqueId=$pageUniqueId, $runtimeType - initState ${this.toStringShort()}");

    Event.addEventListener(KEY_ARGUMENTS_JSON_STRING,
        (eventName, eventData) {
      print(
          "[page] pageUniqueId=$pageUniqueId, $runtimeType - onMethodCallBack eventName=$eventName, eventData=$eventData");

      if (KEY_ARGUMENTS_JSON_STRING == eventName) {
        onPageResult(eventData);
      }
    }, containerId: pageUniqueId);
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
    Event.removeEventListener(KEY_ARGUMENTS_JSON_STRING,
        containerId: pageUniqueId);
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
