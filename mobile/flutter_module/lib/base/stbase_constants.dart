import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

typedef WidgetBuildFunction = Widget Function();

class STBaseConstants {
    static const double DEFAULT_TITLE_HEIGHT = 48.0;
    static const String DEFAULT_IMAGE = "images/default.jpg";
    static const String DEFAULT_AVATAR = "images/default_avatar.jpeg";
    static const Color PRIMARY_COLOR = Colors.blueGrey;
    static const Color ACCENT_COLOR = Color(0x4169e1ff);
    static const Color HINT_COLOR = Colors.black26;
    static const Color HIGHLIGHT_COLOR = Colors.black12;
    static const Color INPUT_DECORATION_COLOR = Color(0XFFDDDDDD);
    static const Color DEFAULT_LINE_COLOR = Color(0xFFEEEEEE);
    static const DEFAULT_STATUS_BAR_COLOR = Color(0x40000000); // android status bar and iphone X top and bottom edges color
}
