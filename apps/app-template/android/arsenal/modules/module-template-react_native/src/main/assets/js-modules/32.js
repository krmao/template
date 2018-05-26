__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var NativeModules = _require(_dependencyMap[0], 'NativeModules');

  var Platform = {
    OS: 'android',

    get Version() {
      var constants = NativeModules.PlatformConstants;
      return constants && constants.Version;
    },

    get isTesting() {
      var constants = NativeModules.PlatformConstants;
      return constants && constants.isTesting;
    },

    get isTV() {
      var constants = NativeModules.PlatformConstants;
      return constants && constants.uiMode === 'tv';
    },

    select: function select(obj) {
      return 'android' in obj ? obj.android : obj.default;
    }
  };
  module.exports = Platform;
},32,[24],"Platform");