__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var Settings = {
    get: function get(key) {
      console.warn('Settings is not yet supported on Android');
      return null;
    },
    set: function set(settings) {
      console.warn('Settings is not yet supported on Android');
    },
    watchKeys: function watchKeys(keys, callback) {
      console.warn('Settings is not yet supported on Android');
      return -1;
    },
    clearWatch: function clearWatch(watchId) {
      console.warn('Settings is not yet supported on Android');
    }
  };
  module.exports = Settings;
},"3763f5a9c38680e04a2b819b6a5931cb",[],"Settings");