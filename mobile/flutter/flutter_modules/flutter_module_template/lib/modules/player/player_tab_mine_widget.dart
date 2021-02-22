import 'dart:ui';

import 'package:flutter/cupertino.dart';
import 'package:flutter_codesdancing/flutter_codesdancing.dart';

class PlayerTabMineWidget extends StatefulWidget {
  @override
  createState() => PlayerTabMineWidgetState();
}

class PlayerTabMineWidgetState extends PageState<PlayerTabMineWidget> {
  @override
  bool get wantKeepAlive => true;

  @override
  void initState() {
    enableTitleBar = true;
    enableLoading = true;
    titleBarWidget = TitleBar(
      disableBack: true,
      title: "玩家信息",
    );
    loadingWidget = Loading(isShow: false);
    statusBarColor = Color(0x4169e1ff);
    super.initState();
  }

  @override
  Widget buildBaseChild(BuildContext context) {
    return WidgetUtil.getOnTapWidget(Container(color: Color(0x4169e1ff), child: WidgetUtil.getNetworkImageWidget("http://oznsh6z3y.bkt.clouddn.com/banner_0.jpg", width: double.infinity, height: 100, fit: BoxFit.fitHeight)), () {
      WidgetUtil.showSnackBar(scaffoldContext, "you clicked");
    });
  }
}
