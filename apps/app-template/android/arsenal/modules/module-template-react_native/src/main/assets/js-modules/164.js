__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var assets = [];

  function registerAsset(asset) {
    return assets.push(asset);
  }

  function getAssetByID(assetId) {
    return assets[assetId - 1];
  }

  module.exports = {
    registerAsset: registerAsset,
    getAssetByID: getAssetByID
  };
},164,[],"AssetRegistry");