import 'dart:convert';
import 'dart:math';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:lib_flutter_base/lib_flutter_base.dart';

class BridgeWidgetState extends BaseStateDefault {
  TextEditingController textEditingController;

  String argumentsFromConstructor;
  DateTime argumentsFromConstructorTime;
  String argumentsFromAsyncGet;
  DateTime argumentsFromAsyncGetTime;
  String dataReturnPreOnBackPressed;
  DateTime dataReturnPreOnBackPressedTime;
  String dataFromNext;
  DateTime dataFromNextTime;

  BridgeWidgetState({uniqueId, argumentsJsonString})
      : super(uniqueId: uniqueId, argumentsJsonString: argumentsJsonString) {
    this.argumentsFromConstructor = argumentsJsonString;
    this.argumentsFromConstructorTime = DateTime.now();
    print(
        "[page] ---- BridgeWidgetState constructor argumentsJsonString=$argumentsJsonString");
  }

  @override
  bool get wantKeepAlive => true;

  @override
  void initState() {
    print("initState time=${DateTime.now()}");
    PageBridge.getCurrentPageInitArguments().then((value) {
      print("argumentsFromAsyncGet return value=$value time=${DateTime.now()}");
      setState(() {
        this.argumentsFromAsyncGet = "$value";
        this.argumentsFromAsyncGetTime = DateTime.now();
      });
    });

    dataReturnPreOnBackPressed = json.encode({"fromPage-next": uniqueId});

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
      dataFromNext = "received -> $data";
    });
  }

  @override
  void dispose() {
    print("[page] dispose ...");
    super.dispose();
  }

  @override
  Future<bool> onBackPressed(BuildContext context) {
    print("onBackPressed dataReturnPre=$dataReturnPreOnBackPressed");
    this.dataReturnPreOnBackPressedTime = DateTime.now();
    PageBridge.popPage(
        argumentsJsonString: json.encode({
      "returnTime": dataReturnPreOnBackPressedTime.toString(),
      "data": dataReturnPreOnBackPressed
    }));
    return Future.value(false);
  }

  @override
  Widget buildBaseChild(BuildContext context) {
    print("buildBaseChild time=${DateTime.now()}");
    return ListView(
      children: <Widget>[
        getItemWidget('[current-page-uniqueId]=${this.uniqueId}',
            () => BaseBridgeCompact.showToast("$uniqueId"),
            fontSize: 12.0, edge: 2),
        getItemWidget(
            '[arguments-Constructor]=(${this.argumentsFromConstructorTime})\n$argumentsFromConstructor',
            () => BaseBridgeCompact.showToast("$argumentsFromConstructor"),
            fontSize: 10.0,
            edge: 2),
        getItemWidget(
            '[arguments-AsyncGet]=(${this.argumentsFromAsyncGetTime})\n$argumentsFromAsyncGet',
            () => BaseBridgeCompact.showToast("$argumentsFromAsyncGet"),
            fontSize: 10.0,
            edge: 2),
        getItemWidget(
            '[data-fromNext]=(${this.dataFromNextTime})\n$dataFromNext',
            () => BaseBridgeCompact.showToast("$dataFromNext"),
            fontSize: 10.0,
            edge: 2),
        getItemWidget(
            '[data-returnOnBackPressed]=(${this.dataReturnPreOnBackPressedTime})\n$dataReturnPreOnBackPressed',
            () => BaseBridgeCompact.showToast("$dataReturnPreOnBackPressed"),
            fontSize: 10.0,
            edge: 2),
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
            () => PageBridge.pushPage("FlutterBridge",
                argument: {"fromPage-pre": uniqueId})),
        getItemWidget(
            'open flutter bridge 2',
            () => URL.openURL<dynamic>(
                'smart://template/flutter?page=FlutterBridge&params=' +
                    json.encode({"fromPage-pre": uniqueId}))),
        getItemWidget(
            'open flutter player',
            () => PageBridge.pushPage("FlutterPlayer",
                argument: {"fromPage-pre": uniqueId})),
        getItemWidget(
            'open flutter order',
            () => PageBridge.pushPage("FlutterOrder",
                argument: {"fromPage-pre": uniqueId})),
        getItemWidget(
            'open flutter settings',
            () => PageBridge.pushPage("FlutterSettings",
                argument: {"fromPage-pre": uniqueId})),
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
