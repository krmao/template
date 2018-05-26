__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-navigation/node_modules/react-navigation-tabs/node_modules/react-native-tab-view/src/SceneMap.js";
  exports.default = SceneMap;

  var _react = _require(_dependencyMap[0], "react");

  var React = babelHelpers.interopRequireWildcard(_react);

  function SceneMap(scenes) {
    var SceneComponent = function (_React$PureComponent) {
      babelHelpers.inherits(SceneComponent, _React$PureComponent);

      function SceneComponent() {
        babelHelpers.classCallCheck(this, SceneComponent);
        return babelHelpers.possibleConstructorReturn(this, (SceneComponent.__proto__ || Object.getPrototypeOf(SceneComponent)).apply(this, arguments));
      }

      babelHelpers.createClass(SceneComponent, [{
        key: "render",
        value: function render() {
          return React.createElement(scenes[this.props.route.key], this.props);
        }
      }]);
      return SceneComponent;
    }(React.PureComponent);

    return function (_ref) {
      var route = _ref.route,
          jumpTo = _ref.jumpTo,
          jumpToIndex = _ref.jumpToIndex;
      return React.createElement(SceneComponent, {
        key: route.key,
        route: route,
        jumpTo: jumpTo,
        jumpToIndex: jumpToIndex,
        __source: {
          fileName: _jsxFileName,
          lineNumber: 13
        }
      });
    };
  }
},436,[12],"node_modules/react-navigation/node_modules/react-navigation-tabs/node_modules/react-native-tab-view/src/SceneMap.js");