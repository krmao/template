__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-navigation/src/views/SwitchView/SwitchView.js";

  var _react = _require(_dependencyMap[0], "react");

  var _react2 = babelHelpers.interopRequireDefault(_react);

  var _SceneView = _require(_dependencyMap[1], "../SceneView");

  var _SceneView2 = babelHelpers.interopRequireDefault(_SceneView);

  var SwitchView = function (_React$Component) {
    babelHelpers.inherits(SwitchView, _React$Component);

    function SwitchView() {
      babelHelpers.classCallCheck(this, SwitchView);
      return babelHelpers.possibleConstructorReturn(this, (SwitchView.__proto__ || Object.getPrototypeOf(SwitchView)).apply(this, arguments));
    }

    babelHelpers.createClass(SwitchView, [{
      key: "render",
      value: function render() {
        var state = this.props.navigation.state;
        var activeKey = state.routes[state.index].key;
        var descriptor = this.props.descriptors[activeKey];
        var ChildComponent = descriptor.getComponent();
        return _react2.default.createElement(_SceneView2.default, {
          component: ChildComponent,
          navigation: descriptor.navigation,
          screenProps: this.props.screenProps,
          __source: {
            fileName: _jsxFileName,
            lineNumber: 12
          }
        });
      }
    }]);
    return SwitchView;
  }(_react2.default.Component);

  exports.default = SwitchView;
},"90b6bc7b10f4e683231c018cad5699c0",["c42a5e17831e80ed1e1c8cf91f5ddb40","642023f04391babe6960216152f851fe"],"node_modules/react-navigation/src/views/SwitchView/SwitchView.js");