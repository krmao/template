__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-navigation/src/views/StackView/StackViewLayout.js";

  var _react = _require(_dependencyMap[0], "react");

  var React = babelHelpers.interopRequireWildcard(_react);

  var _clamp = _require(_dependencyMap[1], "clamp");

  var _clamp2 = babelHelpers.interopRequireDefault(_clamp);

  var _reactNative = _require(_dependencyMap[2], "react-native");

  var _StackViewCard = _require(_dependencyMap[3], "./StackViewCard");

  var _StackViewCard2 = babelHelpers.interopRequireDefault(_StackViewCard);

  var _Header = _require(_dependencyMap[4], "../Header/Header");

  var _Header2 = babelHelpers.interopRequireDefault(_Header);

  var _NavigationActions = _require(_dependencyMap[5], "../../NavigationActions");

  var _NavigationActions2 = babelHelpers.interopRequireDefault(_NavigationActions);

  var _StackActions = _require(_dependencyMap[6], "../../routers/StackActions");

  var _StackActions2 = babelHelpers.interopRequireDefault(_StackActions);

  var _SceneView = _require(_dependencyMap[7], "../SceneView");

  var _SceneView2 = babelHelpers.interopRequireDefault(_SceneView);

  var _withOrientation = _require(_dependencyMap[8], "../withOrientation");

  var _withOrientation2 = babelHelpers.interopRequireDefault(_withOrientation);

  var _NavigationContext = _require(_dependencyMap[9], "../NavigationContext");

  var _StackViewTransitionConfigs = _require(_dependencyMap[10], "./StackViewTransitionConfigs");

  var _StackViewTransitionConfigs2 = babelHelpers.interopRequireDefault(_StackViewTransitionConfigs);

  var _ReactNativeFeatures = _require(_dependencyMap[11], "../../utils/ReactNativeFeatures");

  var ReactNativeFeatures = babelHelpers.interopRequireWildcard(_ReactNativeFeatures);

  var emptyFunction = function emptyFunction() {};

  var _Dimensions$get = _reactNative.Dimensions.get('window'),
      WINDOW_WIDTH = _Dimensions$get.width,
      WINDOW_HEIGHT = _Dimensions$get.height;

  var IS_IPHONE_X = _reactNative.Platform.OS === 'ios' && !_reactNative.Platform.isPad && !_reactNative.Platform.isTVOS && (WINDOW_HEIGHT === 812 || WINDOW_WIDTH === 812);

  var EaseInOut = _reactNative.Easing.inOut(_reactNative.Easing.ease);

  var ANIMATION_DURATION = 500;
  var POSITION_THRESHOLD = 1 / 2;
  var RESPOND_THRESHOLD = 20;
  var GESTURE_RESPONSE_DISTANCE_HORIZONTAL = 25;
  var GESTURE_RESPONSE_DISTANCE_VERTICAL = 135;

  var animatedSubscribeValue = function animatedSubscribeValue(animatedValue) {
    if (!animatedValue.__isNative) {
      return;
    }

    if (Object.keys(animatedValue._listeners).length === 0) {
      animatedValue.addListener(emptyFunction);
    }
  };

  var StackViewLayout = function (_React$Component) {
    babelHelpers.inherits(StackViewLayout, _React$Component);

    function StackViewLayout() {
      var _ref;

      var _temp, _this, _ret;

      babelHelpers.classCallCheck(this, StackViewLayout);

      for (var _len = arguments.length, args = Array(_len), _key = 0; _key < _len; _key++) {
        args[_key] = arguments[_key];
      }

      return _ret = (_temp = (_this = babelHelpers.possibleConstructorReturn(this, (_ref = StackViewLayout.__proto__ || Object.getPrototypeOf(StackViewLayout)).call.apply(_ref, [this].concat(args))), _this), _this._gestureStartValue = 0, _this._isResponding = false, _this._immediateIndex = null, _this._panResponder = _reactNative.PanResponder.create({
        onPanResponderTerminate: function onPanResponderTerminate() {
          _this._isResponding = false;

          _this._reset(index, 0);

          _this.props.onGestureCanceled && _this.props.onGestureCanceled();
        },
        onPanResponderGrant: function onPanResponderGrant() {
          var _this$props$transitio = _this.props.transitionProps,
              navigation = _this$props$transitio.navigation,
              position = _this$props$transitio.position,
              scene = _this$props$transitio.scene;
          var index = navigation.state.index;

          if (index !== scene.index) {
            return false;
          }

          position.stopAnimation(function (value) {
            _this._isResponding = true;
            _this._gestureStartValue = value;
          });
          _this.props.onGestureBegin && _this.props.onGestureBegin();
        },
        onMoveShouldSetPanResponder: function onMoveShouldSetPanResponder(event, gesture) {
          var _this$props = _this.props,
              _this$props$transitio2 = _this$props.transitionProps,
              navigation = _this$props$transitio2.navigation,
              position = _this$props$transitio2.position,
              layout = _this$props$transitio2.layout,
              scene = _this$props$transitio2.scene,
              scenes = _this$props$transitio2.scenes,
              mode = _this$props.mode;
          var index = navigation.state.index;
          var isVertical = mode === 'modal';
          var options = scene.descriptor.options;
          var gestureDirection = options.gestureDirection;
          var gestureDirectionInverted = typeof gestureDirection === 'string' ? gestureDirection === 'inverted' : _reactNative.I18nManager.isRTL;

          if (index !== scene.index) {
            return false;
          }

          var immediateIndex = _this._immediateIndex == null ? index : _this._immediateIndex;
          var currentDragDistance = gesture[isVertical ? 'dy' : 'dx'];
          var currentDragPosition = event.nativeEvent[isVertical ? 'pageY' : 'pageX'];
          var axisLength = isVertical ? layout.height.__getValue() : layout.width.__getValue();
          var axisHasBeenMeasured = !!axisLength;
          var screenEdgeDistance = gestureDirectionInverted ? axisLength - (currentDragPosition - currentDragDistance) : currentDragPosition - currentDragDistance;
          var _options$gestureRespo = options.gestureResponseDistance,
              userGestureResponseDistance = _options$gestureRespo === undefined ? {} : _options$gestureRespo;
          var gestureResponseDistance = isVertical ? userGestureResponseDistance.vertical || GESTURE_RESPONSE_DISTANCE_VERTICAL : userGestureResponseDistance.horizontal || GESTURE_RESPONSE_DISTANCE_HORIZONTAL;

          if (screenEdgeDistance > gestureResponseDistance) {
            return false;
          }

          var hasDraggedEnough = Math.abs(currentDragDistance) > RESPOND_THRESHOLD;
          var isOnFirstCard = immediateIndex === 0;
          var shouldSetResponder = hasDraggedEnough && axisHasBeenMeasured && !isOnFirstCard;
          return shouldSetResponder;
        },
        onPanResponderMove: function onPanResponderMove(event, gesture) {
          var _this$props2 = _this.props,
              _this$props2$transiti = _this$props2.transitionProps,
              navigation = _this$props2$transiti.navigation,
              position = _this$props2$transiti.position,
              layout = _this$props2$transiti.layout,
              scene = _this$props2$transiti.scene,
              mode = _this$props2.mode;
          var index = navigation.state.index;
          var isVertical = mode === 'modal';
          var options = scene.descriptor.options;
          var gestureDirection = options.gestureDirection;
          var gestureDirectionInverted = typeof gestureDirection === 'string' ? gestureDirection === 'inverted' : _reactNative.I18nManager.isRTL;
          var startValue = _this._gestureStartValue;
          var axis = isVertical ? 'dy' : 'dx';
          var axisDistance = isVertical ? layout.height.__getValue() : layout.width.__getValue();
          var currentValue = axis === 'dx' && gestureDirectionInverted ? startValue + gesture[axis] / axisDistance : startValue - gesture[axis] / axisDistance;
          var value = (0, _clamp2.default)(index - 1, currentValue, index);
          position.setValue(value);
        },
        onPanResponderTerminationRequest: function onPanResponderTerminationRequest() {
          return false;
        },
        onPanResponderRelease: function onPanResponderRelease(event, gesture) {
          var _this$props3 = _this.props,
              _this$props3$transiti = _this$props3.transitionProps,
              navigation = _this$props3$transiti.navigation,
              position = _this$props3$transiti.position,
              layout = _this$props3$transiti.layout,
              scene = _this$props3$transiti.scene,
              mode = _this$props3.mode;
          var index = navigation.state.index;
          var isVertical = mode === 'modal';
          var options = scene.descriptor.options;
          var gestureDirection = options.gestureDirection;
          var gestureDirectionInverted = typeof gestureDirection === 'string' ? gestureDirection === 'inverted' : _reactNative.I18nManager.isRTL;

          if (!_this._isResponding) {
            return;
          }

          _this._isResponding = false;
          var immediateIndex = _this._immediateIndex == null ? index : _this._immediateIndex;
          var axisDistance = isVertical ? layout.height.__getValue() : layout.width.__getValue();
          var movementDirection = gestureDirectionInverted ? -1 : 1;
          var movedDistance = movementDirection * gesture[isVertical ? 'dy' : 'dx'];
          var gestureVelocity = movementDirection * gesture[isVertical ? 'vy' : 'vx'];
          var defaultVelocity = axisDistance / ANIMATION_DURATION;
          var velocity = Math.max(Math.abs(gestureVelocity), defaultVelocity);
          var resetDuration = gestureDirectionInverted ? (axisDistance - movedDistance) / velocity : movedDistance / velocity;
          var goBackDuration = gestureDirectionInverted ? movedDistance / velocity : (axisDistance - movedDistance) / velocity;
          position.stopAnimation(function (value) {
            if (gestureVelocity < -0.5) {
              _this.props.onGestureCanceled && _this.props.onGestureCanceled();

              _this._reset(immediateIndex, resetDuration);

              return;
            }

            if (gestureVelocity > 0.5) {
              _this.props.onGestureFinish && _this.props.onGestureFinish();

              _this._goBack(immediateIndex, goBackDuration);

              return;
            }

            if (value <= index - POSITION_THRESHOLD) {
              _this.props.onGestureFinish && _this.props.onGestureFinish();

              _this._goBack(immediateIndex, goBackDuration);
            } else {
              _this.props.onGestureCanceled && _this.props.onGestureCanceled();

              _this._reset(immediateIndex, resetDuration);
            }
          });
        }
      }), _this._getTransitionConfig = function () {
        var isModal = _this.props.mode === 'modal';
        return _StackViewTransitionConfigs2.default.getTransitionConfig(_this.props.transitionConfig, _this.props.transitionProps, _this.props.prevTransitionProps, isModal);
      }, _this._renderCard = function (scene) {
        var _this$_getTransitionC = _this._getTransitionConfig(),
            screenInterpolator = _this$_getTransitionC.screenInterpolator;

        var style = screenInterpolator && screenInterpolator(babelHelpers.extends({}, _this.props.transitionProps, {
          scene: scene
        }));
        var options = scene.descriptor.options;
        var hasHeader = options.header !== null;

        var headerMode = _this._getHeaderMode();

        var marginTop = 0;

        if (!hasHeader && headerMode === 'float') {
          var isLandscape = _this.props.isLandscape;
          var headerHeight = void 0;

          if (_reactNative.Platform.OS === 'ios') {
            if (isLandscape && !_reactNative.Platform.isPad) {
              headerHeight = 52;
            } else if (IS_IPHONE_X) {
              headerHeight = 88;
            } else {
              headerHeight = 64;
            }
          } else {
            headerHeight = 56;
          }

          marginTop = -headerHeight;
        }

        return React.createElement(
          _StackViewCard2.default,
          babelHelpers.extends({}, _this.props.transitionProps, {
            key: "card_" + scene.key,
            style: [style, {
              marginTop: marginTop
            }, _this.props.cardStyle],
            scene: scene,
            __source: {
              fileName: _jsxFileName,
              lineNumber: 537
            }
          }),
          _this._renderInnerScene(scene)
        );
      }, _temp), babelHelpers.possibleConstructorReturn(_this, _ret);
    }

    babelHelpers.createClass(StackViewLayout, [{
      key: "_renderHeader",
      value: function _renderHeader(scene, headerMode) {
        var options = scene.descriptor.options;
        var header = options.header;

        if (header === null && headerMode === 'screen') {
          return null;
        }

        if (React.isValidElement(header)) {
          return header;
        }

        var renderHeader = header || function (props) {
          return React.createElement(_Header2.default, babelHelpers.extends({}, props, {
            __source: {
              fileName: _jsxFileName,
              lineNumber: 106
            }
          }));
        };

        var _getTransitionConfig = this._getTransitionConfig(),
            headerLeftInterpolator = _getTransitionConfig.headerLeftInterpolator,
            headerTitleInterpolator = _getTransitionConfig.headerTitleInterpolator,
            headerRightInterpolator = _getTransitionConfig.headerRightInterpolator;

        var _props = this.props,
            mode = _props.mode,
            transitionProps = _props.transitionProps,
            prevTransitionProps = _props.prevTransitionProps,
            passProps = babelHelpers.objectWithoutProperties(_props, ["mode", "transitionProps", "prevTransitionProps"]);
        return renderHeader(babelHelpers.extends({}, passProps, transitionProps, {
          scene: scene,
          mode: headerMode,
          transitionPreset: this._getHeaderTransitionPreset(),
          leftInterpolator: headerLeftInterpolator,
          titleInterpolator: headerTitleInterpolator,
          rightInterpolator: headerRightInterpolator
        }));
      }
    }, {
      key: "_animatedSubscribe",
      value: function _animatedSubscribe(props) {
        animatedSubscribeValue(props.transitionProps.layout.width);
        animatedSubscribeValue(props.transitionProps.layout.height);
        animatedSubscribeValue(props.transitionProps.position);
      }
    }, {
      key: "_reset",
      value: function _reset(resetToIndex, duration) {
        if (_reactNative.Platform.OS === 'ios' && ReactNativeFeatures.supportsImprovedSpringAnimation()) {
          _reactNative.Animated.spring(this.props.transitionProps.position, {
            toValue: resetToIndex,
            stiffness: 5000,
            damping: 600,
            mass: 3,
            useNativeDriver: this.props.transitionProps.position.__isNative
          }).start();
        } else {
          _reactNative.Animated.timing(this.props.transitionProps.position, {
            toValue: resetToIndex,
            duration: duration,
            easing: EaseInOut,
            useNativeDriver: this.props.transitionProps.position.__isNative
          }).start();
        }
      }
    }, {
      key: "_goBack",
      value: function _goBack(backFromIndex, duration) {
        var _this2 = this;

        var _props$transitionProp = this.props.transitionProps,
            navigation = _props$transitionProp.navigation,
            position = _props$transitionProp.position,
            scenes = _props$transitionProp.scenes;
        var toValue = Math.max(backFromIndex - 1, 0);
        this._immediateIndex = toValue;

        var onCompleteAnimation = function onCompleteAnimation() {
          _this2._immediateIndex = null;
          var backFromScene = scenes.find(function (s) {
            return s.index === toValue + 1;
          });

          if (!_this2._isResponding && backFromScene) {
            navigation.dispatch(_NavigationActions2.default.back({
              key: backFromScene.route.key,
              immediate: true
            }));
            navigation.dispatch(_StackActions2.default.completeTransition());
          }
        };

        if (_reactNative.Platform.OS === 'ios' && ReactNativeFeatures.supportsImprovedSpringAnimation()) {
          _reactNative.Animated.spring(position, {
            toValue: toValue,
            stiffness: 5000,
            damping: 600,
            mass: 3,
            useNativeDriver: position.__isNative
          }).start(onCompleteAnimation);
        } else {
          _reactNative.Animated.timing(position, {
            toValue: toValue,
            duration: duration,
            easing: EaseInOut,
            useNativeDriver: position.__isNative
          }).start(onCompleteAnimation);
        }
      }
    }, {
      key: "render",
      value: function render() {
        var _this3 = this;

        var floatingHeader = null;

        var headerMode = this._getHeaderMode();

        if (headerMode === 'float') {
          var _scene = this.props.transitionProps.scene;
          floatingHeader = React.createElement(
            _NavigationContext.NavigationProvider,
            {
              value: _scene.descriptor.navigation,
              __source: {
                fileName: _jsxFileName,
                lineNumber: 398
              }
            },
            this._renderHeader(_scene, headerMode)
          );
        }

        var _props2 = this.props,
            _props2$transitionPro = _props2.transitionProps,
            navigation = _props2$transitionPro.navigation,
            position = _props2$transitionPro.position,
            layout = _props2$transitionPro.layout,
            scene = _props2$transitionPro.scene,
            scenes = _props2$transitionPro.scenes,
            mode = _props2.mode;
        var index = navigation.state.index;
        var isVertical = mode === 'modal';
        var options = scene.descriptor.options;
        var gestureDirection = options.gestureDirection;
        var gestureDirectionInverted = typeof gestureDirection === 'string' ? gestureDirection === 'inverted' : _reactNative.I18nManager.isRTL;
        var gesturesEnabled = typeof options.gesturesEnabled === 'boolean' ? options.gesturesEnabled : _reactNative.Platform.OS === 'ios';
        var responder = !gesturesEnabled ? null : this._panResponder;
        var handlers = gesturesEnabled ? responder.panHandlers : {};
        var containerStyle = [styles.container, this._getTransitionConfig().containerStyle];
        return React.createElement(
          _reactNative.View,
          babelHelpers.extends({}, handlers, {
            style: containerStyle,
            __source: {
              fileName: _jsxFileName,
              lineNumber: 431
            }
          }),
          React.createElement(
            _reactNative.View,
            {
              style: styles.scenes,
              __source: {
                fileName: _jsxFileName,
                lineNumber: 432
              }
            },
            scenes.map(function (s) {
              return _this3._renderCard(s);
            })
          ),
          floatingHeader
        );
      }
    }, {
      key: "_getHeaderMode",
      value: function _getHeaderMode() {
        if (this.props.headerMode) {
          return this.props.headerMode;
        }

        if (_reactNative.Platform.OS === 'android' || this.props.mode === 'modal') {
          return 'screen';
        }

        return 'float';
      }
    }, {
      key: "_getHeaderTransitionPreset",
      value: function _getHeaderTransitionPreset() {
        if (_reactNative.Platform.OS === 'android' || this._getHeaderMode() === 'screen') {
          return 'fade-in-place';
        }

        if (this.props.headerTransitionPreset) {
          return this.props.headerTransitionPreset;
        } else {
          return 'fade-in-place';
        }
      }
    }, {
      key: "_renderInnerScene",
      value: function _renderInnerScene(scene) {
        var _scene$descriptor = scene.descriptor,
            options = _scene$descriptor.options,
            navigation = _scene$descriptor.navigation,
            getComponent = _scene$descriptor.getComponent;
        var SceneComponent = getComponent();
        var screenProps = this.props.screenProps;

        var headerMode = this._getHeaderMode();

        if (headerMode === 'screen') {
          return React.createElement(
            _reactNative.View,
            {
              style: styles.container,
              __source: {
                fileName: _jsxFileName,
                lineNumber: 473
              }
            },
            React.createElement(
              _reactNative.View,
              {
                style: styles.scenes,
                __source: {
                  fileName: _jsxFileName,
                  lineNumber: 474
                }
              },
              React.createElement(_SceneView2.default, {
                screenProps: screenProps,
                navigation: navigation,
                component: SceneComponent,
                __source: {
                  fileName: _jsxFileName,
                  lineNumber: 475
                }
              })
            ),
            this._renderHeader(scene, headerMode)
          );
        }

        return React.createElement(_SceneView2.default, {
          screenProps: screenProps,
          navigation: navigation,
          component: SceneComponent,
          __source: {
            fileName: _jsxFileName,
            lineNumber: 486
          }
        });
      }
    }]);
    return StackViewLayout;
  }(React.Component);

  var styles = _reactNative.StyleSheet.create({
    container: {
      flex: 1,
      flexDirection: 'column-reverse'
    },
    scenes: {
      flex: 1
    }
  });

  exports.default = (0, _withOrientation2.default)(StackViewLayout);
},"17c948a8b371a835ee443a36d2883b9f",["c42a5e17831e80ed1e1c8cf91f5ddb40","611a3127f00958fcce78b0ac7984a010","cc757a791ecb3cd320f65c256a791c04","77563c833b96b601648a52bbb84413e8","1ee12e34263a5c37106b178984ec4d9b","e91a423170a2d063c973900187b02b24","6bd004bef2f53ff343e677f2e121d275","642023f04391babe6960216152f851fe","36c636e765f427a2f89641e81f1ebba9","98e5c747c38d3feee7f297880784e291","79b022a09e7e5daf4ec8b2b903c645dc","b886806cc3f4ad8d13158780908d3098"],"node_modules/react-navigation/src/views/StackView/StackViewLayout.js");