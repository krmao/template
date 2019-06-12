import 'package:smart/global_imports.dart';

class HomePage extends StatefulWidget {
  @override
  createState() => HomePageState();
}

class HomePageState extends DefaultPageState<HomePage> {
  @override
  bool get wantKeepAlive => true;

  @override
  void initState() {
    titleBarWidget = TitleBarWidget(
      disableBack: true,
      title: "首页",
    );
    loadingWidget = LoadingWidget(isShow: false);
    statusBarColor = Colors.blueAccent;
    super.initState();
  }

  @override
  Widget buildBody() {
    return getOnTapWidget(Container(color: Colors.blueAccent, child: WidgetUtils.getNetworkImageWidget("http://oznsh6z3y.bkt.clouddn.com/banner_0.jpg", width: double.infinity, height: 100, fit: BoxFit.fitHeight)), () {
      showSnackBar("you clicked");
    });
  }
}
