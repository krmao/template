__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var warning = _require(_dependencyMap[0], 'fbjs/lib/warning');

  var VibrationIOS = {
    vibrate: function vibrate() {
      warning('VibrationIOS is not supported on this platform!');
    }
  };
  module.exports = VibrationIOS;
},335,[19],"VibrationIOS");