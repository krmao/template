import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import 'widgets/Loading.dart';
import 'widgets/TitleBar.dart';

typedef WidgetBuildFunction = Widget Function();

class DefaultPageState<T extends StatefulWidget> extends State<T> with AutomaticKeepAliveClientMixin<T>, WidgetsBindingObserver {

    String tag;
    Loading loading;
    TitleBar titleBar;
    BuildContext context;
    WidgetBuildFunction buildRoot;
    WidgetBuildFunction body;
    bool keepAlive = false;

    DefaultPageState({this.body, this.buildRoot, this.titleBar, this.loading, this.keepAlive=false });

    @override
    bool get wantKeepAlive => keepAlive;

    @override
    void initState() {
        super.initState();
        tag = this.toStringShort();
        print("[$tag] initSatte");
        WidgetsBinding.instance.addObserver(this);

        if (loading == null) loading = Loading();
        if (titleBar == null) titleBar = TitleBar();
        if (buildRoot == null) {
            buildRoot = () {
                return Scaffold(
                    backgroundColor: Color(0xFF0f0544),
                    body: Builder(
                        builder: (BuildContext context) {
                            this.context = context;
                            return SafeArea(
                                child: Container(
                                    color: Colors.white,
                                    width: double.infinity,
                                    height: double.infinity,
                                    child: Stack(children: <Widget>[body(), titleBar, loading])
                                )
                            );
                        }
                    )
                );
            };
        }

        if (body == null) body = () => Container();
    }

    @override
    void didChangeAppLifecycleState(AppLifecycleState state) {
        super.didChangeAppLifecycleState(state);
        print("[$tag] didChangeAppLifecycleState state=${state.toString()}");
    }

    void showSnackBar(String msg) => Scaffold.of(context).showSnackBar(SnackBar(content: Text(msg)));

    @override
    Widget build(BuildContext context) {
        print("[$tag] build");
        return this.buildRoot();
    }

    @override
    void dispose() {
        print("[$tag] dispose");
        WidgetsBinding.instance.removeObserver(this);
        super.dispose();
    }
}
