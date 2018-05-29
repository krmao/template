__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var AlertIOS = _require(_dependencyMap[0], 'AlertIOS');

  var NativeModules = _require(_dependencyMap[1], 'NativeModules');

  var Platform = _require(_dependencyMap[2], 'Platform');

  var Alert = function () {
    function Alert() {
      babelHelpers.classCallCheck(this, Alert);
    }

    babelHelpers.createClass(Alert, null, [{
      key: "alert",
      value: function alert(title, message, buttons, options, type) {
        if (Platform.OS === 'ios') {
          if (typeof type !== 'undefined') {
            console.warn('Alert.alert() with a 5th "type" parameter is deprecated and will be removed. Use AlertIOS.prompt() instead.');
            AlertIOS.alert(title, message, buttons, type);
            return;
          }

          AlertIOS.alert(title, message, buttons);
        } else if (Platform.OS === 'android') {
          AlertAndroid.alert(title, message, buttons, options);
        }
      }
    }]);
    return Alert;
  }();

  var AlertAndroid = function () {
    function AlertAndroid() {
      babelHelpers.classCallCheck(this, AlertAndroid);
    }

    babelHelpers.createClass(AlertAndroid, null, [{
      key: "alert",
      value: function alert(title, message, buttons, options) {
        var config = {
          title: title || '',
          message: message || ''
        };

        if (options) {
          config = babelHelpers.extends({}, config, {
            cancelable: options.cancelable
          });
        }

        var validButtons = buttons ? buttons.slice(0, 3) : [{
          text: 'OK'
        }];
        var buttonPositive = validButtons.pop();
        var buttonNegative = validButtons.pop();
        var buttonNeutral = validButtons.pop();

        if (buttonNeutral) {
          config = babelHelpers.extends({}, config, {
            buttonNeutral: buttonNeutral.text || ''
          });
        }

        if (buttonNegative) {
          config = babelHelpers.extends({}, config, {
            buttonNegative: buttonNegative.text || ''
          });
        }

        if (buttonPositive) {
          config = babelHelpers.extends({}, config, {
            buttonPositive: buttonPositive.text || ''
          });
        }

        NativeModules.DialogManagerAndroid.showAlert(config, function (errorMessage) {
          return console.warn(errorMessage);
        }, function (action, buttonKey) {
          if (action === NativeModules.DialogManagerAndroid.buttonClicked) {
            if (buttonKey === NativeModules.DialogManagerAndroid.buttonNeutral) {
              buttonNeutral.onPress && buttonNeutral.onPress();
            } else if (buttonKey === NativeModules.DialogManagerAndroid.buttonNegative) {
              buttonNegative.onPress && buttonNegative.onPress();
            } else if (buttonKey === NativeModules.DialogManagerAndroid.buttonPositive) {
              buttonPositive.onPress && buttonPositive.onPress();
            }
          } else if (action === NativeModules.DialogManagerAndroid.dismissed) {
            options && options.onDismiss && options.onDismiss();
          }
        });
      }
    }]);
    return AlertAndroid;
  }();

  module.exports = Alert;
},"0c8048c84a9b8e4bf9556e3d67e355e7",["c80b578920da20660170a7b3984163ee","ce21807d4d291be64fa852393519f6c8","9493a89f5d95c3a8a47c65cfed9b5542"],"Alert");