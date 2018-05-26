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
},116,[117],"flattenStyle");