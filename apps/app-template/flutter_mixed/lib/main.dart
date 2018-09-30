import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'package:flutter/services.dart';

import 'LoginPage.dart';
import 'TemplatePage.dart';

const _ROUTE_PATH_PREFIX = "flutter://";

void main() {
  debugPaintSizeEnabled = false;
  runApp(_widgetForRoute(window.defaultRouteName));
}

Widget _widgetForRoute(String routeFullPath) {
  if (!routeFullPath.startsWith(_ROUTE_PATH_PREFIX)) {
    return Center(
      child: Text('Unknown route path prefix: $routeFullPath', textDirection: TextDirection.ltr),
    );
  }

  var arguments = routeFullPath.replaceAll(_ROUTE_PATH_PREFIX, "").split("?");

  print("routeFullPath->$routeFullPath");

  var routeName = arguments.length > 0 ? arguments[0] : null;
  var routeParams = Map<String, dynamic>();

  if (arguments.length > 1) {
    arguments[1].split("&").forEach((keyValue) {
      var kv = keyValue.split("=");
      if (kv.length >= 2) routeParams[kv[0]] = kv[1];
    });
  }

  switch (routeName) {
    case 'route1':
      return App(params: routeParams);
    case 'route2':
      return LoginPage(); // MyHomePage(title: 'route2');
    default:
      return Center(
        child: Text('Unknown route: $routeName', textDirection: TextDirection.ltr),
      );
  }
}

class App extends StatefulWidget {
  final dynamic params;

  App({Key key, this.params}) : super(key: key);

  @override
  createState() => new AppState(params);
}

class AppState extends State<App> {
  final dynamic params;

  AppState(this.params) : super();

  @override
  Widget build(BuildContext context) {
//    SystemChrome.setSystemUIOverlayStyle(SystemUiOverlayStyle(
//      statusBarColor: Colors.black, // android >= M
//      statusBarBrightness: Brightness.light, // ios
//      statusBarIconBrightness: Brightness.light, // android >= M
//    ));
    return new MaterialApp(
      home: new Scaffold(
        backgroundColor: Color(0xFF0f0544), // android status bar and iphone X top and bottom edges color
        body: new SafeArea(
          child: new MainTabWidget(),
          bottom: true,
        ),
      ),
      theme: new ThemeData(primaryColor: Colors.blue, accentColor: Colors.lightBlueAccent, primaryColorBrightness: Brightness.dark, hintColor: Colors.black26, highlightColor: Colors.transparent, inputDecorationTheme: new InputDecorationTheme(labelStyle: new TextStyle(color: Color(0xffdddddd)))),
    );
  }
}

class MainTabWidget extends StatefulWidget {
  @override
  createState() => new MainTabWidgetState();
}

class MainTabWidgetState extends State<MainTabWidget> {
  PageController controller;
  var pages = [TemplatePage(), TemplatePage()];
  var currentIndex = 0;

  @override
  Widget build(BuildContext context) {
    controller = new PageController(initialPage: 0);
    return new Scaffold(
      backgroundColor: Colors.white,
      body: new Column(
        children: <Widget>[
          new Expanded(
            flex: 1,
            child: new PageView.builder(
              itemBuilder: (BuildContext context, int index) {
                return pages[index];
              },
              itemCount: pages.length,
              onPageChanged: (index) {
                print("onPageChanged:$index");
                if (currentIndex != index) {
                  setState(() {
                    currentIndex = index;
                  });
                }
              },
              controller: controller,
            ),
          ),
          bottomNavigationBar(),
        ],
      ),
//      bottomNavigationBar: new Container(child: bottomNavigationBar(), color: Colors.white,),
    );
  }

  Widget bottomNavigationBar() {
    return new BottomNavigationBar(
      items: [
        new BottomNavigationBarItem(
            icon: new ImageIcon(
              new AssetImage("images/home_menu_home.png"),
              size: 48.0,
              color: currentIndex == 0 ? Color(0xff0c0435) : Color(0xffb6b6b6),
            ),
            title: new Container()),
        new BottomNavigationBarItem(
            icon: new ImageIcon(
              new AssetImage("images/home_menu_mine.png"),
              size: 48.0,
              color: currentIndex == 1 ? Color(0xff0c0435) : Color(0xffb6b6b6),
            ),
            title: new Container())
      ],
      onTap: (index) {
        print("onTap:$index");
        controller.jumpToPage(index); // https://github.com/flutter/flutter/issues/11895 fixed in v0.8.2-pre.26
        // controller.animateToPage(index, duration: Duration(milliseconds: 300), curve: Curves.linear);
      },
      currentIndex: 0,
      type: BottomNavigationBarType.fixed,
      fixedColor: Color(0xff0c0435),
    );
  }
}
