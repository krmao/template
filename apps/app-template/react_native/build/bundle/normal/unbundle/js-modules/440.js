__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-navigation/src/views/withNavigation.js";
  exports.default = withNavigation;

  var _react = _require(_dependencyMap[0], "react");

  var _react2 = babelHelpers.interopRequireDefault(_react);

  var _propTypes = _require(_dependencyMap[1], "prop-types");

  var _propTypes2 = babelHelpers.interopRequireDefault(_propTypes);

  var _hoistNonReactStatics = _require(_dependencyMap[2], "hoist-non-react-statics");

  var _hoistNonReactStatics2 = babelHelpers.interopRequireDefault(_hoistNonReactStatics);

  var _invariant = _require(_dependencyMap[3], "../utils/invariant");

  var _invariant2 = babelHelpers.interopRequireDefault(_invariant);

  var _NavigationContext = _require(_dependencyMap[4], "./NavigationContext");

  function withNavigation(Component) {
    var ComponentWithNavigation = function (_React$Component) {
      babelHelpers.inherits(ComponentWithNavigation, _React$Component);

      function ComponentWithNavigation() {
        babelHelpers.classCallCheck(this, ComponentWithNavigation);
        return babelHelpers.possibleConstructorReturn(this, (ComponentWithNavigation.__proto__ || Object.getPrototypeOf(ComponentWithNavigation)).apply(this, arguments));
      }

      babelHelpers.createClass(ComponentWithNavigation, [{
        key: "render",
        value: function render() {
          var _this2 = this;

          var navigationProp = this.props.navigation;
          return _react2.default.createElement(
            _NavigationContext.NavigationConsumer,
            {
              __source: {
                fileName: _jsxFileName,
                lineNumber: 15
              }
            },
            function (navigationContext) {
              var navigation = navigationProp || navigationContext;
              (0, _invariant2.default)(!!navigation, 'withNavigation can only be used on a view hierarchy of a navigator. The wrapped component is unable to get access to navigation from props or context.');
              return _react2.default.createElement(Component, babelHelpers.extends({}, _this2.props, {
                navigation: navigation,
                ref: _this2.props.onRef,
                __source: {
                  fileName: _jsxFileName,
                  lineNumber: 23
                }
              }));
            }
          );
        }
      }]);
      return ComponentWithNavigation;
    }(_react2.default.Component);

    ComponentWithNavigation.displayName = "withNavigation(" + (Component.displayName || Component.name) + ")";
    return (0, _hoistNonReactStatics2.default)(ComponentWithNavigation, Component);
  }
},440,[12,129,359,342,371],"node_modules/react-navigation/src/views/withNavigation.js");