import 'package:flutter_module/settings/imports/flutter_imports_material.dart';

typedef void OnInitStateCallback();

class STBaseApplication extends StatefulWidget {
  final Widget child;
  final Color statusBarColor;
  final OnInitStateCallback onInitStateCallback;
  final bool enableSafeArea, enableSafeAreaTop, enableSafeAreaBottom, enableSafeAreaLeft, enableSafeAreaRight, enableExitWithDouble;

  STBaseApplication({Key key, @required this.child, this.statusBarColor = STBaseConstants.DEFAULT_STATUS_BAR_COLOR, this.enableSafeArea = true, this.enableSafeAreaTop = true, this.enableSafeAreaBottom = true, this.enableSafeAreaLeft = true, this.enableSafeAreaRight = true, this.enableExitWithDouble = false, this.onInitStateCallback}) : super(key: key);

  @override
  State<StatefulWidget> createState() => STBaseApplicationState(child: this.child, statusBarColor: this.statusBarColor, enableSafeArea: this.enableSafeArea, enableSafeAreaTop: this.enableSafeAreaTop, enableSafeAreaBottom: this.enableSafeAreaBottom, enableSafeAreaLeft: this.enableSafeAreaLeft, enableSafeAreaRight: this.enableSafeAreaRight, enableExitWithDouble: this.enableExitWithDouble, onInitStateCallback: this.onInitStateCallback);
}

class STBaseApplicationState extends State<STBaseApplication> {
  final Widget child;
  final Color statusBarColor;
  final OnInitStateCallback onInitStateCallback;
  final bool enableSafeArea, enableSafeAreaTop, enableSafeAreaBottom, enableSafeAreaLeft, enableSafeAreaRight, enableExitWithDouble;

  STBaseApplicationState({@required this.child, this.statusBarColor = STBaseConstants.DEFAULT_STATUS_BAR_COLOR, this.enableSafeArea = true, this.enableSafeAreaTop = true, this.enableSafeAreaBottom = true, this.enableSafeAreaLeft = true, this.enableSafeAreaRight = true, this.enableExitWithDouble = false, this.onInitStateCallback}) : super();

  @override
  void initState() {
    super.initState();
    if (this.onInitStateCallback != null) this.onInitStateCallback();
  }

  @override
  Widget build(BuildContext context) {
    SystemChrome.setSystemUIOverlayStyle(SystemUiOverlayStyle(
      statusBarColor: Colors.transparent, // android >= M
      statusBarBrightness: Brightness.dark, // ios
      statusBarIconBrightness: Brightness.light, // android >= M
    ));
    return MaterialApp(
        home: Scaffold(
            backgroundColor: this.statusBarColor,
            // android status bar and iphone X top and bottom edges color
            body: Builder(builder: (BuildContext context) {
              return SafeArea(top: this.enableSafeArea && this.enableSafeAreaTop, left: this.enableSafeArea && this.enableSafeAreaLeft, right: this.enableSafeArea && this.enableSafeAreaRight, bottom: this.enableSafeArea && this.enableSafeAreaBottom, child: WillPopScope(child: this.child, onWillPop: () => _processExit(context)));
            })),
        theme: ThemeData(
            // This is the theme of your application.
            //
            // Try running your application with "flutter run". You'll see the
            // application has a blue toolbar. Then, without quitting the app, try
            // changing the primarySwatch below to Colors.green and then invoke
            // "hot reload" (press "r" in the console where you ran "flutter run",
            // or simply save your changes to "hot reload" in a Flutter IDE).
            // Notice that the counter didn't reset back to zero; the application
            // is not restarted.
            primarySwatch: Colors.red,
            primaryColor: STBaseConstants.PRIMARY_COLOR,
            accentColor: STBaseConstants.ACCENT_COLOR,
            primaryColorBrightness: Brightness.light,
            hintColor: STBaseConstants.HINT_COLOR,
            highlightColor: STBaseConstants.HIGHLIGHT_COLOR,
            inputDecorationTheme: InputDecorationTheme(labelStyle: TextStyle(color: STBaseConstants.INPUT_DECORATION_COLOR))));
  }

  var _lastTime = 0;

  Future<bool> _processExit(BuildContext context) {
    if (this.enableExitWithDouble) {
      return Future.value(true);
    }
    int now = DateTime.now().millisecondsSinceEpoch;
    var duration = now - _lastTime;
    print("_processExit -> now:$now, _lastTime:$_lastTime, duration:$duration");
    _lastTime = now;
    if (duration > 1500) {
      print("_processExit -> context==null?${context == null}");
      Scaffold.of(context).showSnackBar(SnackBar(content: Text("再按一次退出"), duration: Duration(milliseconds: 2000)));
      return Future.value(false);
    } else {
      return Future.value(true);
    }
  }
}

// ignore: unused_element
class _NoOverScrollBehavior extends ScrollBehavior {
  @override
  Widget buildViewportChrome(BuildContext context, Widget child, AxisDirection axisDirection) {
    return child;
  }
}
