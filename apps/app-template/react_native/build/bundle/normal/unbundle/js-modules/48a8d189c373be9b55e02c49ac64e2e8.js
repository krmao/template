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
},"48a8d189c373be9b55e02c49ac64e2e8",["0d090424fe83a777e288ec81a9063c81","45e1de962d0658d8cb6946a3c4531cbf","007a69250301e98c7330c9706693fadc","61a2e2589ea976cf04a51cc242a466dd","1b69977972a3b6ad650756d07de7954c","78f1439346fef507ba07b7154bf4073c","6c409739fa041eb532d3fa5aba97376a"],"ReactNativeStyleAttributes");