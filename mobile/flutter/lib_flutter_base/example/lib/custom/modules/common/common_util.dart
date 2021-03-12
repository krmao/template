
import 'package:lib_flutter_base/lib_flutter_base.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import '../../settings/router/flutter_router.dart';

class CommonUtils {
  static Widget getColumn(BuildContext context, Map params) {
    return Column(crossAxisAlignment: CrossAxisAlignment.start, children: <Widget>[
      Container(
          margin: const EdgeInsets.only(top: 80.0),
          child: Text("params:${params == null ? "" : params['tag'] ?? ''}", style: TextStyle(fontSize: 20.0, color: Colors.blue)),
          alignment: AlignmentDirectional.center),
      Expanded(child: Container()),
      InkWell(
          child: Container(
              padding: const EdgeInsets.all(8.0),
              color: Colors.yellow,
              child: Text(
                'open flutter player',
                style: TextStyle(fontSize: 15.0, color: Colors.black),
              )),
          onTap: () => {
              BoostNavigator.of().push("FlutterPlayer",withContainer: true)
          }),
      InkWell(
          child: Container(
              padding: const EdgeInsets.all(8.0),
              color: Colors.yellow,
              child: Text(
                'open native mine',
                style: TextStyle(fontSize: 15.0, color: Colors.black),
              )),
          onTap: () => BoostNavigator.of().push("FlutterPlayer", arguments:{"urlParams": "1"},withContainer: true) ),
      InkWell(
          child: Container(
              padding: const EdgeInsets.all(8.0),
              color: Colors.yellow,
              child: Text(
                'open flutter order',
                style: TextStyle(fontSize: 15.0, color: Colors.black),
              )),
          onTap: () => BoostNavigator.of().push("FlutterOrder", arguments:{"urlParams": "1"},withContainer: true) ),
      InkWell(
          child: Container(
              padding: const EdgeInsets.all(8.0),
              color: Colors.yellow,
              child: Text(
                'open flutter settings',
                style: TextStyle(fontSize: 15.0, color: Colors.black),
              )),
          onTap: () {
              BoostNavigator.of().push("FlutterSettings", arguments:{"urlParams": "1"},withContainer: true);
          }),
      InkWell(
          child: Container(
              padding: const EdgeInsets.all(8.0),
              color: Colors.yellow,
              child: Text(
                'close(login:yes, token:kkk)',
                style: TextStyle(fontSize: 15.0, color: Colors.black),
              )),
          onTap: () {
              BoostNavigator.of().pop();
          })
    ]);
  }
}
