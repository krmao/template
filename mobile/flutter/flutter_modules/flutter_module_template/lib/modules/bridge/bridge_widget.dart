import 'package:codesdancing_bridge/codesdancing_bridge.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_module_template/settings/router/flutter_router.dart';

class BridgeWidgetState extends PageState {
  TextEditingController textEditingController;

  @override
  bool get wantKeepAlive => true;

  @override
  void initState() {
    loadingWidget = Loading(isShow: false);
    statusBarColor = Color(0xff00008b);
    super.initState();
    textEditingController = TextEditingController();
    textEditingController.addListener(() {
      print("textEditingController listener callback");
    });
  }

  @override
  Widget buildBaseChild(BuildContext context) {
    return ListView(
      children: <Widget>[
        getItemWidget(
          'open new page',
          () => {
            BridgeCompact.open("smart://template/flutter?page=flutter_settings&params="),
          },
        ),
        getItemWidget(
          'close current page with result',
          () => {
            FlutterRouter.closeCurrent(
              result: {
                "result": {"name": "mm", "params": "aa", "login": 1, "token": "kkk"}
              },
            ),
          },
        ),
        getItemWidget('show toast', () => {BridgeCompact.showToast("I am native Compact Toast!!!")}),
        getItemWidgetWithInput(
          textEditingController,
          'put value',
          () => {
            BridgeCompact.put("userName", textEditingController.text).then(
              (value) => {WidgetUtil.showSnackBar(scaffoldContext, value)},
            ),
          },
        ),
        getItemWidget(
          'get value',
          () => {
            BridgeCompact.get("userName").then(
              (value) => {WidgetUtil.showSnackBar(scaffoldContext, value)},
            ),
          },
        ),
        getItemWidget(
          'get user info',
          () => {
            BridgeCompact.getUserInfo().then(
              (value) => {WidgetUtil.showSnackBar(scaffoldContext, value)},
            ),
          },
        ),
        getItemWidget(
          'get location info',
          () => {
            BridgeCompact.getLocationInfo().then(
              (value) => {WidgetUtil.showSnackBar(scaffoldContext, value)},
            )
          },
        ),
        getItemWidget(
          'get device info',
          () => {
            BridgeCompact.getDeviceInfo().then(
              (value) => {WidgetUtil.showSnackBar(scaffoldContext, value)},
            ),
          },
        ),
        getItemWidget('show toast by Toast plugin', () => {Toast.show("I am native Toast!!!")}),
        getItemWidget('open flutter-player by boost', () => {FlutterRouter.open(FlutterRouter.URL_FLUTTER_PLAYER)}),
        getItemWidget(
          'open native-mine by boost',
          () => {
            FlutterRouter.open(FlutterRouter.URL_NATIVE_MINE, urlParams: {"urlParams": "1"}, exts: {"exts": "1"}).then(
              (Map<String, dynamic> result) {
                print("URL_MINE did receive second route result");
                print("URL_MINE did receive second route result $result");
              },
            ),
          },
        ),
        getItemWidget(
          'open flutter-order by boost',
          () => {
            FlutterRouter.open(FlutterRouter.URL_FLUTTER_ORDER).then(
              (Map<String, dynamic> result) {
                print("URL_ORDER did recieve second route result");
                print("URL_ORDER did recieve second route result $result");
              },
            ),
          },
        ),
        getItemWidget(
          'open flutter-settings by boost',
          () => {
            FlutterRouter.open(FlutterRouter.URL_FLUTTER_SETTINGS).then(
              (Map<String, dynamic> result) {
                print("URL_SETTINGS did recieve second route result");
                print("URL_SETTINGS did recieve second route result $result");
              },
            ),
          },
        ),
      ],
    );
  }

  static Widget getItemWidget(String label, GestureTapCallback onTap) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 2.5),
      child: Material(
        color: Colors.red,
        child: Ink(
            color: Color(0xff4169e1),
            child: InkWell(
                onTap: () => Future.delayed(Duration(milliseconds: 200), () => onTap()), // 打开页面看不到水波纹效果, 加延时就可以
                splashColor: Color(0xff191970),
                borderRadius: new BorderRadius.all(new Radius.circular(0.0)),
                child: Container(width: double.infinity, alignment: Alignment.centerLeft, padding: const EdgeInsets.all(15.0), child: Text(label, style: TextStyle(fontSize: 15.0, color: Colors.white, fontWeight: FontWeight.bold))))),
      ),
    );
  }

  static Widget getItemWidgetWithInput(TextEditingController controller, String label, GestureTapCallback onTap) {
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
                  onTap: () => Future.delayed(Duration(milliseconds: 200), () => onTap()), // 打开页面看不到水波纹效果, 加延时就可以
                  splashColor: Color(0xff191970),
                  borderRadius: new BorderRadius.all(new Radius.circular(0.0)),
                  child: Container(
                    width: double.infinity,
                    alignment: Alignment.centerLeft,
                    padding: const EdgeInsets.all(15.0),
                    child: Text(
                      label,
                      style: TextStyle(fontSize: 15.0, color: Colors.white, fontWeight: FontWeight.bold),
                    ),
                  ),
                ),
              ),
            ),
          ),
          Expanded(
            flex: 2,
            child: Container(
              alignment: Alignment.centerLeft,
              padding: const EdgeInsets.only(left: 10.0, right: 10.0),
              child: TextField(
                controller: controller,
                keyboardType: TextInputType.text,
                maxLines: 1,
                style: TextStyle(color: Colors.white, fontSize: 15.0, fontWeight: FontWeight.bold),
                decoration: InputDecoration.collapsed(
                  hintText: '请输入',
                  hintStyle: TextStyle(color: Colors.grey, fontSize: 15.0, fontWeight: FontWeight.bold),
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
