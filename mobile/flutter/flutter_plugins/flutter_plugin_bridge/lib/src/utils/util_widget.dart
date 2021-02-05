
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import '../app_constants.dart';

class WidgetUtil {
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

  static void showSnackBar(BuildContext scaffoldContext, String msg) {
    if (scaffoldContext != null && msg?.trim()?.isNotEmpty == true) {
      Scaffold.of(scaffoldContext).showSnackBar(SnackBar(content: Text(msg), duration: Duration(milliseconds: 2000)));
    }
  }

  static Widget getOnTapWidget(Widget child, GestureTapCallback onTap) => Material(type: MaterialType.transparency, child: InkWell(onTap: onTap, child: child));

  static Widget getOnDoubleTapWidget(Widget child, GestureTapCallback onDoubleTap) => Material(type: MaterialType.transparency, child: InkWell(onDoubleTap: onDoubleTap, child: child));

  static Widget getOnLongPressWidget(Widget child, GestureLongPressCallback onLongPress) => Material(type: MaterialType.transparency, child: InkWell(onLongPress: onLongPress, child: child));
}
