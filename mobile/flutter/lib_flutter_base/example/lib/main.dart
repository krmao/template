import 'dart:ui' as ui;

import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'package:lib_flutter_base/lib_flutter_base.dart';

import 'router_manager.dart';

/// https://flutter.dev/docs/development/add-to-app/debugging
/// https://flutter.cn/docs/development/add-to-app/debugging
/// flutter attach
/// flutter attach -d deviceId # AKC7N19118000852
/// flutter attach --isolate-filter='debug'
void initAndRunApp(String initialRoute) {
  ui.window.setIsolateDebugName("debug isolate");

  debugPaintSizeEnabled = false;
  debugPaintBaselinesEnabled = false;
  debugPrintLayouts = false;
  debugPaintLayerBordersEnabled = false;
  debugPaintPointersEnabled = false;
  debugRepaintRainbowEnabled = false;
  debugRepaintTextRainbowEnabled = false;

  appRun(MaterialApp(routes: {'/': RouterManager.instance.routeMap[initialRoute]}));
}

//region dartEntrypointFunctionName 仅比 initialRoute 多一个 main 前缀, 方便以后灵活的切换 '多引擎单路由' / '单引擎多路由'
@pragma('vm:entry-point') void mainFlutterBridge() => initAndRunApp("FlutterBridge");
@pragma('vm:entry-point') void mainFlutterSettings() => initAndRunApp("FlutterSettings");
@pragma('vm:entry-point') void mainFlutterOrder() => initAndRunApp("FlutterOrder");
@pragma('vm:entry-point') void mainFlutterPlayer() => initAndRunApp("FlutterPlayer");
//endregion

// void main() => initAndRunApp("/");


void main() => runApp(const MyApp());

/// This is the main application widget.
class MyApp extends StatelessWidget {
  const MyApp({Key key}) : super(key: key);

  static const String _title = 'Flutter Code Sample';

  @override
  Widget build(BuildContext context) {
    return const MaterialApp(
      title: _title,
      home: MyStatefulWidget(),
    );
  }
}

/// This is the stateful widget that the main application instantiates.
class MyStatefulWidget extends StatefulWidget {
  const MyStatefulWidget({Key key}) : super(key: key);

  @override
  _MyStatefulWidgetState createState() => _MyStatefulWidgetState();
}

/// This is the private State class that goes with MyStatefulWidget.
class _MyStatefulWidgetState extends State<MyStatefulWidget> {
  bool shouldPop = true;
  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: () async {
        return shouldPop;
      },
      child: Scaffold(
        appBar: AppBar(
          title: const Text('Flutter WillPopScope demo'),
        ),
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              OutlinedButton(
                child: const Text('Push'),
                onPressed: () {
                  Navigator.of(context).push<void>(
                    MaterialPageRoute<void>(
                      builder: (BuildContext context) {
                        return const MyStatefulWidget();
                      },
                    ),
                  );
                },
              ),
              OutlinedButton(
                child: Text('shouldPop: $shouldPop'),
                onPressed: () {
                  setState(
                        () {
                      shouldPop = !shouldPop;
                    },
                  );
                },
              ),
              const Text('Push to a new screen, then tap on shouldPop '
                  'button to toggle its value. Press the back '
                  'button in the appBar to check its behaviour '
                  'for different values of shouldPop'),
            ],
          ),
        ),
      ),
    );
  }
}

