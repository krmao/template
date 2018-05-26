__d(function (global, _require2, module, exports, _dependencyMap) {
  'use strict';

  var _require = _require2(_dependencyMap[0], 'NativeModules'),
      SourceCode = _require.SourceCode;

  var _cachedDevServerURL = void 0;

  var FALLBACK = 'http://localhost:8081/';

  function getDevServer() {
    if (_cachedDevServerURL === undefined) {
      var match = SourceCode && SourceCode.scriptURL && SourceCode.scriptURL.match(/^https?:\/\/.*?\//);
      _cachedDevServerURL = match ? match[0] : null;
    }

    return {
      url: _cachedDevServerURL || FALLBACK,
      bundleLoadedFromServer: _cachedDevServerURL !== null
    };
  }

  module.exports = getDevServer;
},61,[24],"getDevServer");