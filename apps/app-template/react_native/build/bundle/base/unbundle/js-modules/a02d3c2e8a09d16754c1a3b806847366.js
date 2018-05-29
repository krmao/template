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
},"a02d3c2e8a09d16754c1a3b806847366",["f3718d296d8986c9c44d4cd08ac2cd62"],"merge");