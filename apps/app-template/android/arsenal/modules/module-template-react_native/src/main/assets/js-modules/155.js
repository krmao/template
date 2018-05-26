__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var Platform = _require(_dependencyMap[0], 'Platform');

  var normalizeColor = _require(_dependencyMap[1], 'normalizeColor');

  function processColor(color) {
    if (color === undefined || color === null) {
      return color;
    }

    var int32Color = normalizeColor(color);

    if (int32Color === null || int32Color === undefined) {
      return undefined;
    }

    int32Color = (int32Color << 24 | int32Color >>> 8) >>> 0;

    if (Platform.OS === 'android') {
      int32Color = int32Color | 0x0;
    }

    return int32Color;
  }

  module.exports = processColor;
},155,[32,47],"processColor");