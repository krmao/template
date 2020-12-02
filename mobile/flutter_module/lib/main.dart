import 'package:flutter_module/modules/bridge/bridge_widget.dart';

import 'settings/imports/flutter_imports_material.dart';

void main() {
  debugPaintSizeEnabled = false;
  runApp(STBaseApplication(
    enableSafeArea: false,
    statusBarColor: STBaseConstants.DEFAULT_STATUS_BAR_COLOR,
    child: BridgeWidget({}),
    onInitStateCallback: () {
      FlutterRouter.initialize();
    },
  ));
}
