__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  var POP = 'Navigation/POP';
  var POP_TO_TOP = 'Navigation/POP_TO_TOP';
  var PUSH = 'Navigation/PUSH';
  var RESET = 'Navigation/RESET';
  var REPLACE = 'Navigation/REPLACE';
  var COMPLETE_TRANSITION = 'Navigation/COMPLETE_TRANSITION';

  var pop = function pop(payload) {
    return babelHelpers.extends({
      type: POP
    }, payload);
  };

  var popToTop = function popToTop(payload) {
    return babelHelpers.extends({
      type: POP_TO_TOP
    }, payload);
  };

  var push = function push(payload) {
    return babelHelpers.extends({
      type: PUSH
    }, payload);
  };

  var reset = function reset(payload) {
    return babelHelpers.extends({
      type: RESET
    }, payload);
  };

  var replace = function replace(payload) {
    return babelHelpers.extends({
      type: REPLACE
    }, payload);
  };

  var completeTransition = function completeTransition(payload) {
    return babelHelpers.extends({
      type: COMPLETE_TRANSITION
    }, payload);
  };

  exports.default = {
    POP: POP,
    POP_TO_TOP: POP_TO_TOP,
    PUSH: PUSH,
    RESET: RESET,
    REPLACE: REPLACE,
    COMPLETE_TRANSITION: COMPLETE_TRANSITION,
    pop: pop,
    popToTop: popToTop,
    push: push,
    reset: reset,
    replace: replace,
    completeTransition: completeTransition
  };
},369,[],"node_modules/react-navigation/src/routers/StackActions.js");