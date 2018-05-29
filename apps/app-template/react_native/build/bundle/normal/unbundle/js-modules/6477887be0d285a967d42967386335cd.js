__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var ReactNativeStyleAttributes = _require(_dependencyMap[0], 'ReactNativeStyleAttributes');

  var ReactNativeViewAttributes = {};
  ReactNativeViewAttributes.UIView = {
    pointerEvents: true,
    accessible: true,
    accessibilityActions: true,
    accessibilityLabel: true,
    accessibilityComponentType: true,
    accessibilityLiveRegion: true,
    accessibilityTraits: true,
    importantForAccessibility: true,
    nativeID: true,
    testID: true,
    renderToHardwareTextureAndroid: true,
    shouldRasterizeIOS: true,
    onLayout: true,
    onAccessibilityAction: true,
    onAccessibilityTap: true,
    onMagicTap: true,
    collapsable: true,
    needsOffscreenAlphaCompositing: true,
    style: ReactNativeStyleAttributes
  };
  ReactNativeViewAttributes.RCTView = babelHelpers.extends({}, ReactNativeViewAttributes.UIView, {
    removeClippedSubviews: true
  });
  module.exports = ReactNativeViewAttributes;
},"6477887be0d285a967d42967386335cd",["48a8d189c373be9b55e02c49ac64e2e8"],"ReactNativeViewAttributes");