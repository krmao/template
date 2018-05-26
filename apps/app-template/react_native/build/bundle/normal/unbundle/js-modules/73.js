__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var ESCAPED_CHARACTERS = /(\\|\"|\')/g;

  module.exports = function printString(val) {
    return val.replace(ESCAPED_CHARACTERS, '\\$1');
  };
},73,[],"node_modules/pretty-format/printString.js");