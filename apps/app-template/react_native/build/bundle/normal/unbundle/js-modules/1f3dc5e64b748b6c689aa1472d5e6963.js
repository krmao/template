__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-native/Libraries/Inspector/InspectorPanel.js";

  var ElementProperties = _require(_dependencyMap[0], 'ElementProperties');

  var NetworkOverlay = _require(_dependencyMap[1], 'NetworkOverlay');

  var PerformanceOverlay = _require(_dependencyMap[2], 'PerformanceOverlay');

  var React = _require(_dependencyMap[3], 'React');

  var PropTypes = _require(_dependencyMap[4], 'prop-types');

  var ScrollView = _require(_dependencyMap[5], 'ScrollView');

  var StyleSheet = _require(_dependencyMap[6], 'StyleSheet');

  var Text = _require(_dependencyMap[7], 'Text');

  var TouchableHighlight = _require(_dependencyMap[8], 'TouchableHighlight');

  var View = _require(_dependencyMap[9], 'View');

  var InspectorPanel = function (_React$Component) {
    babelHelpers.inherits(InspectorPanel, _React$Component);

    function InspectorPanel() {
      babelHelpers.classCallCheck(this, InspectorPanel);
      return babelHelpers.possibleConstructorReturn(this, (InspectorPanel.__proto__ || Object.getPrototypeOf(InspectorPanel)).apply(this, arguments));
    }

    babelHelpers.createClass(InspectorPanel, [{
      key: "renderWaiting",
      value: function renderWaiting() {
        if (this.props.inspecting) {
          return React.createElement(
            Text,
            {
              style: styles.waitingText,
              __source: {
                fileName: _jsxFileName,
                lineNumber: 27
              }
            },
            "Tap something to inspect it"
          );
        }

        return React.createElement(
          Text,
          {
            style: styles.waitingText,
            __source: {
              fileName: _jsxFileName,
              lineNumber: 32
            }
          },
          "Nothing is inspected"
        );
      }
    }, {
      key: "render",
      value: function render() {
        var contents = void 0;

        if (this.props.inspected) {
          contents = React.createElement(
            ScrollView,
            {
              style: styles.properties,
              __source: {
                fileName: _jsxFileName,
                lineNumber: 39
              }
            },
            React.createElement(ElementProperties, {
              style: this.props.inspected.style,
              frame: this.props.inspected.frame,
              source: this.props.inspected.source,
              hierarchy: this.props.hierarchy,
              selection: this.props.selection,
              setSelection: this.props.setSelection,
              __source: {
                fileName: _jsxFileName,
                lineNumber: 40
              }
            })
          );
        } else if (this.props.perfing) {
          contents = React.createElement(PerformanceOverlay, {
            __source: {
              fileName: _jsxFileName,
              lineNumber: 52
            }
          });
        } else if (this.props.networking) {
          contents = React.createElement(NetworkOverlay, {
            __source: {
              fileName: _jsxFileName,
              lineNumber: 56
            }
          });
        } else {
          contents = React.createElement(
            View,
            {
              style: styles.waiting,
              __source: {
                fileName: _jsxFileName,
                lineNumber: 60
              }
            },
            this.renderWaiting()
          );
        }

        return React.createElement(
          View,
          {
            style: styles.container,
            __source: {
              fileName: _jsxFileName,
              lineNumber: 66
            }
          },
          !this.props.devtoolsIsOpen && contents,
          React.createElement(
            View,
            {
              style: styles.buttonRow,
              __source: {
                fileName: _jsxFileName,
                lineNumber: 68
              }
            },
            React.createElement(Button, {
              title: 'Inspect',
              pressed: this.props.inspecting,
              onClick: this.props.setInspecting,
              __source: {
                fileName: _jsxFileName,
                lineNumber: 69
              }
            }),
            React.createElement(Button, {
              title: 'Perf',
              pressed: this.props.perfing,
              onClick: this.props.setPerfing,
              __source: {
                fileName: _jsxFileName,
                lineNumber: 74
              }
            }),
            React.createElement(Button, {
              title: 'Network',
              pressed: this.props.networking,
              onClick: this.props.setNetworking,
              __source: {
                fileName: _jsxFileName,
                lineNumber: 78
              }
            }),
            React.createElement(Button, {
              title: 'Touchables',
              pressed: this.props.touchTargeting,
              onClick: this.props.setTouchTargeting,
              __source: {
                fileName: _jsxFileName,
                lineNumber: 82
              }
            })
          )
        );
      }
    }]);
    return InspectorPanel;
  }(React.Component);

  InspectorPanel.propTypes = {
    devtoolsIsOpen: PropTypes.bool,
    inspecting: PropTypes.bool,
    setInspecting: PropTypes.func,
    inspected: PropTypes.object,
    perfing: PropTypes.bool,
    setPerfing: PropTypes.func,
    touchTargeting: PropTypes.bool,
    setTouchTargeting: PropTypes.func,
    networking: PropTypes.bool,
    setNetworking: PropTypes.func
  };

  var Button = function (_React$Component2) {
    babelHelpers.inherits(Button, _React$Component2);

    function Button() {
      babelHelpers.classCallCheck(this, Button);
      return babelHelpers.possibleConstructorReturn(this, (Button.__proto__ || Object.getPrototypeOf(Button)).apply(this, arguments));
    }

    babelHelpers.createClass(Button, [{
      key: "render",
      value: function render() {
        var _this3 = this;

        return React.createElement(
          TouchableHighlight,
          {
            onPress: function onPress() {
              return _this3.props.onClick(!_this3.props.pressed);
            },
            style: [styles.button, this.props.pressed && styles.buttonPressed],
            __source: {
              fileName: _jsxFileName,
              lineNumber: 108
            }
          },
          React.createElement(
            Text,
            {
              style: styles.buttonText,
              __source: {
                fileName: _jsxFileName,
                lineNumber: 112
              }
            },
            this.props.title
          )
        );
      }
    }]);
    return Button;
  }(React.Component);

  var styles = StyleSheet.create({
    buttonRow: {
      flexDirection: 'row'
    },
    button: {
      backgroundColor: 'rgba(0, 0, 0, 0.3)',
      margin: 2,
      height: 30,
      justifyContent: 'center',
      alignItems: 'center'
    },
    buttonPressed: {
      backgroundColor: 'rgba(255, 255, 255, 0.3)'
    },
    buttonText: {
      textAlign: 'center',
      color: 'white',
      margin: 5
    },
    container: {
      backgroundColor: 'rgba(0, 0, 0, 0.7)'
    },
    properties: {
      height: 200
    },
    waiting: {
      height: 100
    },
    waitingText: {
      fontSize: 20,
      textAlign: 'center',
      marginVertical: 20,
      color: 'white'
    }
  });
  module.exports = InspectorPanel;
},"1f3dc5e64b748b6c689aa1472d5e6963",["c67cbc1f8e28d7d9d5520a26e742e9b6","d22216a4351a4224118d463bcb0726f1","0480fd806aa0c21fcbc44eeb3e4455fe","e6db4f0efed6b72f641ef0ffed29569f","18eeaf4e01377a466daaccc6ba8ce6f5","aa8514022050149acc8c46c0b18dc75a","d31e8c1a3f9844becc88973ecddac872","c03ca8878a60b3cdaf32e10931ff258d","0d3968e3413084f66ce651afdba962c2","30a3b04291b6e1f01b778ff31271ccc5"],"InspectorPanel");