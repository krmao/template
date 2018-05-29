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
},"9493a89f5d95c3a8a47c65cfed9b5542",["ce21807d4d291be64fa852393519f6c8"],"Platform");