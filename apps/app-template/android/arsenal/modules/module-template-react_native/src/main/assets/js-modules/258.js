__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var invariant = _require(_dependencyMap[0], 'fbjs/lib/invariant');

  var ensureComponentIsNative = function ensureComponentIsNative(component) {
    invariant(component && typeof component.setNativeProps === 'function', 'Touchable child must either be native or forward setNativeProps to a ' + 'native component');
  };

  module.exports = ensureComponentIsNative;
},258,[18],"ensureComponentIsNative");