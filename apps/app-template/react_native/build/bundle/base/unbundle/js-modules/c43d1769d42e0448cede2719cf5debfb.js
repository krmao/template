__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var PropTypes = _require(_dependencyMap[0], 'prop-types');

  var createStrictShapeTypeChecker = _require(_dependencyMap[1], 'createStrictShapeTypeChecker');

  var PointPropType = createStrictShapeTypeChecker({
    x: PropTypes.number,
    y: PropTypes.number
  });
  module.exports = PointPropType;
},"c43d1769d42e0448cede2719cf5debfb",["18eeaf4e01377a466daaccc6ba8ce6f5","b6f8c243191994e825e2bab87dc439b4"],"PointPropType");