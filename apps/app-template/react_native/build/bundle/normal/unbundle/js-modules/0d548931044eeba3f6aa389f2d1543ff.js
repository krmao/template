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
},"0d548931044eeba3f6aa389f2d1543ff",["ce21807d4d291be64fa852393519f6c8"],"ImageEditor");