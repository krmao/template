__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-navigation/node_modules/react-navigation-tabs/dist/views/BottomTabBar.js";

  var _react = _require(_dependencyMap[0], "react");

  var _react2 = babelHelpers.interopRequireDefault(_react);

  var _reactNative = _require(_dependencyMap[1], "react-native");

  var _reactNativeSafeAreaView = _require(_dependencyMap[2], "react-native-safe-area-view");

  var _reactNativeSafeAreaView2 = babelHelpers.interopRequireDefault(_reactNativeSafeAreaView);

  var _CrossFadeIcon = _require(_dependencyMap[3], "./CrossFadeIcon");

  var _CrossFadeIcon2 = babelHelpers.interopRequireDefault(_CrossFadeIcon);

  var _withDimensions = _require(_dependencyMap[4], "../utils/withDimensions");

  var _withDimensions2 = babelHelpers.interopRequireDefault(_withDimensions);

  var majorVersion = parseInt(_reactNative.Platform.Version, 10);
  var isIos = _reactNative.Platform.OS === 'ios';
  var isIOS11 = majorVersion >= 11 && isIos;
  var DEFAULT_MAX_TAB_ITEM_WIDTH = 125;

  var TabBarBottom = function (_React$Component) {
    babelHelpers.inherits(TabBarBottom, _React$Component);

    function TabBarBottom() {
      var _ref;

      var _temp, _this, _ret;

      babelHelpers.classCallCheck(this, TabBarBottom);

      for (var _len = arguments.length, args = Array(_len), _key = 0; _key < _len; _key++) {
        args[_key] = arguments[_key];
      }

      return _ret = (_temp = (_this = babelHelpers.possibleConstructorReturn(this, (_ref = TabBarBottom.__proto__ || Object.getPrototypeOf(TabBarBottom)).call.apply(_ref, [this].concat(args))), _this), _this._renderLabel = function (_ref2) {
        var route = _ref2.route,
            focused = _ref2.focused;
        var _this$props = _this.props,
            activeTintColor = _this$props.activeTintColor,
            inactiveTintColor = _this$props.inactiveTintColor,
            labelStyle = _this$props.labelStyle,
            showLabel = _this$props.showLabel,
            showIcon = _this$props.showIcon,
            allowFontScaling = _this$props.allowFontScaling;

        if (showLabel === false) {
          return null;
        }

        var label = _this.props.getLabelText({
          route: route
        });

        var tintColor = focused ? activeTintColor : inactiveTintColor;

        if (typeof label === 'string') {
          return _react2.default.createElement(
            _reactNative.Animated.Text,
            {
              numberOfLines: 1,
              style: [styles.label, {
                color: tintColor
              }, showIcon && _this._shouldUseHorizontalLabels() ? styles.labelBeside : styles.labelBeneath, styles.labelBeneath, labelStyle],
              allowFontScaling: allowFontScaling,
              __source: {
                fileName: _jsxFileName,
                lineNumber: 44
              }
            },
            label
          );
        }

        if (typeof label === 'function') {
          return label({
            route: route,
            focused: focused,
            tintColor: tintColor
          });
        }

        return label;
      }, _this._renderIcon = function (_ref3) {
        var route = _ref3.route,
            focused = _ref3.focused;
        var _this$props2 = _this.props,
            navigation = _this$props2.navigation,
            activeTintColor = _this$props2.activeTintColor,
            inactiveTintColor = _this$props2.inactiveTintColor,
            renderIcon = _this$props2.renderIcon,
            showIcon = _this$props2.showIcon,
            showLabel = _this$props2.showLabel;

        if (showIcon === false) {
          return null;
        }

        var horizontal = _this._shouldUseHorizontalLabels();

        var activeOpacity = focused ? 1 : 0;
        var inactiveOpacity = focused ? 0 : 1;
        return _react2.default.createElement(_CrossFadeIcon2.default, {
          route: route,
          navigation: navigation,
          activeOpacity: activeOpacity,
          inactiveOpacity: inactiveOpacity,
          activeTintColor: activeTintColor,
          inactiveTintColor: inactiveTintColor,
          renderIcon: renderIcon,
          style: [styles.iconWithExplicitHeight, showLabel === false && !horizontal && styles.iconWithoutLabel, showLabel !== false && !horizontal && styles.iconWithLabel],
          __source: {
            fileName: _jsxFileName,
            lineNumber: 74
          }
        });
      }, _this._shouldUseHorizontalLabels = function () {
        var routes = _this.props.navigation.state.routes;
        var _this$props3 = _this.props,
            isLandscape = _this$props3.isLandscape,
            dimensions = _this$props3.dimensions,
            adaptive = _this$props3.adaptive,
            tabStyle = _this$props3.tabStyle;

        if (!adaptive) {
          return false;
        }

        if (_reactNative.Platform.isPad) {
          var maxTabItemWidth = DEFAULT_MAX_TAB_ITEM_WIDTH;

          var flattenedStyle = _reactNative.StyleSheet.flatten(tabStyle);

          if (flattenedStyle) {
            if (typeof flattenedStyle.width === 'number') {
              maxTabItemWidth = flattenedStyle.width;
            } else if (typeof flattenedStyle.maxWidth === 'number') {
              maxTabItemWidth = flattenedStyle.maxWidth;
            }
          }

          return routes.length * maxTabItemWidth <= dimensions.width;
        } else {
          return isLandscape;
        }
      }, _temp), babelHelpers.possibleConstructorReturn(_this, _ret);
    }

    babelHelpers.createClass(TabBarBottom, [{
      key: "render",
      value: function render() {
        var _this2 = this;

        var _props = this.props,
            navigation = _props.navigation,
            activeBackgroundColor = _props.activeBackgroundColor,
            inactiveBackgroundColor = _props.inactiveBackgroundColor,
            onTabPress = _props.onTabPress,
            jumpTo = _props.jumpTo,
            style = _props.style,
            tabStyle = _props.tabStyle;
        var routes = navigation.state.routes;
        var tabBarStyle = [styles.tabBar, this._shouldUseHorizontalLabels() && !_reactNative.Platform.isPad ? styles.tabBarCompact : styles.tabBarRegular, style];
        return _react2.default.createElement(
          _reactNativeSafeAreaView2.default,
          {
            style: tabBarStyle,
            forceInset: {
              bottom: 'always',
              top: 'never'
            },
            __source: {
              fileName: _jsxFileName,
              lineNumber: 119
            }
          },
          routes.map(function (route, index) {
            var focused = index === navigation.state.index;
            var scene = {
              route: route,
              focused: focused
            };
            var backgroundColor = focused ? activeBackgroundColor : inactiveBackgroundColor;
            return _react2.default.createElement(
              _reactNative.TouchableWithoutFeedback,
              {
                key: route.key,
                onPress: function onPress() {
                  onTabPress({
                    route: route
                  });
                  jumpTo(route.key);
                },
                __source: {
                  fileName: _jsxFileName,
                  lineNumber: 126
                }
              },
              _react2.default.createElement(
                _reactNative.View,
                {
                  style: [styles.tab, {
                    backgroundColor: backgroundColor
                  }, _this2._shouldUseHorizontalLabels() ? styles.tabLandscape : styles.tabPortrait, tabStyle],
                  __source: {
                    fileName: _jsxFileName,
                    lineNumber: 130
                  }
                },
                _this2._renderIcon(scene),
                _this2._renderLabel(scene)
              )
            );
          })
        );
      }
    }]);
    return TabBarBottom;
  }(_react2.default.Component);

  TabBarBottom.defaultProps = {
    activeTintColor: '#3478f6',
    activeBackgroundColor: 'transparent',
    inactiveTintColor: '#929292',
    inactiveBackgroundColor: 'transparent',
    showLabel: true,
    showIcon: true,
    allowFontScaling: true,
    adaptive: isIOS11
  };
  var DEFAULT_HEIGHT = 49;
  var COMPACT_HEIGHT = 29;

  var styles = _reactNative.StyleSheet.create({
    tabBar: {
      backgroundColor: '#F7F7F7',
      borderTopWidth: _reactNative.StyleSheet.hairlineWidth,
      borderTopColor: 'rgba(0, 0, 0, .3)',
      flexDirection: 'row'
    },
    tabBarCompact: {
      height: COMPACT_HEIGHT
    },
    tabBarRegular: {
      height: DEFAULT_HEIGHT
    },
    tab: {
      flex: 1,
      alignItems: isIos ? 'center' : 'stretch'
    },
    tabPortrait: {
      justifyContent: 'flex-end',
      flexDirection: 'column'
    },
    tabLandscape: {
      justifyContent: 'center',
      flexDirection: 'row'
    },
    iconWithoutLabel: {
      flex: 1
    },
    iconWithLabel: {
      flex: 1
    },
    iconWithExplicitHeight: {
      height: _reactNative.Platform.isPad ? DEFAULT_HEIGHT : COMPACT_HEIGHT
    },
    label: {
      textAlign: 'center',
      backgroundColor: 'transparent'
    },
    labelBeneath: {
      fontSize: 10,
      marginBottom: 1.5
    },
    labelBeside: {
      fontSize: 13,
      marginLeft: 20
    }
  });

  exports.default = (0, _withDimensions2.default)(TabBarBottom);
},420,[12,22,421,423,424],"node_modules/react-navigation/node_modules/react-navigation-tabs/dist/views/BottomTabBar.js");