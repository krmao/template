__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var ReactNative = void 0;

  if (__DEV__) {
    ReactNative = _require(_dependencyMap[0], 'ReactNativeRenderer-dev');
  } else {
    ReactNative = _require(_dependencyMap[1], 'ReactNativeRenderer-prod');
  }

  module.exports = ReactNative;
},49,[50,127],"ReactNative");