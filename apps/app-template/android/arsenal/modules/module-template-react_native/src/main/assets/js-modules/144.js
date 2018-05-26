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
},144,[46,129],"ShadowPropTypesIOS");