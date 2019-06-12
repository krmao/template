import 'package:smart/settings/imports/flutter_imports_material.dart';

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
      _showDialog();
    });
  }

  @override
  bool onBackPressed() {
    if (isShow) {
      Navigator.of(context).pop();
      isShow = false;
      return true;
    } else {
      return false;
    }
  }

  bool isShow = false;

  Future<Null> _showDialog() async {
    isShow = true;
    return showDialog<Null>(
      context: context,
      barrierDismissible: true, // user must tap button!
      builder: (BuildContext context) {
        return new AlertDialog(
          title: new Text('弹框'),
          content: new Text('弹框'),
          actions: <Widget>[
            new FlatButton(
              child: new Text('取消'),
              onPressed: () {
                Navigator.of(context).pop();
                isShow = false;
              },
            ),
            new FlatButton(
              child: new Text('下一页'),
              onPressed: () {
                NativeManager.invokeNativeGoToNative("K", "");
              },
            ),
          ],
        );
      },
    );
  }
}
