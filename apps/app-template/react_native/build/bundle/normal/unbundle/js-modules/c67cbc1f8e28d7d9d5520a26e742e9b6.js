__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-native/Libraries/Inspector/ElementProperties.js";

  var BoxInspector = _require(_dependencyMap[0], 'BoxInspector');

  var PropTypes = _require(_dependencyMap[1], 'prop-types');

  var React = _require(_dependencyMap[2], 'React');

  var StyleInspector = _require(_dependencyMap[3], 'StyleInspector');

  var StyleSheet = _require(_dependencyMap[4], 'StyleSheet');

  var Text = _require(_dependencyMap[5], 'Text');

  var TouchableHighlight = _require(_dependencyMap[6], 'TouchableHighlight');

  var TouchableWithoutFeedback = _require(_dependencyMap[7], 'TouchableWithoutFeedback');

  var View = _require(_dependencyMap[8], 'View');

  var flattenStyle = _require(_dependencyMap[9], 'flattenStyle');

  var mapWithSeparator = _require(_dependencyMap[10], 'mapWithSeparator');

  var openFileInEditor = _require(_dependencyMap[11], 'openFileInEditor');

  var ElementProperties = function (_React$Component) {
    babelHelpers.inherits(ElementProperties, _React$Component);

    function ElementProperties() {
      babelHelpers.classCallCheck(this, ElementProperties);
      return babelHelpers.possibleConstructorReturn(this, (ElementProperties.__proto__ || Object.getPrototypeOf(ElementProperties)).apply(this, arguments));
    }

    babelHelpers.createClass(ElementProperties, [{
      key: "render",
      value: function render() {
        var _this2 = this;

        var style = flattenStyle(this.props.style);
        var selection = this.props.selection;
        var openFileButton = void 0;
        var source = this.props.source;

        var _ref = source || {},
            fileName = _ref.fileName,
            lineNumber = _ref.lineNumber;

        if (fileName && lineNumber) {
          var parts = fileName.split('/');
          var fileNameShort = parts[parts.length - 1];
          openFileButton = React.createElement(
            TouchableHighlight,
            {
              style: styles.openButton,
              onPress: openFileInEditor.bind(null, fileName, lineNumber),
              __source: {
                fileName: _jsxFileName,
                lineNumber: 60
              }
            },
            React.createElement(
              Text,
              {
                style: styles.openButtonTitle,
                numberOfLines: 1,
                __source: {
                  fileName: _jsxFileName,
                  lineNumber: 63
                }
              },
              fileNameShort,
              ":",
              lineNumber
            )
          );
        }

        return React.createElement(
          TouchableWithoutFeedback,
          {
            __source: {
              fileName: _jsxFileName,
              lineNumber: 72
            }
          },
          React.createElement(
            View,
            {
              style: styles.info,
              __source: {
                fileName: _jsxFileName,
                lineNumber: 73
              }
            },
            React.createElement(
              View,
              {
                style: styles.breadcrumb,
                __source: {
                  fileName: _jsxFileName,
                  lineNumber: 74
                }
              },
              mapWithSeparator(this.props.hierarchy, function (hierarchyItem, i) {
                return React.createElement(
                  TouchableHighlight,
                  {
                    key: 'item-' + i,
                    style: [styles.breadItem, i === selection && styles.selected],
                    onPress: function onPress() {
                      return _this2.props.setSelection(i);
                    },
                    __source: {
                      fileName: _jsxFileName,
                      lineNumber: 78
                    }
                  },
                  React.createElement(
                    Text,
                    {
                      style: styles.breadItemText,
                      __source: {
                        fileName: _jsxFileName,
                        lineNumber: 83
                      }
                    },
                    hierarchyItem.name
                  )
                );
              }, function (i) {
                return React.createElement(
                  Text,
                  {
                    key: 'sep-' + i,
                    style: styles.breadSep,
                    __source: {
                      fileName: _jsxFileName,
                      lineNumber: 89
                    }
                  },
                  "\u25B8"
                );
              })
            ),
            React.createElement(
              View,
              {
                style: styles.row,
                __source: {
                  fileName: _jsxFileName,
                  lineNumber: 95
                }
              },
              React.createElement(
                View,
                {
                  style: styles.col,
                  __source: {
                    fileName: _jsxFileName,
                    lineNumber: 96
                  }
                },
                React.createElement(StyleInspector, {
                  style: style,
                  __source: {
                    fileName: _jsxFileName,
                    lineNumber: 97
                  }
                }),
                openFileButton
              ),
              React.createElement(BoxInspector, {
                style: style,
                frame: this.props.frame,
                __source: {
                  fileName: _jsxFileName,
                  lineNumber: 102
                }
              })
            )
          )
        );
      }
    }]);
    return ElementProperties;
  }(React.Component);

  ElementProperties.propTypes = {
    hierarchy: PropTypes.array.isRequired,
    style: PropTypes.oneOfType([PropTypes.object, PropTypes.array, PropTypes.number]),
    source: PropTypes.shape({
      fileName: PropTypes.string,
      lineNumber: PropTypes.number
    })
  };
  var styles = StyleSheet.create({
    breadSep: {
      fontSize: 8,
      color: 'white'
    },
    breadcrumb: {
      flexDirection: 'row',
      flexWrap: 'wrap',
      alignItems: 'flex-start',
      marginBottom: 5
    },
    selected: {
      borderColor: 'white',
      borderRadius: 5
    },
    breadItem: {
      borderWidth: 1,
      borderColor: 'transparent',
      marginHorizontal: 2
    },
    breadItemText: {
      fontSize: 10,
      color: 'white',
      marginHorizontal: 5
    },
    row: {
      flexDirection: 'row',
      alignItems: 'center',
      justifyContent: 'space-between'
    },
    col: {
      flex: 1
    },
    info: {
      padding: 10
    },
    openButton: {
      padding: 10,
      backgroundColor: '#000',
      marginVertical: 5,
      marginRight: 5,
      borderRadius: 2
    },
    openButtonTitle: {
      color: 'white',
      fontSize: 8
    }
  });
  module.exports = ElementProperties;
},"c67cbc1f8e28d7d9d5520a26e742e9b6",["145eafc81690b097a402bd375a5a9358","18eeaf4e01377a466daaccc6ba8ce6f5","e6db4f0efed6b72f641ef0ffed29569f","5c149b58cbcea317762e960d5dd30aba","d31e8c1a3f9844becc88973ecddac872","c03ca8878a60b3cdaf32e10931ff258d","0d3968e3413084f66ce651afdba962c2","9e4c8667cb3e1e5fa7d33d9679f26159","30a3b04291b6e1f01b778ff31271ccc5","869f0bd4eed428d95df80a8c03d71093","f583e01cf65abd4f4d1c5e0871a76e9c","74b32f55a0bb628f9e8dc4e21b02d7a1"],"ElementProperties");