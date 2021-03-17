import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:lib_flutter_base/lib_flutter_base.dart';

abstract class BaseState<T extends StatefulWidget> extends State<T> {
  final String uniqueId;

  // ignore: non_constant_identifier_names
  final String KEY_ARGUMENTS_JSON_STRING = "KEY_ARGUMENTS_JSON_STRING";
  final String argumentsJsonString;

  BaseState({this.uniqueId, this.argumentsJsonString}) : super();

  @override
  @mustCallSuper
  void initState() {
    super.initState();
    print(
        "[page] uniqueId=$uniqueId, $runtimeType - initState ${this.toStringShort()}");

    Event.addEventListener(KEY_ARGUMENTS_JSON_STRING, (eventName, eventData) {
      print(
          "[page] uniqueId=$uniqueId, $runtimeType - onMethodCallBack eventName=$eventName, eventData=$eventData");

      if (KEY_ARGUMENTS_JSON_STRING == eventName) {
        onPageResult(eventData);
      }
    }, containerId: uniqueId);
  }

  @mustCallSuper
  void onPageResult(dynamic data) {
    print("[page] uniqueId=$uniqueId, $runtimeType - onPageResult data=$data");
  }

  //region did change

  @override
  @mustCallSuper
  void didChangeDependencies() {
    super.didChangeDependencies();
    print("[page] uniqueId=$uniqueId, $runtimeType - didChangeDependencies");
  }

  //endregion

  @override
  @mustCallSuper
  void didUpdateWidget(Widget oldWidget) {
    super.didUpdateWidget(oldWidget);
    print("[page] uniqueId=$uniqueId, $runtimeType - didUpdateWidget");
  }

  @override
  @mustCallSuper
  void reassemble() {
    super.reassemble();
    print("[page] uniqueId=$uniqueId, $runtimeType - reassemble");
  }

  //当State对象从树中被移除时，会调用此回调
  @override
  @mustCallSuper
  void deactivate() {
    super.deactivate();
    print("[page] uniqueId=$uniqueId, $runtimeType - deactivate");
  }

  @override
  @mustCallSuper
  void dispose() {
    super.dispose();
    Event.removeEventListener(KEY_ARGUMENTS_JSON_STRING, containerId: uniqueId);
    print("[page] uniqueId=$uniqueId, $runtimeType - dispose");
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
      print("[page] uniqueId=$uniqueId, pop, call pageDidDisappear");
    } else {
      return BoostNavigator.of().pop([result]);
    }
  }
}
