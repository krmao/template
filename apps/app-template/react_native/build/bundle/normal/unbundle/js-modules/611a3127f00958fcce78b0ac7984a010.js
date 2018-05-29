__d(function (global, _require, module, exports, _dependencyMap) {
  module.exports = clamp;

  function clamp(value, min, max) {
    return min < max ? value < min ? min : value > max ? max : value : value < max ? max : value > min ? min : value;
  }
},"611a3127f00958fcce78b0ac7984a010",[],"node_modules/clamp/index.js");