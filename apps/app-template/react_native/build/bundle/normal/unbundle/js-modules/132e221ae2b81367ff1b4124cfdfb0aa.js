__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var performance = _require(_dependencyMap[0], './performance');

  var performanceNow;

  if (performance.now) {
    performanceNow = function performanceNow() {
      return performance.now();
    };
  } else {
    performanceNow = function performanceNow() {
      return Date.now();
    };
  }

  module.exports = performanceNow;
},"132e221ae2b81367ff1b4124cfdfb0aa",["310cd7ddef5a265d0c8315d8d98227f4"],"node_modules/fbjs/lib/performanceNow.js");