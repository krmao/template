import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'BaseDefinition.dart';
import 'DefaultPageState.dart';
import 'widgets/LoadingWidget.dart';
import 'widgets/TitleBarWidget.dart';

// ignore: must_be_immutable
class DefaultPage extends StatefulWidget {

    LoadingWidget loadingWidget;
    TitleBarWidget titleBarWidget;
    BuildContext context;
    WidgetBuildFunction body;
    WidgetBuildFunction buildRoot;
    bool keepAlive = false;
    State state;

    DefaultPage({ this.state, this.buildRoot, this.body, this.loadingWidget, this.titleBarWidget, this.keepAlive = false}) {
        if (this.state == null) {
            this.state = DefaultPageState(body: this.body,
                buildRoot: this.buildRoot,
                loadingWidget: this.loadingWidget,
                titleBarWidget: this.titleBarWidget,
                keepAlive: this.keepAlive);
        }
    }

    @override
    State createState() => this.state;
}
