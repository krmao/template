import '../../settings/imports/flutter_imports_material.dart';

class PlayerTabHomeWidget extends StatefulWidget {
  @override
  createState() => PlayerTabHomeWidgetState();
}

class PlayerTabHomeWidgetState extends STBaseStatefulWidgetState<PlayerTabHomeWidget> {
  @override
  bool get wantKeepAlive => true;

  @override
  void initState() {
    titleBarWidget = STBaseTitleBarWidget(
      disableBack: true,
      title: "玩家首页",
    );
    loadingWidget = STBaseLoadingWidget(isShow: false);
    statusBarColor = Colors.pink;
    super.initState();
  }

  @override
  Widget buildBody() {
    return getOnTapWidget(Container(color: Colors.pink, child: STBaseWidgetUtils.getNetworkImageWidget("http://oznsh6z3y.bkt.clouddn.com/banner_0.jpg", width: double.infinity, height: 100, fit: BoxFit.fitHeight)), () {
      showSnackBar("you clicked");
    });
  }
}
