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
},"d4f47991055a71c91b56160302f3fadc",["ce21807d4d291be64fa852393519f6c8"],"I18nManager");