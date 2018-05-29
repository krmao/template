__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var canUseDOM = !!(typeof window !== 'undefined' && window.document && window.document.createElement);
  var ExecutionEnvironment = {
    canUseDOM: canUseDOM,
    canUseWorkers: typeof Worker !== 'undefined',
    canUseEventListeners: canUseDOM && !!(window.addEventListener || window.attachEvent),
    canUseViewport: canUseDOM && !!window.screen,
    isInWorker: !canUseDOM
  };
  module.exports = ExecutionEnvironment;
},"8aee437d234c0bc340e2b5c81b6dc884",[],"node_modules/fbjs/lib/ExecutionEnvironment.js");