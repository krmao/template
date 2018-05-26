__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-native/Libraries/Components/Keyboard/KeyboardAvoidingView.js";

  var createReactClass = _require(_dependencyMap[0], 'create-react-class');

  var Keyboard = _require(_dependencyMap[1], 'Keyboard');

  var LayoutAnimation = _require(_dependencyMap[2], 'LayoutAnimation');

  var Platform = _require(_dependencyMap[3], 'Platform');

  var PropTypes = _require(_dependencyMap[4], 'prop-types');

  var React = _require(_dependencyMap[5], 'React');

  var TimerMixin = _require(_dependencyMap[6], 'react-timer-mixin');

  var View = _require(_dependencyMap[7], 'View');

  var ViewPropTypes = _require(_dependencyMap[8], 'ViewPropTypes');

  var viewRef = 'VIEW';
  var KeyboardAvoidingView = createReactClass({
    displayName: 'KeyboardAvoidingView',
    mixins: [TimerMixin],
    propTypes: babelHelpers.extends({}, ViewPropTypes, {
      behavior: PropTypes.oneOf(['height', 'position', 'padding']),
      contentContainerStyle: ViewPropTypes.style,
      keyboardVerticalOffset: PropTypes.number.isRequired,
      enabled: PropTypes.bool.isRequired
    }),
    getDefaultProps: function getDefaultProps() {
      return {
        enabled: true,
        keyboardVerticalOffset: 0
      };
    },
    getInitialState: function getInitialState() {
      return {
        bottom: 0
      };
    },
    subscriptions: [],
    frame: null,
    _relativeKeyboardHeight: function _relativeKeyboardHeight(keyboardFrame) {
      var frame = this.frame;

      if (!frame || !keyboardFrame) {
        return 0;
      }

      var keyboardY = keyboardFrame.screenY - this.props.keyboardVerticalOffset;
      return Math.max(frame.y + frame.height - keyboardY, 0);
    },
    _onKeyboardChange: function _onKeyboardChange(event) {
      if (!event) {
        this.setState({
          bottom: 0
        });
        return;
      }

      var duration = event.duration,
          easing = event.easing,
          endCoordinates = event.endCoordinates;

      var height = this._relativeKeyboardHeight(endCoordinates);

      if (this.state.bottom === height) {
        return;
      }

      if (duration && easing) {
        LayoutAnimation.configureNext({
          duration: duration,
          update: {
            duration: duration,
            type: LayoutAnimation.Types[easing] || 'keyboard'
          }
        });
      }

      this.setState({
        bottom: height
      });
    },
    _onLayout: function _onLayout(event) {
      this.frame = event.nativeEvent.layout;
    },
    UNSAFE_componentWillUpdate: function UNSAFE_componentWillUpdate(nextProps, nextState, nextContext) {
      if (nextState.bottom === this.state.bottom && this.props.behavior === 'height' && nextProps.behavior === 'height') {
        nextState.bottom = 0;
      }
    },
    UNSAFE_componentWillMount: function UNSAFE_componentWillMount() {
      if (Platform.OS === 'ios') {
        this.subscriptions = [Keyboard.addListener('keyboardWillChangeFrame', this._onKeyboardChange)];
      } else {
        this.subscriptions = [Keyboard.addListener('keyboardDidHide', this._onKeyboardChange), Keyboard.addListener('keyboardDidShow', this._onKeyboardChange)];
      }
    },
    componentWillUnmount: function componentWillUnmount() {
      this.subscriptions.forEach(function (sub) {
        return sub.remove();
      });
    },
    render: function render() {
      var _props = this.props,
          behavior = _props.behavior,
          children = _props.children,
          style = _props.style,
          props = babelHelpers.objectWithoutProperties(_props, ["behavior", "children", "style"]);
      var bottomHeight = this.props.enabled ? this.state.bottom : 0;

      switch (behavior) {
        case 'height':
          var heightStyle = void 0;

          if (this.frame) {
            heightStyle = {
              height: this.frame.height - bottomHeight,
              flex: 0
            };
          }

          return React.createElement(
            View,
            babelHelpers.extends({
              ref: viewRef,
              style: [style, heightStyle],
              onLayout: this._onLayout
            }, props, {
              __source: {
                fileName: _jsxFileName,
                lineNumber: 176
              }
            }),
            children
          );

        case 'position':
          var positionStyle = {
            bottom: bottomHeight
          };
          var contentContainerStyle = this.props.contentContainerStyle;
          return React.createElement(
            View,
            babelHelpers.extends({
              ref: viewRef,
              style: style,
              onLayout: this._onLayout
            }, props, {
              __source: {
                fileName: _jsxFileName,
                lineNumber: 186
              }
            }),
            React.createElement(
              View,
              {
                style: [contentContainerStyle, positionStyle],
                __source: {
                  fileName: _jsxFileName,
                  lineNumber: 187
                }
              },
              children
            )
          );

        case 'padding':
          var paddingStyle = {
            paddingBottom: bottomHeight
          };
          return React.createElement(
            View,
            babelHelpers.extends({
              ref: viewRef,
              style: [style, paddingStyle],
              onLayout: this._onLayout
            }, props, {
              __source: {
                fileName: _jsxFileName,
                lineNumber: 196
              }
            }),
            children
          );

        default:
          return React.createElement(
            View,
            babelHelpers.extends({
              ref: viewRef,
              onLayout: this._onLayout,
              style: style
            }, props, {
              __source: {
                fileName: _jsxFileName,
                lineNumber: 203
              }
            }),
            children
          );
      }
    }
  });
  module.exports = KeyboardAvoidingView;
},261,[176,232,233,32,129,132,196,173,133],"KeyboardAvoidingView");