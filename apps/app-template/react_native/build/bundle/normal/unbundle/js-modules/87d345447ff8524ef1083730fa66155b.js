__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var AnimatedInterpolation = _require(_dependencyMap[0], './AnimatedInterpolation');

  var AnimatedNode = _require(_dependencyMap[1], './AnimatedNode');

  var AnimatedValue = _require(_dependencyMap[2], './AnimatedValue');

  var AnimatedWithChildren = _require(_dependencyMap[3], './AnimatedWithChildren');

  var AnimatedAddition = function (_AnimatedWithChildren) {
    babelHelpers.inherits(AnimatedAddition, _AnimatedWithChildren);

    function AnimatedAddition(a, b) {
      babelHelpers.classCallCheck(this, AnimatedAddition);

      var _this = babelHelpers.possibleConstructorReturn(this, (AnimatedAddition.__proto__ || Object.getPrototypeOf(AnimatedAddition)).call(this));

      _this._a = typeof a === 'number' ? new AnimatedValue(a) : a;
      _this._b = typeof b === 'number' ? new AnimatedValue(b) : b;
      return _this;
    }

    babelHelpers.createClass(AnimatedAddition, [{
      key: "__makeNative",
      value: function __makeNative() {
        this._a.__makeNative();

        this._b.__makeNative();

        babelHelpers.get(AnimatedAddition.prototype.__proto__ || Object.getPrototypeOf(AnimatedAddition.prototype), "__makeNative", this).call(this);
      }
    }, {
      key: "__getValue",
      value: function __getValue() {
        return this._a.__getValue() + this._b.__getValue();
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

        babelHelpers.get(AnimatedAddition.prototype.__proto__ || Object.getPrototypeOf(AnimatedAddition.prototype), "__detach", this).call(this);
      }
    }, {
      key: "__getNativeConfig",
      value: function __getNativeConfig() {
        return {
          type: 'addition',
          input: [this._a.__getNativeTag(), this._b.__getNativeTag()]
        };
      }
    }]);
    return AnimatedAddition;
  }(AnimatedWithChildren);

  module.exports = AnimatedAddition;
},"87d345447ff8524ef1083730fa66155b",["6600ec16bed8e746dbd0612d3959e262","20e798e3651306aa73cc9b93f8a1fa75","9afc2783fb30bfb04619192598012491","432262d84c52a4f1a29f541ab0bb0c48"],"AnimatedAddition");