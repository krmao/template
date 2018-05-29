__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var getDevServer = _require(_dependencyMap[0], 'getDevServer');

  function openFileInEditor(file, lineNumber) {
    fetch(getDevServer().url + 'open-stack-frame', {
      method: 'POST',
      body: JSON.stringify({
        file: file,
        lineNumber: lineNumber
      })
    });
  }

  module.exports = openFileInEditor;
},"74b32f55a0bb628f9e8dc4e21b02d7a1",["760b6f2c9fa353738d851f7c59cb5365"],"openFileInEditor");