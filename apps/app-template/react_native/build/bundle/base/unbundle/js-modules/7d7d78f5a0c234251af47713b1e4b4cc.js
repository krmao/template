__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var defineLazyObjectProperty = _require(_dependencyMap[0], 'defineLazyObjectProperty');

  function polyfillObjectProperty(object, name, getValue) {
    var descriptor = Object.getOwnPropertyDescriptor(object, name);

    if (__DEV__ && descriptor) {
      var backupName = "original" + name[0].toUpperCase() + name.substr(1);
      Object.defineProperty(object, backupName, babelHelpers.extends({}, descriptor, {
        value: object[name]
      }));
    }

    var _ref = descriptor || {},
        enumerable = _ref.enumerable,
        writable = _ref.writable,
        configurable = _ref.configurable;

    if (descriptor && !configurable) {
      console.error('Failed to set polyfill. ' + name + ' is not configurable.');
      return;
    }

    defineLazyObjectProperty(object, name, {
      get: getValue,
      enumerable: enumerable !== false,
      writable: writable !== false
    });
  }

  function polyfillGlobal(name, getValue) {
    polyfillObjectProperty(global, name, getValue);
  }

  module.exports = {
    polyfillObjectProperty: polyfillObjectProperty,
    polyfillGlobal: polyfillGlobal
  };
},"7d7d78f5a0c234251af47713b1e4b4cc",["e5152f0f2bf6d1f6699dd98929954a20"],"PolyfillFunctions");