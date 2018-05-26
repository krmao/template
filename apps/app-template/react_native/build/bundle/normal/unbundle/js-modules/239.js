__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  function processDecelerationRate(decelerationRate) {
    if (decelerationRate === 'normal') {
      decelerationRate = 0.998;
    } else if (decelerationRate === 'fast') {
      decelerationRate = 0.99;
    }

    return decelerationRate;
  }

  module.exports = processDecelerationRate;
},239,[],"processDecelerationRate");