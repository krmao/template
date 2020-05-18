import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:flutter_module/modules/bridge/bridge.dart';
import 'package:flutter_module/modules/order/order_widget.dart';
import 'package:flutter_module/settings/imports/flutter_imports_material.dart';

import 'modules/list/list.dart';
import 'modules/notfound/not_found_widget.dart';

void main() => runApp(_widgetForRoute(window.defaultRouteName));

/// smart://template/flutter?page=order&params=jsonString"
Widget _widgetForRoute(String route) {
  debugPaintSizeEnabled = false;

  Uri uri = Uri.parse(route);
  String page = uri.queryParameters['page'];
  String params = uri.queryParameters['params'];

  debugPrint("flutter main route=$route");
  debugPrint("flutter main page=$page, params=$params");

  switch (page) {
    case 'demo':
      return MyApp(homeWidget: MyHomePage(title: "Flutter PAGE DEMO"));
    case 'bridge':
      return MyApp(homeWidget: BridgeWidget());
    case 'order':
      return MyApp(homeWidget: OrderWidget());
    default:
      return MyApp(homeWidget: NotFoundWidget(route: route));
  }
}

class MyApp extends StatelessWidget {
  MyApp({Key key, this.homeWidget, this.primarySwatch = Colors.blue}) : super(key: key);
  final Widget homeWidget;
  final MaterialColor primarySwatch;

  @override
  Widget build(BuildContext context) {
    return MaterialApp(theme: ThemeData(primarySwatch: primarySwatch), home: homeWidget);
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title}) : super(key: key);

  // This widget is the home page of your application. It is stateful, meaning
  // that it has a State object (defined below) that contains fields that affect
  // how it looks.

  // This class is the configuration for the state. It holds the values (in this
  // case the title) provided by the parent (in this case the App widget) and
  // used by the build method of the State. Fields in a Widget subclass are
  // always marked "final".

  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  int _counter = 0;

  void _incrementCounter() {
    setState(() {
      // This call to setState tells the Flutter framework that something has
      // changed in this State, which causes it to rerun the build method below
      // so that the display can reflect the updated values. If we changed
      // _counter without calling setState(), then the build method would not be
      // called again, and so nothing would appear to happen.
      _counter++;
    });
  }

  @override
  Widget build(BuildContext context) {
    // This method is rerun every time setState is called, for instance as done
    // by the _incrementCounter method above.
    //
    // The Flutter framework has been optimized to make rerunning build methods
    // fast, so that you can just rebuild anything that needs updating rather
    // than having to individually change instances of widgets.
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Text('You have pushed the button this many times:'),
            Text('$_counter', style: Theme.of(context).textTheme.display1),
            STBaseWidgetManager.getOnTapWidget(
              new Image.asset(
                "images/icon_apple.png",
                width: 60.0,
                height: 60.0,
              ),
              () {
                //toast();
                STBaseWidgetManager.goTo(context, ListPage(name: "苹果列表"));
              },
            ),
          ],
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _incrementCounter,
        tooltip: 'Increment',
        child: Icon(Icons.add),
      ), // This trailing comma makes auto-formatting nicer for build methods.
    );
  }
}
