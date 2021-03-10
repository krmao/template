import 'package:flutter/material.dart';
import 'package:lib_flutter_base/lib_flutter_base.dart';

class PlayerTabHomeWidget extends StatefulWidget {
  @override
  createState() => PlayerTabHomeWidgetState();
}

class PlayerTabHomeWidgetState extends BaseStateDefault<PlayerTabHomeWidget> {
  @override
  bool get wantKeepAlive => true;

  @override
  void initState() {
    enableTitleBar = true;
    enableLoading = true;
    titleBarWidget = BaseWidgetTitleBar(
      disableBack: true,
      title: "玩家首页",
    );
    loadingWidget = BaseWidgetLoading(isShow: false);
    statusBarColor = Colors.pink;
    super.initState();
  }

  @override
  Widget buildBaseChild(BuildContext context) {
    return BaseWidgetUtil.getOnTapWidget(
      Container(
        color: Colors.pink,
        child: BaseWidgetUtil.getNetworkImageWidget("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fwww.kepuchina.cn%2Fzmt%2Fmt%2Fbw%2F201701%2FW020170113475943316605.jpg&refer=http%3A%2F%2Fwww.kepuchina.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1612688782&t=9d7aae97717bc9483eccc9dc3287fb6f", width: double.infinity, height: 100, fit: BoxFit.fitHeight),
      ),
      () => BaseWidgetUtil.showSnackBar(scaffoldContext, "you clicked"),
    );
  }
}
