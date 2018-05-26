__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-navigation/node_modules/react-navigation-tabs/node_modules/react-native-tab-view/src/TabViewPagerExperimental.js";

  var _react = _require(_dependencyMap[0], "react");

  var React = babelHelpers.interopRequireWildcard(_react);

  var _propTypes = _require(_dependencyMap[1], "prop-types");

  var _propTypes2 = babelHelpers.interopRequireDefault(_propTypes);

  var _reactNative = _require(_dependencyMap[2], "react-native");

  var _TabViewPropTypes = _require(_dependencyMap[3], "./TabViewPropTypes");

  var DefaultTransitionSpec = {
    timing: _reactNative.Animated.spring,
    tension: 75,
    friction: 25
  };

  var TabViewPagerExperimental = function (_React$Component) {
    babelHelpers.inherits(TabViewPagerExperimental, _React$Component);

    function TabViewPagerExperimental() {
      var _ref;

      var _temp, _this, _ret;

      babelHelpers.classCallCheck(this, TabViewPagerExperimental);

      for (var _len = arguments.length, args = Array(_len), _key = 0; _key < _len; _key++) {
        args[_key] = arguments[_key];
      }

      return _ret = (_temp = (_this = babelHelpers.possibleConstructorReturn(this, (_ref = TabViewPagerExperimental.__proto__ || Object.getPrototypeOf(TabViewPagerExperimental)).call.apply(_ref, [this].concat(args))), _this), _this._handleHandlerStateChange = function (event) {
        var GestureHandler = _this.props.GestureHandler;

        if (event.nativeEvent.state === GestureHandler.State.BEGIN) {
          _this.props.onSwipeStart && _this.props.onSwipeStart();
        } else if (event.nativeEvent.state === GestureHandler.State.END) {
          _this.props.onSwipeEnd && _this.props.onSwipeEnd();

          var _this$props = _this.props,
              navigationState = _this$props.navigationState,
              layout = _this$props.layout,
              _this$props$swipeDist = _this$props.swipeDistanceThreshold,
              _swipeDistanceThreshold = _this$props$swipeDist === undefined ? layout.width / 1.75 : _this$props$swipeDist,
              _this$props$swipeVelo = _this$props.swipeVelocityThreshold,
              _swipeVelocityThreshold = _this$props$swipeVelo === undefined ? 150 : _this$props$swipeVelo;

          var _event$nativeEvent = event.nativeEvent,
              translationX = _event$nativeEvent.translationX,
              translationY = _event$nativeEvent.translationY,
              velocityX = _event$nativeEvent.velocityX,
              velocityY = _event$nativeEvent.velocityY;
          var currentIndex = typeof _this._pendingIndex === 'number' ? _this._pendingIndex : navigationState.index;
          var nextIndex = currentIndex;

          if (Math.abs(translationX) > Math.abs(translationY) && Math.abs(velocityX) > Math.abs(velocityY) && (Math.abs(translationX) > _swipeDistanceThreshold || Math.abs(velocityX) > _swipeVelocityThreshold)) {
            nextIndex = Math.round(Math.min(Math.max(0, currentIndex - translationX / Math.abs(translationX)), navigationState.routes.length - 1));
          }

          if (!isFinite(nextIndex) || !_this.props.canJumpToTab(_this.props.navigationState.routes[nextIndex])) {
            nextIndex = currentIndex;
          }

          _this._transitionTo(nextIndex, velocityX);
        }
      }, _this._transitionTo = function (index, velocity) {
        var animated = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : true;
        var offset = -index * _this.props.layout.width;

        if (_this.props.animationEnabled === false || animated === false) {
          _this.props.panX.setValue(0);

          _this.props.offsetX.setValue(offset);

          return;
        }

        var timing = DefaultTransitionSpec.timing,
            transitionConfig = babelHelpers.objectWithoutProperties(DefaultTransitionSpec, ["timing"]);
        var useNativeDriver = _this.props.useNativeDriver;

        _reactNative.Animated.parallel([timing(_this.props.panX, babelHelpers.extends({}, transitionConfig, {
          toValue: 0,
          velocity: velocity,
          useNativeDriver: useNativeDriver
        })), timing(_this.props.offsetX, babelHelpers.extends({}, transitionConfig, {
          toValue: offset,
          velocity: velocity,
          useNativeDriver: useNativeDriver
        }))]).start(function (_ref2) {
          var finished = _ref2.finished;

          if (finished) {
            var route = _this.props.navigationState.routes[index];

            _this.props.jumpTo(route.key);

            _this.props.onAnimationEnd && _this.props.onAnimationEnd();
            _this._pendingIndex = null;
          }
        });

        _this._pendingIndex = index;
      }, _temp), babelHelpers.possibleConstructorReturn(_this, _ret);
    }

    babelHelpers.createClass(TabViewPagerExperimental, [{
      key: "componentDidUpdate",
      value: function componentDidUpdate(prevProps) {
        if (prevProps.navigationState.routes !== this.props.navigationState.routes || prevProps.layout.width !== this.props.layout.width) {
          this._transitionTo(this.props.navigationState.index, undefined, false);
        } else if (prevProps.navigationState.index !== this.props.navigationState.index) {
          this._transitionTo(this.props.navigationState.index);
        }
      }
    }, {
      key: "render",
      value: function render() {
        var _props = this.props,
            GestureHandler = _props.GestureHandler,
            panX = _props.panX,
            offsetX = _props.offsetX,
            layout = _props.layout,
            navigationState = _props.navigationState,
            swipeEnabled = _props.swipeEnabled,
            children = _props.children;
        var width = layout.width;
        var routes = navigationState.routes;
        var maxTranslate = width * (routes.length - 1);

        var translateX = _reactNative.Animated.add(panX, offsetX).interpolate({
          inputRange: [-maxTranslate, 0],
          outputRange: [-maxTranslate, 0],
          extrapolate: 'clamp'
        });

        return React.createElement(
          GestureHandler.PanGestureHandler,
          {
            enabled: layout.width !== 0 && swipeEnabled !== false,
            minDeltaX: 10,
            onGestureEvent: _reactNative.Animated.event([{
              nativeEvent: {
                translationX: this.props.panX
              }
            }], {
              useNativeDriver: this.props.useNativeDriver
            }),
            onHandlerStateChange: this._handleHandlerStateChange,
            __source: {
              fileName: _jsxFileName,
              lineNumber: 167
            }
          },
          React.createElement(
            _reactNative.Animated.View,
            {
              style: [styles.sheet, width ? {
                width: routes.length * width,
                transform: [{
                  translateX: translateX
                }]
              } : null],
              __source: {
                fileName: _jsxFileName,
                lineNumber: 176
              }
            },
            React.Children.map(children, function (child, i) {
              return React.createElement(
                _reactNative.View,
                {
                  key: navigationState.routes[i].key,
                  testID: navigationState.routes[i].testID,
                  style: width ? {
                    width: width
                  } : i === navigationState.index ? _reactNative.StyleSheet.absoluteFill : null,
                  __source: {
                    fileName: _jsxFileName,
                    lineNumber: 185
                  }
                },
                i === navigationState.index || width ? child : null
              );
            })
          )
        );
      }
    }]);
    return TabViewPagerExperimental;
  }(React.Component);

  TabViewPagerExperimental.propTypes = babelHelpers.extends({}, _TabViewPropTypes.PagerRendererPropType, {
    swipeDistanceThreshold: _propTypes2.default.number,
    swipeVelocityThreshold: _propTypes2.default.number,
    GestureHandler: _propTypes2.default.object
  });
  TabViewPagerExperimental.defaultProps = {
    GestureHandler: global.__expo && global.__expo.DangerZone ? global.__expo.DangerZone.GestureHandler : undefined,
    canJumpToTab: function canJumpToTab() {
      return true;
    }
  };
  exports.default = TabViewPagerExperimental;

  var styles = _reactNative.StyleSheet.create({
    sheet: {
      flex: 1,
      flexDirection: 'row',
      alignItems: 'stretch'
    }
  });
},433,[12,129,22,429],"node_modules/react-navigation/node_modules/react-navigation-tabs/node_modules/react-native-tab-view/src/TabViewPagerExperimental.js");