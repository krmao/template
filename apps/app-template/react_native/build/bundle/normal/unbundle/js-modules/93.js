__d(function (global, _require2, module, exports, _dependencyMap) {
  'use strict';

  var EventTarget = _require2(_dependencyMap[0], 'event-target-shim');

  var Blob = _require2(_dependencyMap[1], 'Blob');

  var _require = _require2(_dependencyMap[2], 'NativeModules'),
      FileReaderModule = _require.FileReaderModule;

  var READER_EVENTS = ['abort', 'error', 'load', 'loadstart', 'loadend', 'progress'];
  var EMPTY = 0;
  var LOADING = 1;
  var DONE = 2;

  var FileReader = function (_EventTarget) {
    babelHelpers.inherits(FileReader, _EventTarget);

    function FileReader() {
      babelHelpers.classCallCheck(this, FileReader);

      var _this = babelHelpers.possibleConstructorReturn(this, (FileReader.__proto__ || Object.getPrototypeOf(FileReader)).call(this));

      _this.EMPTY = EMPTY;
      _this.LOADING = LOADING;
      _this.DONE = DONE;
      _this._aborted = false;
      _this._subscriptions = [];

      _this._reset();

      return _this;
    }

    babelHelpers.createClass(FileReader, [{
      key: "_reset",
      value: function _reset() {
        this._readyState = EMPTY;
        this._error = null;
        this._result = null;
      }
    }, {
      key: "_clearSubscriptions",
      value: function _clearSubscriptions() {
        this._subscriptions.forEach(function (sub) {
          return sub.remove();
        });

        this._subscriptions = [];
      }
    }, {
      key: "_setReadyState",
      value: function _setReadyState(newState) {
        this._readyState = newState;
        this.dispatchEvent({
          type: 'readystatechange'
        });

        if (newState === DONE) {
          if (this._aborted) {
            this.dispatchEvent({
              type: 'abort'
            });
          } else if (this._error) {
            this.dispatchEvent({
              type: 'error'
            });
          } else {
            this.dispatchEvent({
              type: 'load'
            });
          }

          this.dispatchEvent({
            type: 'loadend'
          });
        }
      }
    }, {
      key: "readAsArrayBuffer",
      value: function readAsArrayBuffer() {
        throw new Error('FileReader.readAsArrayBuffer is not implemented');
      }
    }, {
      key: "readAsDataURL",
      value: function readAsDataURL(blob) {
        var _this2 = this;

        this._aborted = false;
        FileReaderModule.readAsDataURL(blob.data).then(function (text) {
          if (_this2._aborted) {
            return;
          }

          _this2._result = text;

          _this2._setReadyState(DONE);
        }, function (error) {
          if (_this2._aborted) {
            return;
          }

          _this2._error = error;

          _this2._setReadyState(DONE);
        });
      }
    }, {
      key: "readAsText",
      value: function readAsText(blob) {
        var _this3 = this;

        var encoding = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : 'UTF-8';
        this._aborted = false;
        FileReaderModule.readAsText(blob.data, encoding).then(function (text) {
          if (_this3._aborted) {
            return;
          }

          _this3._result = text;

          _this3._setReadyState(DONE);
        }, function (error) {
          if (_this3._aborted) {
            return;
          }

          _this3._error = error;

          _this3._setReadyState(DONE);
        });
      }
    }, {
      key: "abort",
      value: function abort() {
        this._aborted = true;

        if (this._readyState !== EMPTY && this._readyState !== DONE) {
          this._reset();

          this._setReadyState(DONE);
        }

        this._reset();
      }
    }, {
      key: "readyState",
      get: function get() {
        return this._readyState;
      }
    }, {
      key: "error",
      get: function get() {
        return this._error;
      }
    }, {
      key: "result",
      get: function get() {
        return this._result;
      }
    }]);
    return FileReader;
  }(EventTarget.apply(undefined, READER_EVENTS));

  FileReader.EMPTY = EMPTY;
  FileReader.LOADING = LOADING;
  FileReader.DONE = DONE;
  module.exports = FileReader;
},93,[76,86,24],"FileReader");