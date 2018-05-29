__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var AnimatedInterpolation = _require(_dependencyMap[0], './AnimatedInterpolation');

  var AnimatedNode = _require(_dependencyMap[1], './AnimatedNode');

  var AnimatedValue = _require(_dependencyMap[2], './AnimatedValue');

  var AnimatedWithChildren = _require(_dependencyMap[3], './AnimatedWithChildren');

  var AnimatedMultiplication = function (_AnimatedWithChildren) {
    babelHelpers.inherits(AnimatedMultiplication, _AnimatedWithChildren);

    function AnimatedMultiplication(a, b) {
      babelHelpers.classCallCheck(this, AnimatedMultiplication);

      var _this = babelHelpers.possibleConstructorReturn(this, (AnimatedMultiplication.__proto__ || Object.getPrototypeOf(AnimatedMultiplication)).call(this));

      _this._a = typeof a === 'number' ? new AnimatedValue(a) : a;
      _this._b = typeof b === 'number' ? new AnimatedValue(b) : b;
      return _this;
    }

    babelHelpers.createClass(AnimatedMultiplication, [{
      key: "__makeNative",
      value: function __makeNative() {
        this._a.__makeNative();

        this._b.__makeNative();

        babelHelpers.get(AnimatedMultiplication.prototype.__proto__ || Object.getPrototypeOf(AnimatedMultiplication.prototype), "__makeNative", this).call(this);
      }
    }, {
      key: "__getValue",
      value: function __getValue() {
        return this._a.__getValue() * this._b.__getValue();
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

        babelHelpers.get(AnimatedMultiplication.prototype.__proto__ || Object.getPrototypeOf(AnimatedMultiplication.prototype), "__detach", this).call(this);
      }
    }, {
      key: "__getNativeConfig",
      value: function __getNativeConfig() {
        return {
          type: 'multiplication',
          input: [this._a.__getNativeTag(), this._b.__getNativeTag()]
        };
      }
    }]);
    return AnimatedMultiplication;
  }(AnimatedWithChildren);

  module.exports = AnimatedMultiplication;
},"704f78956d56345ec4b7195fc460b587",["6600ec16bed8e746dbd0612d3959e262","20e798e3651306aa73cc9b93f8a1fa75","9afc2783fb30bfb04619192598012491","432262d84c52a4f1a29f541ab0bb0c48"],"AnimatedMultiplication");