__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  function getAndroidAssetSuffix(scale) {
    switch (scale) {
      case 0.75:
        return 'ldpi';

      case 1:
        return 'mdpi';

      case 1.5:
        return 'hdpi';

      case 2:
        return 'xhdpi';

      case 3:
        return 'xxhdpi';

      case 4:
        return 'xxxhdpi';
    }

    throw new Error('no such scale');
  }

  var drawableFileTypes = new Set(['gif', 'jpeg', 'jpg', 'png', 'svg', 'webp', 'xml']);

  function getAndroidResourceFolderName(asset, scale) {
    if (!drawableFileTypes.has(asset.type)) {
      return 'raw';
    }

    var suffix = getAndroidAssetSuffix(scale);

    if (!suffix) {
      throw new Error('Don\'t know which android drawable suffix to use for asset: ' + JSON.stringify(asset));
    }

    var androidFolder = 'drawable-' + suffix;
    return androidFolder;
  }

  function getAndroidResourceIdentifier(asset) {
    var folderPath = getBasePath(asset);
    return (folderPath + '/' + asset.name).toLowerCase().replace(/\//g, '_').replace(/([^a-z0-9_])/g, '').replace(/^assets_/, '');
  }

  function getBasePath(asset) {
    var basePath = asset.httpServerLocation;

    if (basePath[0] === '/') {
      basePath = basePath.substr(1);
    }

    return basePath;
  }

  module.exports = {
    getAndroidAssetSuffix: getAndroidAssetSuffix,
    getAndroidResourceFolderName: getAndroidResourceFolderName,
    getAndroidResourceIdentifier: getAndroidResourceIdentifier,
    getBasePath: getBasePath
  };
},169,[],"node_modules/react-native/local-cli/bundle/assetPathUtils.js");