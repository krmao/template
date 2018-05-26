__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var ExecutionEnvironment = _require(_dependencyMap[0], './ExecutionEnvironment');

  var performance;

  if (ExecutionEnvironment.canUseDOM) {
    performance = window.performance || window.msPerformance || window.webkitPerformance;
  }

  module.exports = performance || {};
},34,[35],"node_modules/fbjs/lib/performance.js");