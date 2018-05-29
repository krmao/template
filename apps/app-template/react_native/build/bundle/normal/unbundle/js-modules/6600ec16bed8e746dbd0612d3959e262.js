__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var AnimatedNode = _require(_dependencyMap[0], './AnimatedNode');

  var AnimatedWithChildren = _require(_dependencyMap[1], './AnimatedWithChildren');

  var NativeAnimatedHelper = _require(_dependencyMap[2], '../NativeAnimatedHelper');

  var invariant = _require(_dependencyMap[3], 'fbjs/lib/invariant');

  var normalizeColor = _require(_dependencyMap[4], 'normalizeColor');

  var linear = function linear(t) {
    return t;
  };

  function createInterpolation(config) {
    if (config.outputRange && typeof config.outputRange[0] === 'string') {
      return createInterpolationFromStringOutputRange(config);
    }

    var outputRange = config.outputRange;
    checkInfiniteRange('outputRange', outputRange);
    var inputRange = config.inputRange;
    checkInfiniteRange('inputRange', inputRange);
    checkValidInputRange(inputRange);
    invariant(inputRange.length === outputRange.length, 'inputRange (' + inputRange.length + ') and outputRange (' + outputRange.length + ') must have the same length');
    var easing = config.easing || linear;
    var extrapolateLeft = 'extend';

    if (config.extrapolateLeft !== undefined) {
      extrapolateLeft = config.extrapolateLeft;
    } else if (config.extrapolate !== undefined) {
      extrapolateLeft = config.extrapolate;
    }

    var extrapolateRight = 'extend';

    if (config.extrapolateRight !== undefined) {
      extrapolateRight = config.extrapolateRight;
    } else if (config.extrapolate !== undefined) {
      extrapolateRight = config.extrapolate;
    }

    return function (input) {
      invariant(typeof input === 'number', 'Cannot interpolation an input which is not a number');
      var range = findRange(input, inputRange);
      return interpolate(input, inputRange[range], inputRange[range + 1], outputRange[range], outputRange[range + 1], easing, extrapolateLeft, extrapolateRight);
    };
  }

  function interpolate(input, inputMin, inputMax, outputMin, outputMax, easing, extrapolateLeft, extrapolateRight) {
    var result = input;

    if (result < inputMin) {
      if (extrapolateLeft === 'identity') {
        return result;
      } else if (extrapolateLeft === 'clamp') {
        result = inputMin;
      } else if (extrapolateLeft === 'extend') {}
    }

    if (result > inputMax) {
      if (extrapolateRight === 'identity') {
        return result;
      } else if (extrapolateRight === 'clamp') {
        result = inputMax;
      } else if (extrapolateRight === 'extend') {}
    }

    if (outputMin === outputMax) {
      return outputMin;
    }

    if (inputMin === inputMax) {
      if (input <= inputMin) {
        return outputMin;
      }

      return outputMax;
    }

    if (inputMin === -Infinity) {
      result = -result;
    } else if (inputMax === Infinity) {
      result = result - inputMin;
    } else {
      result = (result - inputMin) / (inputMax - inputMin);
    }

    result = easing(result);

    if (outputMin === -Infinity) {
      result = -result;
    } else if (outputMax === Infinity) {
      result = result + outputMin;
    } else {
      result = result * (outputMax - outputMin) + outputMin;
    }

    return result;
  }

  function colorToRgba(input) {
    var int32Color = normalizeColor(input);

    if (int32Color === null) {
      return input;
    }

    int32Color = int32Color || 0;
    var r = (int32Color & 0xff000000) >>> 24;
    var g = (int32Color & 0x00ff0000) >>> 16;
    var b = (int32Color & 0x0000ff00) >>> 8;
    var a = (int32Color & 0x000000ff) / 255;
    return "rgba(" + r + ", " + g + ", " + b + ", " + a + ")";
  }

  var stringShapeRegex = /[0-9\.-]+/g;

  function createInterpolationFromStringOutputRange(config) {
    var outputRange = config.outputRange;
    invariant(outputRange.length >= 2, 'Bad output range');
    outputRange = outputRange.map(colorToRgba);
    checkPattern(outputRange);
    var outputRanges = outputRange[0].match(stringShapeRegex).map(function () {
      return [];
    });
    outputRange.forEach(function (value) {
      value.match(stringShapeRegex).forEach(function (number, i) {
        outputRanges[i].push(+number);
      });
    });
    var interpolations = outputRange[0].match(stringShapeRegex).map(function (value, i) {
      return createInterpolation(babelHelpers.extends({}, config, {
        outputRange: outputRanges[i]
      }));
    });
    var shouldRound = isRgbOrRgba(outputRange[0]);
    return function (input) {
      var i = 0;
      return outputRange[0].replace(stringShapeRegex, function () {
        var val = +interpolations[i++](input);
        var rounded = shouldRound && i < 4 ? Math.round(val) : Math.round(val * 1000) / 1000;
        return String(rounded);
      });
    };
  }

  function isRgbOrRgba(range) {
    return typeof range === 'string' && range.startsWith('rgb');
  }

  function checkPattern(arr) {
    var pattern = arr[0].replace(stringShapeRegex, '');

    for (var i = 1; i < arr.length; ++i) {
      invariant(pattern === arr[i].replace(stringShapeRegex, ''), 'invalid pattern ' + arr[0] + ' and ' + arr[i]);
    }
  }

  function findRange(input, inputRange) {
    var i = void 0;

    for (i = 1; i < inputRange.length - 1; ++i) {
      if (inputRange[i] >= input) {
        break;
      }
    }

    return i - 1;
  }

  function checkValidInputRange(arr) {
    invariant(arr.length >= 2, 'inputRange must have at least 2 elements');

    for (var i = 1; i < arr.length; ++i) {
      invariant(arr[i] >= arr[i - 1], 'inputRange must be monotonically increasing ' + arr);
    }
  }

  function checkInfiniteRange(name, arr) {
    invariant(arr.length >= 2, name + ' must have at least 2 elements');
    invariant(arr.length !== 2 || arr[0] !== -Infinity || arr[1] !== Infinity, name + 'cannot be ]-infinity;+infinity[ ' + arr);
  }

  var AnimatedInterpolation = function (_AnimatedWithChildren) {
    babelHelpers.inherits(AnimatedInterpolation, _AnimatedWithChildren);

    function AnimatedInterpolation(parent, config) {
      babelHelpers.classCallCheck(this, AnimatedInterpolation);

      var _this = babelHelpers.possibleConstructorReturn(this, (AnimatedInterpolation.__proto__ || Object.getPrototypeOf(AnimatedInterpolation)).call(this));

      _this._parent = parent;
      _this._config = config;
      _this._interpolation = createInterpolation(config);
      return _this;
    }

    babelHelpers.createClass(AnimatedInterpolation, [{
      key: "__makeNative",
      value: function __makeNative() {
        this._parent.__makeNative();

        babelHelpers.get(AnimatedInterpolation.prototype.__proto__ || Object.getPrototypeOf(AnimatedInterpolation.prototype), "__makeNative", this).call(this);
      }
    }, {
      key: "__getValue",
      value: function __getValue() {
        var parentValue = this._parent.__getValue();

        invariant(typeof parentValue === 'number', 'Cannot interpolate an input which is not a number.');
        return this._interpolation(parentValue);
      }
    }, {
      key: "interpolate",
      value: function interpolate(config) {
        return new AnimatedInterpolation(this, config);
      }
    }, {
      key: "__attach",
      value: function __attach() {
        this._parent.__addChild(this);
      }
    }, {
      key: "__detach",
      value: function __detach() {
        this._parent.__removeChild(this);

        babelHelpers.get(AnimatedInterpolation.prototype.__proto__ || Object.getPrototypeOf(AnimatedInterpolation.prototype), "__detach", this).call(this);
      }
    }, {
      key: "__transformDataType",
      value: function __transformDataType(range) {
        return range.map(function (value) {
          if (typeof value !== 'string') {
            return value;
          }

          if (/deg$/.test(value)) {
            var degrees = parseFloat(value) || 0;
            var radians = degrees * Math.PI / 180.0;
            return radians;
          } else {
            return parseFloat(value) || 0;
          }
        });
      }
    }, {
      key: "__getNativeConfig",
      value: function __getNativeConfig() {
        if (__DEV__) {
          NativeAnimatedHelper.validateInterpolation(this._config);
        }

        return {
          inputRange: this._config.inputRange,
          outputRange: this.__transformDataType(this._config.outputRange),
          extrapolateLeft: this._config.extrapolateLeft || this._config.extrapolate || 'extend',
          extrapolateRight: this._config.extrapolateRight || this._config.extrapolate || 'extend',
          type: 'interpolation'
        };
      }
    }]);
    return AnimatedInterpolation;
  }(AnimatedWithChildren);

  AnimatedInterpolation.__createInterpolation = createInterpolation;
  module.exports = AnimatedInterpolation;
},"6600ec16bed8e746dbd0612d3959e262",["20e798e3651306aa73cc9b93f8a1fa75","432262d84c52a4f1a29f541ab0bb0c48","0efe8cba4f7d1f6a111a4698dabb344c","8940a4ad43b101ffc23e725363c70f8d","16e4ebd2e425393d17dd977daf378657"],"AnimatedInterpolation");