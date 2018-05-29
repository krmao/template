__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var ReactNative = void 0;

  if (__DEV__) {
    ReactNative = _require(_dependencyMap[0], 'ReactNativeRenderer-dev');
  } else {
    ReactNative = _require(_dependencyMap[1], 'ReactNativeRenderer-prod');
  }

  module.exports = ReactNative;
},"1102b68d89d7a6aede9677567aa01362",["e8b1ff97f94492f70485eaf5085880a7","b1200c8892cc7d485110adf7a46f4f27"],"ReactNative");