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
},"9ce443f0435b534a4b0cfc7f0a97c5e0",["b2885f10a3cd281ac7558b783d308764","b54f8d6a8fec03e3197a9d378e32fbd2","3901ae74c84a5184d0a5b56392882ced","760b6f2c9fa353738d851f7c59cb5365","869f0bd4eed428d95df80a8c03d71093"],"setupDevtools");