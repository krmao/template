import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import 'Constants.dart';

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
        SystemChrome.setSystemUIOverlayStyle(SystemUiOverlayStyle(
            statusBarColor: Colors.transparent, // android >= M
            statusBarBrightness: Brightness.dark, // ios
            statusBarIconBrightness: Brightness.light, // android >= M
        ));
        return MaterialApp(
            builder: (context, child) {
                return ScrollConfiguration(child: child, behavior: NoOverScrollBehavior());
            },
            home: Scaffold(backgroundColor: widget.statusBarColor, // android status bar and iphone X top and bottom edges color
                body: Builder(builder: (BuildContext context) {
                    return SafeArea(
                        top: widget.enableSafeArea && widget.enableSafeAreaTop,
                        left: widget.enableSafeArea && widget.enableSafeAreaLeft,
                        right: widget.enableSafeArea && widget.enableSafeAreaRight,
                        bottom: widget.enableSafeArea && widget.enableSafeAreaBottom,
                        child: WillPopScope(child: widget.child, onWillPop: () => _processExit(context))
                    );
                })),
            theme: ThemeData(primaryColor: Constants.PRIMARY_COLOR,
                accentColor: Constants.ACCENT_COLOR,
                primaryColorBrightness: Brightness.light,
                hintColor: Constants.HINT_COLOR,
                highlightColor: Constants.HIGHLIGHT_COLOR,
                inputDecorationTheme: InputDecorationTheme(labelStyle: TextStyle(color: Constants.INPUT_DECORATION_COLOR))));
    }
}

class NoOverScrollBehavior extends ScrollBehavior {
    @override
    Widget buildViewportChrome(BuildContext context, Widget child, AxisDirection axisDirection) {
        return child;
    }
}

var _lastTime = 0;

Future<bool> _processExit(BuildContext context) {
    int now = DateTime
        .now()
        .millisecondsSinceEpoch;
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