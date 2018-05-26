__d(function (global, _require2, module, exports, _dependencyMap) {
  'use strict';

  var _require = _require2(_dependencyMap[0], 'NativeModules'),
      PlatformConstants = _require.PlatformConstants;

  var ReactNativeVersion = _require2(_dependencyMap[1], 'ReactNativeVersion');

  exports.checkVersions = function checkVersions() {
    if (!PlatformConstants) {
      return;
    }

    var nativeVersion = PlatformConstants.reactNativeVersion;

    if (ReactNativeVersion.version.major !== nativeVersion.major || ReactNativeVersion.version.minor !== nativeVersion.minor) {
      console.error("React Native version mismatch.\n\nJavaScript version: " + _formatVersion(ReactNativeVersion.version) + "\n" + ("Native version: " + _formatVersion(nativeVersion) + "\n\n") + 'Make sure that you have rebuilt the native code. If the problem ' + 'persists try clearing the Watchman and packager caches with ' + '`watchman watch-del-all && react-native start --reset-cache`.');
    }
  };

  function _formatVersion(version) {
    return version.major + "." + version.minor + "." + version.patch + (version.prerelease !== null ? "-" + version.prerelease : '');
  }
},64,[24,65],"ReactNativeVersionCheck");