__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var invariant = _require(_dependencyMap[0], 'fbjs/lib/invariant');

  var ensureComponentIsNative = function ensureComponentIsNative(component) {
    invariant(component && typeof component.setNativeProps === 'function', 'Touchable child must either be native or forward setNativeProps to a ' + 'native component');
  };

  module.exports = ensureComponentIsNative;
},"1422daa580d530201ccfee48c0551a95",["8940a4ad43b101ffc23e725363c70f8d"],"ensureComponentIsNative");