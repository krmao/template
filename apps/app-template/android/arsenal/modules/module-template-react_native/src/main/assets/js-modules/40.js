__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var EventEmitter = _require(_dependencyMap[0], 'EventEmitter');

  var EventSubscriptionVendor = _require(_dependencyMap[1], 'EventSubscriptionVendor');

  function checkNativeEventModule(eventType) {
    if (eventType) {
      if (eventType.lastIndexOf('statusBar', 0) === 0) {
        throw new Error('`' + eventType + '` event should be registered via the StatusBarIOS module');
      }

      if (eventType.lastIndexOf('keyboard', 0) === 0) {
        throw new Error('`' + eventType + '` event should be registered via the Keyboard module');
      }

      if (eventType === 'appStateDidChange' || eventType === 'memoryWarning') {
        throw new Error('`' + eventType + '` event should be registered via the AppState module');
      }
    }
  }

  var RCTDeviceEventEmitter = function (_EventEmitter) {
    babelHelpers.inherits(RCTDeviceEventEmitter, _EventEmitter);

    function RCTDeviceEventEmitter() {
      babelHelpers.classCallCheck(this, RCTDeviceEventEmitter);
      var sharedSubscriber = new EventSubscriptionVendor();

      var _this = babelHelpers.possibleConstructorReturn(this, (RCTDeviceEventEmitter.__proto__ || Object.getPrototypeOf(RCTDeviceEventEmitter)).call(this, sharedSubscriber));

      _this.sharedSubscriber = sharedSubscriber;
      return _this;
    }

    babelHelpers.createClass(RCTDeviceEventEmitter, [{
      key: "addListener",
      value: function addListener(eventType, listener, context) {
        if (__DEV__) {
          checkNativeEventModule(eventType);
        }

        return babelHelpers.get(RCTDeviceEventEmitter.prototype.__proto__ || Object.getPrototypeOf(RCTDeviceEventEmitter.prototype), "addListener", this).call(this, eventType, listener, context);
      }
    }, {
      key: "removeAllListeners",
      value: function removeAllListeners(eventType) {
        if (__DEV__) {
          checkNativeEventModule(eventType);
        }

        babelHelpers.get(RCTDeviceEventEmitter.prototype.__proto__ || Object.getPrototypeOf(RCTDeviceEventEmitter.prototype), "removeAllListeners", this).call(this, eventType);
      }
    }, {
      key: "removeSubscription",
      value: function removeSubscription(subscription) {
        if (subscription.emitter !== this) {
          subscription.emitter.removeSubscription(subscription);
        } else {
          babelHelpers.get(RCTDeviceEventEmitter.prototype.__proto__ || Object.getPrototypeOf(RCTDeviceEventEmitter.prototype), "removeSubscription", this).call(this, subscription);
        }
      }
    }]);
    return RCTDeviceEventEmitter;
  }(EventEmitter);

  module.exports = new RCTDeviceEventEmitter();
},40,[41,44],"RCTDeviceEventEmitter");