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
},"73fa690922381d7b2ad44d215274a11d",["ce21807d4d291be64fa852393519f6c8","1060a7fdd4114915bad6b6943cf86a21"],"AccessibilityInfo");