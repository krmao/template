__d(function (global, _require, module, exports, _dependencyMap) {
  "use strict";

  var keyOf = function keyOf(oneKeyObj) {
    var key;

    for (key in oneKeyObj) {
      if (!oneKeyObj.hasOwnProperty(key)) {
        continue;
      }

      return key;
    }

    return null;
  };

  module.exports = keyOf;
},"c3fc1c3123a4d0bcadefb6e75134743a",[],"node_modules/fbjs/lib/keyOf.js");