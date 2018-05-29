__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var DeviceEventManager = _require(_dependencyMap[0], 'NativeModules').DeviceEventManager;

  var RCTDeviceEventEmitter = _require(_dependencyMap[1], 'RCTDeviceEventEmitter');

  var DEVICE_BACK_EVENT = 'hardwareBackPress';

  var _backPressSubscriptions = new Set();

  RCTDeviceEventEmitter.addListener(DEVICE_BACK_EVENT, function () {
    var invokeDefault = true;
    var subscriptions = Array.from(_backPressSubscriptions.values()).reverse();

    for (var i = 0; i < subscriptions.length; ++i) {
      if (subscriptions[i]()) {
        invokeDefault = false;
        break;
      }
    }

    if (invokeDefault) {
      BackHandler.exitApp();
    }
  });
  var BackHandler = {
    exitApp: function exitApp() {
      DeviceEventManager.invokeDefaultBackPressHandler();
    },
    addEventListener: function addEventListener(eventName, handler) {
      _backPressSubscriptions.add(handler);

      return {
        remove: function remove() {
          return BackHandler.removeEventListener(eventName, handler);
        }
      };
    },
    removeEventListener: function removeEventListener(eventName, handler) {
      _backPressSubscriptions.delete(handler);
    }
  };
  module.exports = BackHandler;
},"e9ee99a5cbb074c7100807fd71239211",["ce21807d4d291be64fa852393519f6c8","1060a7fdd4114915bad6b6943cf86a21"],"BackHandler");