__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var PropTypes = _require(_dependencyMap[0], 'prop-types');

  var TVViewPropTypes = {
    isTVSelectable: PropTypes.bool,
    hasTVPreferredFocus: PropTypes.bool,
    tvParallaxProperties: PropTypes.object,
    tvParallaxShiftDistanceX: PropTypes.number,
    tvParallaxShiftDistanceY: PropTypes.number,
    tvParallaxTiltAngle: PropTypes.number,
    tvParallaxMagnification: PropTypes.number
  };
  module.exports = TVViewPropTypes;
},140,[129],"TVViewPropTypes");