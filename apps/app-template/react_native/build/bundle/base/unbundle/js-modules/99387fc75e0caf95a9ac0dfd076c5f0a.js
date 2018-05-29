__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-navigation/node_modules/react-native-tab-view/src/TabBar.js";

  var _react = _require(_dependencyMap[0], "react");

  var React = babelHelpers.interopRequireWildcard(_react);

  var _propTypes = _require(_dependencyMap[1], "prop-types");

  var _propTypes2 = babelHelpers.interopRequireDefault(_propTypes);

  var _reactNative = _require(_dependencyMap[2], "react-native");

  var _TouchableItem = _require(_dependencyMap[3], "./TouchableItem");

  var _TouchableItem2 = babelHelpers.interopRequireDefault(_TouchableItem);

  var _TabViewPropTypes = _require(_dependencyMap[4], "./TabViewPropTypes");

  var useNativeDriver = Boolean(_reactNative.NativeModules.NativeAnimatedModule);

  var TabBar = function (_React$Component) {
    babelHelpers.inherits(TabBar, _React$Component);

    function TabBar(props) {
      babelHelpers.classCallCheck(this, TabBar);

      var _this = babelHelpers.possibleConstructorReturn(this, (TabBar.__proto__ || Object.getPrototypeOf(TabBar)).call(this, props));

      _initialiseProps.call(_this);

      var initialVisibility = 1;

      if (_this.props.scrollEnabled) {
        var tabWidth = _this._getTabWidth(_this.props);

        if (!tabWidth) {
          initialVisibility = 0;
        }
      }

      var initialOffset = _this.props.scrollEnabled && _this.props.layout.width ? {
        x: _this._getScrollAmount(_this.props, _this.props.navigationState.index),
        y: 0
      } : undefined;
      _this.state = {
        visibility: new _reactNative.Animated.Value(initialVisibility),
        scrollAmount: new _reactNative.Animated.Value(0),
        initialOffset: initialOffset
      };
      return _this;
    }

    babelHelpers.createClass(TabBar, [{
      key: "componentDidMount",
      value: function componentDidMount() {
        this.props.scrollEnabled && this._startTrackingPosition();
      }
    }, {
      key: "componentDidUpdate",
      value: function componentDidUpdate(prevProps) {
        var prevTabWidth = this._getTabWidth(prevProps);

        var currentTabWidth = this._getTabWidth(this.props);

        if (prevTabWidth !== currentTabWidth && currentTabWidth) {
          this.state.visibility.setValue(1);
        }

        if ((prevProps.navigationState !== this.props.navigationState || prevProps.layout !== this.props.layout || prevTabWidth !== currentTabWidth) && this.props.navigationState.index !== this._pendingIndex) {
          this._resetScroll(this.props.navigationState.index, Boolean(prevProps.layout.width));

          this._pendingIndex = null;
        }
      }
    }, {
      key: "componentWillUnmount",
      value: function componentWillUnmount() {
        this._stopTrackingPosition();
      }
    }, {
      key: "render",
      value: function render() {
        var _this2 = this;

        var _props = this.props,
            position = _props.position,
            navigationState = _props.navigationState,
            scrollEnabled = _props.scrollEnabled,
            bounces = _props.bounces;
        var routes = navigationState.routes,
            index = navigationState.index;

        var tabWidth = this._getTabWidth(this.props);

        var tabBarWidth = tabWidth * routes.length;
        var inputRange = [-1].concat(babelHelpers.toConsumableArray(routes.map(function (x, i) {
          return i;
        })));

        var translateX = _reactNative.Animated.multiply(this.state.scrollAmount, -1);

        return React.createElement(
          _reactNative.Animated.View,
          {
            style: [styles.tabBar, this.props.style],
            __source: {
              fileName: _jsxFileName,
              lineNumber: 346
            }
          },
          React.createElement(
            _reactNative.Animated.View,
            {
              pointerEvents: "none",
              style: [styles.indicatorContainer, scrollEnabled ? {
                width: tabBarWidth,
                transform: [{
                  translateX: translateX
                }]
              } : null],
              __source: {
                fileName: _jsxFileName,
                lineNumber: 347
              }
            },
            this._renderIndicator(babelHelpers.extends({}, this.props, {
              width: tabWidth
            }))
          ),
          React.createElement(
            _reactNative.View,
            {
              style: styles.scroll,
              __source: {
                fileName: _jsxFileName,
                lineNumber: 361
              }
            },
            React.createElement(
              _reactNative.Animated.ScrollView,
              {
                horizontal: true,
                keyboardShouldPersistTaps: "handled",
                scrollEnabled: scrollEnabled,
                bounces: bounces,
                alwaysBounceHorizontal: false,
                scrollsToTop: false,
                showsHorizontalScrollIndicator: false,
                automaticallyAdjustContentInsets: false,
                overScrollMode: "never",
                contentContainerStyle: [styles.tabContent, scrollEnabled ? null : styles.container],
                scrollEventThrottle: 1,
                onScroll: _reactNative.Animated.event([{
                  nativeEvent: {
                    contentOffset: {
                      x: this.state.scrollAmount
                    }
                  }
                }], {
                  useNativeDriver: useNativeDriver
                }),
                onScrollBeginDrag: this._handleBeginDrag,
                onScrollEndDrag: this._handleEndDrag,
                onMomentumScrollBegin: this._handleMomentumScrollBegin,
                onMomentumScrollEnd: this._handleMomentumScrollEnd,
                contentOffset: this.state.initialOffset,
                ref: this._setRef,
                __source: {
                  fileName: _jsxFileName,
                  lineNumber: 362
                }
              },
              routes.map(function (route, i) {
                var focused = index === i;
                var outputRange = inputRange.map(function (inputIndex) {
                  return inputIndex === i ? 1 : 0.7;
                });

                var opacity = _reactNative.Animated.multiply(_this2.state.visibility, position.interpolate({
                  inputRange: inputRange,
                  outputRange: outputRange
                }));

                var scene = {
                  route: route,
                  focused: focused,
                  index: i
                };

                var label = _this2._renderLabel(scene);

                var icon = _this2.props.renderIcon ? _this2.props.renderIcon(scene) : null;
                var badge = _this2.props.renderBadge ? _this2.props.renderBadge(scene) : null;
                var tabStyle = {};
                tabStyle.opacity = opacity;

                if (icon) {
                  if (label) {
                    tabStyle.paddingTop = 8;
                  } else {
                    tabStyle.padding = 12;
                  }
                }

                var passedTabStyle = _reactNative.StyleSheet.flatten(_this2.props.tabStyle);

                var isWidthSet = passedTabStyle && typeof passedTabStyle.width !== 'undefined' || scrollEnabled === true;
                var tabContainerStyle = {};

                if (isWidthSet) {
                  tabStyle.width = tabWidth;
                }

                if (passedTabStyle && typeof passedTabStyle.flex === 'number') {
                  tabContainerStyle.flex = passedTabStyle.flex;
                } else if (!isWidthSet) {
                  tabContainerStyle.flex = 1;
                }

                var accessibilityLabel = route.accessibilityLabel || route.title;
                return React.createElement(
                  _TouchableItem2.default,
                  {
                    borderless: true,
                    key: route.key,
                    testID: route.testID,
                    accessible: route.accessible,
                    accessibilityLabel: accessibilityLabel,
                    accessibilityTraits: "button",
                    pressColor: _this2.props.pressColor,
                    pressOpacity: _this2.props.pressOpacity,
                    delayPressIn: 0,
                    onPress: function onPress() {
                      return _this2._handleTabPress(scene);
                    },
                    style: tabContainerStyle,
                    __source: {
                      fileName: _jsxFileName,
                      lineNumber: 452
                    }
                  },
                  React.createElement(
                    _reactNative.View,
                    {
                      pointerEvents: "none",
                      style: styles.container,
                      __source: {
                        fileName: _jsxFileName,
                        lineNumber: 465
                      }
                    },
                    React.createElement(
                      _reactNative.Animated.View,
                      {
                        style: [styles.tabItem, tabStyle, passedTabStyle, styles.container],
                        __source: {
                          fileName: _jsxFileName,
                          lineNumber: 466
                        }
                      },
                      icon,
                      label
                    ),
                    badge ? React.createElement(
                      _reactNative.Animated.View,
                      {
                        style: [styles.badge, {
                          opacity: _this2.state.visibility
                        }],
                        __source: {
                          fileName: _jsxFileName,
                          lineNumber: 478
                        }
                      },
                      badge
                    ) : null
                  )
                );
              })
            )
          )
        );
      }
    }]);
    return TabBar;
  }(React.Component);

  TabBar.propTypes = babelHelpers.extends({}, _TabViewPropTypes.SceneRendererPropType, {
    scrollEnabled: _propTypes2.default.bool,
    bounces: _propTypes2.default.bool,
    pressColor: _TouchableItem2.default.propTypes.pressColor,
    pressOpacity: _TouchableItem2.default.propTypes.pressOpacity,
    getLabelText: _propTypes2.default.func,
    renderIcon: _propTypes2.default.func,
    renderLabel: _propTypes2.default.func,
    renderIndicator: _propTypes2.default.func,
    onTabPress: _propTypes2.default.func,
    labelStyle: _propTypes2.default.any,
    style: _propTypes2.default.any
  });
  TabBar.defaultProps = {
    getLabelText: function getLabelText(_ref) {
      var route = _ref.route;
      return typeof route.title === 'string' ? route.title.toUpperCase() : route.title;
    }
  };

  var _initialiseProps = function _initialiseProps() {
    var _this3 = this;

    this._isIntial = true;
    this._isManualScroll = false;
    this._isMomentumScroll = false;

    this._startTrackingPosition = function () {
      _this3._offsetXListener = _this3.props.offsetX.addListener(function (_ref2) {
        var value = _ref2.value;
        _this3._lastOffsetX = value;

        _this3._handlePosition();
      });
      _this3._panXListener = _this3.props.panX.addListener(function (_ref3) {
        var value = _ref3.value;
        _this3._lastPanX = value;

        _this3._handlePosition();
      });
    };

    this._stopTrackingPosition = function () {
      _this3.props.offsetX.removeListener(_this3._offsetXListener);

      _this3.props.panX.removeListener(_this3._panXListener);
    };

    this._handlePosition = function () {
      var _props2 = _this3.props,
          navigationState = _props2.navigationState,
          layout = _props2.layout;

      if (layout.width === 0) {
        return;
      }

      var panX = typeof _this3._lastPanX === 'number' ? _this3._lastPanX : 0;
      var offsetX = typeof _this3._lastOffsetX === 'number' ? _this3._lastOffsetX : -navigationState.index * layout.width;
      var value = (panX + offsetX) / -(layout.width || 0.001);

      _this3._adjustScroll(value);
    };

    this._renderLabel = function (scene) {
      if (typeof _this3.props.renderLabel !== 'undefined') {
        return _this3.props.renderLabel(scene);
      }

      var label = _this3.props.getLabelText(scene);

      if (typeof label !== 'string') {
        return null;
      }

      return React.createElement(
        _reactNative.Animated.Text,
        {
          style: [styles.tabLabel, _this3.props.labelStyle],
          __source: {
            fileName: _jsxFileName,
            lineNumber: 187
          }
        },
        label
      );
    };

    this._renderIndicator = function (props) {
      if (typeof _this3.props.renderIndicator !== 'undefined') {
        return _this3.props.renderIndicator(props);
      }

      var width = props.width,
          position = props.position,
          navigationState = props.navigationState;

      var translateX = _reactNative.Animated.multiply(_reactNative.Animated.multiply(position.interpolate({
        inputRange: [0, navigationState.routes.length - 1],
        outputRange: [0, navigationState.routes.length - 1],
        extrapolate: 'clamp'
      }), width), _reactNative.I18nManager.isRTL ? -1 : 1);

      return React.createElement(_reactNative.Animated.View, {
        style: [styles.indicator, {
          width: width,
          transform: [{
            translateX: translateX
          }]
        }, _this3.props.indicatorStyle],
        __source: {
          fileName: _jsxFileName,
          lineNumber: 210
        }
      });
    };

    this._getTabWidth = function (props) {
      var layout = props.layout,
          navigationState = props.navigationState,
          tabStyle = props.tabStyle;

      var flattened = _reactNative.StyleSheet.flatten(tabStyle);

      if (flattened) {
        switch (typeof flattened.width) {
          case 'number':
            return flattened.width;

          case 'string':
            if (flattened.width.endsWith('%')) {
              var _width = parseFloat(flattened.width);

              if (Number.isFinite(_width)) {
                return layout.width * (_width / 100);
              }
            }

        }
      }

      if (props.scrollEnabled) {
        return layout.width / 5 * 2;
      }

      return layout.width / navigationState.routes.length;
    };

    this._handleTabPress = function (scene) {
      _this3._pendingIndex = scene.index;

      _this3.props.jumpTo(scene.route.key);

      if (_this3.props.onTabPress) {
        _this3.props.onTabPress(scene);
      }
    };

    this._normalizeScrollValue = function (props, value) {
      var layout = props.layout,
          navigationState = props.navigationState;

      var tabWidth = _this3._getTabWidth(props);

      var tabBarWidth = Math.max(tabWidth * navigationState.routes.length, layout.width);
      var maxDistance = tabBarWidth - layout.width;
      return Math.max(Math.min(value, maxDistance), 0);
    };

    this._getScrollAmount = function (props, i) {
      var layout = props.layout;

      var tabWidth = _this3._getTabWidth(props);

      var centerDistance = tabWidth * (i + 1 / 2);
      var scrollAmount = centerDistance - layout.width / 2;
      return _this3._normalizeScrollValue(props, scrollAmount);
    };

    this._adjustScroll = function (value) {
      if (_this3.props.scrollEnabled) {
        global.cancelAnimationFrame(_this3._scrollResetCallback);
        _this3._scrollView && _this3._scrollView.scrollTo({
          x: _this3._normalizeScrollValue(_this3.props, _this3._getScrollAmount(_this3.props, value)),
          animated: !_this3._isIntial
        });
        _this3._isIntial = false;
      }
    };

    this._resetScroll = function (value) {
      var animated = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : true;

      if (_this3.props.scrollEnabled) {
        global.cancelAnimationFrame(_this3._scrollResetCallback);
        _this3._scrollResetCallback = global.requestAnimationFrame(function () {
          _this3._scrollView && _this3._scrollView.scrollTo({
            x: _this3._getScrollAmount(_this3.props, value),
            animated: animated
          });
        });
      }
    };

    this._handleBeginDrag = function () {
      _this3._isManualScroll = true;
      _this3._isMomentumScroll = false;
    };

    this._handleEndDrag = function () {
      global.requestAnimationFrame(function () {
        if (_this3._isMomentumScroll) {
          return;
        }

        _this3._isManualScroll = false;
      });
    };

    this._handleMomentumScrollBegin = function () {
      _this3._isMomentumScroll = true;
    };

    this._handleMomentumScrollEnd = function () {
      _this3._isMomentumScroll = false;
      _this3._isManualScroll = false;
    };

    this._setRef = function (el) {
      return _this3._scrollView = el && el._component;
    };
  };

  exports.default = TabBar;

  var styles = _reactNative.StyleSheet.create({
    container: {
      flex: 1
    },
    scroll: {
      overflow: _reactNative.Platform.OS === 'web' ? 'auto' : 'scroll'
    },
    tabBar: {
      backgroundColor: '#2196f3',
      elevation: 4,
      shadowColor: 'black',
      shadowOpacity: 0.1,
      shadowRadius: _reactNative.StyleSheet.hairlineWidth,
      shadowOffset: {
        height: _reactNative.StyleSheet.hairlineWidth
      },
      zIndex: _reactNative.Platform.OS === 'android' ? 0 : 1
    },
    tabContent: {
      flexDirection: 'row',
      flexWrap: 'nowrap'
    },
    tabLabel: {
      backgroundColor: 'transparent',
      color: 'white',
      margin: 8
    },
    tabItem: {
      flex: 1,
      padding: 8,
      alignItems: 'center',
      justifyContent: 'center'
    },
    badge: {
      position: 'absolute',
      top: 0,
      right: 0
    },
    indicatorContainer: {
      position: 'absolute',
      top: 0,
      left: 0,
      right: 0,
      bottom: 0
    },
    indicator: {
      backgroundColor: '#ffeb3b',
      position: 'absolute',
      left: 0,
      bottom: 0,
      right: 0,
      height: 2
    }
  });
},"99387fc75e0caf95a9ac0dfd076c5f0a",["c42a5e17831e80ed1e1c8cf91f5ddb40","18eeaf4e01377a466daaccc6ba8ce6f5","cc757a791ecb3cd320f65c256a791c04","732e863282a76b408bdb4a9618f2f38f","703f6c0239fe76a156bf7b78cbf1643c"],"node_modules/react-navigation/node_modules/react-native-tab-view/src/TabBar.js");