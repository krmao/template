import 'package:smart/settings/imports/flutter_imports_material.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  void initState() {
    super.initState();

    FlutterBoost.singleton.registerPageBuilders({
      FlutterRouter.URL_SETTINGS: (pageName, params, _) => FlutterSettingsWidget(params),
      FlutterRouter.URL_ORDER: (pageName, params, _) => FlutterOrderWidget()
    });

    FlutterBoost.handleOnStartPage();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        title: 'Flutter Boost Test',
        builder: FlutterBoost.init(postPush: _onRoutePushed),
        home: Container());
  }

  void _onRoutePushed(String pageName, String uniqueId, Map params, Route route, Future _) {
    List<OverlayEntry> newEntries = route.overlayEntries
        .map((OverlayEntry entry) =>
        OverlayEntry(
            builder: (BuildContext context) {
              final pageWidget = entry.builder(context);
              return Stack(
                children: <Widget>[
                  pageWidget,
                  Positioned(
                      child: Text(
                          "pageName:$pageName\npageWidget:${pageWidget.toStringShort()}",
                          softWrap: true,
                          style: TextStyle(fontSize: 10.0, color: Colors.red)
                      ),
                      left: 5.0,
                      top: 90.0
                  )
                ],
              );
            },
            opaque: entry.opaque,
            maintainState: entry.maintainState))
        .toList(growable: true);

    route.overlayEntries.clear();
    route.overlayEntries.addAll(newEntries);
  }
}

class PushWidget extends StatefulWidget {
  @override
  _PushWidgetState createState() => _PushWidgetState();
}

class _PushWidgetState extends State<PushWidget> {
  @override
  Widget build(BuildContext context) {
    return FlutterOrderWidget(message: "Pushed Widget");
  }
}

class FlutterOrderWidget extends StatelessWidget {
  final String message;

  FlutterOrderWidget({this.message});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
            title: Text('订单页面')
        ),
        body: WidgetUtils.getColumn(context, null));
  }
}

class FlutterSettingsWidget extends StatelessWidget {
  final Map params;

  FlutterSettingsWidget(this.params);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
            title: Text('设置页面')
        ),
        body: WidgetUtils.getColumn(context, params));
  }
}

class WidgetUtils {


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
                onTap: () => Navigator.push(context, MaterialPageRoute(builder: (_) => PushWidget()))
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

