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
},248,[],"isEmpty");