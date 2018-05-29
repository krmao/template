__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-navigation/node_modules/react-navigation-deprecated-tab-navigator/src/views/TabView.js";

  var _react = _require(_dependencyMap[0], "react");

  var _react2 = babelHelpers.interopRequireDefault(_react);

  var _reactNative = _require(_dependencyMap[1], "react-native");

  var _reactNativeTabView = _require(_dependencyMap[2], "react-native-tab-view");

  var _reactNavigation = _require(_dependencyMap[3], "react-navigation");

  var TabView = function (_React$PureComponent) {
    babelHelpers.inherits(TabView, _React$PureComponent);

    function TabView() {
      var _ref;

      var _temp, _this, _ret;

      babelHelpers.classCallCheck(this, TabView);

      for (var _len = arguments.length, args = Array(_len), _key = 0; _key < _len; _key++) {
        args[_key] = arguments[_key];
      }

      return _ret = (_temp = (_this = babelHelpers.possibleConstructorReturn(this, (_ref = TabView.__proto__ || Object.getPrototypeOf(TabView)).call.apply(_ref, [this].concat(args))), _this), _this._handlePageChanged = function (index) {
        var routeName = _this.props.navigation.state.routes[index].routeName;

        _this.props.navigation.dispatch(_reactNavigation.NavigationActions.navigate({
          routeName: routeName
        }));
      }, _this._renderScene = function (_ref2) {
        var route = _ref2.route;
        var _this$props = _this.props,
            screenProps = _this$props.screenProps,
            navigation = _this$props.navigation,
            descriptors = _this$props.descriptors;
        var _this$props$navigatio = _this.props.navigationConfig,
            lazy = _this$props$navigatio.lazy,
            removeClippedSubviews = _this$props$navigatio.removeClippedSubviews,
            animationEnabled = _this$props$navigatio.animationEnabled,
            swipeEnabled = _this$props$navigatio.swipeEnabled;
        var descriptor = descriptors[route.key];
        var focusedIndex = navigation.state.index;
        var focusedKey = navigation.state.routes[focusedIndex].key;
        var key = route.key;
        var TabComponent = descriptor.getComponent();
        return _react2.default.createElement(_reactNavigation.ResourceSavingSceneView, {
          lazy: lazy,
          isFocused: focusedKey === key,
          removeClippedSubViews: removeClippedSubviews,
          animationEnabled: animationEnabled,
          swipeEnabled: swipeEnabled,
          screenProps: screenProps,
          component: TabComponent,
          navigation: navigation,
          childNavigation: descriptor.navigation,
          __source: {
            fileName: _jsxFileName,
            lineNumber: 35
          }
        });
      }, _this._getLabel = function (_ref3) {
        var route = _ref3.route,
            tintColor = _ref3.tintColor,
            focused = _ref3.focused;
        var _this$props2 = _this.props,
            screenProps = _this$props2.screenProps,
            descriptors = _this$props2.descriptors;
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
      }, _this._getOnPress = function (previousScene, _ref4) {
        var route = _ref4.route;
        var descriptors = _this.props.descriptors;
        var descriptor = descriptors[route.key];
        var options = descriptor.options;
        return options.tabBarOnPress;
      }, _this._getTestIDProps = function (_ref5) {
        var route = _ref5.route;
        var descriptors = _this.props.descriptors;
        var descriptor = descriptors[route.key];
        var options = descriptor.options;
        return typeof options.tabBarTestIDProps === 'function' ? options.tabBarTestIDProps({
          focused: focused
        }) : options.tabBarTestIDProps;
      }, _this._renderIcon = function (_ref6) {
        var focused = _ref6.focused,
            route = _ref6.route,
            tintColor = _ref6.tintColor;
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
        var _this$props$navigatio2 = _this.props.navigationConfig,
            tabBarOptions = _this$props$navigatio2.tabBarOptions,
            TabBarComponent = _this$props$navigatio2.tabBarComponent,
            animationEnabled = _this$props$navigatio2.animationEnabled,
            tabBarPosition = _this$props$navigatio2.tabBarPosition;

        if (typeof TabBarComponent === 'undefined') {
          return null;
        }

        return _react2.default.createElement(TabBarComponent, babelHelpers.extends({}, props, tabBarOptions, {
          tabBarPosition: tabBarPosition,
          screenProps: _this.props.screenProps,
          navigation: _this.props.navigation,
          getLabel: _this._getLabel,
          getTestIDProps: _this._getTestIDProps,
          getOnPress: _this._getOnPress,
          renderIcon: _this._renderIcon,
          animationEnabled: animationEnabled,
          __source: {
            fileName: _jsxFileName,
            lineNumber: 110
          }
        }));
      }, _this._renderPager = function (props) {
        return _react2.default.createElement(_reactNativeTabView.TabViewPagerPan, babelHelpers.extends({}, props, {
          __source: {
            fileName: _jsxFileName,
            lineNumber: 125
          }
        }));
      }, _temp), babelHelpers.possibleConstructorReturn(_this, _ret);
    }

    babelHelpers.createClass(TabView, [{
      key: "render",
      value: function render() {
        var _props$navigationConf = this.props.navigationConfig,
            tabBarComponent = _props$navigationConf.tabBarComponent,
            tabBarPosition = _props$navigationConf.tabBarPosition,
            animationEnabled = _props$navigationConf.animationEnabled,
            configureTransition = _props$navigationConf.configureTransition,
            initialLayout = _props$navigationConf.initialLayout;
        var renderHeader = void 0;
        var renderFooter = void 0;
        var renderPager = void 0;
        var state = this.props.navigation.state;
        var route = state.routes[state.index];
        var descriptors = this.props.descriptors;
        var descriptor = descriptors[route.key];
        var options = descriptor.options;
        var tabBarVisible = options.tabBarVisible == null ? true : options.tabBarVisible;
        var swipeEnabled = options.swipeEnabled == null ? this.props.navigationConfig.swipeEnabled : options.swipeEnabled;

        if (typeof swipeEnabled === 'function') {
          swipeEnabled = swipeEnabled(state);
        }

        if (tabBarComponent !== undefined && tabBarVisible) {
          if (tabBarPosition === 'bottom') {
            renderFooter = this._renderTabBar;
          } else {
            renderHeader = this._renderTabBar;
          }
        }

        if (animationEnabled === false && swipeEnabled === false || typeof configureTransition === 'function') {
          renderPager = this._renderPager;
        }

        var props = {
          initialLayout: initialLayout,
          animationEnabled: animationEnabled,
          configureTransition: configureTransition,
          swipeEnabled: swipeEnabled,
          renderPager: renderPager,
          renderHeader: renderHeader,
          renderFooter: renderFooter,
          renderScene: this._renderScene,
          onIndexChange: this._handlePageChanged,
          navigationState: this.props.navigation.state,
          style: styles.container
        };
        return _react2.default.createElement(_reactNativeTabView.TabViewAnimated, babelHelpers.extends({}, props, {
          __source: {
            fileName: _jsxFileName,
            lineNumber: 187
          }
        }));
      }
    }]);
    return TabView;
  }(_react2.default.PureComponent);

  TabView.defaultProps = {
    lazy: true,
    removedClippedSubviews: true,
    initialLayout: _reactNative.Platform.select({
      android: {
        width: 1,
        height: 0
      }
    })
  };
  exports.default = TabView;

  var styles = _reactNative.StyleSheet.create({
    container: {
      flex: 1
    }
  });
},"0cae5872e45c02bf0136566e7ced9b0d",["c42a5e17831e80ed1e1c8cf91f5ddb40","cc757a791ecb3cd320f65c256a791c04","e5951afd0a464307310339f6e20fba72","3cedea3227936aea9cc98d4a11eaf70c"],"node_modules/react-navigation/node_modules/react-navigation-deprecated-tab-navigator/src/views/TabView.js");