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
},"54816097bcd607377ccff2567a7d892a",["18eeaf4e01377a466daaccc6ba8ce6f5"],"TVViewPropTypes");