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
},"432b5a4e24973de1461ae840f9e0905c",[],"processDecelerationRate");