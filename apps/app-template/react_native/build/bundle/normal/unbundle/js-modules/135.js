__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var invariant = _require(_dependencyMap[0], 'fbjs/lib/invariant');

  var merge = _require(_dependencyMap[1], 'merge');

  function createStrictShapeTypeChecker(shapeTypes) {
    function checkType(isRequired, props, propName, componentName, location) {
      if (!props[propName]) {
        if (isRequired) {
          invariant(false, "Required object `" + propName + "` was not specified in " + ("`" + componentName + "`."));
        }

        return;
      }

      var propValue = props[propName];
      var propType = typeof propValue;
      var locationName = location || '(unknown)';

      if (propType !== 'object') {
        invariant(false, "Invalid " + locationName + " `" + propName + "` of type `" + propType + "` " + ("supplied to `" + componentName + "`, expected `object`."));
      }

      var allKeys = merge(props[propName], shapeTypes);

      for (var _len = arguments.length, rest = Array(_len > 5 ? _len - 5 : 0), _key = 5; _key < _len; _key++) {
        rest[_key - 5] = arguments[_key];
      }

      for (var key in allKeys) {
        var checker = shapeTypes[key];

        if (!checker) {
          invariant(false, "Invalid props." + propName + " key `" + key + "` supplied to `" + componentName + "`." + '\nBad object: ' + JSON.stringify(props[propName], null, '  ') + '\nValid keys: ' + JSON.stringify(Object.keys(shapeTypes), null, '  '));
        }

        var error = checker.apply(undefined, [propValue, key, componentName, location].concat(rest));

        if (error) {
          invariant(false, error.message + '\nBad object: ' + JSON.stringify(props[propName], null, '  '));
        }
      }
    }

    function chainedCheckType(props, propName, componentName, location) {
      for (var _len2 = arguments.length, rest = Array(_len2 > 4 ? _len2 - 4 : 0), _key2 = 4; _key2 < _len2; _key2++) {
        rest[_key2 - 4] = arguments[_key2];
      }

      return checkType.apply(undefined, [false, props, propName, componentName, location].concat(rest));
    }

    chainedCheckType.isRequired = checkType.bind(null, true);
    return chainedCheckType;
  }

  module.exports = createStrictShapeTypeChecker;
},135,[18,136],"createStrictShapeTypeChecker");