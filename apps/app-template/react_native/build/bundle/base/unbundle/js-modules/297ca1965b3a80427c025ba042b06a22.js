__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var Subscribable = {};
  Subscribable.Mixin = {
    UNSAFE_componentWillMount: function UNSAFE_componentWillMount() {
      this._subscribableSubscriptions = [];
    },
    componentWillUnmount: function componentWillUnmount() {
      this._subscribableSubscriptions && this._subscribableSubscriptions.forEach(function (subscription) {
        return subscription.remove();
      });
      this._subscribableSubscriptions = null;
    },
    addListenerOn: function addListenerOn(eventEmitter, eventType, listener, context) {
      this._subscribableSubscriptions.push(eventEmitter.addListener(eventType, listener, context));
    }
  };
  module.exports = Subscribable;
},"297ca1965b3a80427c025ba042b06a22",[],"Subscribable");