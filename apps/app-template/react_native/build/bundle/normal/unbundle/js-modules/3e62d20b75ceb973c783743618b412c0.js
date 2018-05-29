__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  if (process.env.NODE_ENV !== 'production') {
    var invariant = _require(_dependencyMap[0], 'fbjs/lib/invariant');

    var warning = _require(_dependencyMap[1], 'fbjs/lib/warning');

    var ReactPropTypesSecret = _require(_dependencyMap[2], './lib/ReactPropTypesSecret');

    var loggedTypeFailures = {};
  }

  function checkPropTypes(typeSpecs, values, location, componentName, getStack) {
    if (process.env.NODE_ENV !== 'production') {
      for (var typeSpecName in typeSpecs) {
        if (typeSpecs.hasOwnProperty(typeSpecName)) {
          var error;

          try {
            invariant(typeof typeSpecs[typeSpecName] === 'function', '%s: %s type `%s` is invalid; it must be a function, usually from ' + 'the `prop-types` package, but received `%s`.', componentName || 'React class', location, typeSpecName, typeof typeSpecs[typeSpecName]);
            error = typeSpecs[typeSpecName](values, typeSpecName, componentName, location, null, ReactPropTypesSecret);
          } catch (ex) {
            error = ex;
          }

          warning(!error || error instanceof Error, '%s: type specification of %s `%s` is invalid; the type checker ' + 'function must return `null` or an `Error` but returned a %s. ' + 'You may have forgotten to pass an argument to the type checker ' + 'creator (arrayOf, instanceOf, objectOf, oneOf, oneOfType, and ' + 'shape all require an argument).', componentName || 'React class', location, typeSpecName, typeof error);

          if (error instanceof Error && !(error.message in loggedTypeFailures)) {
            loggedTypeFailures[error.message] = true;
            var stack = getStack ? getStack() : '';
            warning(false, 'Failed %s type: %s%s', location, error.message, stack != null ? stack : '');
          }
        }
      }
    }
  }

  module.exports = checkPropTypes;
},"3e62d20b75ceb973c783743618b412c0",["8940a4ad43b101ffc23e725363c70f8d","09babf511a081d9520406a63f452d2ef","5622ec09fe8f3b606d0426f1d9c8d253"],"node_modules/prop-types/checkPropTypes.js");