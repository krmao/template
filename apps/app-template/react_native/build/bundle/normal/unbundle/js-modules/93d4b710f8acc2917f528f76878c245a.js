__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var UIManager = _require(_dependencyMap[0], 'UIManager');

  function deprecatedPropType(propType, explanation) {
    return function validate(props, propName, componentName) {
      if (!UIManager[componentName] && props[propName] !== undefined) {
        console.warn("`" + propName + "` supplied to `" + componentName + "` has been deprecated. " + explanation);
      }

      for (var _len = arguments.length, rest = Array(_len > 3 ? _len - 3 : 0), _key = 3; _key < _len; _key++) {
        rest[_key - 3] = arguments[_key];
      }

      return propType.apply(undefined, [props, propName, componentName].concat(rest));
    };
  }

  module.exports = deprecatedPropType;
},"93d4b710f8acc2917f528f76878c245a",["467cd3365342d9aaa2e941fe7ace641c"],"deprecatedPropType");