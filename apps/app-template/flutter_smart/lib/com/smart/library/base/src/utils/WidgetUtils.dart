import 'package:flutter/cupertino.dart';
import 'package:smart/com/smart/library/base/src/Constants.dart';
import 'package:smart/headers.dart';

import 'NativeManager.dart';

class WidgetUtils {
  static Widget getNetworkImageWidget(String imageUrl, {double width = double.infinity, double height = double.infinity, BoxFit fit = BoxFit.cover, String defaultImage = Constants.DEFAULT_IMAGE}) {
    return Container(
      width: width,
      height: height,
      child: Stack(
        children: <Widget>[
          Center(child: CircularProgressIndicator()),
          FadeInImage.assetNetwork(
            placeholder: defaultImage,
            image: imageUrl,
            fit: fit,
            width: width,
            height: height,
          )
        ],
      ),
    );
  }

  static Widget getVerticalLine({Color lineColor = Constants.DEFAULT_LINE_COLOR, double height = double.infinity, double width = 1.0, EdgeInsetsGeometry margin, EdgeInsetsGeometry padding}) {
    return Container(color: lineColor, height: height, width: width, margin: margin, padding: padding);
  }

  static Widget getHorizontalLine({Color lineColor = Constants.DEFAULT_LINE_COLOR, double height = 1.0, double width = double.infinity, EdgeInsetsGeometry margin, EdgeInsetsGeometry padding}) {
    return Container(
      color: lineColor,
      height: height,
      width: width,
      margin: margin,
      padding: padding,
    );
  }

  static Widget getOnTapWidget(Widget child, GestureTapCallback onTap) => Material(type: MaterialType.transparency, child: InkWell(onTap: onTap, child: child));

  static Widget getOnDoubleTapWidget(Widget child, GestureTapCallback onDoubleTap) => Material(type: MaterialType.transparency, child: InkWell(onDoubleTap: onDoubleTap, child: child));

  static Widget getOnLongPressWidget(Widget child, GestureLongPressCallback onLongPress) => Material(type: MaterialType.transparency, child: InkWell(onLongPress: onLongPress, child: child));

  static Future goTo(BuildContext context, Widget toPage, {bool ensureLogin = false, bool animation = true}) {
    /*if (ensureLogin) {
      UserManager.ensureLogin(context).then((userModel) {
        Navigator.push(context, animation ? CupertinoPageRoute(builder: (_) => toPage) : NoAnimationRoute(builder: (_) => toPage));
      }).catchError((error) {});
    } else {*/
    print("flutter --> goTo start ${toPage.toStringShort()}");
    // Navigator.push(context, animation ? CupertinoPageRoute(builder: (_) => toPage) : NoAnimationRoute(builder: (_) => toPage));
    Navigator.push(context, NoAnimationRoute(builder: (_) => toPage));
    return NativeManager.goTo(toPage.toStringShort(), "haha").then((result) {
      print("flutter --> goTo success result:$result");
    }).catchError((error) {
      print("flutter --> goTo failure error:$error");
    });
    /*}*/
  }

  static Future pop(BuildContext context, [dynamic result]) {
    print("flutter --> pop start param:$result");
    Navigator.pop(context, result);
    return NativeManager.finish("hehe").then((result) {
      print("flutter --> pop success result:$result");
    }).catchError((error) {
      print("flutter --> pop failure error:$error");
    });
  }
}

class NoAnimationRoute<T> extends MaterialPageRoute<T> {
  NoAnimationRoute({WidgetBuilder builder, RouteSettings settings}) : super(builder: builder, settings: settings);

  @override
  Widget buildTransitions(BuildContext context, Animation<double> animation, Animation<double> secondaryAnimation, Widget child) => child; // FadeTransition(opacity: animation, child: child)
}
