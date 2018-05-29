__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var TimePickerModule = _require(_dependencyMap[0], 'NativeModules').TimePickerAndroid;

  var TimePickerAndroid = function () {
    function TimePickerAndroid() {
      babelHelpers.classCallCheck(this, TimePickerAndroid);
    }

    babelHelpers.createClass(TimePickerAndroid, null, [{
      key: "open",
      value: function open(options) {
        return regeneratorRuntime.async(function open$(_context) {
          while (1) {
            switch (_context.prev = _context.next) {
              case 0:
                return _context.abrupt("return", TimePickerModule.open(options));

              case 1:
              case "end":
                return _context.stop();
            }
          }
        }, null, this);
      }
    }, {
      key: "timeSetAction",
      get: function get() {
        return 'timeSetAction';
      }
    }, {
      key: "dismissedAction",
      get: function get() {
        return 'dismissedAction';
      }
    }]);
    return TimePickerAndroid;
  }();

  module.exports = TimePickerAndroid;
},"07adf7180e4a352bcf2fa348733f43df",["ce21807d4d291be64fa852393519f6c8"],"TimePickerAndroid");