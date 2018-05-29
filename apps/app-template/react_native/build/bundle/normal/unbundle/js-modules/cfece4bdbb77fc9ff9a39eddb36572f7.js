__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var RCTImagePicker = _require(_dependencyMap[0], 'NativeModules').ImagePickerIOS;

  var ImagePickerIOS = {
    canRecordVideos: function canRecordVideos(callback) {
      return RCTImagePicker.canRecordVideos(callback);
    },
    canUseCamera: function canUseCamera(callback) {
      return RCTImagePicker.canUseCamera(callback);
    },
    openCameraDialog: function openCameraDialog(config, successCallback, cancelCallback) {
      config = babelHelpers.extends({
        videoMode: false
      }, config);
      return RCTImagePicker.openCameraDialog(config, successCallback, cancelCallback);
    },
    openSelectDialog: function openSelectDialog(config, successCallback, cancelCallback) {
      config = babelHelpers.extends({
        showImages: true,
        showVideos: false
      }, config);
      return RCTImagePicker.openSelectDialog(config, successCallback, cancelCallback);
    }
  };
  module.exports = ImagePickerIOS;
},"cfece4bdbb77fc9ff9a39eddb36572f7",["ce21807d4d291be64fa852393519f6c8"],"ImagePickerIOS");