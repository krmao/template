import 'package:smart/settings/imports/flutter_imports_material.dart';

class APage extends StatefulWidget {
  @override
  createState() => APageState();
}

class APageState extends DefaultPageState<APage> {
  @override
  void initState() {
    titleBarWidget = TitleBarWidget(
      disableBack: false,
      title: "A",
    );
    loadingWidget = LoadingWidget(isShow: false);
    statusBarColor = Colors.blueGrey;
    super.initState();
  }

  @override
  Widget buildBody() {
    return getOnTapWidget(Container(color: Colors.blueGrey), () {
//      NativeManager.goTo(BPage());
    });
  }
}
