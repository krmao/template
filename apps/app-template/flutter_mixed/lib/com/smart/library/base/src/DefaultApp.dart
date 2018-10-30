import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import 'DefaultPage.dart';

var _lastTime = 0;

class DefaultApp extends DefaultPage {
    StatefulWidget _child;
    Color statusBarColor;

    DefaultApp(this._child) :super(state: _DefaultAppState());
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
                body: SafeArea(child: WillPopScope(child: widget._child, onWillPop: () {
                    print('onWillPop');
                    return _processExit(context);
                }), bottom: true)),
            theme: ThemeData(primaryColor: Colors.blue,
                accentColor: Colors.lightBlueAccent,
                primaryColorBrightness: Brightness.dark,
                hintColor: Colors.black26,
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