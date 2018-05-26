__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-navigation/node_modules/react-navigation-tabs/node_modules/react-native-safe-area-view/index.js";

  var _react = _require(_dependencyMap[0], "react");

  var _react2 = babelHelpers.interopRequireDefault(_react);

  var _reactNative = _require(_dependencyMap[1], "react-native");

  var _withOrientation = _require(_dependencyMap[2], "./withOrientation");

  var _withOrientation2 = babelHelpers.interopRequireDefault(_withOrientation);

  var X_WIDTH = 375;
  var X_HEIGHT = 812;
  var PAD_WIDTH = 768;
  var PAD_HEIGHT = 1024;

  var _Dimensions$get = _reactNative.Dimensions.get('window'),
      D_HEIGHT = _Dimensions$get.height,
      D_WIDTH = _Dimensions$get.width;

  var _NativeModules$Platfo = _reactNative.NativeModules.PlatformConstants,
      PlatformConstants = _NativeModules$Platfo === undefined ? {} : _NativeModules$Platfo;

  var _ref = PlatformConstants.reactNativeVersion || {},
      _ref$minor = _ref.minor,
      minor = _ref$minor === undefined ? 0 : _ref$minor;

  var isIPhoneX = function () {
    if (_reactNative.Platform.OS === 'web') return false;

    if (minor >= 50) {
      return _reactNative.DeviceInfo.isIPhoneX_deprecated;
    }

    return _reactNative.Platform.OS === 'ios' && (D_HEIGHT === X_HEIGHT && D_WIDTH === X_WIDTH || D_HEIGHT === X_WIDTH && D_WIDTH === X_HEIGHT);
  }();

  var isIPad = function () {
    if (_reactNative.Platform.OS !== 'ios' || isIPhoneX) return false;

    if (D_HEIGHT > D_WIDTH && D_WIDTH < PAD_WIDTH) {
      return false;
    }

    if (D_WIDTH > D_HEIGHT && D_HEIGHT < PAD_WIDTH) {
      return false;
    }

    return true;
  }();

  var _customStatusBarHeight = null;

  var statusBarHeight = function statusBarHeight(isLandscape) {
    if (_customStatusBarHeight !== null) {
      return _customStatusBarHeight;
    }

    if (_reactNative.Platform.OS === 'android') {
      if (global.Expo) {
        return global.Expo.Constants.statusBarHeight;
      } else {
        return 0;
      }
    }

    if (isIPhoneX) {
      return isLandscape ? 0 : 44;
    }

    if (isIPad) {
      return 20;
    }

    return isLandscape ? 0 : 20;
  };

  var doubleFromPercentString = function doubleFromPercentString(percent) {
    if (!percent.includes('%')) {
      return 0;
    }

    var dbl = parseFloat(percent) / 100;
    if (isNaN(dbl)) return 0;
    return dbl;
  };

  var SafeView = function (_Component) {
    babelHelpers.inherits(SafeView, _Component);

    function SafeView() {
      var _ref2;

      var _temp, _this, _ret;

      babelHelpers.classCallCheck(this, SafeView);

      for (var _len = arguments.length, args = Array(_len), _key = 0; _key < _len; _key++) {
        args[_key] = arguments[_key];
      }

      return _ret = (_temp = (_this = babelHelpers.possibleConstructorReturn(this, (_ref2 = SafeView.__proto__ || Object.getPrototypeOf(SafeView)).call.apply(_ref2, [this].concat(args))), _this), _this.state = {
        touchesTop: true,
        touchesBottom: true,
        touchesLeft: true,
        touchesRight: true,
        orientation: null,
        viewWidth: 0,
        viewHeight: 0
      }, _this._onLayout = function () {
        if (!_this.view) return;
        var isLandscape = _this.props.isLandscape;
        var orientation = _this.state.orientation;
        var newOrientation = isLandscape ? 'landscape' : 'portrait';

        if (orientation && orientation === newOrientation) {
          return;
        }

        var WIDTH = isLandscape ? X_HEIGHT : X_WIDTH;
        var HEIGHT = isLandscape ? X_WIDTH : X_HEIGHT;

        _this.view._component.measureInWindow(function (winX, winY, winWidth, winHeight) {
          var realY = winY;
          var realX = winX;

          if (realY >= HEIGHT) {
            realY = realY % HEIGHT;
          } else if (realY < 0) {
            realY = realY % HEIGHT + HEIGHT;
          }

          if (realX >= WIDTH) {
            realX = realX % WIDTH;
          } else if (realX < 0) {
            realX = realX % WIDTH + WIDTH;
          }

          var touchesTop = realY === 0;
          var touchesBottom = realY + winHeight >= HEIGHT;
          var touchesLeft = realX === 0;
          var touchesRight = realX + winWidth >= WIDTH;

          _this.setState({
            touchesTop: touchesTop,
            touchesBottom: touchesBottom,
            touchesLeft: touchesLeft,
            touchesRight: touchesRight,
            orientation: newOrientation,
            viewWidth: winWidth,
            viewHeight: winHeight
          });
        });
      }, _this._getSafeAreaStyle = function () {
        var _this$state = _this.state,
            touchesTop = _this$state.touchesTop,
            touchesBottom = _this$state.touchesBottom,
            touchesLeft = _this$state.touchesLeft,
            touchesRight = _this$state.touchesRight;
        var _this$props = _this.props,
            forceInset = _this$props.forceInset,
            isLandscape = _this$props.isLandscape;

        var _this$_getViewStyles = _this._getViewStyles(),
            paddingTop = _this$_getViewStyles.paddingTop,
            paddingBottom = _this$_getViewStyles.paddingBottom,
            paddingLeft = _this$_getViewStyles.paddingLeft,
            paddingRight = _this$_getViewStyles.paddingRight,
            viewStyle = _this$_getViewStyles.viewStyle;

        var style = babelHelpers.extends({}, viewStyle, {
          paddingTop: touchesTop ? _this._getInset('top') : 0,
          paddingBottom: touchesBottom ? _this._getInset('bottom') : 0,
          paddingLeft: touchesLeft ? _this._getInset('left') : 0,
          paddingRight: touchesRight ? _this._getInset('right') : 0
        });

        if (forceInset) {
          Object.keys(forceInset).forEach(function (key) {
            var inset = forceInset[key];

            if (inset === 'always') {
              inset = _this._getInset(key);
            }

            if (inset === 'never') {
              inset = 0;
            }

            switch (key) {
              case 'horizontal':
                {
                  style.paddingLeft = inset;
                  style.paddingRight = inset;
                  break;
                }

              case 'vertical':
                {
                  style.paddingTop = inset;
                  style.paddingBottom = inset;
                  break;
                }

              case 'left':
              case 'right':
              case 'top':
              case 'bottom':
                {
                  var padding = "padding" + key[0].toUpperCase() + key.slice(1);
                  style[padding] = inset;
                  break;
                }
            }
          });
        }

        if (style.height && typeof style.height === 'number') {
          style.height += style.paddingTop + style.paddingBottom;
        }

        if (style.width && typeof style.width === 'number') {
          style.width += style.paddingLeft + style.paddingRight;
        }

        style.paddingTop = Math.max(style.paddingTop, paddingTop);
        style.paddingBottom = Math.max(style.paddingBottom, paddingBottom);
        style.paddingLeft = Math.max(style.paddingLeft, paddingLeft);
        style.paddingRight = Math.max(style.paddingRight, paddingRight);
        return style;
      }, _this._getViewStyles = function () {
        var viewWidth = _this.state.viewWidth;

        var _StyleSheet$flatten = _reactNative.StyleSheet.flatten(_this.props.style || {}),
            _StyleSheet$flatten$p = _StyleSheet$flatten.padding,
            padding = _StyleSheet$flatten$p === undefined ? 0 : _StyleSheet$flatten$p,
            _StyleSheet$flatten$p2 = _StyleSheet$flatten.paddingVertical,
            paddingVertical = _StyleSheet$flatten$p2 === undefined ? padding : _StyleSheet$flatten$p2,
            _StyleSheet$flatten$p3 = _StyleSheet$flatten.paddingHorizontal,
            paddingHorizontal = _StyleSheet$flatten$p3 === undefined ? padding : _StyleSheet$flatten$p3,
            _StyleSheet$flatten$p4 = _StyleSheet$flatten.paddingTop,
            paddingTop = _StyleSheet$flatten$p4 === undefined ? paddingVertical : _StyleSheet$flatten$p4,
            _StyleSheet$flatten$p5 = _StyleSheet$flatten.paddingBottom,
            paddingBottom = _StyleSheet$flatten$p5 === undefined ? paddingVertical : _StyleSheet$flatten$p5,
            _StyleSheet$flatten$p6 = _StyleSheet$flatten.paddingLeft,
            paddingLeft = _StyleSheet$flatten$p6 === undefined ? paddingHorizontal : _StyleSheet$flatten$p6,
            _StyleSheet$flatten$p7 = _StyleSheet$flatten.paddingRight,
            paddingRight = _StyleSheet$flatten$p7 === undefined ? paddingHorizontal : _StyleSheet$flatten$p7,
            viewStyle = babelHelpers.objectWithoutProperties(_StyleSheet$flatten, ["padding", "paddingVertical", "paddingHorizontal", "paddingTop", "paddingBottom", "paddingLeft", "paddingRight"]);

        if (typeof paddingTop !== 'number') {
          paddingTop = doubleFromPercentString(paddingTop) * viewWidth;
        }

        if (typeof paddingBottom !== 'number') {
          paddingBottom = doubleFromPercentString(paddingBottom) * viewWidth;
        }

        if (typeof paddingLeft !== 'number') {
          paddingLeft = doubleFromPercentString(paddingLeft) * viewWidth;
        }

        if (typeof paddingRight !== 'number') {
          paddingRight = doubleFromPercentString(paddingRight) * viewWidth;
        }

        return {
          paddingTop: paddingTop,
          paddingBottom: paddingBottom,
          paddingLeft: paddingLeft,
          paddingRight: paddingRight,
          viewStyle: viewStyle
        };
      }, _this._getInset = function (key) {
        var isLandscape = _this.props.isLandscape;

        switch (key) {
          case 'horizontal':
          case 'right':
          case 'left':
            {
              return isLandscape ? isIPhoneX ? 44 : 0 : 0;
            }

          case 'vertical':
          case 'top':
            {
              return statusBarHeight(isLandscape);
            }

          case 'bottom':
            {
              return isIPhoneX ? isLandscape ? 24 : 34 : 0;
            }
        }
      }, _temp), babelHelpers.possibleConstructorReturn(_this, _ret);
    }

    babelHelpers.createClass(SafeView, [{
      key: "componentDidMount",
      value: function componentDidMount() {
        var _this2 = this;

        _reactNative.InteractionManager.runAfterInteractions(function () {
          _this2._onLayout();
        });
      }
    }, {
      key: "componentWillReceiveProps",
      value: function componentWillReceiveProps() {
        this._onLayout();
      }
    }, {
      key: "render",
      value: function render() {
        var _this3 = this;

        var _props = this.props,
            _props$forceInset = _props.forceInset,
            forceInset = _props$forceInset === undefined ? false : _props$forceInset,
            isLandscape = _props.isLandscape,
            children = _props.children,
            style = _props.style;

        var safeAreaStyle = this._getSafeAreaStyle();

        return _react2.default.createElement(
          _reactNative.Animated.View,
          {
            ref: function ref(c) {
              return _this3.view = c;
            },
            onLayout: this._onLayout,
            style: safeAreaStyle,
            pointerEvents: "box-none",
            __source: {
              fileName: _jsxFileName,
              lineNumber: 128
            }
          },
          this.props.children
        );
      }
    }]);
    return SafeView;
  }(_react.Component);

  SafeView.setStatusBarHeight = function (height) {
    _customStatusBarHeight = height;
  };

  exports.default = (0, _withOrientation2.default)(SafeView);
},421,[12,22,422],"node_modules/react-navigation/node_modules/react-navigation-tabs/node_modules/react-native-safe-area-view/index.js");