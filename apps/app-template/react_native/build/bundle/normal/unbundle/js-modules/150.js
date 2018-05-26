__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var ImageStylePropTypes = _require(_dependencyMap[0], 'ImageStylePropTypes');

  var TextStylePropTypes = _require(_dependencyMap[1], 'TextStylePropTypes');

  var ViewStylePropTypes = _require(_dependencyMap[2], 'ViewStylePropTypes');

  var keyMirror = _require(_dependencyMap[3], 'fbjs/lib/keyMirror');

  var processColor = _require(_dependencyMap[4], 'processColor');

  var processTransform = _require(_dependencyMap[5], 'processTransform');

  var sizesDiffer = _require(_dependencyMap[6], 'sizesDiffer');

  var ReactNativeStyleAttributes = babelHelpers.extends({}, keyMirror(ViewStylePropTypes), keyMirror(TextStylePropTypes), keyMirror(ImageStylePropTypes));
  ReactNativeStyleAttributes.transform = {
    process: processTransform
  };
  ReactNativeStyleAttributes.shadowOffset = {
    diff: sizesDiffer
  };
  var colorAttributes = {
    process: processColor
  };
  ReactNativeStyleAttributes.backgroundColor = colorAttributes;
  ReactNativeStyleAttributes.borderBottomColor = colorAttributes;
  ReactNativeStyleAttributes.borderColor = colorAttributes;
  ReactNativeStyleAttributes.borderLeftColor = colorAttributes;
  ReactNativeStyleAttributes.borderRightColor = colorAttributes;
  ReactNativeStyleAttributes.borderTopColor = colorAttributes;
  ReactNativeStyleAttributes.borderStartColor = colorAttributes;
  ReactNativeStyleAttributes.borderEndColor = colorAttributes;
  ReactNativeStyleAttributes.color = colorAttributes;
  ReactNativeStyleAttributes.shadowColor = colorAttributes;
  ReactNativeStyleAttributes.textDecorationColor = colorAttributes;
  ReactNativeStyleAttributes.tintColor = colorAttributes;
  ReactNativeStyleAttributes.textShadowColor = colorAttributes;
  ReactNativeStyleAttributes.overlayColor = colorAttributes;
  module.exports = ReactNativeStyleAttributes;
},150,[151,154,142,153,155,156,158],"ReactNativeStyleAttributes");