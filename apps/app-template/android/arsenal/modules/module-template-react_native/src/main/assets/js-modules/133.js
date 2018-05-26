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
},133,[132,134,139,129,141,142,147],"ViewPropTypes");