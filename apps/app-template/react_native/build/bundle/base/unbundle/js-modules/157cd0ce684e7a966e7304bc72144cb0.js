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
},"157cd0ce684e7a966e7304bc72144cb0",["ce21807d4d291be64fa852393519f6c8"],"Clipboard");