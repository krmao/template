import 'dart:ui';

import 'package:flutter/cupertino.dart';
import 'package:lib_flutter_base/lib_flutter_base.dart';

class PlayerTabMineWidget extends StatefulWidget {
  @override
  createState() => PlayerTabMineWidgetState();
}

class PlayerTabMineWidgetState extends BaseStateDefault<PlayerTabMineWidget> {
  @override
  bool get wantKeepAlive => true;

  @override
  void initState() {
    enableTitleBar = true;
    enableLoading = true;
    titleBarWidget = BaseWidgetTitleBar(
      disableBack: true,
      title: "玩家信息",
    );
    loadingWidget = BaseWidgetLoading(isShow: false);
    statusBarColor = Color(0x4169e1ff);
    super.initState();
  }

  @override
  Widget buildBaseChild(BuildContext context) {
    return BaseWidgetUtil.getOnTapWidget(Container(color: Color(0x4169e1ff), child: BaseWidgetUtil.getNetworkImageWidget("http://oznsh6z3y.bkt.clouddn.com/banner_0.jpg", width: double.infinity, height: 100, fit: BoxFit.fitHeight)), () {
      BaseWidgetUtil.showSnackBar(scaffoldContext, "you clicked");
    });
  }
}
