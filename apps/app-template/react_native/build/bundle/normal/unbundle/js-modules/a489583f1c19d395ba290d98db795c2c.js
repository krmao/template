__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var RCTToastAndroid = _require(_dependencyMap[0], 'NativeModules').ToastAndroid;

  var ToastAndroid = {
    SHORT: RCTToastAndroid.SHORT,
    LONG: RCTToastAndroid.LONG,
    TOP: RCTToastAndroid.TOP,
    BOTTOM: RCTToastAndroid.BOTTOM,
    CENTER: RCTToastAndroid.CENTER,
    show: function show(message, duration) {
      RCTToastAndroid.show(message, duration);
    },
    showWithGravity: function showWithGravity(message, duration, gravity) {
      RCTToastAndroid.showWithGravity(message, duration, gravity);
    },
    showWithGravityAndOffset: function showWithGravityAndOffset(message, duration, gravity, xOffset, yOffset) {
      RCTToastAndroid.showWithGravityAndOffset(message, duration, gravity, xOffset, yOffset);
    }
  };
  module.exports = ToastAndroid;
},"a489583f1c19d395ba290d98db795c2c",["ce21807d4d291be64fa852393519f6c8"],"ToastAndroid");