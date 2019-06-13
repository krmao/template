import 'package:smart/settings/imports/flutter_imports_material.dart';

void main() {
  debugPaintSizeEnabled = false;
  runApp(STBaseApplication(
    enableSafeArea: false,
    statusBarColor: Constants.DEFAULT_STATUS_BAR_COLOR,
    child: Container(),
    onInitStateCallback: () {
      //
      FlutterBoost.singleton.registerPageBuilders({
        FlutterRouter.URL_SETTINGS: (pageName, params, _) => FlutterSettingsWidget(params),
        FlutterRouter.URL_ORDER: (pageName, params, _) => FlutterOrderWidget()
      });
      //
      FlutterBoost.handleOnStartPage();
    },
  ));
}