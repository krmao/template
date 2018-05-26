__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-native/Libraries/Components/ScrollView/ScrollView.js";

  var AnimatedImplementation = _require(_dependencyMap[0], 'AnimatedImplementation');

  var ColorPropType = _require(_dependencyMap[1], 'ColorPropType');

  var EdgeInsetsPropType = _require(_dependencyMap[2], 'EdgeInsetsPropType');

  var Platform = _require(_dependencyMap[3], 'Platform');

  var PointPropType = _require(_dependencyMap[4], 'PointPropType');

  var PropTypes = _require(_dependencyMap[5], 'prop-types');

  var React = _require(_dependencyMap[6], 'React');

  var ReactNative = _require(_dependencyMap[7], 'ReactNative');

  var ScrollResponder = _require(_dependencyMap[8], 'ScrollResponder');

  var ScrollViewStickyHeader = _require(_dependencyMap[9], 'ScrollViewStickyHeader');

  var StyleSheet = _require(_dependencyMap[10], 'StyleSheet');

  var StyleSheetPropType = _require(_dependencyMap[11], 'StyleSheetPropType');

  var View = _require(_dependencyMap[12], 'View');

  var ViewPropTypes = _require(_dependencyMap[13], 'ViewPropTypes');

  var ViewStylePropTypes = _require(_dependencyMap[14], 'ViewStylePropTypes');

  var createReactClass = _require(_dependencyMap[15], 'create-react-class');

  var dismissKeyboard = _require(_dependencyMap[16], 'dismissKeyboard');

  var flattenStyle = _require(_dependencyMap[17], 'flattenStyle');

  var invariant = _require(_dependencyMap[18], 'fbjs/lib/invariant');

  var processDecelerationRate = _require(_dependencyMap[19], 'processDecelerationRate');

  var requireNativeComponent = _require(_dependencyMap[20], 'requireNativeComponent');

  var warning = _require(_dependencyMap[21], 'fbjs/lib/warning');

  var resolveAssetSource = _require(_dependencyMap[22], 'resolveAssetSource');

  var ScrollView = createReactClass({
    displayName: 'ScrollView',
    propTypes: babelHelpers.extends({}, ViewPropTypes, {
      automaticallyAdjustContentInsets: PropTypes.bool,
      contentInset: EdgeInsetsPropType,
      contentOffset: PointPropType,
      bounces: PropTypes.bool,
      bouncesZoom: PropTypes.bool,
      alwaysBounceHorizontal: PropTypes.bool,
      alwaysBounceVertical: PropTypes.bool,
      centerContent: PropTypes.bool,
      contentContainerStyle: StyleSheetPropType(ViewStylePropTypes),
      decelerationRate: PropTypes.oneOfType([PropTypes.oneOf(['fast', 'normal']), PropTypes.number]),
      horizontal: PropTypes.bool,
      indicatorStyle: PropTypes.oneOf(['default', 'black', 'white']),
      invertStickyHeaders: PropTypes.bool,
      directionalLockEnabled: PropTypes.bool,
      canCancelContentTouches: PropTypes.bool,
      keyboardDismissMode: PropTypes.oneOf(['none', 'on-drag', 'interactive']),
      keyboardShouldPersistTaps: PropTypes.oneOf(['always', 'never', 'handled', false, true]),
      maintainVisibleContentPosition: PropTypes.shape({
        minIndexForVisible: PropTypes.number.isRequired,
        autoscrollToTopThreshold: PropTypes.number
      }),
      maximumZoomScale: PropTypes.number,
      minimumZoomScale: PropTypes.number,
      onMomentumScrollBegin: PropTypes.func,
      onMomentumScrollEnd: PropTypes.func,
      onScroll: PropTypes.func,
      onScrollBeginDrag: PropTypes.func,
      onScrollEndDrag: PropTypes.func,
      onContentSizeChange: PropTypes.func,
      pagingEnabled: PropTypes.bool,
      pinchGestureEnabled: PropTypes.bool,
      scrollEnabled: PropTypes.bool,
      scrollEventThrottle: PropTypes.number,
      scrollIndicatorInsets: EdgeInsetsPropType,
      scrollsToTop: PropTypes.bool,
      showsHorizontalScrollIndicator: PropTypes.bool,
      showsVerticalScrollIndicator: PropTypes.bool,
      stickyHeaderIndices: PropTypes.arrayOf(PropTypes.number),
      snapToInterval: PropTypes.number,
      snapToAlignment: PropTypes.oneOf(['start', 'center', 'end']),
      removeClippedSubviews: PropTypes.bool,
      zoomScale: PropTypes.number,
      contentInsetAdjustmentBehavior: PropTypes.oneOf(['automatic', 'scrollableAxes', 'never', 'always']),
      refreshControl: PropTypes.element,
      endFillColor: ColorPropType,
      scrollPerfTag: PropTypes.string,
      overScrollMode: PropTypes.oneOf(['auto', 'always', 'never']),
      DEPRECATED_sendUpdatedChildFrames: PropTypes.bool,
      scrollBarThumbImage: PropTypes.oneOfType([PropTypes.shape({
        uri: PropTypes.string
      }), PropTypes.number])
    }),
    mixins: [ScrollResponder.Mixin],
    _scrollAnimatedValue: new AnimatedImplementation.Value(0),
    _scrollAnimatedValueAttachment: null,
    _stickyHeaderRefs: new Map(),
    _headerLayoutYs: new Map(),
    getInitialState: function getInitialState() {
      return babelHelpers.extends({}, this.scrollResponderMixinGetInitialState(), {
        layoutHeight: null
      });
    },
    UNSAFE_componentWillMount: function UNSAFE_componentWillMount() {
      this._scrollAnimatedValue = new AnimatedImplementation.Value(this.props.contentOffset ? this.props.contentOffset.y : 0);

      this._scrollAnimatedValue.setOffset(this.props.contentInset ? this.props.contentInset.top : 0);

      this._stickyHeaderRefs = new Map();
      this._headerLayoutYs = new Map();
    },
    componentDidMount: function componentDidMount() {
      this._updateAnimatedNodeAttachment();
    },
    componentDidUpdate: function componentDidUpdate() {
      this._updateAnimatedNodeAttachment();
    },
    componentWillUnmount: function componentWillUnmount() {
      if (this._scrollAnimatedValueAttachment) {
        this._scrollAnimatedValueAttachment.detach();
      }
    },
    setNativeProps: function setNativeProps(props) {
      this._scrollViewRef && this._scrollViewRef.setNativeProps(props);
    },
    getScrollResponder: function getScrollResponder() {
      return this;
    },
    getScrollableNode: function getScrollableNode() {
      return ReactNative.findNodeHandle(this._scrollViewRef);
    },
    getInnerViewNode: function getInnerViewNode() {
      return ReactNative.findNodeHandle(this._innerViewRef);
    },
    scrollTo: function scrollTo(y, x, animated) {
      if (typeof y === 'number') {
        console.warn('`scrollTo(y, x, animated)` is deprecated. Use `scrollTo({x: 5, y: 5, ' + 'animated: true})` instead.');
      } else {
        var _ref = y || {};

        x = _ref.x;
        y = _ref.y;
        animated = _ref.animated;
      }

      this.getScrollResponder().scrollResponderScrollTo({
        x: x || 0,
        y: y || 0,
        animated: animated !== false
      });
    },
    scrollToEnd: function scrollToEnd(options) {
      var animated = (options && options.animated) !== false;
      this.getScrollResponder().scrollResponderScrollToEnd({
        animated: animated
      });
    },
    scrollWithoutAnimationTo: function scrollWithoutAnimationTo() {
      var y = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : 0;
      var x = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : 0;
      console.warn('`scrollWithoutAnimationTo` is deprecated. Use `scrollTo` instead');
      this.scrollTo({
        x: x,
        y: y,
        animated: false
      });
    },
    flashScrollIndicators: function flashScrollIndicators() {
      this.getScrollResponder().scrollResponderFlashScrollIndicators();
    },
    _getKeyForIndex: function _getKeyForIndex(index, childArray) {
      var child = childArray[index];
      return child && child.key;
    },
    _updateAnimatedNodeAttachment: function _updateAnimatedNodeAttachment() {
      if (this._scrollAnimatedValueAttachment) {
        this._scrollAnimatedValueAttachment.detach();
      }

      if (this.props.stickyHeaderIndices && this.props.stickyHeaderIndices.length > 0) {
        this._scrollAnimatedValueAttachment = AnimatedImplementation.attachNativeEvent(this._scrollViewRef, 'onScroll', [{
          nativeEvent: {
            contentOffset: {
              y: this._scrollAnimatedValue
            }
          }
        }]);
      }
    },
    _setStickyHeaderRef: function _setStickyHeaderRef(key, ref) {
      if (ref) {
        this._stickyHeaderRefs.set(key, ref);
      } else {
        this._stickyHeaderRefs.delete(key);
      }
    },
    _onStickyHeaderLayout: function _onStickyHeaderLayout(index, event, key) {
      if (!this.props.stickyHeaderIndices) {
        return;
      }

      var childArray = React.Children.toArray(this.props.children);

      if (key !== this._getKeyForIndex(index, childArray)) {
        return;
      }

      var layoutY = event.nativeEvent.layout.y;

      this._headerLayoutYs.set(key, layoutY);

      var indexOfIndex = this.props.stickyHeaderIndices.indexOf(index);
      var previousHeaderIndex = this.props.stickyHeaderIndices[indexOfIndex - 1];

      if (previousHeaderIndex != null) {
        var previousHeader = this._stickyHeaderRefs.get(this._getKeyForIndex(previousHeaderIndex, childArray));

        previousHeader && previousHeader.setNextHeaderY(layoutY);
      }
    },
    _handleScroll: function _handleScroll(e) {
      if (__DEV__) {
        if (this.props.onScroll && this.props.scrollEventThrottle == null && Platform.OS === 'ios') {
          console.log('You specified `onScroll` on a <ScrollView> but not ' + '`scrollEventThrottle`. You will only receive one event. ' + 'Using `16` you get all the events but be aware that it may ' + 'cause frame drops, use a bigger number if you don\'t need as ' + 'much precision.');
        }
      }

      if (Platform.OS === 'android') {
        if (this.props.keyboardDismissMode === 'on-drag') {
          dismissKeyboard();
        }
      }

      this.scrollResponderHandleScroll(e);
    },
    _handleLayout: function _handleLayout(e) {
      if (this.props.invertStickyHeaders) {
        this.setState({
          layoutHeight: e.nativeEvent.layout.height
        });
      }

      if (this.props.onLayout) {
        this.props.onLayout(e);
      }
    },
    _handleContentOnLayout: function _handleContentOnLayout(e) {
      var _e$nativeEvent$layout = e.nativeEvent.layout,
          width = _e$nativeEvent$layout.width,
          height = _e$nativeEvent$layout.height;
      this.props.onContentSizeChange && this.props.onContentSizeChange(width, height);
    },
    _scrollViewRef: null,
    _setScrollViewRef: function _setScrollViewRef(ref) {
      this._scrollViewRef = ref;
    },
    _innerViewRef: null,
    _setInnerViewRef: function _setInnerViewRef(ref) {
      this._innerViewRef = ref;
    },
    render: function render() {
      var _this = this;

      var ScrollViewClass = void 0;
      var ScrollContentContainerViewClass = void 0;

      if (Platform.OS === 'android') {
        if (this.props.horizontal) {
          ScrollViewClass = AndroidHorizontalScrollView;
          ScrollContentContainerViewClass = AndroidHorizontalScrollContentView;
        } else {
          ScrollViewClass = AndroidScrollView;
          ScrollContentContainerViewClass = View;
        }
      } else {
        ScrollViewClass = RCTScrollView;
        ScrollContentContainerViewClass = RCTScrollContentView;
        warning(!this.props.snapToInterval || !this.props.pagingEnabled, 'snapToInterval is currently ignored when pagingEnabled is true.');
      }

      invariant(ScrollViewClass !== undefined, 'ScrollViewClass must not be undefined');
      invariant(ScrollContentContainerViewClass !== undefined, 'ScrollContentContainerViewClass must not be undefined');
      var contentContainerStyle = [this.props.horizontal && styles.contentContainerHorizontal, this.props.contentContainerStyle];
      var style = void 0,
          childLayoutProps = void 0;

      if (__DEV__ && this.props.style) {
        style = flattenStyle(this.props.style);
        childLayoutProps = ['alignItems', 'justifyContent'].filter(function (prop) {
          return style && style[prop] !== undefined;
        });
        invariant(childLayoutProps.length === 0, 'ScrollView child layout (' + JSON.stringify(childLayoutProps) + ') must be applied through the contentContainerStyle prop.');
      }

      var contentSizeChangeProps = {};

      if (this.props.onContentSizeChange) {
        contentSizeChangeProps = {
          onLayout: this._handleContentOnLayout
        };
      }

      var stickyHeaderIndices = this.props.stickyHeaderIndices;
      var hasStickyHeaders = stickyHeaderIndices && stickyHeaderIndices.length > 0;
      var childArray = hasStickyHeaders && React.Children.toArray(this.props.children);
      var children = hasStickyHeaders ? childArray.map(function (child, index) {
        var indexOfIndex = child ? stickyHeaderIndices.indexOf(index) : -1;

        if (indexOfIndex > -1) {
          var key = child.key;
          var nextIndex = stickyHeaderIndices[indexOfIndex + 1];
          return React.createElement(
            ScrollViewStickyHeader,
            {
              key: key,
              ref: function ref(_ref2) {
                return _this._setStickyHeaderRef(key, _ref2);
              },
              nextHeaderLayoutY: _this._headerLayoutYs.get(_this._getKeyForIndex(nextIndex, childArray)),
              onLayout: function onLayout(event) {
                return _this._onStickyHeaderLayout(index, event, key);
              },
              scrollAnimatedValue: _this._scrollAnimatedValue,
              inverted: _this.props.invertStickyHeaders,
              scrollViewHeight: _this.state.layoutHeight,
              __source: {
                fileName: _jsxFileName,
                lineNumber: 772
              }
            },
            child
          );
        } else {
          return child;
        }
      }) : this.props.children;
      var contentContainer = React.createElement(
        ScrollContentContainerViewClass,
        babelHelpers.extends({}, contentSizeChangeProps, {
          ref: this._setInnerViewRef,
          style: contentContainerStyle,
          removeClippedSubviews: Platform.OS === 'android' && hasStickyHeaders ? false : this.props.removeClippedSubviews,
          collapsable: false,
          __source: {
            fileName: _jsxFileName,
            lineNumber: 791
          }
        }),
        children
      );
      var alwaysBounceHorizontal = this.props.alwaysBounceHorizontal !== undefined ? this.props.alwaysBounceHorizontal : this.props.horizontal;
      var alwaysBounceVertical = this.props.alwaysBounceVertical !== undefined ? this.props.alwaysBounceVertical : !this.props.horizontal;
      var DEPRECATED_sendUpdatedChildFrames = !!this.props.DEPRECATED_sendUpdatedChildFrames;
      var baseStyle = this.props.horizontal ? styles.baseHorizontal : styles.baseVertical;
      var props = babelHelpers.extends({}, this.props, {
        alwaysBounceHorizontal: alwaysBounceHorizontal,
        alwaysBounceVertical: alwaysBounceVertical,
        style: [baseStyle, this.props.style],
        onContentSizeChange: null,
        onLayout: this._handleLayout,
        onMomentumScrollBegin: this.scrollResponderHandleMomentumScrollBegin,
        onMomentumScrollEnd: this.scrollResponderHandleMomentumScrollEnd,
        onResponderGrant: this.scrollResponderHandleResponderGrant,
        onResponderReject: this.scrollResponderHandleResponderReject,
        onResponderRelease: this.scrollResponderHandleResponderRelease,
        onResponderTerminate: this.scrollResponderHandleTerminate,
        onResponderTerminationRequest: this.scrollResponderHandleTerminationRequest,
        onScroll: this._handleScroll,
        onScrollBeginDrag: this.scrollResponderHandleScrollBeginDrag,
        onScrollEndDrag: this.scrollResponderHandleScrollEndDrag,
        onScrollShouldSetResponder: this.scrollResponderHandleScrollShouldSetResponder,
        onStartShouldSetResponder: this.scrollResponderHandleStartShouldSetResponder,
        onStartShouldSetResponderCapture: this.scrollResponderHandleStartShouldSetResponderCapture,
        onTouchEnd: this.scrollResponderHandleTouchEnd,
        onTouchMove: this.scrollResponderHandleTouchMove,
        onTouchStart: this.scrollResponderHandleTouchStart,
        onTouchCancel: this.scrollResponderHandleTouchCancel,
        scrollBarThumbImage: resolveAssetSource(this.props.scrollBarThumbImage),
        scrollEventThrottle: hasStickyHeaders ? 1 : this.props.scrollEventThrottle,
        sendMomentumEvents: this.props.onMomentumScrollBegin || this.props.onMomentumScrollEnd ? true : false,
        DEPRECATED_sendUpdatedChildFrames: DEPRECATED_sendUpdatedChildFrames
      });
      var decelerationRate = this.props.decelerationRate;

      if (decelerationRate) {
        props.decelerationRate = processDecelerationRate(decelerationRate);
      }

      var refreshControl = this.props.refreshControl;

      if (refreshControl) {
        if (Platform.OS === 'ios') {
          return React.createElement(
            ScrollViewClass,
            babelHelpers.extends({}, props, {
              ref: this._setScrollViewRef,
              __source: {
                fileName: _jsxFileName,
                lineNumber: 865
              }
            }),
            Platform.isTVOS ? null : refreshControl,
            contentContainer
          );
        } else if (Platform.OS === 'android') {
          return React.cloneElement(refreshControl, {
            style: props.style
          }, React.createElement(
            ScrollViewClass,
            babelHelpers.extends({}, props, {
              style: baseStyle,
              ref: this._setScrollViewRef,
              __source: {
                fileName: _jsxFileName,
                lineNumber: 880
              }
            }),
            contentContainer
          ));
        }
      }

      return React.createElement(
        ScrollViewClass,
        babelHelpers.extends({}, props, {
          ref: this._setScrollViewRef,
          __source: {
            fileName: _jsxFileName,
            lineNumber: 887
          }
        }),
        contentContainer
      );
    }
  });
  var styles = StyleSheet.create({
    baseVertical: {
      flexGrow: 1,
      flexShrink: 1,
      flexDirection: 'column',
      overflow: 'scroll'
    },
    baseHorizontal: {
      flexGrow: 1,
      flexShrink: 1,
      flexDirection: 'row',
      overflow: 'scroll'
    },
    contentContainerHorizontal: {
      flexDirection: 'row'
    }
  });
  var nativeOnlyProps = void 0,
      AndroidScrollView = void 0,
      AndroidHorizontalScrollContentView = void 0,
      AndroidHorizontalScrollView = void 0,
      RCTScrollView = void 0,
      RCTScrollContentView = void 0;

  if (Platform.OS === 'android') {
    nativeOnlyProps = {
      nativeOnly: {
        sendMomentumEvents: true
      }
    };
    AndroidScrollView = requireNativeComponent('RCTScrollView', ScrollView, nativeOnlyProps);
    AndroidHorizontalScrollView = requireNativeComponent('AndroidHorizontalScrollView', ScrollView, nativeOnlyProps);
    AndroidHorizontalScrollContentView = requireNativeComponent('AndroidHorizontalScrollContentView');
  } else if (Platform.OS === 'ios') {
    nativeOnlyProps = {
      nativeOnly: {
        onMomentumScrollBegin: true,
        onMomentumScrollEnd: true,
        onScrollBeginDrag: true,
        onScrollEndDrag: true
      }
    };
    RCTScrollView = requireNativeComponent('RCTScrollView', ScrollView, nativeOnlyProps);
    RCTScrollContentView = requireNativeComponent('RCTScrollContentView', View);
  } else {
    nativeOnlyProps = {
      nativeOnly: {}
    };
    RCTScrollView = requireNativeComponent('RCTScrollView', null, nativeOnlyProps);
    RCTScrollContentView = requireNativeComponent('RCTScrollContentView', View);
  }

  module.exports = ScrollView;
},228,[200,46,134,32,229,129,132,49,230,238,171,141,173,133,142,176,234,116,18,239,148,19,163],"ScrollView");