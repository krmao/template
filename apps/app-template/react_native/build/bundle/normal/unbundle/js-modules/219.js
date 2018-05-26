__d(function (global, _require2, module, exports, _dependencyMap) {
  'use strict';

  var Animation = _require2(_dependencyMap[0], './Animation');

  var _require = _require2(_dependencyMap[1], '../NativeAnimatedHelper'),
      shouldUseNativeDriver = _require.shouldUseNativeDriver;

  var DecayAnimation = function (_Animation) {
    babelHelpers.inherits(DecayAnimation, _Animation);

    function DecayAnimation(config) {
      babelHelpers.classCallCheck(this, DecayAnimation);

      var _this = babelHelpers.possibleConstructorReturn(this, (DecayAnimation.__proto__ || Object.getPrototypeOf(DecayAnimation)).call(this));

      _this._deceleration = config.deceleration !== undefined ? config.deceleration : 0.998;
      _this._velocity = config.velocity;
      _this._useNativeDriver = shouldUseNativeDriver(config);
      _this.__isInteraction = config.isInteraction !== undefined ? config.isInteraction : true;
      _this.__iterations = config.iterations !== undefined ? config.iterations : 1;
      return _this;
    }

    babelHelpers.createClass(DecayAnimation, [{
      key: "__getNativeAnimationConfig",
      value: function __getNativeAnimationConfig() {
        return {
          type: 'decay',
          deceleration: this._deceleration,
          velocity: this._velocity,
          iterations: this.__iterations
        };
      }
    }, {
      key: "start",
      value: function start(fromValue, onUpdate, onEnd, previousAnimation, animatedValue) {
        this.__active = true;
        this._lastValue = fromValue;
        this._fromValue = fromValue;
        this._onUpdate = onUpdate;
        this.__onEnd = onEnd;
        this._startTime = Date.now();

        if (this._useNativeDriver) {
          this.__startNativeAnimation(animatedValue);
        } else {
          this._animationFrame = requestAnimationFrame(this.onUpdate.bind(this));
        }
      }
    }, {
      key: "onUpdate",
      value: function onUpdate() {
        var now = Date.now();
        var value = this._fromValue + this._velocity / (1 - this._deceleration) * (1 - Math.exp(-(1 - this._deceleration) * (now - this._startTime)));

        this._onUpdate(value);

        if (Math.abs(this._lastValue - value) < 0.1) {
          this.__debouncedOnEnd({
            finished: true
          });

          return;
        }

        this._lastValue = value;

        if (this.__active) {
          this._animationFrame = requestAnimationFrame(this.onUpdate.bind(this));
        }
      }
    }, {
      key: "stop",
      value: function stop() {
        babelHelpers.get(DecayAnimation.prototype.__proto__ || Object.getPrototypeOf(DecayAnimation.prototype), "stop", this).call(this);
        this.__active = false;
        global.cancelAnimationFrame(this._animationFrame);

        this.__debouncedOnEnd({
          finished: false
        });
      }
    }]);
    return DecayAnimation;
  }(Animation);

  module.exports = DecayAnimation;
},219,[220,205],"DecayAnimation");