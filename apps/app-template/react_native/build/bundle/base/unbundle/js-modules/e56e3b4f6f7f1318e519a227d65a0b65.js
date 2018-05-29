__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-navigation/src/views/TouchableItem.js";

  var _react = _require(_dependencyMap[0], "react");

  var _react2 = babelHelpers.interopRequireDefault(_react);

  var _reactNative = _require(_dependencyMap[1], "react-native");

  var ANDROID_VERSION_LOLLIPOP = 21;

  var TouchableItem = function (_React$Component) {
    babelHelpers.inherits(TouchableItem, _React$Component);

    function TouchableItem() {
      babelHelpers.classCallCheck(this, TouchableItem);
      return babelHelpers.possibleConstructorReturn(this, (TouchableItem.__proto__ || Object.getPrototypeOf(TouchableItem)).apply(this, arguments));
    }

    babelHelpers.createClass(TouchableItem, [{
      key: "render",
      value: function render() {
        if (_reactNative.Platform.OS === 'android' && _reactNative.Platform.Version >= ANDROID_VERSION_LOLLIPOP) {
          var _props = this.props,
              style = _props.style,
              rest = babelHelpers.objectWithoutProperties(_props, ["style"]);
          return _react2.default.createElement(
            _reactNative.TouchableNativeFeedback,
            babelHelpers.extends({}, rest, {
              style: null,
              background: _reactNative.TouchableNativeFeedback.Ripple(this.props.pressColor, this.props.borderless),
              __source: {
                fileName: _jsxFileName,
                lineNumber: 41
              }
            }),
            _react2.default.createElement(
              _reactNative.View,
              {
                style: style,
                __source: {
                  fileName: _jsxFileName,
                  lineNumber: 49
                }
              },
              _react2.default.Children.only(this.props.children)
            )
          );
        }

        return _react2.default.createElement(
          _reactNative.TouchableOpacity,
          babelHelpers.extends({}, this.props, {
            __source: {
              fileName: _jsxFileName,
              lineNumber: 55
            }
          }),
          this.props.children
        );
      }
    }]);
    return TouchableItem;
  }(_react2.default.Component);

  TouchableItem.defaultProps = {
    borderless: false,
    pressColor: 'rgba(0, 0, 0, .32)'
  };
  exports.default = TouchableItem;
},"e56e3b4f6f7f1318e519a227d65a0b65",["c42a5e17831e80ed1e1c8cf91f5ddb40","cc757a791ecb3cd320f65c256a791c04"],"node_modules/react-navigation/src/views/TouchableItem.js");