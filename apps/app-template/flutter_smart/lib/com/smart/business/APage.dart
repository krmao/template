import 'package:smart/headers.dart';

import 'BPage.dart';

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
      WidgetUtils.goTo(methodChannel, context, BPage());
    });
  }
}
