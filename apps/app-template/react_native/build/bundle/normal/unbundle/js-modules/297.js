__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-native/Libraries/Experimental/SwipeableRow/SwipeableRow.js";

  var Animated = _require(_dependencyMap[0], 'Animated');

  var I18nManager = _require(_dependencyMap[1], 'I18nManager');

  var PanResponder = _require(_dependencyMap[2], 'PanResponder');

  var React = _require(_dependencyMap[3], 'React');

  var PropTypes = _require(_dependencyMap[4], 'prop-types');

  var StyleSheet = _require(_dependencyMap[5], 'StyleSheet');

  var TimerMixin = _require(_dependencyMap[6], 'react-timer-mixin');

  var View = _require(_dependencyMap[7], 'View');

  var createReactClass = _require(_dependencyMap[8], 'create-react-class');

  var emptyFunction = _require(_dependencyMap[9], 'fbjs/lib/emptyFunction');

  var IS_RTL = I18nManager.isRTL;
  var CLOSED_LEFT_POSITION = 0;
  var HORIZONTAL_SWIPE_DISTANCE_THRESHOLD = 10;
  var HORIZONTAL_FULL_SWIPE_SPEED_THRESHOLD = 0.3;
  var SLOW_SPEED_SWIPE_FACTOR = 4;
  var SWIPE_DURATION = 300;
  var ON_MOUNT_BOUNCE_DELAY = 700;
  var ON_MOUNT_BOUNCE_DURATION = 400;
  var RIGHT_SWIPE_BOUNCE_BACK_DISTANCE = 30;
  var RIGHT_SWIPE_BOUNCE_BACK_DURATION = 300;
  var RIGHT_SWIPE_THRESHOLD = 30 * SLOW_SPEED_SWIPE_FACTOR;
  var SwipeableRow = createReactClass({
    displayName: 'SwipeableRow',
    _panResponder: {},
    _previousLeft: CLOSED_LEFT_POSITION,
    mixins: [TimerMixin],
    propTypes: {
      children: PropTypes.any,
      isOpen: PropTypes.bool,
      preventSwipeRight: PropTypes.bool,
      maxSwipeDistance: PropTypes.number.isRequired,
      onOpen: PropTypes.func.isRequired,
      onClose: PropTypes.func.isRequired,
      onSwipeEnd: PropTypes.func.isRequired,
      onSwipeStart: PropTypes.func.isRequired,
      shouldBounceOnMount: PropTypes.bool,
      slideoutView: PropTypes.node.isRequired,
      swipeThreshold: PropTypes.number.isRequired
    },
    getInitialState: function getInitialState() {
      return {
        currentLeft: new Animated.Value(this._previousLeft),
        isSwipeableViewRendered: false,
        rowHeight: null
      };
    },
    getDefaultProps: function getDefaultProps() {
      return {
        isOpen: false,
        preventSwipeRight: false,
        maxSwipeDistance: 0,
        onOpen: emptyFunction,
        onClose: emptyFunction,
        onSwipeEnd: emptyFunction,
        onSwipeStart: emptyFunction,
        swipeThreshold: 30
      };
    },
    UNSAFE_componentWillMount: function UNSAFE_componentWillMount() {
      this._panResponder = PanResponder.create({
        onMoveShouldSetPanResponderCapture: this._handleMoveShouldSetPanResponderCapture,
        onPanResponderGrant: this._handlePanResponderGrant,
        onPanResponderMove: this._handlePanResponderMove,
        onPanResponderRelease: this._handlePanResponderEnd,
        onPanResponderTerminationRequest: this._onPanResponderTerminationRequest,
        onPanResponderTerminate: this._handlePanResponderEnd,
        onShouldBlockNativeResponder: function onShouldBlockNativeResponder(event, gestureState) {
          return false;
        }
      });
    },
    componentDidMount: function componentDidMount() {
      var _this = this;

      if (this.props.shouldBounceOnMount) {
        this.setTimeout(function () {
          _this._animateBounceBack(ON_MOUNT_BOUNCE_DURATION);
        }, ON_MOUNT_BOUNCE_DELAY);
      }
    },
    UNSAFE_componentWillReceiveProps: function UNSAFE_componentWillReceiveProps(nextProps) {
      if (this.props.isOpen && !nextProps.isOpen) {
        this._animateToClosedPosition();
      }
    },
    render: function render() {
      var slideOutView = void 0;

      if (this.state.isSwipeableViewRendered && this.state.rowHeight) {
        slideOutView = React.createElement(
          View,
          {
            style: [styles.slideOutContainer, {
              height: this.state.rowHeight
            }],
            __source: {
              fileName: _jsxFileName,
              lineNumber: 162
            }
          },
          this.props.slideoutView
        );
      }

      var swipeableView = React.createElement(
        Animated.View,
        {
          onLayout: this._onSwipeableViewLayout,
          style: {
            transform: [{
              translateX: this.state.currentLeft
            }]
          },
          __source: {
            fileName: _jsxFileName,
            lineNumber: 173
          }
        },
        this.props.children
      );
      return React.createElement(
        View,
        babelHelpers.extends({}, this._panResponder.panHandlers, {
          __source: {
            fileName: _jsxFileName,
            lineNumber: 181
          }
        }),
        slideOutView,
        swipeableView
      );
    },
    close: function close() {
      this.props.onClose();

      this._animateToClosedPosition();
    },
    _onSwipeableViewLayout: function _onSwipeableViewLayout(event) {
      this.setState({
        isSwipeableViewRendered: true,
        rowHeight: event.nativeEvent.layout.height
      });
    },
    _handleMoveShouldSetPanResponderCapture: function _handleMoveShouldSetPanResponderCapture(event, gestureState) {
      return gestureState.dy < 10 && this._isValidSwipe(gestureState);
    },
    _handlePanResponderGrant: function _handlePanResponderGrant(event, gestureState) {},
    _handlePanResponderMove: function _handlePanResponderMove(event, gestureState) {
      if (this._isSwipingExcessivelyRightFromClosedPosition(gestureState)) {
        return;
      }

      this.props.onSwipeStart();

      if (this._isSwipingRightFromClosed(gestureState)) {
        this._swipeSlowSpeed(gestureState);
      } else {
        this._swipeFullSpeed(gestureState);
      }
    },
    _isSwipingRightFromClosed: function _isSwipingRightFromClosed(gestureState) {
      var gestureStateDx = IS_RTL ? -gestureState.dx : gestureState.dx;
      return this._previousLeft === CLOSED_LEFT_POSITION && gestureStateDx > 0;
    },
    _swipeFullSpeed: function _swipeFullSpeed(gestureState) {
      this.state.currentLeft.setValue(this._previousLeft + gestureState.dx);
    },
    _swipeSlowSpeed: function _swipeSlowSpeed(gestureState) {
      this.state.currentLeft.setValue(this._previousLeft + gestureState.dx / SLOW_SPEED_SWIPE_FACTOR);
    },
    _isSwipingExcessivelyRightFromClosedPosition: function _isSwipingExcessivelyRightFromClosedPosition(gestureState) {
      var gestureStateDx = IS_RTL ? -gestureState.dx : gestureState.dx;
      return this._isSwipingRightFromClosed(gestureState) && gestureStateDx > RIGHT_SWIPE_THRESHOLD;
    },
    _onPanResponderTerminationRequest: function _onPanResponderTerminationRequest(event, gestureState) {
      return false;
    },
    _animateTo: function _animateTo(toValue) {
      var _this2 = this;

      var duration = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : SWIPE_DURATION;
      var callback = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : emptyFunction;
      Animated.timing(this.state.currentLeft, {
        duration: duration,
        toValue: toValue,
        useNativeDriver: true
      }).start(function () {
        _this2._previousLeft = toValue;
        callback();
      });
    },
    _animateToOpenPosition: function _animateToOpenPosition() {
      var maxSwipeDistance = IS_RTL ? -this.props.maxSwipeDistance : this.props.maxSwipeDistance;

      this._animateTo(-maxSwipeDistance);
    },
    _animateToOpenPositionWith: function _animateToOpenPositionWith(speed, distMoved) {
      speed = speed > HORIZONTAL_FULL_SWIPE_SPEED_THRESHOLD ? speed : HORIZONTAL_FULL_SWIPE_SPEED_THRESHOLD;
      var duration = Math.abs((this.props.maxSwipeDistance - Math.abs(distMoved)) / speed);
      var maxSwipeDistance = IS_RTL ? -this.props.maxSwipeDistance : this.props.maxSwipeDistance;

      this._animateTo(-maxSwipeDistance, duration);
    },
    _animateToClosedPosition: function _animateToClosedPosition() {
      var duration = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : SWIPE_DURATION;

      this._animateTo(CLOSED_LEFT_POSITION, duration);
    },
    _animateToClosedPositionDuringBounce: function _animateToClosedPositionDuringBounce() {
      this._animateToClosedPosition(RIGHT_SWIPE_BOUNCE_BACK_DURATION);
    },
    _animateBounceBack: function _animateBounceBack(duration) {
      var swipeBounceBackDistance = IS_RTL ? -RIGHT_SWIPE_BOUNCE_BACK_DISTANCE : RIGHT_SWIPE_BOUNCE_BACK_DISTANCE;

      this._animateTo(-swipeBounceBackDistance, duration, this._animateToClosedPositionDuringBounce);
    },
    _isValidSwipe: function _isValidSwipe(gestureState) {
      if (this.props.preventSwipeRight && this._previousLeft === CLOSED_LEFT_POSITION && gestureState.dx > 0) {
        return false;
      }

      return Math.abs(gestureState.dx) > HORIZONTAL_SWIPE_DISTANCE_THRESHOLD;
    },
    _shouldAnimateRemainder: function _shouldAnimateRemainder(gestureState) {
      return Math.abs(gestureState.dx) > this.props.swipeThreshold || gestureState.vx > HORIZONTAL_FULL_SWIPE_SPEED_THRESHOLD;
    },
    _handlePanResponderEnd: function _handlePanResponderEnd(event, gestureState) {
      var horizontalDistance = IS_RTL ? -gestureState.dx : gestureState.dx;

      if (this._isSwipingRightFromClosed(gestureState)) {
        this.props.onOpen();

        this._animateBounceBack(RIGHT_SWIPE_BOUNCE_BACK_DURATION);
      } else if (this._shouldAnimateRemainder(gestureState)) {
        if (horizontalDistance < 0) {
          this.props.onOpen();

          this._animateToOpenPositionWith(gestureState.vx, horizontalDistance);
        } else {
          this.props.onClose();

          this._animateToClosedPosition();
        }
      } else {
        if (this._previousLeft === CLOSED_LEFT_POSITION) {
          this._animateToClosedPosition();
        } else {
          this._animateToOpenPosition();
        }
      }

      this.props.onSwipeEnd();
    }
  });
  var styles = StyleSheet.create({
    slideOutContainer: {
      bottom: 0,
      left: 0,
      position: 'absolute',
      right: 0,
      top: 0
    }
  });
  module.exports = SwipeableRow;
},297,[199,284,298,132,129,171,196,173,176,16],"SwipeableRow");