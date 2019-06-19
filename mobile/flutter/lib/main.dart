import 'package:smart/settings/imports/flutter_imports_material.dart';

void main() {
  debugPaintSizeEnabled = false;
  runApp(STBaseApplication(
    enableSafeArea: false,
    statusBarColor: STBaseConstants.DEFAULT_STATUS_BAR_COLOR,
    child: OrderWidget(),
    onInitStateCallback: () {
      //
      FlutterBoost.singleton.registerPageBuilders({
        FlutterRouter.URL_SETTINGS: (pageName, params, _) => SettingsWidget(params),
        FlutterRouter.URL_ORDER: (pageName, params, _) => OrderWidget()
      });
      //
      FlutterBoost.handleOnStartPage();
    },
  ));
}