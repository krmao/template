__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-native/Libraries/Components/SegmentedControlIOS/SegmentedControlIOS.android.js";

  var React = _require(_dependencyMap[0], 'React');

  var StyleSheet = _require(_dependencyMap[1], 'StyleSheet');

  var Text = _require(_dependencyMap[2], 'Text');

  var View = _require(_dependencyMap[3], 'View');

  var DummySegmentedControlIOS = function (_React$Component) {
    babelHelpers.inherits(DummySegmentedControlIOS, _React$Component);

    function DummySegmentedControlIOS() {
      babelHelpers.classCallCheck(this, DummySegmentedControlIOS);
      return babelHelpers.possibleConstructorReturn(this, (DummySegmentedControlIOS.__proto__ || Object.getPrototypeOf(DummySegmentedControlIOS)).apply(this, arguments));
    }

    babelHelpers.createClass(DummySegmentedControlIOS, [{
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
            "SegmentedControlIOS is not supported on this platform!"
          )
        );
      }
    }]);
    return DummySegmentedControlIOS;
  }(React.Component);

  var styles = StyleSheet.create({
    dummy: {
      width: 120,
      height: 50,
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
  module.exports = DummySegmentedControlIOS;
},"310fd40dcc3ce7a36765eae667b2cd82",["e6db4f0efed6b72f641ef0ffed29569f","d31e8c1a3f9844becc88973ecddac872","c03ca8878a60b3cdaf32e10931ff258d","30a3b04291b6e1f01b778ff31271ccc5"],"SegmentedControlIOS");