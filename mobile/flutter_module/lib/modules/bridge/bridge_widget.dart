import '../../settings/imports/flutter_imports_material.dart';

class BridgeWidget extends StatefulWidget {
  @override
  createState() => BridgeWidgetState();
}

class BridgeWidgetState extends STBaseStatefulWidgetState<BridgeWidget> {
  @override
  bool get wantKeepAlive => true;

  @override
  void initState() {
    loadingWidget = STBaseLoadingWidget(isShow: false);
    statusBarColor = Color(0xff00008b);
    super.initState();
  }

  @override
  Widget buildBody() {
    return ListView(
      children: <Widget>[
        getItemWidget('open flutter player', () => {FlutterRouter.open(FlutterRouter.URL_FLUTTER_PLAYER)}),
        getItemWidget(
          'open native mine',
          () => {
            FlutterRouter.open(FlutterRouter.URL_NATIVE_MINE, urlParams: {"urlParams": "1"}, exts: {"exts": "1"}).then(
              (Map<String, dynamic> result) {
                print("URL_MINE did recieve second route result");
                print("URL_MINE did recieve second route result $result");
              },
            ),
          },
        ),
        getItemWidget(
          'open flutter order',
          () => {
            FlutterRouter.open(FlutterRouter.URL_FLUTTER_ORDER).then(
              (Map<String, dynamic> result) {
                print("URL_ORDER did recieve second route result");
                print("URL_ORDER did recieve second route result $result");
              },
            ),
          },
        ),
        getItemWidget(
          'open flutter settings',
          () => {
            FlutterRouter.open(FlutterRouter.URL_FLUTTER_SETTINGS).then(
              (Map<String, dynamic> result) {
                print("URL_SETTINGS did recieve second route result");
                print("URL_SETTINGS did recieve second route result $result");
              },
            ),
          },
        ),
        getItemWidget(
          'close current page',
          () => {
            FlutterRouter.closeCurrent(
              result: {
                "result": {"name": "mm", "params": "aa", "login": 1, "token": "kkk"}
              },
            ),
          },
        ),
      ],
    );
  }

  static Widget getItemWidget(String label, GestureTapCallback onTap) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 2.5),
      child: Material(
        color: Colors.red,
        child: Ink(
            color: Color(0xff4169e1),
            child: InkWell(
                onTap: () => Future.delayed(Duration(milliseconds: 200), () => onTap()), // 打开页面看不到水波纹效果, 加延时就可以
                splashColor: Color(0xff191970),
                borderRadius: new BorderRadius.all(new Radius.circular(0.0)),
                child: Container(
                    width: double.infinity,
                    alignment: Alignment.centerLeft,
                    padding: const EdgeInsets.all(15.0),
                    child: Text(label, style: TextStyle(fontSize: 15.0, color: Colors.white, fontWeight: FontWeight.bold))))),
      ),
    );
  }
}
