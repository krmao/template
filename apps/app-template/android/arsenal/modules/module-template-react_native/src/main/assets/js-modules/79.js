__d(function (global, _require, module, exports, _dependencyMap) {
    "use strict";

    var createUniqueKey = _require(_dependencyMap[0], "./commons").createUniqueKey;

    var STOP_IMMEDIATE_PROPAGATION_FLAG = createUniqueKey("stop_immediate_propagation_flag");
    var CANCELED_FLAG = createUniqueKey("canceled_flag");
    var ORIGINAL_EVENT = createUniqueKey("original_event");
    var wrapperPrototypeDefinition = Object.freeze({
        stopPropagation: Object.freeze({
            value: function stopPropagation() {
                var e = this[ORIGINAL_EVENT];

                if (typeof e.stopPropagation === "function") {
                    e.stopPropagation();
                }
            },
            writable: true,
            configurable: true
        }),
        stopImmediatePropagation: Object.freeze({
            value: function stopImmediatePropagation() {
                this[STOP_IMMEDIATE_PROPAGATION_FLAG] = true;
                var e = this[ORIGINAL_EVENT];

                if (typeof e.stopImmediatePropagation === "function") {
                    e.stopImmediatePropagation();
                }
            },
            writable: true,
            configurable: true
        }),
        preventDefault: Object.freeze({
            value: function preventDefault() {
                if (this.cancelable === true) {
                    this[CANCELED_FLAG] = true;
                }

                var e = this[ORIGINAL_EVENT];

                if (typeof e.preventDefault === "function") {
                    e.preventDefault();
                }
            },
            writable: true,
            configurable: true
        }),
        defaultPrevented: Object.freeze({
            get: function defaultPrevented() {
                return this[CANCELED_FLAG];
            },
            enumerable: true,
            configurable: true
        })
    });
    exports.STOP_IMMEDIATE_PROPAGATION_FLAG = STOP_IMMEDIATE_PROPAGATION_FLAG;

    exports.createEventWrapper = function createEventWrapper(event, eventTarget) {
        var timeStamp = typeof event.timeStamp === "number" ? event.timeStamp : Date.now();
        var propertyDefinition = {
            type: {
                value: event.type,
                enumerable: true
            },
            target: {
                value: eventTarget,
                enumerable: true
            },
            currentTarget: {
                value: eventTarget,
                enumerable: true
            },
            eventPhase: {
                value: 2,
                enumerable: true
            },
            bubbles: {
                value: Boolean(event.bubbles),
                enumerable: true
            },
            cancelable: {
                value: Boolean(event.cancelable),
                enumerable: true
            },
            timeStamp: {
                value: timeStamp,
                enumerable: true
            },
            isTrusted: {
                value: false,
                enumerable: true
            }
        };
        propertyDefinition[STOP_IMMEDIATE_PROPAGATION_FLAG] = {
            value: false,
            writable: true
        };
        propertyDefinition[CANCELED_FLAG] = {
            value: false,
            writable: true
        };
        propertyDefinition[ORIGINAL_EVENT] = {
            value: event
        };

        if (typeof event.detail !== "undefined") {
            propertyDefinition.detail = {
                value: event.detail,
                enumerable: true
            };
        }

        return Object.create(Object.create(event, wrapperPrototypeDefinition), propertyDefinition);
    };
},79,[77],"node_modules/event-target-shim/lib/event-wrapper.js");