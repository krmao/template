import 'package:smart/headers.dart';

import 'CPage.dart';

class BPage extends StatefulWidget {
  @override
  createState() => BPageState();
}

class BPageState extends DefaultPageState<BPage> {
  @override
  void initState() {
    titleBarWidget = TitleBarWidget(
      disableBack: false,
      title: "B",
    );
    loadingWidget = LoadingWidget(isShow: false);
    statusBarColor = Colors.yellow;
    super.initState();
  }

  @override
  Widget buildBody() {
    return getOnTapWidget(Container(color: Colors.yellow), () {
      NativeManager.goTo(CPage());
    });
  }
}
