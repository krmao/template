__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var BatchedBridge = _require(_dependencyMap[0], 'BatchedBridge');

  var RCTEventEmitter = {
    register: function register(eventEmitter) {
      BatchedBridge.registerCallableModule('RCTEventEmitter', eventEmitter);
    }
  };
  module.exports = RCTEventEmitter;
},"808ccd8f6989097bdad06b22e71c23ca",["fb53f04427490b2bd8fd29ce5c52844e"],"RCTEventEmitter");