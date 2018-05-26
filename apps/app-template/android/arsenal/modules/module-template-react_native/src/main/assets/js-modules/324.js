__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var Clipboard = _require(_dependencyMap[0], 'NativeModules').Clipboard;

  module.exports = {
    getString: function getString() {
      return Clipboard.getString();
    },
    setString: function setString(content) {
      Clipboard.setString(content);
    }
  };
},324,[24],"Clipboard");