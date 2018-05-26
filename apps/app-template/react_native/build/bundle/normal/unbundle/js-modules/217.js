__d(function (global, _require2, module, exports, _dependencyMap) {
  'use strict';

  var AnimatedValue = _require2(_dependencyMap[0], './AnimatedValue');

  var AnimatedNode = _require2(_dependencyMap[1], './AnimatedNode');

  var _require = _require2(_dependencyMap[2], '../NativeAnimatedHelper'),
      generateNewAnimationId = _require.generateNewAnimationId,
      shouldUseNativeDriver = _require.shouldUseNativeDriver;

  var AnimatedTracking = function (_AnimatedNode) {
    babelHelpers.inherits(AnimatedTracking, _AnimatedNode);

    function AnimatedTracking(value, parent, animationClass, animationConfig, callback) {
      babelHelpers.classCallCheck(this, AnimatedTracking);

      var _this = babelHelpers.possibleConstructorReturn(this, (AnimatedTracking.__proto__ || Object.getPrototypeOf(AnimatedTracking)).call(this));

      _this._value = value;
      _this._parent = parent;
      _this._animationClass = animationClass;
      _this._animationConfig = animationConfig;
      _this._useNativeDriver = shouldUseNativeDriver(animationConfig);
      _this._callback = callback;

      _this.__attach();

      return _this;
    }

    babelHelpers.createClass(AnimatedTracking, [{
      key: "__makeNative",
      value: function __makeNative() {
        this.__isNative = true;

        this._parent.__makeNative();

        babelHelpers.get(AnimatedTracking.prototype.__proto__ || Object.getPrototypeOf(AnimatedTracking.prototype), "__makeNative", this).call(this);

        this._value.__makeNative();
      }
    }, {
      key: "__getValue",
      value: function __getValue() {
        return this._parent.__getValue();
      }
    }, {
      key: "__attach",
      value: function __attach() {
        this._parent.__addChild(this);

        if (this._useNativeDriver) {
          this.__makeNative();
        }
      }
    }, {
      key: "__detach",
      value: function __detach() {
        this._parent.__removeChild(this);

        babelHelpers.get(AnimatedTracking.prototype.__proto__ || Object.getPrototypeOf(AnimatedTracking.prototype), "__detach", this).call(this);
      }
    }, {
      key: "update",
      value: function update() {
        this._value.animate(new this._animationClass(babelHelpers.extends({}, this._animationConfig, {
          toValue: this._animationConfig.toValue.__getValue()
        })), this._callback);
      }
    }, {
      key: "__getNativeConfig",
      value: function __getNativeConfig() {
        var animation = new this._animationClass(babelHelpers.extends({}, this._animationConfig, {
          toValue: undefined
        }));

        var animationConfig = animation.__getNativeAnimationConfig();

        return {
          type: 'tracking',
          animationId: generateNewAnimationId(),
          animationConfig: animationConfig,
          toValue: this._parent.__getNativeTag(),
          value: this._value.__getNativeTag()
        };
      }
    }]);
    return AnimatedTracking;
  }(AnimatedNode);

  module.exports = AnimatedTracking;
},217,[202,204,205],"AnimatedTracking");