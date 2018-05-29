__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var key = '__global_unique_id__';

  module.exports = function () {
    return global[key] = (global[key] || 0) + 1;
  };
},"dbb421498ca47cd5ab1606b82043fbe4",[],"node_modules/gud/index.js");