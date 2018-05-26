__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var WebSocketEvent = function WebSocketEvent(type, eventInitDict) {
    babelHelpers.classCallCheck(this, WebSocketEvent);
    this.type = type.toString();
    babelHelpers.extends(this, eventInitDict);
  };

  module.exports = WebSocketEvent;
},91,[],"WebSocketEvent");