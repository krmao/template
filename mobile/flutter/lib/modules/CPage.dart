import 'package:smart/global_imports.dart';

class CPage extends StatefulWidget {
  @override
  createState() => CPageState();
}

class CPageState extends DefaultPageState<CPage> {
  @override
  void initState() {
    titleBarWidget = TitleBarWidget(
      disableBack: false,
      title: "C",
    );
    loadingWidget = LoadingWidget(isShow: false);
    statusBarColor = Colors.orange;
    super.initState();
  }

  @override
  Widget buildBody() {
    return getOnTapWidget(Container(color: Colors.orange), () {
      showSnackBar("C");
    });
  }
}
