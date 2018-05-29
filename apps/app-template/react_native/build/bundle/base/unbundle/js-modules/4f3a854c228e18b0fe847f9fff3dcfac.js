__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  function stringifySafe(arg) {
    var ret;
    var type = typeof arg;

    if (arg === undefined) {
      ret = 'undefined';
    } else if (arg === null) {
      ret = 'null';
    } else if (type === 'string') {
      ret = '"' + arg + '"';
    } else if (type === 'function') {
      try {
        ret = arg.toString();
      } catch (e) {
        ret = '[function unknown]';
      }
    } else {
      try {
        ret = JSON.stringify(arg);
      } catch (e) {
        if (typeof arg.toString === 'function') {
          try {
            ret = arg.toString();
          } catch (E) {}
        }
      }
    }

    return ret || '["' + type + '" failed to stringify]';
  }

  module.exports = stringifySafe;
},"4f3a854c228e18b0fe847f9fff3dcfac",[],"stringifySafe");