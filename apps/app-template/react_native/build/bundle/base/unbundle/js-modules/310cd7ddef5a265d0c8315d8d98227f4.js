__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var ExecutionEnvironment = _require(_dependencyMap[0], './ExecutionEnvironment');

  var performance;

  if (ExecutionEnvironment.canUseDOM) {
    performance = window.performance || window.msPerformance || window.webkitPerformance;
  }

  module.exports = performance || {};
},"310cd7ddef5a265d0c8315d8d98227f4",["8aee437d234c0bc340e2b5c81b6dc884"],"node_modules/fbjs/lib/performance.js");