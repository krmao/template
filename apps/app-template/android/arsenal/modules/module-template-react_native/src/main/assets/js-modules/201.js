__d(function (global, _require2, module, exports, _dependencyMap) {
  'use strict';

  var AnimatedValue = _require2(_dependencyMap[0], './nodes/AnimatedValue');

  var NativeAnimatedHelper = _require2(_dependencyMap[1], './NativeAnimatedHelper');

  var ReactNative = _require2(_dependencyMap[2], 'ReactNative');

  var invariant = _require2(_dependencyMap[3], 'fbjs/lib/invariant');

  var _require = _require2(_dependencyMap[1], './NativeAnimatedHelper'),
      shouldUseNativeDriver = _require.shouldUseNativeDriver;

  function attachNativeEvent(viewRef, eventName, argMapping) {
    var eventMappings = [];

    var traverse = function traverse(value, path) {
      if (value instanceof AnimatedValue) {
        value.__makeNative();

        eventMappings.push({
          nativeEventPath: path,
          animatedValueTag: value.__getNativeTag()
        });
      } else if (typeof value === 'object') {
        for (var _key in value) {
          traverse(value[_key], path.concat(_key));
        }
      }
    };

    invariant(argMapping[0] && argMapping[0].nativeEvent, 'Native driven events only support animated values contained inside `nativeEvent`.');
    traverse(argMapping[0].nativeEvent, []);
    var viewTag = ReactNative.findNodeHandle(viewRef);
    eventMappings.forEach(function (mapping) {
      NativeAnimatedHelper.API.addAnimatedEventToView(viewTag, eventName, mapping);
    });
    return {
      detach: function detach() {
        eventMappings.forEach(function (mapping) {
          NativeAnimatedHelper.API.removeAnimatedEventFromView(viewTag, eventName, mapping.animatedValueTag);
        });
      }
    };
  }

  var AnimatedEvent = function () {
    function AnimatedEvent(argMapping) {
      var config = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
      babelHelpers.classCallCheck(this, AnimatedEvent);
      this._listeners = [];
      this._argMapping = argMapping;

      if (config.listener) {
        this.__addListener(config.listener);
      }

      this._callListeners = this._callListeners.bind(this);
      this._attachedEvent = null;
      this.__isNative = shouldUseNativeDriver(config);

      if (__DEV__) {
        this._validateMapping();
      }
    }

    babelHelpers.createClass(AnimatedEvent, [{
      key: "__addListener",
      value: function __addListener(callback) {
        this._listeners.push(callback);
      }
    }, {
      key: "__removeListener",
      value: function __removeListener(callback) {
        this._listeners = this._listeners.filter(function (listener) {
          return listener !== callback;
        });
      }
    }, {
      key: "__attach",
      value: function __attach(viewRef, eventName) {
        invariant(this.__isNative, 'Only native driven events need to be attached.');
        this._attachedEvent = attachNativeEvent(viewRef, eventName, this._argMapping);
      }
    }, {
      key: "__detach",
      value: function __detach(viewTag, eventName) {
        invariant(this.__isNative, 'Only native driven events need to be detached.');
        this._attachedEvent && this._attachedEvent.detach();
      }
    }, {
      key: "__getHandler",
      value: function __getHandler() {
        var _this = this;

        if (this.__isNative) {
          return this._callListeners;
        }

        return function () {
          for (var _len = arguments.length, args = Array(_len), _key2 = 0; _key2 < _len; _key2++) {
            args[_key2] = arguments[_key2];
          }

          var traverse = function traverse(recMapping, recEvt, key) {
            if (typeof recEvt === 'number' && recMapping instanceof AnimatedValue) {
              recMapping.setValue(recEvt);
            } else if (typeof recMapping === 'object') {
              for (var mappingKey in recMapping) {
                traverse(recMapping[mappingKey], recEvt[mappingKey], mappingKey);
              }
            }
          };

          if (!_this.__isNative) {
            _this._argMapping.forEach(function (mapping, idx) {
              traverse(mapping, args[idx], 'arg' + idx);
            });
          }

          _this._callListeners.apply(_this, babelHelpers.toConsumableArray(args));
        };
      }
    }, {
      key: "_callListeners",
      value: function _callListeners() {
        for (var _len2 = arguments.length, args = Array(_len2), _key3 = 0; _key3 < _len2; _key3++) {
          args[_key3] = arguments[_key3];
        }

        this._listeners.forEach(function (listener) {
          return listener.apply(undefined, args);
        });
      }
    }, {
      key: "_validateMapping",
      value: function _validateMapping() {
        var traverse = function traverse(recMapping, recEvt, key) {
          if (typeof recEvt === 'number') {
            invariant(recMapping instanceof AnimatedValue, 'Bad mapping of type ' + typeof recMapping + ' for key ' + key + ', event value must map to AnimatedValue');
            return;
          }

          invariant(typeof recMapping === 'object', 'Bad mapping of type ' + typeof recMapping + ' for key ' + key);
          invariant(typeof recEvt === 'object', 'Bad event of type ' + typeof recEvt + ' for key ' + key);

          for (var mappingKey in recMapping) {
            traverse(recMapping[mappingKey], recEvt[mappingKey], mappingKey);
          }
        };
      }
    }]);
    return AnimatedEvent;
  }();

  module.exports = {
    AnimatedEvent: AnimatedEvent,
    attachNativeEvent: attachNativeEvent
  };
},201,[202,205,49,18],"AnimatedEvent");