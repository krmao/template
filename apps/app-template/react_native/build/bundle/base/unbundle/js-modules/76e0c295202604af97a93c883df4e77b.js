__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-native/Libraries/Inspector/BorderBox.js";

  var React = _require(_dependencyMap[0], 'React');

  var View = _require(_dependencyMap[1], 'View');

  var BorderBox = function (_React$Component) {
    babelHelpers.inherits(BorderBox, _React$Component);

    function BorderBox() {
      babelHelpers.classCallCheck(this, BorderBox);
      return babelHelpers.possibleConstructorReturn(this, (BorderBox.__proto__ || Object.getPrototypeOf(BorderBox)).apply(this, arguments));
    }

    babelHelpers.createClass(BorderBox, [{
      key: "render",
      value: function render() {
        var box = this.props.box;

        if (!box) {
          return this.props.children;
        }

        var style = {
          borderTopWidth: box.top,
          borderBottomWidth: box.bottom,
          borderLeftWidth: box.left,
          borderRightWidth: box.right
        };
        return React.createElement(
          View,
          {
            style: [style, this.props.style],
            __source: {
              fileName: _jsxFileName,
              lineNumber: 28
            }
          },
          this.props.children
        );
      }
    }]);
    return BorderBox;
  }(React.Component);

  module.exports = BorderBox;
},"76e0c295202604af97a93c883df4e77b",["e6db4f0efed6b72f641ef0ffed29569f","30a3b04291b6e1f01b778ff31271ccc5"],"BorderBox");