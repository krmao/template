import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:lib_flutter_base/lib_flutter_base.dart';

class BridgeWidgetState extends BaseStateDefault {
  TextEditingController textEditingController;

  @override
  bool get wantKeepAlive => true;

  @override
  void initState() {
    loadingWidget = BaseWidgetLoading(isShow: false);
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
            'open flutter bridge', () => PageBridge.pushPage("FlutterBridge")),
        getItemWidget(
            'open flutter player', () => PageBridge.pushPage("FlutterPlayer")),
        getItemWidget(
            'open flutter order', () => PageBridge.pushPage("FlutterOrder")),
        getItemWidget('open flutter settings',
            () => PageBridge.pushPage("FlutterSettings")),
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
                onTap: () => Future.delayed(Duration(milliseconds: 200),
                    () => onTap()), // 打开页面看不到水波纹效果, 加延时就可以
                splashColor: Color(0xff191970),
                borderRadius: new BorderRadius.all(new Radius.circular(0.0)),
                child: Container(
                    width: double.infinity,
                    alignment: Alignment.centerLeft,
                    padding: const EdgeInsets.all(15.0),
                    child: Text(label,
                        style: TextStyle(
                            fontSize: 15.0,
                            color: Colors.white,
                            fontWeight: FontWeight.bold))))),
      ),
    );
  }

  static Widget getItemWidgetWithInput(TextEditingController controller,
      String label, GestureTapCallback onTap) {
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
                  onTap: () => Future.delayed(Duration(milliseconds: 200),
                      () => onTap()), // 打开页面看不到水波纹效果, 加延时就可以
                  splashColor: Color(0xff191970),
                  borderRadius: new BorderRadius.all(new Radius.circular(0.0)),
                  child: Container(
                    width: double.infinity,
                    alignment: Alignment.centerLeft,
                    padding: const EdgeInsets.all(15.0),
                    child: Text(
                      label,
                      style: TextStyle(
                          fontSize: 15.0,
                          color: Colors.white,
                          fontWeight: FontWeight.bold),
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
                style: TextStyle(
                    color: Colors.white,
                    fontSize: 15.0,
                    fontWeight: FontWeight.bold),
                decoration: InputDecoration.collapsed(
                  hintText: '请输入',
                  hintStyle: TextStyle(
                      color: Colors.grey,
                      fontSize: 15.0,
                      fontWeight: FontWeight.bold),
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
