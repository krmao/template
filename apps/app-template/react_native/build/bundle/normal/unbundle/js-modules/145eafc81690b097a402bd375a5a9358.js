__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-native/Libraries/Inspector/BoxInspector.js";

  var React = _require(_dependencyMap[0], 'React');

  var StyleSheet = _require(_dependencyMap[1], 'StyleSheet');

  var Text = _require(_dependencyMap[2], 'Text');

  var View = _require(_dependencyMap[3], 'View');

  var resolveBoxStyle = _require(_dependencyMap[4], 'resolveBoxStyle');

  var blank = {
    top: 0,
    left: 0,
    right: 0,
    bottom: 0
  };

  var BoxInspector = function (_React$Component) {
    babelHelpers.inherits(BoxInspector, _React$Component);

    function BoxInspector() {
      babelHelpers.classCallCheck(this, BoxInspector);
      return babelHelpers.possibleConstructorReturn(this, (BoxInspector.__proto__ || Object.getPrototypeOf(BoxInspector)).apply(this, arguments));
    }

    babelHelpers.createClass(BoxInspector, [{
      key: "render",
      value: function render() {
        var frame = this.props.frame;
        var style = this.props.style;
        var margin = style && resolveBoxStyle('margin', style) || blank;
        var padding = style && resolveBoxStyle('padding', style) || blank;
        return React.createElement(
          BoxContainer,
          {
            title: "margin",
            titleStyle: styles.marginLabel,
            box: margin,
            __source: {
              fileName: _jsxFileName,
              lineNumber: 32
            }
          },
          React.createElement(
            BoxContainer,
            {
              title: "padding",
              box: padding,
              __source: {
                fileName: _jsxFileName,
                lineNumber: 33
              }
            },
            React.createElement(
              View,
              {
                __source: {
                  fileName: _jsxFileName,
                  lineNumber: 34
                }
              },
              React.createElement(
                Text,
                {
                  style: styles.innerText,
                  __source: {
                    fileName: _jsxFileName,
                    lineNumber: 35
                  }
                },
                "(",
                (frame.left || 0).toFixed(1),
                ", ",
                (frame.top || 0).toFixed(1),
                ")"
              ),
              React.createElement(
                Text,
                {
                  style: styles.innerText,
                  __source: {
                    fileName: _jsxFileName,
                    lineNumber: 38
                  }
                },
                (frame.width || 0).toFixed(1),
                " \xD7 ",
                (frame.height || 0).toFixed(1)
              )
            )
          )
        );
      }
    }]);
    return BoxInspector;
  }(React.Component);

  var BoxContainer = function (_React$Component2) {
    babelHelpers.inherits(BoxContainer, _React$Component2);

    function BoxContainer() {
      babelHelpers.classCallCheck(this, BoxContainer);
      return babelHelpers.possibleConstructorReturn(this, (BoxContainer.__proto__ || Object.getPrototypeOf(BoxContainer)).apply(this, arguments));
    }

    babelHelpers.createClass(BoxContainer, [{
      key: "render",
      value: function render() {
        var box = this.props.box;
        return React.createElement(
          View,
          {
            style: styles.box,
            __source: {
              fileName: _jsxFileName,
              lineNumber: 52
            }
          },
          React.createElement(
            View,
            {
              style: styles.row,
              __source: {
                fileName: _jsxFileName,
                lineNumber: 53
              }
            },
            React.createElement(
              Text,
              {
                style: [this.props.titleStyle, styles.label],
                __source: {
                  fileName: _jsxFileName,
                  lineNumber: 56
                }
              },
              this.props.title
            ),
            React.createElement(
              Text,
              {
                style: styles.boxText,
                __source: {
                  fileName: _jsxFileName,
                  lineNumber: 57
                }
              },
              box.top
            )
          ),
          React.createElement(
            View,
            {
              style: styles.row,
              __source: {
                fileName: _jsxFileName,
                lineNumber: 59
              }
            },
            React.createElement(
              Text,
              {
                style: styles.boxText,
                __source: {
                  fileName: _jsxFileName,
                  lineNumber: 60
                }
              },
              box.left
            ),
            this.props.children,
            React.createElement(
              Text,
              {
                style: styles.boxText,
                __source: {
                  fileName: _jsxFileName,
                  lineNumber: 62
                }
              },
              box.right
            )
          ),
          React.createElement(
            Text,
            {
              style: styles.boxText,
              __source: {
                fileName: _jsxFileName,
                lineNumber: 64
              }
            },
            box.bottom
          )
        );
      }
    }]);
    return BoxContainer;
  }(React.Component);

  var styles = StyleSheet.create({
    row: {
      flexDirection: 'row',
      alignItems: 'center',
      justifyContent: 'space-around'
    },
    marginLabel: {
      width: 60
    },
    label: {
      fontSize: 10,
      color: 'rgb(255,100,0)',
      marginLeft: 5,
      flex: 1,
      textAlign: 'left',
      top: -3
    },
    buffer: {
      fontSize: 10,
      color: 'yellow',
      flex: 1,
      textAlign: 'center'
    },
    innerText: {
      color: 'yellow',
      fontSize: 12,
      textAlign: 'center',
      width: 70
    },
    box: {
      borderWidth: 1,
      borderColor: 'grey'
    },
    boxText: {
      color: 'white',
      fontSize: 12,
      marginHorizontal: 3,
      marginVertical: 2,
      textAlign: 'center'
    }
  });
  module.exports = BoxInspector;
},"145eafc81690b097a402bd375a5a9358",["e6db4f0efed6b72f641ef0ffed29569f","d31e8c1a3f9844becc88973ecddac872","c03ca8878a60b3cdaf32e10931ff258d","30a3b04291b6e1f01b778ff31271ccc5","91ca9ebf378ea906277ffee2784ae076"],"BoxInspector");