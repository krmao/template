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
},"0d090424fe83a777e288ec81a9063c81",["63c61c7eda525c10d0670d2ef8475012","b33f31a24ba813e4eb6eda74806436a0","8ea8f5ebe2044017e275a1899793cb51","18eeaf4e01377a466daaccc6ba8ce6f5","43ea09fa0639e14101d7b13aa049f25d","b5616b617f746142e26b40e74fea4035"],"ImageStylePropTypes");