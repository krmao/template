__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-navigation/src/views/Header/HeaderTitle.js";

  var _react = _require(_dependencyMap[0], "react");

  var _react2 = babelHelpers.interopRequireDefault(_react);

  var _reactNative = _require(_dependencyMap[1], "react-native");

  var AnimatedText = _reactNative.Animated.Text;

  var HeaderTitle = function HeaderTitle(_ref) {
    var style = _ref.style,
        rest = babelHelpers.objectWithoutProperties(_ref, ["style"]);
    return _react2.default.createElement(AnimatedText, babelHelpers.extends({
      numberOfLines: 1
    }, rest, {
      style: [styles.title, style],
      accessibilityTraits: "header",
      __source: {
        fileName: _jsxFileName,
        lineNumber: 7
      }
    }));
  };

  var styles = _reactNative.StyleSheet.create({
    title: {
      fontSize: _reactNative.Platform.OS === 'ios' ? 17 : 20,
      fontWeight: _reactNative.Platform.OS === 'ios' ? '700' : '500',
      color: 'rgba(0, 0, 0, .9)',
      textAlign: _reactNative.Platform.OS === 'ios' ? 'center' : 'left',
      marginHorizontal: 16
    }
  });

  exports.default = HeaderTitle;
},"9b2d8d757c32a5aade25f6f151e5a821",["c42a5e17831e80ed1e1c8cf91f5ddb40","cc757a791ecb3cd320f65c256a791c04"],"node_modules/react-navigation/src/views/Header/HeaderTitle.js");