import 'package:smart/settings/imports/flutter_imports_material.dart';

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

    return
      Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: <Widget>[
            Container(
                margin: const EdgeInsets.only(top: 80.0),
                child: Text("params:${params == null ? "" : params['tag'] ?? ''}", style: TextStyle(fontSize: 28.0, color: Colors.blue)),
                alignment: AlignmentDirectional.center
            ),
            Expanded(child: Container()),
            InkWell(
                child: Container(
                    padding: const EdgeInsets.all(8.0),
                    margin: const EdgeInsets.all(8.0),
                    color: Colors.yellow,
                    child: Text('PUSH 玩家模块', style: TextStyle(fontSize: 22.0, color: Colors.black),)),
                onTap: () => Navigator.push(context, MaterialPageRoute(builder: (_) => STBaseStatefulWidget(state: MainTabWidgetState(),)))
            ),
            InkWell(
                child: Container(
                    padding: const EdgeInsets.all(8.0),
                    margin: const EdgeInsets.all(8.0),
                    color: Colors.yellow,
                    child: Text('打开我的页面(native)', style: TextStyle(fontSize: 22.0, color: Colors.black),)),
                onTap: () => FlutterRouter.open(FlutterRouter.URL_MINE).then((Map<String, dynamic> result){
                  print("URL_MINE did recieve second route result");
                  print("URL_MINE did recieve second route result $result");
                })
            ),
            InkWell(
                child: Container(
                    padding: const EdgeInsets.all(8.0),
                    margin: const EdgeInsets.all(8.0),
                    color: Colors.yellow,
                    child: Text('打开订单页面(flutter)', style: TextStyle(fontSize: 22.0, color: Colors.black),)),
                onTap: () => FlutterRouter.open(FlutterRouter.URL_ORDER).then((Map<String, dynamic> result){
                    print("URL_ORDER did recieve second route result");
                    print("URL_ORDER did recieve second route result $result");
                })
            ),
            InkWell(
                child: Container(
                    padding: const EdgeInsets.all(8.0),
                    margin: const EdgeInsets.fromLTRB(8.0, 8.0, 8.0, 80.0),
                    color: Colors.yellow,
                    child: Text('打开设置页面(flutter)', style: TextStyle(fontSize: 22.0, color: Colors.black),)),
                onTap: () {
                  FlutterRouter.open(FlutterRouter.URL_SETTINGS).then((Map<String, dynamic> result){
                    print("URL_SETTINGS did recieve second route result");
                    print("URL_SETTINGS did recieve second route result $result");
                  });
                }
            ),
            InkWell(
                child: Container(
                    padding: const EdgeInsets.all(8.0),
                    margin: const EdgeInsets.fromLTRB(8.0, 8.0, 8.0, 80.0),
                    color: Colors.yellow,
                    child: Text('close(login yes, token:kkk)', style: TextStyle(fontSize: 22.0, color: Colors.black),)),
                onTap: () {

                  FlutterRouter.close(settings.uniqueId,
                      result: {
                        "result":{
                          "name":settings.name,
                          "params":settings.params,
                          "login":1,
                          "token":"kkk"
                        }
                  });
                }
            )
          ]
      );
  }
}

