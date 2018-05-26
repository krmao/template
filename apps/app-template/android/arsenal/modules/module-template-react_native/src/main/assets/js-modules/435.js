__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-navigation/node_modules/react-navigation-tabs/node_modules/react-native-tab-view/src/TouchableItem.js";

  var _react = _require(_dependencyMap[0], "react");

  var React = babelHelpers.interopRequireWildcard(_react);

  var _propTypes = _require(_dependencyMap[1], "prop-types");

  var _propTypes2 = babelHelpers.interopRequireDefault(_propTypes);

  var _reactNative = _require(_dependencyMap[2], "react-native");

  var LOLLIPOP = 21;

  var TouchableItem = function (_React$Component) {
    babelHelpers.inherits(TouchableItem, _React$Component);

    function TouchableItem() {
      var _ref;

      var _temp, _this, _ret;

      babelHelpers.classCallCheck(this, TouchableItem);

      for (var _len = arguments.length, args = Array(_len), _key = 0; _key < _len; _key++) {
        args[_key] = arguments[_key];
      }

      return _ret = (_temp = (_this = babelHelpers.possibleConstructorReturn(this, (_ref = TouchableItem.__proto__ || Object.getPrototypeOf(TouchableItem)).call.apply(_ref, [this].concat(args))), _this), _this._handlePress = function () {
        global.requestAnimationFrame(_this.props.onPress);
      }, _temp), babelHelpers.possibleConstructorReturn(_this, _ret);
    }

    babelHelpers.createClass(TouchableItem, [{
      key: "render",
      value: function render() {
        var _props = this.props,
            style = _props.style,
            pressOpacity = _props.pressOpacity,
            pressColor = _props.pressColor,
            borderless = _props.borderless,
            rest = babelHelpers.objectWithoutProperties(_props, ["style", "pressOpacity", "pressColor", "borderless"]);

        if (_reactNative.Platform.OS === 'android' && _reactNative.Platform.Version >= LOLLIPOP) {
          return React.createElement(
            _reactNative.TouchableNativeFeedback,
            babelHelpers.extends({}, rest, {
              onPress: this._handlePress,
              background: _reactNative.TouchableNativeFeedback.Ripple(pressColor, borderless),
              __source: {
                fileName: _jsxFileName,
                lineNumber: 48
              }
            }),
            React.createElement(
              _reactNative.View,
              {
                style: style,
                __source: {
                  fileName: _jsxFileName,
                  lineNumber: 53
                }
              },
              React.Children.only(this.props.children)
            )
          );
        } else {
          return React.createElement(
            _reactNative.TouchableOpacity,
            babelHelpers.extends({}, rest, {
              onPress: this._handlePress,
              style: style,
              activeOpacity: pressOpacity,
              __source: {
                fileName: _jsxFileName,
                lineNumber: 58
              }
            }),
            this.props.children
          );
        }
      }
    }]);
    return TouchableItem;
  }(React.Component);

  TouchableItem.propTypes = {
    onPress: _propTypes2.default.func.isRequired,
    delayPressIn: _propTypes2.default.number,
    borderless: _propTypes2.default.bool,
    pressColor: _propTypes2.default.string,
    pressOpacity: _propTypes2.default.number,
    children: _propTypes2.default.node.isRequired
  };
  TouchableItem.defaultProps = {
    pressColor: 'rgba(255, 255, 255, .4)'
  };
  exports.default = TouchableItem;
},435,[12,129,22],"node_modules/react-navigation/node_modules/react-navigation-tabs/node_modules/react-native-tab-view/src/TouchableItem.js");