__d(function (global, _require, module, exports, _dependencyMap) {
  "use strict";

  var mergeInto = _require(_dependencyMap[0], 'mergeInto');

  var merge = function merge(one, two) {
    var result = {};
    mergeInto(result, one);
    mergeInto(result, two);
    return result;
  };

  module.exports = merge;
},136,[137],"merge");