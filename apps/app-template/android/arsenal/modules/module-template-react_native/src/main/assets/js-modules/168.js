__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var DeviceInfo = _require(_dependencyMap[0], 'NativeModules').DeviceInfo;

  var invariant = _require(_dependencyMap[1], 'fbjs/lib/invariant');

  invariant(DeviceInfo, 'DeviceInfo native module is not installed correctly');
  module.exports = DeviceInfo;
},168,[24,18],"DeviceInfo");