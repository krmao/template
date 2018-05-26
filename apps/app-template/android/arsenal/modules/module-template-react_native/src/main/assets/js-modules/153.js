__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var invariant = _require(_dependencyMap[0], './invariant');

  var keyMirror = function keyMirror(obj) {
    var ret = {};
    var key;
    !(obj instanceof Object && !Array.isArray(obj)) ? process.env.NODE_ENV !== 'production' ? invariant(false, 'keyMirror(...): Argument must be an object.') : invariant(false) : void 0;

    for (key in obj) {
      if (!obj.hasOwnProperty(key)) {
        continue;
      }

      ret[key] = key;
    }

    return ret;
  };

  module.exports = keyMirror;
},153,[18],"node_modules/fbjs/lib/keyMirror.js");