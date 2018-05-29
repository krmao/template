__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var AnimatedInterpolation = _require(_dependencyMap[0], './AnimatedInterpolation');

  var AnimatedNode = _require(_dependencyMap[1], './AnimatedNode');

  var AnimatedWithChildren = _require(_dependencyMap[2], './AnimatedWithChildren');

  var AnimatedDiffClamp = function (_AnimatedWithChildren) {
    babelHelpers.inherits(AnimatedDiffClamp, _AnimatedWithChildren);

    function AnimatedDiffClamp(a, min, max) {
      babelHelpers.classCallCheck(this, AnimatedDiffClamp);

      var _this = babelHelpers.possibleConstructorReturn(this, (AnimatedDiffClamp.__proto__ || Object.getPrototypeOf(AnimatedDiffClamp)).call(this));

      _this._a = a;
      _this._min = min;
      _this._max = max;
      _this._value = _this._lastValue = _this._a.__getValue();
      return _this;
    }

    babelHelpers.createClass(AnimatedDiffClamp, [{
      key: "__makeNative",
      value: function __makeNative() {
        this._a.__makeNative();

        babelHelpers.get(AnimatedDiffClamp.prototype.__proto__ || Object.getPrototypeOf(AnimatedDiffClamp.prototype), "__makeNative", this).call(this);
      }
    }, {
      key: "interpolate",
      value: function interpolate(config) {
        return new AnimatedInterpolation(this, config);
      }
    }, {
      key: "__getValue",
      value: function __getValue() {
        var value = this._a.__getValue();

        var diff = value - this._lastValue;
        this._lastValue = value;
        this._value = Math.min(Math.max(this._value + diff, this._min), this._max);
        return this._value;
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

        babelHelpers.get(AnimatedDiffClamp.prototype.__proto__ || Object.getPrototypeOf(AnimatedDiffClamp.prototype), "__detach", this).call(this);
      }
    }, {
      key: "__getNativeConfig",
      value: function __getNativeConfig() {
        return {
          type: 'diffclamp',
          input: this._a.__getNativeTag(),
          min: this._min,
          max: this._max
        };
      }
    }]);
    return AnimatedDiffClamp;
  }(AnimatedWithChildren);

  module.exports = AnimatedDiffClamp;
},"7d1a23da15861380f540aa93491d6349",["6600ec16bed8e746dbd0612d3959e262","20e798e3651306aa73cc9b93f8a1fa75","432262d84c52a4f1a29f541ab0bb0c48"],"AnimatedDiffClamp");