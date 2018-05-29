__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-native/Libraries/Components/UnimplementedViews/UnimplementedView.js";

  var React = _require(_dependencyMap[0], 'React');

  var StyleSheet = _require(_dependencyMap[1], 'StyleSheet');

  var UnimplementedView = function (_React$Component) {
    babelHelpers.inherits(UnimplementedView, _React$Component);

    function UnimplementedView() {
      babelHelpers.classCallCheck(this, UnimplementedView);
      return babelHelpers.possibleConstructorReturn(this, (UnimplementedView.__proto__ || Object.getPrototypeOf(UnimplementedView)).apply(this, arguments));
    }

    babelHelpers.createClass(UnimplementedView, [{
      key: "setNativeProps",
      value: function setNativeProps() {}
    }, {
      key: "render",
      value: function render() {
        var View = _require(_dependencyMap[2], 'View');

        return React.createElement(
          View,
          {
            style: [styles.unimplementedView, this.props.style],
            __source: {
              fileName: _jsxFileName,
              lineNumber: 31
            }
          },
          this.props.children
        );
      }
    }]);
    return UnimplementedView;
  }(React.Component);

  var styles = StyleSheet.create({
    unimplementedView: __DEV__ ? {
      alignSelf: 'flex-start',
      borderColor: 'red',
      borderWidth: 1
    } : {}
  });
  module.exports = UnimplementedView;
},"2048fde248982a55be3eceb4c402bf60",["e6db4f0efed6b72f641ef0ffed29569f","d31e8c1a3f9844becc88973ecddac872","30a3b04291b6e1f01b778ff31271ccc5"],"UnimplementedView");