import 'dart:ui';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';

import 'LoginPage.dart';
import 'TemplatePage.dart';
import 'UserManager.dart';

const _ROUTE_PATH_PREFIX = "flutter://";

const title_height = 50.0;

class CommonWidgetManager {
  static Widget getLoadingWidget(bool isShow) {
    return !isShow
        ? new Container()
        : new Container(
            color: Color(0x99000000),
            child: new Align(
              alignment: Alignment.center,
              child: new Container(
                width: 100.0,
                height: 100.0,
                decoration: new BoxDecoration(
                    //borderRadius: new BorderRadius.all(const Radius.circular(8.0)),
                    color: Color(0xFFEEEEEE),
                    borderRadius: BorderRadius.all(Radius.circular(10.0)),
                    boxShadow: [
                      new BoxShadow(
                        color: Color(0x40000000),
                        offset: Offset(2.0, 2.0),
                        blurRadius: 10.0,
                      ),
                    ]),
                alignment: Alignment.center,
                child: new CircularProgressIndicator(valueColor: new AlwaysStoppedAnimation<Color>(Colors.lightBlue)),
              ),
            ),
          );
  }

  static const String DEFAULT_IMAGE = "images/default.jpg";
  static const String DEFAULT_AVATAR = "images/default_avatar.jpeg";

  static Widget getNetworkImageWidget(String imageUrl, [double width = double.infinity, double height = double.infinity, String defaultImage = DEFAULT_IMAGE]) {
    return new Container(
      width: width,
      height: height,
      child: new Stack(
        children: <Widget>[
          Center(child: CircularProgressIndicator()),
          FadeInImage.assetNetwork(
            placeholder: defaultImage,
            image: imageUrl,
            fit: BoxFit.cover,
            width: width,
            height: height,
          )
        ],
      ),
    );
  }

  static Widget getTitleTextWidget(String title) {
    return new Container(
      margin: EdgeInsets.only(top: 10.0),
      alignment: Alignment.topCenter,
      child: new Text(
        title,
        style: new TextStyle(color: Colors.white, fontSize: 20.0, fontWeight: FontWeight.normal),
        textAlign: TextAlign.center,
        maxLines: 1,
      ),
    );
  }

  static Widget getTitleRightButtonText(String text, final VoidCallback onPressed) {
    return new Container(
      alignment: Alignment.topRight,
      margin: EdgeInsets.all(0.0),
      child: new FlatButton(
          onPressed: onPressed,
          child: new Text(
            text,
            style: new TextStyle(color: Colors.white, fontSize: 18.0, fontWeight: FontWeight.normal),
            textAlign: TextAlign.center,
            maxLines: 1,
          )),
    );
  }

  static Widget getTitleWidget(BuildContext context, {String title, String rightText, final VoidCallback onRightPressed, Color titleBackgroundColor, bool disableBack = false}) {
    return new Container(
      color: titleBackgroundColor,
      width: double.infinity,
      height: title_height,
      child: new Stack(
        children: <Widget>[
          disableBack ? new Container() : new Align(alignment: Alignment.centerLeft, child: CommonWidgetManager.getTitleBackWidget(context)),
          (title == null || title.length <= 0)
              ? new Container()
              : new Align(
                  alignment: Alignment.center,
                  child: CommonWidgetManager.getTitleTextWidget(title),
                ),
          (rightText == null || rightText.length <= 0) ? new Container() : new Align(alignment: Alignment.centerRight, child: CommonWidgetManager.getTitleRightButtonText(rightText, onRightPressed)),
        ],
      ),
    );
  }

  static Widget getCommonPageWidget(BuildContext context, Widget child, {bool showLoading, String title, String rightText, final VoidCallback onRightPressed, Color titleBackgroundColor, bool disableBack = false}) {
    return new SafeArea(
        child: new Container(
            color: Colors.white,
            width: double.infinity,
            height: double.infinity,
            child: new Stack(children: <Widget>[
              child,
              getTitleWidget(context, title: title, rightText: rightText, onRightPressed: onRightPressed, titleBackgroundColor: titleBackgroundColor, disableBack: disableBack),
              CommonWidgetManager.getLoadingWidget(showLoading),
            ])));
  }

