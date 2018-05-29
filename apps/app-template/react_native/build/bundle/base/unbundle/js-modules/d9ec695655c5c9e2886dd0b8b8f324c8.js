__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var AnimatedNode = _require(_dependencyMap[0], './AnimatedNode');

  var AnimatedTransform = _require(_dependencyMap[1], './AnimatedTransform');

  var AnimatedWithChildren = _require(_dependencyMap[2], './AnimatedWithChildren');

  var NativeAnimatedHelper = _require(_dependencyMap[3], '../NativeAnimatedHelper');

  var flattenStyle = _require(_dependencyMap[4], 'flattenStyle');

  var AnimatedStyle = function (_AnimatedWithChildren) {
    babelHelpers.inherits(AnimatedStyle, _AnimatedWithChildren);

    function AnimatedStyle(style) {
      babelHelpers.classCallCheck(this, AnimatedStyle);

      var _this = babelHelpers.possibleConstructorReturn(this, (AnimatedStyle.__proto__ || Object.getPrototypeOf(AnimatedStyle)).call(this));

      style = flattenStyle(style) || {};

      if (style.transform) {
        style = babelHelpers.extends({}, style, {
          transform: new AnimatedTransform(style.transform)
        });
      }

      _this._style = style;
      return _this;
    }

    babelHelpers.createClass(AnimatedStyle, [{
      key: "_walkStyleAndGetValues",
      value: function _walkStyleAndGetValues(style) {
        var updatedStyle = {};

        for (var key in style) {
          var value = style[key];

          if (value instanceof AnimatedNode) {
            if (!value.__isNative) {
              updatedStyle[key] = value.__getValue();
            }
          } else if (value && !Array.isArray(value) && typeof value === 'object') {
            updatedStyle[key] = this._walkStyleAndGetValues(value);
          } else {
            updatedStyle[key] = value;
          }
        }

        return updatedStyle;
      }
    }, {
      key: "__getValue",
      value: function __getValue() {
        return this._walkStyleAndGetValues(this._style);
      }
    }, {
      key: "_walkStyleAndGetAnimatedValues",
      value: function _walkStyleAndGetAnimatedValues(style) {
        var updatedStyle = {};

        for (var key in style) {
          var value = style[key];

          if (value instanceof AnimatedNode) {
            updatedStyle[key] = value.__getAnimatedValue();
          } else if (value && !Array.isArray(value) && typeof value === 'object') {
            updatedStyle[key] = this._walkStyleAndGetAnimatedValues(value);
          }
        }

        return updatedStyle;
      }
    }, {
      key: "__getAnimatedValue",
      value: function __getAnimatedValue() {
        return this._walkStyleAndGetAnimatedValues(this._style);
      }
    }, {
      key: "__attach",
      value: function __attach() {
        for (var key in this._style) {
          var value = this._style[key];

          if (value instanceof AnimatedNode) {
            value.__addChild(this);
          }
        }
      }
    }, {
      key: "__detach",
      value: function __detach() {
        for (var key in this._style) {
          var value = this._style[key];

          if (value instanceof AnimatedNode) {
            value.__removeChild(this);
          }
        }

        babelHelpers.get(AnimatedStyle.prototype.__proto__ || Object.getPrototypeOf(AnimatedStyle.prototype), "__detach", this).call(this);
      }
    }, {
      key: "__makeNative",
      value: function __makeNative() {
        babelHelpers.get(AnimatedStyle.prototype.__proto__ || Object.getPrototypeOf(AnimatedStyle.prototype), "__makeNative", this).call(this);

        for (var key in this._style) {
          var value = this._style[key];

          if (value instanceof AnimatedNode) {
            value.__makeNative();
          }
        }
      }
    }, {
      key: "__getNativeConfig",
      value: function __getNativeConfig() {
        var styleConfig = {};

        for (var styleKey in this._style) {
          if (this._style[styleKey] instanceof AnimatedNode) {
            styleConfig[styleKey] = this._style[styleKey].__getNativeTag();
          }
        }

        NativeAnimatedHelper.validateStyles(styleConfig);
        return {
          type: 'style',
          style: styleConfig
        };
      }
    }]);
    return AnimatedStyle;
  }(AnimatedWithChildren);

  module.exports = AnimatedStyle;
},"d9ec695655c5c9e2886dd0b8b8f324c8",["20e798e3651306aa73cc9b93f8a1fa75","83ab086bc646a982b8f23888710ebff2","432262d84c52a4f1a29f541ab0bb0c48","0efe8cba4f7d1f6a111a4698dabb344c","869f0bd4eed428d95df80a8c03d71093"],"AnimatedStyle");