__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.supportsImprovedSpringAnimation = undefined;

  var _reactNative = _require(_dependencyMap[0], "react-native");

  var PlatformConstants = _reactNative.NativeModules.PlatformConstants;

  var supportsImprovedSpringAnimation = exports.supportsImprovedSpringAnimation = function supportsImprovedSpringAnimation() {
    if (PlatformConstants && PlatformConstants.reactNativeVersion) {
      var _PlatformConstants$re = PlatformConstants.reactNativeVersion,
          major = _PlatformConstants$re.major,
          minor = _PlatformConstants$re.minor;
      return minor >= 50 || major === 0 && minor === 0;
    }

    return false;
  };
},"b886806cc3f4ad8d13158780908d3098",["cc757a791ecb3cd320f65c256a791c04"],"node_modules/react-navigation/src/utils/ReactNativeFeatures.js");