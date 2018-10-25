import 'dart:ui';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'package:smart/com/smart/base/user/UserManager.dart';

const _ROUTE_PATH_PREFIX = "flutter://";

const title_height = 48.0;

class CommonWidgetManager {

    static const String DEFAULT_IMAGE = "images/default.jpg";
    static const String DEFAULT_AVATAR = "images/default_avatar.jpeg";

    static Widget getNetworkImageWidget(String imageUrl, [double width = double.infinity, double height = double.infinity, String defaultImage = DEFAULT_IMAGE]) {
        return Container(
            width: width,
            height: height,
            child: Stack(
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

    static const default_line_color = Color(0xFFEEEEEE);

    static Widget getVerticalLine({Color lineColor = default_line_color, double height = double.infinity, double width = 1.0, EdgeInsetsGeometry margin, EdgeInsetsGeometry padding}) {
        return Container(color: lineColor,
            height: height,
            width: width,
            margin: margin,
            padding: padding);
    }

    static Widget getHorizontalLine({Color lineColor = default_line_color, double height = 1.0, double width = double.infinity, EdgeInsetsGeometry margin, EdgeInsetsGeometry padding}) {
        return Container(
            color: lineColor,
            height: height,
            width: width,
            margin: margin,
            padding: padding,
        );
    }

    static Widget getOnTapWidget(Widget child, GestureTapCallback onTap) {
        return Material(type: MaterialType.transparency, child: InkWell(onTap: onTap, child: child));
    }

    static Widget getOnLongPressWidget(Widget child, GestureLongPressCallback onLongPress) {
        return Material(type: MaterialType.transparency, child: InkWell(onLongPress: onLongPress, child: child));
    }

    static Widget getOnDoubleTapWidget(Widget child, GestureTapCallback onDoubleTap) {
        return Material(type: MaterialType.transparency, child: InkWell(onDoubleTap: onDoubleTap, child: child));
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

    static DateTime getCurrentTime = DateTime.now();

    /// yyyy年MM月dd
//  static String getTimeText(int millisecondsSinceEpoch, {String Pattern = "yyyyMMdd"}) {
//    import 'package:intl/intl.dart';
//    return millisecondsSinceEpoch == -1 ? "" : "${ DateFormat(Pattern).format( DateTime.fromMillisecondsSinceEpoch(millisecondsSinceEpoch))}";
//  }

    static List<dynamic> routeNameByFullPath(String routeFullPath) {
        var arguments = routeFullPath.replaceAll(_ROUTE_PATH_PREFIX, "").split("?");
        print("\nrouteFullPath->$routeFullPath");
        var routeName = arguments.length > 0 ? arguments[0] : null;
        var routeParams = Map<String, dynamic>();
        print("\trouteName->$routeName");
        print("\trouteParams->$routeParams");
        if (arguments.length > 1) {
            arguments[1].split("&").forEach((keyValue) {
                var kv = keyValue.split("=");
                if (kv.length >= 2) routeParams[kv[0]] = kv[1];
            });
        }
        return [routeName, routeParams];
    }

    static Widget widgetByRoute(String routeFullPath) {
        if (!routeFullPath.startsWith(_ROUTE_PATH_PREFIX)) {
            return Center(
                child: Text('Unknown route path prefix: $routeFullPath', textDirection: TextDirection.ltr),
            );
        }

        var routeList = routeNameByFullPath(routeFullPath);
        String routeName = routeList[0];
        Map<String, dynamic> routeParams = routeList[1];

        switch (routeName) {
            case 'route1':
                return Container();
            case 'route2':
                return Container(); // MyHomePage(title: 'route2');
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
        // return  FadeTransition(opacity: animation, child: child);
    }
}

/*Widget _widgetForRoute(String routeFullPath) {
    if (!routeFullPath.startsWith(PATH_ROUTE_PREFIX)) {
        return Center(child: Text('Unknown route path prefix: $routeFullPath', textDirection: TextDirection.ltr),);
    }

    var arguments = routeFullPath.replaceAll(PATH_ROUTE_PREFIX, "").split("?");

    print("routeFullPath->$routeFullPath");

    var routeName = arguments.length > 0 ? arguments[0] : null;
    var routeParams = Map<String, dynamic>();

    if (arguments.length > 1) {
        arguments[1].split("&").forEach((keyValue) {
            var kv = keyValue.split("=");
            if (kv.length >= 2) routeParams[kv[0]] = kv[1];
        });
    }
}*/


