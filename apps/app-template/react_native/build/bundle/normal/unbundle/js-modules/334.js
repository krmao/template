__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var RCTVibration = _require(_dependencyMap[0], 'NativeModules').Vibration;

  var Platform = _require(_dependencyMap[1], 'Platform');

  var _vibrating = false;
  var _id = 0;

  function vibrateByPattern(pattern) {
    var repeat = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : false;

    if (_vibrating) {
      return;
    }

    _vibrating = true;

    if (pattern[0] === 0) {
      RCTVibration.vibrate();
      pattern = pattern.slice(1);
    }

    if (pattern.length === 0) {
      _vibrating = false;
      return;
    }

    setTimeout(function () {
      return vibrateScheduler(++_id, pattern, repeat, 1);
    }, pattern[0]);
  }

  function vibrateScheduler(id, pattern, repeat, nextIndex) {
    if (!_vibrating || id !== _id) {
      return;
    }

    RCTVibration.vibrate();

    if (nextIndex >= pattern.length) {
      if (repeat) {
        nextIndex = 0;
      } else {
        _vibrating = false;
        return;
      }
    }

    setTimeout(function () {
      return vibrateScheduler(id, pattern, repeat, nextIndex + 1);
    }, pattern[nextIndex]);
  }

  var Vibration = {
    vibrate: function vibrate() {
      var pattern = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : 400;
      var repeat = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : false;

      if (Platform.OS === 'android') {
        if (typeof pattern === 'number') {
          RCTVibration.vibrate(pattern);
        } else if (Array.isArray(pattern)) {
          RCTVibration.vibrateByPattern(pattern, repeat ? 0 : -1);
        } else {
          throw new Error('Vibration pattern should be a number or array');
        }
      } else {
        if (_vibrating) {
          return;
        }

        if (typeof pattern === 'number') {
          RCTVibration.vibrate();
        } else if (Array.isArray(pattern)) {
          vibrateByPattern(pattern, repeat);
        } else {
          throw new Error('Vibration pattern should be a number or array');
        }
      }
    },
    cancel: function cancel() {
      if (Platform.OS === 'ios') {
        _vibrating = false;
      } else {
        RCTVibration.cancel();
      }
    }
  };
  module.exports = Vibration;
},334,[24,32],"Vibration");