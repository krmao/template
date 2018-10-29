import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import 'widgets/Loading.dart';
import 'widgets/TitleBar.dart';

abstract class BasePageState<T extends StatefulWidget> extends State<T> with AutomaticKeepAliveClientMixin<T>, WidgetsBindingObserver {

    String tag;
    Loading loading;
    TitleBar titleBar;

    BuildContext _context;

    @override
    bool get wantKeepAlive => false;

    @override
    void initState() {
        super.initState();
        tag = this.toStringShort();
        print("[$tag] initSatte");
        WidgetsBinding.instance.addObserver(this);

        if (loading == null) loading = Loading();
        if (titleBar == null) titleBar = TitleBar();
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
        return Scaffold(
            backgroundColor: Color(0xFF0f0544),
            body: Builder(
                builder: (BuildContext context) {
                    this._context = context;
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
    }

    Widget body();

    @override
    void dispose() {
        print("[$tag] dispose");
        WidgetsBinding.instance.removeObserver(this);
        super.dispose();
    }
}
