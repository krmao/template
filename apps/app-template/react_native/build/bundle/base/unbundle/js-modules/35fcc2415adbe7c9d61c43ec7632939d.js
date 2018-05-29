__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var InteractionManager = _require(_dependencyMap[0], 'InteractionManager');

  var Batchinator = function () {
    function Batchinator(callback, delayMS) {
      babelHelpers.classCallCheck(this, Batchinator);
      this._delay = delayMS;
      this._callback = callback;
    }

    babelHelpers.createClass(Batchinator, [{
      key: "dispose",
      value: function dispose() {
        var options = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {
          abort: false
        };

        if (this._taskHandle) {
          this._taskHandle.cancel();

          if (!options.abort) {
            this._callback();
          }

          this._taskHandle = null;
        }
      }
    }, {
      key: "schedule",
      value: function schedule() {
        var _this = this;

        if (this._taskHandle) {
          return;
        }

        var timeoutHandle = setTimeout(function () {
          _this._taskHandle = InteractionManager.runAfterInteractions(function () {
            _this._taskHandle = null;

            _this._callback();
          });
        }, this._delay);
        this._taskHandle = {
          cancel: function cancel() {
            return clearTimeout(timeoutHandle);
          }
        };
      }
    }]);
    return Batchinator;
  }();

  module.exports = Batchinator;
},"35fcc2415adbe7c9d61c43ec7632939d",["000e46d8eda06a212663acb7d3fd4965"],"Batchinator");