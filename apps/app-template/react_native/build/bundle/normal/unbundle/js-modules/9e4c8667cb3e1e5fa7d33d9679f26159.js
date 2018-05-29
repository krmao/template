__d(function (global, _require2, module, exports, _dependencyMap) {
  'use strict';

  var EdgeInsetsPropType = _require2(_dependencyMap[0], 'EdgeInsetsPropType');

  var React = _require2(_dependencyMap[1], 'React');

  var PropTypes = _require2(_dependencyMap[2], 'prop-types');

  var TimerMixin = _require2(_dependencyMap[3], 'react-timer-mixin');

  var Touchable = _require2(_dependencyMap[4], 'Touchable');

  var createReactClass = _require2(_dependencyMap[5], 'create-react-class');

  var ensurePositiveDelayProps = _require2(_dependencyMap[6], 'ensurePositiveDelayProps');

  var warning = _require2(_dependencyMap[7], 'fbjs/lib/warning');

  var _require = _require2(_dependencyMap[8], 'ViewAccessibility'),
      AccessibilityComponentTypes = _require.AccessibilityComponentTypes,
      AccessibilityTraits = _require.AccessibilityTraits;

  var PRESS_RETENTION_OFFSET = {
    top: 20,
    left: 20,
    right: 20,
    bottom: 30
  };
  var TouchableWithoutFeedback = createReactClass({
    displayName: 'TouchableWithoutFeedback',
    mixins: [TimerMixin, Touchable.Mixin],
    propTypes: {
      accessible: PropTypes.bool,
      accessibilityComponentType: PropTypes.oneOf(AccessibilityComponentTypes),
      accessibilityTraits: PropTypes.oneOfType([PropTypes.oneOf(AccessibilityTraits), PropTypes.arrayOf(PropTypes.oneOf(AccessibilityTraits))]),
      disabled: PropTypes.bool,
      onPress: PropTypes.func,
      onPressIn: PropTypes.func,
      onPressOut: PropTypes.func,
      onLayout: PropTypes.func,
      onLongPress: PropTypes.func,
      delayPressIn: PropTypes.number,
      delayPressOut: PropTypes.number,
      delayLongPress: PropTypes.number,
      pressRetentionOffset: EdgeInsetsPropType,
      hitSlop: EdgeInsetsPropType
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
    touchableHandlePress: function touchableHandlePress(e) {
      this.props.onPress && this.props.onPress(e);
    },
    touchableHandleActivePressIn: function touchableHandleActivePressIn(e) {
      this.props.onPressIn && this.props.onPressIn(e);
    },
    touchableHandleActivePressOut: function touchableHandleActivePressOut(e) {
      this.props.onPressOut && this.props.onPressOut(e);
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
      return this.props.delayPressOut || 0;
    },
    render: function render() {
      var child = React.Children.only(this.props.children);
      var children = child.props.children;
      warning(!child.type || child.type.displayName !== 'Text', 'TouchableWithoutFeedback does not work well with Text children. Wrap children in a View instead. See ' + (child._owner && child._owner.getName && child._owner.getName() || '<unknown>'));

      if (Touchable.TOUCH_TARGET_DEBUG && child.type && child.type.displayName === 'View') {
        children = React.Children.toArray(children);
        children.push(Touchable.renderDebugView({
          color: 'red',
          hitSlop: this.props.hitSlop
        }));
      }

      var style = Touchable.TOUCH_TARGET_DEBUG && child.type && child.type.displayName === 'Text' ? [child.props.style, {
        color: 'red'
      }] : child.props.style;
      return React.cloneElement(child, {
        accessible: this.props.accessible !== false,
        accessibilityLabel: this.props.accessibilityLabel,
        accessibilityComponentType: this.props.accessibilityComponentType,
        accessibilityTraits: this.props.accessibilityTraits,
        nativeID: this.props.nativeID,
        testID: this.props.testID,
        onLayout: this.props.onLayout,
        hitSlop: this.props.hitSlop,
        onStartShouldSetResponder: this.touchableHandleStartShouldSetResponder,
        onResponderTerminationRequest: this.touchableHandleResponderTerminationRequest,
        onResponderGrant: this.touchableHandleResponderGrant,
        onResponderMove: this.touchableHandleResponderMove,
        onResponderRelease: this.touchableHandleResponderRelease,
        onResponderTerminate: this.touchableHandleResponderTerminate,
        style: style,
        children: children
      });
    }
  });
  module.exports = TouchableWithoutFeedback;
},"9e4c8667cb3e1e5fa7d33d9679f26159",["20099b775ac7bd546d3c34ceb85c88e4","e6db4f0efed6b72f641ef0ffed29569f","18eeaf4e01377a466daaccc6ba8ce6f5","5c0ec7f0bbb23ef3f86807b9f50421dc","9be7bff2ec732c7f9f96a83cea3bc22f","29cb0e104e5fce198008f3e789631772","6ba49cd3c30b475f38fef604b303c1ac","09babf511a081d9520406a63f452d2ef","f436935d9ebc8f545189df019c06c4b6"],"TouchableWithoutFeedback");