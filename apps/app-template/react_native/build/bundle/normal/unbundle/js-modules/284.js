__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var I18nManager = _require(_dependencyMap[0], 'NativeModules').I18nManager || {
    isRTL: false,
    doLeftAndRightSwapInRTL: true,
    allowRTL: function allowRTL() {},
    forceRTL: function forceRTL() {},
    swapLeftAndRightInRTL: function swapLeftAndRightInRTL() {}
  };
  module.exports = I18nManager;
},284,[24],"I18nManager");