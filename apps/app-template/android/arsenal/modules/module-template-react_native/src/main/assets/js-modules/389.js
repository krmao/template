__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });

  var _react = _require(_dependencyMap[0], "react");

  var _react2 = babelHelpers.interopRequireDefault(_react);

  var _createNavigationContainer = _require(_dependencyMap[1], "../createNavigationContainer");

  var _createNavigationContainer2 = babelHelpers.interopRequireDefault(_createNavigationContainer);

  var _createNavigator = _require(_dependencyMap[2], "../navigators/createNavigator");

  var _createNavigator2 = babelHelpers.interopRequireDefault(_createNavigator);

  var _SwitchRouter = _require(_dependencyMap[3], "../routers/SwitchRouter");

  var _SwitchRouter2 = babelHelpers.interopRequireDefault(_SwitchRouter);

  var _SwitchView = _require(_dependencyMap[4], "../views/SwitchView/SwitchView");

  var _SwitchView2 = babelHelpers.interopRequireDefault(_SwitchView);

  function createSwitchNavigator(routeConfigMap) {
    var switchConfig = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    var router = (0, _SwitchRouter2.default)(routeConfigMap, switchConfig);
    var Navigator = (0, _createNavigator2.default)(_SwitchView2.default, router, switchConfig);
    return (0, _createNavigationContainer2.default)(Navigator);
  }

  exports.default = createSwitchNavigator;
},389,[12,338,346,390,391],"node_modules/react-navigation/src/navigators/createSwitchNavigator.js");