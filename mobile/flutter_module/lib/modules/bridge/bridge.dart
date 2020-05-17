import 'package:flutter/painting.dart';
import 'package:flutter_module/settings/imports/flutter_imports_material.dart';

class BridgeWidget extends StatelessWidget {
  static Widget getItemWidget(String title, GestureTapCallback onTap) {
    return InkWell(
        highlightColor: Color(0x191970ff),
        focusColor: Color(0x191970ff),
        hoverColor: Color(0x191970ff),
        child: Container(
            padding: const EdgeInsets.all(15.0),
            margin: const EdgeInsets.only(bottom: 2.5),
            color: Color(0x4169e1ff),
            child: Text(
              title,
              textAlign: TextAlign.start,
              style: TextStyle(fontSize: 15.0, color: Colors.white, fontWeight: FontWeight.bold, fontStyle: FontStyle.italic),
            )),
        onTap: onTap);
  }

  @override
  Widget build(BuildContext context) {
    print("BridgeWidget build");
    return Scaffold(
        backgroundColor: Color(0x00008bff),
        appBar: AppBar(title: Text('BRIDGE FLUTTER CALL NATIVE')),
        body: new SingleChildScrollView(
            scrollDirection: Axis.vertical,
            padding: const EdgeInsets.all(10.0),
            physics: BouncingScrollPhysics(),
            reverse: false,
            child: new Column(children: <Widget>[
              getItemWidget('1. open new page', () => {}),
              getItemWidget('2. close current page with result', () => {}),
              getItemWidget('3. show toast', () => {}),
              getItemWidget('4. put value', () => {}),
              getItemWidget('5. get value', () => {}),
              getItemWidget('6. get user info', () => {}),
              getItemWidget('7. get location info', () => {}),
              getItemWidget('8. get device info', () => {}),
            ])));
  }
}
