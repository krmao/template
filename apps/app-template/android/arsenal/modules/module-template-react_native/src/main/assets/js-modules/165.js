__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var PixelRatio = _require(_dependencyMap[0], 'PixelRatio');

  var Platform = _require(_dependencyMap[1], 'Platform');

  var assetPathUtils = _require(_dependencyMap[2], '../../local-cli/bundle/assetPathUtils');

  var invariant = _require(_dependencyMap[3], 'fbjs/lib/invariant');

  function getScaledAssetPath(asset) {
    var scale = AssetSourceResolver.pickScale(asset.scales, PixelRatio.get());
    var scaleSuffix = scale === 1 ? '' : '@' + scale + 'x';
    var assetDir = assetPathUtils.getBasePath(asset);
    return assetDir + '/' + asset.name + scaleSuffix + '.' + asset.type;
  }

  function getAssetPathInDrawableFolder(asset) {
    var scale = AssetSourceResolver.pickScale(asset.scales, PixelRatio.get());
    var drawbleFolder = assetPathUtils.getAndroidResourceFolderName(asset, scale);
    var fileName = assetPathUtils.getAndroidResourceIdentifier(asset);
    return drawbleFolder + '/' + fileName + '.' + asset.type;
  }

  var AssetSourceResolver = function () {
    function AssetSourceResolver(serverUrl, jsbundleUrl, asset) {
      babelHelpers.classCallCheck(this, AssetSourceResolver);
      this.serverUrl = serverUrl;
      this.jsbundleUrl = jsbundleUrl;
      this.asset = asset;
    }

    babelHelpers.createClass(AssetSourceResolver, [{
      key: "isLoadedFromServer",
      value: function isLoadedFromServer() {
        return !!this.serverUrl;
      }
    }, {
      key: "isLoadedFromFileSystem",
      value: function isLoadedFromFileSystem() {
        return !!(this.jsbundleUrl && this.jsbundleUrl.startsWith('file://'));
      }
    }, {
      key: "defaultAsset",
      value: function defaultAsset() {
        if (this.isLoadedFromServer()) {
          return this.assetServerURL();
        }

        if (Platform.OS === 'android') {
          return this.isLoadedFromFileSystem() ? this.drawableFolderInBundle() : this.resourceIdentifierWithoutScale();
        } else {
          return this.scaledAssetURLNearBundle();
        }
      }
    }, {
      key: "assetServerURL",
      value: function assetServerURL() {
        invariant(!!this.serverUrl, 'need server to load from');
        return this.fromSource(this.serverUrl + getScaledAssetPath(this.asset) + '?platform=' + Platform.OS + '&hash=' + this.asset.hash);
      }
    }, {
      key: "scaledAssetPath",
      value: function scaledAssetPath() {
        return this.fromSource(getScaledAssetPath(this.asset));
      }
    }, {
      key: "scaledAssetURLNearBundle",
      value: function scaledAssetURLNearBundle() {
        var path = this.jsbundleUrl || 'file://';
        return this.fromSource(path + getScaledAssetPath(this.asset));
      }
    }, {
      key: "resourceIdentifierWithoutScale",
      value: function resourceIdentifierWithoutScale() {
        invariant(Platform.OS === 'android', 'resource identifiers work on Android');
        return this.fromSource(assetPathUtils.getAndroidResourceIdentifier(this.asset));
      }
    }, {
      key: "drawableFolderInBundle",
      value: function drawableFolderInBundle() {
        var path = this.jsbundleUrl || 'file://';
        return this.fromSource(path + getAssetPathInDrawableFolder(this.asset));
      }
    }, {
      key: "fromSource",
      value: function fromSource(source) {
        return {
          __packager_asset: true,
          width: this.asset.width,
          height: this.asset.height,
          uri: source,
          scale: AssetSourceResolver.pickScale(this.asset.scales, PixelRatio.get())
        };
      }
    }], [{
      key: "pickScale",
      value: function pickScale(scales, deviceScale) {
        for (var i = 0; i < scales.length; i++) {
          if (scales[i] >= deviceScale) {
            return scales[i];
          }
        }

        return scales[scales.length - 1] || 1;
      }
    }]);
    return AssetSourceResolver;
  }();

  module.exports = AssetSourceResolver;
},165,[166,32,169,18],"AssetSourceResolver");