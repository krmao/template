__d(function (global, _require, module, exports, _dependencyMap) {
  "use strict";

  var createUniqueKey = exports.createUniqueKey = typeof Symbol !== "undefined" ? Symbol : function createUniqueKey(name) {
    return "[[" + name + "_" + Math.random().toFixed(8).slice(2) + "]]";
  };
  exports.LISTENERS = createUniqueKey("listeners");
  exports.CAPTURE = 1;
  exports.BUBBLE = 2;
  exports.ATTRIBUTE = 3;

  exports.newNode = function newNode(listener, kind) {
    return {
      listener: listener,
      kind: kind,
      next: null
    };
  };
},"6d77002344308dbe7a483da6d3a6a4c0",[],"node_modules/event-target-shim/lib/commons.js");