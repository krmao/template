__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-navigation/node_modules/react-navigation-deprecated-tab-navigator/src/views/TabBarIcon.js";

  var _react = _require(_dependencyMap[0], "react");

  var _react2 = babelHelpers.interopRequireDefault(_react);

  var _reactNative = _require(_dependencyMap[1], "react-native");

  var TabBarIcon = function (_React$PureComponent) {
    babelHelpers.inherits(TabBarIcon, _React$PureComponent);

    function TabBarIcon() {
      babelHelpers.classCallCheck(this, TabBarIcon);
      return babelHelpers.possibleConstructorReturn(this, (TabBarIcon.__proto__ || Object.getPrototypeOf(TabBarIcon)).apply(this, arguments));
    }

    babelHelpers.createClass(TabBarIcon, [{
      key: "render",
      value: function render() {
        var _props = this.props,
            position = _props.position,
            scene = _props.scene,
            navigation = _props.navigation,
            activeTintColor = _props.activeTintColor,
            inactiveTintColor = _props.inactiveTintColor,
            style = _props.style;
        var route = scene.route,
            index = scene.index;
        var routes = navigation.state.routes;
        var inputRange = [-1].concat(babelHelpers.toConsumableArray(routes.map(function (x, i) {
          return i;
        })));
        var activeOpacity = position.interpolate({
          inputRange: inputRange,
          outputRange: inputRange.map(function (i) {
            return i === index ? 1 : 0;
          })
        });
        var inactiveOpacity = position.interpolate({
          inputRange: inputRange,
          outputRange: inputRange.map(function (i) {
            return i === index ? 0 : 1;
          })
        });
        return _react2.default.createElement(
          _reactNative.View,
          {
            style: style,
            __source: {
              fileName: _jsxFileName,
              lineNumber: 30
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
                lineNumber: 31
              }
            },
            this.props.renderIcon({
              route: route,
              index: index,
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
                lineNumber: 39
              }
            },
            this.props.renderIcon({
              route: route,
              index: index,
              focused: false,
              tintColor: inactiveTintColor
            })
          )
        );
      }
    }]);
    return TabBarIcon;
  }(_react2.default.PureComponent);

  exports.default = TabBarIcon;

  var styles = _reactNative.StyleSheet.create({
    icon: {
      position: 'absolute',
      alignSelf: 'center',
      alignItems: 'center',
      justifyContent: 'center',
      height: '100%',
      width: '100%',
      minWidth: 30
    }
  });
},"b4c880aab035c4092bd2859d9f45c628",["c42a5e17831e80ed1e1c8cf91f5ddb40","cc757a791ecb3cd320f65c256a791c04"],"node_modules/react-navigation/node_modules/react-navigation-deprecated-tab-navigator/src/views/TabBarIcon.js");