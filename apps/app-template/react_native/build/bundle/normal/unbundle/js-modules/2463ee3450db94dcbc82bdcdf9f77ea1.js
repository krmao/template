__d(function (global, _require2, module, exports, _dependencyMap) {
  'use strict';

  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-native/Libraries/Animated/src/createAnimatedComponent.js";

  var _require = _require2(_dependencyMap[0], './AnimatedEvent'),
      AnimatedEvent = _require.AnimatedEvent;

  var AnimatedProps = _require2(_dependencyMap[1], './nodes/AnimatedProps');

  var React = _require2(_dependencyMap[2], 'React');

  var ViewStylePropTypes = _require2(_dependencyMap[3], 'ViewStylePropTypes');

  var invariant = _require2(_dependencyMap[4], 'fbjs/lib/invariant');

  function createAnimatedComponent(Component) {
    invariant(typeof Component === 'string' || Component.prototype && Component.prototype.isReactComponent, '`createAnimatedComponent` does not support stateless functional components; ' + 'use a class component instead.');

    var AnimatedComponent = function (_React$Component) {
      babelHelpers.inherits(AnimatedComponent, _React$Component);

      function AnimatedComponent(props) {
        babelHelpers.classCallCheck(this, AnimatedComponent);

        var _this = babelHelpers.possibleConstructorReturn(this, (AnimatedComponent.__proto__ || Object.getPrototypeOf(AnimatedComponent)).call(this, props));

        _this._invokeAnimatedPropsCallbackOnMount = false;
        _this._eventDetachers = [];

        _this._animatedPropsCallback = function () {
          if (_this._component == null) {
            _this._invokeAnimatedPropsCallbackOnMount = true;
          } else if (AnimatedComponent.__skipSetNativeProps_FOR_TESTS_ONLY || typeof _this._component.setNativeProps !== 'function') {
            _this.forceUpdate();
          } else if (!_this._propsAnimated.__isNative) {
            _this._component.setNativeProps(_this._propsAnimated.__getAnimatedValue());
          } else {
            throw new Error('Attempting to run JS driven animation on animated ' + 'node that has been moved to "native" earlier by starting an ' + 'animation with `useNativeDriver: true`');
          }
        };

        _this._setComponentRef = _this._setComponentRef.bind(_this);
        return _this;
      }

      babelHelpers.createClass(AnimatedComponent, [{
        key: "componentWillUnmount",
        value: function componentWillUnmount() {
          this._propsAnimated && this._propsAnimated.__detach();

          this._detachNativeEvents();
        }
      }, {
        key: "setNativeProps",
        value: function setNativeProps(props) {
          this._component.setNativeProps(props);
        }
      }, {
        key: "UNSAFE_componentWillMount",
        value: function UNSAFE_componentWillMount() {
          this._attachProps(this.props);
        }
      }, {
        key: "componentDidMount",
        value: function componentDidMount() {
          if (this._invokeAnimatedPropsCallbackOnMount) {
            this._invokeAnimatedPropsCallbackOnMount = false;

            this._animatedPropsCallback();
          }

          this._propsAnimated.setNativeView(this._component);

          this._attachNativeEvents();
        }
      }, {
        key: "_attachNativeEvents",
        value: function _attachNativeEvents() {
          var _this2 = this;

          var scrollableNode = this._component.getScrollableNode ? this._component.getScrollableNode() : this._component;

          var _loop = function _loop(key) {
            var prop = _this2.props[key];

            if (prop instanceof AnimatedEvent && prop.__isNative) {
              prop.__attach(scrollableNode, key);

              _this2._eventDetachers.push(function () {
                return prop.__detach(scrollableNode, key);
              });
            }
          };

          for (var key in this.props) {
            _loop(key);
          }
        }
      }, {
        key: "_detachNativeEvents",
        value: function _detachNativeEvents() {
          this._eventDetachers.forEach(function (remove) {
            return remove();
          });

          this._eventDetachers = [];
        }
      }, {
        key: "_attachProps",
        value: function _attachProps(nextProps) {
          var oldPropsAnimated = this._propsAnimated;
          this._propsAnimated = new AnimatedProps(nextProps, this._animatedPropsCallback);
          oldPropsAnimated && oldPropsAnimated.__detach();
        }
      }, {
        key: "UNSAFE_componentWillReceiveProps",
        value: function UNSAFE_componentWillReceiveProps(newProps) {
          this._attachProps(newProps);
        }
      }, {
        key: "componentDidUpdate",
        value: function componentDidUpdate(prevProps) {
          if (this._component !== this._prevComponent) {
            this._propsAnimated.setNativeView(this._component);
          }

          if (this._component !== this._prevComponent || prevProps !== this.props) {
            this._detachNativeEvents();

            this._attachNativeEvents();
          }
        }
      }, {
        key: "render",
        value: function render() {
          var props = this._propsAnimated.__getValue();

          return React.createElement(Component, babelHelpers.extends({}, props, {
            ref: this._setComponentRef,
            collapsable: this._propsAnimated.__isNative ? false : props.collapsable,
            __source: {
              fileName: _jsxFileName,
              lineNumber: 154
            }
          }));
        }
      }, {
        key: "_setComponentRef",
        value: function _setComponentRef(c) {
          this._prevComponent = this._component;
          this._component = c;
        }
      }, {
        key: "getNode",
        value: function getNode() {
          return this._component;
        }
      }]);
      return AnimatedComponent;
    }(React.Component);

    AnimatedComponent.__skipSetNativeProps_FOR_TESTS_ONLY = false;
    var propTypes = Component.propTypes;
    AnimatedComponent.propTypes = {
      style: function style(props, propName, componentName) {
        if (!propTypes) {
          return;
        }

        for (var key in ViewStylePropTypes) {
          if (!propTypes[key] && props[key] !== undefined) {
            console.warn('You are setting the style `{ ' + key + ': ... }` as a prop. You ' + 'should nest it in a style object. ' + 'E.g. `{ style: { ' + key + ': ... } }`');
          }
        }
      }
    };
    return AnimatedComponent;
  }

  module.exports = createAnimatedComponent;
},"2463ee3450db94dcbc82bdcdf9f77ea1",["73cfdce39648875fdc7a47dfedb7b199","52f8bc76ea3b5dc5a28a372c22a5af34","e6db4f0efed6b72f641ef0ffed29569f","007a69250301e98c7330c9706693fadc","8940a4ad43b101ffc23e725363c70f8d"],"createAnimatedComponent");