__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var matricesDiffer = function matricesDiffer(one, two) {
    if (one === two) {
      return false;
    }

    return !one || !two || one[12] !== two[12] || one[13] !== two[13] || one[14] !== two[14] || one[5] !== two[5] || one[10] !== two[10] || one[1] !== two[1] || one[2] !== two[2] || one[3] !== two[3] || one[4] !== two[4] || one[6] !== two[6] || one[7] !== two[7] || one[8] !== two[8] || one[9] !== two[9] || one[11] !== two[11] || one[15] !== two[15];
  };

  module.exports = matricesDiffer;
},161,[],"matricesDiffer");