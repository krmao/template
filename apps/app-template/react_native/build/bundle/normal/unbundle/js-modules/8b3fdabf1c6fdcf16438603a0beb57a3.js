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
},"8b3fdabf1c6fdcf16438603a0beb57a3",["522e0292cd937e7e7dc15e8d27ea9246","ce21807d4d291be64fa852393519f6c8","9493a89f5d95c3a8a47c65cfed9b5542","8940a4ad43b101ffc23e725363c70f8d"],"Linking");