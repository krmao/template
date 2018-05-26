__d(function (global, _require2, module, exports, _dependencyMap) {
  'use strict';

  var AnimatedValue = _require2(_dependencyMap[0], '../nodes/AnimatedValue');

  var AnimatedValueXY = _require2(_dependencyMap[1], '../nodes/AnimatedValueXY');

  var Animation = _require2(_dependencyMap[2], './Animation');

  var SpringConfig = _require2(_dependencyMap[3], '../SpringConfig');

  var invariant = _require2(_dependencyMap[4], 'fbjs/lib/invariant');

  var _require = _require2(_dependencyMap[5], '../NativeAnimatedHelper'),
      shouldUseNativeDriver = _require.shouldUseNativeDriver;

  function withDefault(value, defaultValue) {
    if (value === undefined || value === null) {
      return defaultValue;
    }

    return value;
  }

  var SpringAnimation = function (_Animation) {
    babelHelpers.inherits(SpringAnimation, _Animation);

    function SpringAnimation(config) {
      babelHelpers.classCallCheck(this, SpringAnimation);

      var _this = babelHelpers.possibleConstructorReturn(this, (SpringAnimation.__proto__ || Object.getPrototypeOf(SpringAnimation)).call(this));

      _this._overshootClamping = withDefault(config.overshootClamping, false);
      _this._restDisplacementThreshold = withDefault(config.restDisplacementThreshold, 0.001);
      _this._restSpeedThreshold = withDefault(config.restSpeedThreshold, 0.001);
      _this._initialVelocity = withDefault(config.velocity, 0);
      _this._lastVelocity = withDefault(config.velocity, 0);
      _this._toValue = config.toValue;
      _this._delay = withDefault(config.delay, 0);
      _this._useNativeDriver = shouldUseNativeDriver(config);
      _this.__isInteraction = config.isInteraction !== undefined ? config.isInteraction : true;
      _this.__iterations = config.iterations !== undefined ? config.iterations : 1;

      if (config.stiffness !== undefined || config.damping !== undefined || config.mass !== undefined) {
        invariant(config.bounciness === undefined && config.speed === undefined && config.tension === undefined && config.friction === undefined, 'You can define one of bounciness/speed, tension/friction, or stiffness/damping/mass, but not more than one');
        _this._stiffness = withDefault(config.stiffness, 100);
        _this._damping = withDefault(config.damping, 10);
        _this._mass = withDefault(config.mass, 1);
      } else if (config.bounciness !== undefined || config.speed !== undefined) {
        invariant(config.tension === undefined && config.friction === undefined && config.stiffness === undefined && config.damping === undefined && config.mass === undefined, 'You can define one of bounciness/speed, tension/friction, or stiffness/damping/mass, but not more than one');
        var springConfig = SpringConfig.fromBouncinessAndSpeed(withDefault(config.bounciness, 8), withDefault(config.speed, 12));
        _this._stiffness = springConfig.stiffness;
        _this._damping = springConfig.damping;
        _this._mass = 1;
      } else {
        var _springConfig = SpringConfig.fromOrigamiTensionAndFriction(withDefault(config.tension, 40), withDefault(config.friction, 7));

        _this._stiffness = _springConfig.stiffness;
        _this._damping = _springConfig.damping;
        _this._mass = 1;
      }

      invariant(_this._stiffness > 0, 'Stiffness value must be greater than 0');
      invariant(_this._damping > 0, 'Damping value must be greater than 0');
      invariant(_this._mass > 0, 'Mass value must be greater than 0');
      return _this;
    }

    babelHelpers.createClass(SpringAnimation, [{
      key: "__getNativeAnimationConfig",
      value: function __getNativeAnimationConfig() {
        return {
          type: 'spring',
          overshootClamping: this._overshootClamping,
          restDisplacementThreshold: this._restDisplacementThreshold,
          restSpeedThreshold: this._restSpeedThreshold,
          stiffness: this._stiffness,
          damping: this._damping,
          mass: this._mass,
          initialVelocity: withDefault(this._initialVelocity, this._lastVelocity),
          toValue: this._toValue,
          iterations: this.__iterations
        };
      }
    }, {
      key: "start",
      value: function start(fromValue, onUpdate, onEnd, previousAnimation, animatedValue) {
        var _this2 = this;

        this.__active = true;
        this._startPosition = fromValue;
        this._lastPosition = this._startPosition;
        this._onUpdate = onUpdate;
        this.__onEnd = onEnd;
        this._lastTime = Date.now();
        this._frameTime = 0.0;

        if (previousAnimation instanceof SpringAnimation) {
          var internalState = previousAnimation.getInternalState();
          this._lastPosition = internalState.lastPosition;
          this._lastVelocity = internalState.lastVelocity;
          this._initialVelocity = this._lastVelocity;
          this._lastTime = internalState.lastTime;
        }

        var start = function start() {
          if (_this2._useNativeDriver) {
            _this2.__startNativeAnimation(animatedValue);
          } else {
            _this2.onUpdate();
          }
        };

        if (this._delay) {
          this._timeout = setTimeout(start, this._delay);
        } else {
          start();
        }
      }
    }, {
      key: "getInternalState",
      value: function getInternalState() {
        return {
          lastPosition: this._lastPosition,
          lastVelocity: this._lastVelocity,
          lastTime: this._lastTime
        };
      }
    }, {
      key: "onUpdate",
      value: function onUpdate() {
        var MAX_STEPS = 64;
        var now = Date.now();

        if (now > this._lastTime + MAX_STEPS) {
          now = this._lastTime + MAX_STEPS;
        }

        var deltaTime = (now - this._lastTime) / 1000;
        this._frameTime += deltaTime;
        var c = this._damping;
        var m = this._mass;
        var k = this._stiffness;
        var v0 = -this._initialVelocity;
        var zeta = c / (2 * Math.sqrt(k * m));
        var omega0 = Math.sqrt(k / m);
        var omega1 = omega0 * Math.sqrt(1.0 - zeta * zeta);
        var x0 = this._toValue - this._startPosition;
        var position = 0.0;
        var velocity = 0.0;
        var t = this._frameTime;

        if (zeta < 1) {
          var envelope = Math.exp(-zeta * omega0 * t);
          position = this._toValue - envelope * ((v0 + zeta * omega0 * x0) / omega1 * Math.sin(omega1 * t) + x0 * Math.cos(omega1 * t));
          velocity = zeta * omega0 * envelope * (Math.sin(omega1 * t) * (v0 + zeta * omega0 * x0) / omega1 + x0 * Math.cos(omega1 * t)) - envelope * (Math.cos(omega1 * t) * (v0 + zeta * omega0 * x0) - omega1 * x0 * Math.sin(omega1 * t));
        } else {
          var _envelope = Math.exp(-omega0 * t);

          position = this._toValue - _envelope * (x0 + (v0 + omega0 * x0) * t);
          velocity = _envelope * (v0 * (t * omega0 - 1) + t * x0 * (omega0 * omega0));
        }

        this._lastTime = now;
        this._lastPosition = position;
        this._lastVelocity = velocity;

        this._onUpdate(position);

        if (!this.__active) {
          return;
        }

        var isOvershooting = false;

        if (this._overshootClamping && this._stiffness !== 0) {
          if (this._startPosition < this._toValue) {
            isOvershooting = position > this._toValue;
          } else {
            isOvershooting = position < this._toValue;
          }
        }

        var isVelocity = Math.abs(velocity) <= this._restSpeedThreshold;

        var isDisplacement = true;

        if (this._stiffness !== 0) {
          isDisplacement = Math.abs(this._toValue - position) <= this._restDisplacementThreshold;
        }

        if (isOvershooting || isVelocity && isDisplacement) {
          if (this._stiffness !== 0) {
            this._lastPosition = this._toValue;
            this._lastVelocity = 0;

            this._onUpdate(this._toValue);
          }

          this.__debouncedOnEnd({
            finished: true
          });

          return;
        }

        this._animationFrame = requestAnimationFrame(this.onUpdate.bind(this));
      }
    }, {
      key: "stop",
      value: function stop() {
        babelHelpers.get(SpringAnimation.prototype.__proto__ || Object.getPrototypeOf(SpringAnimation.prototype), "stop", this).call(this);
        this.__active = false;
        clearTimeout(this._timeout);
        global.cancelAnimationFrame(this._animationFrame);

        this.__debouncedOnEnd({
          finished: false
        });
      }
    }]);
    return SpringAnimation;
  }(Animation);

  module.exports = SpringAnimation;
},221,[202,218,220,222,18,205],"SpringAnimation");