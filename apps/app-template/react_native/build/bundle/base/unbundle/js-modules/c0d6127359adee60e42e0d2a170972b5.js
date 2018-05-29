__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var Platform = _require(_dependencyMap[0], 'Platform');

  var React = _require(_dependencyMap[1], 'React');

  var PropTypes = _require(_dependencyMap[2], 'prop-types');

  var ReactNative = _require(_dependencyMap[3], 'ReactNative');

  var Touchable = _require(_dependencyMap[4], 'Touchable');

  var TouchableWithoutFeedback = _require(_dependencyMap[5], 'TouchableWithoutFeedback');

  var UIManager = _require(_dependencyMap[6], 'UIManager');

  var createReactClass = _require(_dependencyMap[7], 'create-react-class');

  var ensurePositiveDelayProps = _require(_dependencyMap[8], 'ensurePositiveDelayProps');

  var processColor = _require(_dependencyMap[9], 'processColor');

  var rippleBackgroundPropType = PropTypes.shape({
    type: PropTypes.oneOf(['RippleAndroid']),
    color: PropTypes.number,
    borderless: PropTypes.bool
  });
  var themeAttributeBackgroundPropType = PropTypes.shape({
    type: PropTypes.oneOf(['ThemeAttrAndroid']),
    attribute: PropTypes.string.isRequired
  });
  var backgroundPropType = PropTypes.oneOfType([rippleBackgroundPropType, themeAttributeBackgroundPropType]);
  var PRESS_RETENTION_OFFSET = {
    top: 20,
    left: 20,
    right: 20,
    bottom: 30
  };
  var TouchableNativeFeedback = createReactClass({
    displayName: 'TouchableNativeFeedback',
    propTypes: babelHelpers.extends({}, TouchableWithoutFeedback.propTypes, {
      background: backgroundPropType,
      hasTVPreferredFocus: PropTypes.bool,
      useForeground: PropTypes.bool
    }),
    statics: {
      SelectableBackground: function SelectableBackground() {
        return {
          type: 'ThemeAttrAndroid',
          attribute: 'selectableItemBackground'
        };
      },
      SelectableBackgroundBorderless: function SelectableBackgroundBorderless() {
        return {
          type: 'ThemeAttrAndroid',
          attribute: 'selectableItemBackgroundBorderless'
        };
      },
      Ripple: function Ripple(color, borderless) {
        return {
          type: 'RippleAndroid',
          color: processColor(color),
          borderless: borderless
        };
      },
      canUseNativeForeground: function canUseNativeForeground() {
        return Platform.OS === 'android' && Platform.Version >= 23;
      }
    },
    mixins: [Touchable.Mixin],
    getDefaultProps: function getDefaultProps() {
      return {
        background: this.SelectableBackground()
      };
    },
    getInitialState: function getInitialState() {
      return this.touchableGetInitialState();
    },
    componentDidMount: function componentDidMount() {
      ensurePositiveDelayProps(this.props);
    },
    UNSAFE_componentWillReceiveProps: function UNSAFE_componentWillReceiveProps(nextProps) {
      ensurePositiveDelayProps(nextProps);
    },
    touchableHandleActivePressIn: function touchableHandleActivePressIn(e) {
      this.props.onPressIn && this.props.onPressIn(e);

      this._dispatchPressedStateChange(true);

      if (this.pressInLocation) {
        this._dispatchHotspotUpdate(this.pressInLocation.locationX, this.pressInLocation.locationY);
      }
    },
    touchableHandleActivePressOut: function touchableHandleActivePressOut(e) {
      this.props.onPressOut && this.props.onPressOut(e);

      this._dispatchPressedStateChange(false);
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
      return this.props.delayPressIn;
    },
    touchableGetLongPressDelayMS: function touchableGetLongPressDelayMS() {
      return this.props.delayLongPress;
    },
    touchableGetPressOutDelayMS: function touchableGetPressOutDelayMS() {
      return this.props.delayPressOut;
    },
    _handleResponderMove: function _handleResponderMove(e) {
      this.touchableHandleResponderMove(e);

      this._dispatchHotspotUpdate(e.nativeEvent.locationX, e.nativeEvent.locationY);
    },
    _dispatchHotspotUpdate: function _dispatchHotspotUpdate(destX, destY) {
      UIManager.dispatchViewManagerCommand(ReactNative.findNodeHandle(this), UIManager.RCTView.Commands.hotspotUpdate, [destX || 0, destY || 0]);
    },
    _dispatchPressedStateChange: function _dispatchPressedStateChange(pressed) {
      UIManager.dispatchViewManagerCommand(ReactNative.findNodeHandle(this), UIManager.RCTView.Commands.setPressed, [pressed]);
    },
    render: function render() {
      var _babelHelpers$extends;

      var child = React.Children.only(this.props.children);
      var children = child.props.children;

      if (Touchable.TOUCH_TARGET_DEBUG && child.type.displayName === 'View') {
        if (!Array.isArray(children)) {
          children = [children];
        }

        children.push(Touchable.renderDebugView({
          color: 'brown',
          hitSlop: this.props.hitSlop
        }));
      }

      if (this.props.useForeground && !TouchableNativeFeedback.canUseNativeForeground()) {
        console.warn('Requested foreground ripple, but it is not available on this version of Android. ' + 'Consider calling TouchableNativeFeedback.canUseNativeForeground() and using a different ' + 'Touchable if the result is false.');
      }

      var drawableProp = this.props.useForeground && TouchableNativeFeedback.canUseNativeForeground() ? 'nativeForegroundAndroid' : 'nativeBackgroundAndroid';
      var childProps = babelHelpers.extends({}, child.props, (_babelHelpers$extends = {}, babelHelpers.defineProperty(_babelHelpers$extends, drawableProp, this.props.background), babelHelpers.defineProperty(_babelHelpers$extends, "accessible", this.props.accessible !== false), babelHelpers.defineProperty(_babelHelpers$extends, "accessibilityLabel", this.props.accessibilityLabel), babelHelpers.defineProperty(_babelHelpers$extends, "accessibilityComponentType", this.props.accessibilityComponentType), babelHelpers.defineProperty(_babelHelpers$extends, "accessibilityTraits", this.props.accessibilityTraits), babelHelpers.defineProperty(_babelHelpers$extends, "children", children), babelHelpers.defineProperty(_babelHelpers$extends, "testID", this.props.testID), babelHelpers.defineProperty(_babelHelpers$extends, "onLayout", this.props.onLayout), babelHelpers.defineProperty(_babelHelpers$extends, "hitSlop", this.props.hitSlop), babelHelpers.defineProperty(_babelHelpers$extends, "isTVSelectable", true), babelHelpers.defineProperty(_babelHelpers$extends, "hasTVPreferredFocus", this.props.hasTVPreferredFocus), babelHelpers.defineProperty(_babelHelpers$extends, "onStartShouldSetResponder", this.touchableHandleStartShouldSetResponder), babelHelpers.defineProperty(_babelHelpers$extends, "onResponderTerminationRequest", this.touchableHandleResponderTerminationRequest), babelHelpers.defineProperty(_babelHelpers$extends, "onResponderGrant", this.touchableHandleResponderGrant), babelHelpers.defineProperty(_babelHelpers$extends, "onResponderMove", this._handleResponderMove), babelHelpers.defineProperty(_babelHelpers$extends, "onResponderRelease", this.touchableHandleResponderRelease), babelHelpers.defineProperty(_babelHelpers$extends, "onResponderTerminate", this.touchableHandleResponderTerminate), _babelHelpers$extends));
      return React.cloneElement(child, childProps);
    }
  });
  module.exports = TouchableNativeFeedback;
},"c0d6127359adee60e42e0d2a170972b5",["9493a89f5d95c3a8a47c65cfed9b5542","e6db4f0efed6b72f641ef0ffed29569f","18eeaf4e01377a466daaccc6ba8ce6f5","1102b68d89d7a6aede9677567aa01362","9be7bff2ec732c7f9f96a83cea3bc22f","9e4c8667cb3e1e5fa7d33d9679f26159","467cd3365342d9aaa2e941fe7ace641c","29cb0e104e5fce198008f3e789631772","6ba49cd3c30b475f38fef604b303c1ac","1b69977972a3b6ad650756d07de7954c"],"TouchableNativeFeedback");