__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-native/Libraries/Components/ScrollView/ScrollViewStickyHeader.js";

  var AnimatedImplementation = _require(_dependencyMap[0], 'AnimatedImplementation');

  var React = _require(_dependencyMap[1], 'React');

  var StyleSheet = _require(_dependencyMap[2], 'StyleSheet');

  var View = _require(_dependencyMap[3], 'View');

  var AnimatedView = AnimatedImplementation.createAnimatedComponent(View);

  var ScrollViewStickyHeader = function (_React$Component) {
    babelHelpers.inherits(ScrollViewStickyHeader, _React$Component);

    function ScrollViewStickyHeader() {
      var _ref;

      var _temp, _this, _ret;

      babelHelpers.classCallCheck(this, ScrollViewStickyHeader);

      for (var _len = arguments.length, args = Array(_len), _key = 0; _key < _len; _key++) {
        args[_key] = arguments[_key];
      }

      return _ret = (_temp = (_this = babelHelpers.possibleConstructorReturn(this, (_ref = ScrollViewStickyHeader.__proto__ || Object.getPrototypeOf(ScrollViewStickyHeader)).call.apply(_ref, [this].concat(args))), _this), _this.state = {
        measured: false,
        layoutY: 0,
        layoutHeight: 0,
        nextHeaderLayoutY: _this.props.nextHeaderLayoutY
      }, _this._onLayout = function (event) {
        _this.setState({
          measured: true,
          layoutY: event.nativeEvent.layout.y,
          layoutHeight: event.nativeEvent.layout.height
        });

        _this.props.onLayout(event);

        var child = React.Children.only(_this.props.children);

        if (child.props.onLayout) {
          child.props.onLayout(event);
        }
      }, _temp), babelHelpers.possibleConstructorReturn(_this, _ret);
    }

    babelHelpers.createClass(ScrollViewStickyHeader, [{
      key: "setNextHeaderY",
      value: function setNextHeaderY(y) {
        this.setState({
          nextHeaderLayoutY: y
        });
      }
    }, {
      key: "render",
      value: function render() {
        var _props = this.props,
            inverted = _props.inverted,
            scrollViewHeight = _props.scrollViewHeight;
        var _state = this.state,
            measured = _state.measured,
            layoutHeight = _state.layoutHeight,
            layoutY = _state.layoutY,
            nextHeaderLayoutY = _state.nextHeaderLayoutY;
        var inputRange = [-1, 0];
        var outputRange = [0, 0];

        if (measured) {
          if (inverted) {
            if (scrollViewHeight != null) {
              var stickStartPoint = layoutY + layoutHeight - scrollViewHeight;

              if (stickStartPoint > 0) {
                inputRange.push(stickStartPoint);
                outputRange.push(0);
                inputRange.push(stickStartPoint + 1);
                outputRange.push(1);
                var collisionPoint = (nextHeaderLayoutY || 0) - layoutHeight - scrollViewHeight;

                if (collisionPoint > stickStartPoint) {
                  inputRange.push(collisionPoint, collisionPoint + 1);
                  outputRange.push(collisionPoint - stickStartPoint, collisionPoint - stickStartPoint);
                }
              }
            }
          } else {
            inputRange.push(layoutY);
            outputRange.push(0);

            var _collisionPoint = (nextHeaderLayoutY || 0) - layoutHeight;

            if (_collisionPoint >= layoutY) {
              inputRange.push(_collisionPoint, _collisionPoint + 1);
              outputRange.push(_collisionPoint - layoutY, _collisionPoint - layoutY);
            } else {
              inputRange.push(layoutY + 1);
              outputRange.push(1);
            }
          }
        }

        var translateY = this.props.scrollAnimatedValue.interpolate({
          inputRange: inputRange,
          outputRange: outputRange
        });
        var child = React.Children.only(this.props.children);
        return React.createElement(
          AnimatedView,
          {
            collapsable: false,
            onLayout: this._onLayout,
            style: [child.props.style, styles.header, {
              transform: [{
                translateY: translateY
              }]
            }],
            __source: {
              fileName: _jsxFileName,
              lineNumber: 142
            }
          },
          React.cloneElement(child, {
            style: styles.fill,
            onLayout: undefined
          })
        );
      }
    }]);
    return ScrollViewStickyHeader;
  }(React.Component);

  var styles = StyleSheet.create({
    header: {
      zIndex: 10
    },
    fill: {
      flex: 1
    }
  });
  module.exports = ScrollViewStickyHeader;
},"cc7a48154b35440313a4df9da559dcf2",["a5a3455ec2263330f8d878126eaa4345","e6db4f0efed6b72f641ef0ffed29569f","d31e8c1a3f9844becc88973ecddac872","30a3b04291b6e1f01b778ff31271ccc5"],"ScrollViewStickyHeader");