__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var AnimatedNode = _require(_dependencyMap[0], './AnimatedNode');

  var AnimatedWithChildren = _require(_dependencyMap[1], './AnimatedWithChildren');

  var NativeAnimatedHelper = _require(_dependencyMap[2], '../NativeAnimatedHelper');

  var AnimatedTransform = function (_AnimatedWithChildren) {
    babelHelpers.inherits(AnimatedTransform, _AnimatedWithChildren);

    function AnimatedTransform(transforms) {
      babelHelpers.classCallCheck(this, AnimatedTransform);

      var _this = babelHelpers.possibleConstructorReturn(this, (AnimatedTransform.__proto__ || Object.getPrototypeOf(AnimatedTransform)).call(this));

      _this._transforms = transforms;
      return _this;
    }

    babelHelpers.createClass(AnimatedTransform, [{
      key: "__makeNative",
      value: function __makeNative() {
        babelHelpers.get(AnimatedTransform.prototype.__proto__ || Object.getPrototypeOf(AnimatedTransform.prototype), "__makeNative", this).call(this);

        this._transforms.forEach(function (transform) {
          for (var key in transform) {
            var value = transform[key];

            if (value instanceof AnimatedNode) {
              value.__makeNative();
            }
          }
        });
      }
    }, {
      key: "__getValue",
      value: function __getValue() {
        return this._transforms.map(function (transform) {
          var result = {};

          for (var key in transform) {
            var value = transform[key];

            if (value instanceof AnimatedNode) {
              result[key] = value.__getValue();
            } else {
              result[key] = value;
            }
          }

          return result;
        });
      }
    }, {
      key: "__getAnimatedValue",
      value: function __getAnimatedValue() {
        return this._transforms.map(function (transform) {
          var result = {};

          for (var key in transform) {
            var value = transform[key];

            if (value instanceof AnimatedNode) {
              result[key] = value.__getAnimatedValue();
            } else {
              result[key] = value;
            }
          }

          return result;
        });
      }
    }, {
      key: "__attach",
      value: function __attach() {
        var _this2 = this;

        this._transforms.forEach(function (transform) {
          for (var key in transform) {
            var value = transform[key];

            if (value instanceof AnimatedNode) {
              value.__addChild(_this2);
            }
          }
        });
      }
    }, {
      key: "__detach",
      value: function __detach() {
        var _this3 = this;

        this._transforms.forEach(function (transform) {
          for (var key in transform) {
            var value = transform[key];

            if (value instanceof AnimatedNode) {
              value.__removeChild(_this3);
            }
          }
        });

        babelHelpers.get(AnimatedTransform.prototype.__proto__ || Object.getPrototypeOf(AnimatedTransform.prototype), "__detach", this).call(this);
      }
    }, {
      key: "__getNativeConfig",
      value: function __getNativeConfig() {
        var transConfigs = [];

        this._transforms.forEach(function (transform) {
          for (var key in transform) {
            var value = transform[key];

            if (value instanceof AnimatedNode) {
              transConfigs.push({
                type: 'animated',
                property: key,
                nodeTag: value.__getNativeTag()
              });
            } else {
              transConfigs.push({
                type: 'static',
                property: key,
                value: value
              });
            }
          }
        });

        NativeAnimatedHelper.validateTransform(transConfigs);
        return {
          type: 'transform',
          transforms: transConfigs
        };
      }
    }]);
    return AnimatedTransform;
  }(AnimatedWithChildren);

  module.exports = AnimatedTransform;
},"83ab086bc646a982b8f23888710ebff2",["20e798e3651306aa73cc9b93f8a1fa75","432262d84c52a4f1a29f541ab0bb0c48","0efe8cba4f7d1f6a111a4698dabb344c"],"AnimatedTransform");