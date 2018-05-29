__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-navigation/node_modules/react-navigation-tabs/dist/views/CrossFadeIcon.js";

  var _react = _require(_dependencyMap[0], "react");

  var _react2 = babelHelpers.interopRequireDefault(_react);

  var _reactNative = _require(_dependencyMap[1], "react-native");

  var TabBarIcon = function (_React$Component) {
    babelHelpers.inherits(TabBarIcon, _React$Component);

    function TabBarIcon() {
      babelHelpers.classCallCheck(this, TabBarIcon);
      return babelHelpers.possibleConstructorReturn(this, (TabBarIcon.__proto__ || Object.getPrototypeOf(TabBarIcon)).apply(this, arguments));
    }

    babelHelpers.createClass(TabBarIcon, [{
      key: "render",
      value: function render() {
        var _props = this.props,
            route = _props.route,
            activeOpacity = _props.activeOpacity,
            inactiveOpacity = _props.inactiveOpacity,
            activeTintColor = _props.activeTintColor,
            inactiveTintColor = _props.inactiveTintColor,
            renderIcon = _props.renderIcon,
            style = _props.style;
        return _react2.default.createElement(
          _reactNative.View,
          {
            style: style,
            __source: {
              fileName: _jsxFileName,
              lineNumber: 18
            }
          },
          _react2.default.createElement(
            _reactNative.Animated.View,
            {
              style: [styles.icon, {
                opacity: activeOpacity
              }],
              __source: {
                fileName: _jsxFileName,
                lineNumber: 19
              }
            },
            renderIcon({
              route: route,
              focused: true,
              tintColor: activeTintColor
            })
          ),
          _react2.default.createElement(
            _reactNative.Animated.View,
            {
              style: [styles.icon, {
                opacity: inactiveOpacity
              }],
              __source: {
                fileName: _jsxFileName,
                lineNumber: 26
              }
            },
            renderIcon({
              route: route,
              focused: false,
              tintColor: inactiveTintColor
            })
          )
        );
      }
    }]);
    return TabBarIcon;
  }(_react2.default.Component);

  exports.default = TabBarIcon;

  var styles = _reactNative.StyleSheet.create({
    icon: {
      position: 'absolute',
      alignSelf: 'center',
      alignItems: 'center',
      justifyContent: 'center',
      height: '100%',
      width: '100%',
      minWidth: 25
    }
  });
},"4b9f50aabe1bfc273eedec5ed59a00b4",["c42a5e17831e80ed1e1c8cf91f5ddb40","cc757a791ecb3cd320f65c256a791c04"],"node_modules/react-navigation/node_modules/react-navigation-tabs/dist/views/CrossFadeIcon.js");