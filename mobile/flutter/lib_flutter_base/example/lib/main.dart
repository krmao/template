import 'dart:ui' as ui;

import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'package:lib_flutter_base/lib_flutter_base.dart';

import 'modules/bridge/bridge_widget.dart';
import 'modules/order/order_widget.dart';
import 'modules/player/player_tab_widget_state.dart';
import 'modules/settings/settings_widget.dart';

/// https://flutter.dev/docs/development/add-to-app/debugging
/// https://flutter.cn/docs/development/add-to-app/debugging
/// flutter attach
/// flutter attach -d deviceId # AKC7N19118000852
/// flutter attach --isolate-filter='debug'
void initAndRunApp(RoutesBuilder routesBuilder) {
  ui.window.setIsolateDebugName("debug isolate");

  debugPaintSizeEnabled = false;
  debugPaintBaselinesEnabled = false;
  debugPrintLayouts = false;
  debugPaintLayerBordersEnabled = false;
  debugPaintPointersEnabled = false;
  debugRepaintRainbowEnabled = false;
  debugRepaintTextRainbowEnabled = false;

  appRun(routesBuilder);
}

//region dartEntrypointFunctionName 仅比 initialRoute 多一个 main 前缀, 方便以后灵活的切换 '多引擎单路由' / '单引擎多路由'
@pragma('vm:entry-point')
void mainFlutterBridge() => initAndRunApp((String argumentsJsonString) => {
      '/': (BuildContext context) => BasePageDefault(
          state: BridgeWidgetState(argumentsJsonString: argumentsJsonString))
    });

@pragma('vm:entry-point')
void mainFlutterSettings() => initAndRunApp((String argumentsJsonString) => {
      '/': (BuildContext context) => BasePageDefault(
          state: SettingsState({}, argumentsJsonString: argumentsJsonString))
    });

@pragma('vm:entry-point')
void mainFlutterOrder() => initAndRunApp((String argumentsJsonString) => {
      '/': (BuildContext context) =>
          OrderWidget(argumentsJsonString: argumentsJsonString)
    });

@pragma('vm:entry-point')
void mainFlutterPlayer() => initAndRunApp((String argumentsJsonString) => {
      '/': (BuildContext context) => BasePageDefault(
          state: MainTabWidgetState(argumentsJsonString: argumentsJsonString))
    });

void main() => initAndRunApp((String argumentsJsonString) => {
      '/': (BuildContext context) =>
          MyTestApp(argumentsJsonString: argumentsJsonString)
    });
//endregion

//region test app
// void main() => runApp(const MyApp());

/// This is the main application widget.
class MyTestApp extends BasePageStateless {
  const MyTestApp({Key key, String argumentsJsonString})
      : super(key: key, argumentsJsonString: argumentsJsonString);

  static const String _title = 'Flutter Code Sample';

  @override
  Widget build(BuildContext context) {
    print("[page] argumentsJsonString=${this.argumentsJsonString}");
    return const MaterialApp(
      title: _title,
      home: MyTestStatefulWidget(),
    );
  }
}

/// This is the stateful widget that the main application instantiates.
class MyTestStatefulWidget extends StatefulWidget {
  const MyTestStatefulWidget({Key key}) : super(key: key);

  @override
  _MyTestStatefulWidgetState createState() => _MyTestStatefulWidgetState();
}

/// This is the private State class that goes with MyStatefulWidget.
class _MyTestStatefulWidgetState extends State<MyTestStatefulWidget> {
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
                        return const MyTestStatefulWidget();
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
//endregion
