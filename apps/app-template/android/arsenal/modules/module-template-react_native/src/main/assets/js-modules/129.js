__d(function (global, _require, module, exports, _dependencyMap) {
  if (process.env.NODE_ENV !== 'production') {
    var REACT_ELEMENT_TYPE = typeof Symbol === 'function' && (typeof Symbol === "function" ? Symbol.for : "@@for") && (typeof Symbol === "function" ? Symbol.for : "@@for")('react.element') || 0xeac7;

    var isValidElement = function isValidElement(object) {
      return typeof object === 'object' && object !== null && object.$$typeof === REACT_ELEMENT_TYPE;
    };

    var throwOnDirectAccess = true;
    module.exports = _require(_dependencyMap[0], './factoryWithTypeCheckers')(isValidElement, throwOnDirectAccess);
  } else {
    module.exports = _require(_dependencyMap[1], './factoryWithThrowingShims')();
  }
},129,[130,131],"node_modules/prop-types/index.js");