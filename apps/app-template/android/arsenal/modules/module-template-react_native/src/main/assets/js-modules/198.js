__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-native/Libraries/Components/Touchable/TouchableOpacity.js";

  var Animated = _require(_dependencyMap[0], 'Animated');

  var Easing = _require(_dependencyMap[1], 'Easing');

  var NativeMethodsMixin = _require(_dependencyMap[2], 'NativeMethodsMixin');

  var React = _require(_dependencyMap[3], 'React');

  var PropTypes = _require(_dependencyMap[4], 'prop-types');

  var TimerMixin = _require(_dependencyMap[5], 'react-timer-mixin');

  var Touchable = _require(_dependencyMap[6], 'Touchable');

  var TouchableWithoutFeedback = _require(_dependencyMap[7], 'TouchableWithoutFeedback');

  var createReactClass = _require(_dependencyMap[8], 'create-react-class');

  var ensurePositiveDelayProps = _require(_dependencyMap[9], 'ensurePositiveDelayProps');

  var flattenStyle = _require(_dependencyMap[10], 'flattenStyle');

  var PRESS_RETENTION_OFFSET = {
    top: 20,
    left: 20,
    right: 20,
    bottom: 30
  };
  var TouchableOpacity = createReactClass({
    displayName: 'TouchableOpacity',
    mixins: [TimerMixin, Touchable.Mixin, NativeMethodsMixin],
    propTypes: babelHelpers.extends({}, TouchableWithoutFeedback.propTypes, {
      activeOpacity: PropTypes.number,
      hasTVPreferredFocus: PropTypes.bool,
      tvParallaxProperties: PropTypes.object
    }),
    getDefaultProps: function getDefaultProps() {
      return {
        activeOpacity: 0.2
      };
    },
    getInitialState: function getInitialState() {
      return babelHelpers.extends({}, this.touchableGetInitialState(), {
        anim: new Animated.Value(this._getChildStyleOpacityWithDefault())
      });
    },
    componentDidMount: function componentDidMount() {
      ensurePositiveDelayProps(this.props);
    },
    UNSAFE_componentWillReceiveProps: function UNSAFE_componentWillReceiveProps(nextProps) {
      ensurePositiveDelayProps(nextProps);
    },
    componentDidUpdate: function componentDidUpdate(prevProps, prevState) {
      if (this.props.disabled !== prevProps.disabled) {
        this._opacityInactive(250);
      }
    },
    setOpacityTo: function setOpacityTo(value, duration) {
      Animated.timing(this.state.anim, {
        toValue: value,
        duration: duration,
        easing: Easing.inOut(Easing.quad),
        useNativeDriver: true
      }).start();
    },
    touchableHandleActivePressIn: function touchableHandleActivePressIn(e) {
      if (e.dispatchConfig.registrationName === 'onResponderGrant') {
        this._opacityActive(0);
      } else {
        this._opacityActive(150);
      }

      this.props.onPressIn && this.props.onPressIn(e);
    },
    touchableHandleActivePressOut: function touchableHandleActivePressOut(e) {
      this._opacityInactive(250);

      this.props.onPressOut && this.props.onPressOut(e);
    },
    touchableHandlePress: function touchableHandlePress(e) {
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
      return this.props.delayPressIn || 0;
    },
    touchableGetLongPressDelayMS: function touchableGetLongPressDelayMS() {
      return this.props.delayLongPress === 0 ? 0 : this.props.delayLongPress || 500;
    },
    touchableGetPressOutDelayMS: function touchableGetPressOutDelayMS() {
      return this.props.delayPressOut;
    },
    _opacityActive: function _opacityActive(duration) {
      this.setOpacityTo(this.props.activeOpacity, duration);
    },
    _opacityInactive: function _opacityInactive(duration) {
      this.setOpacityTo(this._getChildStyleOpacityWithDefault(), duration);
    },
    _getChildStyleOpacityWithDefault: function _getChildStyleOpacityWithDefault() {
      var childStyle = flattenStyle(this.props.style) || {};
      return childStyle.opacity == undefined ? 1 : childStyle.opacity;
    },
    render: function render() {
      return React.createElement(
        Animated.View,
        {
          accessible: this.props.accessible !== false,
          accessibilityLabel: this.props.accessibilityLabel,
          accessibilityComponentType: this.props.accessibilityComponentType,
          accessibilityTraits: this.props.accessibilityTraits,
          style: [this.props.style, {
            opacity: this.state.anim
          }],
          nativeID: this.props.nativeID,
          testID: this.props.testID,
          onLayout: this.props.onLayout,
          isTVSelectable: true,
          hasTVPreferredFocus: this.props.hasTVPreferredFocus,
          tvParallaxProperties: this.props.tvParallaxProperties,
          hitSlop: this.props.hitSlop,
          onStartShouldSetResponder: this.touchableHandleStartShouldSetResponder,
          onResponderTerminationRequest: this.touchableHandleResponderTerminationRequest,
          onResponderGrant: this.touchableHandleResponderGrant,
          onResponderMove: this.touchableHandleResponderMove,
          onResponderRelease: this.touchableHandleResponderRelease,
          onResponderTerminate: this.touchableHandleResponderTerminate,
          __source: {
            fileName: _jsxFileName,
            lineNumber: 247
          }
        },
        this.props.children,
        Touchable.renderDebugView({
          color: 'cyan',
          hitSlop: this.props.hitSlop
        })
      );
    }
  });
  module.exports = TouchableOpacity;
},198,[199,224,48,132,129,196,187,195,176,197,116],"TouchableOpacity");