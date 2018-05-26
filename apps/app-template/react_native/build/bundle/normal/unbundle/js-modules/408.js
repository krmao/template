__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-navigation/node_modules/react-native-tab-view/src/TabViewPagerPan.js";

  var _react = _require(_dependencyMap[0], "react");

  var React = babelHelpers.interopRequireWildcard(_react);

  var _propTypes = _require(_dependencyMap[1], "prop-types");

  var _propTypes2 = babelHelpers.interopRequireDefault(_propTypes);

  var _reactNative = _require(_dependencyMap[2], "react-native");

  var _TabViewPropTypes = _require(_dependencyMap[3], "./TabViewPropTypes");

  var DEAD_ZONE = 12;
  var DefaultTransitionSpec = {
    timing: _reactNative.Animated.spring,
    tension: 300,
    friction: 35
  };

  var TabViewPagerPan = function (_React$Component) {
    babelHelpers.inherits(TabViewPagerPan, _React$Component);

    function TabViewPagerPan(props) {
      babelHelpers.classCallCheck(this, TabViewPagerPan);

      var _this = babelHelpers.possibleConstructorReturn(this, (TabViewPagerPan.__proto__ || Object.getPrototypeOf(TabViewPagerPan)).call(this, props));

      _this._isMovingHorizontally = function (evt, gestureState) {
        return Math.abs(gestureState.dx) > Math.abs(gestureState.dy * 2) && Math.abs(gestureState.vx) > Math.abs(gestureState.vy * 2);
      };

      _this._canMoveScreen = function (evt, gestureState) {
        if (_this.props.swipeEnabled === false) {
          return false;
        }

        var routes = _this.props.navigationState.routes;
        return _this._isMovingHorizontally(evt, gestureState) && (gestureState.dx >= DEAD_ZONE && _this._currentIndex > 0 || gestureState.dx <= -DEAD_ZONE && _this._currentIndex < routes.length - 1);
      };

      _this._startGesture = function () {
        _this.props.onSwipeStart && _this.props.onSwipeStart();

        _this.props.panX.stopAnimation();
      };

      _this._respondToGesture = function (evt, gestureState) {
        var _this$props$navigatio = _this.props.navigationState,
            routes = _this$props$navigatio.routes,
            index = _this$props$navigatio.index;

        if (gestureState.dx > 0 && index <= 0 || gestureState.dx < 0 && index >= routes.length - 1) {
          return;
        }

        _this.props.panX.setValue(gestureState.dx);
      };

      _this._finishGesture = function (evt, gestureState) {
        var _this$props = _this.props,
            navigationState = _this$props.navigationState,
            layout = _this$props.layout,
            _this$props$swipeDist = _this$props.swipeDistanceThreshold,
            swipeDistanceThreshold = _this$props$swipeDist === undefined ? layout.width / 1.75 : _this$props$swipeDist;
        var _this$props$swipeVelo = _this.props.swipeVelocityThreshold,
            swipeVelocityThreshold = _this$props$swipeVelo === undefined ? 0.15 : _this$props$swipeVelo;
        _this.props.onSwipeEnd && _this.props.onSwipeEnd();

        if (_reactNative.Platform.OS === 'android') {
          swipeVelocityThreshold /= 1000000;
        }

        var currentIndex = typeof _this._pendingIndex === 'number' ? _this._pendingIndex : _this._currentIndex;
        var nextIndex = currentIndex;

        if (Math.abs(gestureState.dx) > Math.abs(gestureState.dy) && Math.abs(gestureState.vx) > Math.abs(gestureState.vy) && (Math.abs(gestureState.dx) > swipeDistanceThreshold || Math.abs(gestureState.vx) > swipeVelocityThreshold)) {
          nextIndex = Math.round(Math.min(Math.max(0, currentIndex - gestureState.dx / Math.abs(gestureState.dx)), navigationState.routes.length - 1));
          _this._currentIndex = nextIndex;
        }

        if (!isFinite(nextIndex) || !_this.props.canJumpToTab(_this.props.navigationState.routes[nextIndex])) {
          nextIndex = currentIndex;
        }

        _this._transitionTo(nextIndex);
      };

      _this._transitionTo = function (index) {
        var animated = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : true;
        var offset = -index * _this.props.layout.width;

        if (_this.props.animationEnabled === false || animated === false) {
          _this.props.panX.setValue(0);

          _this.props.offsetX.setValue(offset);

          return;
        }

        var timing = DefaultTransitionSpec.timing,
            transitionConfig = babelHelpers.objectWithoutProperties(DefaultTransitionSpec, ["timing"]);

        _reactNative.Animated.parallel([timing(_this.props.panX, babelHelpers.extends({}, transitionConfig, {
          toValue: 0
        })), timing(_this.props.offsetX, babelHelpers.extends({}, transitionConfig, {
          toValue: offset
        }))]).start(function (_ref) {
          var finished = _ref.finished;

          if (finished) {
            var route = _this.props.navigationState.routes[index];

            _this.props.jumpTo(route.key);

            _this.props.onAnimationEnd && _this.props.onAnimationEnd();
            _this._pendingIndex = null;
          }
        });

        _this._pendingIndex = index;
      };

      _this._currentIndex = _this.props.navigationState.index;
      return _this;
    }

    babelHelpers.createClass(TabViewPagerPan, [{
      key: "componentWillMount",
      value: function componentWillMount() {
        this._panResponder = _reactNative.PanResponder.create({
          onMoveShouldSetPanResponder: this._canMoveScreen,
          onMoveShouldSetPanResponderCapture: this._canMoveScreen,
          onPanResponderGrant: this._startGesture,
          onPanResponderMove: this._respondToGesture,
          onPanResponderTerminate: this._finishGesture,
          onPanResponderRelease: this._finishGesture,
          onPanResponderTerminationRequest: function onPanResponderTerminationRequest() {
            return true;
          }
        });
      }
    }, {
      key: "componentDidUpdate",
      value: function componentDidUpdate(prevProps) {
        this._currentIndex = this.props.navigationState.index;

        if (prevProps.navigationState.routes !== this.props.navigationState.routes || prevProps.layout.width !== this.props.layout.width) {
          this._transitionTo(this.props.navigationState.index, false);
        } else if (prevProps.navigationState.index !== this.props.navigationState.index) {
          this._transitionTo(this.props.navigationState.index);
        }
      }
    }, {
      key: "render",
      value: function render() {
        var _props = this.props,
            panX = _props.panX,
            offsetX = _props.offsetX,
            navigationState = _props.navigationState,
            layout = _props.layout,
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
          _reactNative.Animated.View,
          babelHelpers.extends({
            style: [styles.sheet, width ? {
              width: routes.length * width,
              transform: [{
                translateX: translateX
              }]
            } : null]
          }, this._panResponder.panHandlers, {
            __source: {
              fileName: _jsxFileName,
              lineNumber: 249
            }
          }),
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
                  lineNumber: 262
                }
              },
              i === navigationState.index || width ? child : null
            );
          })
        );
      }
    }]);
    return TabViewPagerPan;
  }(React.Component);

  TabViewPagerPan.propTypes = babelHelpers.extends({}, _TabViewPropTypes.PagerRendererPropType, {
    swipeDistanceThreshold: _propTypes2.default.number,
    swipeVelocityThreshold: _propTypes2.default.number
  });
  TabViewPagerPan.defaultProps = {
    canJumpToTab: function canJumpToTab() {
      return true;
    },
    initialLayout: {
      height: 0,
      width: 0
    }
  };
  exports.default = TabViewPagerPan;

  var styles = _reactNative.StyleSheet.create({
    sheet: {
      flex: 1,
      flexDirection: 'row',
      alignItems: 'stretch'
    }
  });
},408,[12,129,22,405],"node_modules/react-navigation/node_modules/react-native-tab-view/src/TabViewPagerPan.js");