__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-native/Libraries/Components/TabBarIOS/TabBarIOS.android.js";

  var React = _require(_dependencyMap[0], 'React');

  var StyleSheet = _require(_dependencyMap[1], 'StyleSheet');

  var TabBarItemIOS = _require(_dependencyMap[2], 'TabBarItemIOS');

  var View = _require(_dependencyMap[3], 'View');

  var DummyTabBarIOS = function (_React$Component) {
    babelHelpers.inherits(DummyTabBarIOS, _React$Component);

    function DummyTabBarIOS() {
      babelHelpers.classCallCheck(this, DummyTabBarIOS);
      return babelHelpers.possibleConstructorReturn(this, (DummyTabBarIOS.__proto__ || Object.getPrototypeOf(DummyTabBarIOS)).apply(this, arguments));
    }

    babelHelpers.createClass(DummyTabBarIOS, [{
      key: "render",
      value: function render() {
        return React.createElement(
          View,
          {
            style: [this.props.style, styles.tabGroup],
            __source: {
              fileName: _jsxFileName,
              lineNumber: 23
            }
          },
          this.props.children
        );
      }
    }]);
    return DummyTabBarIOS;
  }(React.Component);

  DummyTabBarIOS.Item = TabBarItemIOS;
  var styles = StyleSheet.create({
    tabGroup: {
      flex: 1
    }
  });
  module.exports = DummyTabBarIOS;
},"755ea8e27f579f2d7865f732fdbc28e0",["e6db4f0efed6b72f641ef0ffed29569f","d31e8c1a3f9844becc88973ecddac872","1bbf7d0cbae439e58e827cb49e484a73","30a3b04291b6e1f01b778ff31271ccc5"],"TabBarIOS");