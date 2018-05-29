__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });

  var _react = _require(_dependencyMap[0], "react");

  var _react2 = babelHelpers.interopRequireDefault(_react);

  var _reactNative = _require(_dependencyMap[1], "react-native");

  var _reactNavigation = _require(_dependencyMap[2], "react-navigation");

  var _TabView = _require(_dependencyMap[3], "./views/TabView");

  var _TabView2 = babelHelpers.interopRequireDefault(_TabView);

  var _TabBarTop = _require(_dependencyMap[4], "./views/TabBarTop");

  var _TabBarTop2 = babelHelpers.interopRequireDefault(_TabBarTop);

  var _TabBarBottom = _require(_dependencyMap[5], "./views/TabBarBottom");

  var _TabBarBottom2 = babelHelpers.interopRequireDefault(_TabBarBottom);

  var createTabNavigator = function createTabNavigator(routeConfigs) {
    var config = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    var tabsConfig = babelHelpers.extends({}, createTabNavigator.Presets.Default, config);
    var router = (0, _reactNavigation.TabRouter)(routeConfigs, tabsConfig);
    var navigator = (0, _reactNavigation.createNavigator)(_TabView2.default, router, tabsConfig);
    return (0, _reactNavigation.createNavigationContainer)(navigator);
  };

  var Presets = {
    iOSBottomTabs: {
      tabBarComponent: _TabBarBottom2.default,
      tabBarPosition: 'bottom',
      swipeEnabled: false,
      animationEnabled: false,
      initialLayout: undefined
    },
    AndroidTopTabs: {
      tabBarComponent: _TabBarTop2.default,
      tabBarPosition: 'top',
      swipeEnabled: true,
      animationEnabled: true,
      initialLayout: undefined
    }
  };
  createTabNavigator.Presets = {
    iOSBottomTabs: Presets.iOSBottomTabs,
    AndroidTopTabs: Presets.AndroidTopTabs,
    Default: _reactNative.Platform.OS === 'ios' ? Presets.iOSBottomTabs : Presets.AndroidTopTabs
  };
  exports.default = createTabNavigator;
},"6e8427944356dd97e2c1f1886eecfbcd",["c42a5e17831e80ed1e1c8cf91f5ddb40","cc757a791ecb3cd320f65c256a791c04","3cedea3227936aea9cc98d4a11eaf70c","0cae5872e45c02bf0136566e7ced9b0d","524637b4103a08a14246fb7a134fc448","3b6ec151a732138fe2d98b4f7aee435a"],"node_modules/react-navigation/node_modules/react-navigation-deprecated-tab-navigator/src/createTabNavigator.js");