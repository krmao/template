__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var AnimatedInterpolation = _require(_dependencyMap[0], './AnimatedInterpolation');

  var AnimatedNode = _require(_dependencyMap[1], './AnimatedNode');

  var AnimatedWithChildren = _require(_dependencyMap[2], './AnimatedWithChildren');

  var InteractionManager = _require(_dependencyMap[3], 'InteractionManager');

  var NativeAnimatedHelper = _require(_dependencyMap[4], '../NativeAnimatedHelper');

  var NativeAnimatedAPI = NativeAnimatedHelper.API;
  var _uniqueId = 1;

  function _flush(rootNode) {
    var animatedStyles = new Set();

    function findAnimatedStyles(node) {
      if (typeof node.update === 'function') {
        animatedStyles.add(node);
      } else {
        node.__getChildren().forEach(findAnimatedStyles);
      }
    }

    findAnimatedStyles(rootNode);
    animatedStyles.forEach(function (animatedStyle) {
      return animatedStyle.update();
    });
  }

  var AnimatedValue = function (_AnimatedWithChildren) {
    babelHelpers.inherits(AnimatedValue, _AnimatedWithChildren);

    function AnimatedValue(value) {
      babelHelpers.classCallCheck(this, AnimatedValue);

      var _this = babelHelpers.possibleConstructorReturn(this, (AnimatedValue.__proto__ || Object.getPrototypeOf(AnimatedValue)).call(this));

      _this._startingValue = _this._value = value;
      _this._offset = 0;
      _this._animation = null;
      _this._listeners = {};
      return _this;
    }

    babelHelpers.createClass(AnimatedValue, [{
      key: "__detach",
      value: function __detach() {
        this.stopAnimation();
        babelHelpers.get(AnimatedValue.prototype.__proto__ || Object.getPrototypeOf(AnimatedValue.prototype), "__detach", this).call(this);
      }
    }, {
      key: "__getValue",
      value: function __getValue() {
        return this._value + this._offset;
      }
    }, {
      key: "__makeNative",
      value: function __makeNative() {
        babelHelpers.get(AnimatedValue.prototype.__proto__ || Object.getPrototypeOf(AnimatedValue.prototype), "__makeNative", this).call(this);

        if (Object.keys(this._listeners).length) {
          this._startListeningToNativeValueUpdates();
        }
      }
    }, {
      key: "setValue",
      value: function setValue(value) {
        if (this._animation) {
          this._animation.stop();

          this._animation = null;
        }

        this._updateValue(value, !this.__isNative);

        if (this.__isNative) {
          NativeAnimatedAPI.setAnimatedNodeValue(this.__getNativeTag(), value);
        }
      }
    }, {
      key: "setOffset",
      value: function setOffset(offset) {
        this._offset = offset;

        if (this.__isNative) {
          NativeAnimatedAPI.setAnimatedNodeOffset(this.__getNativeTag(), offset);
        }
      }
    }, {
      key: "flattenOffset",
      value: function flattenOffset() {
        this._value += this._offset;
        this._offset = 0;

        if (this.__isNative) {
          NativeAnimatedAPI.flattenAnimatedNodeOffset(this.__getNativeTag());
        }
      }
    }, {
      key: "extractOffset",
      value: function extractOffset() {
        this._offset += this._value;
        this._value = 0;

        if (this.__isNative) {
          NativeAnimatedAPI.extractAnimatedNodeOffset(this.__getNativeTag());
        }
      }
    }, {
      key: "addListener",
      value: function addListener(callback) {
        var id = String(_uniqueId++);
        this._listeners[id] = callback;

        if (this.__isNative) {
          this._startListeningToNativeValueUpdates();
        }

        return id;
      }
    }, {
      key: "removeListener",
      value: function removeListener(id) {
        delete this._listeners[id];

        if (this.__isNative && Object.keys(this._listeners).length === 0) {
          this._stopListeningForNativeValueUpdates();
        }
      }
    }, {
      key: "removeAllListeners",
      value: function removeAllListeners() {
        this._listeners = {};

        if (this.__isNative) {
          this._stopListeningForNativeValueUpdates();
        }
      }
    }, {
      key: "_startListeningToNativeValueUpdates",
      value: function _startListeningToNativeValueUpdates() {
        var _this2 = this;

        if (this.__nativeAnimatedValueListener) {
          return;
        }

        NativeAnimatedAPI.startListeningToAnimatedNodeValue(this.__getNativeTag());
        this.__nativeAnimatedValueListener = NativeAnimatedHelper.nativeEventEmitter.addListener('onAnimatedValueUpdate', function (data) {
          if (data.tag !== _this2.__getNativeTag()) {
            return;
          }

          _this2._updateValue(data.value, false);
        });
      }
    }, {
      key: "_stopListeningForNativeValueUpdates",
      value: function _stopListeningForNativeValueUpdates() {
        if (!this.__nativeAnimatedValueListener) {
          return;
        }

        this.__nativeAnimatedValueListener.remove();

        this.__nativeAnimatedValueListener = null;
        NativeAnimatedAPI.stopListeningToAnimatedNodeValue(this.__getNativeTag());
      }
    }, {
      key: "stopAnimation",
      value: function stopAnimation(callback) {
        this.stopTracking();
        this._animation && this._animation.stop();
        this._animation = null;
        callback && callback(this.__getValue());
      }
    }, {
      key: "resetAnimation",
      value: function resetAnimation(callback) {
        this.stopAnimation(callback);
        this._value = this._startingValue;
      }
    }, {
      key: "interpolate",
      value: function interpolate(config) {
        return new AnimatedInterpolation(this, config);
      }
    }, {
      key: "animate",
      value: function animate(animation, callback) {
        var _this3 = this;

        var handle = null;

        if (animation.__isInteraction) {
          handle = InteractionManager.createInteractionHandle();
        }

        var previousAnimation = this._animation;
        this._animation && this._animation.stop();
        this._animation = animation;
        animation.start(this._value, function (value) {
          _this3._updateValue(value, true);
        }, function (result) {
          _this3._animation = null;

          if (handle !== null) {
            InteractionManager.clearInteractionHandle(handle);
          }

          callback && callback(result);
        }, previousAnimation, this);
      }
    }, {
      key: "stopTracking",
      value: function stopTracking() {
        this._tracking && this._tracking.__detach();
        this._tracking = null;
      }
    }, {
      key: "track",
      value: function track(tracking) {
        this.stopTracking();
        this._tracking = tracking;
      }
    }, {
      key: "_updateValue",
      value: function _updateValue(value, flush) {
        this._value = value;

        if (flush) {
          _flush(this);
        }

        for (var _key in this._listeners) {
          this._listeners[_key]({
            value: this.__getValue()
          });
        }
      }
    }, {
      key: "__getNativeConfig",
      value: function __getNativeConfig() {
        return {
          type: 'value',
          value: this._value,
          offset: this._offset
        };
      }
    }]);
    return AnimatedValue;
  }(AnimatedWithChildren);

  module.exports = AnimatedValue;
},"9afc2783fb30bfb04619192598012491",["6600ec16bed8e746dbd0612d3959e262","20e798e3651306aa73cc9b93f8a1fa75","432262d84c52a4f1a29f541ab0bb0c48","000e46d8eda06a212663acb7d3fd4965","0efe8cba4f7d1f6a111a4698dabb344c"],"AnimatedValue");