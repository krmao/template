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
},232,[233,18,82,24,234],"Keyboard");