import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import 'Constants.dart';

var _lastTime = 0;

class DefaultApp extends StatefulWidget {
    final StatefulWidget child;
    final Color statusBarColor;
    final bool enableSafeArea, enableSafeAreaTop, enableSafeAreaBottom, enableSafeAreaLeft, enableSafeAreaRight;

    DefaultApp({Key key, @required this.child, this.statusBarColor = Constants.DEFAULT_STATUS_BAR_COLOR, this.enableSafeArea = true, this.enableSafeAreaTop = true, this.enableSafeAreaBottom = true, this.enableSafeAreaLeft = true, this.enableSafeAreaRight = true}) :super(key: key);

    @override
    State<StatefulWidget> createState() => _DefaultAppState();
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
            theme: ThemeData(primaryColor: Constants.PRIMARY_COLOR,
                accentColor: Constants.ACCENT_COLOR,
                primaryColorBrightness: Brightness.dark,
                hintColor: Constants.HINT_COLOR,
                highlightColor: Constants.HIGHLIGHT_COLOR,
                inputDecorationTheme: InputDecorationTheme(labelStyle: TextStyle(color: Constants.INPUT_DECORATION_COLOR))));
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