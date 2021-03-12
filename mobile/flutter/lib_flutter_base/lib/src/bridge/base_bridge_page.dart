import '../../lib_flutter_base.dart';
import 'base_bridge.dart';

class PageBridge extends BaseBridge {
  static bool enableMultiple = true;
  static void popPage() {
    if (enableMultiple) {
      BaseBridgeCompact.close([]);
    } else {
      BoostNavigator.of().pop();
    }
  }

  static void pushPage(String name) {
    if (enableMultiple) {
      BaseBridgeCompact.open("smart://template/flutter?page=$name");
    } else {
      BoostNavigator.of().push(name, withContainer: true);
    }
  }

  static Future<T> enableExitWithDoubleBackPressed<T>(bool enable) async {
    return await BaseBridge.callNativeStatic(
        "Page", "enableExitWithDoubleBackPressed", {"enable": enable});
  }

  @override
  String getPluginName() {
    return "Page";
  }
}
