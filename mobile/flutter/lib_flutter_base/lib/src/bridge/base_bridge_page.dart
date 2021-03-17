import 'dart:convert';

import '../../lib_flutter_base.dart';
import 'base_bridge.dart';

class PageBridge extends BaseBridge {
    static bool enableMultiple = true;

    static void pushPage(String name, {Object argument}) {
        if (enableMultiple) {
            String paramsString = argument != null ? json.encode(argument) : "";
            String schemaUrl = "smart://template/flutter?page=$name&params=$paramsString";
            print("[page] pushPage schemaUrl=$schemaUrl, paramsString=$paramsString");
            BaseBridgeCompact.open(schemaUrl);
        } else {
            BoostNavigator.of().push(name, withContainer: true);
        }
    }

    static Future<dynamic> getCurrentPageInitArguments() async {
        String argumentsJsonString = await BaseBridge.callNativeStatic("Page", "getCurrentPageInitArguments", {});

        print("[page] getCurrentPageInitArguments argumentsJsonString=$argumentsJsonString");

        if (argumentsJsonString == null || argumentsJsonString.isEmpty || !argumentsJsonString.startsWith("{") || !argumentsJsonString.endsWith("}")) {
            print("[page] getCurrentPageInitArguments arguments={}");
            return {};
        }
        dynamic arguments = json.decode(argumentsJsonString);
        print("[page] getCurrentPageInitArguments arguments=$arguments");
        return arguments;
    }

    static void popPage({String argumentsJsonString}) {
        if (enableMultiple) {
            print("[page] popPage argumentsJsonString=$argumentsJsonString");
            BaseBridge.callNativeStatic("Page", "popPage", {"argumentsJsonString": argumentsJsonString});
        } else {
            BoostNavigator.of().pop();
        }
    }

    static Future<T> enableExitWithDoubleBackPressed<T>(bool enable) async {
        return await BaseBridge.callNativeStatic("Page", "enableExitWithDoubleBackPressed", {"enable": enable});
    }

    @override
    String getPluginName() {
        return "Page";
    }
}
