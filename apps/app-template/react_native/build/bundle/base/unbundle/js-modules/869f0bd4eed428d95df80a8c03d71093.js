__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var ReactNativePropRegistry;

  function getStyle(style) {
    if (ReactNativePropRegistry === undefined) {
      ReactNativePropRegistry = _require(_dependencyMap[0], 'ReactNativePropRegistry');
    }

    if (typeof style === 'number') {
      return ReactNativePropRegistry.getByID(style);
    }

    return style;
  }

  function flattenStyle(style) {
    if (style == null) {
      return undefined;
    }

    if (!Array.isArray(style)) {
      return getStyle(style);
    }

    var result = {};

    for (var i = 0, styleLength = style.length; i < styleLength; ++i) {
      var computedStyle = flattenStyle(style[i]);

      if (computedStyle) {
        for (var key in computedStyle) {
          result[key] = computedStyle[key];
        }
      }
    }

    return result;
  }

  module.exports = flattenStyle;
},"869f0bd4eed428d95df80a8c03d71093",["fa81bb30364d914a3fc2c9c4a20e13d8"],"flattenStyle");