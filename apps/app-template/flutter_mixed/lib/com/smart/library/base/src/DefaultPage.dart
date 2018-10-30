import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import 'DefaultPageState.dart';
import 'Constants.dart';
import 'widgets/LoadingWidget.dart';
import 'widgets/TitleBarWidget.dart';

class DefaultPage extends StatefulWidget {

    LoadingWidget loadingWidget;
    TitleBarWidget titleBarWidget;
    WidgetBuildFunction child;
    bool keepAlive = false;
    State state;

    DefaultPage({this.child, this.loadingWidget, this.titleBarWidget, this.keepAlive, this.state });

    @override
    State createState() {
        if (this.state == null) {
            this.state = DefaultPageState(child: this.child,
                loadingWidget: this.loadingWidget,
                titleBarWidget: this.titleBarWidget,
                keepAlive: this.keepAlive);
        }
        return this.state;
    }

}
