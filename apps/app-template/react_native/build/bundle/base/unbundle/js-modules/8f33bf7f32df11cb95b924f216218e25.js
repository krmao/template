__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-navigation/node_modules/react-navigation-tabs/dist/views/ResourceSavingScene.js";

  var _react = _require(_dependencyMap[0], "react");

  var React = babelHelpers.interopRequireWildcard(_react);

  var _reactNative = _require(_dependencyMap[1], "react-native");

  var FAR_FAR_AWAY = 3000;

  var ResourceSavingScene = function (_React$Component) {
    babelHelpers.inherits(ResourceSavingScene, _React$Component);

    function ResourceSavingScene() {
      babelHelpers.classCallCheck(this, ResourceSavingScene);
      return babelHelpers.possibleConstructorReturn(this, (ResourceSavingScene.__proto__ || Object.getPrototypeOf(ResourceSavingScene)).apply(this, arguments));
    }

    babelHelpers.createClass(ResourceSavingScene, [{
      key: "render",
      value: function render() {
        var _props = this.props,
            isFocused = _props.isFocused,
            children = _props.children,
            style = _props.style,
            rest = babelHelpers.objectWithoutProperties(_props, ["isFocused", "children", "style"]);
        return React.createElement(
          _reactNative.View,
          babelHelpers.extends({
            style: [styles.container, style],
            collapsable: false,
            removeClippedSubviews: _reactNative.Platform.OS === 'ios' ? !isFocused : true,
            pointerEvents: isFocused ? 'auto' : 'none'
          }, rest, {
            __source: {
              fileName: _jsxFileName,
              lineNumber: 10
            }
          }),
          React.createElement(
            _reactNative.View,
            {
              style: isFocused ? styles.attached : styles.detached,
              __source: {
                fileName: _jsxFileName,
                lineNumber: 14
              }
            },
            children
          )
        );
      }
    }]);
    return ResourceSavingScene;
  }(React.Component);

  exports.default = ResourceSavingScene;

  var styles = _reactNative.StyleSheet.create({
    container: {
      flex: 1,
      overflow: 'hidden'
    },
    attached: {
      flex: 1
    },
    detached: {
      flex: 1,
      top: FAR_FAR_AWAY
    }
  });
},"8f33bf7f32df11cb95b924f216218e25",["c42a5e17831e80ed1e1c8cf91f5ddb40","cc757a791ecb3cd320f65c256a791c04"],"node_modules/react-navigation/node_modules/react-navigation-tabs/dist/views/ResourceSavingScene.js");