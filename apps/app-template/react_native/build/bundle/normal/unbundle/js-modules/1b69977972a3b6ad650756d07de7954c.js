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
},"1b69977972a3b6ad650756d07de7954c",["9493a89f5d95c3a8a47c65cfed9b5542","16e4ebd2e425393d17dd977daf378657"],"processColor");