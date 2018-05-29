__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var JSInspector = {
    registerAgent: function registerAgent(type) {
      if (global.__registerInspectorAgent) {
        global.__registerInspectorAgent(type);
      }
    },
    getTimestamp: function getTimestamp() {
      return global.__inspectorTimestamp();
    }
  };
  module.exports = JSInspector;
},"2bf7838d360efae9f2a55682481f7cb7",[],"JSInspector");