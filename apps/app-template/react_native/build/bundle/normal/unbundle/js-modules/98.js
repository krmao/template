__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var logError = function logError() {
    for (var _len = arguments.length, args = Array(_len), _key = 0; _key < _len; _key++) {
      args[_key] = arguments[_key];
    }

    if (args.length === 1 && args[0] instanceof Error) {
      var err = args[0];
      console.error('Error: "' + err.message + '".  Stack:\n' + err.stack);
    } else {
      console.error.apply(console, args);
    }
  };

  module.exports = logError;
},98,[],"logError");