__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-native/Libraries/Inspector/PerformanceOverlay.js";

  var PerformanceLogger = _require(_dependencyMap[0], 'PerformanceLogger');

  var React = _require(_dependencyMap[1], 'React');

  var StyleSheet = _require(_dependencyMap[2], 'StyleSheet');

  var Text = _require(_dependencyMap[3], 'Text');

  var View = _require(_dependencyMap[4], 'View');

  var PerformanceOverlay = function (_React$Component) {
    babelHelpers.inherits(PerformanceOverlay, _React$Component);

    function PerformanceOverlay() {
      babelHelpers.classCallCheck(this, PerformanceOverlay);
      return babelHelpers.possibleConstructorReturn(this, (PerformanceOverlay.__proto__ || Object.getPrototypeOf(PerformanceOverlay)).apply(this, arguments));
    }

    babelHelpers.createClass(PerformanceOverlay, [{
      key: "render",
      value: function render() {
        var perfLogs = PerformanceLogger.getTimespans();
        var items = [];

        for (var key in perfLogs) {
          if (perfLogs[key].totalTime) {
            var unit = key === 'BundleSize' ? 'b' : 'ms';
            items.push(React.createElement(
              View,
              {
                style: styles.row,
                key: key,
                __source: {
                  fileName: _jsxFileName,
                  lineNumber: 27
                }
              },
              React.createElement(
                Text,
                {
                  style: [styles.text, styles.label],
                  __source: {
                    fileName: _jsxFileName,
                    lineNumber: 28
                  }
                },
                key
              ),
              React.createElement(
                Text,
                {
                  style: [styles.text, styles.totalTime],
                  __source: {
                    fileName: _jsxFileName,
                    lineNumber: 29
                  }
                },
                perfLogs[key].totalTime + unit
              )
            ));
          }
        }

        return React.createElement(
          View,
          {
            style: styles.container,
            __source: {
              fileName: _jsxFileName,
              lineNumber: 38
            }
          },
          items
        );
      }
    }]);
    return PerformanceOverlay;
  }(React.Component);

  var styles = StyleSheet.create({
    container: {
      height: 100,
      paddingTop: 10
    },
    label: {
      flex: 1
    },
    row: {
      flexDirection: 'row',
      paddingHorizontal: 10
    },
    text: {
      color: 'white',
      fontSize: 12
    },
    totalTime: {
      paddingRight: 100
    }
  });
  module.exports = PerformanceOverlay;
},281,[104,132,171,185,173],"PerformanceOverlay");