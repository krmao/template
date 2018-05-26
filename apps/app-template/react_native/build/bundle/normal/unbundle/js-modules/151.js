__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var ColorPropType = _require(_dependencyMap[0], 'ColorPropType');

  var ImageResizeMode = _require(_dependencyMap[1], 'ImageResizeMode');

  var LayoutPropTypes = _require(_dependencyMap[2], 'LayoutPropTypes');

  var ReactPropTypes = _require(_dependencyMap[3], 'prop-types');

  var ShadowPropTypesIOS = _require(_dependencyMap[4], 'ShadowPropTypesIOS');

  var TransformPropTypes = _require(_dependencyMap[5], 'TransformPropTypes');

  var ImageStylePropTypes = babelHelpers.extends({}, LayoutPropTypes, ShadowPropTypesIOS, TransformPropTypes, {
    resizeMode: ReactPropTypes.oneOf(Object.keys(ImageResizeMode)),
    backfaceVisibility: ReactPropTypes.oneOf(['visible', 'hidden']),
    backgroundColor: ColorPropType,
    borderColor: ColorPropType,
    borderWidth: ReactPropTypes.number,
    borderRadius: ReactPropTypes.number,
    overflow: ReactPropTypes.oneOf(['visible', 'hidden']),
    tintColor: ColorPropType,
    opacity: ReactPropTypes.number,
    overlayColor: ReactPropTypes.string,
    borderTopLeftRadius: ReactPropTypes.number,
    borderTopRightRadius: ReactPropTypes.number,
    borderBottomLeftRadius: ReactPropTypes.number,
    borderBottomRightRadius: ReactPropTypes.number
  });
  module.exports = ImageStylePropTypes;
},151,[46,152,143,129,144,145],"ImageStylePropTypes");