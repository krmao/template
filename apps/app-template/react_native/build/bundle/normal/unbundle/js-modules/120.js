__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var InspectorAgent = function () {
    function InspectorAgent(eventSender) {
      babelHelpers.classCallCheck(this, InspectorAgent);
      this._eventSender = eventSender;
    }

    babelHelpers.createClass(InspectorAgent, [{
      key: "sendEvent",
      value: function sendEvent(name, params) {
        this._eventSender(name, params);
      }
    }]);
    return InspectorAgent;
  }();

  module.exports = InspectorAgent;
},120,[],"InspectorAgent");