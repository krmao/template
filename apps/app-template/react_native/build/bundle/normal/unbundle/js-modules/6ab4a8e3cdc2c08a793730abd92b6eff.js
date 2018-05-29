__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var EmitterSubscription = _require(_dependencyMap[0], 'EmitterSubscription');

  var EventSubscriptionVendor = _require(_dependencyMap[1], 'EventSubscriptionVendor');

  var emptyFunction = _require(_dependencyMap[2], 'fbjs/lib/emptyFunction');

  var invariant = _require(_dependencyMap[3], 'fbjs/lib/invariant');

  var EventEmitter = function () {
    function EventEmitter(subscriber) {
      babelHelpers.classCallCheck(this, EventEmitter);
      this._subscriber = subscriber || new EventSubscriptionVendor();
    }

    babelHelpers.createClass(EventEmitter, [{
      key: "addListener",
      value: function addListener(eventType, listener, context) {
        return this._subscriber.addSubscription(eventType, new EmitterSubscription(this, this._subscriber, listener, context));
      }
    }, {
      key: "once",
      value: function once(eventType, listener, context) {
        var _this = this;

        return this.addListener(eventType, function () {
          for (var _len = arguments.length, args = Array(_len), _key = 0; _key < _len; _key++) {
            args[_key] = arguments[_key];
          }

          _this.removeCurrentListener();

          listener.apply(context, args);
        });
      }
    }, {
      key: "removeAllListeners",
      value: function removeAllListeners(eventType) {
        this._subscriber.removeAllSubscriptions(eventType);
      }
    }, {
      key: "removeCurrentListener",
      value: function removeCurrentListener() {
        invariant(!!this._currentSubscription, 'Not in an emitting cycle; there is no current subscription');
        this.removeSubscription(this._currentSubscription);
      }
    }, {
      key: "removeSubscription",
      value: function removeSubscription(subscription) {
        invariant(subscription.emitter === this, 'Subscription does not belong to this emitter.');

        this._subscriber.removeSubscription(subscription);
      }
    }, {
      key: "listeners",
      value: function listeners(eventType) {
        var subscriptions = this._subscriber.getSubscriptionsForType(eventType);

        return subscriptions ? subscriptions.filter(emptyFunction.thatReturnsTrue).map(function (subscription) {
          return subscription.listener;
        }) : [];
      }
    }, {
      key: "emit",
      value: function emit(eventType) {
        var subscriptions = this._subscriber.getSubscriptionsForType(eventType);

        if (subscriptions) {
          for (var i = 0, l = subscriptions.length; i < l; i++) {
            var subscription = subscriptions[i];

            if (subscription) {
              this._currentSubscription = subscription;
              subscription.listener.apply(subscription.context, Array.prototype.slice.call(arguments, 1));
            }
          }

          this._currentSubscription = null;
        }
      }
    }, {
      key: "removeListener",
      value: function removeListener(eventType, listener) {
        var subscriptions = this._subscriber.getSubscriptionsForType(eventType);

        if (subscriptions) {
          for (var i = 0, l = subscriptions.length; i < l; i++) {
            var subscription = subscriptions[i];

            if (subscription && subscription.listener === listener) {
              subscription.remove();
            }
          }
        }
      }
    }]);
    return EventEmitter;
  }();

  module.exports = EventEmitter;
},"6ab4a8e3cdc2c08a793730abd92b6eff",["bfead221247b162887cda7d2b4ca630b","1206b7c3420c22f521eae17efd495587","7be5aa3f60ced36f3bf5972d0a12f299","8940a4ad43b101ffc23e725363c70f8d"],"EventEmitter");