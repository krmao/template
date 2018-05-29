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
},"61a2e2589ea976cf04a51cc242a466dd",["8940a4ad43b101ffc23e725363c70f8d"],"node_modules/fbjs/lib/keyMirror.js");