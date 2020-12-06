import 'package:flutter/material.dart';
import 'package:flutter_module/libraries/codesdancing_bridge/codesdancing_bridge.dart';
import 'package:flutter_module/libraries/codesdancing_bridge/src/page_state.dart';

class PlayerTabHomeWidget extends StatefulWidget {
  @override
  createState() => PlayerTabHomeWidgetState();
}

class PlayerTabHomeWidgetState extends PageState<PlayerTabHomeWidget> {
  @override
  bool get wantKeepAlive => true;

  @override
  void initState() {
    enableTitleBar = true;
    enableLoading = true;
    titleBarWidget = TitleBar(
      disableBack: true,
      title: "玩家首页",
    );
    loadingWidget = Loading(isShow: false);
    statusBarColor = Colors.pink;
    super.initState();
  }

  @override
  Widget buildBaseChild(BuildContext context) {
    return WidgetUtil.getOnTapWidget(
      Container(
        color: Colors.pink,
        child: WidgetUtil.getNetworkImageWidget("http://oznsh6z3y.bkt.clouddn.com/banner_0.jpg", width: double.infinity, height: 100, fit: BoxFit.fitHeight),
      ),
      () => WidgetUtil.showSnackBar(scaffoldContext, "you clicked"),
    );
  }
}
