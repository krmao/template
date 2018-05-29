__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-native/Libraries/Components/ProgressViewIOS/ProgressViewIOS.android.js";

  var React = _require(_dependencyMap[0], 'React');

  var StyleSheet = _require(_dependencyMap[1], 'StyleSheet');

  var Text = _require(_dependencyMap[2], 'Text');

  var View = _require(_dependencyMap[3], 'View');

  var DummyProgressViewIOS = function (_React$Component) {
    babelHelpers.inherits(DummyProgressViewIOS, _React$Component);

    function DummyProgressViewIOS() {
      babelHelpers.classCallCheck(this, DummyProgressViewIOS);
      return babelHelpers.possibleConstructorReturn(this, (DummyProgressViewIOS.__proto__ || Object.getPrototypeOf(DummyProgressViewIOS)).apply(this, arguments));
    }

    babelHelpers.createClass(DummyProgressViewIOS, [{
      key: "render",
      value: function render() {
        return React.createElement(
          View,
          {
            style: [styles.dummy, this.props.style],
            __source: {
              fileName: _jsxFileName,
              lineNumber: 21
            }
          },
          React.createElement(
            Text,
            {
              style: styles.text,
              __source: {
                fileName: _jsxFileName,
                lineNumber: 22
              }
            },
            "ProgressViewIOS is not supported on this platform!"
          )
        );
      }
    }]);
    return DummyProgressViewIOS;
  }(React.Component);

  var styles = StyleSheet.create({
    dummy: {
      width: 120,
      height: 20,
      backgroundColor: '#ffbcbc',
      borderWidth: 1,
      borderColor: 'red',
      alignItems: 'center',
      justifyContent: 'center'
    },
    text: {
      color: '#333333',
      margin: 5,
      fontSize: 10
    }
  });
  module.exports = DummyProgressViewIOS;
},"c1db3fdd78db6eff9d7ad78819e5910b",["e6db4f0efed6b72f641ef0ffed29569f","d31e8c1a3f9844becc88973ecddac872","c03ca8878a60b3cdaf32e10931ff258d","30a3b04291b6e1f01b778ff31271ccc5"],"ProgressViewIOS");