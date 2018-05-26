__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var RCTImageEditingManager = _require(_dependencyMap[0], 'NativeModules').ImageEditingManager;

  var ImageEditor = function () {
    function ImageEditor() {
      babelHelpers.classCallCheck(this, ImageEditor);
    }

    babelHelpers.createClass(ImageEditor, null, [{
      key: "cropImage",
      value: function cropImage(uri, cropData, success, failure) {
        RCTImageEditingManager.cropImage(uri, cropData, success, failure);
      }
    }]);
    return ImageEditor;
  }();

  module.exports = ImageEditor;
},259,[24],"ImageEditor");