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
},277,[61],"openFileInEditor");