__d(function (global, _require2, module, exports, _dependencyMap) {
  'use strict';

  var Platform = _require2(_dependencyMap[0], 'Platform');

  var invariant = _require2(_dependencyMap[1], 'fbjs/lib/invariant');

  var processColor = _require2(_dependencyMap[2], 'processColor');

  var _require = _require2(_dependencyMap[3], 'NativeModules'),
      ActionSheetManager = _require.ActionSheetManager,
      ShareModule = _require.ShareModule;

  var Share = function () {
    function Share() {
      babelHelpers.classCallCheck(this, Share);
    }

    babelHelpers.createClass(Share, null, [{
      key: "share",
      value: function share(content) {
        var options = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
        invariant(typeof content === 'object' && content !== null, 'Content to share must be a valid object');
        invariant(typeof content.url === 'string' || typeof content.message === 'string', 'At least one of URL and message is required');
        invariant(typeof options === 'object' && options !== null, 'Options must be a valid object');

        if (Platform.OS === 'android') {
          invariant(!content.title || typeof content.title === 'string', 'Invalid title: title should be a string.');
          return ShareModule.share(content, options.dialogTitle);
        } else if (Platform.OS === 'ios') {
          return new Promise(function (resolve, reject) {
            ActionSheetManager.showShareActionSheetWithOptions(babelHelpers.extends({}, content, options, {
              tintColor: processColor(options.tintColor)
            }), function (error) {
              return reject(error);
            }, function (success, activityType) {
              if (success) {
                resolve({
                  'action': 'sharedAction',
                  'activityType': activityType
                });
              } else {
                resolve({
                  'action': 'dismissedAction'
                });
              }
            });
          });
        } else {
          return Promise.reject(new Error('Unsupported platform'));
        }
      }
    }, {
      key: "sharedAction",
      get: function get() {
        return 'sharedAction';
      }
    }, {
      key: "dismissedAction",
      get: function get() {
        return 'dismissedAction';
      }
    }]);
    return Share;
  }();

  module.exports = Share;
},"2873fbc02acdb2392310fd8078ece860",["9493a89f5d95c3a8a47c65cfed9b5542","8940a4ad43b101ffc23e725363c70f8d","1b69977972a3b6ad650756d07de7954c","ce21807d4d291be64fa852393519f6c8"],"Share");