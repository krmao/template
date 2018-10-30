import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import 'Constants.dart';
import 'package:smart/com/smart/library/base/src/utils/WidgetUtils.dart';
import 'widgets/LoadingWidget.dart';
import 'widgets/TitleBarWidget.dart';

abstract class BasePageState<T extends StatefulWidget> extends State<T> with AutomaticKeepAliveClientMixin<T>, WidgetsBindingObserver {

    String tag;
    LoadingWidget loadingWidget;
    TitleBarWidget titleBarWidget;

    @override
    bool get wantKeepAlive => false;

    @override
    void initState() {
        super.initState();
        tag = this.toStringShort();
        print("[$tag] initSatte");
        WidgetsBinding.instance.addObserver(this);

        if (loadingWidget == null) loadingWidget = LoadingWidget();
        if (titleBarWidget == null) titleBarWidget = TitleBarWidget();
    }

    @override
    void didChangeAppLifecycleState(AppLifecycleState state) {
        super.didChangeAppLifecycleState(state);
        print("[$tag] didChangeAppLifecycleState state=${state.toString()}");
    }

    @override
    Widget build(BuildContext context) {
        print("[$tag] build");
        return Scaffold(
            backgroundColor: Color(0xFF0f0544),
            body: Builder(
                builder: (BuildContext context) {
                    return SafeArea(
                        child: Container(
                            color: Colors.white,
                            width: double.infinity,
                            height: double.infinity,
                            child: Stack(children: <Widget>[body(), titleBarWidget, loadingWidget])
                        )
                    );
                }
            )
        );
    }

    Widget body(); // abstract

    @override
    void dispose() {
        print("[$tag] dispose");
        WidgetsBinding.instance.removeObserver(this);
        super.dispose();
    }

    // -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
    // -- common functions definition
    // -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --

    void showSnackBar(String msg) => Scaffold.of(context).showSnackBar(SnackBar(content: Text(msg)));

    Widget getVerticalLine({Color lineColor = Constants.DEFAULT_LINE_COLOR, double height = double.infinity, double width = 1.0, EdgeInsetsGeometry margin, EdgeInsetsGeometry padding}) =>
        WidgetUtils.getVerticalLine(lineColor: lineColor,
            height: height,
            width: width,
            margin: margin,
            padding: padding);

    Widget getHorizontalLine({Color lineColor = Constants.DEFAULT_LINE_COLOR, double height = 1.0, double width = double.infinity, EdgeInsetsGeometry margin, EdgeInsetsGeometry padding}) =>
        WidgetUtils.getHorizontalLine(lineColor: lineColor,
            height: height,
            width: width,
            margin: margin,
            padding: padding);

    Widget getOnTapWidget(Widget child, GestureTapCallback onTap) => WidgetUtils.getOnTapWidget(child, onTap);

    Widget getOnDoubleTapWidget(Widget child, GestureTapCallback onDoubleTap) => WidgetUtils.getOnDoubleTapWidget(child, onDoubleTap);

    Widget getOnLongPressWidget(Widget child, GestureLongPressCallback onLongPress) => WidgetUtils.getOnLongPressWidget(child, onLongPress);

}
