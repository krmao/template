__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });

  exports.default = function (obj, key, defaultValue) {
    if (obj.hasOwnProperty(key)) {
      return obj;
    }

    obj[key] = defaultValue;
    return obj;
  };
},394,[],"node_modules/react-navigation/src/utils/withDefaultValue.js");