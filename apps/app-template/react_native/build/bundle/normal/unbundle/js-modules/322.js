__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var BackHandler = _require(_dependencyMap[0], 'BackHandler');

  var warning = _require(_dependencyMap[1], 'fbjs/lib/warning');

  var BackAndroid = {
    exitApp: function exitApp() {
      warning(false, 'BackAndroid is deprecated.  Please use BackHandler instead.');
      BackHandler.exitApp();
    },
    addEventListener: function addEventListener(eventName, handler) {
      warning(false, 'BackAndroid is deprecated.  Please use BackHandler instead.');
      return BackHandler.addEventListener(eventName, handler);
    },
    removeEventListener: function removeEventListener(eventName, handler) {
      warning(false, 'BackAndroid is deprecated.  Please use BackHandler instead.');
      BackHandler.removeEventListener(eventName, handler);
    }
  };
  module.exports = BackAndroid;
},322,[320,19],"BackAndroid");