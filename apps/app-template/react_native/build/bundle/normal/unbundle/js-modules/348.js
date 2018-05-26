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
},348,[12,338,349,346,350,381],"node_modules/react-navigation/src/navigators/createStackNavigator.js");