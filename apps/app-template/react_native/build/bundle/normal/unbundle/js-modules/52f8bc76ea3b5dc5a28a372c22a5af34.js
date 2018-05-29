__d(function (global, _require2, module, exports, _dependencyMap) {
  'use strict';

  var _require = _require2(_dependencyMap[0], '../AnimatedEvent'),
      AnimatedEvent = _require.AnimatedEvent;

  var AnimatedNode = _require2(_dependencyMap[1], './AnimatedNode');

  var AnimatedStyle = _require2(_dependencyMap[2], './AnimatedStyle');

  var NativeAnimatedHelper = _require2(_dependencyMap[3], '../NativeAnimatedHelper');

  var ReactNative = _require2(_dependencyMap[4], 'ReactNative');

  var invariant = _require2(_dependencyMap[5], 'fbjs/lib/invariant');

  var AnimatedProps = function (_AnimatedNode) {
    babelHelpers.inherits(AnimatedProps, _AnimatedNode);

    function AnimatedProps(props, callback) {
      babelHelpers.classCallCheck(this, AnimatedProps);

      var _this = babelHelpers.possibleConstructorReturn(this, (AnimatedProps.__proto__ || Object.getPrototypeOf(AnimatedProps)).call(this));

      if (props.style) {
        props = babelHelpers.extends({}, props, {
          style: new AnimatedStyle(props.style)
        });
      }

      _this._props = props;
      _this._callback = callback;

      _this.__attach();

      return _this;
    }

    babelHelpers.createClass(AnimatedProps, [{
      key: "__getValue",
      value: function __getValue() {
        var props = {};

        for (var key in this._props) {
          var value = this._props[key];

          if (value instanceof AnimatedNode) {
            if (!value.__isNative || value instanceof AnimatedStyle) {
              props[key] = value.__getValue();
            }
          } else if (value instanceof AnimatedEvent) {
            props[key] = value.__getHandler();
          } else {
            props[key] = value;
          }
        }

        return props;
      }
    }, {
      key: "__getAnimatedValue",
      value: function __getAnimatedValue() {
        var props = {};

        for (var key in this._props) {
          var value = this._props[key];

          if (value instanceof AnimatedNode) {
            props[key] = value.__getAnimatedValue();
          }
        }

        return props;
      }
    }, {
      key: "__attach",
      value: function __attach() {
        for (var key in this._props) {
          var value = this._props[key];

          if (value instanceof AnimatedNode) {
            value.__addChild(this);
          }
        }
      }
    }, {
      key: "__detach",
      value: function __detach() {
        if (this.__isNative && this._animatedView) {
          this.__disconnectAnimatedView();
        }

        for (var key in this._props) {
          var value = this._props[key];

          if (value instanceof AnimatedNode) {
            value.__removeChild(this);
          }
        }

        babelHelpers.get(AnimatedProps.prototype.__proto__ || Object.getPrototypeOf(AnimatedProps.prototype), "__detach", this).call(this);
      }
    }, {
      key: "update",
      value: function update() {
        this._callback();
      }
    }, {
      key: "__makeNative",
      value: function __makeNative() {
        if (!this.__isNative) {
          this.__isNative = true;

          for (var key in this._props) {
            var value = this._props[key];

            if (value instanceof AnimatedNode) {
              value.__makeNative();
            }
          }

          if (this._animatedView) {
            this.__connectAnimatedView();
          }
        }
      }
    }, {
      key: "setNativeView",
      value: function setNativeView(animatedView) {
        if (this._animatedView === animatedView) {
          return;
        }

        this._animatedView = animatedView;

        if (this.__isNative) {
          this.__connectAnimatedView();
        }
      }
    }, {
      key: "__connectAnimatedView",
      value: function __connectAnimatedView() {
        invariant(this.__isNative, 'Expected node to be marked as "native"');
        var nativeViewTag = ReactNative.findNodeHandle(this._animatedView);
        invariant(nativeViewTag != null, 'Unable to locate attached view in the native tree');
        NativeAnimatedHelper.API.connectAnimatedNodeToView(this.__getNativeTag(), nativeViewTag);
      }
    }, {
      key: "__disconnectAnimatedView",
      value: function __disconnectAnimatedView() {
        invariant(this.__isNative, 'Expected node to be marked as "native"');
        var nativeViewTag = ReactNative.findNodeHandle(this._animatedView);
        invariant(nativeViewTag != null, 'Unable to locate attached view in the native tree');
        NativeAnimatedHelper.API.disconnectAnimatedNodeFromView(this.__getNativeTag(), nativeViewTag);
      }
    }, {
      key: "__getNativeConfig",
      value: function __getNativeConfig() {
        var propsConfig = {};

        for (var propKey in this._props) {
          var value = this._props[propKey];

          if (value instanceof AnimatedNode) {
            propsConfig[propKey] = value.__getNativeTag();
          }
        }

        return {
          type: 'props',
          props: propsConfig
        };
      }
    }]);
    return AnimatedProps;
  }(AnimatedNode);

  module.exports = AnimatedProps;
},"52f8bc76ea3b5dc5a28a372c22a5af34",["73cfdce39648875fdc7a47dfedb7b199","20e798e3651306aa73cc9b93f8a1fa75","d9ec695655c5c9e2886dd0b8b8f324c8","0efe8cba4f7d1f6a111a4698dabb344c","1102b68d89d7a6aede9677567aa01362","8940a4ad43b101ffc23e725363c70f8d"],"AnimatedProps");