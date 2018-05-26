__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var register = function register() {};

  if (__DEV__) {
    var AppState = _require(_dependencyMap[0], 'AppState');

    var WebSocket = _require(_dependencyMap[1], 'WebSocket');

    var reactDevTools = _require(_dependencyMap[2], 'react-devtools-core');

    var getDevServer = _require(_dependencyMap[3], 'getDevServer');

    if (WebSocket.isAvailable) {
      var _isAppActive = function _isAppActive() {
        return AppState.currentState !== 'background';
      };

      var devServer = getDevServer();

      var _host = devServer.bundleLoadedFromServer ? devServer.url.replace(/https?:\/\//, '').split(':')[0] : 'localhost';

      reactDevTools.connectToDevTools({
        isAppActive: _isAppActive,
        host: _host,
        port: window.__REACT_DEVTOOLS_PORT__,
        resolveRNStyle: _require(_dependencyMap[4], 'flattenStyle')
      });
    }
  }

  module.exports = {
    register: register
  };
},113,[114,90,115,61,116],"setupDevtools");