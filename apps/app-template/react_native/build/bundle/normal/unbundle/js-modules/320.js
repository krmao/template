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
},320,[24,40],"BackHandler");