  static Widget getTitleBackWidget(BuildContext context) {
    return new Container(
      child: new FlatButton(
        onPressed: () {
          Navigator.pop(context);
        },
        child: new Image.asset(
          "images/arrow_left_white.png",
          fit: BoxFit.contain,
          width: title_height,
          height: title_height,
        ),
        padding: EdgeInsets.all(12.0),
        materialTapTargetSize: MaterialTapTargetSize.shrinkWrap,
      ),
      width: title_height,
      height: title_height,
      margin: EdgeInsets.all(0.0),
      padding: EdgeInsets.all(0.0),
    );
  }

  static const default_line_color = Color(0xFFEEEEEE);

  static Widget getVerticalLine({Color lineColor = default_line_color, double height = double.infinity, double width = 1.0, EdgeInsetsGeometry margin, EdgeInsetsGeometry padding}) {
    return new Container(
      color: lineColor,
      height: height,
      width: width,
      margin: margin,
      padding: padding,
    );
  }

  static Widget getHorizontalLine({Color lineColor = default_line_color, double height = 1.0, double width = double.infinity, EdgeInsetsGeometry margin, EdgeInsetsGeometry padding}) {
    return new Container(
      color: lineColor,
      height: height,
      width: width,
      margin: margin,
      padding: padding,
    );
  }

  static Widget getOnTapWidget(Widget child, GestureTapCallback onTap) {
    return new Material(type: MaterialType.transparency, child: new InkWell(onTap: onTap, child: child));
  }

  static Widget getOnLongPressWidget(Widget child, GestureLongPressCallback onLongPress) {
    return new Material(type: MaterialType.transparency, child: new InkWell(onLongPress: onLongPress, child: child));
  }

  static Widget getOnDoubleTapWidget(Widget child, GestureTapCallback onDoubleTap) {
    return new Material(type: MaterialType.transparency, child: new InkWell(onDoubleTap: onDoubleTap, child: child));
  }

  static void goTo(BuildContext context, Widget toPage, {bool ensureLogin = false, bool animation = true}) {
    if (ensureLogin) {
      UserManager.ensureLogin(context).then((userModel) {
        if (animation)
          Navigator.push(context, CupertinoPageRoute(builder: (context) => toPage));
        else
          Navigator.push(context, MyCustomRoute(builder: (_) => toPage));
      }).catchError((error) {});
    } else {
      if (animation)
        Navigator.push(context, CupertinoPageRoute(builder: (context) => toPage));
      else
        Navigator.push(context, MyCustomRoute(builder: (_) => toPage));
    }
  }

  static bool pop<T extends Object>(BuildContext context, [T result]) {
    return Navigator.pop(context, result);
  }

  static DateTime getCurrentTime = new DateTime.now();

  /// yyyy年MM月dd
//  static String getTimeText(int millisecondsSinceEpoch, {String newPattern = "yyyyMMdd"}) {
//    import 'package:intl/intl.dart';
//    return millisecondsSinceEpoch == -1 ? "" : "${new DateFormat(newPattern).format(new DateTime.fromMillisecondsSinceEpoch(millisecondsSinceEpoch))}";
//  }

  static Widget widgetByRoute(String routeFullPath) {
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
        return TemplatePage();
      case 'route2':
        return LoginPage(); // MyHomePage(title: 'route2');
      default:
        return Center(
          child: Text('Unknown route: $routeName', textDirection: TextDirection.ltr),
        );
    }
  }
}

class MyCustomRoute<T> extends MaterialPageRoute<T> {
  MyCustomRoute({WidgetBuilder builder, RouteSettings settings}) : super(builder: builder, settings: settings);

  @override
  Widget buildTransitions(BuildContext context, Animation<double> animation, Animation<double> secondaryAnimation, Widget child) {
    return child;
    // Fades between routes. (If you don't want any animation,
    // just return child.)
    // return new FadeTransition(opacity: animation, child: child);
  }
}
