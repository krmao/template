__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var EventEmitter = _require(_dependencyMap[0], 'EventEmitter');

  var EventEmitterWithHolding = _require(_dependencyMap[1], 'EventEmitterWithHolding');

  var EventHolder = _require(_dependencyMap[2], 'EventHolder');

  var invariant = _require(_dependencyMap[3], 'fbjs/lib/invariant');

  var keyOf = _require(_dependencyMap[4], 'fbjs/lib/keyOf');

  var TYPES_KEY = keyOf({
    __types: true
  });

  function mixInEventEmitter(cls, types) {
    invariant(types, 'Must supply set of valid event types');
    var target = cls.prototype || cls;
    invariant(!target.__eventEmitter, 'An active emitter is already mixed in');
    var ctor = cls.constructor;

    if (ctor) {
      invariant(ctor === Object || ctor === Function, 'Mix EventEmitter into a class, not an instance');
    }

    if (target.hasOwnProperty(TYPES_KEY)) {
      babelHelpers.extends(target.__types, types);
    } else if (target.__types) {
      target.__types = babelHelpers.extends({}, target.__types, types);
    } else {
      target.__types = types;
    }

    babelHelpers.extends(target, EventEmitterMixin);
  }

  var EventEmitterMixin = {
    emit: function emit(eventType, a, b, c, d, e, _) {
      return this.__getEventEmitter().emit(eventType, a, b, c, d, e, _);
    },
    emitAndHold: function emitAndHold(eventType, a, b, c, d, e, _) {
      return this.__getEventEmitter().emitAndHold(eventType, a, b, c, d, e, _);
    },
    addListener: function addListener(eventType, listener, context) {
      return this.__getEventEmitter().addListener(eventType, listener, context);
    },
    once: function once(eventType, listener, context) {
      return this.__getEventEmitter().once(eventType, listener, context);
    },
    addRetroactiveListener: function addRetroactiveListener(eventType, listener, context) {
      return this.__getEventEmitter().addRetroactiveListener(eventType, listener, context);
    },
    addListenerMap: function addListenerMap(listenerMap, context) {
      return this.__getEventEmitter().addListenerMap(listenerMap, context);
    },
    addRetroactiveListenerMap: function addRetroactiveListenerMap(listenerMap, context) {
      return this.__getEventEmitter().addListenerMap(listenerMap, context);
    },
    removeAllListeners: function removeAllListeners() {
      this.__getEventEmitter().removeAllListeners();
    },
    removeCurrentListener: function removeCurrentListener() {
      this.__getEventEmitter().removeCurrentListener();
    },
    releaseHeldEventType: function releaseHeldEventType(eventType) {
      this.__getEventEmitter().releaseHeldEventType(eventType);
    },
    __getEventEmitter: function __getEventEmitter() {
      if (!this.__eventEmitter) {
        var emitter = new EventEmitter();

        if (__DEV__) {
          var EventValidator = _require(_dependencyMap[5], 'EventValidator');

          emitter = EventValidator.addValidation(emitter, this.__types);
        }

        var holder = new EventHolder();
        this.__eventEmitter = new EventEmitterWithHolding(emitter, holder);
      }

      return this.__eventEmitter;
    }
  };
  module.exports = mixInEventEmitter;
},"0eb13c8b81942ae25b210ccb3dbd88bb",["6ab4a8e3cdc2c08a793730abd92b6eff","e06c4edc1aed80d686aacbb60aa9d103","b02be26a2f90bff94ecc1f64626d97ad","8940a4ad43b101ffc23e725363c70f8d","c3fc1c3123a4d0bcadefb6e75134743a","9d43bbcafbecc3519300a22d368d41ec"],"mixInEventEmitter");