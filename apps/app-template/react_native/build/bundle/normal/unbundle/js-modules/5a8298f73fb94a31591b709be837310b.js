__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var AnimatedInterpolation = _require(_dependencyMap[0], './AnimatedInterpolation');

  var AnimatedNode = _require(_dependencyMap[1], './AnimatedNode');

  var AnimatedWithChildren = _require(_dependencyMap[2], './AnimatedWithChildren');

  var AnimatedModulo = function (_AnimatedWithChildren) {
    babelHelpers.inherits(AnimatedModulo, _AnimatedWithChildren);

    function AnimatedModulo(a, modulus) {
      babelHelpers.classCallCheck(this, AnimatedModulo);

      var _this = babelHelpers.possibleConstructorReturn(this, (AnimatedModulo.__proto__ || Object.getPrototypeOf(AnimatedModulo)).call(this));

      _this._a = a;
      _this._modulus = modulus;
      return _this;
    }

    babelHelpers.createClass(AnimatedModulo, [{
      key: "__makeNative",
      value: function __makeNative() {
        this._a.__makeNative();

        babelHelpers.get(AnimatedModulo.prototype.__proto__ || Object.getPrototypeOf(AnimatedModulo.prototype), "__makeNative", this).call(this);
      }
    }, {
      key: "__getValue",
      value: function __getValue() {
        return (this._a.__getValue() % this._modulus + this._modulus) % this._modulus;
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
      }
    }, {
      key: "__detach",
      value: function __detach() {
        this._a.__removeChild(this);

        babelHelpers.get(AnimatedModulo.prototype.__proto__ || Object.getPrototypeOf(AnimatedModulo.prototype), "__detach", this).call(this);
      }
    }, {
      key: "__getNativeConfig",
      value: function __getNativeConfig() {
        return {
          type: 'modulus',
          input: this._a.__getNativeTag(),
          modulus: this._modulus
        };
      }
    }]);
    return AnimatedModulo;
  }(AnimatedWithChildren);

  module.exports = AnimatedModulo;
},"5a8298f73fb94a31591b709be837310b",["6600ec16bed8e746dbd0612d3959e262","20e798e3651306aa73cc9b93f8a1fa75","432262d84c52a4f1a29f541ab0bb0c48"],"AnimatedModulo");