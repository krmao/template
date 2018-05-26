__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });

  var _SwitchRouter = _require(_dependencyMap[0], "./SwitchRouter");

  var _SwitchRouter2 = babelHelpers.interopRequireDefault(_SwitchRouter);

  var _withDefaultValue = _require(_dependencyMap[1], "../utils/withDefaultValue");

  var _withDefaultValue2 = babelHelpers.interopRequireDefault(_withDefaultValue);

  exports.default = function (routeConfigs) {
    var config = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    config = babelHelpers.extends({}, config);
    config = (0, _withDefaultValue2.default)(config, 'resetOnBlur', false);
    config = (0, _withDefaultValue2.default)(config, 'backBehavior', 'initialRoute');
    var switchRouter = (0, _SwitchRouter2.default)(routeConfigs, config);
    return switchRouter;
  };
},438,[390,394],"node_modules/react-navigation/src/routers/TabRouter.js");