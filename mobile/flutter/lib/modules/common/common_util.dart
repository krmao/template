import 'package:smart/settings/imports/flutter_imports_material.dart';

class CommonUtils {


  static Widget getColumn(BuildContext context, Map params) {
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
                    child: Text('内部路由 Navigator.push', style: TextStyle(fontSize: 22.0, color: Colors.black),)),
                onTap: () => Navigator.push(context, MaterialPageRoute(builder: (_) => STBaseStatefulWidget(state: MainTabWidgetState(),)))
            ),
            InkWell(
                child: Container(
                    padding: const EdgeInsets.all(8.0),
                    margin: const EdgeInsets.all(8.0),
                    color: Colors.yellow,
                    child: Text('打开我的页面(native)', style: TextStyle(fontSize: 22.0, color: Colors.black),)),
                onTap: () => FlutterBoost.singleton.openPage(FlutterRouter.URL_MINE, {})
            ),
            InkWell(
                child: Container(
                    padding: const EdgeInsets.all(8.0),
                    margin: const EdgeInsets.all(8.0),
                    color: Colors.yellow,
                    child: Text('打开订单页面(flutter)', style: TextStyle(fontSize: 22.0, color: Colors.black),)),
                onTap: () => FlutterBoost.singleton.openPage(FlutterRouter.URL_ORDER, {})
            ),
            InkWell(
                child: Container(
                    padding: const EdgeInsets.all(8.0),
                    margin: const EdgeInsets.fromLTRB(8.0, 8.0, 8.0, 80.0),
                    color: Colors.yellow,
                    child: Text('打开设置页面(flutter)', style: TextStyle(fontSize: 22.0, color: Colors.black),)),
                onTap: () => FlutterBoost.singleton.openPage(FlutterRouter.URL_SETTINGS, {})
            )
          ]
      );
  }
}

