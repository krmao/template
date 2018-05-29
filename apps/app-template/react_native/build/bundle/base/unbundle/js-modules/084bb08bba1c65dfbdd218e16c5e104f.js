__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-navigation/node_modules/react-navigation-tabs/dist/views/MaterialTopTabBar.js";

  var _react = _require(_dependencyMap[0], "react");

  var React = babelHelpers.interopRequireWildcard(_react);

  var _reactNative = _require(_dependencyMap[1], "react-native");

  var _reactNativeTabView = _require(_dependencyMap[2], "react-native-tab-view");

  var _CrossFadeIcon = _require(_dependencyMap[3], "./CrossFadeIcon");

  var _CrossFadeIcon2 = babelHelpers.interopRequireDefault(_CrossFadeIcon);

  var TabBarTop = function (_React$PureComponent) {
    babelHelpers.inherits(TabBarTop, _React$PureComponent);

    function TabBarTop() {
      var _ref;

      var _temp, _this, _ret;

      babelHelpers.classCallCheck(this, TabBarTop);

      for (var _len = arguments.length, args = Array(_len), _key = 0; _key < _len; _key++) {
        args[_key] = arguments[_key];
      }

      return _ret = (_temp = (_this = babelHelpers.possibleConstructorReturn(this, (_ref = TabBarTop.__proto__ || Object.getPrototypeOf(TabBarTop)).call.apply(_ref, [this].concat(args))), _this), _this._renderLabel = function (_ref2) {
        var route = _ref2.route,
            index = _ref2.index,
            focused = _ref2.focused;
        var _this$props = _this.props,
            position = _this$props.position,
            navigation = _this$props.navigation,
            activeTintColor = _this$props.activeTintColor,
            inactiveTintColor = _this$props.inactiveTintColor,
            showLabel = _this$props.showLabel,
            upperCaseLabel = _this$props.upperCaseLabel,
            labelStyle = _this$props.labelStyle,
            allowFontScaling = _this$props.allowFontScaling;

        if (showLabel === false) {
          return null;
        }

        var routes = navigation.state.routes;
        var inputRange = [-1].concat(babelHelpers.toConsumableArray(routes.map(function (x, i) {
          return i;
        })));
        var outputRange = inputRange.map(function (inputIndex) {
          return inputIndex === index ? activeTintColor : inactiveTintColor;
        });
        var color = position.interpolate({
          inputRange: inputRange,
          outputRange: outputRange
        });
        var tintColor = focused ? activeTintColor : inactiveTintColor;

        var label = _this.props.getLabelText({
          route: route
        });

        if (typeof label === 'string') {
          return React.createElement(
            _reactNative.Animated.Text,
            {
              style: [styles.label, {
                color: color
              }, labelStyle],
              allowFontScaling: allowFontScaling,
              __source: {
                fileName: _jsxFileName,
                lineNumber: 45
              }
            },
            upperCaseLabel ? label.toUpperCase() : label
          );
        }

        if (typeof label === 'function') {
          return label({
            focused: focused,
            tintColor: tintColor
          });
        }

        return label;
      }, _this._renderIcon = function (_ref3) {
        var route = _ref3.route,
            index = _ref3.index;
        var _this$props2 = _this.props,
            position = _this$props2.position,
            navigation = _this$props2.navigation,
            activeTintColor = _this$props2.activeTintColor,
            inactiveTintColor = _this$props2.inactiveTintColor,
            renderIcon = _this$props2.renderIcon,
            showIcon = _this$props2.showIcon,
            iconStyle = _this$props2.iconStyle;

        if (showIcon === false) {
          return null;
        }

        var inputRange = [-1].concat(babelHelpers.toConsumableArray(navigation.state.routes.map(function (x, i) {
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
        return React.createElement(_CrossFadeIcon2.default, {
          route: route,
          navigation: navigation,
          activeOpacity: activeOpacity,
          inactiveOpacity: inactiveOpacity,
          activeTintColor: activeTintColor,
          inactiveTintColor: inactiveTintColor,
          renderIcon: renderIcon,
          style: [styles.icon, iconStyle],
          __source: {
            fileName: _jsxFileName,
            lineNumber: 82
          }
        });
      }, _temp), babelHelpers.possibleConstructorReturn(_this, _ret);
    }

    babelHelpers.createClass(TabBarTop, [{
      key: "render",
      value: function render() {
        var _props = this.props,
            navigation = _props.navigation,
            renderIcon = _props.renderIcon,
            getLabelText = _props.getLabelText,
            rest = babelHelpers.objectWithoutProperties(_props, ["navigation", "renderIcon", "getLabelText"]);
        return React.createElement(_reactNativeTabView.TabBar, babelHelpers.extends({}, rest, {
          navigationState: navigation.state,
          renderIcon: this._renderIcon,
          renderLabel: this._renderLabel,
          __source: {
            fileName: _jsxFileName,
            lineNumber: 91
          }
        }));
      }
    }]);
    return TabBarTop;
  }(React.PureComponent);

  TabBarTop.defaultProps = {
    activeTintColor: '#fff',
    inactiveTintColor: '#fff',
    showIcon: false,
    showLabel: true,
    upperCaseLabel: true,
    allowFontScaling: true
  };
  exports.default = TabBarTop;

  var styles = _reactNative.StyleSheet.create({
    icon: {
      height: 24,
      width: 24
    },
    label: {
      textAlign: 'center',
      fontSize: 13,
      margin: 8,
      backgroundColor: 'transparent'
    }
  });
},"084bb08bba1c65dfbdd218e16c5e104f",["c42a5e17831e80ed1e1c8cf91f5ddb40","cc757a791ecb3cd320f65c256a791c04","71fe908006d7688fb9fb0e7762d600b1","4b9f50aabe1bfc273eedec5ed59a00b4"],"node_modules/react-navigation/node_modules/react-navigation-tabs/dist/views/MaterialTopTabBar.js");