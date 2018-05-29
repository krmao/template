__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = getScreenForRouteName;

  var _invariant = _require(_dependencyMap[0], "../utils/invariant");

  var _invariant2 = babelHelpers.interopRequireDefault(_invariant);

  function getScreenForRouteName(routeConfigs, routeName) {
    var routeConfig = routeConfigs[routeName];

    if (!routeConfig) {
      throw new Error("There is no route defined for key " + routeName + ".\n" + ("Must be one of: " + Object.keys(routeConfigs).map(function (a) {
        return "'" + a + "'";
      }).join(',')));
    }

    if (routeConfig.screen) {
      return routeConfig.screen;
    }

    if (typeof routeConfig.getScreen === 'function') {
      var screen = routeConfig.getScreen();
      (0, _invariant2.default)(typeof screen === 'function', "The getScreen defined for route '" + routeName + " didn't return a valid " + 'screen or navigator.\n\n' + 'Please pass it like this:\n' + (routeName + ": {\n  getScreen: () => require('./MyScreen').default\n}"));
      return screen;
    }

    return routeConfig;
  }
},"6405153adc2f639554606506cfaae361",["09df40ab147e7353903f31659d93ee58"],"node_modules/react-navigation/src/routers/getScreenForRouteName.js");