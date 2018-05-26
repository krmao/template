__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-navigation/src/views/withNavigationFocus.js";
  exports.default = withNavigationFocus;

  var _react = _require(_dependencyMap[0], "react");

  var _react2 = babelHelpers.interopRequireDefault(_react);

  var _propTypes = _require(_dependencyMap[1], "prop-types");

  var _propTypes2 = babelHelpers.interopRequireDefault(_propTypes);

  var _hoistNonReactStatics = _require(_dependencyMap[2], "hoist-non-react-statics");

  var _hoistNonReactStatics2 = babelHelpers.interopRequireDefault(_hoistNonReactStatics);

  var _invariant = _require(_dependencyMap[3], "../utils/invariant");

  var _invariant2 = babelHelpers.interopRequireDefault(_invariant);

  var _withNavigation = _require(_dependencyMap[4], "./withNavigation");

  var _withNavigation2 = babelHelpers.interopRequireDefault(_withNavigation);

  function withNavigationFocus(Component) {
    var ComponentWithNavigationFocus = function (_React$Component) {
      babelHelpers.inherits(ComponentWithNavigationFocus, _React$Component);

      function ComponentWithNavigationFocus(props) {
        babelHelpers.classCallCheck(this, ComponentWithNavigationFocus);

        var _this = babelHelpers.possibleConstructorReturn(this, (ComponentWithNavigationFocus.__proto__ || Object.getPrototypeOf(ComponentWithNavigationFocus)).call(this, props));

        _this.state = {
          isFocused: props.navigation ? props.navigation.isFocused() : false
        };
        return _this;
      }

      babelHelpers.createClass(ComponentWithNavigationFocus, [{
        key: "componentDidMount",
        value: function componentDidMount() {
          var _this2 = this;

          var navigation = this.props.navigation;
          (0, _invariant2.default)(!!navigation, 'withNavigationFocus can only be used on a view hierarchy of a navigator. The wrapped component is unable to get access to navigation from props or context.');
          this.subscriptions = [navigation.addListener('didFocus', function () {
            return _this2.setState({
              isFocused: true
            });
          }), navigation.addListener('willBlur', function () {
            return _this2.setState({
              isFocused: false
            });
          })];
        }
      }, {
        key: "componentWillUnmount",
        value: function componentWillUnmount() {
          this.subscriptions.forEach(function (sub) {
            return sub.remove();
          });
        }
      }, {
        key: "render",
        value: function render() {
          return _react2.default.createElement(Component, babelHelpers.extends({}, this.props, {
            isFocused: this.state.isFocused,
            ref: this.props.onRef,
            __source: {
              fileName: _jsxFileName,
              lineNumber: 43
            }
          }));
        }
      }]);
      return ComponentWithNavigationFocus;
    }(_react2.default.Component);

    ComponentWithNavigationFocus.displayName = "withNavigationFocus(" + (Component.displayName || Component.name) + ")";
    return (0, _hoistNonReactStatics2.default)((0, _withNavigation2.default)(ComponentWithNavigationFocus), Component);
  }
},441,[12,129,359,342,440],"node_modules/react-navigation/src/views/withNavigationFocus.js");