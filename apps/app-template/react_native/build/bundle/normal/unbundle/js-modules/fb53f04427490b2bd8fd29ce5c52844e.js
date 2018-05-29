__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var MessageQueue = _require(_dependencyMap[0], 'MessageQueue');

  var BatchedBridge = new MessageQueue(typeof __fbUninstallRNGlobalErrorHandler !== 'undefined' && __fbUninstallRNGlobalErrorHandler === true);
  Object.defineProperty(global, '__fbBatchedBridge', {
    configurable: true,
    value: BatchedBridge
  });
  module.exports = BatchedBridge;
},"fb53f04427490b2bd8fd29ce5c52844e",["a85985c373d93e0fd200bd438fe5e038"],"BatchedBridge");