__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var Platform = _require(_dependencyMap[0], 'Platform');

  var TVNavigationEventEmitter = _require(_dependencyMap[1], 'NativeModules').TVNavigationEventEmitter;

  var NativeEventEmitter = _require(_dependencyMap[2], 'NativeEventEmitter');

  function TVEventHandler() {
    this.__nativeTVNavigationEventListener = null;
    this.__nativeTVNavigationEventEmitter = null;
  }

  TVEventHandler.prototype.enable = function (component, callback) {
    if (Platform.OS === 'ios' && !TVNavigationEventEmitter) {
      return;
    }

    this.__nativeTVNavigationEventEmitter = new NativeEventEmitter(TVNavigationEventEmitter);
    this.__nativeTVNavigationEventListener = this.__nativeTVNavigationEventEmitter.addListener('onHWKeyEvent', function (data) {
      if (callback) {
        callback(component, data);
      }
    });
  };

  TVEventHandler.prototype.disable = function () {
    if (this.__nativeTVNavigationEventListener) {
      this.__nativeTVNavigationEventListener.remove();

      delete this.__nativeTVNavigationEventListener;
    }

    if (this.__nativeTVNavigationEventEmitter) {
      delete this.__nativeTVNavigationEventEmitter;
    }
  };

  module.exports = TVEventHandler;
},"62d7d2f98a3339bcd69cd68459d15603",["9493a89f5d95c3a8a47c65cfed9b5542","ce21807d4d291be64fa852393519f6c8","522e0292cd937e7e7dc15e8d27ea9246"],"TVEventHandler");