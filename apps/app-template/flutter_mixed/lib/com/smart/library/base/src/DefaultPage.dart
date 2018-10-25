import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'BaseDefinition.dart';
import 'DefaultPageState.dart';
import 'widgets/Loading.dart';
import 'widgets/TitleBar.dart';

// ignore: must_be_immutable
class DefaultPage extends StatefulWidget {

    Loading loading;
    TitleBar titleBar;
    BuildContext context;
    WidgetBuildFunction body;
    WidgetBuildFunction buildRoot;
    bool keepAlive = false;
    State state;

    DefaultPage({ this.state, this.buildRoot, this.body, this.loading, this.titleBar, this.keepAlive = false}) {
        if (this.state == null) {
            this.state = DefaultPageState(body: this.body,
                buildRoot: this.buildRoot,
                loading: this.loading,
                titleBar: this.titleBar,
                keepAlive: this.keepAlive);
        }
    }

    @override
    State createState() => this.state;
}
