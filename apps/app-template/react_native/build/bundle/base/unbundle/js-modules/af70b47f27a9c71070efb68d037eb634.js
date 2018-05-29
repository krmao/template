__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });

  var _invariant = _require(_dependencyMap[0], "../utils/invariant");

  var _invariant2 = babelHelpers.interopRequireDefault(_invariant);

  function validateRouteConfigMap(routeConfigs) {
    var routeNames = Object.keys(routeConfigs);
    (0, _invariant2.default)(routeNames.length > 0, 'Please specify at least one route when configuring a navigator.');
    routeNames.forEach(function (routeName) {
      var routeConfig = routeConfigs[routeName];
      var screenComponent = getScreenComponent(routeConfig);

      if (!screenComponent || typeof screenComponent !== 'function' && typeof screenComponent !== 'string' && !routeConfig.getScreen) {
        throw new Error("The component for route '" + routeName + "' must be a " + 'React component. For example:\n\n' + "import MyScreen from './MyScreen';\n" + '...\n' + (routeName + ": MyScreen,\n") + '}\n\n' + 'You can also use a navigator:\n\n' + "import MyNavigator from './MyNavigator';\n" + '...\n' + (routeName + ": MyNavigator,\n") + '}');
      }

      if (routeConfig.screen && routeConfig.getScreen) {
        throw new Error("Route '" + routeName + "' should declare a screen or " + 'a getScreen, not both.');
      }
    });
  }

  function getScreenComponent(routeConfig) {
    if (!routeConfig) {
      return null;
    }

    return routeConfig.screen ? routeConfig.screen : routeConfig;
  }

  exports.default = validateRouteConfigMap;
},"af70b47f27a9c71070efb68d037eb634",["09df40ab147e7353903f31659d93ee58"],"node_modules/react-navigation/src/routers/validateRouteConfigMap.js");