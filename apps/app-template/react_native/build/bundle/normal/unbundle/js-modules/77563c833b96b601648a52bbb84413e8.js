__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-navigation/src/views/StackView/StackViewCard.js";

  var _react = _require(_dependencyMap[0], "react");

  var _react2 = babelHelpers.interopRequireDefault(_react);

  var _reactNative = _require(_dependencyMap[1], "react-native");

  var _createPointerEventsContainer = _require(_dependencyMap[2], "./createPointerEventsContainer");

  var _createPointerEventsContainer2 = babelHelpers.interopRequireDefault(_createPointerEventsContainer);

  var Card = function (_React$Component) {
    babelHelpers.inherits(Card, _React$Component);

    function Card() {
      babelHelpers.classCallCheck(this, Card);
      return babelHelpers.possibleConstructorReturn(this, (Card.__proto__ || Object.getPrototypeOf(Card)).apply(this, arguments));
    }

    babelHelpers.createClass(Card, [{
      key: "render",
      value: function render() {
        var _props = this.props,
            children = _props.children,
            pointerEvents = _props.pointerEvents,
            style = _props.style;
        return _react2.default.createElement(
          _reactNative.Animated.View,
          {
            pointerEvents: pointerEvents,
            ref: this.props.onComponentRef,
            style: [styles.main, style],
            __source: {
              fileName: _jsxFileName,
              lineNumber: 12
            }
          },
          children
        );
      }
    }]);
    return Card;
  }(_react2.default.Component);

  var styles = _reactNative.StyleSheet.create({
    main: babelHelpers.extends({}, _reactNative.StyleSheet.absoluteFillObject, {
      backgroundColor: '#E9E9EF',
      shadowColor: 'black',
      shadowOffset: {
        width: 0,
        height: 0
      },
      shadowOpacity: 0.2,
      shadowRadius: 5
    })
  });

  Card = (0, _createPointerEventsContainer2.default)(Card);
  exports.default = Card;
},"77563c833b96b601648a52bbb84413e8",["c42a5e17831e80ed1e1c8cf91f5ddb40","cc757a791ecb3cd320f65c256a791c04","dd583e8c465bf5c47bb614a6c4772b80"],"node_modules/react-navigation/src/views/StackView/StackViewCard.js");