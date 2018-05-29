__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var invariant = _require(_dependencyMap[0], 'fbjs/lib/invariant');

  var EventHolder = function () {
    function EventHolder() {
      babelHelpers.classCallCheck(this, EventHolder);
      this._heldEvents = {};
      this._currentEventKey = null;
    }

    babelHelpers.createClass(EventHolder, [{
      key: "holdEvent",
      value: function holdEvent(eventType) {
        this._heldEvents[eventType] = this._heldEvents[eventType] || [];
        var eventsOfType = this._heldEvents[eventType];
        var key = {
          eventType: eventType,
          index: eventsOfType.length
        };

        for (var _len = arguments.length, args = Array(_len > 1 ? _len - 1 : 0), _key = 1; _key < _len; _key++) {
          args[_key - 1] = arguments[_key];
        }

        eventsOfType.push(args);
        return key;
      }
    }, {
      key: "emitToListener",
      value: function emitToListener(eventType, listener, context) {
        var _this = this;

        var eventsOfType = this._heldEvents[eventType];

        if (!eventsOfType) {
          return;
        }

        var origEventKey = this._currentEventKey;
        eventsOfType.forEach(function (eventHeld, index) {
          if (!eventHeld) {
            return;
          }

          _this._currentEventKey = {
            eventType: eventType,
            index: index
          };
          listener.apply(context, eventHeld);
        });
        this._currentEventKey = origEventKey;
      }
    }, {
      key: "releaseCurrentEvent",
      value: function releaseCurrentEvent() {
        invariant(this._currentEventKey !== null, 'Not in an emitting cycle; there is no current event');
        this._currentEventKey && this.releaseEvent(this._currentEventKey);
      }
    }, {
      key: "releaseEvent",
      value: function releaseEvent(token) {
        delete this._heldEvents[token.eventType][token.index];
      }
    }, {
      key: "releaseEventType",
      value: function releaseEventType(type) {
        this._heldEvents[type] = [];
      }
    }]);
    return EventHolder;
  }();

  module.exports = EventHolder;
},"b02be26a2f90bff94ecc1f64626d97ad",["8940a4ad43b101ffc23e725363c70f8d"],"EventHolder");