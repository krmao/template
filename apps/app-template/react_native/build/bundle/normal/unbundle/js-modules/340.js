__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.MaskedViewIOS = exports.BackHandler = undefined;

  var _reactNative = _require(_dependencyMap[0], "react-native");

  var BackHandler = _reactNative.BackHandler || _reactNative.BackAndroid;
  exports.BackHandler = BackHandler;
  exports.MaskedViewIOS = _reactNative.MaskedViewIOS;
},340,[22],"node_modules/react-navigation/src/PlatformHelpers.native.js");