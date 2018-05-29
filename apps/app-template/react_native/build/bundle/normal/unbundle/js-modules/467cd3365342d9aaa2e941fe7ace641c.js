__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var NativeModules = _require(_dependencyMap[0], 'NativeModules');

  var Platform = _require(_dependencyMap[1], 'Platform');

  var defineLazyObjectProperty = _require(_dependencyMap[2], 'defineLazyObjectProperty');

  var invariant = _require(_dependencyMap[3], 'fbjs/lib/invariant');

  var UIManager = NativeModules.UIManager;
  invariant(UIManager, 'UIManager is undefined. The native module config is probably incorrect.');
  UIManager.__takeSnapshot = UIManager.takeSnapshot;

  UIManager.takeSnapshot = function () {
    invariant(false, 'UIManager.takeSnapshot should not be called directly. ' + 'Use ReactNative.takeSnapshot instead.');
  };

  if (Platform.OS === 'ios') {
    Object.keys(UIManager).forEach(function (viewName) {
      var viewConfig = UIManager[viewName];

      if (viewConfig.Manager) {
        defineLazyObjectProperty(viewConfig, 'Constants', {
          get: function get() {
            var viewManager = NativeModules[viewConfig.Manager];
            var constants = {};
            viewManager && Object.keys(viewManager).forEach(function (key) {
              var value = viewManager[key];

              if (typeof value !== 'function') {
                constants[key] = value;
              }
            });
            return constants;
          }
        });
        defineLazyObjectProperty(viewConfig, 'Commands', {
          get: function get() {
            var viewManager = NativeModules[viewConfig.Manager];
            var commands = {};
            var index = 0;
            viewManager && Object.keys(viewManager).forEach(function (key) {
              var value = viewManager[key];

              if (typeof value === 'function') {
                commands[key] = index++;
              }
            });
            return commands;
          }
        });
      }
    });
  } else if (UIManager.ViewManagerNames) {
    UIManager.ViewManagerNames.forEach(function (viewManagerName) {
      defineLazyObjectProperty(UIManager, viewManagerName, {
        get: function get() {
          return UIManager.getConstantsForViewManager(viewManagerName);
        }
      });
    });
  }

  module.exports = UIManager;
},"467cd3365342d9aaa2e941fe7ace641c",["ce21807d4d291be64fa852393519f6c8","9493a89f5d95c3a8a47c65cfed9b5542","e5152f0f2bf6d1f6699dd98929954a20","8940a4ad43b101ffc23e725363c70f8d"],"UIManager");