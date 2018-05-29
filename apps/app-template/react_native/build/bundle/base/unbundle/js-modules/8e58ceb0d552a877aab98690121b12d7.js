__d(function (global, _require3, module, exports, _dependencyMap) {
  'use strict';

  var Dimensions = _require3(_dependencyMap[0], 'Dimensions');

  var FrameRateLogger = _require3(_dependencyMap[1], 'FrameRateLogger');

  var Keyboard = _require3(_dependencyMap[2], 'Keyboard');

  var ReactNative = _require3(_dependencyMap[3], 'ReactNative');

  var Subscribable = _require3(_dependencyMap[4], 'Subscribable');

  var TextInputState = _require3(_dependencyMap[5], 'TextInputState');

  var UIManager = _require3(_dependencyMap[6], 'UIManager');

  var invariant = _require3(_dependencyMap[7], 'fbjs/lib/invariant');

  var nullthrows = _require3(_dependencyMap[8], 'fbjs/lib/nullthrows');

  var performanceNow = _require3(_dependencyMap[9], 'fbjs/lib/performanceNow');

  var warning = _require3(_dependencyMap[10], 'fbjs/lib/warning');

  var _require = _require3(_dependencyMap[11], 'NativeModules'),
      ScrollViewManager = _require.ScrollViewManager;

  var _require2 = _require3(_dependencyMap[12], 'ReactNativeComponentTree'),
      getInstanceFromNode = _require2.getInstanceFromNode;

  var IS_ANIMATING_TOUCH_START_THRESHOLD_MS = 16;

  function isTagInstanceOfTextInput(tag) {
    var instance = getInstanceFromNode(tag);
    return instance && instance.viewConfig && (instance.viewConfig.uiViewClassName === 'AndroidTextInput' || instance.viewConfig.uiViewClassName === 'RCTMultilineTextInputView' || instance.viewConfig.uiViewClassName === 'RCTSinglelineTextInputView');
  }

  var ScrollResponderMixin = {
    mixins: [Subscribable.Mixin],
    scrollResponderMixinGetInitialState: function scrollResponderMixinGetInitialState() {
      return {
        isTouching: false,
        lastMomentumScrollBeginTime: 0,
        lastMomentumScrollEndTime: 0,
        observedScrollSinceBecomingResponder: false,
        becameResponderWhileAnimating: false
      };
    },
    scrollResponderHandleScrollShouldSetResponder: function scrollResponderHandleScrollShouldSetResponder() {
      return this.state.isTouching;
    },
    scrollResponderHandleStartShouldSetResponder: function scrollResponderHandleStartShouldSetResponder(e) {
      var currentlyFocusedTextInput = TextInputState.currentlyFocusedField();

      if (this.props.keyboardShouldPersistTaps === 'handled' && currentlyFocusedTextInput != null && e.target !== currentlyFocusedTextInput) {
        return true;
      }

      return false;
    },
    scrollResponderHandleStartShouldSetResponderCapture: function scrollResponderHandleStartShouldSetResponderCapture(e) {
      var currentlyFocusedTextInput = TextInputState.currentlyFocusedField();
      var keyboardShouldPersistTaps = this.props.keyboardShouldPersistTaps;
      var keyboardNeverPersistTaps = !keyboardShouldPersistTaps || keyboardShouldPersistTaps === 'never';

      if (keyboardNeverPersistTaps && currentlyFocusedTextInput != null && !isTagInstanceOfTextInput(e.target)) {
        return true;
      }

      return this.scrollResponderIsAnimating();
    },
    scrollResponderHandleResponderReject: function scrollResponderHandleResponderReject() {},
    scrollResponderHandleTerminationRequest: function scrollResponderHandleTerminationRequest() {
      return !this.state.observedScrollSinceBecomingResponder;
    },
    scrollResponderHandleTouchEnd: function scrollResponderHandleTouchEnd(e) {
      var nativeEvent = e.nativeEvent;
      this.state.isTouching = nativeEvent.touches.length !== 0;
      this.props.onTouchEnd && this.props.onTouchEnd(e);
    },
    scrollResponderHandleTouchCancel: function scrollResponderHandleTouchCancel(e) {
      this.state.isTouching = false;
      this.props.onTouchCancel && this.props.onTouchCancel(e);
    },
    scrollResponderHandleResponderRelease: function scrollResponderHandleResponderRelease(e) {
      this.props.onResponderRelease && this.props.onResponderRelease(e);
      var currentlyFocusedTextInput = TextInputState.currentlyFocusedField();

      if (this.props.keyboardShouldPersistTaps !== true && this.props.keyboardShouldPersistTaps !== 'always' && currentlyFocusedTextInput != null && e.target !== currentlyFocusedTextInput && !this.state.observedScrollSinceBecomingResponder && !this.state.becameResponderWhileAnimating) {
        this.props.onScrollResponderKeyboardDismissed && this.props.onScrollResponderKeyboardDismissed(e);
        TextInputState.blurTextInput(currentlyFocusedTextInput);
      }
    },
    scrollResponderHandleScroll: function scrollResponderHandleScroll(e) {
      this.state.observedScrollSinceBecomingResponder = true;
      this.props.onScroll && this.props.onScroll(e);
    },
    scrollResponderHandleResponderGrant: function scrollResponderHandleResponderGrant(e) {
      this.state.observedScrollSinceBecomingResponder = false;
      this.props.onResponderGrant && this.props.onResponderGrant(e);
      this.state.becameResponderWhileAnimating = this.scrollResponderIsAnimating();
    },
    scrollResponderHandleScrollBeginDrag: function scrollResponderHandleScrollBeginDrag(e) {
      FrameRateLogger.beginScroll();
      this.props.onScrollBeginDrag && this.props.onScrollBeginDrag(e);
    },
    scrollResponderHandleScrollEndDrag: function scrollResponderHandleScrollEndDrag(e) {
      var velocity = e.nativeEvent.velocity;

      if (!this.scrollResponderIsAnimating() && (!velocity || velocity.x === 0 && velocity.y === 0)) {
        FrameRateLogger.endScroll();
      }

      this.props.onScrollEndDrag && this.props.onScrollEndDrag(e);
    },
    scrollResponderHandleMomentumScrollBegin: function scrollResponderHandleMomentumScrollBegin(e) {
      this.state.lastMomentumScrollBeginTime = performanceNow();
      this.props.onMomentumScrollBegin && this.props.onMomentumScrollBegin(e);
    },
    scrollResponderHandleMomentumScrollEnd: function scrollResponderHandleMomentumScrollEnd(e) {
      FrameRateLogger.endScroll();
      this.state.lastMomentumScrollEndTime = performanceNow();
      this.props.onMomentumScrollEnd && this.props.onMomentumScrollEnd(e);
    },
    scrollResponderHandleTouchStart: function scrollResponderHandleTouchStart(e) {
      this.state.isTouching = true;
      this.props.onTouchStart && this.props.onTouchStart(e);
    },
    scrollResponderHandleTouchMove: function scrollResponderHandleTouchMove(e) {
      this.props.onTouchMove && this.props.onTouchMove(e);
    },
    scrollResponderIsAnimating: function scrollResponderIsAnimating() {
      var now = performanceNow();
      var timeSinceLastMomentumScrollEnd = now - this.state.lastMomentumScrollEndTime;
      var isAnimating = timeSinceLastMomentumScrollEnd < IS_ANIMATING_TOUCH_START_THRESHOLD_MS || this.state.lastMomentumScrollEndTime < this.state.lastMomentumScrollBeginTime;
      return isAnimating;
    },
    scrollResponderGetScrollableNode: function scrollResponderGetScrollableNode() {
      return this.getScrollableNode ? this.getScrollableNode() : ReactNative.findNodeHandle(this);
    },
    scrollResponderScrollTo: function scrollResponderScrollTo(x, y, animated) {
      if (typeof x === 'number') {
        console.warn('`scrollResponderScrollTo(x, y, animated)` is deprecated. Use `scrollResponderScrollTo({x: 5, y: 5, animated: true})` instead.');
      } else {
        var _ref = x || {};

        x = _ref.x;
        y = _ref.y;
        animated = _ref.animated;
      }

      UIManager.dispatchViewManagerCommand(nullthrows(this.scrollResponderGetScrollableNode()), UIManager.RCTScrollView.Commands.scrollTo, [x || 0, y || 0, animated !== false]);
    },
    scrollResponderScrollToEnd: function scrollResponderScrollToEnd(options) {
      var animated = (options && options.animated) !== false;
      UIManager.dispatchViewManagerCommand(this.scrollResponderGetScrollableNode(), UIManager.RCTScrollView.Commands.scrollToEnd, [animated]);
    },
    scrollResponderScrollWithoutAnimationTo: function scrollResponderScrollWithoutAnimationTo(offsetX, offsetY) {
      console.warn('`scrollResponderScrollWithoutAnimationTo` is deprecated. Use `scrollResponderScrollTo` instead');
      this.scrollResponderScrollTo({
        x: offsetX,
        y: offsetY,
        animated: false
      });
    },
    scrollResponderZoomTo: function scrollResponderZoomTo(rect, animated) {
      invariant(ScrollViewManager && ScrollViewManager.zoomToRect, 'zoomToRect is not implemented');

      if ('animated' in rect) {
        animated = rect.animated;
        delete rect.animated;
      } else if (typeof animated !== 'undefined') {
        console.warn('`scrollResponderZoomTo` `animated` argument is deprecated. Use `options.animated` instead');
      }

      ScrollViewManager.zoomToRect(this.scrollResponderGetScrollableNode(), rect, animated !== false);
    },
    scrollResponderFlashScrollIndicators: function scrollResponderFlashScrollIndicators() {
      UIManager.dispatchViewManagerCommand(this.scrollResponderGetScrollableNode(), UIManager.RCTScrollView.Commands.flashScrollIndicators, []);
    },
    scrollResponderScrollNativeHandleToKeyboard: function scrollResponderScrollNativeHandleToKeyboard(nodeHandle, additionalOffset, preventNegativeScrollOffset) {
      this.additionalScrollOffset = additionalOffset || 0;
      this.preventNegativeScrollOffset = !!preventNegativeScrollOffset;
      UIManager.measureLayout(nodeHandle, ReactNative.findNodeHandle(this.getInnerViewNode()), this.scrollResponderTextInputFocusError, this.scrollResponderInputMeasureAndScrollToKeyboard);
    },
    scrollResponderInputMeasureAndScrollToKeyboard: function scrollResponderInputMeasureAndScrollToKeyboard(left, top, width, height) {
      var keyboardScreenY = Dimensions.get('window').height;

      if (this.keyboardWillOpenTo) {
        keyboardScreenY = this.keyboardWillOpenTo.endCoordinates.screenY;
      }

      var scrollOffsetY = top - keyboardScreenY + height + this.additionalScrollOffset;

      if (this.preventNegativeScrollOffset) {
        scrollOffsetY = Math.max(0, scrollOffsetY);
      }

      this.scrollResponderScrollTo({
        x: 0,
        y: scrollOffsetY,
        animated: true
      });
      this.additionalOffset = 0;
      this.preventNegativeScrollOffset = false;
    },
    scrollResponderTextInputFocusError: function scrollResponderTextInputFocusError(e) {
      console.error('Error measuring text field: ', e);
    },
    UNSAFE_componentWillMount: function UNSAFE_componentWillMount() {
      var keyboardShouldPersistTaps = this.props.keyboardShouldPersistTaps;
      warning(typeof keyboardShouldPersistTaps !== 'boolean', "'keyboardShouldPersistTaps={" + keyboardShouldPersistTaps + "}' is deprecated. " + ("Use 'keyboardShouldPersistTaps=\"" + (keyboardShouldPersistTaps ? 'always' : 'never') + "\"' instead"));
      this.keyboardWillOpenTo = null;
      this.additionalScrollOffset = 0;
      this.addListenerOn(Keyboard, 'keyboardWillShow', this.scrollResponderKeyboardWillShow);
      this.addListenerOn(Keyboard, 'keyboardWillHide', this.scrollResponderKeyboardWillHide);
      this.addListenerOn(Keyboard, 'keyboardDidShow', this.scrollResponderKeyboardDidShow);
      this.addListenerOn(Keyboard, 'keyboardDidHide', this.scrollResponderKeyboardDidHide);
    },
    scrollResponderKeyboardWillShow: function scrollResponderKeyboardWillShow(e) {
      this.keyboardWillOpenTo = e;
      this.props.onKeyboardWillShow && this.props.onKeyboardWillShow(e);
    },
    scrollResponderKeyboardWillHide: function scrollResponderKeyboardWillHide(e) {
      this.keyboardWillOpenTo = null;
      this.props.onKeyboardWillHide && this.props.onKeyboardWillHide(e);
    },
    scrollResponderKeyboardDidShow: function scrollResponderKeyboardDidShow(e) {
      if (e) {
        this.keyboardWillOpenTo = e;
      }

      this.props.onKeyboardDidShow && this.props.onKeyboardDidShow(e);
    },
    scrollResponderKeyboardDidHide: function scrollResponderKeyboardDidHide(e) {
      this.keyboardWillOpenTo = null;
      this.props.onKeyboardDidHide && this.props.onKeyboardDidHide(e);
    }
  };
  var ScrollResponder = {
    Mixin: ScrollResponderMixin
  };
  module.exports = ScrollResponder;
},"8e58ceb0d552a877aab98690121b12d7",["cbac9baa189a2fa69f7f5fdd76e9bc71","7ce68119ca8cc878ecda6c429e543689","a78d5645d35f0086ade08580dc4b6985","1102b68d89d7a6aede9677567aa01362","297ca1965b3a80427c025ba042b06a22","a54fbb2d9dfc450c68d1d302d42f00e1","467cd3365342d9aaa2e941fe7ace641c","8940a4ad43b101ffc23e725363c70f8d","414343d6c18458363152ad20d294a37b","132e221ae2b81367ff1b4124cfdfb0aa","09babf511a081d9520406a63f452d2ef","ce21807d4d291be64fa852393519f6c8","5f0acb3793a716210fc3ff0aec1d1f30"],"ScrollResponder");