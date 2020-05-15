import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

const title_height = 50.0;

class STBaseWidgetManager {
  static const String DEFAULT_IMAGE = "images/icon_icon_default.jpg";

  static Widget getLoadingWidget(bool isShow) {
    return !isShow
        ? new Container()
        : new Container(
            color: Color(0x00000000),
            child: new Align(
              alignment: Alignment.center,
              child: new Container(
                width: 100.0,
                height: 100.0,
                decoration: new BoxDecoration(
                    //borderRadius: new BorderRadius.all(const Radius.circular(8.0)),
                    color: Color(0xFFEFEFEF),
                    borderRadius: BorderRadius.all(Radius.circular(10.0)),
                    boxShadow: [
                      new BoxShadow(
                        color: Color(0x40000000),
                        offset: Offset(2.0, 2.0),
                        blurRadius: 10.0,
                      ),
                    ]),
                alignment: Alignment.center,
                child: new CircularProgressIndicator(
                    valueColor:
                        new AlwaysStoppedAnimation<Color>(Colors.blueGrey)),
              ),
            ),
          );
  }

  static Widget getNetworkImageWidget(String imageUrl,
      [double width = double.infinity,
      double height = double.infinity,
      String defaultImage = DEFAULT_IMAGE]) {
    return new Container(
      width: width,
      height: height,
      child: new Stack(
        children: <Widget>[
          // Center(child: CircularProgressIndicator(valueColor: new AlwaysStoppedAnimation<Color>(Colors.blueGrey))),
          imageUrl == null || imageUrl.length == 0
              ? Image.asset(
                  defaultImage,
                  fit: BoxFit.cover,
                  width: width,
                  height: height,
                )
              : FadeInImage.assetNetwork(
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
        style: new TextStyle(
            color: Colors.white, fontSize: 20.0, fontWeight: FontWeight.normal),
        textAlign: TextAlign.center,
        maxLines: 1,
      ),
    );
  }

  static Widget getTitleRightButtonText(
      String text, final VoidCallback onPressed) {
    return new Container(
      alignment: Alignment.topRight,
      margin: EdgeInsets.all(0.0),
      child: new FlatButton(
          onPressed: onPressed,
          child: new Text(
            text,
            style: new TextStyle(
                color: Colors.white,
                fontSize: 18.0,
                fontWeight: FontWeight.normal),
            textAlign: TextAlign.center,
            maxLines: 1,
          )),
    );
  }

  static Widget getTitleWidget(BuildContext context,
      {String title,
      String rightText,
      final VoidCallback onRightPressed,
      Color titleBackgroundColor,
      bool disableBack = false}) {
    return new Container(
      color: titleBackgroundColor,
      width: double.infinity,
      height: title_height,
      child: new Stack(
        children: <Widget>[
          disableBack
              ? new Container()
              : new Align(
                  alignment: Alignment.centerLeft,
                  child: STBaseWidgetManager.getTitleBackWidget(context)),
          (title == null || title.length <= 0)
              ? new Container()
              : new Align(
                  alignment: Alignment.center,
                  child: STBaseWidgetManager.getTitleTextWidget(title),
                ),
          (rightText == null || rightText.length <= 0)
              ? new Container()
              : new Align(
                  alignment: Alignment.centerRight,
                  child: STBaseWidgetManager.getTitleRightButtonText(
                      rightText, onRightPressed)),
        ],
      ),
    );
  }

  static Widget getCommonPageWidget(BuildContext context, Widget child,
      {bool showLoading,
      String title,
      String rightText,
      final VoidCallback onRightPressed,
      Color titleBackgroundColor,
      bool disableBack = false}) {
    return new SafeArea(
        child: new Container(
            color: Colors.white,
            width: double.infinity,
            height: double.infinity,
            child: new Stack(children: <Widget>[
              child,
              getTitleWidget(context,
                  title: title,
                  rightText: rightText,
                  onRightPressed: onRightPressed,
                  titleBackgroundColor: titleBackgroundColor,
                  disableBack: disableBack),
              STBaseWidgetManager.getLoadingWidget(showLoading),
            ])));
  }

  static Widget getTitleBackWidget(BuildContext context) {
    return new Container(
      child: new FlatButton(
        onPressed: () {
          Navigator.pop(context);
        },
        child: new Image.asset(
          "images/icon_arrow_left.png",
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

  static Widget getVerticalLine(
      {Color lineColor = default_line_color,
      double height = double.infinity,
      double width = 1.0,
      EdgeInsetsGeometry margin,
      EdgeInsetsGeometry padding}) {
    return new Container(
      color: lineColor,
      height: height,
      width: width,
      margin: margin,
      padding: padding,
    );
  }

  static Widget getHorizontalLine(
      {Color lineColor = default_line_color,
      double height = 1.0,
      double width = double.infinity,
      EdgeInsetsGeometry margin,
      EdgeInsetsGeometry padding}) {
    return new Container(
      color: lineColor,
      height: height,
      width: width,
      margin: margin,
      padding: padding,
    );
  }

  static Widget getOnTapWidget(Widget child, GestureTapCallback onTap) {
    return new Material(
        type: MaterialType.transparency,
        child: new InkWell(onTap: onTap, child: child));
  }

  static Widget getOnLongPressWidget(
      Widget child, GestureLongPressCallback onLongPress) {
    return new Material(
        type: MaterialType.transparency,
        child: new InkWell(onLongPress: onLongPress, child: child));
  }

  static Widget getOnDoubleTapWidget(
      Widget child, GestureTapCallback onDoubleTap) {
    return new Material(
        type: MaterialType.transparency,
        child: new InkWell(onDoubleTap: onDoubleTap, child: child));
  }

  static void goTo(BuildContext context, Widget toPage,
      {bool ensureLogin = false}) {
    if (ensureLogin) {
    } else {
      Navigator.push(context, CupertinoPageRoute(builder: (context) => toPage));
    }
  }
}
