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
},131,[16,18,21],"node_modules/prop-types/factoryWithThrowingShims.js");