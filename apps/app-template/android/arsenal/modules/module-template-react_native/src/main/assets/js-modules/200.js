__d(function (global, _require2, module, exports, _dependencyMap) {
  'use strict';

  var _require = _require2(_dependencyMap[0], './AnimatedEvent'),
      AnimatedEvent = _require.AnimatedEvent,
      attachNativeEvent = _require.attachNativeEvent;

  var AnimatedAddition = _require2(_dependencyMap[1], './nodes/AnimatedAddition');

  var AnimatedDiffClamp = _require2(_dependencyMap[2], './nodes/AnimatedDiffClamp');

  var AnimatedDivision = _require2(_dependencyMap[3], './nodes/AnimatedDivision');

  var AnimatedInterpolation = _require2(_dependencyMap[4], './nodes/AnimatedInterpolation');

  var AnimatedModulo = _require2(_dependencyMap[5], './nodes/AnimatedModulo');

  var AnimatedMultiplication = _require2(_dependencyMap[6], './nodes/AnimatedMultiplication');

  var AnimatedNode = _require2(_dependencyMap[7], './nodes/AnimatedNode');

  var AnimatedProps = _require2(_dependencyMap[8], './nodes/AnimatedProps');

  var AnimatedTracking = _require2(_dependencyMap[9], './nodes/AnimatedTracking');

  var AnimatedValue = _require2(_dependencyMap[10], './nodes/AnimatedValue');

  var AnimatedValueXY = _require2(_dependencyMap[11], './nodes/AnimatedValueXY');

  var DecayAnimation = _require2(_dependencyMap[12], './animations/DecayAnimation');

  var SpringAnimation = _require2(_dependencyMap[13], './animations/SpringAnimation');

  var TimingAnimation = _require2(_dependencyMap[14], './animations/TimingAnimation');

  var createAnimatedComponent = _require2(_dependencyMap[15], './createAnimatedComponent');

  var add = function add(a, b) {
    return new AnimatedAddition(a, b);
  };

  var divide = function divide(a, b) {
    return new AnimatedDivision(a, b);
  };

  var multiply = function multiply(a, b) {
    return new AnimatedMultiplication(a, b);
  };

  var modulo = function modulo(a, modulus) {
    return new AnimatedModulo(a, modulus);
  };

  var diffClamp = function diffClamp(a, min, max) {
    return new AnimatedDiffClamp(a, min, max);
  };

  var _combineCallbacks = function _combineCallbacks(callback, config) {
    if (callback && config.onComplete) {
      return function () {
        config.onComplete && config.onComplete.apply(config, arguments);
        callback && callback.apply(undefined, arguments);
      };
    } else {
      return callback || config.onComplete;
    }
  };

  var maybeVectorAnim = function maybeVectorAnim(value, config, anim) {
    if (value instanceof AnimatedValueXY) {
      var configX = babelHelpers.extends({}, config);
      var configY = babelHelpers.extends({}, config);

      for (var key in config) {
        var _config$key = config[key],
            x = _config$key.x,
            y = _config$key.y;

        if (x !== undefined && y !== undefined) {
          configX[key] = x;
          configY[key] = y;
        }
      }

      var aX = anim(value.x, configX);
      var aY = anim(value.y, configY);
      return parallel([aX, aY], {
        stopTogether: false
      });
    }

    return null;
  };

  var spring = function spring(value, config) {
    var start = function start(animatedValue, configuration, callback) {
      callback = _combineCallbacks(callback, configuration);
      var singleValue = animatedValue;
      var singleConfig = configuration;
      singleValue.stopTracking();

      if (configuration.toValue instanceof AnimatedNode) {
        singleValue.track(new AnimatedTracking(singleValue, configuration.toValue, SpringAnimation, singleConfig, callback));
      } else {
        singleValue.animate(new SpringAnimation(singleConfig), callback);
      }
    };

    return maybeVectorAnim(value, config, spring) || {
      start: function (_start) {
        function start(_x) {
          return _start.apply(this, arguments);
        }

        start.toString = function () {
          return _start.toString();
        };

        return start;
      }(function (callback) {
        start(value, config, callback);
      }),
      stop: function stop() {
        value.stopAnimation();
      },
      reset: function reset() {
        value.resetAnimation();
      },
      _startNativeLoop: function _startNativeLoop(iterations) {
        var singleConfig = babelHelpers.extends({}, config, {
          iterations: iterations
        });
        start(value, singleConfig);
      },
      _isUsingNativeDriver: function _isUsingNativeDriver() {
        return config.useNativeDriver || false;
      }
    };
  };

  var timing = function timing(value, config) {
    var start = function start(animatedValue, configuration, callback) {
      callback = _combineCallbacks(callback, configuration);
      var singleValue = animatedValue;
      var singleConfig = configuration;
      singleValue.stopTracking();

      if (configuration.toValue instanceof AnimatedNode) {
        singleValue.track(new AnimatedTracking(singleValue, configuration.toValue, TimingAnimation, singleConfig, callback));
      } else {
        singleValue.animate(new TimingAnimation(singleConfig), callback);
      }
    };

    return maybeVectorAnim(value, config, timing) || {
      start: function (_start2) {
        function start(_x2) {
          return _start2.apply(this, arguments);
        }

        start.toString = function () {
          return _start2.toString();
        };

        return start;
      }(function (callback) {
        start(value, config, callback);
      }),
      stop: function stop() {
        value.stopAnimation();
      },
      reset: function reset() {
        value.resetAnimation();
      },
      _startNativeLoop: function _startNativeLoop(iterations) {
        var singleConfig = babelHelpers.extends({}, config, {
          iterations: iterations
        });
        start(value, singleConfig);
      },
      _isUsingNativeDriver: function _isUsingNativeDriver() {
        return config.useNativeDriver || false;
      }
    };
  };

  var decay = function decay(value, config) {
    var start = function start(animatedValue, configuration, callback) {
      callback = _combineCallbacks(callback, configuration);
      var singleValue = animatedValue;
      var singleConfig = configuration;
      singleValue.stopTracking();
      singleValue.animate(new DecayAnimation(singleConfig), callback);
    };

    return maybeVectorAnim(value, config, decay) || {
      start: function (_start3) {
        function start(_x3) {
          return _start3.apply(this, arguments);
        }

        start.toString = function () {
          return _start3.toString();
        };

        return start;
      }(function (callback) {
        start(value, config, callback);
      }),
      stop: function stop() {
        value.stopAnimation();
      },
      reset: function reset() {
        value.resetAnimation();
      },
      _startNativeLoop: function _startNativeLoop(iterations) {
        var singleConfig = babelHelpers.extends({}, config, {
          iterations: iterations
        });
        start(value, singleConfig);
      },
      _isUsingNativeDriver: function _isUsingNativeDriver() {
        return config.useNativeDriver || false;
      }
    };
  };

  var sequence = function sequence(animations) {
    var current = 0;
    return {
      start: function start(callback) {
        var onComplete = function onComplete(result) {
          if (!result.finished) {
            callback && callback(result);
            return;
          }

          current++;

          if (current === animations.length) {
            callback && callback(result);
            return;
          }

          animations[current].start(onComplete);
        };

        if (animations.length === 0) {
          callback && callback({
            finished: true
          });
        } else {
          animations[current].start(onComplete);
        }
      },
      stop: function stop() {
        if (current < animations.length) {
          animations[current].stop();
        }
      },
      reset: function reset() {
        animations.forEach(function (animation, idx) {
          if (idx <= current) {
            animation.reset();
          }
        });
        current = 0;
      },
      _startNativeLoop: function _startNativeLoop() {
        throw new Error('Loops run using the native driver cannot contain Animated.sequence animations');
      },
      _isUsingNativeDriver: function _isUsingNativeDriver() {
        return false;
      }
    };
  };

  var parallel = function parallel(animations, config) {
    var doneCount = 0;
    var hasEnded = {};
    var stopTogether = !(config && config.stopTogether === false);
    var result = {
      start: function start(callback) {
        if (doneCount === animations.length) {
          callback && callback({
            finished: true
          });
          return;
        }

        animations.forEach(function (animation, idx) {
          var cb = function cb(endResult) {
            hasEnded[idx] = true;
            doneCount++;

            if (doneCount === animations.length) {
              doneCount = 0;
              callback && callback(endResult);
              return;
            }

            if (!endResult.finished && stopTogether) {
              result.stop();
            }
          };

          if (!animation) {
            cb({
              finished: true
            });
          } else {
            animation.start(cb);
          }
        });
      },
      stop: function stop() {
        animations.forEach(function (animation, idx) {
          !hasEnded[idx] && animation.stop();
          hasEnded[idx] = true;
        });
      },
      reset: function reset() {
        animations.forEach(function (animation, idx) {
          animation.reset();
          hasEnded[idx] = false;
          doneCount = 0;
        });
      },
      _startNativeLoop: function _startNativeLoop() {
        throw new Error('Loops run using the native driver cannot contain Animated.parallel animations');
      },
      _isUsingNativeDriver: function _isUsingNativeDriver() {
        return false;
      }
    };
    return result;
  };

  var delay = function delay(time) {
    return timing(new AnimatedValue(0), {
      toValue: 0,
      delay: time,
      duration: 0
    });
  };

  var stagger = function stagger(time, animations) {
    return parallel(animations.map(function (animation, i) {
      return sequence([delay(time * i), animation]);
    }));
  };

  var loop = function loop(animation) {
    var _ref = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {},
        _ref$iterations = _ref.iterations,
        iterations = _ref$iterations === undefined ? -1 : _ref$iterations;

    var isFinished = false;
    var iterationsSoFar = 0;
    return {
      start: function start(callback) {
        var restart = function restart() {
          var result = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {
            finished: true
          };

          if (isFinished || iterationsSoFar === iterations || result.finished === false) {
            callback && callback(result);
          } else {
            iterationsSoFar++;
            animation.reset();
            animation.start(restart);
          }
        };

        if (!animation || iterations === 0) {
          callback && callback({
            finished: true
          });
        } else {
          if (animation._isUsingNativeDriver()) {
            animation._startNativeLoop(iterations);
          } else {
            restart();
          }
        }
      },
      stop: function stop() {
        isFinished = true;
        animation.stop();
      },
      reset: function reset() {
        iterationsSoFar = 0;
        isFinished = false;
        animation.reset();
      },
      _startNativeLoop: function _startNativeLoop() {
        throw new Error('Loops run using the native driver cannot contain Animated.loop animations');
      },
      _isUsingNativeDriver: function _isUsingNativeDriver() {
        return animation._isUsingNativeDriver();
      }
    };
  };

  function forkEvent(event, listener) {
    if (!event) {
      return listener;
    } else if (event instanceof AnimatedEvent) {
      event.__addListener(listener);

      return event;
    } else {
      return function () {
        typeof event === 'function' && event.apply(undefined, arguments);
        listener.apply(undefined, arguments);
      };
    }
  }

  function unforkEvent(event, listener) {
    if (event && event instanceof AnimatedEvent) {
      event.__removeListener(listener);
    }
  }

  var event = function event(argMapping, config) {
    var animatedEvent = new AnimatedEvent(argMapping, config);

    if (animatedEvent.__isNative) {
      return animatedEvent;
    } else {
      return animatedEvent.__getHandler();
    }
  };

  module.exports = {
    Value: AnimatedValue,
    ValueXY: AnimatedValueXY,
    Interpolation: AnimatedInterpolation,
    Node: AnimatedNode,
    decay: decay,
    timing: timing,
    spring: spring,
    add: add,
    divide: divide,
    multiply: multiply,
    modulo: modulo,
    diffClamp: diffClamp,
    delay: delay,
    sequence: sequence,
    parallel: parallel,
    stagger: stagger,
    loop: loop,
    event: event,
    createAnimatedComponent: createAnimatedComponent,
    attachNativeEvent: attachNativeEvent,
    forkEvent: forkEvent,
    unforkEvent: unforkEvent,
    __PropsOnlyForTests: AnimatedProps
  };
},200,[201,209,210,211,203,212,213,204,214,217,202,218,219,221,223,226],"AnimatedImplementation");