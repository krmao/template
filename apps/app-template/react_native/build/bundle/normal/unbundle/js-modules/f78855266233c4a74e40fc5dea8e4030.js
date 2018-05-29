__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var ESCAPED_CHARACTERS = /(\\|\"|\')/g;

  module.exports = function printString(val) {
    return val.replace(ESCAPED_CHARACTERS, '\\$1');
  };
},"f78855266233c4a74e40fc5dea8e4030",[],"node_modules/pretty-format/printString.js");