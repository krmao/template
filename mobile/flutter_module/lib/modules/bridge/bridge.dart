import 'package:flutter/painting.dart';
import 'package:flutter_module/settings/imports/flutter_imports_material.dart';

/// https://api.flutter.dev/flutter/material/InkWell-class.html
class BridgeWidget extends StatefulWidget {
  static const CHANNEL_METHOD = const MethodChannel("smart.template.flutter/method");

  Future<dynamic> onMethodCallHandler(MethodCall call) async {
    switch (call.method) {
      case "":
        break;
    }
  }

  @override
  State<StatefulWidget> createState() {
    CHANNEL_METHOD.setMethodCallHandler(onMethodCallHandler);
    return BridgeState();
  }
}

class BridgeState extends STBaseStatefulWidgetState<BridgeWidget> {
  @override
  void initState() {
    statusBarColor = null;
    super.initState();
  }

  Widget getItemWidget(String title, GestureTapCallback onTap) {
    return Container(
        margin: const EdgeInsets.only(bottom: 2.5),
        child: Material(
          color: Color(0xFF4169E1),
          child: InkWell(
              // https://api.flutter.dev/flutter/material/InkWell-class.html
              child: Container(
                  width: double.infinity,
                  padding: const EdgeInsets.only(top: 15.0, bottom: 15.0, left: 20.0, right: 20.0),
                  child: Text(
                    title,
                    textAlign: TextAlign.start,
                    style: TextStyle(fontSize: 15.0, color: Colors.white, fontWeight: FontWeight.bold, fontStyle: FontStyle.italic),
                  )),
              onTap: onTap),
        ));
  }

  Widget getItemWithEditWidget(String title, GestureTapCallback onTap) {
    return Container(
        margin: const EdgeInsets.only(bottom: 2.5),
        child: Row(
          children: <Widget>[
            Expanded(
              child: Material(
                  color: Color(0xFF4169E1),
                  child: InkWell(
                      child: Container(
                          width: double.infinity,
                          padding: const EdgeInsets.only(top: 15.0, bottom: 15.0, left: 20.0),
                          child: Text(
                            title,
                            textAlign: TextAlign.start,
                            style: TextStyle(fontSize: 15.0, color: Colors.white, fontWeight: FontWeight.bold, fontStyle: FontStyle.italic),
                          )),
                      onTap: onTap)),
              flex: 1,
            ),
            Expanded(
              child: TextField(
                maxLines: 1,
                textAlign: TextAlign.start,
                style: TextStyle(fontSize: 15.0, color: Colors.black, fontWeight: FontWeight.bold, fontStyle: FontStyle.italic),
                decoration: InputDecoration(filled: true, fillColor: Colors.white, hintText: '请输入', hintStyle: TextStyle(color: Color(0xff333333), fontSize: 15.0), contentPadding: const EdgeInsets.only(left: 5.0, right: 5.0), border: InputBorder.none, enabledBorder: null, disabledBorder: null),
                onChanged: (text) {
                  inputValue = text;
                },
                onEditingComplete: () {},
                onSubmitted: (text) {},
              ),
              flex: 1,
            ),
          ],
        ));
  }

  String inputValue = "";

  @override
  Widget buildBody() {
    print("BridgeWidget build");
    return new SingleChildScrollView(
        scrollDirection: Axis.vertical,
        padding: const EdgeInsets.only(top: 15.0, bottom: 15.0),
        physics: BouncingScrollPhysics(),
        reverse: false,
        child: new Column(children: <Widget>[
          getItemWidget(
              '1. open new page',
              () => {
                    BridgeWidget.CHANNEL_METHOD.invokeMethod("open", {"url": "smart://template/flutter?page=demo&params=jsonString"}).then((value) => {showSnackBar(value)})
                  }),
          getItemWidget(
              '2. close current page with result',
              () => {
                    BridgeWidget.CHANNEL_METHOD.invokeMethod("close").then((value) => {showSnackBar(value)})
                  }),
          getItemWidget(
              '3. show toast',
              () => {
                    BridgeWidget.CHANNEL_METHOD.invokeMethod("showToast", {"message": "I AM FROM FLUTTER"}).then((value) => {showSnackBar(value)})
                  }),
          getItemWithEditWidget(
              '4. put value',
              () => {
                    BridgeWidget.CHANNEL_METHOD.invokeMethod("put", {"key": "inputValue", "value": inputValue}).then((value) => {showSnackBar(value)})
                  }),
          getItemWidget(
              '5. get value',
              () => {
                    BridgeWidget.CHANNEL_METHOD.invokeMethod("get", {"key": "inputValue"}).then((value) => {showSnackBar(value)})
                  }),
          getItemWidget('6. get user info', () => {BridgeWidget.CHANNEL_METHOD.invokeMethod("getUserInfo")}),
          getItemWidget(
              '7. get location info',
              () => {
                    BridgeWidget.CHANNEL_METHOD.invokeMethod("getLocationInfo").then((value) => {showSnackBar(value)})
                  }),
          getItemWidget(
              '8. get device info',
              () => {
                    BridgeWidget.CHANNEL_METHOD.invokeMethod("getDeviceInfo").then((value) => {showSnackBar(value)})
                  }),
        ]));
  }
}
