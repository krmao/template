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
},377,[22],"node_modules/react-navigation/src/utils/ReactNativeFeatures.js");