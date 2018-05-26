__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var AnimatedInterpolation = _require(_dependencyMap[0], './AnimatedInterpolation');

  var AnimatedNode = _require(_dependencyMap[1], './AnimatedNode');

  var AnimatedValue = _require(_dependencyMap[2], './AnimatedValue');

  var AnimatedWithChildren = _require(_dependencyMap[3], './AnimatedWithChildren');

  var AnimatedDivision = function (_AnimatedWithChildren) {
    babelHelpers.inherits(AnimatedDivision, _AnimatedWithChildren);

    function AnimatedDivision(a, b) {
      babelHelpers.classCallCheck(this, AnimatedDivision);

      var _this = babelHelpers.possibleConstructorReturn(this, (AnimatedDivision.__proto__ || Object.getPrototypeOf(AnimatedDivision)).call(this));

      _this._a = typeof a === 'number' ? new AnimatedValue(a) : a;
      _this._b = typeof b === 'number' ? new AnimatedValue(b) : b;
      return _this;
    }

    babelHelpers.createClass(AnimatedDivision, [{
      key: "__makeNative",
      value: function __makeNative() {
        this._a.__makeNative();

        this._b.__makeNative();

        babelHelpers.get(AnimatedDivision.prototype.__proto__ || Object.getPrototypeOf(AnimatedDivision.prototype), "__makeNative", this).call(this);
      }
    }, {
      key: "__getValue",
      value: function __getValue() {
        var a = this._a.__getValue();

        var b = this._b.__getValue();

        if (b === 0) {
          console.error('Detected division by zero in AnimatedDivision');
        }

        return a / b;
      }
    }, {
      key: "interpolate",
      value: function interpolate(config) {
        return new AnimatedInterpolation(this, config);
      }
    }, {
      key: "__attach",
      value: function __attach() {
        this._a.__addChild(this);

        this._b.__addChild(this);
      }
    }, {
      key: "__detach",
      value: function __detach() {
        this._a.__removeChild(this);

        this._b.__removeChild(this);

        babelHelpers.get(AnimatedDivision.prototype.__proto__ || Object.getPrototypeOf(AnimatedDivision.prototype), "__detach", this).call(this);
      }
    }, {
      key: "__getNativeConfig",
      value: function __getNativeConfig() {
        return {
          type: 'division',
          input: [this._a.__getNativeTag(), this._b.__getNativeTag()]
        };
      }
    }]);
    return AnimatedDivision;
  }(AnimatedWithChildren);

  module.exports = AnimatedDivision;
},211,[203,204,202,206],"AnimatedDivision");