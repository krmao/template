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
},152,[153],"ImageResizeMode");