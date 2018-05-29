__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var ColorPropType = _require(_dependencyMap[0], 'ColorPropType');

  var ReactPropTypes = _require(_dependencyMap[1], 'prop-types');

  var ViewStylePropTypes = _require(_dependencyMap[2], 'ViewStylePropTypes');

  var TextStylePropTypes = babelHelpers.extends({}, ViewStylePropTypes, {
    color: ColorPropType,
    fontFamily: ReactPropTypes.string,
    fontSize: ReactPropTypes.number,
    fontStyle: ReactPropTypes.oneOf(['normal', 'italic']),
    fontWeight: ReactPropTypes.oneOf(['normal', 'bold', '100', '200', '300', '400', '500', '600', '700', '800', '900']),
    fontVariant: ReactPropTypes.arrayOf(ReactPropTypes.oneOf(['small-caps', 'oldstyle-nums', 'lining-nums', 'tabular-nums', 'proportional-nums'])),
    textShadowOffset: ReactPropTypes.shape({
      width: ReactPropTypes.number,
      height: ReactPropTypes.number
    }),
    textShadowRadius: ReactPropTypes.number,
    textShadowColor: ColorPropType,
    letterSpacing: ReactPropTypes.number,
    lineHeight: ReactPropTypes.number,
    textAlign: ReactPropTypes.oneOf(['auto', 'left', 'right', 'center', 'justify']),
    textAlignVertical: ReactPropTypes.oneOf(['auto', 'top', 'bottom', 'center']),
    includeFontPadding: ReactPropTypes.bool,
    textDecorationLine: ReactPropTypes.oneOf(['none', 'underline', 'line-through', 'underline line-through']),
    textDecorationStyle: ReactPropTypes.oneOf(['solid', 'double', 'dotted', 'dashed']),
    textDecorationColor: ColorPropType,
    writingDirection: ReactPropTypes.oneOf(['auto', 'ltr', 'rtl'])
  });
  module.exports = TextStylePropTypes;
},"45e1de962d0658d8cb6946a3c4531cbf",["63c61c7eda525c10d0670d2ef8475012","18eeaf4e01377a466daaccc6ba8ce6f5","007a69250301e98c7330c9706693fadc"],"TextStylePropTypes");