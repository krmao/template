__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-navigation/node_modules/react-navigation-tabs/dist/navigators/createBottomTabNavigator.js";

  var _react = _require(_dependencyMap[0], "react");

  var React = babelHelpers.interopRequireWildcard(_react);

  var _reactNative = _require(_dependencyMap[1], "react-native");

  var _reactLifecyclesCompat = _require(_dependencyMap[2], "react-lifecycles-compat");

  var _createTabNavigator = _require(_dependencyMap[3], "../utils/createTabNavigator");

  var _createTabNavigator2 = babelHelpers.interopRequireDefault(_createTabNavigator);

  var _BottomTabBar = _require(_dependencyMap[4], "../views/BottomTabBar");

  var _BottomTabBar2 = babelHelpers.interopRequireDefault(_BottomTabBar);

  var _ResourceSavingScene = _require(_dependencyMap[5], "../views/ResourceSavingScene");

  var _ResourceSavingScene2 = babelHelpers.interopRequireDefault(_ResourceSavingScene);

  var TabNavigationView = function (_React$PureComponent) {
    babelHelpers.inherits(TabNavigationView, _React$PureComponent);

    function TabNavigationView() {
      var _ref;

      var _temp, _this, _ret;

      babelHelpers.classCallCheck(this, TabNavigationView);

      for (var _len = arguments.length, args = Array(_len), _key = 0; _key < _len; _key++) {
        args[_key] = arguments[_key];
      }

      return _ret = (_temp = (_this = babelHelpers.possibleConstructorReturn(this, (_ref = TabNavigationView.__proto__ || Object.getPrototypeOf(TabNavigationView)).call.apply(_ref, [this].concat(args))), _this), _this.state = {
        loaded: [_this.props.navigation.state.index]
      }, _this._getLabel = function (_ref2) {
        var route = _ref2.route,
            focused = _ref2.focused,
            tintColor = _ref2.tintColor;

        var label = _this.props.getLabelText({
          route: route
        });

        if (typeof label === 'function') {
          return label({
            focused: focused,
            tintColor: tintColor
          });
        }

        return label;
      }, _this._renderTabBar = function () {
        var _this$props = _this.props,
            _this$props$tabBarCom = _this$props.tabBarComponent,
            TabBarComponent = _this$props$tabBarCom === undefined ? _BottomTabBar2.default : _this$props$tabBarCom,
            tabBarOptions = _this$props.tabBarOptions,
            navigation = _this$props.navigation,
            screenProps = _this$props.screenProps,
            getLabelText = _this$props.getLabelText,
            renderIcon = _this$props.renderIcon,
            onTabPress = _this$props.onTabPress;
        var descriptors = _this.props.descriptors;
        var state = _this.props.navigation.state;
        var route = state.routes[state.index];
        var descriptor = descriptors[route.key];
        var options = descriptor.options;

        if (options.tabBarVisible === false) {
          return null;
        }

        return React.createElement(TabBarComponent, babelHelpers.extends({}, tabBarOptions, {
          jumpTo: _this._jumpTo,
          navigation: navigation,
          screenProps: screenProps,
          onTabPress: onTabPress,
          getLabelText: getLabelText,
          renderIcon: renderIcon,
          __source: {
            fileName: _jsxFileName,
            lineNumber: 57
          }
        }));
      }, _this._jumpTo = function (key) {
        var _this$props2 = _this.props,
            navigation = _this$props2.navigation,
            onIndexChange = _this$props2.onIndexChange;
        var index = navigation.state.routes.findIndex(function (route) {
          return route.key === key;
        });
        onIndexChange(index);
      }, _temp), babelHelpers.possibleConstructorReturn(_this, _ret);
    }

    babelHelpers.createClass(TabNavigationView, [{
      key: "render",
      value: function render() {
        var _props = this.props,
            navigation = _props.navigation,
            renderScene = _props.renderScene,
            lazy = _props.lazy;
        var routes = navigation.state.routes;
        var loaded = this.state.loaded;
        return React.createElement(
          _reactNative.View,
          {
            style: styles.container,
            __source: {
              fileName: _jsxFileName,
              lineNumber: 73
            }
          },
          React.createElement(
            _reactNative.View,
            {
              style: styles.pages,
              __source: {
                fileName: _jsxFileName,
                lineNumber: 74
              }
            },
            routes.map(function (route, index) {
              if (lazy && !loaded.includes(index)) {
                return null;
              }

              var isFocused = navigation.state.index === index;
              return React.createElement(
                _ResourceSavingScene2.default,
                {
                  key: route.key,
                  style: [_reactNative.StyleSheet.absoluteFill, {
                    opacity: isFocused ? 1 : 0
                  }],
                  isFocused: isFocused,
                  __source: {
                    fileName: _jsxFileName,
                    lineNumber: 83
                  }
                },
                renderScene({
                  route: route
                })
              );
            })
          ),
          this._renderTabBar()
        );
      }
    }], [{
      key: "getDerivedStateFromProps",
      value: function getDerivedStateFromProps(nextProps, prevState) {
        var index = nextProps.navigation.state.index;
        return {
          loaded: prevState.loaded.includes(index) ? prevState.loaded : [].concat(babelHelpers.toConsumableArray(prevState.loaded), [index])
        };
      }
    }]);
    return TabNavigationView;
  }(React.PureComponent);

  TabNavigationView.defaultProps = {
    lazy: true
  };
  (0, _reactLifecyclesCompat.polyfill)(TabNavigationView);

  var styles = _reactNative.StyleSheet.create({
    container: {
      flex: 1,
      overflow: 'hidden'
    },
    pages: {
      flex: 1
    }
  });

  exports.default = (0, _createTabNavigator2.default)(TabNavigationView);
},"b198b03b0c2fc5513c0571e39ff0c2a8",["c42a5e17831e80ed1e1c8cf91f5ddb40","cc757a791ecb3cd320f65c256a791c04","04f36bbcb68de9b1a3736a25bd8827bc","c237d25f932c60ab5f6da545341f2bb9","3428988f13d25a50f5c1799805d11334","8f33bf7f32df11cb95b924f216218e25"],"node_modules/react-navigation/node_modules/react-navigation-tabs/dist/navigators/createBottomTabNavigator.js");