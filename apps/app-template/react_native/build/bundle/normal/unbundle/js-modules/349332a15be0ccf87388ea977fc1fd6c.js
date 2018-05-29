__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var DatePickerModule = _require(_dependencyMap[0], 'NativeModules').DatePickerAndroid;

  function _toMillis(options, key) {
    var dateVal = options[key];

    if (typeof dateVal === 'object' && typeof dateVal.getMonth === 'function') {
      options[key] = dateVal.getTime();
    }
  }

  var DatePickerAndroid = function () {
    function DatePickerAndroid() {
      babelHelpers.classCallCheck(this, DatePickerAndroid);
    }

    babelHelpers.createClass(DatePickerAndroid, null, [{
      key: "open",
      value: function open(options) {
        var optionsMs;
        return regeneratorRuntime.async(function open$(_context) {
          while (1) {
            switch (_context.prev = _context.next) {
              case 0:
                optionsMs = options;

                if (optionsMs) {
                  _toMillis(options, 'date');

                  _toMillis(options, 'minDate');

                  _toMillis(options, 'maxDate');
                }

                return _context.abrupt("return", DatePickerModule.open(options));

              case 3:
              case "end":
                return _context.stop();
            }
          }
        }, null, this);
      }
    }, {
      key: "dateSetAction",
      get: function get() {
        return 'dateSetAction';
      }
    }, {
      key: "dismissedAction",
      get: function get() {
        return 'dismissedAction';
      }
    }]);
    return DatePickerAndroid;
  }();

  module.exports = DatePickerAndroid;
},"349332a15be0ccf87388ea977fc1fd6c",["ce21807d4d291be64fa852393519f6c8"],"DatePickerAndroid");