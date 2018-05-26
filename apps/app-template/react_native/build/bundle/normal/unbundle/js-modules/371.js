__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.NavigationConsumer = exports.NavigationProvider = undefined;

  var _react = _require(_dependencyMap[0], "react");

  var _react2 = babelHelpers.interopRequireDefault(_react);

  var _propTypes = _require(_dependencyMap[1], "prop-types");

  var _propTypes2 = babelHelpers.interopRequireDefault(_propTypes);

  var _createReactContext = _require(_dependencyMap[2], "create-react-context");

  var _createReactContext2 = babelHelpers.interopRequireDefault(_createReactContext);

  var NavigationContext = (0, _createReactContext2.default)();
  var NavigationProvider = exports.NavigationProvider = NavigationContext.Provider;
  var NavigationConsumer = exports.NavigationConsumer = NavigationContext.Consumer;
},371,[12,129,372],"node_modules/react-navigation/src/views/NavigationContext.js");