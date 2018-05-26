__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var BatchedBridge = _require(_dependencyMap[0], 'BatchedBridge');

  var RCTEventEmitter = {
    register: function register(eventEmitter) {
      BatchedBridge.registerCallableModule('RCTEventEmitter', eventEmitter);
    }
  };
  module.exports = RCTEventEmitter;
},122,[25],"RCTEventEmitter");