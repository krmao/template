import 'package:flutter/painting.dart';
import 'package:flutter_module/settings/imports/flutter_imports_material.dart';

class BridgeWidget extends StatelessWidget {
  Widget getItemWidget(String title, GestureTapCallback onTap) {
    return InkWell(
        highlightColor: Color(0x191970), // 点击时，水波纹的底色颜色
        child: Container(
            width: double.infinity,
            padding: const EdgeInsets.only(top: 15.0, bottom: 15.0),
            margin: const EdgeInsets.only(bottom: 2.5),
            color: Color(0x4169E1),
            child: Text(
              title,
              textAlign: TextAlign.start,
              style: TextStyle(fontSize: 15.0, color: Colors.white, fontWeight: FontWeight.bold, fontStyle: FontStyle.italic),
            )),
        onTap: onTap);
  }

  Widget getItemWithEditWidget(String title, GestureTapCallback onTap) {
    return Row(
      children: <Widget>[
        Expanded(
          child: InkWell(
              highlightColor: Color(0x191970), // 点击时，水波纹的底色颜色
              child: Container(
                  width: double.infinity,
                  padding: const EdgeInsets.only(top: 15.0, bottom: 15.0),
                  margin: const EdgeInsets.only(bottom: 2.5),
                  color: Color(0x4169E1),
                  child: Text(
                    title,
                    textAlign: TextAlign.start,
                    style: TextStyle(fontSize: 15.0, color: Colors.white, fontWeight: FontWeight.bold, fontStyle: FontStyle.italic),
                  )),
              onTap: onTap),
          flex: 1,
        ),
        Expanded(
          child: TextField(
            maxLines: 1,
            textAlign: TextAlign.start,
            style: TextStyle(fontSize: 15.0, color: Colors.black, fontWeight: FontWeight.bold, fontStyle: FontStyle.italic),
            onChanged: (text) {
              inputValue = text;
            },
            onEditingComplete: () {},
            onSubmitted: (text) {},
          ),
          flex: 1,
        ),
      ],
    );
  }

  String inputValue = "";

  @override
  Widget build(BuildContext context) {
    print("BridgeWidget build");
    return Scaffold(
        backgroundColor: Color(0xD3D3D3),
        appBar: AppBar(title: Text('BRIDGE')),
        body: new SingleChildScrollView(
            scrollDirection: Axis.vertical,
            padding: const EdgeInsets.all(10.0),
            physics: BouncingScrollPhysics(),
            reverse: false,
            child: new Column(children: <Widget>[
              getItemWidget('1. open new page', () => {}),
              getItemWidget('2. close current page with result', () => {}),
              getItemWidget('3. show toast', () => {}),
              getItemWithEditWidget('4. put value', () => {}),
              getItemWidget('5. get value', () => {}),
              getItemWidget('6. get user info', () => {}),
              getItemWidget('7. get location info', () => {}),
              getItemWidget('8. get device info', () => {}),
            ])));
  }
}
