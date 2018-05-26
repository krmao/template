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
},33,[34],"node_modules/fbjs/lib/performanceNow.js");