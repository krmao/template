__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var DeviceInfo = _require(_dependencyMap[0], 'NativeModules').DeviceInfo;

  var invariant = _require(_dependencyMap[1], 'fbjs/lib/invariant');

  invariant(DeviceInfo, 'DeviceInfo native module is not installed correctly');
  module.exports = DeviceInfo;
},"bbcc69856ded422ceb1f7386b087a4ad",["ce21807d4d291be64fa852393519f6c8","8940a4ad43b101ffc23e725363c70f8d"],"DeviceInfo");