import 'dart:ui';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'package:flutter/services.dart';

import '../utils/NativeManager.dart';

/// title bar widget for flutter
class TitleBarWidget extends StatefulWidget {
  static const DEFAULT_TITLE_HEIGHT = 48.0;

  final double width;
  final double height;
  final String title;
  final String rightText;
  final VoidCallback onBackPressed;
  final VoidCallback onRightPressed;
  final MethodChannel methodChannel;
  final Color titleBackgroundColor;
  final bool disableBack;

  TitleBarWidget({this.title, this.width = double.infinity, this.methodChannel, this.height = DEFAULT_TITLE_HEIGHT, this.rightText, this.onBackPressed, this.onRightPressed, this.titleBackgroundColor, this.disableBack = true, Key key}) : super(key: key);

  @override
  createState() => _TitleBarWidgetState();
}

class _TitleBarWidgetState extends State<TitleBarWidget> {
  @override
  Widget build(BuildContext context) {
    return Container(
      color: widget.titleBackgroundColor,
      width: widget.width,
      height: widget.height,
      child: Stack(
        children: <Widget>[
          widget.disableBack
              ? Container()
              : Align(
                  alignment: Alignment.centerLeft,
                  child: Container(
                    child: FlatButton(
                      onPressed: widget.onBackPressed ?? () => NativeManager.enableNative ? NativeManager.pop(widget.methodChannel, context) : Navigator.pop(context),
                      child: Image.asset(
                        "images/arrow_left_white.png",
                        fit: BoxFit.contain,
                        width: widget.height,
                        height: widget.height,
                      ),
                      padding: EdgeInsets.all(12.0),
                      materialTapTargetSize: MaterialTapTargetSize.shrinkWrap,
                    ),
                    width: widget.height,
                    height: widget.height,
                    margin: EdgeInsets.all(0.0),
                    padding: EdgeInsets.all(0.0),
                  )),
          (widget.title == null || widget.title.length <= 0)
              ? Container()
              : Align(
                  alignment: Alignment.center,
                  child: Container(
                    margin: EdgeInsets.only(top: 10.0),
                    alignment: Alignment.topCenter,
                    child: Text(
                      widget.title,
                      style: TextStyle(color: Colors.white, fontSize: 20.0, fontWeight: FontWeight.normal),
                      textAlign: TextAlign.center,
                      maxLines: 1,
                    ),
                  ),
                ),
          (widget.rightText == null || widget.rightText.length <= 0)
              ? Container()
              : Align(
                  alignment: Alignment.centerRight,
                  child: Container(
                    alignment: Alignment.topRight,
                    margin: EdgeInsets.all(0.0),
                    child: FlatButton(
                        onPressed: widget.onRightPressed,
                        child: Text(
                          widget.rightText,
                          style: TextStyle(color: Colors.white, fontSize: 18.0, fontWeight: FontWeight.normal),
                          textAlign: TextAlign.center,
                          maxLines: 1,
                        )),
                  )),
        ],
      ),
    );
  }
}
