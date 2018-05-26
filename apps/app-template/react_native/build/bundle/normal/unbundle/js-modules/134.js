__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var PropTypes = _require(_dependencyMap[0], 'prop-types');

  var createStrictShapeTypeChecker = _require(_dependencyMap[1], 'createStrictShapeTypeChecker');

  var EdgeInsetsPropType = createStrictShapeTypeChecker({
    top: PropTypes.number,
    left: PropTypes.number,
    bottom: PropTypes.number,
    right: PropTypes.number
  });
  module.exports = EdgeInsetsPropType;
},134,[129,135],"EdgeInsetsPropType");