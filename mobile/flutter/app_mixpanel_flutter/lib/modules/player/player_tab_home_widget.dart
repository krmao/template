import 'package:flutter/material.dart';
import 'package:flutter_codesdancing/flutter_codesdancing.dart';

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
        child: WidgetUtil.getNetworkImageWidget("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fwww.kepuchina.cn%2Fzmt%2Fmt%2Fbw%2F201701%2FW020170113475943316605.jpg&refer=http%3A%2F%2Fwww.kepuchina.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1612688782&t=9d7aae97717bc9483eccc9dc3287fb6f", width: double.infinity, height: 100, fit: BoxFit.fitHeight),
      ),
      () => WidgetUtil.showSnackBar(scaffoldContext, "you clicked"),
    );
  }
}
