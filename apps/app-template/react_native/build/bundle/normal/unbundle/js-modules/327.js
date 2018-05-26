__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var NativeEventEmitter = _require(_dependencyMap[0], 'NativeEventEmitter');

  var NativeModules = _require(_dependencyMap[1], 'NativeModules');

  var Platform = _require(_dependencyMap[2], 'Platform');

  var invariant = _require(_dependencyMap[3], 'fbjs/lib/invariant');

  var LinkingManager = Platform.OS === 'android' ? NativeModules.IntentAndroid : NativeModules.LinkingManager;

  var Linking = function (_NativeEventEmitter) {
    babelHelpers.inherits(Linking, _NativeEventEmitter);

    function Linking() {
      babelHelpers.classCallCheck(this, Linking);
      return babelHelpers.possibleConstructorReturn(this, (Linking.__proto__ || Object.getPrototypeOf(Linking)).call(this, LinkingManager));
    }

    babelHelpers.createClass(Linking, [{
      key: "addEventListener",
      value: function addEventListener(type, handler) {
        this.addListener(type, handler);
      }
    }, {
      key: "removeEventListener",
      value: function removeEventListener(type, handler) {
        this.removeListener(type, handler);
      }
    }, {
      key: "openURL",
      value: function openURL(url) {
        this._validateURL(url);

        return LinkingManager.openURL(url);
      }
    }, {
      key: "canOpenURL",
      value: function canOpenURL(url) {
        this._validateURL(url);

        return LinkingManager.canOpenURL(url);
      }
    }, {
      key: "getInitialURL",
      value: function getInitialURL() {
        return LinkingManager.getInitialURL();
      }
    }, {
      key: "_validateURL",
      value: function _validateURL(url) {
        invariant(typeof url === 'string', 'Invalid URL: should be a string. Was: ' + url);
        invariant(url, 'Invalid URL: cannot be empty');
      }
    }]);
    return Linking;
  }(NativeEventEmitter);

  module.exports = new Linking();
},327,[82,24,32,18],"Linking");