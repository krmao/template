import 'package:flutter/painting.dart';
import 'package:flutter_module/settings/imports/flutter_imports_material.dart';

/// https://api.flutter.dev/flutter/material/InkWell-class.html
class BridgeState extends STBaseStatefulWidgetState<STBaseStatefulWidget> {
  @override
  bool get wantKeepAlive => false;

  @override
  void initState() {
    /*titleBarWidget = STBaseTitleBarWidget(
            disableBack: true,
            title: "玩家首页",
        );
        loadingWidget = STBaseLoadingWidget(isShow: false);*/
    statusBarColor = Color(0xFFD3D3D3);
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
                  print("inputValue=$inputValue");
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
                    STBaseBridgeManager.open("smart://template/flutter?page=demo&params=jsonString").then((value) => {showSnackBar("return $value")})
                  }),
          getItemWidget(
              '2. close current page with result',
              () => {
                    STBaseBridgeManager.close("smart").then((value) => {showSnackBar("return $value")})
                  }),
          getItemWidget(
              '3. show toast',
              () => {
                    STBaseBridgeManager.showToast("I AM FROM FLUTTER").then((value) => {showSnackBar("return $value")})
                  }),
          getItemWithEditWidget(
              '4. put value',
              () => {
                    STBaseBridgeManager.put("inputValue", inputValue).then((value) => {showSnackBar("return $value")})
                  }),
          getItemWidget(
              '5. get value',
              () => {
                    STBaseBridgeManager.get("inputValue").then((value) => {showSnackBar("return $value")})
                  }),
          getItemWidget(
              '6. get user info',
              () => {
                    STBaseBridgeManager.getUserInfo().then((value) => {showSnackBar("return $value")})
                  }),
          getItemWidget(
              '7. get location info',
              () => {
                    STBaseBridgeManager.getLocationInfo().then((value) => {showSnackBar("return $value")})
                  }),
          getItemWidget(
              '8. get device info',
              () => {
                    STBaseBridgeManager.getDeviceInfo().then((value) => {showSnackBar("return $value")})
                  }),
        ]));
  }
}
