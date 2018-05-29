__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-native/Libraries/Components/DatePicker/DatePickerIOS.android.js";

  var React = _require(_dependencyMap[0], 'React');

  var StyleSheet = _require(_dependencyMap[1], 'StyleSheet');

  var Text = _require(_dependencyMap[2], 'Text');

  var View = _require(_dependencyMap[3], 'View');

  var DummyDatePickerIOS = function (_React$Component) {
    babelHelpers.inherits(DummyDatePickerIOS, _React$Component);

    function DummyDatePickerIOS() {
      babelHelpers.classCallCheck(this, DummyDatePickerIOS);
      return babelHelpers.possibleConstructorReturn(this, (DummyDatePickerIOS.__proto__ || Object.getPrototypeOf(DummyDatePickerIOS)).apply(this, arguments));
    }

    babelHelpers.createClass(DummyDatePickerIOS, [{
      key: "render",
      value: function render() {
        return React.createElement(
          View,
          {
            style: [styles.dummyDatePickerIOS, this.props.style],
            __source: {
              fileName: _jsxFileName,
              lineNumber: 20
            }
          },
          React.createElement(
            Text,
            {
              style: styles.datePickerText,
              __source: {
                fileName: _jsxFileName,
                lineNumber: 21
              }
            },
            "DatePickerIOS is not supported on this platform!"
          )
        );
      }
    }]);
    return DummyDatePickerIOS;
  }(React.Component);

  var styles = StyleSheet.create({
    dummyDatePickerIOS: {
      height: 100,
      width: 300,
      backgroundColor: '#ffbcbc',
      borderWidth: 1,
      borderColor: 'red',
      alignItems: 'center',
      justifyContent: 'center',
      margin: 10
    },
    datePickerText: {
      color: '#333333',
      margin: 20
    }
  });
  module.exports = DummyDatePickerIOS;
},"35091c3024add07f1b7acf79457ee9eb",["e6db4f0efed6b72f641ef0ffed29569f","d31e8c1a3f9844becc88973ecddac872","c03ca8878a60b3cdaf32e10931ff258d","30a3b04291b6e1f01b778ff31271ccc5"],"DatePickerIOS");