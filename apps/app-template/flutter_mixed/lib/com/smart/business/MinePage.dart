import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import '../base/BasePageState.dart';
import '../base/widgets/Loading.dart';
import '../base/widgets/TitleBar.dart';

class MinePage extends StatefulWidget {
    @override
    createState() => MinePageState();
}

class MinePageState extends BasePageState<MinePage> {

    @override
    bool get wantKeepAlive => false;

    @override
    void initState() {
        titleBar = TitleBar(disableBack: true, title: "我的",);
        loading = Loading(isShow: false);
        super.initState();
    }

    @override
    Widget body() {
        return Container(color: Colors.deepOrangeAccent,);
    }

}
