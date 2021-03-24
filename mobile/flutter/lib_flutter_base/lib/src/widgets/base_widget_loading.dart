import 'dart:async';
import 'dart:ui';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';

const DEFAULT_LOADING_WIDTH = 96.0;
const DEFAULT_LOADING_SHOW_DELAY = 0;
const DEFAULT_LOADING_HIDE_DELAY = 500;

/// loading widget for flutter
// ignore: must_be_immutable
class BaseWidgetLoading extends StatefulWidget {
  final double width;
  final double height;
  bool isShow;
  final _loadingState = _BaseWidgetLoadingState();

  BaseWidgetLoading(
      {this.width = DEFAULT_LOADING_WIDTH,
      this.height = DEFAULT_LOADING_WIDTH,
      this.isShow = false,
      Key key})
      : super(key: key);

  @override
  createState() => _loadingState;

  void showLoading() => _loadingState._showLoading();

  void hideLoading() => _loadingState._hideLoading();
}

class _BaseWidgetLoadingState extends State<BaseWidgetLoading> {
  void _showLoading({int delay}) => Future.delayed(
      Duration(milliseconds: delay ?? DEFAULT_LOADING_SHOW_DELAY),
      () => setState(() => widget.isShow = true));

  void _hideLoading({int delay}) => Future.delayed(
      Duration(milliseconds: delay ?? DEFAULT_LOADING_HIDE_DELAY),
      () => setState(() => widget.isShow = false));

  @override
  Widget build(BuildContext context) {
    return !widget.isShow
        ? new Container()
        : new Container(
            color: Color(0x99000000),
            child: new Align(
              alignment: Alignment.center,
              child: new Container(
                width: widget.width,
                height: widget.height,
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
                child: new CircularProgressIndicator(
                    valueColor:
                        new AlwaysStoppedAnimation<Color>(Colors.lightBlue)),
              ),
            ),
          );
  }
}
