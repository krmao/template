__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var ColorPropType = _require(_dependencyMap[0], 'ColorPropType');

  var ReactPropTypes = _require(_dependencyMap[1], 'prop-types');

  var ShadowPropTypesIOS = {
    shadowColor: ColorPropType,
    shadowOffset: ReactPropTypes.shape({
      width: ReactPropTypes.number,
      height: ReactPropTypes.number
    }),
    shadowOpacity: ReactPropTypes.number,
    shadowRadius: ReactPropTypes.number
  };
  module.exports = ShadowPropTypesIOS;
},"43ea09fa0639e14101d7b13aa049f25d",["63c61c7eda525c10d0670d2ef8475012","18eeaf4e01377a466daaccc6ba8ce6f5"],"ShadowPropTypesIOS");