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
},"98e5c747c38d3feee7f297880784e291",["c42a5e17831e80ed1e1c8cf91f5ddb40","18eeaf4e01377a466daaccc6ba8ce6f5","1b7bbc51b6e63a0c92d8311081556013"],"node_modules/react-navigation/src/views/NavigationContext.js");