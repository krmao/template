__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var emptyFunction = _require(_dependencyMap[0], 'fbjs/lib/emptyFunction');

  var invariant = _require(_dependencyMap[1], 'fbjs/lib/invariant');

  var ReactPropTypesSecret = _require(_dependencyMap[2], './lib/ReactPropTypesSecret');

  module.exports = function () {
    function shim(props, propName, componentName, location, propFullName, secret) {
      if (secret === ReactPropTypesSecret) {
        return;
      }

      invariant(false, 'Calling PropTypes validators directly is not supported by the `prop-types` package. ' + 'Use PropTypes.checkPropTypes() to call them. ' + 'Read more at http://fb.me/use-check-prop-types');
    }

    ;
    shim.isRequired = shim;

    function getShim() {
      return shim;
    }

    ;
    var ReactPropTypes = {
      array: shim,
      bool: shim,
      func: shim,
      number: shim,
      object: shim,
      string: shim,
      symbol: shim,
      any: shim,
      arrayOf: getShim,
      element: shim,
      instanceOf: getShim,
      node: shim,
      objectOf: getShim,
      oneOf: getShim,
      oneOfType: getShim,
      shape: getShim,
      exact: getShim
    };
    ReactPropTypes.checkPropTypes = emptyFunction;
    ReactPropTypes.PropTypes = ReactPropTypes;
    return ReactPropTypes;
  };
},"83fd0f3694f5e1c2f77d83470a455f09",["7be5aa3f60ced36f3bf5972d0a12f299","8940a4ad43b101ffc23e725363c70f8d","5622ec09fe8f3b606d0426f1d9c8d253"],"node_modules/prop-types/factoryWithThrowingShims.js");