__d(function (global, _require, module, exports, _dependencyMap) {
  "use strict";

  function makeEmptyFunction(arg) {
    return function () {
      return arg;
    };
  }

  var emptyFunction = function emptyFunction() {};

  emptyFunction.thatReturns = makeEmptyFunction;
  emptyFunction.thatReturnsFalse = makeEmptyFunction(false);
  emptyFunction.thatReturnsTrue = makeEmptyFunction(true);
  emptyFunction.thatReturnsNull = makeEmptyFunction(null);

  emptyFunction.thatReturnsThis = function () {
    return this;
  };

  emptyFunction.thatReturnsArgument = function (arg) {
    return arg;
  };

  module.exports = emptyFunction;
},"7be5aa3f60ced36f3bf5972d0a12f299",[],"node_modules/fbjs/lib/emptyFunction.js");