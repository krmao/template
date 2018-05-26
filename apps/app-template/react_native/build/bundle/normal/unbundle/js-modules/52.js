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
},52,[39],"PolyfillFunctions");