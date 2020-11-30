import '../../settings/imports/flutter_imports_material.dart';

class PlayerTabMineWidget extends StatefulWidget {
  @override
  createState() => PlayerTabMineWidgetState();
}

class PlayerTabMineWidgetState extends STBaseStatefulWidgetState<PlayerTabMineWidget> {
  @override
  bool get wantKeepAlive => true;

  @override
  void initState() {
    titleBarWidget = STBaseTitleBarWidget(
      disableBack: true,
      title: "玩家信息",
    );
    loadingWidget = STBaseLoadingWidget(isShow: false);
    statusBarColor = Colors.blueAccent;
    super.initState();
  }

  @override
  Widget buildBody() {
    return getOnTapWidget(Container(color: Colors.blueAccent, child: STBaseWidgetUtils.getNetworkImageWidget("http://oznsh6z3y.bkt.clouddn.com/banner_0.jpg", width: double.infinity, height: 100, fit: BoxFit.fitHeight)), () {
      showSnackBar("you clicked");
    });
  }
}
