__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-navigation/src/views/StackView/StackView.js";

  var _react = _require(_dependencyMap[0], "react");

  var React = babelHelpers.interopRequireWildcard(_react);

  var _reactNative = _require(_dependencyMap[1], "react-native");

  var _StackViewLayout = _require(_dependencyMap[2], "./StackViewLayout");

  var _StackViewLayout2 = babelHelpers.interopRequireDefault(_StackViewLayout);

  var _Transitioner = _require(_dependencyMap[3], "../Transitioner");

  var _Transitioner2 = babelHelpers.interopRequireDefault(_Transitioner);

  var _NavigationActions = _require(_dependencyMap[4], "../../NavigationActions");

  var _NavigationActions2 = babelHelpers.interopRequireDefault(_NavigationActions);

  var _StackActions = _require(_dependencyMap[5], "../../routers/StackActions");

  var _StackActions2 = babelHelpers.interopRequireDefault(_StackActions);

  var _StackViewTransitionConfigs = _require(_dependencyMap[6], "./StackViewTransitionConfigs");

  var _StackViewTransitionConfigs2 = babelHelpers.interopRequireDefault(_StackViewTransitionConfigs);

  var NativeAnimatedModule = _reactNative.NativeModules && _reactNative.NativeModules.NativeAnimatedModule;

  var StackView = function (_React$Component) {
    babelHelpers.inherits(StackView, _React$Component);

    function StackView() {
      var _ref;

      var _temp, _this, _ret;

      babelHelpers.classCallCheck(this, StackView);

      for (var _len = arguments.length, args = Array(_len), _key = 0; _key < _len; _key++) {
        args[_key] = arguments[_key];
      }

      return _ret = (_temp = (_this = babelHelpers.possibleConstructorReturn(this, (_ref = StackView.__proto__ || Object.getPrototypeOf(StackView)).call.apply(_ref, [this].concat(args))), _this), _this._configureTransition = function (transitionProps, prevTransitionProps) {
        return babelHelpers.extends({}, _StackViewTransitionConfigs2.default.getTransitionConfig(_this.props.navigationConfig.transitionConfig, transitionProps, prevTransitionProps, _this.props.navigationConfig.mode === 'modal').transitionSpec, {
          useNativeDriver: !!NativeAnimatedModule
        });
      }, _this._render = function (transitionProps, lastTransitionProps) {
        var _this$props = _this.props,
            screenProps = _this$props.screenProps,
            navigationConfig = _this$props.navigationConfig;
        return React.createElement(_StackViewLayout2.default, babelHelpers.extends({}, navigationConfig, {
          onGestureBegin: _this.props.onGestureBegin,
          onGestureCanceled: _this.props.onGestureCanceled,
          onGestureEnd: _this.props.onGestureEnd,
          screenProps: screenProps,
          descriptors: _this.props.descriptors,
          transitionProps: transitionProps,
          lastTransitionProps: lastTransitionProps,
          __source: {
            fileName: _jsxFileName,
            lineNumber: 58
          }
        }));
      }, _temp), babelHelpers.possibleConstructorReturn(_this, _ret);
    }

    babelHelpers.createClass(StackView, [{
      key: "render",
      value: function render() {
        var _this2 = this;

        return React.createElement(_Transitioner2.default, {
          render: this._render,
          configureTransition: this._configureTransition,
          navigation: this.props.navigation,
          descriptors: this.props.descriptors,
          onTransitionStart: this.props.onTransitionStart,
          onTransitionEnd: function onTransitionEnd(transition, lastTransition) {
            var _props = _this2.props,
                onTransitionEnd = _props.onTransitionEnd,
                navigation = _props.navigation;

            if (transition.navigation.state.isTransitioning) {
              navigation.dispatch(_StackActions2.default.completeTransition({
                key: navigation.state.key
              }));
            }

            onTransitionEnd && onTransitionEnd(transition, lastTransition);
          },
          __source: {
            fileName: _jsxFileName,
            lineNumber: 22
          }
        });
      }
    }]);
    return StackView;
  }(React.Component);

  StackView.defaultProps = {
    navigationConfig: {
      mode: 'card'
    }
  };
  exports.default = StackView;
},350,[12,22,351,378,341,369,375],"node_modules/react-navigation/src/views/StackView/StackView.js");