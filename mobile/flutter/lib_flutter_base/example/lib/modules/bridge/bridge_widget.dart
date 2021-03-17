import 'dart:convert';
import 'dart:math';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:lib_flutter_base/lib_flutter_base.dart';

class BridgeWidgetState extends BaseStateDefault {
  TextEditingController textEditingController;

  String currentPageInitArgumentsFromConstructor;
  DateTime currentPageInitArgumentsFromConstructorGetTime;
  String currentPageInitArgumentsFromAsyncBridgeGet;
  DateTime currentPageInitArgumentsFromAsyncBridgeGetTime;
  String willReturnToPrePageData;
  String onNextPageReturnData;

  BridgeWidgetState({String argumentsJsonString})
      : super(argumentsJsonString: argumentsJsonString) {
    this.currentPageInitArgumentsFromConstructor = argumentsJsonString;
    this.currentPageInitArgumentsFromConstructorGetTime = DateTime.now();
    print(
        "[page] ---- BridgeWidgetState constructor argumentsJsonString=$argumentsJsonString");
  }

  @override
  bool get wantKeepAlive => true;

  @override
  void initState() {
    print("initState time=${DateTime.now()}");
    PageBridge.getCurrentPageInitArguments().then((value) {
      print(
          "getCurrentPageInitArguments return value=$value time=${DateTime.now()}");
      setState(() {
        this.currentPageInitArgumentsFromAsyncBridgeGet = "$value";
        this.currentPageInitArgumentsFromAsyncBridgeGetTime = DateTime.now();
      });
    });

    willReturnToPrePageData = json.encode({
      "name": "Jack",
      "age": 10,
      "gender": "boy",
      "from": pageUniqueId,
    });

    loadingWidget = BaseWidgetLoading(isShow: false);
    // statusBarColor =  Color(0xff00008b);
    statusBarColor =
        Colors.primaries[Random().nextInt(Colors.primaries.length)];
    super.initState();
    textEditingController = TextEditingController();
    textEditingController.addListener(() {
      print("textEditingController listener callback");
    });
  }

  @override
  void onPageResult(data) {
    super.onPageResult(data);
    setState(() {
      onNextPageReturnData = "received -> $data";
    });
  }

  @override
  void dispose() {
    print("[page] dispose ...");
    super.dispose();
  }

  @override
  Future<bool> onBackPressed(BuildContext context) {
    print("onBackPressed willReturnToPrePageData=$willReturnToPrePageData");
    PageBridge.popPage(argumentsJsonString: willReturnToPrePageData);
    return Future.value(false);
  }

  @override
  Widget buildBaseChild(BuildContext context) {
    print("buildBaseChild time=${DateTime.now()}");
    return ListView(
      children: <Widget>[
        getItemWidget(
            'currentPageInitArgumentsFromConstructor=(${this.currentPageInitArgumentsFromConstructorGetTime})\n$currentPageInitArgumentsFromConstructor',
            () => BaseBridgeCompact.showToast(
                "$currentPageInitArgumentsFromConstructor"),
            fontSize: 10.0,
            edge: 2),
        getItemWidget(
            'currentPageInitArgumentsFromAsyncBridgeGet=(${this.currentPageInitArgumentsFromAsyncBridgeGetTime})\n$currentPageInitArgumentsFromAsyncBridgeGet',
            () => BaseBridgeCompact.showToast(
                "$currentPageInitArgumentsFromAsyncBridgeGet"),
            fontSize: 10.0,
            edge: 2),
        getItemWidget('onNextPageReturnData=\n$onNextPageReturnData',
            () => BaseBridgeCompact.showToast("$onNextPageReturnData"),
            fontSize: 10.0, edge: 2),
        getItemWidget('willReturnToPrePageData=\n$willReturnToPrePageData',
            () => BaseBridgeCompact.showToast("$willReturnToPrePageData"),
            fontSize: 10.0, edge: 2),
        getItemWidget(
            'open new page',
            () => BaseBridgeCompact.open(
                "smart://template/flutter?page=flutter_settings&params=")),
        getItemWidget('close current page', () => PageBridge.popPage()),
        getItemWidget(
            'show toast',
            () =>
                {BaseBridgeCompact.showToast("I am native Compact Toast!!!")}),
        getItemWidgetWithInput(
          textEditingController,
          'put value',
          () => {
            BaseBridgeCompact.put("userName", textEditingController.text).then(
              (value) => {BaseWidgetUtil.showSnackBar(scaffoldContext, value)},
            ),
          },
        ),
        getItemWidget(
          'get value',
          () => {
            BaseBridgeCompact.get("userName").then(
              (value) => {BaseWidgetUtil.showSnackBar(scaffoldContext, value)},
            ),
          },
        ),
        getItemWidget(
          'get user info',
          () => {
            BaseBridgeCompact.getUserInfo().then(
              (value) => {BaseWidgetUtil.showSnackBar(scaffoldContext, value)},
            ),
          },
        ),
        getItemWidget(
          'get location info',
          () => {
            BaseBridgeCompact.getLocationInfo().then(
              (value) => {BaseWidgetUtil.showSnackBar(scaffoldContext, value)},
            )
          },
        ),
        getItemWidget(
          'get device info',
          () => {
            BaseBridgeCompact.getDeviceInfo().then(
              (value) => {BaseWidgetUtil.showSnackBar(scaffoldContext, value)},
            ),
          },
        ),
        getItemWidget('show toast by Toast plugin',
            () => {Toast.show("I am native Toast!!!")}),
        getItemWidget(
            'open flutter bridge',
            () => PageBridge.pushPage("FlutterBridge", argument: {
                  "pageUniqueId": pageUniqueId,
                  "name": "li bridge",
                })),
        getItemWidget(
            'open flutter bridge 2',
            () => URL.openURL<dynamic>(
                'smart://template/flutter?page=FlutterBridge&params=' +
                    json.encode({
                      "name": "mate",
                    }))),
        getItemWidget(
            'open flutter player',
            () => PageBridge.pushPage("FlutterPlayer", argument: {
                  "pageUniqueId": pageUniqueId,
                  "name": "li player",
                })),
        getItemWidget(
            'open flutter order',
            () => PageBridge.pushPage("FlutterOrder", argument: {
                  "pageUniqueId": pageUniqueId,
                  "name": "li order",
                })),
        getItemWidget('open flutter settings',
            () => PageBridge.pushPage("FlutterSettings")),
      ],
    );
  }

