__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.isOrientationLandscape = undefined;
  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-navigation/node_modules/react-navigation-tabs/dist/utils/withDimensions.js";
  exports.default = withDimensions;

  var _react = _require(_dependencyMap[0], "react");

  var React = babelHelpers.interopRequireWildcard(_react);

  var _reactNative = _require(_dependencyMap[1], "react-native");

  var _hoistNonReactStatics = _require(_dependencyMap[2], "hoist-non-react-statics");

  var _hoistNonReactStatics2 = babelHelpers.interopRequireDefault(_hoistNonReactStatics);

  var isOrientationLandscape = exports.isOrientationLandscape = function isOrientationLandscape(_ref) {
    var width = _ref.width,
        height = _ref.height;
    return width > height;
  };

  function withDimensions(WrappedComponent) {
    var _Dimensions$get = _reactNative.Dimensions.get('window'),
        width = _Dimensions$get.width,
        height = _Dimensions$get.height;

    var EnhancedComponent = function (_React$Component) {
      babelHelpers.inherits(EnhancedComponent, _React$Component);

      function EnhancedComponent() {
        var _ref2;

        var _temp, _this, _ret;

        babelHelpers.classCallCheck(this, EnhancedComponent);

        for (var _len = arguments.length, args = Array(_len), _key = 0; _key < _len; _key++) {
          args[_key] = arguments[_key];
        }

        return _ret = (_temp = (_this = babelHelpers.possibleConstructorReturn(this, (_ref2 = EnhancedComponent.__proto__ || Object.getPrototypeOf(EnhancedComponent)).call.apply(_ref2, [this].concat(args))), _this), _this.state = {
          dimensions: {
            width: width,
            height: height
          },
          isLandscape: isOrientationLandscape({
            width: width,
            height: height
          })
        }, _this.handleOrientationChange = function (_ref3) {
          var window = _ref3.window;
          var isLandscape = isOrientationLandscape(window);

          _this.setState({
            isLandscape: isLandscape
          });
        }, _temp), babelHelpers.possibleConstructorReturn(_this, _ret);
      }

      babelHelpers.createClass(EnhancedComponent, [{
        key: "componentDidMount",
        value: function componentDidMount() {
          _reactNative.Dimensions.addEventListener('change', this.handleOrientationChange);
        }
      }, {
        key: "componentWillUnmount",
        value: function componentWillUnmount() {
          _reactNative.Dimensions.removeEventListener('change', this.handleOrientationChange);
        }
      }, {
        key: "render",
        value: function render() {
          return React.createElement(WrappedComponent, babelHelpers.extends({}, this.props, this.state, {
            __source: {
              fileName: _jsxFileName,
              lineNumber: 32
            }
          }));
        }
      }]);
      return EnhancedComponent;
    }(React.Component);

    EnhancedComponent.displayName = "withDimensions(" + WrappedComponent.displayName + ")";
    return (0, _hoistNonReactStatics2.default)(EnhancedComponent, WrappedComponent);
  }
},"96d800472136f99b1eedfa07b802b481",["c42a5e17831e80ed1e1c8cf91f5ddb40","cc757a791ecb3cd320f65c256a791c04","f907f0073e464c1cfd16c432b370b585"],"node_modules/react-navigation/node_modules/react-navigation-tabs/dist/utils/withDimensions.js");