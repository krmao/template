__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var ColorPropType = _require(_dependencyMap[0], 'ColorPropType');

  var LayoutPropTypes = _require(_dependencyMap[1], 'LayoutPropTypes');

  var ReactPropTypes = _require(_dependencyMap[2], 'prop-types');

  var ShadowPropTypesIOS = _require(_dependencyMap[3], 'ShadowPropTypesIOS');

  var TransformPropTypes = _require(_dependencyMap[4], 'TransformPropTypes');

  var ViewStylePropTypes = babelHelpers.extends({}, LayoutPropTypes, ShadowPropTypesIOS, TransformPropTypes, {
    backfaceVisibility: ReactPropTypes.oneOf(['visible', 'hidden']),
    backgroundColor: ColorPropType,
    borderColor: ColorPropType,
    borderTopColor: ColorPropType,
    borderRightColor: ColorPropType,
    borderBottomColor: ColorPropType,
    borderLeftColor: ColorPropType,
    borderStartColor: ColorPropType,
    borderEndColor: ColorPropType,
    borderRadius: ReactPropTypes.number,
    borderTopLeftRadius: ReactPropTypes.number,
    borderTopRightRadius: ReactPropTypes.number,
    borderTopStartRadius: ReactPropTypes.number,
    borderTopEndRadius: ReactPropTypes.number,
    borderBottomLeftRadius: ReactPropTypes.number,
    borderBottomRightRadius: ReactPropTypes.number,
    borderBottomStartRadius: ReactPropTypes.number,
    borderBottomEndRadius: ReactPropTypes.number,
    borderStyle: ReactPropTypes.oneOf(['solid', 'dotted', 'dashed']),
    borderWidth: ReactPropTypes.number,
    borderTopWidth: ReactPropTypes.number,
    borderRightWidth: ReactPropTypes.number,
    borderBottomWidth: ReactPropTypes.number,
    borderLeftWidth: ReactPropTypes.number,
    opacity: ReactPropTypes.number,
    elevation: ReactPropTypes.number
  });
  module.exports = ViewStylePropTypes;
},142,[46,143,129,144,145],"ViewStylePropTypes");