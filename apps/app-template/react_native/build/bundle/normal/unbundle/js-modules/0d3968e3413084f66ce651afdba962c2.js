__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-native/Libraries/Components/Touchable/TouchableHighlight.js";

  var ColorPropType = _require(_dependencyMap[0], 'ColorPropType');

  var NativeMethodsMixin = _require(_dependencyMap[1], 'NativeMethodsMixin');

  var PropTypes = _require(_dependencyMap[2], 'prop-types');

  var Platform = _require(_dependencyMap[3], 'Platform');

  var React = _require(_dependencyMap[4], 'React');

  var ReactNativeViewAttributes = _require(_dependencyMap[5], 'ReactNativeViewAttributes');

  var StyleSheet = _require(_dependencyMap[6], 'StyleSheet');

  var Touchable = _require(_dependencyMap[7], 'Touchable');

  var TouchableWithoutFeedback = _require(_dependencyMap[8], 'TouchableWithoutFeedback');

  var View = _require(_dependencyMap[9], 'View');

  var ViewPropTypes = _require(_dependencyMap[10], 'ViewPropTypes');

  var createReactClass = _require(_dependencyMap[11], 'create-react-class');

  var ensurePositiveDelayProps = _require(_dependencyMap[12], 'ensurePositiveDelayProps');

  var DEFAULT_PROPS = {
    activeOpacity: 0.85,
    delayPressOut: 100,
    underlayColor: 'black'
  };
  var PRESS_RETENTION_OFFSET = {
    top: 20,
    left: 20,
    right: 20,
    bottom: 30
  };
  var TouchableHighlight = createReactClass({
    displayName: 'TouchableHighlight',
    propTypes: babelHelpers.extends({}, TouchableWithoutFeedback.propTypes, {
      activeOpacity: PropTypes.number,
      underlayColor: ColorPropType,
      style: ViewPropTypes.style,
      onShowUnderlay: PropTypes.func,
      onHideUnderlay: PropTypes.func,
      hasTVPreferredFocus: PropTypes.bool,
      tvParallaxProperties: PropTypes.object,
      testOnly_pressed: PropTypes.bool
    }),
    mixins: [NativeMethodsMixin, Touchable.Mixin],
    getDefaultProps: function getDefaultProps() {
      return DEFAULT_PROPS;
    },
    getInitialState: function getInitialState() {
      this._isMounted = false;

      if (this.props.testOnly_pressed) {
        return babelHelpers.extends({}, this.touchableGetInitialState(), {
          extraChildStyle: {
            opacity: this.props.activeOpacity
          },
          extraUnderlayStyle: {
            backgroundColor: this.props.underlayColor
          }
        });
      } else {
        return babelHelpers.extends({}, this.touchableGetInitialState(), {
          extraChildStyle: null,
          extraUnderlayStyle: null
        });
      }
    },
    componentDidMount: function componentDidMount() {
      this._isMounted = true;
      ensurePositiveDelayProps(this.props);
    },
    componentWillUnmount: function componentWillUnmount() {
      this._isMounted = false;
      clearTimeout(this._hideTimeout);
    },
    UNSAFE_componentWillReceiveProps: function UNSAFE_componentWillReceiveProps(nextProps) {
      ensurePositiveDelayProps(nextProps);
    },
    viewConfig: {
      uiViewClassName: 'RCTView',
      validAttributes: ReactNativeViewAttributes.RCTView
    },
    touchableHandleActivePressIn: function touchableHandleActivePressIn(e) {
      clearTimeout(this._hideTimeout);
      this._hideTimeout = null;

      this._showUnderlay();

      this.props.onPressIn && this.props.onPressIn(e);
    },
    touchableHandleActivePressOut: function touchableHandleActivePressOut(e) {
      if (!this._hideTimeout) {
        this._hideUnderlay();
      }

      this.props.onPressOut && this.props.onPressOut(e);
    },
    touchableHandlePress: function touchableHandlePress(e) {
      clearTimeout(this._hideTimeout);

      if (!Platform.isTVOS) {
        this._showUnderlay();

        this._hideTimeout = setTimeout(this._hideUnderlay, this.props.delayPressOut);
      }

      this.props.onPress && this.props.onPress(e);
    },
    touchableHandleLongPress: function touchableHandleLongPress(e) {
      this.props.onLongPress && this.props.onLongPress(e);
    },
    touchableGetPressRectOffset: function touchableGetPressRectOffset() {
      return this.props.pressRetentionOffset || PRESS_RETENTION_OFFSET;
    },
    touchableGetHitSlop: function touchableGetHitSlop() {
      return this.props.hitSlop;
    },
    touchableGetHighlightDelayMS: function touchableGetHighlightDelayMS() {
      return this.props.delayPressIn;
    },
    touchableGetLongPressDelayMS: function touchableGetLongPressDelayMS() {
      return this.props.delayLongPress;
    },
    touchableGetPressOutDelayMS: function touchableGetPressOutDelayMS() {
      return this.props.delayPressOut;
    },
    _showUnderlay: function _showUnderlay() {
      if (!this._isMounted || !this._hasPressHandler()) {
        return;
      }

      this.setState({
        extraChildStyle: {
          opacity: this.props.activeOpacity
        },
        extraUnderlayStyle: {
          backgroundColor: this.props.underlayColor
        }
      });
      this.props.onShowUnderlay && this.props.onShowUnderlay();
    },
    _hideUnderlay: function _hideUnderlay() {
      clearTimeout(this._hideTimeout);
      this._hideTimeout = null;

      if (this.props.testOnly_pressed) {
        return;
      }

      if (this._hasPressHandler()) {
        this.setState({
          extraChildStyle: null,
          extraUnderlayStyle: null
        });
        this.props.onHideUnderlay && this.props.onHideUnderlay();
      }
    },
    _hasPressHandler: function _hasPressHandler() {
      return !!(this.props.onPress || this.props.onPressIn || this.props.onPressOut || this.props.onLongPress);
    },
    render: function render() {
      var child = React.Children.only(this.props.children);
      return React.createElement(
        View,
        {
          accessible: this.props.accessible !== false,
          accessibilityLabel: this.props.accessibilityLabel,
          accessibilityComponentType: this.props.accessibilityComponentType,
          accessibilityTraits: this.props.accessibilityTraits,
          style: StyleSheet.compose(this.props.style, this.state.extraUnderlayStyle),
          onLayout: this.props.onLayout,
          hitSlop: this.props.hitSlop,
          isTVSelectable: true,
          tvParallaxProperties: this.props.tvParallaxProperties,
          hasTVPreferredFocus: this.props.hasTVPreferredFocus,
          onStartShouldSetResponder: this.touchableHandleStartShouldSetResponder,
          onResponderTerminationRequest: this.touchableHandleResponderTerminationRequest,
          onResponderGrant: this.touchableHandleResponderGrant,
          onResponderMove: this.touchableHandleResponderMove,
          onResponderRelease: this.touchableHandleResponderRelease,
          onResponderTerminate: this.touchableHandleResponderTerminate,
          nativeID: this.props.nativeID,
          testID: this.props.testID,
          __source: {
            fileName: _jsxFileName,
            lineNumber: 329
          }
        },
        React.cloneElement(child, {
          style: StyleSheet.compose(child.props.style, this.state.extraChildStyle)
        }),
        Touchable.renderDebugView({
          color: 'green',
          hitSlop: this.props.hitSlop
        })
      );
    }
  });
  module.exports = TouchableHighlight;
},"0d3968e3413084f66ce651afdba962c2",["63c61c7eda525c10d0670d2ef8475012","e2817b4a53aaef19afef34f031e1b9c9","18eeaf4e01377a466daaccc6ba8ce6f5","9493a89f5d95c3a8a47c65cfed9b5542","e6db4f0efed6b72f641ef0ffed29569f","6477887be0d285a967d42967386335cd","d31e8c1a3f9844becc88973ecddac872","9be7bff2ec732c7f9f96a83cea3bc22f","9e4c8667cb3e1e5fa7d33d9679f26159","30a3b04291b6e1f01b778ff31271ccc5","9ff7e107ed674a99182e71b796d889aa","29cb0e104e5fce198008f3e789631772","6ba49cd3c30b475f38fef604b303c1ac"],"TouchableHighlight");