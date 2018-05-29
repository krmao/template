__d(function (global, _require, module, exports, _dependencyMap) {
    "use strict";

    var Commons = _require(_dependencyMap[0], "./commons");

    var LISTENERS = Commons.LISTENERS;
    var ATTRIBUTE = Commons.ATTRIBUTE;
    var newNode = Commons.newNode;

    function getAttributeListener(eventTarget, type) {
        var node = eventTarget[LISTENERS][type];

        while (node != null) {
            if (node.kind === ATTRIBUTE) {
                return node.listener;
            }

            node = node.next;
        }

        return null;
    }

    function setAttributeListener(eventTarget, type, listener) {
        if (typeof listener !== "function" && typeof listener !== "object") {
            listener = null;
        }

        var prev = null;
        var node = eventTarget[LISTENERS][type];

        while (node != null) {
            if (node.kind === ATTRIBUTE) {
                if (prev == null) {
                    eventTarget[LISTENERS][type] = node.next;
                } else {
                    prev.next = node.next;
                }
            } else {
                prev = node;
            }

            node = node.next;
        }

        if (listener != null) {
            if (prev == null) {
                eventTarget[LISTENERS][type] = newNode(listener, ATTRIBUTE);
            } else {
                prev.next = newNode(listener, ATTRIBUTE);
            }
        }
    }

    exports.defineCustomEventTarget = function (EventTargetBase, types) {
        function EventTarget() {
            EventTargetBase.call(this);
        }

        var descripter = {
            constructor: {
                value: EventTarget,
                configurable: true,
                writable: true
            }
        };
        types.forEach(function (type) {
            descripter["on" + type] = {
                get: function get() {
                    return getAttributeListener(this, type);
                },
                set: function set(listener) {
                    setAttributeListener(this, type, listener);
                },
                configurable: true,
                enumerable: true
            };
        });
        EventTarget.prototype = Object.create(EventTargetBase.prototype, descripter);
        return EventTarget;
    };
},"3ec9ff9c4da15502227db5a451c8d3b7",["6d77002344308dbe7a483da6d3a6a4c0"],"node_modules/event-target-shim/lib/custom-event-target.js");