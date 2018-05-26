__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-navigation/node_modules/react-navigation-deprecated-tab-navigator/src/views/TabBarTop.js";

  var _react = _require(_dependencyMap[0], "react");

  var _react2 = babelHelpers.interopRequireDefault(_react);

  var _reactNative = _require(_dependencyMap[1], "react-native");

  var _reactNativeTabView = _require(_dependencyMap[2], "react-native-tab-view");

  var _TabBarIcon = _require(_dependencyMap[3], "./TabBarIcon");

  var _TabBarIcon2 = babelHelpers.interopRequireDefault(_TabBarIcon);

  var TabBarTop = function (_React$PureComponent) {
    babelHelpers.inherits(TabBarTop, _React$PureComponent);

    function TabBarTop() {
      var _ref;

      var _temp, _this, _ret;

      babelHelpers.classCallCheck(this, TabBarTop);

      for (var _len = arguments.length, args = Array(_len), _key = 0; _key < _len; _key++) {
        args[_key] = arguments[_key];
      }

      return _ret = (_temp = (_this = babelHelpers.possibleConstructorReturn(this, (_ref = TabBarTop.__proto__ || Object.getPrototypeOf(TabBarTop)).call.apply(_ref, [this].concat(args))), _this), _this._renderLabel = function (scene) {
        var _this$props = _this.props,
            position = _this$props.position,
            tabBarPosition = _this$props.tabBarPosition,
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
              style: [styles.label, {
                color: color
              }, labelStyle],
              allowFontScaling: allowFontScaling,
              __source: {
                fileName: _jsxFileName,
                lineNumber: 47
              }
            },
            upperCaseLabel ? label.toUpperCase() : label
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
            iconStyle = _this$props2.iconStyle;

        if (showIcon === false) {
          return null;
        }

        return _react2.default.createElement(_TabBarIcon2.default, {
          position: position,
          navigation: navigation,
          activeTintColor: activeTintColor,
          inactiveTintColor: inactiveTintColor,
          renderIcon: renderIcon,
          scene: scene,
          style: [styles.icon, iconStyle],
          __source: {
            fileName: _jsxFileName,
            lineNumber: 75
          }
        });
      }, _this._handleOnPress = function (scene) {
        var _this$props3 = _this.props,
            getOnPress = _this$props3.getOnPress,
            jumpToIndex = _this$props3.jumpToIndex,
            navigation = _this$props3.navigation;
        var previousScene = navigation.state.routes[navigation.state.index];
        var onPress = getOnPress(previousScene, scene);

        if (onPress) {
          onPress({
            previousScene: previousScene,
            scene: scene,
            jumpToIndex: jumpToIndex,
            defaultHandler: jumpToIndex
          });
        } else {
          jumpToIndex(scene.index);
        }
      }, _temp), babelHelpers.possibleConstructorReturn(_this, _ret);
    }

    babelHelpers.createClass(TabBarTop, [{
      key: "render",
      value: function render() {
        var props = this.props;
        return _react2.default.createElement(_reactNativeTabView.TabBar, babelHelpers.extends({}, props, {
          onTabPress: this._handleOnPress,
          jumpToIndex: function jumpToIndex() {},
          renderIcon: this._renderIcon,
          renderLabel: this._renderLabel,
          __source: {
            fileName: _jsxFileName,
            lineNumber: 112
          }
        }));
      }
    }]);
    return TabBarTop;
  }(_react2.default.PureComponent);

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
},413,[12,22,403,414],"node_modules/react-navigation/node_modules/react-navigation-deprecated-tab-navigator/src/views/TabBarTop.js");