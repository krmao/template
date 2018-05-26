__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  function defineLazyObjectProperty(object, name, descriptor) {
    var get = descriptor.get;
    var enumerable = descriptor.enumerable !== false;
    var writable = descriptor.writable !== false;
    var value = void 0;
    var valueSet = false;

    function getValue() {
      if (!valueSet) {
        valueSet = true;
        setValue(get());
      }

      return value;
    }

    function setValue(newValue) {
      value = newValue;
      valueSet = true;
      Object.defineProperty(object, name, {
        value: newValue,
        configurable: true,
        enumerable: enumerable,
        writable: writable
      });
    }

    Object.defineProperty(object, name, {
      get: getValue,
      set: setValue,
      configurable: true,
      enumerable: enumerable
    });
  }

  module.exports = defineLazyObjectProperty;
},39,[],"defineLazyObjectProperty");