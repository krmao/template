import 'package:flutter/services.dart';

typedef NativeToDartMethodCallCallBack = void Function(String methodName, dynamic arguments);

abstract class Bridge {
  static const String CHANNEL_NAME = "codesdancing.flutter.bridge/callNative";

  static MethodChannel _globalBridgeMethodChannel;
  static Map<String, NativeToDartMethodCallCallBack> _globalNativeToDartMethodMap = new Map();

  static void _invokeNativeToDartMethod(String methodName, dynamic arguments) {
    if (_globalNativeToDartMethodMap.containsKey(methodName)) {
      _globalNativeToDartMethodMap[methodName](methodName, arguments);
    }
  }

  static void _initGlobalBridgeMethodChannel() {
    if (_globalBridgeMethodChannel == null) {
      _globalBridgeMethodChannel = MethodChannel(CHANNEL_NAME, JSONMethodCodec());
      // ignore: missing_return
      _globalBridgeMethodChannel.setMethodCallHandler((MethodCall call) {
        _invokeNativeToDartMethod(call.method, call.arguments);
      });
    }
  }

  static void registerMethodCallBack(String methodName, NativeToDartMethodCallCallBack callBack) {
    _initGlobalBridgeMethodChannel();
    _globalNativeToDartMethodMap[methodName] = callBack;
  }

  static void unregisterMethodCallBack(String methodName) {
    _globalNativeToDartMethodMap.remove(methodName);
  }

  /// 调用Native Plugin方法
  static Future<T> callNativeStatic<T>(String pluginName, String methodName, [dynamic arguments]) async {
    _initGlobalBridgeMethodChannel();
    return await _globalBridgeMethodChannel.invokeMethod("$pluginName-$methodName", arguments);
  }

  Future<T> callNative<T>(String methodName, [dynamic arguments]) async {
    return await callNativeStatic(getPluginName(), methodName, arguments);
  }

  /// plugin名字
  String getPluginName();
}
