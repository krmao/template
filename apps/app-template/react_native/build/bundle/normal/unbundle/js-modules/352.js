__d(function (global, _require, module, exports, _dependencyMap) {
  module.exports = clamp;

  function clamp(value, min, max) {
    return min < max ? value < min ? min : value > max ? max : value : value < max ? max : value > min ? min : value;
  }
},352,[],"node_modules/clamp/index.js");