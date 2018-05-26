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
},289,[132,171,185,173],"ProgressViewIOS");