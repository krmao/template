__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.MaskedViewIOS = exports.BackHandler = undefined;

  var _reactNative = _require(_dependencyMap[0], "react-native");

  var BackHandler = _reactNative.BackHandler || _reactNative.BackAndroid;
  exports.BackHandler = BackHandler;
  exports.MaskedViewIOS = _reactNative.MaskedViewIOS;
},"96119f6541fc2eb680448e5350aaae93",["cc757a791ecb3cd320f65c256a791c04"],"node_modules/react-navigation/src/PlatformHelpers.native.js");