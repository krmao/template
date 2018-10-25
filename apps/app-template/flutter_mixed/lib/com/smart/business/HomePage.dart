import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import '../base/BasePageState.dart';
import '../base/widgets/Loading.dart';
import '../base/widgets/TitleBar.dart';

class HomePage extends StatefulWidget {
    @override
    createState() => HomePageState();
}

class HomePageState extends BasePageState<HomePage> {

    @override
    bool get wantKeepAlive => false;

    @override
    void initState() {
        titleBar = TitleBar(disableBack: true, title: "首页",);
        loading = Loading(isShow: false);
        super.initState();
    }

    @override
    Widget body() {
        return Container(color: Colors.blueGrey,);
    }

}