  static Widget getItemWidget(String label, GestureTapCallback onTap,
      {double fontSize = 12.0, double edge = 7.0, double height = 40.0}) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 2.5),
      child: Material(
        color: Colors.red,
        child: Ink(
            color: Color(0xff4169e1),
            child: InkWell(
                onTap: () =>
                    Future.delayed(Duration(milliseconds: 200), () => onTap()),
                // 打开页面看不到水波纹效果, 加延时就可以
                splashColor: Color(0xff191970),
                borderRadius: new BorderRadius.all(new Radius.circular(0.0)),
                child: Container(
                    width: double.infinity,
                    height: height,
                    alignment: Alignment.centerLeft,
                    padding: EdgeInsets.only(
                        left: 10, right: 10, top: edge, bottom: edge),
                    child: Text(label,
                        style: TextStyle(
                            fontSize: fontSize,
                            color: Colors.white,
                            fontWeight: FontWeight.normal))))),
      ),
    );
  }

  static Widget getItemWidgetWithInput(
      TextEditingController controller, String label, GestureTapCallback onTap,
      {double fontSize = 12.0, double edge = 7.0, double height = 40.0}) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 2.5),
      child: Row(
        children: [
          Expanded(
            flex: 5,
            child: Material(
              color: Colors.red,
              child: Ink(
                color: Color(0xff4169e1),
                child: InkWell(
                  onTap: () => Future.delayed(
                      Duration(milliseconds: 200), () => onTap()),
                  // 打开页面看不到水波纹效果, 加延时就可以
                  splashColor: Color(0xff191970),
                  borderRadius: new BorderRadius.all(new Radius.circular(0.0)),
                  child: Container(
                    width: double.infinity,
                    height: height,
                    alignment: Alignment.centerLeft,
                    padding: EdgeInsets.only(
                        left: 10, right: 10, top: edge, bottom: edge),
                    child: Text(
                      label,
                      style: TextStyle(
                          fontSize: 12.0,
                          color: Colors.white,
                          fontWeight: FontWeight.normal),
                    ),
                  ),
                ),
              ),
            ),
          ),
          Expanded(
            flex: 2,
            child: Container(
              height: height,
              alignment: Alignment.centerLeft,
              padding:
                  EdgeInsets.only(left: 10, right: 10, top: edge, bottom: edge),
              child: TextField(
                controller: controller,
                keyboardType: TextInputType.text,
                maxLines: 1,
                style: TextStyle(
                    color: Colors.white,
                    fontSize: 12.0,
                    fontWeight: FontWeight.normal),
                decoration: InputDecoration.collapsed(
                  hintText: '请输入',
                  hintStyle: TextStyle(
                      color: Colors.grey,
                      fontSize: 12.0,
                      fontWeight: FontWeight.normal),
                ),
                onChanged: (value) => {print("onChanged $value")},
                autofocus: false,
              ),
            ),
          )
        ],
      ),
    );
  }
}
