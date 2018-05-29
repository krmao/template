__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var LayoutAnimation = _require(_dependencyMap[0], 'LayoutAnimation');

  var invariant = _require(_dependencyMap[1], 'fbjs/lib/invariant');

  var NativeEventEmitter = _require(_dependencyMap[2], 'NativeEventEmitter');

  var KeyboardObserver = _require(_dependencyMap[3], 'NativeModules').KeyboardObserver;

  var dismissKeyboard = _require(_dependencyMap[4], 'dismissKeyboard');

  var KeyboardEventEmitter = new NativeEventEmitter(KeyboardObserver);
  var Keyboard = {
    addListener: function addListener(eventName, callback) {
      invariant(false, 'Dummy method used for documentation');
    },
    removeListener: function removeListener(eventName, callback) {
      invariant(false, 'Dummy method used for documentation');
    },
    removeAllListeners: function removeAllListeners(eventName) {
      invariant(false, 'Dummy method used for documentation');
    },
    dismiss: function dismiss() {
      invariant(false, 'Dummy method used for documentation');
    },
    scheduleLayoutAnimation: function scheduleLayoutAnimation(event) {
      invariant(false, 'Dummy method used for documentation');
    }
  };
  Keyboard = KeyboardEventEmitter;
  Keyboard.dismiss = dismissKeyboard;

  Keyboard.scheduleLayoutAnimation = function (event) {
    var duration = event.duration,
        easing = event.easing;

    if (duration) {
      LayoutAnimation.configureNext({
        duration: duration,
        update: {
          duration: duration,
          type: easing && LayoutAnimation.Types[easing] || 'keyboard'
        }
      });
    }
  };

  module.exports = Keyboard;
},"a78d5645d35f0086ade08580dc4b6985",["c49af8845a46d9e34f4f556b366e752b","8940a4ad43b101ffc23e725363c70f8d","522e0292cd937e7e7dc15e8d27ea9246","ce21807d4d291be64fa852393519f6c8","9bc80013596b455d6a897518595d41ba"],"Keyboard");