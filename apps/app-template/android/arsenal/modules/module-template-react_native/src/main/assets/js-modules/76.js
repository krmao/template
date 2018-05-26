__d(function (global, _require, module, exports, _dependencyMap) {
    "use strict";

    var Commons = _require(_dependencyMap[0], "./commons");

    var CustomEventTarget = _require(_dependencyMap[1], "./custom-event-target");

    var EventWrapper = _require(_dependencyMap[2], "./event-wrapper");

    var LISTENERS = Commons.LISTENERS;
    var CAPTURE = Commons.CAPTURE;
    var BUBBLE = Commons.BUBBLE;
    var ATTRIBUTE = Commons.ATTRIBUTE;
    var newNode = Commons.newNode;
    var defineCustomEventTarget = CustomEventTarget.defineCustomEventTarget;
    var createEventWrapper = EventWrapper.createEventWrapper;
    var STOP_IMMEDIATE_PROPAGATION_FLAG = EventWrapper.STOP_IMMEDIATE_PROPAGATION_FLAG;
    var HAS_EVENTTARGET_INTERFACE = typeof window !== "undefined" && typeof window.EventTarget !== "undefined";

    var EventTarget = module.exports = function EventTarget() {
        if (this instanceof EventTarget) {
            Object.defineProperty(this, LISTENERS, {
                value: Object.create(null)
            });
        } else if (arguments.length === 1 && Array.isArray(arguments[0])) {
            return defineCustomEventTarget(EventTarget, arguments[0]);
        } else if (arguments.length > 0) {
            var types = Array(arguments.length);

            for (var i = 0; i < arguments.length; ++i) {
                types[i] = arguments[i];
            }

            return defineCustomEventTarget(EventTarget, types);
        } else {
            throw new TypeError("Cannot call a class as a function");
        }
    };

    EventTarget.prototype = Object.create((HAS_EVENTTARGET_INTERFACE ? window.EventTarget : Object).prototype, {
        constructor: {
            value: EventTarget,
            writable: true,
            configurable: true
        },
        addEventListener: {
            value: function addEventListener(type, listener, capture) {
                if (listener == null) {
                    return false;
                }

                if (typeof listener !== "function" && typeof listener !== "object") {
                    throw new TypeError("\"listener\" is not an object.");
                }

                var kind = capture ? CAPTURE : BUBBLE;
                var node = this[LISTENERS][type];

                if (node == null) {
                    this[LISTENERS][type] = newNode(listener, kind);
                    return true;
                }

                var prev = null;

                while (node != null) {
                    if (node.listener === listener && node.kind === kind) {
                        return false;
                    }

                    prev = node;
                    node = node.next;
                }

                prev.next = newNode(listener, kind);
                return true;
            },
            configurable: true,
            writable: true
        },
        removeEventListener: {
            value: function removeEventListener(type, listener, capture) {
                if (listener == null) {
                    return false;
                }

                var kind = capture ? CAPTURE : BUBBLE;
                var prev = null;
                var node = this[LISTENERS][type];

                while (node != null) {
                    if (node.listener === listener && node.kind === kind) {
                        if (prev == null) {
                            this[LISTENERS][type] = node.next;
                        } else {
                            prev.next = node.next;
                        }

                        return true;
                    }

                    prev = node;
                    node = node.next;
                }

                return false;
            },
            configurable: true,
            writable: true
        },
        dispatchEvent: {
            value: function dispatchEvent(event) {
                var node = this[LISTENERS][event.type];

                if (node == null) {
                    return true;
                }

                var wrapped = createEventWrapper(event, this);

                while (node != null) {
                    if (typeof node.listener === "function") {
                        node.listener.call(this, wrapped);
                    } else if (node.kind !== ATTRIBUTE && typeof node.listener.handleEvent === "function") {
                        node.listener.handleEvent(wrapped);
                    }

                    if (wrapped[STOP_IMMEDIATE_PROPAGATION_FLAG]) {
                        break;
                    }

                    node = node.next;
                }

                return !wrapped.defaultPrevented;
            },
            configurable: true,
            writable: true
        }
    });
},76,[77,78,79],"node_modules/event-target-shim/lib/event-target.js");