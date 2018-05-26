__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-navigation/src/views/Header/Header.js";

  var _react = _require(_dependencyMap[0], "react");

  var _react2 = babelHelpers.interopRequireDefault(_react);

  var _reactNative = _require(_dependencyMap[1], "react-native");

  var _PlatformHelpers = _require(_dependencyMap[2], "../../PlatformHelpers");

  var _reactNativeSafeAreaView = _require(_dependencyMap[3], "react-native-safe-area-view");

  var _reactNativeSafeAreaView2 = babelHelpers.interopRequireDefault(_reactNativeSafeAreaView);

  var _HeaderTitle = _require(_dependencyMap[4], "./HeaderTitle");

  var _HeaderTitle2 = babelHelpers.interopRequireDefault(_HeaderTitle);

  var _HeaderBackButton = _require(_dependencyMap[5], "./HeaderBackButton");

  var _HeaderBackButton2 = babelHelpers.interopRequireDefault(_HeaderBackButton);

  var _ModularHeaderBackButton = _require(_dependencyMap[6], "./ModularHeaderBackButton");

  var _ModularHeaderBackButton2 = babelHelpers.interopRequireDefault(_ModularHeaderBackButton);

  var _HeaderStyleInterpolator = _require(_dependencyMap[7], "./HeaderStyleInterpolator");

  var _HeaderStyleInterpolator2 = babelHelpers.interopRequireDefault(_HeaderStyleInterpolator);

  var _withOrientation = _require(_dependencyMap[8], "../withOrientation");

  var _withOrientation2 = babelHelpers.interopRequireDefault(_withOrientation);

  var APPBAR_HEIGHT = _reactNative.Platform.OS === 'ios' ? 44 : 56;
  var STATUSBAR_HEIGHT = _reactNative.Platform.OS === 'ios' ? 20 : 0;
  var TITLE_OFFSET = _reactNative.Platform.OS === 'ios' ? 70 : 56;

  var getAppBarHeight = function getAppBarHeight(isLandscape) {
    return _reactNative.Platform.OS === 'ios' ? isLandscape && !_reactNative.Platform.isPad ? 32 : 44 : 56;
  };

  var Header = function (_React$PureComponent) {
    babelHelpers.inherits(Header, _React$PureComponent);

    function Header() {
      var _ref;

      var _temp, _this, _ret;

      babelHelpers.classCallCheck(this, Header);

      for (var _len = arguments.length, args = Array(_len), _key = 0; _key < _len; _key++) {
        args[_key] = arguments[_key];
      }

      return _ret = (_temp = (_this = babelHelpers.possibleConstructorReturn(this, (_ref = Header.__proto__ || Object.getPrototypeOf(Header)).call.apply(_ref, [this].concat(args))), _this), _this.state = {
        widths: {}
      }, _this._renderTitleComponent = function (props) {
        var options = props.scene.descriptor.options;
        var headerTitle = options.headerTitle;

        if (_react2.default.isValidElement(headerTitle)) {
          return headerTitle;
        }

        var titleString = _this._getHeaderTitleString(props.scene);

        var titleStyle = options.headerTitleStyle;
        var color = options.headerTintColor;
        var allowFontScaling = options.headerTitleAllowFontScaling;
        var onLayoutIOS = _reactNative.Platform.OS === 'ios' ? function (e) {
          _this.setState({
            widths: babelHelpers.extends({}, _this.state.widths, babelHelpers.defineProperty({}, props.scene.key, e.nativeEvent.layout.width))
          });
        } : undefined;
        var RenderedHeaderTitle = headerTitle && typeof headerTitle !== 'string' ? headerTitle : _HeaderTitle2.default;
        return _react2.default.createElement(
          RenderedHeaderTitle,
          {
            onLayout: onLayoutIOS,
            allowFontScaling: allowFontScaling == null ? true : allowFontScaling,
            style: [color ? {
              color: color
            } : null, titleStyle],
            __source: {
              fileName: _jsxFileName,
              lineNumber: 116
            }
          },
          titleString
        );
      }, _this._renderLeftComponent = function (props) {
        var options = props.scene.descriptor.options;

        if (_react2.default.isValidElement(options.headerLeft) || options.headerLeft === null) {
          return options.headerLeft;
        }

        if (props.scene.index === 0) {
          return;
        }

        var backButtonTitle = _this._getBackButtonTitleString(props.scene);

        var truncatedBackButtonTitle = _this._getTruncatedBackButtonTitle(props.scene);

        var width = _this.state.widths[props.scene.key] ? (_this.props.layout.initWidth - _this.state.widths[props.scene.key]) / 2 : undefined;
        var RenderedLeftComponent = options.headerLeft || _HeaderBackButton2.default;

        var goBack = function goBack() {
          requestAnimationFrame(function () {
            props.scene.descriptor.navigation.goBack(props.scene.descriptor.key);
          });
        };

        return _react2.default.createElement(RenderedLeftComponent, {
          onPress: goBack,
          pressColorAndroid: options.headerPressColorAndroid,
          tintColor: options.headerTintColor,
          backImage: options.headerBackImage,
          title: backButtonTitle,
          truncatedTitle: truncatedBackButtonTitle,
          titleStyle: options.headerBackTitleStyle,
          width: width,
          __source: {
            fileName: _jsxFileName,
            lineNumber: 154
          }
        });
      }, _this._renderModularLeftComponent = function (props, ButtonContainerComponent, LabelContainerComponent) {
        var _props$scene$descript = props.scene.descriptor,
            options = _props$scene$descript.options,
            navigation = _props$scene$descript.navigation;

        var backButtonTitle = _this._getBackButtonTitleString(props.scene);

        var truncatedBackButtonTitle = _this._getTruncatedBackButtonTitle(props.scene);

        var width = _this.state.widths[props.scene.key] ? (_this.props.layout.initWidth - _this.state.widths[props.scene.key]) / 2 : undefined;

        var goBack = function goBack() {
          requestAnimationFrame(function () {
            navigation.goBack(props.scene.descriptor.key);
          });
        };

        return _react2.default.createElement(_ModularHeaderBackButton2.default, {
          onPress: goBack,
          ButtonContainerComponent: ButtonContainerComponent,
          LabelContainerComponent: LabelContainerComponent,
          pressColorAndroid: options.headerPressColorAndroid,
          tintColor: options.headerTintColor,
          backImage: options.headerBackImage,
          title: backButtonTitle,
          truncatedTitle: truncatedBackButtonTitle,
          titleStyle: options.headerBackTitleStyle,
          width: width,
          __source: {
            fileName: _jsxFileName,
            lineNumber: 189
          }
        });
      }, _this._renderRightComponent = function (props) {
        var headerRight = props.scene.descriptor.options.headerRight;
        return headerRight || null;
      }, _temp), babelHelpers.possibleConstructorReturn(_this, _ret);
    }

    babelHelpers.createClass(Header, [{
      key: "_getHeaderTitleString",
      value: function _getHeaderTitleString(scene) {
        var options = scene.descriptor.options;

        if (typeof options.headerTitle === 'string') {
          return options.headerTitle;
        }

        return options.title;
      }
    }, {
      key: "_getLastScene",
      value: function _getLastScene(scene) {
        return this.props.scenes.find(function (s) {
          return s.index === scene.index - 1;
        });
      }
    }, {
      key: "_getBackButtonTitleString",
      value: function _getBackButtonTitleString(scene) {
        var lastScene = this._getLastScene(scene);

        if (!lastScene) {
          return null;
        }

        var headerBackTitle = lastScene.descriptor.options.headerBackTitle;

        if (headerBackTitle || headerBackTitle === null) {
          return headerBackTitle;
        }

        return this._getHeaderTitleString(lastScene);
      }
    }, {
      key: "_getTruncatedBackButtonTitle",
      value: function _getTruncatedBackButtonTitle(scene) {
        var lastScene = this._getLastScene(scene);

        if (!lastScene) {
          return null;
        }

        return lastScene.descriptor.options.headerTruncatedBackTitle;
      }
    }, {
      key: "_renderLeft",
      value: function _renderLeft(props) {
        var options = props.scene.descriptor.options;
        var transitionPreset = this.props.transitionPreset;

        if (transitionPreset !== 'uikit' || options.headerBackImage || options.headerLeft || options.headerLeft === null) {
          return this._renderSubView(props, 'left', this._renderLeftComponent, this.props.leftInterpolator);
        } else {
          return this._renderModularSubView(props, 'left', this._renderModularLeftComponent, this.props.leftLabelInterpolator, this.props.leftButtonInterpolator);
        }
      }
    }, {
      key: "_renderTitle",
      value: function _renderTitle(props, options) {
        var style = {};
        var transitionPreset = this.props.transitionPreset;

        if (_reactNative.Platform.OS === 'android') {
          if (!options.hasLeftComponent) {
            style.left = 0;
          }

          if (!options.hasRightComponent) {
            style.right = 0;
          }
        } else if (_reactNative.Platform.OS === 'ios' && !options.hasLeftComponent && !options.hasRightComponent) {
          style.left = 0;
          style.right = 0;
        }

        return this._renderSubView(babelHelpers.extends({}, props, {
          style: style
        }), 'title', this._renderTitleComponent, transitionPreset === 'uikit' ? this.props.titleFromLeftInterpolator : this.props.titleInterpolator);
      }
    }, {
      key: "_renderRight",
      value: function _renderRight(props) {
        return this._renderSubView(props, 'right', this._renderRightComponent, this.props.rightInterpolator);
      }
    }, {
      key: "_renderModularSubView",
      value: function _renderModularSubView(props, name, renderer, labelStyleInterpolator, buttonStyleInterpolator) {
        var _this2 = this;

        var scene = props.scene;
        var index = scene.index,
            isStale = scene.isStale,
            key = scene.key;

        if (index === 0) {
          return;
        }

        var offset = this.props.navigation.state.index - index;

        if (Math.abs(offset) > 2) {
          return null;
        }

        var ButtonContainer = function ButtonContainer(_ref2) {
          var children = _ref2.children;
          return _react2.default.createElement(
            _reactNative.Animated.View,
            {
              style: [buttonStyleInterpolator(babelHelpers.extends({}, _this2.props, props))],
              __source: {
                fileName: _jsxFileName,
                lineNumber: 302
              }
            },
            children
          );
        };

        var LabelContainer = function LabelContainer(_ref3) {
          var children = _ref3.children;
          return _react2.default.createElement(
            _reactNative.Animated.View,
            {
              style: [labelStyleInterpolator(babelHelpers.extends({}, _this2.props, props))],
              __source: {
                fileName: _jsxFileName,
                lineNumber: 310
              }
            },
            children
          );
        };

        var subView = renderer(props, ButtonContainer, LabelContainer);

        if (subView === null) {
          return subView;
        }

        var pointerEvents = offset !== 0 || isStale ? 'none' : 'box-none';
        return _react2.default.createElement(
          _reactNative.View,
          {
            key: name + "_" + key,
            pointerEvents: pointerEvents,
            style: [styles.item, styles[name], props.style],
            __source: {
              fileName: _jsxFileName,
              lineNumber: 326
            }
          },
          subView
        );
      }
    }, {
      key: "_renderSubView",
      value: function _renderSubView(props, name, renderer, styleInterpolator) {
        var scene = props.scene;
        var index = scene.index,
            isStale = scene.isStale,
            key = scene.key;
        var offset = this.props.navigation.state.index - index;

        if (Math.abs(offset) > 2) {
          return null;
        }

        var subView = renderer(props);

        if (subView == null) {
          return null;
        }

        var pointerEvents = offset !== 0 || isStale ? 'none' : 'box-none';
        return _react2.default.createElement(
          _reactNative.Animated.View,
          {
            pointerEvents: pointerEvents,
            key: name + "_" + key,
            style: [styles.item, styles[name], props.style, styleInterpolator(babelHelpers.extends({}, this.props, props))],
            __source: {
              fileName: _jsxFileName,
              lineNumber: 357
            }
          },
          subView
        );
      }
    }, {
      key: "_renderHeader",
      value: function _renderHeader(props) {
        var options = props.scene.descriptor.options;

        if (options.header === null) {
          return null;
        }

        var left = this._renderLeft(props);

        var right = this._renderRight(props);

        var title = this._renderTitle(props, {
          hasLeftComponent: !!left,
          hasRightComponent: !!right
        });

        var _props = this.props,
            isLandscape = _props.isLandscape,
            transitionPreset = _props.transitionPreset;
        var wrapperProps = {
          style: styles.header,
          key: "scene_" + props.scene.key
        };

        if (options.headerLeft || options.headerBackImage || _reactNative.Platform.OS !== 'ios' || transitionPreset !== 'uikit') {
          return _react2.default.createElement(
            _reactNative.View,
            babelHelpers.extends({}, wrapperProps, {
              __source: {
                fileName: _jsxFileName,
                lineNumber: 402
              }
            }),
            title,
            left,
            right
          );
        } else {
          return _react2.default.createElement(
            _PlatformHelpers.MaskedViewIOS,
            babelHelpers.extends({}, wrapperProps, {
              maskElement: _react2.default.createElement(
                _reactNative.View,
                {
                  style: styles.iconMaskContainer,
                  __source: {
                    fileName: _jsxFileName,
                    lineNumber: 413
                  }
                },
                _react2.default.createElement(_reactNative.Image, {
                  source: _require(_dependencyMap[9], '../assets/back-icon-mask.png'),
                  style: styles.iconMask,
                  __source: {
                    fileName: _jsxFileName,
                    lineNumber: 414
                  }
                }),
                _react2.default.createElement(_reactNative.View, {
                  style: styles.iconMaskFillerRect,
                  __source: {
                    fileName: _jsxFileName,
                    lineNumber: 418
                  }
                })
              ),
              __source: {
                fileName: _jsxFileName,
                lineNumber: 410
              }
            }),
            title,
            left,
            right
          );
        }
      }
    }, {
      key: "render",
      value: function render() {
        var _this3 = this;

        var appBar = void 0;
        var _props2 = this.props,
            mode = _props2.mode,
            scene = _props2.scene,
            isLandscape = _props2.isLandscape;

        if (mode === 'float') {
          var scenesByIndex = {};
          this.props.scenes.forEach(function (scene) {
            scenesByIndex[scene.index] = scene;
          });
          var scenesProps = Object.values(scenesByIndex).map(function (scene) {
            return {
              position: _this3.props.position,
              progress: _this3.props.progress,
              scene: scene
            };
          });
          appBar = scenesProps.map(this._renderHeader, this);
        } else {
          appBar = this._renderHeader({
            position: new _reactNative.Animated.Value(this.props.scene.index),
            progress: new _reactNative.Animated.Value(0),
            scene: this.props.scene
          });
        }

        var options = scene.descriptor.options;
        var _options$headerStyle = options.headerStyle,
            headerStyle = _options$headerStyle === undefined ? {} : _options$headerStyle;

        var headerStyleObj = _reactNative.StyleSheet.flatten(headerStyle);

        var appBarHeight = getAppBarHeight(isLandscape);
        var alignItems = headerStyleObj.alignItems,
            justifyContent = headerStyleObj.justifyContent,
            flex = headerStyleObj.flex,
            flexDirection = headerStyleObj.flexDirection,
            flexGrow = headerStyleObj.flexGrow,
            flexShrink = headerStyleObj.flexShrink,
            flexBasis = headerStyleObj.flexBasis,
            flexWrap = headerStyleObj.flexWrap,
            safeHeaderStyle = babelHelpers.objectWithoutProperties(headerStyleObj, ["alignItems", "justifyContent", "flex", "flexDirection", "flexGrow", "flexShrink", "flexBasis", "flexWrap"]);

        if (__DEV__) {
          warnIfHeaderStyleDefined(alignItems, 'alignItems');
          warnIfHeaderStyleDefined(justifyContent, 'justifyContent');
          warnIfHeaderStyleDefined(flex, 'flex');
          warnIfHeaderStyleDefined(flexDirection, 'flexDirection');
          warnIfHeaderStyleDefined(flexGrow, 'flexGrow');
          warnIfHeaderStyleDefined(flexShrink, 'flexShrink');
          warnIfHeaderStyleDefined(flexBasis, 'flexBasis');
          warnIfHeaderStyleDefined(flexWrap, 'flexWrap');
        }

        var containerStyles = [options.headerTransparent ? styles.transparentContainer : styles.container, {
          height: appBarHeight
        }, safeHeaderStyle];
        var headerForceInset = options.headerForceInset;
        var forceInset = headerForceInset || {
          top: 'always',
          bottom: 'never'
        };
        return _react2.default.createElement(
          _reactNative.Animated.View,
          {
            style: this.props.layoutInterpolator(this.props),
            __source: {
              fileName: _jsxFileName,
              lineNumber: 494
            }
          },
          _react2.default.createElement(
            _reactNativeSafeAreaView2.default,
            {
              forceInset: forceInset,
              style: containerStyles,
              __source: {
                fileName: _jsxFileName,
                lineNumber: 495
              }
            },
            _react2.default.createElement(
              _reactNative.View,
              {
                style: _reactNative.StyleSheet.absoluteFill,
                __source: {
                  fileName: _jsxFileName,
                  lineNumber: 496
                }
              },
              options.headerBackground
            ),
            _react2.default.createElement(
              _reactNative.View,
              {
                style: styles.flexOne,
                __source: {
                  fileName: _jsxFileName,
                  lineNumber: 499
                }
              },
              appBar
            )
          )
        );
      }
    }], [{
      key: "HEIGHT",
      get: function get() {
        return APPBAR_HEIGHT + STATUSBAR_HEIGHT;
      }
    }]);
    return Header;
  }(_react2.default.PureComponent);

  Header.defaultProps = {
    layoutInterpolator: _HeaderStyleInterpolator2.default.forLayout,
    leftInterpolator: _HeaderStyleInterpolator2.default.forLeft,
    leftButtonInterpolator: _HeaderStyleInterpolator2.default.forLeftButton,
    leftLabelInterpolator: _HeaderStyleInterpolator2.default.forLeftLabel,
    titleFromLeftInterpolator: _HeaderStyleInterpolator2.default.forCenterFromLeft,
    titleInterpolator: _HeaderStyleInterpolator2.default.forCenter,
    rightInterpolator: _HeaderStyleInterpolator2.default.forRight
  };

  function warnIfHeaderStyleDefined(value, styleProp) {
    if (value !== undefined) {
      console.warn(styleProp + " was given a value of " + value + ", this has no effect on headerStyle.");
    }
  }

  var platformContainerStyles = void 0;

  if (_reactNative.Platform.OS === 'ios') {
    platformContainerStyles = {
      borderBottomWidth: _reactNative.StyleSheet.hairlineWidth,
      borderBottomColor: '#A7A7AA'
    };
  } else {
    platformContainerStyles = {
      shadowColor: 'black',
      shadowOpacity: 0.1,
      shadowRadius: _reactNative.StyleSheet.hairlineWidth,
      shadowOffset: {
        height: _reactNative.StyleSheet.hairlineWidth
      },
      elevation: 4
    };
  }

  var styles = _reactNative.StyleSheet.create({
    container: babelHelpers.extends({
      backgroundColor: _reactNative.Platform.OS === 'ios' ? '#F7F7F7' : '#FFF'
    }, platformContainerStyles),
    transparentContainer: babelHelpers.extends({
      position: 'absolute',
      top: 0,
      left: 0,
      right: 0
    }, platformContainerStyles),
    header: babelHelpers.extends({}, _reactNative.StyleSheet.absoluteFillObject, {
      flexDirection: 'row'
    }),
    item: {
      backgroundColor: 'transparent'
    },
    iconMaskContainer: {
      flex: 1,
      flexDirection: 'row',
      justifyContent: 'center'
    },
    iconMaskFillerRect: {
      flex: 1,
      backgroundColor: '#d8d8d8',
      marginLeft: -3
    },
    iconMask: {
      height: 21,
      width: 12,
      marginLeft: 9,
      marginTop: -0.5,
      alignSelf: 'center',
      resizeMode: 'contain',
      transform: [{
        scaleX: _reactNative.I18nManager.isRTL ? -1 : 1
      }]
    },
    title: {
      bottom: 0,
      top: 0,
      left: TITLE_OFFSET,
      right: TITLE_OFFSET,
      position: 'absolute',
      alignItems: 'center',
      flexDirection: 'row',
      justifyContent: _reactNative.Platform.OS === 'ios' ? 'center' : 'flex-start'
    },
    left: {
      left: 0,
      bottom: 0,
      top: 0,
      position: 'absolute',
      alignItems: 'center',
      flexDirection: 'row'
    },
    right: {
      right: 0,
      bottom: 0,
      top: 0,
      position: 'absolute',
      flexDirection: 'row',
      alignItems: 'center'
    },
    flexOne: {
      flex: 1
    }
  });

  exports.default = (0, _withOrientation2.default)(Header);
},356,[12,22,340,357,360,361,364,365,367,368],"node_modules/react-navigation/src/views/Header/Header.js");