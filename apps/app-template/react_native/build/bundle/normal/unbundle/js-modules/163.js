__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var AssetRegistry = _require(_dependencyMap[0], 'AssetRegistry');

  var AssetSourceResolver = _require(_dependencyMap[1], 'AssetSourceResolver');

  var _customSourceTransformer = void 0,
      _serverURL = void 0,
      _scriptURL = void 0;

  var _sourceCodeScriptURL = void 0;

  function getDevServerURL() {
    if (_serverURL === undefined) {
      var match = _sourceCodeScriptURL && _sourceCodeScriptURL.match(/^https?:\/\/.*?\//);

      if (match) {
        _serverURL = match[0];
      } else {
        _serverURL = null;
      }
    }

    return _serverURL;
  }

  function _coerceLocalScriptURL(scriptURL) {
    if (scriptURL) {
      if (scriptURL.startsWith('assets://')) {
        return null;
      }

      scriptURL = scriptURL.substring(0, scriptURL.lastIndexOf('/') + 1);

      if (!scriptURL.includes('://')) {
        scriptURL = 'file://' + scriptURL;
      }
    }

    return scriptURL;
  }

  function getScriptURL() {
    if (_scriptURL === undefined) {
      _scriptURL = _coerceLocalScriptURL(_sourceCodeScriptURL);
    }

    return _scriptURL;
  }

  function setCustomSourceTransformer(transformer) {
    _customSourceTransformer = transformer;
  }

  function resolveAssetSource(source) {
    if (typeof source === 'object') {
      return source;
    }

    var asset = AssetRegistry.getAssetByID(source);

    if (!asset) {
      return null;
    }

    var resolver = new AssetSourceResolver(getDevServerURL(), getScriptURL(), asset);

    if (_customSourceTransformer) {
      return _customSourceTransformer(resolver);
    }

    return resolver.defaultAsset();
  }

  var sourceCode = global.nativeExtensions && global.nativeExtensions.SourceCode;

  if (!sourceCode) {
    var NativeModules = _require(_dependencyMap[2], 'NativeModules');

    sourceCode = NativeModules && NativeModules.SourceCode;
  }

  _sourceCodeScriptURL = sourceCode && sourceCode.scriptURL;
  module.exports = resolveAssetSource;
  module.exports.pickScale = AssetSourceResolver.pickScale;
  module.exports.setCustomSourceTransformer = setCustomSourceTransformer;
},163,[164,165,24],"resolveAssetSource");