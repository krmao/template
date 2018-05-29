__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var invariant = _require(_dependencyMap[0], 'fbjs/lib/invariant');

  var EventSubscriptionVendor = function () {
    function EventSubscriptionVendor() {
      babelHelpers.classCallCheck(this, EventSubscriptionVendor);
      this._subscriptionsForType = {};
      this._currentSubscription = null;
    }

    babelHelpers.createClass(EventSubscriptionVendor, [{
      key: "addSubscription",
      value: function addSubscription(eventType, subscription) {
        invariant(subscription.subscriber === this, 'The subscriber of the subscription is incorrectly set.');

        if (!this._subscriptionsForType[eventType]) {
          this._subscriptionsForType[eventType] = [];
        }

        var key = this._subscriptionsForType[eventType].length;

        this._subscriptionsForType[eventType].push(subscription);

        subscription.eventType = eventType;
        subscription.key = key;
        return subscription;
      }
    }, {
      key: "removeAllSubscriptions",
      value: function removeAllSubscriptions(eventType) {
        if (eventType === undefined) {
          this._subscriptionsForType = {};
        } else {
          delete this._subscriptionsForType[eventType];
        }
      }
    }, {
      key: "removeSubscription",
      value: function removeSubscription(subscription) {
        var eventType = subscription.eventType;
        var key = subscription.key;
        var subscriptionsForType = this._subscriptionsForType[eventType];

        if (subscriptionsForType) {
          delete subscriptionsForType[key];
        }
      }
    }, {
      key: "getSubscriptionsForType",
      value: function getSubscriptionsForType(eventType) {
        return this._subscriptionsForType[eventType];
      }
    }]);
    return EventSubscriptionVendor;
  }();

  module.exports = EventSubscriptionVendor;
},"1206b7c3420c22f521eae17efd495587",["8940a4ad43b101ffc23e725363c70f8d"],"EventSubscriptionVendor");