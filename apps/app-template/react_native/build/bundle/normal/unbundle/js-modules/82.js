__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var EventEmitter = _require(_dependencyMap[0], 'EventEmitter');

  var Platform = _require(_dependencyMap[1], 'Platform');

  var RCTDeviceEventEmitter = _require(_dependencyMap[2], 'RCTDeviceEventEmitter');

  var invariant = _require(_dependencyMap[3], 'fbjs/lib/invariant');

  var NativeEventEmitter = function (_EventEmitter) {
    babelHelpers.inherits(NativeEventEmitter, _EventEmitter);

    function NativeEventEmitter(nativeModule) {
      babelHelpers.classCallCheck(this, NativeEventEmitter);

      var _this = babelHelpers.possibleConstructorReturn(this, (NativeEventEmitter.__proto__ || Object.getPrototypeOf(NativeEventEmitter)).call(this, RCTDeviceEventEmitter.sharedSubscriber));

      if (Platform.OS === 'ios') {
        invariant(nativeModule, 'Native module cannot be null.');
        _this._nativeModule = nativeModule;
      }

      return _this;
    }

    babelHelpers.createClass(NativeEventEmitter, [{
      key: "addListener",
      value: function addListener(eventType, listener, context) {
        if (this._nativeModule != null) {
          this._nativeModule.addListener(eventType);
        }

        return babelHelpers.get(NativeEventEmitter.prototype.__proto__ || Object.getPrototypeOf(NativeEventEmitter.prototype), "addListener", this).call(this, eventType, listener, context);
      }
    }, {
      key: "removeAllListeners",
      value: function removeAllListeners(eventType) {
        invariant(eventType, 'eventType argument is required.');
        var count = this.listeners(eventType).length;

        if (this._nativeModule != null) {
          this._nativeModule.removeListeners(count);
        }

        babelHelpers.get(NativeEventEmitter.prototype.__proto__ || Object.getPrototypeOf(NativeEventEmitter.prototype), "removeAllListeners", this).call(this, eventType);
      }
    }, {
      key: "removeSubscription",
      value: function removeSubscription(subscription) {
        if (this._nativeModule != null) {
          this._nativeModule.removeListeners(1);
        }

        babelHelpers.get(NativeEventEmitter.prototype.__proto__ || Object.getPrototypeOf(NativeEventEmitter.prototype), "removeSubscription", this).call(this, subscription);
      }
    }]);
    return NativeEventEmitter;
  }(EventEmitter);

  module.exports = NativeEventEmitter;
},82,[41,32,40,18],"NativeEventEmitter");