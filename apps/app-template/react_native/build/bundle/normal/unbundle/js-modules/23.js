__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var NativeModules = _require(_dependencyMap[0], 'NativeModules');

  var RCTDeviceEventEmitter = _require(_dependencyMap[1], 'RCTDeviceEventEmitter');

  var RCTAccessibilityInfo = NativeModules.AccessibilityInfo;
  var TOUCH_EXPLORATION_EVENT = 'touchExplorationDidChange';

  var _subscriptions = new Map();

  var AccessibilityInfo = {
    fetch: function fetch() {
      return new Promise(function (resolve, reject) {
        RCTAccessibilityInfo.isTouchExplorationEnabled(function (resp) {
          resolve(resp);
        });
      });
    },
    addEventListener: function addEventListener(eventName, handler) {
      var listener = RCTDeviceEventEmitter.addListener(TOUCH_EXPLORATION_EVENT, function (enabled) {
        handler(enabled);
      });

      _subscriptions.set(handler, listener);
    },
    removeEventListener: function removeEventListener(eventName, handler) {
      var listener = _subscriptions.get(handler);

      if (!listener) {
        return;
      }

      listener.remove();

      _subscriptions.delete(handler);
    }
  };
  module.exports = AccessibilityInfo;
},23,[24,40],"AccessibilityInfo");