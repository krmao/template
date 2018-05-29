__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.default = getChildEventSubscriber;

  function getChildEventSubscriber(addListener, key) {
    var actionSubscribers = new Set();
    var willFocusSubscribers = new Set();
    var didFocusSubscribers = new Set();
    var willBlurSubscribers = new Set();
    var didBlurSubscribers = new Set();

    var removeAll = function removeAll() {
      [actionSubscribers, willFocusSubscribers, didFocusSubscribers, willBlurSubscribers, didBlurSubscribers].forEach(function (set) {
        return set.clear();
      });
      upstreamSubscribers.forEach(function (subs) {
        return subs && subs.remove();
      });
    };

    var getChildSubscribers = function getChildSubscribers(evtName) {
      switch (evtName) {
        case 'action':
          return actionSubscribers;

        case 'willFocus':
          return willFocusSubscribers;

        case 'didFocus':
          return didFocusSubscribers;

        case 'willBlur':
          return willBlurSubscribers;

        case 'didBlur':
          return didBlurSubscribers;

        default:
          return null;
      }
    };

    var emit = function emit(type, payload) {
      var payloadWithType = babelHelpers.extends({}, payload, {
        type: type
      });
      var subscribers = getChildSubscribers(type);
      subscribers && subscribers.forEach(function (subs) {
        subs(payloadWithType);
      });
    };

    var lastEmittedEvent = 'didBlur';
    var upstreamEvents = ['willFocus', 'didFocus', 'willBlur', 'didBlur', 'action'];
    var upstreamSubscribers = upstreamEvents.map(function (eventName) {
      return addListener(eventName, function (payload) {
        var state = payload.state,
            lastState = payload.lastState,
            action = payload.action;
        var lastRoutes = lastState && lastState.routes;
        var routes = state && state.routes;
        var lastFocusKey = lastState && lastState.routes && lastState.routes[lastState.index].key;
        var focusKey = routes && routes[state.index].key;
        var isChildFocused = focusKey === key;
        var lastRoute = lastRoutes && lastRoutes.find(function (route) {
          return route.key === key;
        });
        var newRoute = routes && routes.find(function (route) {
          return route.key === key;
        });
        var childPayload = {
          context: key + ":" + action.type + "_" + (payload.context || 'Root'),
          state: newRoute,
          lastState: lastRoute,
          action: action,
          type: eventName
        };
        var isTransitioning = !!state && state.isTransitioning;
        var previouslyLastEmittedEvent = lastEmittedEvent;

        if (lastEmittedEvent === 'didBlur') {
          if (eventName === 'willFocus' && isChildFocused) {
            emit(lastEmittedEvent = 'willFocus', childPayload);
          } else if (eventName === 'action' && isChildFocused) {
            emit(lastEmittedEvent = 'willFocus', childPayload);
          }
        }

        if (lastEmittedEvent === 'willFocus') {
          if (eventName === 'didFocus' && isChildFocused && !isTransitioning) {
            emit(lastEmittedEvent = 'didFocus', childPayload);
          } else if (eventName === 'action' && isChildFocused && !isTransitioning) {
            emit(lastEmittedEvent = 'didFocus', childPayload);
          }
        }

        if (lastEmittedEvent === 'didFocus') {
          if (!isChildFocused) {
            emit(lastEmittedEvent = 'willBlur', childPayload);
          } else if (eventName === 'willBlur') {
            emit(lastEmittedEvent = 'willBlur', childPayload);
          } else if (eventName === 'action' && previouslyLastEmittedEvent === 'didFocus') {
            emit('action', childPayload);
          }
        }

        if (lastEmittedEvent === 'willBlur') {
          if (eventName === 'action' && !isChildFocused && !isTransitioning) {
            emit(lastEmittedEvent = 'didBlur', childPayload);
          } else if (eventName === 'didBlur') {
            emit(lastEmittedEvent = 'didBlur', childPayload);
          }
        }

        if (lastEmittedEvent === 'didBlur' && !newRoute) {
          removeAll();
        }
      });
    });
    return {
      addListener: function addListener(eventName, eventHandler) {
        var subscribers = getChildSubscribers(eventName);

        if (!subscribers) {
          throw new Error("Invalid event name \"" + eventName + "\"");
        }

        subscribers.add(eventHandler);

        var remove = function remove() {
          subscribers.delete(eventHandler);
        };

        return {
          remove: remove
        };
      }
    };
  }
},"b00f70989b181b053baf833c33435a78",[],"node_modules/react-navigation/src/getChildEventSubscriber.js");