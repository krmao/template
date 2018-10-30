import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import 'Constants.dart';

var _lastTime = 0;

class DefaultApp extends StatefulWidget {
    StatefulWidget child;
    Color statusBarColor;
    bool enableSafeArea, enableSafeAreaTop, enableSafeAreaBottom, enableSafeAreaLeft, enableSafeAreaRight;

    DefaultApp({Key key, @required this.child, this.statusBarColor = Constants.DEFAULT_STATUS_BAR_COLOR, this.enableSafeArea = true, this.enableSafeAreaTop = true, this.enableSafeAreaBottom = true, this.enableSafeAreaLeft = true, this.enableSafeAreaRight = true}) :super(key: key);

    @override
    State<StatefulWidget> createState() {
        return _DefaultAppState();
    }
}

class _DefaultAppState extends State<DefaultApp> {
    @override
    Widget build(BuildContext context) {

        /*SystemChrome.setSystemUIOverlayStyle(SytemUiOverlayStyle(
            statusBarColor: Colors.black, // android >= M
            statusBarBrightness: Brightness.light, // ios
            statusBarIconBrightness: Brightness.light, // android >= M
            ));*/

        return MaterialApp(
            home: Scaffold(backgroundColor: widget.statusBarColor, // android status bar and iphone X top and bottom edges color
                body: SafeArea(
                    top: widget.enableSafeArea && widget.enableSafeAreaTop,
                    left: widget.enableSafeArea && widget.enableSafeAreaLeft,
                    right: widget.enableSafeArea && widget.enableSafeAreaRight,
                    bottom: widget.enableSafeArea && widget.enableSafeAreaBottom,
                    child: WillPopScope(child: widget.child, onWillPop: () => _processExit(context)))),
            theme: ThemeData(primaryColor: Colors.yellow,
                accentColor: Colors.limeAccent,
                primaryColorBrightness: Brightness.light,
                hintColor: Colors.black12,
                highlightColor: Colors.transparent,
                inputDecorationTheme: InputDecorationTheme(labelStyle: TextStyle(color: Color(0xffdddddd)))));
    }
}

Future<bool> _processExit(BuildContext context) {
    int now = DateTime
        .now()
        .millisecondsSinceEpoch;
    var duration = now - _lastTime;
    print("_processExit -> now:$now, _lastTime:$_lastTime, duration:$duration");
    _lastTime = now;
    if (duration > 1500) {
        Scaffold.of(context).showSnackBar(SnackBar(content: Text("再按一次退出")));
        return Future.value(false);
    } else {
        return Future.value(true);
    }
}