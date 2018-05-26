__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var PropTypes = _require(_dependencyMap[0], 'prop-types');

  var createStrictShapeTypeChecker = _require(_dependencyMap[1], 'createStrictShapeTypeChecker');

  var PointPropType = createStrictShapeTypeChecker({
    x: PropTypes.number,
    y: PropTypes.number
  });
  module.exports = PointPropType;
},229,[129,135],"PointPropType");