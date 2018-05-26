__d(function (global, _require2, module, exports, _dependencyMap) {
  'use strict';

  var AnimatedValue = _require2(_dependencyMap[0], '../nodes/AnimatedValue');

  var AnimatedValueXY = _require2(_dependencyMap[1], '../nodes/AnimatedValueXY');

  var Animation = _require2(_dependencyMap[2], './Animation');

  var _require = _require2(_dependencyMap[3], '../NativeAnimatedHelper'),
      shouldUseNativeDriver = _require.shouldUseNativeDriver;

  var _easeInOut = void 0;

  function easeInOut() {
    if (!_easeInOut) {
      var Easing = _require2(_dependencyMap[4], 'Easing');

      _easeInOut = Easing.inOut(Easing.ease);
    }

    return _easeInOut;
  }

  var TimingAnimation = function (_Animation) {
    babelHelpers.inherits(TimingAnimation, _Animation);

    function TimingAnimation(config) {
      babelHelpers.classCallCheck(this, TimingAnimation);

      var _this = babelHelpers.possibleConstructorReturn(this, (TimingAnimation.__proto__ || Object.getPrototypeOf(TimingAnimation)).call(this));

      _this._toValue = config.toValue;
      _this._easing = config.easing !== undefined ? config.easing : easeInOut();
      _this._duration = config.duration !== undefined ? config.duration : 500;
      _this._delay = config.delay !== undefined ? config.delay : 0;
      _this.__iterations = config.iterations !== undefined ? config.iterations : 1;
      _this.__isInteraction = config.isInteraction !== undefined ? config.isInteraction : true;
      _this._useNativeDriver = shouldUseNativeDriver(config);
      return _this;
    }

    babelHelpers.createClass(TimingAnimation, [{
      key: "__getNativeAnimationConfig",
      value: function __getNativeAnimationConfig() {
        var frameDuration = 1000.0 / 60.0;
        var frames = [];

        for (var dt = 0.0; dt < this._duration; dt += frameDuration) {
          frames.push(this._easing(dt / this._duration));
        }

        frames.push(this._easing(1));
        return {
          type: 'frames',
          frames: frames,
          toValue: this._toValue,
          iterations: this.__iterations
        };
      }
    }, {
      key: "start",
      value: function start(fromValue, onUpdate, onEnd, previousAnimation, animatedValue) {
        var _this2 = this;

        this.__active = true;
        this._fromValue = fromValue;
        this._onUpdate = onUpdate;
        this.__onEnd = onEnd;

        var start = function start() {
          if (_this2._duration === 0 && !_this2._useNativeDriver) {
            _this2._onUpdate(_this2._toValue);

            _this2.__debouncedOnEnd({
              finished: true
            });
          } else {
            _this2._startTime = Date.now();

            if (_this2._useNativeDriver) {
              _this2.__startNativeAnimation(animatedValue);
            } else {
              _this2._animationFrame = requestAnimationFrame(_this2.onUpdate.bind(_this2));
            }
          }
        };

        if (this._delay) {
          this._timeout = setTimeout(start, this._delay);
        } else {
          start();
        }
      }
    }, {
      key: "onUpdate",
      value: function onUpdate() {
        var now = Date.now();

        if (now >= this._startTime + this._duration) {
          if (this._duration === 0) {
            this._onUpdate(this._toValue);
          } else {
            this._onUpdate(this._fromValue + this._easing(1) * (this._toValue - this._fromValue));
          }

          this.__debouncedOnEnd({
            finished: true
          });

          return;
        }

        this._onUpdate(this._fromValue + this._easing((now - this._startTime) / this._duration) * (this._toValue - this._fromValue));

        if (this.__active) {
          this._animationFrame = requestAnimationFrame(this.onUpdate.bind(this));
        }
      }
    }, {
      key: "stop",
      value: function stop() {
        babelHelpers.get(TimingAnimation.prototype.__proto__ || Object.getPrototypeOf(TimingAnimation.prototype), "stop", this).call(this);
        this.__active = false;
        clearTimeout(this._timeout);
        global.cancelAnimationFrame(this._animationFrame);

        this.__debouncedOnEnd({
          finished: false
        });
      }
    }]);
    return TimingAnimation;
  }(Animation);

  module.exports = TimingAnimation;
},223,[202,218,220,205,224],"TimingAnimation");