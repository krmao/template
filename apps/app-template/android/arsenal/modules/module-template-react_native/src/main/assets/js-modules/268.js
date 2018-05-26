__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-native/Libraries/Inspector/ElementBox.js";

  var React = _require(_dependencyMap[0], 'React');

  var View = _require(_dependencyMap[1], 'View');

  var StyleSheet = _require(_dependencyMap[2], 'StyleSheet');

  var BorderBox = _require(_dependencyMap[3], 'BorderBox');

  var resolveBoxStyle = _require(_dependencyMap[4], 'resolveBoxStyle');

  var flattenStyle = _require(_dependencyMap[5], 'flattenStyle');

  var ElementBox = function (_React$Component) {
    babelHelpers.inherits(ElementBox, _React$Component);

    function ElementBox() {
      babelHelpers.classCallCheck(this, ElementBox);
      return babelHelpers.possibleConstructorReturn(this, (ElementBox.__proto__ || Object.getPrototypeOf(ElementBox)).apply(this, arguments));
    }

    babelHelpers.createClass(ElementBox, [{
      key: "render",
      value: function render() {
        var style = flattenStyle(this.props.style) || {};
        var margin = resolveBoxStyle('margin', style);
        var padding = resolveBoxStyle('padding', style);
        var frameStyle = this.props.frame;

        if (margin) {
          frameStyle = {
            top: frameStyle.top - margin.top,
            left: frameStyle.left - margin.left,
            height: frameStyle.height + margin.top + margin.bottom,
            width: frameStyle.width + margin.left + margin.right
          };
        }

        var contentStyle = {
          width: this.props.frame.width,
          height: this.props.frame.height
        };

        if (padding) {
          contentStyle = {
            width: contentStyle.width - padding.left - padding.right,
            height: contentStyle.height - padding.top - padding.bottom
          };
        }

        return React.createElement(
          View,
          {
            style: [styles.frame, frameStyle],
            pointerEvents: "none",
            __source: {
              fileName: _jsxFileName,
              lineNumber: 45
            }
          },
          React.createElement(
            BorderBox,
            {
              box: margin,
              style: styles.margin,
              __source: {
                fileName: _jsxFileName,
                lineNumber: 46
              }
            },
            React.createElement(
              BorderBox,
              {
                box: padding,
                style: styles.padding,
                __source: {
                  fileName: _jsxFileName,
                  lineNumber: 47
                }
              },
              React.createElement(View, {
                style: [styles.content, contentStyle],
                __source: {
                  fileName: _jsxFileName,
                  lineNumber: 48
                }
              })
            )
          )
        );
      }
    }]);
    return ElementBox;
  }(React.Component);

  var styles = StyleSheet.create({
    frame: {
      position: 'absolute'
    },
    content: {
      backgroundColor: 'rgba(200, 230, 255, 0.8)'
    },
    padding: {
      borderColor: 'rgba(77, 255, 0, 0.3)'
    },
    margin: {
      borderColor: 'rgba(255, 132, 0, 0.3)'
    }
  });
  module.exports = ElementBox;
},268,[132,173,171,269,270,116],"ElementBox");