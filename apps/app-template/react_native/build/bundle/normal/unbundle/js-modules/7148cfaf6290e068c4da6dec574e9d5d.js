__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var EmitterSubscription = _require(_dependencyMap[0], 'EmitterSubscription');

  var EventEmitter = _require(_dependencyMap[1], 'EventEmitter');

  var invariant = _require(_dependencyMap[2], 'fbjs/lib/invariant');

  var MissingNativeEventEmitterShim = function (_EventEmitter) {
    babelHelpers.inherits(MissingNativeEventEmitterShim, _EventEmitter);

    function MissingNativeEventEmitterShim(nativeModuleName, nativeEventEmitterName) {
      babelHelpers.classCallCheck(this, MissingNativeEventEmitterShim);

      var _this = babelHelpers.possibleConstructorReturn(this, (MissingNativeEventEmitterShim.__proto__ || Object.getPrototypeOf(MissingNativeEventEmitterShim)).call(this, null));

      _this.isAvailable = false;
      _this._nativeModuleName = nativeModuleName;
      _this._nativeEventEmitterName = nativeEventEmitterName;
      return _this;
    }

    babelHelpers.createClass(MissingNativeEventEmitterShim, [{
      key: "throwMissingNativeModule",
      value: function throwMissingNativeModule() {
        invariant(false, "Cannot use '" + this._nativeEventEmitterName + "' module when " + ("native '" + this._nativeModuleName + "' is not included in the build. ") + ("Either include it, or check '" + this._nativeEventEmitterName + "'.isAvailable ") + 'before calling any methods.');
      }
    }, {
      key: "addListener",
      value: function addListener(eventType, listener, context) {
        this.throwMissingNativeModule();
      }
    }, {
      key: "removeAllListeners",
      value: function removeAllListeners(eventType) {
        this.throwMissingNativeModule();
      }
    }, {
      key: "removeSubscription",
      value: function removeSubscription(subscription) {
        this.throwMissingNativeModule();
      }
    }]);
    return MissingNativeEventEmitterShim;
  }(EventEmitter);

  module.exports = MissingNativeEventEmitterShim;
},"7148cfaf6290e068c4da6dec574e9d5d",["bfead221247b162887cda7d2b4ca630b","6ab4a8e3cdc2c08a793730abd92b6eff","8940a4ad43b101ffc23e725363c70f8d"],"MissingNativeEventEmitterShim");