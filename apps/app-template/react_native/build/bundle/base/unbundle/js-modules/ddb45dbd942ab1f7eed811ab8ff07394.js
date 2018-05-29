__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var warning = _require(_dependencyMap[0], 'fbjs/lib/warning');

  var VibrationIOS = {
    vibrate: function vibrate() {
      warning('VibrationIOS is not supported on this platform!');
    }
  };
  module.exports = VibrationIOS;
},"ddb45dbd942ab1f7eed811ab8ff07394",["09babf511a081d9520406a63f452d2ef"],"VibrationIOS");