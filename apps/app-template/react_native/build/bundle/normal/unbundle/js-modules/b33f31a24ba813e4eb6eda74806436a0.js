__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var keyMirror = _require(_dependencyMap[0], 'fbjs/lib/keyMirror');

  var ImageResizeMode = keyMirror({
    contain: null,
    cover: null,
    stretch: null,
    center: null,
    repeat: null
  });
  module.exports = ImageResizeMode;
},"b33f31a24ba813e4eb6eda74806436a0",["61a2e2589ea976cf04a51cc242a466dd"],"ImageResizeMode");