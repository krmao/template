__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var MessageQueue = _require(_dependencyMap[0], 'MessageQueue');

  var BatchedBridge = new MessageQueue(typeof __fbUninstallRNGlobalErrorHandler !== 'undefined' && __fbUninstallRNGlobalErrorHandler === true);
  Object.defineProperty(global, '__fbBatchedBridge', {
    configurable: true,
    value: BatchedBridge
  });
  module.exports = BatchedBridge;
},25,[26],"BatchedBridge");