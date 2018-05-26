__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-navigation/src/views/Header/ModularHeaderBackButton.js";

  var _react = _require(_dependencyMap[0], "react");

  var _react2 = babelHelpers.interopRequireDefault(_react);

  var _reactNative = _require(_dependencyMap[1], "react-native");

  var _TouchableItem = _require(_dependencyMap[2], "../TouchableItem");

  var _TouchableItem2 = babelHelpers.interopRequireDefault(_TouchableItem);

  var defaultBackImage = _require(_dependencyMap[3], '../assets/back-icon.png');

  var ModularHeaderBackButton = function (_React$PureComponent) {
    babelHelpers.inherits(ModularHeaderBackButton, _React$PureComponent);

    function ModularHeaderBackButton() {
      var _ref;

      var _temp, _this, _ret;

      babelHelpers.classCallCheck(this, ModularHeaderBackButton);

      for (var _len = arguments.length, args = Array(_len), _key = 0; _key < _len; _key++) {
        args[_key] = arguments[_key];
      }

      return _ret = (_temp = (_this = babelHelpers.possibleConstructorReturn(this, (_ref = ModularHeaderBackButton.__proto__ || Object.getPrototypeOf(ModularHeaderBackButton)).call.apply(_ref, [this].concat(args))), _this), _this.state = {}, _this._onTextLayout = function (e) {
        if (_this.state.initialTextWidth) {
          return;
        }

        _this.setState({
          initialTextWidth: e.nativeEvent.layout.x + e.nativeEvent.layout.width
        });
      }, _temp), babelHelpers.possibleConstructorReturn(_this, _ret);
    }

    babelHelpers.createClass(ModularHeaderBackButton, [{
      key: "_renderBackImage",
      value: function _renderBackImage() {
        var _props = this.props,
            backImage = _props.backImage,
            title = _props.title,
            tintColor = _props.tintColor;
        var BackImage = void 0;
        var props = void 0;

        if (_react2.default.isValidElement(backImage)) {
          return backImage;
        } else if (backImage) {
          BackImage = backImage;
          props = {
            tintColor: tintColor,
            title: title
          };
        } else {
          BackImage = _reactNative.Image;
          props = {
            style: [styles.icon, !!title && styles.iconWithTitle, !!tintColor && {
              tintColor: tintColor
            }],
            source: defaultBackImage
          };
        }

        return _react2.default.createElement(BackImage, babelHelpers.extends({}, props, {
          __source: {
            fileName: _jsxFileName,
            lineNumber: 51
          }
        }));
      }
    }, {
      key: "render",
      value: function render() {
        var _props2 = this.props,
            onPress = _props2.onPress,
            width = _props2.width,
            title = _props2.title,
            titleStyle = _props2.titleStyle,
            tintColor = _props2.tintColor,
            truncatedTitle = _props2.truncatedTitle;
        var renderTruncated = this.state.initialTextWidth && width ? this.state.initialTextWidth > width : false;
        var backButtonTitle = renderTruncated ? truncatedTitle : title;

        if (backButtonTitle && backButtonTitle.length > 8) {
          backButtonTitle = truncatedTitle;
        }

        var _props3 = this.props,
            ButtonContainerComponent = _props3.ButtonContainerComponent,
            LabelContainerComponent = _props3.LabelContainerComponent;
        return _react2.default.createElement(
          _TouchableItem2.default,
          {
            accessibilityComponentType: "button",
            accessibilityLabel: backButtonTitle,
            accessibilityTraits: "button",
            testID: "header-back",
            delayPressIn: 0,
            onPress: onPress,
            style: styles.container,
            borderless: true,
            __source: {
              fileName: _jsxFileName,
              lineNumber: 80
            }
          },
          _react2.default.createElement(
            _reactNative.View,
            {
              style: styles.container,
              __source: {
                fileName: _jsxFileName,
                lineNumber: 90
              }
            },
            _react2.default.createElement(
              ButtonContainerComponent,
              {
                __source: {
                  fileName: _jsxFileName,
                  lineNumber: 91
                }
              },
              this._renderBackImage()
            ),
            typeof backButtonTitle === 'string' && _react2.default.createElement(
              LabelContainerComponent,
              {
                __source: {
                  fileName: _jsxFileName,
                  lineNumber: 95
                }
              },
              _react2.default.createElement(
                _reactNative.Text,
                {
                  onLayout: this._onTextLayout,
                  style: [styles.title, !!tintColor && {
                    color: tintColor
                  }, titleStyle],
                  numberOfLines: 1,
                  __source: {
                    fileName: _jsxFileName,
                    lineNumber: 96
                  }
                },
                backButtonTitle
              )
            )
          )
        );
      }
    }]);
    return ModularHeaderBackButton;
  }(_react2.default.PureComponent);

  ModularHeaderBackButton.defaultProps = {
    tintColor: '#037aff',
    truncatedTitle: 'Back'
  };

  var styles = _reactNative.StyleSheet.create({
    container: {
      alignItems: 'center',
      flexDirection: 'row',
      backgroundColor: 'transparent'
    },
    title: {
      fontSize: 17,
      paddingRight: 10
    },
    icon: {
      height: 21,
      width: 12,
      marginLeft: 9,
      marginRight: 22,
      marginVertical: 12,
      resizeMode: 'contain',
      transform: [{
        scaleX: _reactNative.I18nManager.isRTL ? -1 : 1
      }]
    },
    iconWithTitle: {
      marginRight: 3
    }
  });

  exports.default = ModularHeaderBackButton;
},364,[12,22,362,363],"node_modules/react-navigation/src/views/Header/ModularHeaderBackButton.js");