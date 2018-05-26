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
},309,[],"node_modules/fbjs/lib/keyOf.js");