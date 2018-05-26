__d(function (global, _require2, module, exports, _dependencyMap) {
  'use strict';

  var Blob = _require2(_dependencyMap[0], 'Blob');

  var _require = _require2(_dependencyMap[1], 'NativeModules'),
      BlobModule = _require.BlobModule;

  var BLOB_URL_PREFIX = null;

  if (BlobModule && typeof BlobModule.BLOB_URI_SCHEME === 'string') {
    BLOB_URL_PREFIX = BlobModule.BLOB_URI_SCHEME + ':';

    if (typeof BlobModule.BLOB_URI_HOST === 'string') {
      BLOB_URL_PREFIX += "//" + BlobModule.BLOB_URI_HOST + "/";
    }
  }

  var URL = function () {
    function URL() {
      babelHelpers.classCallCheck(this, URL);
      throw new Error('Creating URL objects is not supported yet.');
    }

    babelHelpers.createClass(URL, null, [{
      key: "createObjectURL",
      value: function createObjectURL(blob) {
        if (BLOB_URL_PREFIX === null) {
          throw new Error('Cannot create URL for blob!');
        }

        return "" + BLOB_URL_PREFIX + blob.data.blobId + "?offset=" + blob.data.offset + "&size=" + blob.size;
      }
    }, {
      key: "revokeObjectURL",
      value: function revokeObjectURL(url) {}
    }]);
    return URL;
  }();

  module.exports = URL;
},94,[86,24],"URL");