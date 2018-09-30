import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import 'CommonWidgetManager.dart';

const _CHANNEL_METHOD = "flutter.channel.method";
const platform = const MethodChannel(_CHANNEL_METHOD);

class TemplatePage extends StatefulWidget {
  @override
  createState() => new TemplatePageState();
}

class TemplatePageState extends State<TemplatePage> with AutomaticKeepAliveClientMixin<TemplatePage>, WidgetsBindingObserver {
  @override
  bool get wantKeepAlive => true;

  @override
  void initState() {
    super.initState();
    print("[TemplatePage] initSatte");
    WidgetsBinding.instance.addObserver(this);

    platform.setMethodCallHandler(_onNativeCallHandler);

    _request();
  }

  Future<dynamic> _onNativeCallHandler(MethodCall methodCall) async {
    print("_onNativeCallHandler -> ${methodCall.method}");
    switch (methodCall.method) {
      case 'push':
        CommonWidgetManager.goTo(context, CommonWidgetManager.widgetByRoute(methodCall.arguments), animation: false);
        print("_onNativeCallHandler -> ${methodCall.method}");
        return null;
      case 'pop':
        CommonWidgetManager.pop(context);
        return null;
      default:
        return null;
    }
  }

  @override
  void didChangeAppLifecycleState(AppLifecycleState state) {
    print("[TemplatePage] didChangeAppLifecycleState state=${state.toString()}");
    super.didChangeAppLifecycleState(state);
    switch (state) {
      case AppLifecycleState.inactive:
        break;
      case AppLifecycleState.paused:
        break;
      case AppLifecycleState.resumed:
        break;
      case AppLifecycleState.suspending:
        break;
      default:
        break;
    }
  }

  @override
  void dispose() {
    print("[TemplatePage] dispose");
    WidgetsBinding.instance.removeObserver(this);
    super.dispose();
  }

  bool _isShow = false;

  void _showLoading() => setState(() => _isShow = true);

  void _hideLoading() => setState(() => _isShow = false);

  BuildContext _scaffoldContext;

  void _showToast(String msg) => Scaffold.of(_scaffoldContext).showSnackBar(new SnackBar(content: new Text(msg)));

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      backgroundColor: Colors.black,
      body: new Builder(builder: (BuildContext context) {
        _scaffoldContext = context;
        return CommonWidgetManager.getCommonPageWidget(
            context,
            new Container(
              child: _body(),
            ),
            showLoading: _isShow,
            title: "文章详情",
            titleBackgroundColor: Color(0xFF0f0544),
            rightText: "通过Native跳转",
            onBackPressed: () => finish(),
            onRightPressed: () => goToByNative());
      }),
    );
  }

  // --CUSTOM ----------------------

  Widget _body() {
    return new Container();
  }

  void _request() {}

  void finish() {
    platform.invokeMethod('finish', ["finish with argument"]).then((value) {
      print("onNativeCallback:success:$value");
      _showToast("success:$value");
    }).catchError((error) {
      print("onNativeCallback:error:$error");
      _showToast("failure:$error");
    });
  }

// -
  void goToByNative() {
    platform.invokeMethod('goTo', ["haha, I am from flutter, nice to meet you :)"]).then((value) {
      print("onNativeCallback:success:$value");
      _showToast("success:$value");
    }).catchError((error) {
      print("onNativeCallback:error:$error");
      _showToast("failure:$error");
    });
  }
// --CUSTOM ----------------------
}
