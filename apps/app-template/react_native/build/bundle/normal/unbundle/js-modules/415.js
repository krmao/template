__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-navigation/node_modules/react-navigation-deprecated-tab-navigator/src/views/TabBarBottom.js";

  var _react = _require(_dependencyMap[0], "react");

  var _react2 = babelHelpers.interopRequireDefault(_react);

  var _reactNative = _require(_dependencyMap[1], "react-native");

  var _reactNavigation = _require(_dependencyMap[2], "react-navigation");

  var _TabBarIcon = _require(_dependencyMap[3], "./TabBarIcon");

  var _TabBarIcon2 = babelHelpers.interopRequireDefault(_TabBarIcon);

  var majorVersion = parseInt(_reactNative.Platform.Version, 10);
  var isIos = _reactNative.Platform.OS === 'ios';
  var isIOS11 = majorVersion >= 11 && isIos;
  var defaultMaxTabBarItemWidth = 125;

  var TabBarBottom = function (_React$PureComponent) {
    babelHelpers.inherits(TabBarBottom, _React$PureComponent);

    function TabBarBottom() {
      var _ref;

      var _temp, _this, _ret;

      babelHelpers.classCallCheck(this, TabBarBottom);

      for (var _len = arguments.length, args = Array(_len), _key = 0; _key < _len; _key++) {
        args[_key] = arguments[_key];
      }

      return _ret = (_temp = (_this = babelHelpers.possibleConstructorReturn(this, (_ref = TabBarBottom.__proto__ || Object.getPrototypeOf(TabBarBottom)).call.apply(_ref, [this].concat(args))), _this), _this._renderLabel = function (scene) {
        var _this$props = _this.props,
            position = _this$props.position,
            navigation = _this$props.navigation,
            activeTintColor = _this$props.activeTintColor,
            inactiveTintColor = _this$props.inactiveTintColor,
            labelStyle = _this$props.labelStyle,
            showLabel = _this$props.showLabel,
            showIcon = _this$props.showIcon,
            isLandscape = _this$props.isLandscape,
            allowFontScaling = _this$props.allowFontScaling;

        if (showLabel === false) {
          return null;
        }

        var index = scene.index;
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
        var tintColor = scene.focused ? activeTintColor : inactiveTintColor;

        var label = _this.props.getLabel(babelHelpers.extends({}, scene, {
          tintColor: tintColor
        }));

        if (typeof label === 'string') {
          return _react2.default.createElement(
            _reactNative.Animated.Text,
            {
              numberOfLines: 1,
              style: [styles.label, {
                color: color
              }, showIcon && _this._shouldUseHorizontalTabs() ? styles.labelBeside : styles.labelBeneath, labelStyle],
              allowFontScaling: allowFontScaling,
              __source: {
                fileName: _jsxFileName,
                lineNumber: 69
              }
            },
            label
          );
        }

        if (typeof label === 'function') {
          return label(babelHelpers.extends({}, scene, {
            tintColor: tintColor
          }));
        }

        return label;
      }, _this._renderIcon = function (scene) {
        var _this$props2 = _this.props,
            position = _this$props2.position,
            navigation = _this$props2.navigation,
            activeTintColor = _this$props2.activeTintColor,
            inactiveTintColor = _this$props2.inactiveTintColor,
            renderIcon = _this$props2.renderIcon,
            showIcon = _this$props2.showIcon,
            showLabel = _this$props2.showLabel;

        if (showIcon === false) {
          return null;
        }

        var horizontal = _this._shouldUseHorizontalTabs();

        return _react2.default.createElement(_TabBarIcon2.default, {
          position: position,
          navigation: navigation,
          activeTintColor: activeTintColor,
          inactiveTintColor: inactiveTintColor,
          renderIcon: renderIcon,
          scene: scene,
          style: [styles.iconWithExplicitHeight, showLabel === false && !horizontal && styles.iconWithoutLabel, showLabel !== false && !horizontal && styles.iconWithLabel],
          __source: {
            fileName: _jsxFileName,
            lineNumber: 110
          }
        });
      }, _this._renderTestIDProps = function (scene) {
        var testIDProps = _this.props.getTestIDProps && _this.props.getTestIDProps(scene);

        return testIDProps;
      }, _this._handleTabPress = function (index) {
        var _this$props3 = _this.props,
            jumpToIndex = _this$props3.jumpToIndex,
            navigation = _this$props3.navigation;
        var currentIndex = navigation.state.index;

        if (currentIndex === index) {
          var childRoute = navigation.state.routes[index];

          if (childRoute.hasOwnProperty('index') && childRoute.index > 0) {
            navigation.dispatch(_reactNavigation.StackActions.popToTop({
              key: childRoute.key
            }));
          } else {}
        } else {
          jumpToIndex(index);
        }
      }, _temp), babelHelpers.possibleConstructorReturn(_this, _ret);
    }

    babelHelpers.createClass(TabBarBottom, [{
      key: "_tabItemMaxWidth",
      value: function _tabItemMaxWidth() {
        var _props = this.props,
            tabStyle = _props.tabStyle,
            layout = _props.layout;
        var maxTabBarItemWidth = void 0;

        var flattenedTabStyle = _reactNative.StyleSheet.flatten(tabStyle);

        if (flattenedTabStyle) {
          if (typeof flattenedTabStyle.width === 'number') {
            maxTabBarItemWidth = flattenedTabStyle.width;
          } else if (typeof flattenedTabStyle.width === 'string' && flattenedTabStyle.width.endsWith('%')) {
            var width = parseFloat(flattenedTabStyle.width);

            if (Number.isFinite(width)) {
              maxTabBarItemWidth = layout.width * (width / 100);
            }
          } else if (typeof flattenedTabStyle.maxWidth === 'number') {
            maxTabBarItemWidth = flattenedTabStyle.maxWidth;
          } else if (typeof flattenedTabStyle.maxWidth === 'string' && flattenedTabStyle.width.endsWith('%')) {
            var _width = parseFloat(flattenedTabStyle.maxWidth);

            if (Number.isFinite(_width)) {
              maxTabBarItemWidth = layout.width * (_width / 100);
            }
          }
        }

        if (!maxTabBarItemWidth) {
          maxTabBarItemWidth = defaultMaxTabBarItemWidth;
        }

        return maxTabBarItemWidth;
      }
    }, {
      key: "_shouldUseHorizontalTabs",
      value: function _shouldUseHorizontalTabs() {
        var routes = this.props.navigation.state.routes;
        var _props2 = this.props,
            isLandscape = _props2.isLandscape,
            layout = _props2.layout,
            adaptive = _props2.adaptive,
            tabStyle = _props2.tabStyle;

        if (!adaptive) {
          return false;
        }

        var tabBarWidth = layout.width;

        if (tabBarWidth === 0) {
          return _reactNative.Platform.isPad;
        }

        if (!_reactNative.Platform.isPad) {
          return isLandscape;
        } else {
          var maxTabBarItemWidth = this._tabItemMaxWidth();

          return routes.length * maxTabBarItemWidth <= tabBarWidth;
        }
      }
    }, {
      key: "render",
      value: function render() {
        var _this2 = this;

        var _props3 = this.props,
            position = _props3.position,
            navigation = _props3.navigation,
            jumpToIndex = _props3.jumpToIndex,
            getOnPress = _props3.getOnPress,
            getTestIDProps = _props3.getTestIDProps,
            activeBackgroundColor = _props3.activeBackgroundColor,
            inactiveBackgroundColor = _props3.inactiveBackgroundColor,
            style = _props3.style,
            animateStyle = _props3.animateStyle,
            tabStyle = _props3.tabStyle,
            isLandscape = _props3.isLandscape;
        var routes = navigation.state.routes;
        var previousScene = routes[navigation.state.index];
        var inputRange = [-1].concat(babelHelpers.toConsumableArray(routes.map(function (x, i) {
          return i;
        })));
        var tabBarStyle = [styles.tabBar, this._shouldUseHorizontalTabs() && !_reactNative.Platform.isPad ? styles.tabBarCompact : styles.tabBarRegular, style];
        return _react2.default.createElement(
          _reactNative.Animated.View,
          {
            style: animateStyle,
            __source: {
              fileName: _jsxFileName,
              lineNumber: 236
            }
          },
          _react2.default.createElement(
            _reactNavigation.SafeAreaView,
            {
              style: tabBarStyle,
              forceInset: {
                bottom: 'always',
                top: 'never'
              },
              __source: {
                fileName: _jsxFileName,
                lineNumber: 237
              }
            },
            routes.map(function (route, index) {
              var focused = index === navigation.state.index;
              var scene = {
                route: route,
                index: index,
                focused: focused
              };

              var _onPress = getOnPress(previousScene, scene);

              var outputRange = inputRange.map(function (inputIndex) {
                return inputIndex === index ? activeBackgroundColor : inactiveBackgroundColor;
              });
              var backgroundColor = position.interpolate({
                inputRange: inputRange,
                outputRange: outputRange
              });
              var justifyContent = _this2.props.showIcon ? 'flex-end' : 'center';
              var extraProps = _this2._renderTestIDProps(scene) || {};
              var testID = extraProps.testID,
                  accessibilityLabel = extraProps.accessibilityLabel;
              return _react2.default.createElement(
                _reactNative.TouchableWithoutFeedback,
                {
                  key: route.key,
                  testID: testID,
                  accessibilityLabel: accessibilityLabel,
                  onPress: function onPress() {
                    return _onPress ? _onPress({
                      previousScene: previousScene,
                      scene: scene,
                      jumpToIndex: jumpToIndex,
                      defaultHandler: _this2._handleTabPress
                    }) : _this2._handleTabPress(index);
                  },
                  __source: {
                    fileName: _jsxFileName,
                    lineNumber: 261
                  }
                },
                _react2.default.createElement(
                  _reactNative.Animated.View,
                  {
                    style: [styles.tab, {
                      backgroundColor: backgroundColor
                    }],
                    __source: {
                      fileName: _jsxFileName,
                      lineNumber: 276
                    }
                  },
                  _react2.default.createElement(
                    _reactNative.View,
                    {
                      style: [styles.tab, _this2._shouldUseHorizontalTabs() ? styles.tabLandscape : styles.tabPortrait, tabStyle],
                      __source: {
                        fileName: _jsxFileName,
                        lineNumber: 277
                      }
                    },
                    _this2._renderIcon(scene),
                    _this2._renderLabel(scene)
                  )
                )
              );
            })
          )
        );
      }
    }]);
    return TabBarBottom;
  }(_react2.default.PureComponent);

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

  exports.default = (0, _reactNavigation.withOrientation)(TabBarBottom);
},415,[12,22,337,414],"node_modules/react-navigation/node_modules/react-navigation-deprecated-tab-navigator/src/views/TabBarBottom.js");