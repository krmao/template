__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });

  var _react = _require(_dependencyMap[0], "react");

  var React = babelHelpers.interopRequireWildcard(_react);

  var _createNavigationContainer = _require(_dependencyMap[1], "../createNavigationContainer");

  var _createNavigationContainer2 = babelHelpers.interopRequireDefault(_createNavigationContainer);

  var _createKeyboardAwareNavigator = _require(_dependencyMap[2], "./createKeyboardAwareNavigator");

  var _createKeyboardAwareNavigator2 = babelHelpers.interopRequireDefault(_createKeyboardAwareNavigator);

  var _createNavigator = _require(_dependencyMap[3], "./createNavigator");

  var _createNavigator2 = babelHelpers.interopRequireDefault(_createNavigator);

  var _StackView = _require(_dependencyMap[4], "../views/StackView/StackView");

  var _StackView2 = babelHelpers.interopRequireDefault(_StackView);

  var _StackRouter = _require(_dependencyMap[5], "../routers/StackRouter");

  var _StackRouter2 = babelHelpers.interopRequireDefault(_StackRouter);

  function createStackNavigator(routeConfigMap) {
    var stackConfig = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    var initialRouteKey = stackConfig.initialRouteKey,
        initialRouteName = stackConfig.initialRouteName,
        initialRouteParams = stackConfig.initialRouteParams,
        paths = stackConfig.paths,
        navigationOptions = stackConfig.navigationOptions,
        disableKeyboardHandling = stackConfig.disableKeyboardHandling;
    var stackRouterConfig = {
      initialRouteKey: initialRouteKey,
      initialRouteName: initialRouteName,
      initialRouteParams: initialRouteParams,
      paths: paths,
      navigationOptions: navigationOptions
    };
    var router = (0, _StackRouter2.default)(routeConfigMap, stackRouterConfig);
    var Navigator = (0, _createNavigator2.default)(_StackView2.default, router, stackConfig);

    if (!disableKeyboardHandling) {
      Navigator = (0, _createKeyboardAwareNavigator2.default)(Navigator);
    }

    return (0, _createNavigationContainer2.default)(Navigator);
  }

  exports.default = createStackNavigator;
},"53633159adb3fa4b6e34bb38c107c293",["c42a5e17831e80ed1e1c8cf91f5ddb40","2682705ec52ed4b42777ccfb240eb792","898c82a210fc063cc8e3896490509f64","80bc0b30071534809c7bc11f7396fe43","e4b2ccf4e724c26e8054886e2cde54c1","b0a0a8203dafaf12fbd104b343b6497a"],"node_modules/react-navigation/src/navigators/createStackNavigator.js");