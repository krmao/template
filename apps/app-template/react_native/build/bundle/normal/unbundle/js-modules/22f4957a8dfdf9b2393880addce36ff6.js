__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  function isEmpty(obj) {
    if (Array.isArray(obj)) {
      return obj.length === 0;
    } else if (typeof obj === 'object') {
      for (var i in obj) {
        return false;
      }

      return true;
    } else {
      return !obj;
    }
  }

  module.exports = isEmpty;
},"22f4957a8dfdf9b2393880addce36ff6",[],"isEmpty");