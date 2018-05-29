__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var RCTAlertManager = _require(_dependencyMap[0], 'NativeModules').AlertManager;

  var AlertIOS = function () {
    function AlertIOS() {
      babelHelpers.classCallCheck(this, AlertIOS);
    }

    babelHelpers.createClass(AlertIOS, null, [{
      key: "alert",
      value: function alert(title, message, callbackOrButtons, type) {
        if (typeof type !== 'undefined') {
          console.warn('AlertIOS.alert() with a 4th "type" parameter is deprecated and will be removed. Use AlertIOS.prompt() instead.');
          this.prompt(title, message, callbackOrButtons, type);
          return;
        }

        this.prompt(title, message, callbackOrButtons, 'default');
      }
    }, {
      key: "prompt",
      value: function prompt(title, message, callbackOrButtons) {
        var type = arguments.length > 3 && arguments[3] !== undefined ? arguments[3] : 'plain-text';
        var defaultValue = arguments[4];
        var keyboardType = arguments[5];

        if (typeof type === 'function') {
          console.warn('You passed a callback function as the "type" argument to AlertIOS.prompt(). React Native is ' + 'assuming  you want to use the deprecated AlertIOS.prompt(title, defaultValue, buttons, callback) ' + 'signature. The current signature is AlertIOS.prompt(title, message, callbackOrButtons, type, defaultValue, ' + 'keyboardType) and the old syntax will be removed in a future version.');
          var callback = type;
          RCTAlertManager.alertWithArgs({
            title: title || '',
            type: 'plain-text',
            defaultValue: message
          }, function (id, value) {
            callback(value);
          });
          return;
        }

        var callbacks = [];
        var buttons = [];
        var cancelButtonKey;
        var destructiveButtonKey;

        if (typeof callbackOrButtons === 'function') {
          callbacks = [callbackOrButtons];
        } else if (callbackOrButtons instanceof Array) {
          callbackOrButtons.forEach(function (btn, index) {
            callbacks[index] = btn.onPress;

            if (btn.style === 'cancel') {
              cancelButtonKey = String(index);
            } else if (btn.style === 'destructive') {
              destructiveButtonKey = String(index);
            }

            if (btn.text || index < (callbackOrButtons || []).length - 1) {
              var btnDef = {};
              btnDef[index] = btn.text || '';
              buttons.push(btnDef);
            }
          });
        }

        RCTAlertManager.alertWithArgs({
          title: title || '',
          message: message || undefined,
          buttons: buttons,
          type: type || undefined,
          defaultValue: defaultValue,
          cancelButtonKey: cancelButtonKey,
          destructiveButtonKey: destructiveButtonKey,
          keyboardType: keyboardType
        }, function (id, value) {
          var cb = callbacks[id];
          cb && cb(value);
        });
      }
    }]);
    return AlertIOS;
  }();

  module.exports = AlertIOS;
},"c80b578920da20660170a7b3984163ee",["ce21807d4d291be64fa852393519f6c8"],"AlertIOS");