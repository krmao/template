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
},302,[132,171,303,173],"TabBarIOS");