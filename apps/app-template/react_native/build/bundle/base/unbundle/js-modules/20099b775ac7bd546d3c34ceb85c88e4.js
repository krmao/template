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
},"20099b775ac7bd546d3c34ceb85c88e4",["18eeaf4e01377a466daaccc6ba8ce6f5","b6f8c243191994e825e2bab87dc439b4"],"EdgeInsetsPropType");