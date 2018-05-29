__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });

  var _invariant = _require(_dependencyMap[0], "../utils/invariant");

  var _invariant2 = babelHelpers.interopRequireDefault(_invariant);

  var _getScreenForRouteName = _require(_dependencyMap[1], "./getScreenForRouteName");

  var _getScreenForRouteName2 = babelHelpers.interopRequireDefault(_getScreenForRouteName);

  var _validateScreenOptions = _require(_dependencyMap[2], "./validateScreenOptions");

  var _validateScreenOptions2 = babelHelpers.interopRequireDefault(_validateScreenOptions);

  function applyConfig(configurer, navigationOptions, configProps) {
    if (typeof configurer === 'function') {
      return babelHelpers.extends({}, navigationOptions, configurer(babelHelpers.extends({}, configProps, {
        navigationOptions: navigationOptions
      })));
    }

    if (typeof configurer === 'object') {
      return babelHelpers.extends({}, navigationOptions, configurer);
    }

    return navigationOptions;
  }

  exports.default = function (routeConfigs, navigatorScreenConfig) {
    return function (navigation, screenProps) {
      var state = navigation.state,
          dispatch = navigation.dispatch;
      var route = state;
      (0, _invariant2.default)(route.routeName && typeof route.routeName === 'string', 'Cannot get config because the route does not have a routeName.');
      var Component = (0, _getScreenForRouteName2.default)(routeConfigs, route.routeName);
      var routeConfig = routeConfigs[route.routeName];
      var routeScreenConfig = routeConfig === Component ? null : routeConfig.navigationOptions;
      var componentScreenConfig = Component.navigationOptions;
      var configOptions = {
        navigation: navigation,
        screenProps: screenProps || {}
      };
      var outputConfig = applyConfig(navigatorScreenConfig, {}, configOptions);
      outputConfig = applyConfig(componentScreenConfig, outputConfig, configOptions);
      outputConfig = applyConfig(routeScreenConfig, outputConfig, configOptions);
      (0, _validateScreenOptions2.default)(outputConfig, route);
      return outputConfig;
    };
  };
},"b4f5a74289f659543a354c90625eb19f",["09df40ab147e7353903f31659d93ee58","6405153adc2f639554606506cfaae361","3e2d6d17cc819a32f7afe1a74e40b3e1"],"node_modules/react-navigation/src/routers/createConfigGetter.js");