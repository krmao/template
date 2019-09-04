import 'package:smart/settings/imports/flutter_imports_material.dart';

void main() {
  debugPaintSizeEnabled = false;
  runApp(STBaseApplication(
    enableSafeArea: false,
    statusBarColor: STBaseConstants.DEFAULT_STATUS_BAR_COLOR,
    child: OrderWidget(),
    onInitStateCallback: () {
      FlutterRouter.init();
    },
  ));
}
