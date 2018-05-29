__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var createStrictShapeTypeChecker = _require(_dependencyMap[0], 'createStrictShapeTypeChecker');

  var flattenStyle = _require(_dependencyMap[1], 'flattenStyle');

  function StyleSheetPropType(shape) {
    var shapePropType = createStrictShapeTypeChecker(shape);
    return function (props, propName, componentName, location) {
      var newProps = props;

      if (props[propName]) {
        newProps = {};
        newProps[propName] = flattenStyle(props[propName]);
      }

      for (var _len = arguments.length, rest = Array(_len > 4 ? _len - 4 : 0), _key = 4; _key < _len; _key++) {
        rest[_key - 4] = arguments[_key];
      }

      return shapePropType.apply(undefined, [newProps, propName, componentName, location].concat(rest));
    };
  }

  module.exports = StyleSheetPropType;
},"60dc775dcc40daa6b8d0b23f322ce91f",["b6f8c243191994e825e2bab87dc439b4","869f0bd4eed428d95df80a8c03d71093"],"StyleSheetPropType");