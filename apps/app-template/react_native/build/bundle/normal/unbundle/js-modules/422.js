__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.isOrientationLandscape = undefined;
  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-navigation/node_modules/react-navigation-tabs/node_modules/react-native-safe-area-view/withOrientation.js";

  exports.default = function (WrappedComponent) {
    var withOrientation = function (_React$Component) {
      babelHelpers.inherits(withOrientation, _React$Component);

      function withOrientation() {
        babelHelpers.classCallCheck(this, withOrientation);

        var _this = babelHelpers.possibleConstructorReturn(this, (withOrientation.__proto__ || Object.getPrototypeOf(withOrientation)).call(this));

        _initialiseProps.call(_this);

        var isLandscape = isOrientationLandscape(_reactNative.Dimensions.get('window'));
        _this.state = {
          isLandscape: isLandscape
        };
        return _this;
      }

      babelHelpers.createClass(withOrientation, [{
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
              lineNumber: 50
            }
          }));
        }
      }]);
      return withOrientation;
    }(React.Component);

    var _initialiseProps = function _initialiseProps() {
      var _this2 = this;

      this.handleOrientationChange = function (_ref2) {
        var window = _ref2.window;
        var isLandscape = isOrientationLandscape(window);

        _this2.setState({
          isLandscape: isLandscape
        });
      };
    };

    return (0, _hoistNonReactStatics2.default)(withOrientation, WrappedComponent);
  };

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
},422,[12,22,359],"node_modules/react-navigation/node_modules/react-navigation-tabs/node_modules/react-native-safe-area-view/withOrientation.js");