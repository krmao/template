__d(function (global, _require2, module, exports, _dependencyMap) {
  'use strict';

  var React = _require2(_dependencyMap[0], 'React');

  var EdgeInsetsPropType = _require2(_dependencyMap[1], 'EdgeInsetsPropType');

  var PlatformViewPropTypes = _require2(_dependencyMap[2], 'PlatformViewPropTypes');

  var PropTypes = _require2(_dependencyMap[3], 'prop-types');

  var StyleSheetPropType = _require2(_dependencyMap[4], 'StyleSheetPropType');

  var ViewStylePropTypes = _require2(_dependencyMap[5], 'ViewStylePropTypes');

  var _require = _require2(_dependencyMap[6], 'ViewAccessibility'),
      AccessibilityComponentTypes = _require.AccessibilityComponentTypes,
      AccessibilityTraits = _require.AccessibilityTraits;

  var stylePropType = StyleSheetPropType(ViewStylePropTypes);
  module.exports = babelHelpers.extends({}, PlatformViewPropTypes, {
    accessible: PropTypes.bool,
    accessibilityLabel: PropTypes.node,
    accessibilityActions: PropTypes.arrayOf(PropTypes.string),
    accessibilityComponentType: PropTypes.oneOf(AccessibilityComponentTypes),
    accessibilityLiveRegion: PropTypes.oneOf(['none', 'polite', 'assertive']),
    importantForAccessibility: PropTypes.oneOf(['auto', 'yes', 'no', 'no-hide-descendants']),
    accessibilityTraits: PropTypes.oneOfType([PropTypes.oneOf(AccessibilityTraits), PropTypes.arrayOf(PropTypes.oneOf(AccessibilityTraits))]),
    accessibilityViewIsModal: PropTypes.bool,
    accessibilityElementsHidden: PropTypes.bool,
    onAccessibilityAction: PropTypes.func,
    onAccessibilityTap: PropTypes.func,
    onMagicTap: PropTypes.func,
    testID: PropTypes.string,
    nativeID: PropTypes.string,
    onResponderGrant: PropTypes.func,
    onResponderMove: PropTypes.func,
    onResponderReject: PropTypes.func,
    onResponderRelease: PropTypes.func,
    onResponderTerminate: PropTypes.func,
    onResponderTerminationRequest: PropTypes.func,
    onStartShouldSetResponder: PropTypes.func,
    onStartShouldSetResponderCapture: PropTypes.func,
    onMoveShouldSetResponder: PropTypes.func,
    onMoveShouldSetResponderCapture: PropTypes.func,
    hitSlop: EdgeInsetsPropType,
    onLayout: PropTypes.func,
    pointerEvents: PropTypes.oneOf(['box-none', 'none', 'box-only', 'auto']),
    style: stylePropType,
    removeClippedSubviews: PropTypes.bool,
    renderToHardwareTextureAndroid: PropTypes.bool,
    shouldRasterizeIOS: PropTypes.bool,
    collapsable: PropTypes.bool,
    needsOffscreenAlphaCompositing: PropTypes.bool
  });
},"9ff7e107ed674a99182e71b796d889aa",["e6db4f0efed6b72f641ef0ffed29569f","20099b775ac7bd546d3c34ceb85c88e4","790dec3eba5569b328f412e9e7a6101d","18eeaf4e01377a466daaccc6ba8ce6f5","60dc775dcc40daa6b8d0b23f322ce91f","007a69250301e98c7330c9706693fadc","f436935d9ebc8f545189df019c06c4b6"],"ViewPropTypes");