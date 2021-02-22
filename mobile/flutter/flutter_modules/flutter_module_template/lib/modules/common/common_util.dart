
import 'package:flutter_codesdancing/flutter_codesdancing.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_module_template/settings/router/flutter_router.dart';

class CommonUtils {
  static Widget getColumn(BuildContext context, Map params) {
    BoostContainerSettings settings = BoostContainer.of(context).settings;
    print("settings ------------->");
    print(settings);
    print(settings.uniqueId);
    print(settings.params);
    print(settings.name);
    print(settings.toString());
    print("settings <-------------");

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
              RouterUtil.open(FlutterRouter.URL_FLUTTER_PLAYER)
          }),
      InkWell(
          child: Container(
              padding: const EdgeInsets.all(8.0),
              color: Colors.yellow,
              child: Text(
                'open native mine',
                style: TextStyle(fontSize: 15.0, color: Colors.black),
              )),
          onTap: () => RouterUtil.open(FlutterRouter.URL_NATIVE_MINE, urlParams: {"urlParams": "1"}, exts: {"exts": "1"}).then((Map<dynamic, dynamic> result) {
                print("URL_MINE did recieve second route result");
                print("URL_MINE did recieve second route result $result");
              })),
      InkWell(
          child: Container(
              padding: const EdgeInsets.all(8.0),
              color: Colors.yellow,
              child: Text(
                'open flutter order',
                style: TextStyle(fontSize: 15.0, color: Colors.black),
              )),
          onTap: () => RouterUtil.open(FlutterRouter.URL_FLUTTER_ORDER).then((Map<dynamic, dynamic> result) {
                print("URL_ORDER did recieve second route result");
                print("URL_ORDER did recieve second route result $result");
              })),
      InkWell(
          child: Container(
              padding: const EdgeInsets.all(8.0),
              color: Colors.yellow,
              child: Text(
                'open flutter settings',
                style: TextStyle(fontSize: 15.0, color: Colors.black),
              )),
          onTap: () {
              RouterUtil.open(FlutterRouter.URL_FLUTTER_SETTINGS).then((Map<dynamic, dynamic> result) {
              print("URL_SETTINGS did recieve second route result");
              print("URL_SETTINGS did recieve second route result $result");
            });
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
              RouterUtil.close(settings.uniqueId, result: {
              "result": {"name": settings.name, "params": settings.params, "login": 1, "token": "kkk"}
            });
          })
    ]);
  }
}
