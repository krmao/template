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
},314,[24,18,155],"ActionSheetIOS");