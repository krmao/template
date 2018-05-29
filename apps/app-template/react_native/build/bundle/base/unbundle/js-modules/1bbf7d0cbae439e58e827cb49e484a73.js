__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-native/Libraries/Components/TabBarIOS/TabBarItemIOS.android.js";

  var React = _require(_dependencyMap[0], 'React');

  var View = _require(_dependencyMap[1], 'View');

  var StyleSheet = _require(_dependencyMap[2], 'StyleSheet');

  var DummyTab = function (_React$Component) {
    babelHelpers.inherits(DummyTab, _React$Component);

    function DummyTab() {
      babelHelpers.classCallCheck(this, DummyTab);
      return babelHelpers.possibleConstructorReturn(this, (DummyTab.__proto__ || Object.getPrototypeOf(DummyTab)).apply(this, arguments));
    }

    babelHelpers.createClass(DummyTab, [{
      key: "render",
      value: function render() {
        if (!this.props.selected) {
          return React.createElement(View, {
            __source: {
              fileName: _jsxFileName,
              lineNumber: 19
            }
          });
        }

        return React.createElement(
          View,
          {
            style: [this.props.style, styles.tab],
            __source: {
              fileName: _jsxFileName,
              lineNumber: 22
            }
          },
          this.props.children
        );
      }
    }]);
    return DummyTab;
  }(React.Component);

  var styles = StyleSheet.create({
    tab: {
      top: 0,
      right: 0,
      bottom: 0,
      left: 0,
      borderColor: 'red',
      borderWidth: 1
    }
  });
  module.exports = DummyTab;
},"1bbf7d0cbae439e58e827cb49e484a73",["e6db4f0efed6b72f641ef0ffed29569f","30a3b04291b6e1f01b778ff31271ccc5","d31e8c1a3f9844becc88973ecddac872"],"TabBarItemIOS");