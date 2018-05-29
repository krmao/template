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
},"a9f6e522b9deca7a3cff0ca4420ffce4",["096007123c886a641b5b7e629326a442","ce21807d4d291be64fa852393519f6c8"],"URL");