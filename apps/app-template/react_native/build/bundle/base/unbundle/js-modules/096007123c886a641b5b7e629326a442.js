__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var Blob = function () {
    function Blob() {
      var parts = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : [];
      var options = arguments[1];
      babelHelpers.classCallCheck(this, Blob);

      var BlobManager = _require(_dependencyMap[0], 'BlobManager');

      this.data = BlobManager.createFromParts(parts, options).data;
    }

    babelHelpers.createClass(Blob, [{
      key: "slice",
      value: function slice(start, end) {
        var BlobManager = _require(_dependencyMap[0], 'BlobManager');

        var _data = this.data,
            offset = _data.offset,
            size = _data.size;

        if (typeof start === 'number') {
          if (start > size) {
            start = size;
          }

          offset += start;
          size -= start;

          if (typeof end === 'number') {
            if (end < 0) {
              end = this.size + end;
            }

            size = end - start;
          }
        }

        return BlobManager.createFromOptions({
          blobId: this.data.blobId,
          offset: offset,
          size: size
        });
      }
    }, {
      key: "close",
      value: function close() {
        var BlobManager = _require(_dependencyMap[0], 'BlobManager');

        BlobManager.release(this.data.blobId);
        this.data = null;
      }
    }, {
      key: "data",
      set: function set(data) {
        this._data = data;
      },
      get: function get() {
        if (!this._data) {
          throw new Error('Blob has been closed and is no longer available');
        }

        return this._data;
      }
    }, {
      key: "size",
      get: function get() {
        return this.data.size;
      }
    }, {
      key: "type",
      get: function get() {
        return this.data.type || '';
      }
    }]);
    return Blob;
  }();

  module.exports = Blob;
},"096007123c886a641b5b7e629326a442",["6432c6a42bdc71908935daf61e70dba8"],"Blob");