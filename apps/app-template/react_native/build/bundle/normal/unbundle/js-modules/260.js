__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var RCTImageStoreManager = _require(_dependencyMap[0], 'NativeModules').ImageStoreManager;

  var ImageStore = function () {
    function ImageStore() {
      babelHelpers.classCallCheck(this, ImageStore);
    }

    babelHelpers.createClass(ImageStore, null, [{
      key: "hasImageForTag",
      value: function hasImageForTag(uri, callback) {
        if (RCTImageStoreManager.hasImageForTag) {
          RCTImageStoreManager.hasImageForTag(uri, callback);
        } else {
          console.warn('hasImageForTag() not implemented');
        }
      }
    }, {
      key: "removeImageForTag",
      value: function removeImageForTag(uri) {
        if (RCTImageStoreManager.removeImageForTag) {
          RCTImageStoreManager.removeImageForTag(uri);
        } else {
          console.warn('removeImageForTag() not implemented');
        }
      }
    }, {
      key: "addImageFromBase64",
      value: function addImageFromBase64(base64ImageData, success, failure) {
        RCTImageStoreManager.addImageFromBase64(base64ImageData, success, failure);
      }
    }, {
      key: "getBase64ForTag",
      value: function getBase64ForTag(uri, success, failure) {
        RCTImageStoreManager.getBase64ForTag(uri, success, failure);
      }
    }]);
    return ImageStore;
  }();

  module.exports = ImageStore;
},260,[24],"ImageStore");