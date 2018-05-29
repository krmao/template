__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-navigation/node_modules/react-navigation-tabs/dist/navigators/createMaterialTopTabNavigator.js";

  var _react = _require(_dependencyMap[0], "react");

  var React = babelHelpers.interopRequireWildcard(_react);

  var _reactNative = _require(_dependencyMap[1], "react-native");

  var _reactNativeTabView = _require(_dependencyMap[2], "react-native-tab-view");

  var _createTabNavigator = _require(_dependencyMap[3], "../utils/createTabNavigator");

  var _createTabNavigator2 = babelHelpers.interopRequireDefault(_createTabNavigator);

  var _MaterialTopTabBar = _require(_dependencyMap[4], "../views/MaterialTopTabBar");

  var _MaterialTopTabBar2 = babelHelpers.interopRequireDefault(_MaterialTopTabBar);

  var _ResourceSavingScene = _require(_dependencyMap[5], "../views/ResourceSavingScene");

  var _ResourceSavingScene2 = babelHelpers.interopRequireDefault(_ResourceSavingScene);

  var TabView = function (_React$PureComponent) {
    babelHelpers.inherits(TabView, _React$PureComponent);

    function TabView() {
      var _ref;

      var _temp, _this, _ret;

      babelHelpers.classCallCheck(this, TabView);

      for (var _len = arguments.length, args = Array(_len), _key = 0; _key < _len; _key++) {
        args[_key] = arguments[_key];
      }

      return _ret = (_temp = (_this = babelHelpers.possibleConstructorReturn(this, (_ref = TabView.__proto__ || Object.getPrototypeOf(TabView)).call.apply(_ref, [this].concat(args))), _this), _this._getLabel = function (_ref2) {
        var route = _ref2.route,
            tintColor = _ref2.tintColor,
            focused = _ref2.focused;
        var descriptors = _this.props.descriptors;
        var descriptor = descriptors[route.key];
        var options = descriptor.options;

        if (options.tabBarLabel) {
          return typeof options.tabBarLabel === 'function' ? options.tabBarLabel({
            tintColor: tintColor,
            focused: focused
          }) : options.tabBarLabel;
        }

        if (typeof options.title === 'string') {
          return options.title;
        }

        return route.routeName;
      }, _this._getTestIDProps = function (_ref3) {
        var route = _ref3.route,
            focused = _ref3.focused;
        var descriptors = _this.props.descriptors;
        var descriptor = descriptors[route.key];
        var options = descriptor.options;
        return typeof options.tabBarTestIDProps === 'function' ? options.tabBarTestIDProps({
          focused: focused
        }) : options.tabBarTestIDProps;
      }, _this._renderIcon = function (_ref4) {
        var focused = _ref4.focused,
            route = _ref4.route,
            tintColor = _ref4.tintColor;
        var descriptors = _this.props.descriptors;
        var descriptor = descriptors[route.key];
        var options = descriptor.options;

        if (options.tabBarIcon) {
          return typeof options.tabBarIcon === 'function' ? options.tabBarIcon({
            tintColor: tintColor,
            focused: focused
          }) : options.tabBarIcon;
        }

        return null;
      }, _this._renderTabBar = function (props) {
        var state = _this.props.navigation.state;
        var route = state.routes[state.index];
        var descriptors = _this.props.descriptors;
        var descriptor = descriptors[route.key];
        var options = descriptor.options;
        var tabBarVisible = options.tabBarVisible == null ? true : options.tabBarVisible;
        var _this$props = _this.props,
            _this$props$tabBarCom = _this$props.tabBarComponent,
            TabBarComponent = _this$props$tabBarCom === undefined ? _MaterialTopTabBar2.default : _this$props$tabBarCom,
            tabBarPosition = _this$props.tabBarPosition,
            tabBarOptions = _this$props.tabBarOptions;

        if (TabBarComponent === null || !tabBarVisible) {
          return null;
        }

        return React.createElement(TabBarComponent, babelHelpers.extends({}, tabBarOptions, props, {
          tabBarPosition: tabBarPosition,
          screenProps: _this.props.screenProps,
          navigation: _this.props.navigation,
          getLabelText: _this.props.getLabelText,
          getTestIDProps: _this._getTestIDProps,
          renderIcon: _this._renderIcon,
          onTabPress: _this.props.onTabPress,
          __source: {
            fileName: _jsxFileName,
            lineNumber: 71
          }
        }));
      }, _this._renderPanPager = function (props) {
        return React.createElement(_reactNativeTabView.TabViewPagerPan, babelHelpers.extends({}, props, {
          __source: {
            fileName: _jsxFileName,
            lineNumber: 74
          }
        }));
      }, _this._renderScene = function (_ref5) {
        var route = _ref5.route,
            focused = _ref5.focused;
        var _this$props2 = _this.props,
            renderScene = _this$props2.renderScene,
            animationEnabled = _this$props2.animationEnabled,
            swipeEnabled = _this$props2.swipeEnabled;

        if (animationEnabled === false && swipeEnabled === false) {
          return React.createElement(
            _ResourceSavingScene2.default,
            {
              isFocused: focused,
              __source: {
                fileName: _jsxFileName,
                lineNumber: 80
              }
            },
            renderScene({
              route: route
            })
          );
        }

        return renderScene({
          route: route
        });
      }, _temp), babelHelpers.possibleConstructorReturn(_this, _ret);
    }

    babelHelpers.createClass(TabView, [{
      key: "render",
      value: function render() {
        var _props = this.props,
            navigation = _props.navigation,
            tabBarPosition = _props.tabBarPosition,
            animationEnabled = _props.animationEnabled,
            renderScene = _props.renderScene,
            rest = babelHelpers.objectWithoutProperties(_props, ["navigation", "tabBarPosition", "animationEnabled", "renderScene"]);
        var renderHeader = void 0;
        var renderFooter = void 0;
        var renderPager = void 0;
        var state = this.props.navigation.state;
        var route = state.routes[state.index];
        var descriptors = this.props.descriptors;
        var descriptor = descriptors[route.key];
        var options = descriptor.options;
        var swipeEnabled = options.swipeEnabled == null ? this.props.swipeEnabled : options.swipeEnabled;

        if (typeof swipeEnabled === 'function') {
          swipeEnabled = swipeEnabled(state);
        }

        if (tabBarPosition === 'bottom') {
          renderFooter = this._renderTabBar;
        } else {
          renderHeader = this._renderTabBar;
        }

        if (animationEnabled === false && swipeEnabled === false) {
          renderPager = this._renderPanPager;
        }

        return React.createElement(_reactNativeTabView.TabViewAnimated, babelHelpers.extends({}, rest, {
          navigationState: navigation.state,
          animationEnabled: animationEnabled,
          swipeEnabled: swipeEnabled,
          renderPager: renderPager,
          renderHeader: renderHeader,
          renderFooter: renderFooter,
          renderScene: this._renderScene,
          __source: {
            fileName: _jsxFileName,
            lineNumber: 124
          }
        }));
      }
    }]);
    return TabView;
  }(React.PureComponent);

  TabView.defaultProps = {
    initialLayout: _reactNative.Platform.select({
      android: {
        width: 1,
        height: 0
      }
    })
  };
  exports.default = (0, _createTabNavigator2.default)(TabView);
},"920f873b2c6dfac1e53d27d987410d16",["c42a5e17831e80ed1e1c8cf91f5ddb40","cc757a791ecb3cd320f65c256a791c04","71fe908006d7688fb9fb0e7762d600b1","c237d25f932c60ab5f6da545341f2bb9","084bb08bba1c65dfbdd218e16c5e104f","8f33bf7f32df11cb95b924f216218e25"],"node_modules/react-navigation/node_modules/react-navigation-tabs/dist/navigators/createMaterialTopTabNavigator.js");