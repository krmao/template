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
},36,[37],"parseErrorStack");