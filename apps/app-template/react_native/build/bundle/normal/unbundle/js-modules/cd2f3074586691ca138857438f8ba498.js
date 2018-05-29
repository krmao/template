__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var mergeFast = function mergeFast(one, two) {
    var ret = {};

    for (var keyOne in one) {
      ret[keyOne] = one[keyOne];
    }

    for (var keyTwo in two) {
      ret[keyTwo] = two[keyTwo];
    }

    return ret;
  };

  module.exports = mergeFast;
},"cd2f3074586691ca138857438f8ba498",[],"mergeFast");