__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var RCTActionSheetManager = _require(_dependencyMap[0], 'NativeModules').ActionSheetManager;

  var invariant = _require(_dependencyMap[1], 'fbjs/lib/invariant');

  var processColor = _require(_dependencyMap[2], 'processColor');

  var ActionSheetIOS = {
    showActionSheetWithOptions: function showActionSheetWithOptions(options, callback) {
      invariant(typeof options === 'object' && options !== null, 'Options must be a valid object');
      invariant(typeof callback === 'function', 'Must provide a valid callback');
      RCTActionSheetManager.showActionSheetWithOptions(babelHelpers.extends({}, options, {
        tintColor: processColor(options.tintColor)
      }), callback);
    },
    showShareActionSheetWithOptions: function showShareActionSheetWithOptions(options, failureCallback, successCallback) {
      invariant(typeof options === 'object' && options !== null, 'Options must be a valid object');
      invariant(typeof failureCallback === 'function', 'Must provide a valid failureCallback');
      invariant(typeof successCallback === 'function', 'Must provide a valid successCallback');
      RCTActionSheetManager.showShareActionSheetWithOptions(babelHelpers.extends({}, options, {
        tintColor: processColor(options.tintColor)
      }), failureCallback, successCallback);
    }
  };
  module.exports = ActionSheetIOS;
},"04856e5fd79e2e21740f32f3cc510f18",["ce21807d4d291be64fa852393519f6c8","8940a4ad43b101ffc23e725363c70f8d","1b69977972a3b6ad650756d07de7954c"],"ActionSheetIOS");