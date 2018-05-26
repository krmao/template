__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-navigation/src/views/SceneView.js";

  var _react = _require(_dependencyMap[0], "react");

  var _react2 = babelHelpers.interopRequireDefault(_react);

  var _propTypes = _require(_dependencyMap[1], "prop-types");

  var _propTypes2 = babelHelpers.interopRequireDefault(_propTypes);

  var _NavigationContext = _require(_dependencyMap[2], "./NavigationContext");

  var SceneView = function (_React$PureComponent) {
    babelHelpers.inherits(SceneView, _React$PureComponent);

    function SceneView() {
      babelHelpers.classCallCheck(this, SceneView);
      return babelHelpers.possibleConstructorReturn(this, (SceneView.__proto__ || Object.getPrototypeOf(SceneView)).apply(this, arguments));
    }

    babelHelpers.createClass(SceneView, [{
      key: "render",
      value: function render() {
        var _props = this.props,
            screenProps = _props.screenProps,
            Component = _props.component,
            navigation = _props.navigation;
        return _react2.default.createElement(
          _NavigationContext.NavigationProvider,
          {
            value: navigation,
            __source: {
              fileName: _jsxFileName,
              lineNumber: 9
            }
          },
          _react2.default.createElement(Component, {
            screenProps: screenProps,
            navigation: navigation,
            __source: {
              fileName: _jsxFileName,
              lineNumber: 10
            }
          })
        );
      }
    }]);
    return SceneView;
  }(_react2.default.PureComponent);

  exports.default = SceneView;
},370,[12,129,371],"node_modules/react-navigation/src/views/SceneView.js");