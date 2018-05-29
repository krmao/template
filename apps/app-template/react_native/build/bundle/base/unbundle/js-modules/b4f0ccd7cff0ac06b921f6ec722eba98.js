__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  function parseErrorStack(e) {
    if (!e || !e.stack) {
      return [];
    }

    var stacktraceParser = _require(_dependencyMap[0], 'stacktrace-parser');

    var stack = Array.isArray(e.stack) ? e.stack : stacktraceParser.parse(e.stack);
    var framesToPop = typeof e.framesToPop === 'number' ? e.framesToPop : 0;

    while (framesToPop--) {
      stack.shift();
    }

    return stack;
  }

  module.exports = parseErrorStack;
},"b4f0ccd7cff0ac06b921f6ec722eba98",["69773e09a24b641b783e09f5c26d7e17"],"parseErrorStack");