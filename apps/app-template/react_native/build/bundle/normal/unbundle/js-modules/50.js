__d(function (global, _require2, module, exports, _dependencyMap) {
  'use strict';

  if (__DEV__) {
    (function () {
      "use strict";

      _require2(_dependencyMap[0], "InitializeCore");

      var invariant = _require2(_dependencyMap[1], "fbjs/lib/invariant");

      var warning = _require2(_dependencyMap[2], "fbjs/lib/warning");

      var emptyFunction = _require2(_dependencyMap[3], "fbjs/lib/emptyFunction");

      var UIManager = _require2(_dependencyMap[4], "UIManager");

      var RCTEventEmitter = _require2(_dependencyMap[5], "RCTEventEmitter");

      var TextInputState = _require2(_dependencyMap[6], "TextInputState");

      var deepDiffer = _require2(_dependencyMap[7], "deepDiffer");

      var flattenStyle = _require2(_dependencyMap[8], "flattenStyle");

      var React = _require2(_dependencyMap[9], "react");

      var emptyObject = _require2(_dependencyMap[10], "fbjs/lib/emptyObject");

      var shallowEqual = _require2(_dependencyMap[11], "fbjs/lib/shallowEqual");

      var ExceptionsManager = _require2(_dependencyMap[12], "ExceptionsManager");

      var checkPropTypes = _require2(_dependencyMap[13], "prop-types/checkPropTypes");

      var deepFreezeAndThrowOnMutationInDev = _require2(_dependencyMap[14], "deepFreezeAndThrowOnMutationInDev");

      var invokeGuardedCallback = function invokeGuardedCallback(name, func, context, a, b, c, d, e, f) {
        this._hasCaughtError = false;
        this._caughtError = null;
        var funcArgs = Array.prototype.slice.call(arguments, 3);

        try {
          func.apply(context, funcArgs);
        } catch (error) {
          this._caughtError = error;
          this._hasCaughtError = true;
        }
      };

      {
        if (typeof window !== "undefined" && typeof window.dispatchEvent === "function" && typeof document !== "undefined" && typeof document.createEvent === "function") {
          var fakeNode = document.createElement("react");

          var invokeGuardedCallbackDev = function invokeGuardedCallbackDev(name, func, context, a, b, c, d, e, f) {
            invariant(typeof document !== "undefined", "The `document` global was defined when React was initialized, but is not " + "defined anymore. This can happen in a test environment if a component " + "schedules an update from an asynchronous callback, but the test has already " + "finished running. To solve this, you can either unmount the component at " + "the end of your test (and ensure that any asynchronous operations get " + "canceled in `componentWillUnmount`), or you can change the test itself " + "to be asynchronous.");
            var evt = document.createEvent("Event");
            var didError = true;
            var funcArgs = Array.prototype.slice.call(arguments, 3);

            function callCallback() {
              fakeNode.removeEventListener(evtType, callCallback, false);
              func.apply(context, funcArgs);
              didError = false;
            }

            var error = void 0;
            var didSetError = false;
            var isCrossOriginError = false;

            function onError(event) {
              error = event.error;
              didSetError = true;

              if (error === null && event.colno === 0 && event.lineno === 0) {
                isCrossOriginError = true;
              }
            }

            var evtType = "react-" + (name ? name : "invokeguardedcallback");
            window.addEventListener("error", onError);
            fakeNode.addEventListener(evtType, callCallback, false);
            evt.initEvent(evtType, false, false);
            fakeNode.dispatchEvent(evt);

            if (didError) {
              if (!didSetError) {
                error = new Error("An error was thrown inside one of your components, but React " + "doesn't know what it was. This is likely due to browser " + 'flakiness. React does its best to preserve the "Pause on ' + 'exceptions" behavior of the DevTools, which requires some ' + "DEV-mode only tricks. It's possible that these don't work in " + "your browser. Try triggering the error in production mode, " + "or switching to a modern browser. If you suspect that this is " + "actually an issue with React, please file an issue.");
              } else if (isCrossOriginError) {
                error = new Error("A cross-origin error was thrown. React doesn't have access to " + "the actual error object in development. " + "See https://fb.me/react-crossorigin-error for more information.");
              }

              this._hasCaughtError = true;
              this._caughtError = error;
            } else {
              this._hasCaughtError = false;
              this._caughtError = null;
            }

            window.removeEventListener("error", onError);
          };

          invokeGuardedCallback = invokeGuardedCallbackDev;
        }
      }
      var invokeGuardedCallback$1 = invokeGuardedCallback;
      var ReactErrorUtils = {
        _caughtError: null,
        _hasCaughtError: false,
        _rethrowError: null,
        _hasRethrowError: false,
        invokeGuardedCallback: function invokeGuardedCallback(name, func, context, a, b, c, d, e, f) {
          invokeGuardedCallback$1.apply(ReactErrorUtils, arguments);
        },
        invokeGuardedCallbackAndCatchFirstError: function invokeGuardedCallbackAndCatchFirstError(name, func, context, a, b, c, d, e, f) {
          ReactErrorUtils.invokeGuardedCallback.apply(this, arguments);

          if (ReactErrorUtils.hasCaughtError()) {
            var error = ReactErrorUtils.clearCaughtError();

            if (!ReactErrorUtils._hasRethrowError) {
              ReactErrorUtils._hasRethrowError = true;
              ReactErrorUtils._rethrowError = error;
            }
          }
        },
        rethrowCaughtError: function rethrowCaughtError() {
          return _rethrowCaughtError.apply(ReactErrorUtils, arguments);
        },
        hasCaughtError: function hasCaughtError() {
          return ReactErrorUtils._hasCaughtError;
        },
        clearCaughtError: function clearCaughtError() {
          if (ReactErrorUtils._hasCaughtError) {
            var error = ReactErrorUtils._caughtError;
            ReactErrorUtils._caughtError = null;
            ReactErrorUtils._hasCaughtError = false;
            return error;
          } else {
            invariant(false, "clearCaughtError was called but no error was captured. This error " + "is likely caused by a bug in React. Please file an issue.");
          }
        }
      };

      var _rethrowCaughtError = function _rethrowCaughtError() {
        if (ReactErrorUtils._hasRethrowError) {
          var error = ReactErrorUtils._rethrowError;
          ReactErrorUtils._rethrowError = null;
          ReactErrorUtils._hasRethrowError = false;
          throw error;
        }
      };

      var eventPluginOrder = null;
      var namesToPlugins = {};

      function recomputePluginOrdering() {
        if (!eventPluginOrder) {
          return;
        }

        for (var pluginName in namesToPlugins) {
          var pluginModule = namesToPlugins[pluginName];
          var pluginIndex = eventPluginOrder.indexOf(pluginName);
          invariant(pluginIndex > -1, "EventPluginRegistry: Cannot inject event plugins that do not exist in " + "the plugin ordering, `%s`.", pluginName);

          if (plugins[pluginIndex]) {
            continue;
          }

          invariant(pluginModule.extractEvents, "EventPluginRegistry: Event plugins must implement an `extractEvents` " + "method, but `%s` does not.", pluginName);
          plugins[pluginIndex] = pluginModule;
          var publishedEvents = pluginModule.eventTypes;

          for (var eventName in publishedEvents) {
            invariant(publishEventForPlugin(publishedEvents[eventName], pluginModule, eventName), "EventPluginRegistry: Failed to publish event `%s` for plugin `%s`.", eventName, pluginName);
          }
        }
      }

      function publishEventForPlugin(dispatchConfig, pluginModule, eventName) {
        invariant(!eventNameDispatchConfigs.hasOwnProperty(eventName), "EventPluginHub: More than one plugin attempted to publish the same " + "event name, `%s`.", eventName);
        eventNameDispatchConfigs[eventName] = dispatchConfig;
        var phasedRegistrationNames = dispatchConfig.phasedRegistrationNames;

        if (phasedRegistrationNames) {
          for (var phaseName in phasedRegistrationNames) {
            if (phasedRegistrationNames.hasOwnProperty(phaseName)) {
              var phasedRegistrationName = phasedRegistrationNames[phaseName];
              publishRegistrationName(phasedRegistrationName, pluginModule, eventName);
            }
          }

          return true;
        } else if (dispatchConfig.registrationName) {
          publishRegistrationName(dispatchConfig.registrationName, pluginModule, eventName);
          return true;
        }

        return false;
      }

      function publishRegistrationName(registrationName, pluginModule, eventName) {
        invariant(!registrationNameModules[registrationName], "EventPluginHub: More than one plugin attempted to publish the same " + "registration name, `%s`.", registrationName);
        registrationNameModules[registrationName] = pluginModule;
        registrationNameDependencies[registrationName] = pluginModule.eventTypes[eventName].dependencies;
        {
          var lowerCasedName = registrationName.toLowerCase();
        }
      }

      var plugins = [];
      var eventNameDispatchConfigs = {};
      var registrationNameModules = {};
      var registrationNameDependencies = {};

      function injectEventPluginOrder(injectedEventPluginOrder) {
        invariant(!eventPluginOrder, "EventPluginRegistry: Cannot inject event plugin ordering more than " + "once. You are likely trying to load more than one copy of React.");
        eventPluginOrder = Array.prototype.slice.call(injectedEventPluginOrder);
        recomputePluginOrdering();
      }

      function injectEventPluginsByName(injectedNamesToPlugins) {
        var isOrderingDirty = false;

        for (var pluginName in injectedNamesToPlugins) {
          if (!injectedNamesToPlugins.hasOwnProperty(pluginName)) {
            continue;
          }

          var pluginModule = injectedNamesToPlugins[pluginName];

          if (!namesToPlugins.hasOwnProperty(pluginName) || namesToPlugins[pluginName] !== pluginModule) {
            invariant(!namesToPlugins[pluginName], "EventPluginRegistry: Cannot inject two different event plugins " + "using the same name, `%s`.", pluginName);
            namesToPlugins[pluginName] = pluginModule;
            isOrderingDirty = true;
          }
        }

        if (isOrderingDirty) {
          recomputePluginOrdering();
        }
      }

      var getFiberCurrentPropsFromNode = null;
      var getInstanceFromNode = null;
      var getNodeFromInstance = null;
      var injection$1 = {
        injectComponentTree: function injectComponentTree(Injected) {
          getFiberCurrentPropsFromNode = Injected.getFiberCurrentPropsFromNode;
          getInstanceFromNode = Injected.getInstanceFromNode;
          getNodeFromInstance = Injected.getNodeFromInstance;
          {
            !(getNodeFromInstance && getInstanceFromNode) ? warning(false, "EventPluginUtils.injection.injectComponentTree(...): Injected " + "module is missing getNodeFromInstance or getInstanceFromNode.") : void 0;
          }
        }
      };

      function isEndish(topLevelType) {
        return topLevelType === "topMouseUp" || topLevelType === "topTouchEnd" || topLevelType === "topTouchCancel";
      }

      function isMoveish(topLevelType) {
        return topLevelType === "topMouseMove" || topLevelType === "topTouchMove";
      }

      function isStartish(topLevelType) {
        return topLevelType === "topMouseDown" || topLevelType === "topTouchStart";
      }

      var validateEventDispatches = void 0;
      {
        validateEventDispatches = function validateEventDispatches(event) {
          var dispatchListeners = event._dispatchListeners;
          var dispatchInstances = event._dispatchInstances;
          var listenersIsArr = Array.isArray(dispatchListeners);
          var listenersLen = listenersIsArr ? dispatchListeners.length : dispatchListeners ? 1 : 0;
          var instancesIsArr = Array.isArray(dispatchInstances);
          var instancesLen = instancesIsArr ? dispatchInstances.length : dispatchInstances ? 1 : 0;
          !(instancesIsArr === listenersIsArr && instancesLen === listenersLen) ? warning(false, "EventPluginUtils: Invalid `event`.") : void 0;
        };
      }

      function executeDispatch(event, simulated, listener, inst) {
        var type = event.type || "unknown-event";
        event.currentTarget = getNodeFromInstance(inst);
        ReactErrorUtils.invokeGuardedCallbackAndCatchFirstError(type, listener, undefined, event);
        event.currentTarget = null;
      }

      function executeDispatchesInOrder(event, simulated) {
        var dispatchListeners = event._dispatchListeners;
        var dispatchInstances = event._dispatchInstances;
        {
          validateEventDispatches(event);
        }

        if (Array.isArray(dispatchListeners)) {
          for (var i = 0; i < dispatchListeners.length; i++) {
            if (event.isPropagationStopped()) {
              break;
            }

            executeDispatch(event, simulated, dispatchListeners[i], dispatchInstances[i]);
          }
        } else if (dispatchListeners) {
          executeDispatch(event, simulated, dispatchListeners, dispatchInstances);
        }

        event._dispatchListeners = null;
        event._dispatchInstances = null;
      }

      function executeDispatchesInOrderStopAtTrueImpl(event) {
        var dispatchListeners = event._dispatchListeners;
        var dispatchInstances = event._dispatchInstances;
        {
          validateEventDispatches(event);
        }

        if (Array.isArray(dispatchListeners)) {
          for (var i = 0; i < dispatchListeners.length; i++) {
            if (event.isPropagationStopped()) {
              break;
            }

            if (dispatchListeners[i](event, dispatchInstances[i])) {
              return dispatchInstances[i];
            }
          }
        } else if (dispatchListeners) {
          if (dispatchListeners(event, dispatchInstances)) {
            return dispatchInstances;
          }
        }

        return null;
      }

      function executeDispatchesInOrderStopAtTrue(event) {
        var ret = executeDispatchesInOrderStopAtTrueImpl(event);
        event._dispatchInstances = null;
        event._dispatchListeners = null;
        return ret;
      }

      function executeDirectDispatch(event) {
        {
          validateEventDispatches(event);
        }
        var dispatchListener = event._dispatchListeners;
        var dispatchInstance = event._dispatchInstances;
        invariant(!Array.isArray(dispatchListener), "executeDirectDispatch(...): Invalid `event`.");
        event.currentTarget = dispatchListener ? getNodeFromInstance(dispatchInstance) : null;
        var res = dispatchListener ? dispatchListener(event) : null;
        event.currentTarget = null;
        event._dispatchListeners = null;
        event._dispatchInstances = null;
        return res;
      }

      function hasDispatches(event) {
        return !!event._dispatchListeners;
      }

      function accumulateInto(current, next) {
        invariant(next != null, "accumulateInto(...): Accumulated items must not be null or undefined.");

        if (current == null) {
          return next;
        }

        if (Array.isArray(current)) {
          if (Array.isArray(next)) {
            current.push.apply(current, next);
            return current;
          }

          current.push(next);
          return current;
        }

        if (Array.isArray(next)) {
          return [current].concat(next);
        }

        return [current, next];
      }

      function forEachAccumulated(arr, cb, scope) {
        if (Array.isArray(arr)) {
          arr.forEach(cb, scope);
        } else if (arr) {
          cb.call(scope, arr);
        }
      }

      var eventQueue = null;

      var executeDispatchesAndRelease = function executeDispatchesAndRelease(event, simulated) {
        if (event) {
          executeDispatchesInOrder(event, simulated);

          if (!event.isPersistent()) {
            event.constructor.release(event);
          }
        }
      };

      var executeDispatchesAndReleaseSimulated = function executeDispatchesAndReleaseSimulated(e) {
        return executeDispatchesAndRelease(e, true);
      };

      var executeDispatchesAndReleaseTopLevel = function executeDispatchesAndReleaseTopLevel(e) {
        return executeDispatchesAndRelease(e, false);
      };

      function isInteractive(tag) {
        return tag === "button" || tag === "input" || tag === "select" || tag === "textarea";
      }

      function shouldPreventMouseEvent(name, type, props) {
        switch (name) {
          case "onClick":
          case "onClickCapture":
          case "onDoubleClick":
          case "onDoubleClickCapture":
          case "onMouseDown":
          case "onMouseDownCapture":
          case "onMouseMove":
          case "onMouseMoveCapture":
          case "onMouseUp":
          case "onMouseUpCapture":
            return !!(props.disabled && isInteractive(type));

          default:
            return false;
        }
      }

      var injection = {
        injectEventPluginOrder: injectEventPluginOrder,
        injectEventPluginsByName: injectEventPluginsByName
      };

      function getListener(inst, registrationName) {
        var listener = void 0;
        var stateNode = inst.stateNode;

        if (!stateNode) {
          return null;
        }

        var props = getFiberCurrentPropsFromNode(stateNode);

        if (!props) {
          return null;
        }

        listener = props[registrationName];

        if (shouldPreventMouseEvent(registrationName, inst.type, props)) {
          return null;
        }

        invariant(!listener || typeof listener === "function", "Expected `%s` listener to be a function, instead got a value of `%s` type.", registrationName, typeof listener);
        return listener;
      }

      function extractEvents(topLevelType, targetInst, nativeEvent, nativeEventTarget) {
        var events = null;

        for (var i = 0; i < plugins.length; i++) {
          var possiblePlugin = plugins[i];

          if (possiblePlugin) {
            var extractedEvents = possiblePlugin.extractEvents(topLevelType, targetInst, nativeEvent, nativeEventTarget);

            if (extractedEvents) {
              events = accumulateInto(events, extractedEvents);
            }
          }
        }

        return events;
      }

      function runEventsInBatch(events, simulated) {
        if (events !== null) {
          eventQueue = accumulateInto(eventQueue, events);
        }

        var processingEventQueue = eventQueue;
        eventQueue = null;

        if (!processingEventQueue) {
          return;
        }

        if (simulated) {
          forEachAccumulated(processingEventQueue, executeDispatchesAndReleaseSimulated);
        } else {
          forEachAccumulated(processingEventQueue, executeDispatchesAndReleaseTopLevel);
        }

        invariant(!eventQueue, "processEventQueue(): Additional events were enqueued while processing " + "an event queue. Support for this has not yet been implemented.");
        ReactErrorUtils.rethrowCaughtError();
      }

      function runExtractedEventsInBatch(topLevelType, targetInst, nativeEvent, nativeEventTarget) {
        var events = extractEvents(topLevelType, targetInst, nativeEvent, nativeEventTarget);
        runEventsInBatch(events, false);
      }

      var IndeterminateComponent = 0;
      var FunctionalComponent = 1;
      var ClassComponent = 2;
      var HostRoot = 3;
      var HostPortal = 4;
      var HostComponent = 5;
      var HostText = 6;
      var CallComponent = 7;
      var CallHandlerPhase = 8;
      var ReturnComponent = 9;
      var Fragment = 10;
      var Mode = 11;
      var ContextConsumer = 12;
      var ContextProvider = 13;
      var ForwardRef = 14;

      function getParent(inst) {
        do {
          inst = inst["return"];
        } while (inst && inst.tag !== HostComponent);

        if (inst) {
          return inst;
        }

        return null;
      }

      function getLowestCommonAncestor(instA, instB) {
        var depthA = 0;

        for (var tempA = instA; tempA; tempA = getParent(tempA)) {
          depthA++;
        }

        var depthB = 0;

        for (var tempB = instB; tempB; tempB = getParent(tempB)) {
          depthB++;
        }

        while (depthA - depthB > 0) {
          instA = getParent(instA);
          depthA--;
        }

        while (depthB - depthA > 0) {
          instB = getParent(instB);
          depthB--;
        }

        var depth = depthA;

        while (depth--) {
          if (instA === instB || instA === instB.alternate) {
            return instA;
          }

          instA = getParent(instA);
          instB = getParent(instB);
        }

        return null;
      }

      function isAncestor(instA, instB) {
        while (instB) {
          if (instA === instB || instA === instB.alternate) {
            return true;
          }

          instB = getParent(instB);
        }

        return false;
      }

      function getParentInstance(inst) {
        return getParent(inst);
      }

      function traverseTwoPhase(inst, fn, arg) {
        var path = [];

        while (inst) {
          path.push(inst);
          inst = getParent(inst);
        }

        var i = void 0;

        for (i = path.length; i-- > 0;) {
          fn(path[i], "captured", arg);
        }

        for (i = 0; i < path.length; i++) {
          fn(path[i], "bubbled", arg);
        }
      }

      function listenerAtPhase(inst, event, propagationPhase) {
        var registrationName = event.dispatchConfig.phasedRegistrationNames[propagationPhase];
        return getListener(inst, registrationName);
      }

      function accumulateDirectionalDispatches(inst, phase, event) {
        {
          !inst ? warning(false, "Dispatching inst must not be null") : void 0;
        }
        var listener = listenerAtPhase(inst, event, phase);

        if (listener) {
          event._dispatchListeners = accumulateInto(event._dispatchListeners, listener);
          event._dispatchInstances = accumulateInto(event._dispatchInstances, inst);
        }
      }

      function accumulateTwoPhaseDispatchesSingle(event) {
        if (event && event.dispatchConfig.phasedRegistrationNames) {
          traverseTwoPhase(event._targetInst, accumulateDirectionalDispatches, event);
        }
      }

      function accumulateTwoPhaseDispatchesSingleSkipTarget(event) {
        if (event && event.dispatchConfig.phasedRegistrationNames) {
          var targetInst = event._targetInst;
          var parentInst = targetInst ? getParentInstance(targetInst) : null;
          traverseTwoPhase(parentInst, accumulateDirectionalDispatches, event);
        }
      }

      function accumulateDispatches(inst, ignoredDirection, event) {
        if (inst && event && event.dispatchConfig.registrationName) {
          var registrationName = event.dispatchConfig.registrationName;
          var listener = getListener(inst, registrationName);

          if (listener) {
            event._dispatchListeners = accumulateInto(event._dispatchListeners, listener);
            event._dispatchInstances = accumulateInto(event._dispatchInstances, inst);
          }
        }
      }

      function accumulateDirectDispatchesSingle(event) {
        if (event && event.dispatchConfig.registrationName) {
          accumulateDispatches(event._targetInst, null, event);
        }
      }

      function accumulateTwoPhaseDispatches(events) {
        forEachAccumulated(events, accumulateTwoPhaseDispatchesSingle);
      }

      function accumulateTwoPhaseDispatchesSkipTarget(events) {
        forEachAccumulated(events, accumulateTwoPhaseDispatchesSingleSkipTarget);
      }

      function accumulateDirectDispatches(events) {
        forEachAccumulated(events, accumulateDirectDispatchesSingle);
      }

      var didWarnForAddedNewProperty = false;
      var EVENT_POOL_SIZE = 10;
      var shouldBeReleasedProperties = ["dispatchConfig", "_targetInst", "nativeEvent", "isDefaultPrevented", "isPropagationStopped", "_dispatchListeners", "_dispatchInstances"];
      var EventInterface = {
        type: null,
        target: null,
        currentTarget: emptyFunction.thatReturnsNull,
        eventPhase: null,
        bubbles: null,
        cancelable: null,
        timeStamp: function timeStamp(event) {
          return event.timeStamp || Date.now();
        },
        defaultPrevented: null,
        isTrusted: null
      };

      function SyntheticEvent(dispatchConfig, targetInst, nativeEvent, nativeEventTarget) {
        {
          delete this.nativeEvent;
          delete this.preventDefault;
          delete this.stopPropagation;
        }
        this.dispatchConfig = dispatchConfig;
        this._targetInst = targetInst;
        this.nativeEvent = nativeEvent;
        var Interface = this.constructor.Interface;

        for (var propName in Interface) {
          if (!Interface.hasOwnProperty(propName)) {
            continue;
          }

          {
            delete this[propName];
          }
          var normalize = Interface[propName];

          if (normalize) {
            this[propName] = normalize(nativeEvent);
          } else {
            if (propName === "target") {
              this.target = nativeEventTarget;
            } else {
              this[propName] = nativeEvent[propName];
            }
          }
        }

        var defaultPrevented = nativeEvent.defaultPrevented != null ? nativeEvent.defaultPrevented : nativeEvent.returnValue === false;

        if (defaultPrevented) {
          this.isDefaultPrevented = emptyFunction.thatReturnsTrue;
        } else {
          this.isDefaultPrevented = emptyFunction.thatReturnsFalse;
        }

        this.isPropagationStopped = emptyFunction.thatReturnsFalse;
        return this;
      }

      babelHelpers.extends(SyntheticEvent.prototype, {
        preventDefault: function preventDefault() {
          this.defaultPrevented = true;
          var event = this.nativeEvent;

          if (!event) {
            return;
          }

          if (event.preventDefault) {
            event.preventDefault();
          } else if (typeof event.returnValue !== "unknown") {
            event.returnValue = false;
          }

          this.isDefaultPrevented = emptyFunction.thatReturnsTrue;
        },
        stopPropagation: function stopPropagation() {
          var event = this.nativeEvent;

          if (!event) {
            return;
          }

          if (event.stopPropagation) {
            event.stopPropagation();
          } else if (typeof event.cancelBubble !== "unknown") {
            event.cancelBubble = true;
          }

          this.isPropagationStopped = emptyFunction.thatReturnsTrue;
        },
        persist: function persist() {
          this.isPersistent = emptyFunction.thatReturnsTrue;
        },
        isPersistent: emptyFunction.thatReturnsFalse,
        destructor: function destructor() {
          var Interface = this.constructor.Interface;

          for (var propName in Interface) {
            {
              Object.defineProperty(this, propName, getPooledWarningPropertyDefinition(propName, Interface[propName]));
            }
          }

          for (var i = 0; i < shouldBeReleasedProperties.length; i++) {
            this[shouldBeReleasedProperties[i]] = null;
          }

          {
            Object.defineProperty(this, "nativeEvent", getPooledWarningPropertyDefinition("nativeEvent", null));
            Object.defineProperty(this, "preventDefault", getPooledWarningPropertyDefinition("preventDefault", emptyFunction));
            Object.defineProperty(this, "stopPropagation", getPooledWarningPropertyDefinition("stopPropagation", emptyFunction));
          }
        }
      });
      SyntheticEvent.Interface = EventInterface;

      SyntheticEvent.extend = function (Interface) {
        var Super = this;

        var E = function E() {};

        E.prototype = Super.prototype;
        var prototype = new E();

        function Class() {
          return Super.apply(this, arguments);
        }

        babelHelpers.extends(prototype, Class.prototype);
        Class.prototype = prototype;
        Class.prototype.constructor = Class;
        Class.Interface = babelHelpers.extends({}, Super.Interface, Interface);
        Class.extend = Super.extend;
        addEventPoolingTo(Class);
        return Class;
      };

      {
        var isProxySupported = typeof Proxy === "function" && !Object.isSealed(new Proxy({}, {}));

        if (isProxySupported) {
          SyntheticEvent = new Proxy(SyntheticEvent, {
            construct: function construct(target, args) {
              return this.apply(target, Object.create(target.prototype), args);
            },
            apply: function apply(constructor, that, args) {
              return new Proxy(constructor.apply(that, args), {
                set: function set(target, prop, value) {
                  if (prop !== "isPersistent" && !target.constructor.Interface.hasOwnProperty(prop) && shouldBeReleasedProperties.indexOf(prop) === -1) {
                    !(didWarnForAddedNewProperty || target.isPersistent()) ? warning(false, "This synthetic event is reused for performance reasons. If you're " + "seeing this, you're adding a new property in the synthetic event object. " + "The property is never released. See " + "https://fb.me/react-event-pooling for more information.") : void 0;
                    didWarnForAddedNewProperty = true;
                  }

                  target[prop] = value;
                  return true;
                }
              });
            }
          });
        }
      }
      addEventPoolingTo(SyntheticEvent);

      function getPooledWarningPropertyDefinition(propName, getVal) {
        var isFunction = typeof getVal === "function";
        return {
          configurable: true,
          set: set,
          get: get
        };

        function set(val) {
          var action = isFunction ? "setting the method" : "setting the property";
          warn(action, "This is effectively a no-op");
          return val;
        }

        function get() {
          var action = isFunction ? "accessing the method" : "accessing the property";
          var result = isFunction ? "This is a no-op function" : "This is set to null";
          warn(action, result);
          return getVal;
        }

        function warn(action, result) {
          var warningCondition = false;
          !warningCondition ? warning(false, "This synthetic event is reused for performance reasons. If you're seeing this, " + "you're %s `%s` on a released/nullified synthetic event. %s. " + "If you must keep the original synthetic event around, use event.persist(). " + "See https://fb.me/react-event-pooling for more information.", action, propName, result) : void 0;
        }
      }

      function getPooledEvent(dispatchConfig, targetInst, nativeEvent, nativeInst) {
        var EventConstructor = this;

        if (EventConstructor.eventPool.length) {
          var instance = EventConstructor.eventPool.pop();
          EventConstructor.call(instance, dispatchConfig, targetInst, nativeEvent, nativeInst);
          return instance;
        }

        return new EventConstructor(dispatchConfig, targetInst, nativeEvent, nativeInst);
      }

      function releasePooledEvent(event) {
        var EventConstructor = this;
        invariant(event instanceof EventConstructor, "Trying to release an event instance  into a pool of a different type.");
        event.destructor();

        if (EventConstructor.eventPool.length < EVENT_POOL_SIZE) {
          EventConstructor.eventPool.push(event);
        }
      }

      function addEventPoolingTo(EventConstructor) {
        EventConstructor.eventPool = [];
        EventConstructor.getPooled = getPooledEvent;
        EventConstructor.release = releasePooledEvent;
      }

      var SyntheticEvent$1 = SyntheticEvent;
      var ResponderSyntheticEvent = SyntheticEvent$1.extend({
        touchHistory: function touchHistory(nativeEvent) {
          return null;
        }
      });
      var MAX_TOUCH_BANK = 20;
      var touchBank = [];
      var touchHistory = {
        touchBank: touchBank,
        numberActiveTouches: 0,
        indexOfSingleActiveTouch: -1,
        mostRecentTimeStamp: 0
      };

      function timestampForTouch(touch) {
        return touch.timeStamp || touch.timestamp;
      }

      function createTouchRecord(touch) {
        return {
          touchActive: true,
          startPageX: touch.pageX,
          startPageY: touch.pageY,
          startTimeStamp: timestampForTouch(touch),
          currentPageX: touch.pageX,
          currentPageY: touch.pageY,
          currentTimeStamp: timestampForTouch(touch),
          previousPageX: touch.pageX,
          previousPageY: touch.pageY,
          previousTimeStamp: timestampForTouch(touch)
        };
      }

      function resetTouchRecord(touchRecord, touch) {
        touchRecord.touchActive = true;
        touchRecord.startPageX = touch.pageX;
        touchRecord.startPageY = touch.pageY;
        touchRecord.startTimeStamp = timestampForTouch(touch);
        touchRecord.currentPageX = touch.pageX;
        touchRecord.currentPageY = touch.pageY;
        touchRecord.currentTimeStamp = timestampForTouch(touch);
        touchRecord.previousPageX = touch.pageX;
        touchRecord.previousPageY = touch.pageY;
        touchRecord.previousTimeStamp = timestampForTouch(touch);
      }

      function getTouchIdentifier(_ref) {
        var identifier = _ref.identifier;
        invariant(identifier != null, "Touch object is missing identifier.");
        {
          !(identifier <= MAX_TOUCH_BANK) ? warning(false, "Touch identifier %s is greater than maximum supported %s which causes " + "performance issues backfilling array locations for all of the indices.", identifier, MAX_TOUCH_BANK) : void 0;
        }
        return identifier;
      }

      function recordTouchStart(touch) {
        var identifier = getTouchIdentifier(touch);
        var touchRecord = touchBank[identifier];

        if (touchRecord) {
          resetTouchRecord(touchRecord, touch);
        } else {
          touchBank[identifier] = createTouchRecord(touch);
        }

        touchHistory.mostRecentTimeStamp = timestampForTouch(touch);
      }

      function recordTouchMove(touch) {
        var touchRecord = touchBank[getTouchIdentifier(touch)];

        if (touchRecord) {
          touchRecord.touchActive = true;
          touchRecord.previousPageX = touchRecord.currentPageX;
          touchRecord.previousPageY = touchRecord.currentPageY;
          touchRecord.previousTimeStamp = touchRecord.currentTimeStamp;
          touchRecord.currentPageX = touch.pageX;
          touchRecord.currentPageY = touch.pageY;
          touchRecord.currentTimeStamp = timestampForTouch(touch);
          touchHistory.mostRecentTimeStamp = timestampForTouch(touch);
        } else {
          console.error("Cannot record touch move without a touch start.\n" + "Touch Move: %s\n", "Touch Bank: %s", printTouch(touch), printTouchBank());
        }
      }

      function recordTouchEnd(touch) {
        var touchRecord = touchBank[getTouchIdentifier(touch)];

        if (touchRecord) {
          touchRecord.touchActive = false;
          touchRecord.previousPageX = touchRecord.currentPageX;
          touchRecord.previousPageY = touchRecord.currentPageY;
          touchRecord.previousTimeStamp = touchRecord.currentTimeStamp;
          touchRecord.currentPageX = touch.pageX;
          touchRecord.currentPageY = touch.pageY;
          touchRecord.currentTimeStamp = timestampForTouch(touch);
          touchHistory.mostRecentTimeStamp = timestampForTouch(touch);
        } else {
          console.error("Cannot record touch end without a touch start.\n" + "Touch End: %s\n", "Touch Bank: %s", printTouch(touch), printTouchBank());
        }
      }

      function printTouch(touch) {
        return JSON.stringify({
          identifier: touch.identifier,
          pageX: touch.pageX,
          pageY: touch.pageY,
          timestamp: timestampForTouch(touch)
        });
      }

      function printTouchBank() {
        var printed = JSON.stringify(touchBank.slice(0, MAX_TOUCH_BANK));

        if (touchBank.length > MAX_TOUCH_BANK) {
          printed += " (original size: " + touchBank.length + ")";
        }

        return printed;
      }

      var ResponderTouchHistoryStore = {
        recordTouchTrack: function recordTouchTrack(topLevelType, nativeEvent) {
          if (isMoveish(topLevelType)) {
            nativeEvent.changedTouches.forEach(recordTouchMove);
          } else if (isStartish(topLevelType)) {
            nativeEvent.changedTouches.forEach(recordTouchStart);
            touchHistory.numberActiveTouches = nativeEvent.touches.length;

            if (touchHistory.numberActiveTouches === 1) {
              touchHistory.indexOfSingleActiveTouch = nativeEvent.touches[0].identifier;
            }
          } else if (isEndish(topLevelType)) {
            nativeEvent.changedTouches.forEach(recordTouchEnd);
            touchHistory.numberActiveTouches = nativeEvent.touches.length;

            if (touchHistory.numberActiveTouches === 1) {
              for (var i = 0; i < touchBank.length; i++) {
                var touchTrackToCheck = touchBank[i];

                if (touchTrackToCheck != null && touchTrackToCheck.touchActive) {
                  touchHistory.indexOfSingleActiveTouch = i;
                  break;
                }
              }

              {
                var activeRecord = touchBank[touchHistory.indexOfSingleActiveTouch];
                !(activeRecord != null && activeRecord.touchActive) ? warning(false, "Cannot find single active touch.") : void 0;
              }
            }
          }
        },
        touchHistory: touchHistory
      };

      function accumulate(current, next) {
        invariant(next != null, "accumulate(...): Accumulated items must be not be null or undefined.");

        if (current == null) {
          return next;
        }

        if (Array.isArray(current)) {
          return current.concat(next);
        }

        if (Array.isArray(next)) {
          return [current].concat(next);
        }

        return [current, next];
      }

      var responderInst = null;
      var trackedTouchCount = 0;
      var previousActiveTouches = 0;

      var changeResponder = function changeResponder(nextResponderInst, blockHostResponder) {
        var oldResponderInst = responderInst;
        responderInst = nextResponderInst;

        if (ResponderEventPlugin.GlobalResponderHandler !== null) {
          ResponderEventPlugin.GlobalResponderHandler.onChange(oldResponderInst, nextResponderInst, blockHostResponder);
        }
      };

      var eventTypes = {
        startShouldSetResponder: {
          phasedRegistrationNames: {
            bubbled: "onStartShouldSetResponder",
            captured: "onStartShouldSetResponderCapture"
          }
        },
        scrollShouldSetResponder: {
          phasedRegistrationNames: {
            bubbled: "onScrollShouldSetResponder",
            captured: "onScrollShouldSetResponderCapture"
          }
        },
        selectionChangeShouldSetResponder: {
          phasedRegistrationNames: {
            bubbled: "onSelectionChangeShouldSetResponder",
            captured: "onSelectionChangeShouldSetResponderCapture"
          }
        },
        moveShouldSetResponder: {
          phasedRegistrationNames: {
            bubbled: "onMoveShouldSetResponder",
            captured: "onMoveShouldSetResponderCapture"
          }
        },
        responderStart: {
          registrationName: "onResponderStart"
        },
        responderMove: {
          registrationName: "onResponderMove"
        },
        responderEnd: {
          registrationName: "onResponderEnd"
        },
        responderRelease: {
          registrationName: "onResponderRelease"
        },
        responderTerminationRequest: {
          registrationName: "onResponderTerminationRequest"
        },
        responderGrant: {
          registrationName: "onResponderGrant"
        },
        responderReject: {
          registrationName: "onResponderReject"
        },
        responderTerminate: {
          registrationName: "onResponderTerminate"
        }
      };

      function setResponderAndExtractTransfer(topLevelType, targetInst, nativeEvent, nativeEventTarget) {
        var shouldSetEventType = isStartish(topLevelType) ? eventTypes.startShouldSetResponder : isMoveish(topLevelType) ? eventTypes.moveShouldSetResponder : topLevelType === "topSelectionChange" ? eventTypes.selectionChangeShouldSetResponder : eventTypes.scrollShouldSetResponder;
        var bubbleShouldSetFrom = !responderInst ? targetInst : getLowestCommonAncestor(responderInst, targetInst);
        var skipOverBubbleShouldSetFrom = bubbleShouldSetFrom === responderInst;
        var shouldSetEvent = ResponderSyntheticEvent.getPooled(shouldSetEventType, bubbleShouldSetFrom, nativeEvent, nativeEventTarget);
        shouldSetEvent.touchHistory = ResponderTouchHistoryStore.touchHistory;

        if (skipOverBubbleShouldSetFrom) {
          accumulateTwoPhaseDispatchesSkipTarget(shouldSetEvent);
        } else {
          accumulateTwoPhaseDispatches(shouldSetEvent);
        }

        var wantsResponderInst = executeDispatchesInOrderStopAtTrue(shouldSetEvent);

        if (!shouldSetEvent.isPersistent()) {
          shouldSetEvent.constructor.release(shouldSetEvent);
        }

        if (!wantsResponderInst || wantsResponderInst === responderInst) {
          return null;
        }

        var extracted = void 0;
        var grantEvent = ResponderSyntheticEvent.getPooled(eventTypes.responderGrant, wantsResponderInst, nativeEvent, nativeEventTarget);
        grantEvent.touchHistory = ResponderTouchHistoryStore.touchHistory;
        accumulateDirectDispatches(grantEvent);
        var blockHostResponder = executeDirectDispatch(grantEvent) === true;

        if (responderInst) {
          var terminationRequestEvent = ResponderSyntheticEvent.getPooled(eventTypes.responderTerminationRequest, responderInst, nativeEvent, nativeEventTarget);
          terminationRequestEvent.touchHistory = ResponderTouchHistoryStore.touchHistory;
          accumulateDirectDispatches(terminationRequestEvent);
          var shouldSwitch = !hasDispatches(terminationRequestEvent) || executeDirectDispatch(terminationRequestEvent);

          if (!terminationRequestEvent.isPersistent()) {
            terminationRequestEvent.constructor.release(terminationRequestEvent);
          }

          if (shouldSwitch) {
            var terminateEvent = ResponderSyntheticEvent.getPooled(eventTypes.responderTerminate, responderInst, nativeEvent, nativeEventTarget);
            terminateEvent.touchHistory = ResponderTouchHistoryStore.touchHistory;
            accumulateDirectDispatches(terminateEvent);
            extracted = accumulate(extracted, [grantEvent, terminateEvent]);
            changeResponder(wantsResponderInst, blockHostResponder);
          } else {
            var rejectEvent = ResponderSyntheticEvent.getPooled(eventTypes.responderReject, wantsResponderInst, nativeEvent, nativeEventTarget);
            rejectEvent.touchHistory = ResponderTouchHistoryStore.touchHistory;
            accumulateDirectDispatches(rejectEvent);
            extracted = accumulate(extracted, rejectEvent);
          }
        } else {
          extracted = accumulate(extracted, grantEvent);
          changeResponder(wantsResponderInst, blockHostResponder);
        }

        return extracted;
      }

      function canTriggerTransfer(topLevelType, topLevelInst, nativeEvent) {
        return topLevelInst && (topLevelType === "topScroll" && !nativeEvent.responderIgnoreScroll || trackedTouchCount > 0 && topLevelType === "topSelectionChange" || isStartish(topLevelType) || isMoveish(topLevelType));
      }

      function noResponderTouches(nativeEvent) {
        var touches = nativeEvent.touches;

        if (!touches || touches.length === 0) {
          return true;
        }

        for (var i = 0; i < touches.length; i++) {
          var activeTouch = touches[i];
          var target = activeTouch.target;

          if (target !== null && target !== undefined && target !== 0) {
            var targetInst = getInstanceFromNode(target);

            if (isAncestor(responderInst, targetInst)) {
              return false;
            }
          }
        }

        return true;
      }

      var ResponderEventPlugin = {
        _getResponder: function _getResponder() {
          return responderInst;
        },
        eventTypes: eventTypes,
        extractEvents: function extractEvents(topLevelType, targetInst, nativeEvent, nativeEventTarget) {
          if (isStartish(topLevelType)) {
            trackedTouchCount += 1;
          } else if (isEndish(topLevelType)) {
            if (trackedTouchCount >= 0) {
              trackedTouchCount -= 1;
            } else {
              console.error("Ended a touch event which was not counted in `trackedTouchCount`.");
              return null;
            }
          }

          ResponderTouchHistoryStore.recordTouchTrack(topLevelType, nativeEvent);
          var extracted = canTriggerTransfer(topLevelType, targetInst, nativeEvent) ? setResponderAndExtractTransfer(topLevelType, targetInst, nativeEvent, nativeEventTarget) : null;
          var isResponderTouchStart = responderInst && isStartish(topLevelType);
          var isResponderTouchMove = responderInst && isMoveish(topLevelType);
          var isResponderTouchEnd = responderInst && isEndish(topLevelType);
          var incrementalTouch = isResponderTouchStart ? eventTypes.responderStart : isResponderTouchMove ? eventTypes.responderMove : isResponderTouchEnd ? eventTypes.responderEnd : null;

          if (incrementalTouch) {
            var gesture = ResponderSyntheticEvent.getPooled(incrementalTouch, responderInst, nativeEvent, nativeEventTarget);
            gesture.touchHistory = ResponderTouchHistoryStore.touchHistory;
            accumulateDirectDispatches(gesture);
            extracted = accumulate(extracted, gesture);
          }

          var isResponderTerminate = responderInst && topLevelType === "topTouchCancel";
          var isResponderRelease = responderInst && !isResponderTerminate && isEndish(topLevelType) && noResponderTouches(nativeEvent);
          var finalTouch = isResponderTerminate ? eventTypes.responderTerminate : isResponderRelease ? eventTypes.responderRelease : null;

          if (finalTouch) {
            var finalEvent = ResponderSyntheticEvent.getPooled(finalTouch, responderInst, nativeEvent, nativeEventTarget);
            finalEvent.touchHistory = ResponderTouchHistoryStore.touchHistory;
            accumulateDirectDispatches(finalEvent);
            extracted = accumulate(extracted, finalEvent);
            changeResponder(null);
          }

          var numberActiveTouches = ResponderTouchHistoryStore.touchHistory.numberActiveTouches;

          if (ResponderEventPlugin.GlobalInteractionHandler && numberActiveTouches !== previousActiveTouches) {
            ResponderEventPlugin.GlobalInteractionHandler.onChange(numberActiveTouches);
          }

          previousActiveTouches = numberActiveTouches;
          return extracted;
        },
        GlobalResponderHandler: null,
        GlobalInteractionHandler: null,
        injection: {
          injectGlobalResponderHandler: function injectGlobalResponderHandler(GlobalResponderHandler) {
            ResponderEventPlugin.GlobalResponderHandler = GlobalResponderHandler;
          },
          injectGlobalInteractionHandler: function injectGlobalInteractionHandler(GlobalInteractionHandler) {
            ResponderEventPlugin.GlobalInteractionHandler = GlobalInteractionHandler;
          }
        }
      };
      var customBubblingEventTypes = {};
      var customDirectEventTypes = {};
      var ReactNativeBridgeEventPlugin = {
        eventTypes: {},
        extractEvents: function extractEvents(topLevelType, targetInst, nativeEvent, nativeEventTarget) {
          if (targetInst == null) {
            return null;
          }

          var bubbleDispatchConfig = customBubblingEventTypes[topLevelType];
          var directDispatchConfig = customDirectEventTypes[topLevelType];
          invariant(bubbleDispatchConfig || directDispatchConfig, 'Unsupported top level event type "%s" dispatched', topLevelType);
          var event = SyntheticEvent$1.getPooled(bubbleDispatchConfig || directDispatchConfig, targetInst, nativeEvent, nativeEventTarget);

          if (bubbleDispatchConfig) {
            accumulateTwoPhaseDispatches(event);
          } else if (directDispatchConfig) {
            accumulateDirectDispatches(event);
          } else {
            return null;
          }

          return event;
        },
        processEventTypes: function processEventTypes(viewConfig) {
          var bubblingEventTypes = viewConfig.bubblingEventTypes,
              directEventTypes = viewConfig.directEventTypes;
          {
            if (bubblingEventTypes != null && directEventTypes != null) {
              for (var topLevelType in directEventTypes) {
                invariant(bubblingEventTypes[topLevelType] == null, "Event cannot be both direct and bubbling: %s", topLevelType);
              }
            }
          }

          if (bubblingEventTypes != null) {
            for (var _topLevelType in bubblingEventTypes) {
              if (customBubblingEventTypes[_topLevelType] == null) {
                ReactNativeBridgeEventPlugin.eventTypes[_topLevelType] = customBubblingEventTypes[_topLevelType] = bubblingEventTypes[_topLevelType];
              }
            }
          }

          if (directEventTypes != null) {
            for (var _topLevelType2 in directEventTypes) {
              if (customDirectEventTypes[_topLevelType2] == null) {
                ReactNativeBridgeEventPlugin.eventTypes[_topLevelType2] = customDirectEventTypes[_topLevelType2] = directEventTypes[_topLevelType2];
              }
            }
          }
        }
      };
      var instanceCache = {};
      var instanceProps = {};

      function precacheFiberNode(hostInst, tag) {
        instanceCache[tag] = hostInst;
      }

      function uncacheFiberNode(tag) {
        delete instanceCache[tag];
        delete instanceProps[tag];
      }

      function getInstanceFromTag(tag) {
        if (typeof tag === "number") {
          return instanceCache[tag] || null;
        } else {
          return tag;
        }
      }

      function getTagFromInstance(inst) {
        var tag = inst.stateNode._nativeTag;

        if (tag === undefined) {
          tag = inst.stateNode.canonical._nativeTag;
        }

        invariant(tag, "All native instances should have a tag.");
        return tag;
      }

      function getFiberCurrentPropsFromNode$1(stateNode) {
        return instanceProps[stateNode._nativeTag] || null;
      }

      function updateFiberProps(tag, props) {
        instanceProps[tag] = props;
      }

      var ReactNativeComponentTree = Object.freeze({
        precacheFiberNode: precacheFiberNode,
        uncacheFiberNode: uncacheFiberNode,
        getClosestInstanceFromNode: getInstanceFromTag,
        getInstanceFromNode: getInstanceFromTag,
        getNodeFromInstance: getTagFromInstance,
        getFiberCurrentPropsFromNode: getFiberCurrentPropsFromNode$1,
        updateFiberProps: updateFiberProps
      });
      var ReactNativeEventPluginOrder = ["ResponderEventPlugin", "ReactNativeBridgeEventPlugin"];
      var ReactNativeGlobalResponderHandler = {
        onChange: function onChange(from, to, blockNativeResponder) {
          if (to !== null) {
            var tag = to.stateNode._nativeTag;
            UIManager.setJSResponder(tag, blockNativeResponder);
          } else {
            UIManager.clearJSResponder();
          }
        }
      };
      injection.injectEventPluginOrder(ReactNativeEventPluginOrder);
      injection$1.injectComponentTree(ReactNativeComponentTree);
      ResponderEventPlugin.injection.injectGlobalResponderHandler(ReactNativeGlobalResponderHandler);
      injection.injectEventPluginsByName({
        ResponderEventPlugin: ResponderEventPlugin,
        ReactNativeBridgeEventPlugin: ReactNativeBridgeEventPlugin
      });
      var fiberHostComponent = null;
      var restoreTarget = null;
      var restoreQueue = null;

      function restoreStateOfTarget(target) {
        var internalInstance = getInstanceFromNode(target);

        if (!internalInstance) {
          return;
        }

        invariant(fiberHostComponent && typeof fiberHostComponent.restoreControlledState === "function", "Fiber needs to be injected to handle a fiber target for controlled " + "events. This error is likely caused by a bug in React. Please file an issue.");
        var props = getFiberCurrentPropsFromNode(internalInstance.stateNode);
        fiberHostComponent.restoreControlledState(internalInstance.stateNode, internalInstance.type, props);
      }

      function needsStateRestore() {
        return restoreTarget !== null || restoreQueue !== null;
      }

      function restoreStateIfNeeded() {
        if (!restoreTarget) {
          return;
        }

        var target = restoreTarget;
        var queuedTargets = restoreQueue;
        restoreTarget = null;
        restoreQueue = null;
        restoreStateOfTarget(target);

        if (queuedTargets) {
          for (var i = 0; i < queuedTargets.length; i++) {
            restoreStateOfTarget(queuedTargets[i]);
          }
        }
      }

      var _batchedUpdates = function _batchedUpdates(fn, bookkeeping) {
        return fn(bookkeeping);
      };

      var _interactiveUpdates = function _interactiveUpdates(fn, a, b) {
        return fn(a, b);
      };

      var _flushInteractiveUpdates = function _flushInteractiveUpdates() {};

      var isBatching = false;

      function batchedUpdates(fn, bookkeeping) {
        if (isBatching) {
          return fn(bookkeeping);
        }

        isBatching = true;

        try {
          return _batchedUpdates(fn, bookkeeping);
        } finally {
          isBatching = false;
          var controlledComponentsHavePendingUpdates = needsStateRestore();

          if (controlledComponentsHavePendingUpdates) {
            _flushInteractiveUpdates();

            restoreStateIfNeeded();
          }
        }
      }

      var injection$2 = {
        injectRenderer: function injectRenderer(renderer) {
          _batchedUpdates = renderer.batchedUpdates;
          _interactiveUpdates = renderer.interactiveUpdates;
          _flushInteractiveUpdates = renderer.flushInteractiveUpdates;
        }
      };
      var INITIAL_TAG_COUNT = 1;
      var ReactNativeTagHandles = {
        tagsStartAt: INITIAL_TAG_COUNT,
        tagCount: INITIAL_TAG_COUNT,
        allocateTag: function allocateTag() {
          while (this.reactTagIsNativeTopRootID(ReactNativeTagHandles.tagCount)) {
            ReactNativeTagHandles.tagCount++;
          }

          var tag = ReactNativeTagHandles.tagCount;
          ReactNativeTagHandles.tagCount++;
          return tag;
        },
        assertRootTag: function assertRootTag(tag) {
          invariant(this.reactTagIsNativeTopRootID(tag), "Expect a native root tag, instead got %s", tag);
        },
        reactTagIsNativeTopRootID: function reactTagIsNativeTopRootID(reactTag) {
          return reactTag % 10 === 1;
        }
      };
      var EMPTY_NATIVE_EVENT = {};

      var touchSubsequence = function touchSubsequence(touches, indices) {
        var ret = [];

        for (var i = 0; i < indices.length; i++) {
          ret.push(touches[indices[i]]);
        }

        return ret;
      };

      var removeTouchesAtIndices = function removeTouchesAtIndices(touches, indices) {
        var rippedOut = [];
        var temp = touches;

        for (var i = 0; i < indices.length; i++) {
          var index = indices[i];
          rippedOut.push(touches[index]);
          temp[index] = null;
        }

        var fillAt = 0;

        for (var j = 0; j < temp.length; j++) {
          var cur = temp[j];

          if (cur !== null) {
            temp[fillAt++] = cur;
          }
        }

        temp.length = fillAt;
        return rippedOut;
      };

      function _receiveRootNodeIDEvent(rootNodeID, topLevelType, nativeEventParam) {
        var nativeEvent = nativeEventParam || EMPTY_NATIVE_EVENT;
        var inst = getInstanceFromTag(rootNodeID);
        batchedUpdates(function () {
          runExtractedEventsInBatch(topLevelType, inst, nativeEvent, nativeEvent.target);
        });
      }

      function receiveEvent(rootNodeID, topLevelType, nativeEventParam) {
        _receiveRootNodeIDEvent(rootNodeID, topLevelType, nativeEventParam);
      }

      function receiveTouches(eventTopLevelType, touches, changedIndices) {
        var changedTouches = eventTopLevelType === "topTouchEnd" || eventTopLevelType === "topTouchCancel" ? removeTouchesAtIndices(touches, changedIndices) : touchSubsequence(touches, changedIndices);

        for (var jj = 0; jj < changedTouches.length; jj++) {
          var touch = changedTouches[jj];
          touch.changedTouches = changedTouches;
          touch.touches = touches;
          var nativeEvent = touch;
          var rootNodeID = null;
          var target = nativeEvent.target;

          if (target !== null && target !== undefined) {
            if (target < ReactNativeTagHandles.tagsStartAt) {
              {
                warning(false, "A view is reporting that a touch occurred on tag zero.");
              }
            } else {
              rootNodeID = target;
            }
          }

          _receiveRootNodeIDEvent(rootNodeID, eventTopLevelType, nativeEvent);
        }
      }

      var ReactNativeEventEmitter = Object.freeze({
        getListener: getListener,
        registrationNames: registrationNameModules,
        _receiveRootNodeIDEvent: _receiveRootNodeIDEvent,
        receiveEvent: receiveEvent,
        receiveTouches: receiveTouches
      });
      RCTEventEmitter.register(ReactNativeEventEmitter);
      var hasSymbol = typeof Symbol === "function" && Symbol["for"];
      var REACT_ELEMENT_TYPE = hasSymbol ? Symbol["for"]("react.element") : 0xeac7;
      var REACT_CALL_TYPE = hasSymbol ? Symbol["for"]("react.call") : 0xeac8;
      var REACT_RETURN_TYPE = hasSymbol ? Symbol["for"]("react.return") : 0xeac9;
      var REACT_PORTAL_TYPE = hasSymbol ? Symbol["for"]("react.portal") : 0xeaca;
      var REACT_FRAGMENT_TYPE = hasSymbol ? Symbol["for"]("react.fragment") : 0xeacb;
      var REACT_STRICT_MODE_TYPE = hasSymbol ? Symbol["for"]("react.strict_mode") : 0xeacc;
      var REACT_PROVIDER_TYPE = hasSymbol ? Symbol["for"]("react.provider") : 0xeacd;
      var REACT_CONTEXT_TYPE = hasSymbol ? Symbol["for"]("react.context") : 0xeace;
      var REACT_ASYNC_MODE_TYPE = hasSymbol ? Symbol["for"]("react.async_mode") : 0xeacf;
      var REACT_FORWARD_REF_TYPE = hasSymbol ? Symbol["for"]("react.forward_ref") : 0xead0;
      var MAYBE_ITERATOR_SYMBOL = typeof Symbol === "function" && (typeof Symbol === "function" ? Symbol.iterator : "@@iterator");
      var FAUX_ITERATOR_SYMBOL = "@@iterator";

      function getIteratorFn(maybeIterable) {
        if (maybeIterable === null || typeof maybeIterable === "undefined") {
          return null;
        }

        var maybeIterator = MAYBE_ITERATOR_SYMBOL && maybeIterable[MAYBE_ITERATOR_SYMBOL] || maybeIterable[FAUX_ITERATOR_SYMBOL];

        if (typeof maybeIterator === "function") {
          return maybeIterator;
        }

        return null;
      }

      function _createPortal(children, containerInfo, implementation) {
        var key = arguments.length > 3 && arguments[3] !== undefined ? arguments[3] : null;
        return {
          $$typeof: REACT_PORTAL_TYPE,
          key: key == null ? null : "" + key,
          children: children,
          containerInfo: containerInfo,
          implementation: implementation
        };
      }

      var TouchHistoryMath = {
        centroidDimension: function centroidDimension(touchHistory, touchesChangedAfter, isXAxis, ofCurrent) {
          var touchBank = touchHistory.touchBank;
          var total = 0;
          var count = 0;
          var oneTouchData = touchHistory.numberActiveTouches === 1 ? touchHistory.touchBank[touchHistory.indexOfSingleActiveTouch] : null;

          if (oneTouchData !== null) {
            if (oneTouchData.touchActive && oneTouchData.currentTimeStamp > touchesChangedAfter) {
              total += ofCurrent && isXAxis ? oneTouchData.currentPageX : ofCurrent && !isXAxis ? oneTouchData.currentPageY : !ofCurrent && isXAxis ? oneTouchData.previousPageX : oneTouchData.previousPageY;
              count = 1;
            }
          } else {
            for (var i = 0; i < touchBank.length; i++) {
              var touchTrack = touchBank[i];

              if (touchTrack !== null && touchTrack !== undefined && touchTrack.touchActive && touchTrack.currentTimeStamp >= touchesChangedAfter) {
                var toAdd = void 0;

                if (ofCurrent && isXAxis) {
                  toAdd = touchTrack.currentPageX;
                } else if (ofCurrent && !isXAxis) {
                  toAdd = touchTrack.currentPageY;
                } else if (!ofCurrent && isXAxis) {
                  toAdd = touchTrack.previousPageX;
                } else {
                  toAdd = touchTrack.previousPageY;
                }

                total += toAdd;
                count++;
              }
            }
          }

          return count > 0 ? total / count : TouchHistoryMath.noCentroid;
        },
        currentCentroidXOfTouchesChangedAfter: function currentCentroidXOfTouchesChangedAfter(touchHistory, touchesChangedAfter) {
          return TouchHistoryMath.centroidDimension(touchHistory, touchesChangedAfter, true, true);
        },
        currentCentroidYOfTouchesChangedAfter: function currentCentroidYOfTouchesChangedAfter(touchHistory, touchesChangedAfter) {
          return TouchHistoryMath.centroidDimension(touchHistory, touchesChangedAfter, false, true);
        },
        previousCentroidXOfTouchesChangedAfter: function previousCentroidXOfTouchesChangedAfter(touchHistory, touchesChangedAfter) {
          return TouchHistoryMath.centroidDimension(touchHistory, touchesChangedAfter, true, false);
        },
        previousCentroidYOfTouchesChangedAfter: function previousCentroidYOfTouchesChangedAfter(touchHistory, touchesChangedAfter) {
          return TouchHistoryMath.centroidDimension(touchHistory, touchesChangedAfter, false, false);
        },
        currentCentroidX: function currentCentroidX(touchHistory) {
          return TouchHistoryMath.centroidDimension(touchHistory, 0, true, true);
        },
        currentCentroidY: function currentCentroidY(touchHistory) {
          return TouchHistoryMath.centroidDimension(touchHistory, 0, false, true);
        },
        noCentroid: -1
      };
      var ReactVersion = "16.3.1";

      function _classCallCheck(instance, Constructor) {
        if (!(instance instanceof Constructor)) {
          throw new TypeError("Cannot call a class as a function");
        }
      }

      var objects = {};
      var uniqueID = 1;
      var emptyObject$2 = {};

      var ReactNativePropRegistry = function () {
        function ReactNativePropRegistry() {
          _classCallCheck(this, ReactNativePropRegistry);
        }

        ReactNativePropRegistry.register = function register(object) {
          var id = ++uniqueID;
          {
            Object.freeze(object);
          }
          objects[id] = object;
          return id;
        };

        ReactNativePropRegistry.getByID = function getByID(id) {
          if (!id) {
            return emptyObject$2;
          }

          var object = objects[id];

          if (!object) {
            console.warn("Invalid style with id `" + id + "`. Skipping ...");
            return emptyObject$2;
          }

          return object;
        };

        return ReactNativePropRegistry;
      }();

      var emptyObject$1 = {};
      var removedKeys = null;
      var removedKeyCount = 0;

      function defaultDiffer(prevProp, nextProp) {
        if (typeof nextProp !== "object" || nextProp === null) {
          return true;
        } else {
          return deepDiffer(prevProp, nextProp);
        }
      }

      function resolveObject(idOrObject) {
        if (typeof idOrObject === "number") {
          return ReactNativePropRegistry.getByID(idOrObject);
        }

        return idOrObject;
      }

      function restoreDeletedValuesInNestedArray(updatePayload, node, validAttributes) {
        if (Array.isArray(node)) {
          var i = node.length;

          while (i-- && removedKeyCount > 0) {
            restoreDeletedValuesInNestedArray(updatePayload, node[i], validAttributes);
          }
        } else if (node && removedKeyCount > 0) {
          var obj = resolveObject(node);

          for (var propKey in removedKeys) {
            if (!removedKeys[propKey]) {
              continue;
            }

            var _nextProp = obj[propKey];

            if (_nextProp === undefined) {
              continue;
            }

            var attributeConfig = validAttributes[propKey];

            if (!attributeConfig) {
              continue;
            }

            if (typeof _nextProp === "function") {
              _nextProp = true;
            }

            if (typeof _nextProp === "undefined") {
              _nextProp = null;
            }

            if (typeof attributeConfig !== "object") {
              updatePayload[propKey] = _nextProp;
            } else if (typeof attributeConfig.diff === "function" || typeof attributeConfig.process === "function") {
              var nextValue = typeof attributeConfig.process === "function" ? attributeConfig.process(_nextProp) : _nextProp;
              updatePayload[propKey] = nextValue;
            }

            removedKeys[propKey] = false;
            removedKeyCount--;
          }
        }
      }

      function diffNestedArrayProperty(updatePayload, prevArray, nextArray, validAttributes) {
        var minLength = prevArray.length < nextArray.length ? prevArray.length : nextArray.length;
        var i = void 0;

        for (i = 0; i < minLength; i++) {
          updatePayload = diffNestedProperty(updatePayload, prevArray[i], nextArray[i], validAttributes);
        }

        for (; i < prevArray.length; i++) {
          updatePayload = clearNestedProperty(updatePayload, prevArray[i], validAttributes);
        }

        for (; i < nextArray.length; i++) {
          updatePayload = addNestedProperty(updatePayload, nextArray[i], validAttributes);
        }

        return updatePayload;
      }

      function diffNestedProperty(updatePayload, prevProp, nextProp, validAttributes) {
        if (!updatePayload && prevProp === nextProp) {
          return updatePayload;
        }

        if (!prevProp || !nextProp) {
          if (nextProp) {
            return addNestedProperty(updatePayload, nextProp, validAttributes);
          }

          if (prevProp) {
            return clearNestedProperty(updatePayload, prevProp, validAttributes);
          }

          return updatePayload;
        }

        if (!Array.isArray(prevProp) && !Array.isArray(nextProp)) {
          return diffProperties(updatePayload, resolveObject(prevProp), resolveObject(nextProp), validAttributes);
        }

        if (Array.isArray(prevProp) && Array.isArray(nextProp)) {
          return diffNestedArrayProperty(updatePayload, prevProp, nextProp, validAttributes);
        }

        if (Array.isArray(prevProp)) {
          return diffProperties(updatePayload, flattenStyle(prevProp), resolveObject(nextProp), validAttributes);
        }

        return diffProperties(updatePayload, resolveObject(prevProp), flattenStyle(nextProp), validAttributes);
      }

      function addNestedProperty(updatePayload, nextProp, validAttributes) {
        if (!nextProp) {
          return updatePayload;
        }

        if (!Array.isArray(nextProp)) {
          return addProperties(updatePayload, resolveObject(nextProp), validAttributes);
        }

        for (var i = 0; i < nextProp.length; i++) {
          updatePayload = addNestedProperty(updatePayload, nextProp[i], validAttributes);
        }

        return updatePayload;
      }

      function clearNestedProperty(updatePayload, prevProp, validAttributes) {
        if (!prevProp) {
          return updatePayload;
        }

        if (!Array.isArray(prevProp)) {
          return clearProperties(updatePayload, resolveObject(prevProp), validAttributes);
        }

        for (var i = 0; i < prevProp.length; i++) {
          updatePayload = clearNestedProperty(updatePayload, prevProp[i], validAttributes);
        }

        return updatePayload;
      }

      function diffProperties(updatePayload, prevProps, nextProps, validAttributes) {
        var attributeConfig = void 0;
        var nextProp = void 0;
        var prevProp = void 0;

        for (var propKey in nextProps) {
          attributeConfig = validAttributes[propKey];

          if (!attributeConfig) {
            continue;
          }

          prevProp = prevProps[propKey];
          nextProp = nextProps[propKey];

          if (typeof nextProp === "function") {
            nextProp = true;

            if (typeof prevProp === "function") {
              prevProp = true;
            }
          }

          if (typeof nextProp === "undefined") {
            nextProp = null;

            if (typeof prevProp === "undefined") {
              prevProp = null;
            }
          }

          if (removedKeys) {
            removedKeys[propKey] = false;
          }

          if (updatePayload && updatePayload[propKey] !== undefined) {
            if (typeof attributeConfig !== "object") {
              updatePayload[propKey] = nextProp;
            } else if (typeof attributeConfig.diff === "function" || typeof attributeConfig.process === "function") {
              var nextValue = typeof attributeConfig.process === "function" ? attributeConfig.process(nextProp) : nextProp;
              updatePayload[propKey] = nextValue;
            }

            continue;
          }

          if (prevProp === nextProp) {
            continue;
          }

          if (typeof attributeConfig !== "object") {
            if (defaultDiffer(prevProp, nextProp)) {
              (updatePayload || (updatePayload = {}))[propKey] = nextProp;
            }
          } else if (typeof attributeConfig.diff === "function" || typeof attributeConfig.process === "function") {
            var shouldUpdate = prevProp === undefined || (typeof attributeConfig.diff === "function" ? attributeConfig.diff(prevProp, nextProp) : defaultDiffer(prevProp, nextProp));

            if (shouldUpdate) {
              var _nextValue = typeof attributeConfig.process === "function" ? attributeConfig.process(nextProp) : nextProp;

              (updatePayload || (updatePayload = {}))[propKey] = _nextValue;
            }
          } else {
            removedKeys = null;
            removedKeyCount = 0;
            updatePayload = diffNestedProperty(updatePayload, prevProp, nextProp, attributeConfig);

            if (removedKeyCount > 0 && updatePayload) {
              restoreDeletedValuesInNestedArray(updatePayload, nextProp, attributeConfig);
              removedKeys = null;
            }
          }
        }

        for (var _propKey in prevProps) {
          if (nextProps[_propKey] !== undefined) {
            continue;
          }

          attributeConfig = validAttributes[_propKey];

          if (!attributeConfig) {
            continue;
          }

          if (updatePayload && updatePayload[_propKey] !== undefined) {
            continue;
          }

          prevProp = prevProps[_propKey];

          if (prevProp === undefined) {
            continue;
          }

          if (typeof attributeConfig !== "object" || typeof attributeConfig.diff === "function" || typeof attributeConfig.process === "function") {
            (updatePayload || (updatePayload = {}))[_propKey] = null;

            if (!removedKeys) {
              removedKeys = {};
            }

            if (!removedKeys[_propKey]) {
              removedKeys[_propKey] = true;
              removedKeyCount++;
            }
          } else {
            updatePayload = clearNestedProperty(updatePayload, prevProp, attributeConfig);
          }
        }

        return updatePayload;
      }

      function addProperties(updatePayload, props, validAttributes) {
        return diffProperties(updatePayload, emptyObject$1, props, validAttributes);
      }

      function clearProperties(updatePayload, prevProps, validAttributes) {
        return diffProperties(updatePayload, prevProps, emptyObject$1, validAttributes);
      }

      function create(props, validAttributes) {
        return addProperties(null, props, validAttributes);
      }

      function diff(prevProps, nextProps, validAttributes) {
        return diffProperties(null, prevProps, nextProps, validAttributes);
      }

      function mountSafeCallback(context, callback) {
        return function () {
          if (!callback) {
            return undefined;
          }

          if (typeof context.__isMounted === "boolean") {
            if (!context.__isMounted) {
              return undefined;
            }
          } else if (typeof context.isMounted === "function") {
            if (!context.isMounted()) {
              return undefined;
            }
          }

          return callback.apply(context, arguments);
        };
      }

      function throwOnStylesProp(component, props) {
        if (props.styles !== undefined) {
          var owner = component._owner || null;
          var name = component.constructor.displayName;
          var msg = "`styles` is not a supported property of `" + name + "`, did " + "you mean `style` (singular)?";

          if (owner && owner.constructor && owner.constructor.displayName) {
            msg += "\n\nCheck the `" + owner.constructor.displayName + "` parent " + " component.";
          }

          throw new Error(msg);
        }
      }

      function warnForStyleProps(props, validAttributes) {
        for (var key in validAttributes.style) {
          if (!(validAttributes[key] || props[key] === undefined)) {
            console.error("You are setting the style `{ " + key + ": ... }` as a prop. You " + "should nest it in a style object. " + "E.g. `{ style: { " + key + ": ... } }`");
          }
        }
      }

      function get(key) {
        return key._reactInternalFiber;
      }

      function set(key, value) {
        key._reactInternalFiber = value;
      }

      var ReactInternals = React.__SECRET_INTERNALS_DO_NOT_USE_OR_YOU_WILL_BE_FIRED;
      var ReactCurrentOwner = ReactInternals.ReactCurrentOwner;
      var ReactDebugCurrentFrame = ReactInternals.ReactDebugCurrentFrame;

      function getComponentName(fiber) {
        var type = fiber.type;

        if (typeof type === "function") {
          return type.displayName || type.name;
        }

        if (typeof type === "string") {
          return type;
        }

        switch (type) {
          case REACT_FRAGMENT_TYPE:
            return "ReactFragment";

          case REACT_PORTAL_TYPE:
            return "ReactPortal";

          case REACT_CALL_TYPE:
            return "ReactCall";

          case REACT_RETURN_TYPE:
            return "ReactReturn";
        }

        return null;
      }

      var findHostInstance = function findHostInstance(fiber) {
        return null;
      };

      var findHostInstanceFabric = function findHostInstanceFabric(fiber) {
        return null;
      };

      function injectFindHostInstance(impl) {
        findHostInstance = impl;
      }

      function findNodeHandle(componentOrHandle) {
        {
          var owner = ReactCurrentOwner.current;

          if (owner !== null && owner.stateNode !== null) {
            !owner.stateNode._warnedAboutRefsInRender ? warning(false, "%s is accessing findNodeHandle inside its render(). " + "render() should be a pure function of props and state. It should " + "never access something that requires stale data from the previous " + "render, such as refs. Move this logic to componentDidMount and " + "componentDidUpdate instead.", getComponentName(owner) || "A component") : void 0;
            owner.stateNode._warnedAboutRefsInRender = true;
          }
        }

        if (componentOrHandle == null) {
          return null;
        }

        if (typeof componentOrHandle === "number") {
          return componentOrHandle;
        }

        var component = componentOrHandle;
        var internalInstance = get(component);

        if (internalInstance) {
          return findHostInstance(internalInstance) || findHostInstanceFabric(internalInstance);
        } else {
          if (component) {
            return component;
          } else {
            invariant(typeof component === "object" && "_nativeTag" in component || component.render != null && typeof component.render === "function", "findNodeHandle(...): Argument is not a component " + "(type: %s, keys: %s)", typeof component, Object.keys(component));
            invariant(false, "findNodeHandle(...): Unable to find node handle for unmounted " + "component.");
          }
        }
      }

      function findNumericNodeHandleFiber(componentOrHandle) {
        var instance = findNodeHandle(componentOrHandle);

        if (instance == null || typeof instance === "number") {
          return instance;
        }

        return instance._nativeTag;
      }

      var NativeMethodsMixin = {
        measure: function measure(callback) {
          UIManager.measure(findNumericNodeHandleFiber(this), mountSafeCallback(this, callback));
        },
        measureInWindow: function measureInWindow(callback) {
          UIManager.measureInWindow(findNumericNodeHandleFiber(this), mountSafeCallback(this, callback));
        },
        measureLayout: function measureLayout(relativeToNativeNode, onSuccess, onFail) {
          UIManager.measureLayout(findNumericNodeHandleFiber(this), relativeToNativeNode, mountSafeCallback(this, onFail), mountSafeCallback(this, onSuccess));
        },
        setNativeProps: function setNativeProps(nativeProps) {
          var maybeInstance = void 0;

          try {
            maybeInstance = findNodeHandle(this);
          } catch (error) {}

          if (maybeInstance == null) {
            return;
          }

          var viewConfig = maybeInstance.viewConfig;
          {
            warnForStyleProps(nativeProps, viewConfig.validAttributes);
          }
          var updatePayload = create(nativeProps, viewConfig.validAttributes);

          if (updatePayload != null) {
            UIManager.updateView(maybeInstance._nativeTag, viewConfig.uiViewClassName, updatePayload);
          }
        },
        focus: function focus() {
          TextInputState.focusTextInput(findNumericNodeHandleFiber(this));
        },
        blur: function blur() {
          TextInputState.blurTextInput(findNumericNodeHandleFiber(this));
        }
      };
      {
        var NativeMethodsMixin_DEV = NativeMethodsMixin;
        invariant(!NativeMethodsMixin_DEV.componentWillMount && !NativeMethodsMixin_DEV.componentWillReceiveProps && !NativeMethodsMixin_DEV.UNSAFE_componentWillMount && !NativeMethodsMixin_DEV.UNSAFE_componentWillReceiveProps, "Do not override existing functions.");

        NativeMethodsMixin_DEV.componentWillMount = function () {
          throwOnStylesProp(this, this.props);
        };

        NativeMethodsMixin_DEV.componentWillReceiveProps = function (newProps) {
          throwOnStylesProp(this, newProps);
        };

        NativeMethodsMixin_DEV.UNSAFE_componentWillMount = function () {
          throwOnStylesProp(this, this.props);
        };

        NativeMethodsMixin_DEV.UNSAFE_componentWillReceiveProps = function (newProps) {
          throwOnStylesProp(this, newProps);
        };

        NativeMethodsMixin_DEV.componentWillMount.__suppressDeprecationWarning = true;
        NativeMethodsMixin_DEV.componentWillReceiveProps.__suppressDeprecationWarning = true;
      }

      function _classCallCheck$1(instance, Constructor) {
        if (!(instance instanceof Constructor)) {
          throw new TypeError("Cannot call a class as a function");
        }
      }

      function _possibleConstructorReturn(self, call) {
        if (!self) {
          throw new ReferenceError("this hasn't been initialised - super() hasn't been called");
        }

        return call && (typeof call === "object" || typeof call === "function") ? call : self;
      }

      function _inherits(subClass, superClass) {
        if (typeof superClass !== "function" && superClass !== null) {
          throw new TypeError("Super expression must either be null or a function, not " + typeof superClass);
        }

        subClass.prototype = Object.create(superClass && superClass.prototype, {
          constructor: {
            value: subClass,
            enumerable: false,
            writable: true,
            configurable: true
          }
        });
        if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass;
      }

      var ReactNativeComponent = function (_React$Component) {
        _inherits(ReactNativeComponent, _React$Component);

        function ReactNativeComponent() {
          _classCallCheck$1(this, ReactNativeComponent);

          return _possibleConstructorReturn(this, _React$Component.apply(this, arguments));
        }

        ReactNativeComponent.prototype.blur = function blur() {
          TextInputState.blurTextInput(findNumericNodeHandleFiber(this));
        };

        ReactNativeComponent.prototype.focus = function focus() {
          TextInputState.focusTextInput(findNumericNodeHandleFiber(this));
        };

        ReactNativeComponent.prototype.measure = function measure(callback) {
          UIManager.measure(findNumericNodeHandleFiber(this), mountSafeCallback(this, callback));
        };

        ReactNativeComponent.prototype.measureInWindow = function measureInWindow(callback) {
          UIManager.measureInWindow(findNumericNodeHandleFiber(this), mountSafeCallback(this, callback));
        };

        ReactNativeComponent.prototype.measureLayout = function measureLayout(relativeToNativeNode, onSuccess, onFail) {
          UIManager.measureLayout(findNumericNodeHandleFiber(this), relativeToNativeNode, mountSafeCallback(this, onFail), mountSafeCallback(this, onSuccess));
        };

        ReactNativeComponent.prototype.setNativeProps = function setNativeProps(nativeProps) {
          var maybeInstance = void 0;

          try {
            maybeInstance = findNodeHandle(this);
          } catch (error) {}

          if (maybeInstance == null) {
            return;
          }

          var viewConfig = maybeInstance.viewConfig || maybeInstance.canonical.viewConfig;
          var updatePayload = create(nativeProps, viewConfig.validAttributes);

          if (updatePayload != null) {
            UIManager.updateView(maybeInstance._nativeTag, viewConfig.uiViewClassName, updatePayload);
          }
        };

        return ReactNativeComponent;
      }(React.Component);

      var NoEffect = 0;
      var PerformedWork = 1;
      var Placement = 2;
      var Update = 4;
      var PlacementAndUpdate = 6;
      var Deletion = 8;
      var ContentReset = 16;
      var Callback = 32;
      var DidCapture = 64;
      var Ref = 128;
      var ErrLog = 256;
      var Snapshot = 2048;
      var HostEffectMask = 2559;
      var Incomplete = 512;
      var ShouldCapture = 1024;
      var MOUNTING = 1;
      var MOUNTED = 2;
      var UNMOUNTED = 3;

      function isFiberMountedImpl(fiber) {
        var node = fiber;

        if (!fiber.alternate) {
          if ((node.effectTag & Placement) !== NoEffect) {
            return MOUNTING;
          }

          while (node["return"]) {
            node = node["return"];

            if ((node.effectTag & Placement) !== NoEffect) {
              return MOUNTING;
            }
          }
        } else {
          while (node["return"]) {
            node = node["return"];
          }
        }

        if (node.tag === HostRoot) {
          return MOUNTED;
        }

        return UNMOUNTED;
      }

      function isFiberMounted(fiber) {
        return isFiberMountedImpl(fiber) === MOUNTED;
      }

      function isMounted(component) {
        {
          var owner = ReactCurrentOwner.current;

          if (owner !== null && owner.tag === ClassComponent) {
            var ownerFiber = owner;
            var instance = ownerFiber.stateNode;
            !instance._warnedAboutRefsInRender ? warning(false, "%s is accessing isMounted inside its render() function. " + "render() should be a pure function of props and state. It should " + "never access something that requires stale data from the previous " + "render, such as refs. Move this logic to componentDidMount and " + "componentDidUpdate instead.", getComponentName(ownerFiber) || "A component") : void 0;
            instance._warnedAboutRefsInRender = true;
          }
        }
        var fiber = get(component);

        if (!fiber) {
          return false;
        }

        return isFiberMountedImpl(fiber) === MOUNTED;
      }

      function assertIsMounted(fiber) {
        invariant(isFiberMountedImpl(fiber) === MOUNTED, "Unable to find node on an unmounted component.");
      }

      function findCurrentFiberUsingSlowPath(fiber) {
        var alternate = fiber.alternate;

        if (!alternate) {
          var state = isFiberMountedImpl(fiber);
          invariant(state !== UNMOUNTED, "Unable to find node on an unmounted component.");

          if (state === MOUNTING) {
            return null;
          }

          return fiber;
        }

        var a = fiber;
        var b = alternate;

        while (true) {
          var parentA = a["return"];
          var parentB = parentA ? parentA.alternate : null;

          if (!parentA || !parentB) {
            break;
          }

          if (parentA.child === parentB.child) {
            var child = parentA.child;

            while (child) {
              if (child === a) {
                assertIsMounted(parentA);
                return fiber;
              }

              if (child === b) {
                assertIsMounted(parentA);
                return alternate;
              }

              child = child.sibling;
            }

            invariant(false, "Unable to find node on an unmounted component.");
          }

          if (a["return"] !== b["return"]) {
            a = parentA;
            b = parentB;
          } else {
            var didFindChild = false;
            var _child = parentA.child;

            while (_child) {
              if (_child === a) {
                didFindChild = true;
                a = parentA;
                b = parentB;
                break;
              }

              if (_child === b) {
                didFindChild = true;
                b = parentA;
                a = parentB;
                break;
              }

              _child = _child.sibling;
            }

            if (!didFindChild) {
              _child = parentB.child;

              while (_child) {
                if (_child === a) {
                  didFindChild = true;
                  a = parentB;
                  b = parentA;
                  break;
                }

                if (_child === b) {
                  didFindChild = true;
                  b = parentB;
                  a = parentA;
                  break;
                }

                _child = _child.sibling;
              }

              invariant(didFindChild, "Child was not found in either parent set. This indicates a bug " + "in React related to the return pointer. Please file an issue.");
            }
          }

          invariant(a.alternate === b, "Return fibers should always be each others' alternates. " + "This error is likely caused by a bug in React. Please file an issue.");
        }

        invariant(a.tag === HostRoot, "Unable to find node on an unmounted component.");

        if (a.stateNode.current === a) {
          return fiber;
        }

        return alternate;
      }

      function findCurrentHostFiber(parent) {
        var currentParent = findCurrentFiberUsingSlowPath(parent);

        if (!currentParent) {
          return null;
        }

        var node = currentParent;

        while (true) {
          if (node.tag === HostComponent || node.tag === HostText) {
            return node;
          } else if (node.child) {
            node.child["return"] = node;
            node = node.child;
            continue;
          }

          if (node === currentParent) {
            return null;
          }

          while (!node.sibling) {
            if (!node["return"] || node["return"] === currentParent) {
              return null;
            }

            node = node["return"];
          }

          node.sibling["return"] = node["return"];
          node = node.sibling;
        }

        return null;
      }

      function findCurrentHostFiberWithNoPortals(parent) {
        var currentParent = findCurrentFiberUsingSlowPath(parent);

        if (!currentParent) {
          return null;
        }

        var node = currentParent;

        while (true) {
          if (node.tag === HostComponent || node.tag === HostText) {
            return node;
          } else if (node.child && node.tag !== HostPortal) {
            node.child["return"] = node;
            node = node.child;
            continue;
          }

          if (node === currentParent) {
            return null;
          }

          while (!node.sibling) {
            if (!node["return"] || node["return"] === currentParent) {
              return null;
            }

            node = node["return"];
          }

          node.sibling["return"] = node["return"];
          node = node.sibling;
        }

        return null;
      }

      var MAX_SIGNED_31_BIT_INT = 1073741823;
      var NoWork = 0;
      var Sync = 1;
      var Never = MAX_SIGNED_31_BIT_INT;
      var UNIT_SIZE = 10;
      var MAGIC_NUMBER_OFFSET = 2;

      function msToExpirationTime(ms) {
        return (ms / UNIT_SIZE | 0) + MAGIC_NUMBER_OFFSET;
      }

      function expirationTimeToMs(expirationTime) {
        return (expirationTime - MAGIC_NUMBER_OFFSET) * UNIT_SIZE;
      }

      function ceiling(num, precision) {
        return ((num / precision | 0) + 1) * precision;
      }

      function computeExpirationBucket(currentTime, expirationInMs, bucketSizeMs) {
        return ceiling(currentTime + expirationInMs / UNIT_SIZE, bucketSizeMs / UNIT_SIZE);
      }

      var NoContext = 0;
      var AsyncMode = 1;
      var StrictMode = 2;
      var hasBadMapPolyfill = void 0;
      {
        hasBadMapPolyfill = false;

        try {
          var nonExtensibleObject = Object.preventExtensions({});
          var testMap = new Map([[nonExtensibleObject, null]]);
          var testSet = new Set([nonExtensibleObject]);
          testMap.set(0, 0);
          testSet.add(0);
        } catch (e) {
          hasBadMapPolyfill = true;
        }
      }
      var debugCounter = void 0;
      {
        debugCounter = 1;
      }

      function FiberNode(tag, pendingProps, key, mode) {
        this.tag = tag;
        this.key = key;
        this.type = null;
        this.stateNode = null;
        this["return"] = null;
        this.child = null;
        this.sibling = null;
        this.index = 0;
        this.ref = null;
        this.pendingProps = pendingProps;
        this.memoizedProps = null;
        this.updateQueue = null;
        this.memoizedState = null;
        this.mode = mode;
        this.effectTag = NoEffect;
        this.nextEffect = null;
        this.firstEffect = null;
        this.lastEffect = null;
        this.expirationTime = NoWork;
        this.alternate = null;
        {
          this._debugID = debugCounter++;
          this._debugSource = null;
          this._debugOwner = null;
          this._debugIsCurrentlyTiming = false;

          if (!hasBadMapPolyfill && typeof Object.preventExtensions === "function") {
            Object.preventExtensions(this);
          }
        }
      }

      var createFiber = function createFiber(tag, pendingProps, key, mode) {
        return new FiberNode(tag, pendingProps, key, mode);
      };

      function shouldConstruct(Component) {
        return !!(Component.prototype && Component.prototype.isReactComponent);
      }

      function createWorkInProgress(current, pendingProps, expirationTime) {
        var workInProgress = current.alternate;

        if (workInProgress === null) {
          workInProgress = createFiber(current.tag, pendingProps, current.key, current.mode);
          workInProgress.type = current.type;
          workInProgress.stateNode = current.stateNode;
          {
            workInProgress._debugID = current._debugID;
            workInProgress._debugSource = current._debugSource;
            workInProgress._debugOwner = current._debugOwner;
          }
          workInProgress.alternate = current;
          current.alternate = workInProgress;
        } else {
          workInProgress.pendingProps = pendingProps;
          workInProgress.effectTag = NoEffect;
          workInProgress.nextEffect = null;
          workInProgress.firstEffect = null;
          workInProgress.lastEffect = null;
        }

        workInProgress.expirationTime = expirationTime;
        workInProgress.child = current.child;
        workInProgress.memoizedProps = current.memoizedProps;
        workInProgress.memoizedState = current.memoizedState;
        workInProgress.updateQueue = current.updateQueue;
        workInProgress.sibling = current.sibling;
        workInProgress.index = current.index;
        workInProgress.ref = current.ref;
        return workInProgress;
      }

      function createHostRootFiber(isAsync) {
        var mode = isAsync ? AsyncMode | StrictMode : NoContext;
        return createFiber(HostRoot, null, null, mode);
      }

      function createFiberFromElement(element, mode, expirationTime) {
        var owner = null;
        {
          owner = element._owner;
        }
        var fiber = void 0;
        var type = element.type;
        var key = element.key;
        var pendingProps = element.props;
        var fiberTag = void 0;

        if (typeof type === "function") {
          fiberTag = shouldConstruct(type) ? ClassComponent : IndeterminateComponent;
        } else if (typeof type === "string") {
          fiberTag = HostComponent;
        } else {
          switch (type) {
            case REACT_FRAGMENT_TYPE:
              return createFiberFromFragment(pendingProps.children, mode, expirationTime, key);

            case REACT_ASYNC_MODE_TYPE:
              fiberTag = Mode;
              mode |= AsyncMode | StrictMode;
              break;

            case REACT_STRICT_MODE_TYPE:
              fiberTag = Mode;
              mode |= StrictMode;
              break;

            case REACT_CALL_TYPE:
              fiberTag = CallComponent;
              break;

            case REACT_RETURN_TYPE:
              fiberTag = ReturnComponent;
              break;

            default:
              {
                if (typeof type === "object" && type !== null) {
                  switch (type.$$typeof) {
                    case REACT_PROVIDER_TYPE:
                      fiberTag = ContextProvider;
                      break;

                    case REACT_CONTEXT_TYPE:
                      fiberTag = ContextConsumer;
                      break;

                    case REACT_FORWARD_REF_TYPE:
                      fiberTag = ForwardRef;
                      break;

                    default:
                      if (typeof type.tag === "number") {
                        fiber = type;
                        fiber.pendingProps = pendingProps;
                        fiber.expirationTime = expirationTime;
                        return fiber;
                      } else {
                        throwOnInvalidElementType(type, owner);
                      }

                      break;
                  }
                } else {
                  throwOnInvalidElementType(type, owner);
                }
              }
          }
        }

        fiber = createFiber(fiberTag, pendingProps, key, mode);
        fiber.type = type;
        fiber.expirationTime = expirationTime;
        {
          fiber._debugSource = element._source;
          fiber._debugOwner = element._owner;
        }
        return fiber;
      }

      function throwOnInvalidElementType(type, owner) {
        var info = "";
        {
          if (type === undefined || typeof type === "object" && type !== null && Object.keys(type).length === 0) {
            info += " You likely forgot to export your component from the file " + "it's defined in, or you might have mixed up default and " + "named imports.";
          }

          var ownerName = owner ? getComponentName(owner) : null;

          if (ownerName) {
            info += "\n\nCheck the render method of `" + ownerName + "`.";
          }
        }
        invariant(false, "Element type is invalid: expected a string (for built-in " + "components) or a class/function (for composite components) " + "but got: %s.%s", type == null ? type : typeof type, info);
      }

      function createFiberFromFragment(elements, mode, expirationTime, key) {
        var fiber = createFiber(Fragment, elements, key, mode);
        fiber.expirationTime = expirationTime;
        return fiber;
      }

      function createFiberFromText(content, mode, expirationTime) {
        var fiber = createFiber(HostText, content, null, mode);
        fiber.expirationTime = expirationTime;
        return fiber;
      }

      function createFiberFromHostInstanceForDeletion() {
        var fiber = createFiber(HostComponent, null, null, NoContext);
        fiber.type = "DELETED";
        return fiber;
      }

      function createFiberFromPortal(portal, mode, expirationTime) {
        var pendingProps = portal.children !== null ? portal.children : [];
        var fiber = createFiber(HostPortal, pendingProps, portal.key, mode);
        fiber.expirationTime = expirationTime;
        fiber.stateNode = {
          containerInfo: portal.containerInfo,
          pendingChildren: null,
          implementation: portal.implementation
        };
        return fiber;
      }

      function assignFiberPropertiesInDEV(target, source) {
        if (target === null) {
          target = createFiber(IndeterminateComponent, null, null, NoContext);
        }

        target.tag = source.tag;
        target.key = source.key;
        target.type = source.type;
        target.stateNode = source.stateNode;
        target["return"] = source["return"];
        target.child = source.child;
        target.sibling = source.sibling;
        target.index = source.index;
        target.ref = source.ref;
        target.pendingProps = source.pendingProps;
        target.memoizedProps = source.memoizedProps;
        target.updateQueue = source.updateQueue;
        target.memoizedState = source.memoizedState;
        target.mode = source.mode;
        target.effectTag = source.effectTag;
        target.nextEffect = source.nextEffect;
        target.firstEffect = source.firstEffect;
        target.lastEffect = source.lastEffect;
        target.expirationTime = source.expirationTime;
        target.alternate = source.alternate;
        target._debugID = source._debugID;
        target._debugSource = source._debugSource;
        target._debugOwner = source._debugOwner;
        target._debugIsCurrentlyTiming = source._debugIsCurrentlyTiming;
        return target;
      }

      function createFiberRoot(containerInfo, isAsync, hydrate) {
        var uninitializedFiber = createHostRootFiber(isAsync);
        var root = {
          current: uninitializedFiber,
          containerInfo: containerInfo,
          pendingChildren: null,
          pendingCommitExpirationTime: NoWork,
          finishedWork: null,
          context: null,
          pendingContext: null,
          hydrate: hydrate,
          remainingExpirationTime: NoWork,
          firstBatch: null,
          nextScheduledRoot: null
        };
        uninitializedFiber.stateNode = root;
        return root;
      }

      var onCommitFiberRoot = null;
      var onCommitFiberUnmount = null;
      var hasLoggedError = false;

      function catchErrors(fn) {
        return function (arg) {
          try {
            return fn(arg);
          } catch (err) {
            if (true && !hasLoggedError) {
              hasLoggedError = true;
              warning(false, "React DevTools encountered an error: %s", err);
            }
          }
        };
      }

      function injectInternals(internals) {
        if (typeof __REACT_DEVTOOLS_GLOBAL_HOOK__ === "undefined") {
          return false;
        }

        var hook = __REACT_DEVTOOLS_GLOBAL_HOOK__;

        if (hook.isDisabled) {
          return true;
        }

        if (!hook.supportsFiber) {
          {
            warning(false, "The installed version of React DevTools is too old and will not work " + "with the current version of React. Please update React DevTools. " + "https://fb.me/react-devtools");
          }
          return true;
        }

        try {
          var rendererID = hook.inject(internals);
          onCommitFiberRoot = catchErrors(function (root) {
            return hook.onCommitFiberRoot(rendererID, root);
          });
          onCommitFiberUnmount = catchErrors(function (fiber) {
            return hook.onCommitFiberUnmount(rendererID, fiber);
          });
        } catch (err) {
          {
            warning(false, "React DevTools encountered an error: %s.", err);
          }
        }

        return true;
      }

      function onCommitRoot(root) {
        if (typeof onCommitFiberRoot === "function") {
          onCommitFiberRoot(root);
        }
      }

      function onCommitUnmount(fiber) {
        if (typeof onCommitFiberUnmount === "function") {
          onCommitFiberUnmount(fiber);
        }
      }

      var describeComponentFrame = function describeComponentFrame(name, source, ownerName) {
        return "\n    in " + (name || "Unknown") + (source ? " (at " + source.fileName.replace(/^.*[\\\/]/, "") + ":" + source.lineNumber + ")" : ownerName ? " (created by " + ownerName + ")" : "");
      };

      function describeFiber(fiber) {
        switch (fiber.tag) {
          case IndeterminateComponent:
          case FunctionalComponent:
          case ClassComponent:
          case HostComponent:
            var owner = fiber._debugOwner;
            var source = fiber._debugSource;
            var name = getComponentName(fiber);
            var ownerName = null;

            if (owner) {
              ownerName = getComponentName(owner);
            }

            return describeComponentFrame(name, source, ownerName);

          default:
            return "";
        }
      }

      function getStackAddendumByWorkInProgressFiber(workInProgress) {
        var info = "";
        var node = workInProgress;

        do {
          info += describeFiber(node);
          node = node["return"];
        } while (node);

        return info;
      }

      var lowPriorityWarning = function lowPriorityWarning() {};

      {
        var printWarning = function printWarning(format) {
          for (var _len = arguments.length, args = Array(_len > 1 ? _len - 1 : 0), _key = 1; _key < _len; _key++) {
            args[_key - 1] = arguments[_key];
          }

          var argIndex = 0;
          var message = "Warning: " + format.replace(/%s/g, function () {
            return args[argIndex++];
          });

          if (typeof console !== "undefined") {
            console.warn(message);
          }

          try {
            throw new Error(message);
          } catch (x) {}
        };

        lowPriorityWarning = function lowPriorityWarning(condition, format) {
          if (format === undefined) {
            throw new Error("`warning(condition, format, ...args)` requires a warning " + "message argument");
          }

          if (!condition) {
            for (var _len2 = arguments.length, args = Array(_len2 > 2 ? _len2 - 2 : 0), _key2 = 2; _key2 < _len2; _key2++) {
              args[_key2 - 2] = arguments[_key2];
            }

            printWarning.apply(undefined, [format].concat(args));
          }
        };
      }
      var lowPriorityWarning$1 = lowPriorityWarning;
      var ReactStrictModeWarnings = {
        discardPendingWarnings: function discardPendingWarnings() {},
        flushPendingDeprecationWarnings: function flushPendingDeprecationWarnings() {},
        flushPendingUnsafeLifecycleWarnings: function flushPendingUnsafeLifecycleWarnings() {},
        recordDeprecationWarnings: function recordDeprecationWarnings(fiber, instance) {},
        recordUnsafeLifecycleWarnings: function recordUnsafeLifecycleWarnings(fiber, instance) {}
      };
      {
        var LIFECYCLE_SUGGESTIONS = {
          UNSAFE_componentWillMount: "componentDidMount",
          UNSAFE_componentWillReceiveProps: "static getDerivedStateFromProps",
          UNSAFE_componentWillUpdate: "componentDidUpdate"
        };
        var pendingComponentWillMountWarnings = [];
        var pendingComponentWillReceivePropsWarnings = [];
        var pendingComponentWillUpdateWarnings = [];
        var pendingUnsafeLifecycleWarnings = new Map();
        var didWarnAboutDeprecatedLifecycles = new Set();
        var didWarnAboutUnsafeLifecycles = new Set();

        ReactStrictModeWarnings.discardPendingWarnings = function () {
          pendingComponentWillMountWarnings = [];
          pendingComponentWillReceivePropsWarnings = [];
          pendingComponentWillUpdateWarnings = [];
          pendingUnsafeLifecycleWarnings = new Map();
        };

        ReactStrictModeWarnings.flushPendingUnsafeLifecycleWarnings = function () {
          pendingUnsafeLifecycleWarnings.forEach(function (lifecycleWarningsMap, strictRoot) {
            var lifecyclesWarningMesages = [];
            Object.keys(lifecycleWarningsMap).forEach(function (lifecycle) {
              var lifecycleWarnings = lifecycleWarningsMap[lifecycle];

              if (lifecycleWarnings.length > 0) {
                var componentNames = new Set();
                lifecycleWarnings.forEach(function (fiber) {
                  componentNames.add(getComponentName(fiber) || "Component");
                  didWarnAboutUnsafeLifecycles.add(fiber.type);
                });
                var formatted = lifecycle.replace("UNSAFE_", "");
                var suggestion = LIFECYCLE_SUGGESTIONS[lifecycle];
                var sortedComponentNames = Array.from(componentNames).sort().join(", ");
                lifecyclesWarningMesages.push(formatted + ": Please update the following components to use " + (suggestion + " instead: " + sortedComponentNames));
              }
            });

            if (lifecyclesWarningMesages.length > 0) {
              var strictRootComponentStack = getStackAddendumByWorkInProgressFiber(strictRoot);
              warning(false, "Unsafe lifecycle methods were found within a strict-mode tree:%s" + "\n\n%s" + "\n\nLearn more about this warning here:" + "\nhttps://fb.me/react-strict-mode-warnings", strictRootComponentStack, lifecyclesWarningMesages.join("\n\n"));
            }
          });
          pendingUnsafeLifecycleWarnings = new Map();
        };

        var getStrictRoot = function getStrictRoot(fiber) {
          var maybeStrictRoot = null;

          while (fiber !== null) {
            if (fiber.mode & StrictMode) {
              maybeStrictRoot = fiber;
            }

            fiber = fiber["return"];
          }

          return maybeStrictRoot;
        };

        ReactStrictModeWarnings.flushPendingDeprecationWarnings = function () {
          if (pendingComponentWillMountWarnings.length > 0) {
            var uniqueNames = new Set();
            pendingComponentWillMountWarnings.forEach(function (fiber) {
              uniqueNames.add(getComponentName(fiber) || "Component");
              didWarnAboutDeprecatedLifecycles.add(fiber.type);
            });
            var sortedNames = Array.from(uniqueNames).sort().join(", ");
            lowPriorityWarning$1(false, "componentWillMount is deprecated and will be removed in the next major version. " + "Use componentDidMount instead. As a temporary workaround, " + "you can rename to UNSAFE_componentWillMount." + "\n\nPlease update the following components: %s" + "\n\nLearn more about this warning here:" + "\nhttps://fb.me/react-async-component-lifecycle-hooks", sortedNames);
            pendingComponentWillMountWarnings = [];
          }

          if (pendingComponentWillReceivePropsWarnings.length > 0) {
            var _uniqueNames = new Set();

            pendingComponentWillReceivePropsWarnings.forEach(function (fiber) {
              _uniqueNames.add(getComponentName(fiber) || "Component");

              didWarnAboutDeprecatedLifecycles.add(fiber.type);
            });

            var _sortedNames = Array.from(_uniqueNames).sort().join(", ");

            lowPriorityWarning$1(false, "componentWillReceiveProps is deprecated and will be removed in the next major version. " + "Use static getDerivedStateFromProps instead." + "\n\nPlease update the following components: %s" + "\n\nLearn more about this warning here:" + "\nhttps://fb.me/react-async-component-lifecycle-hooks", _sortedNames);
            pendingComponentWillReceivePropsWarnings = [];
          }

          if (pendingComponentWillUpdateWarnings.length > 0) {
            var _uniqueNames2 = new Set();

            pendingComponentWillUpdateWarnings.forEach(function (fiber) {
              _uniqueNames2.add(getComponentName(fiber) || "Component");

              didWarnAboutDeprecatedLifecycles.add(fiber.type);
            });

            var _sortedNames2 = Array.from(_uniqueNames2).sort().join(", ");

            lowPriorityWarning$1(false, "componentWillUpdate is deprecated and will be removed in the next major version. " + "Use componentDidUpdate instead. As a temporary workaround, " + "you can rename to UNSAFE_componentWillUpdate." + "\n\nPlease update the following components: %s" + "\n\nLearn more about this warning here:" + "\nhttps://fb.me/react-async-component-lifecycle-hooks", _sortedNames2);
            pendingComponentWillUpdateWarnings = [];
          }
        };

        ReactStrictModeWarnings.recordDeprecationWarnings = function (fiber, instance) {
          if (didWarnAboutDeprecatedLifecycles.has(fiber.type)) {
            return;
          }

          if (typeof instance.componentWillMount === "function" && instance.componentWillMount.__suppressDeprecationWarning !== true) {
            pendingComponentWillMountWarnings.push(fiber);
          }

          if (typeof instance.componentWillReceiveProps === "function" && instance.componentWillReceiveProps.__suppressDeprecationWarning !== true) {
            pendingComponentWillReceivePropsWarnings.push(fiber);
          }

          if (typeof instance.componentWillUpdate === "function" && instance.componentWillUpdate.__suppressDeprecationWarning !== true) {
            pendingComponentWillUpdateWarnings.push(fiber);
          }
        };

        ReactStrictModeWarnings.recordUnsafeLifecycleWarnings = function (fiber, instance) {
          var strictRoot = getStrictRoot(fiber);

          if (didWarnAboutUnsafeLifecycles.has(fiber.type)) {
            return;
          }

          if (typeof instance.componentWillMount === "function" && instance.componentWillMount.__suppressDeprecationWarning === true) {
            return;
          }

          var warningsForRoot = void 0;

          if (!pendingUnsafeLifecycleWarnings.has(strictRoot)) {
            warningsForRoot = {
              UNSAFE_componentWillMount: [],
              UNSAFE_componentWillReceiveProps: [],
              UNSAFE_componentWillUpdate: []
            };
            pendingUnsafeLifecycleWarnings.set(strictRoot, warningsForRoot);
          } else {
            warningsForRoot = pendingUnsafeLifecycleWarnings.get(strictRoot);
          }

          var unsafeLifecycles = [];

          if (typeof instance.componentWillMount === "function" || typeof instance.UNSAFE_componentWillMount === "function") {
            unsafeLifecycles.push("UNSAFE_componentWillMount");
          }

          if (typeof instance.componentWillReceiveProps === "function" || typeof instance.UNSAFE_componentWillReceiveProps === "function") {
            unsafeLifecycles.push("UNSAFE_componentWillReceiveProps");
          }

          if (typeof instance.componentWillUpdate === "function" || typeof instance.UNSAFE_componentWillUpdate === "function") {
            unsafeLifecycles.push("UNSAFE_componentWillUpdate");
          }

          if (unsafeLifecycles.length > 0) {
            unsafeLifecycles.forEach(function (lifecycle) {
              warningsForRoot[lifecycle].push(fiber);
            });
          }
        };
      }

      var _require = _require2(_dependencyMap[15], "ReactFeatureFlags");

      var enableGetDerivedStateFromCatch = _require.enableGetDerivedStateFromCatch;
      var debugRenderPhaseSideEffects = _require.debugRenderPhaseSideEffects;
      var debugRenderPhaseSideEffectsForStrictMode = _require.debugRenderPhaseSideEffectsForStrictMode;
      var warnAboutDeprecatedLifecycles = _require.warnAboutDeprecatedLifecycles;
      var replayFailedUnitOfWorkWithInvokeGuardedCallback = _require.replayFailedUnitOfWorkWithInvokeGuardedCallback;
      var enableUserTimingAPI = true;
      var enableMutatingReconciler = true;
      var enableNoopReconciler = false;
      var enablePersistentReconciler = false;

      function getCurrentFiberOwnerName() {
        {
          var fiber = ReactDebugCurrentFiber.current;

          if (fiber === null) {
            return null;
          }

          var owner = fiber._debugOwner;

          if (owner !== null && typeof owner !== "undefined") {
            return getComponentName(owner);
          }
        }
        return null;
      }

      function getCurrentFiberStackAddendum() {
        {
          var fiber = ReactDebugCurrentFiber.current;

          if (fiber === null) {
            return null;
          }

          return getStackAddendumByWorkInProgressFiber(fiber);
        }
        return null;
      }

      function resetCurrentFiber() {
        ReactDebugCurrentFrame.getCurrentStack = null;
        ReactDebugCurrentFiber.current = null;
        ReactDebugCurrentFiber.phase = null;
      }

      function setCurrentFiber(fiber) {
        ReactDebugCurrentFrame.getCurrentStack = getCurrentFiberStackAddendum;
        ReactDebugCurrentFiber.current = fiber;
        ReactDebugCurrentFiber.phase = null;
      }

      function setCurrentPhase(phase) {
        ReactDebugCurrentFiber.phase = phase;
      }

      var ReactDebugCurrentFiber = {
        current: null,
        phase: null,
        resetCurrentFiber: resetCurrentFiber,
        setCurrentFiber: setCurrentFiber,
        setCurrentPhase: setCurrentPhase,
        getCurrentFiberOwnerName: getCurrentFiberOwnerName,
        getCurrentFiberStackAddendum: getCurrentFiberStackAddendum
      };
      var reactEmoji = "\u269B";
      var warningEmoji = "\u26D4";
      var supportsUserTiming = typeof performance !== "undefined" && typeof performance.mark === "function" && typeof performance.clearMarks === "function" && typeof performance.measure === "function" && typeof performance.clearMeasures === "function";
      var currentFiber = null;
      var currentPhase = null;
      var currentPhaseFiber = null;
      var isCommitting = false;
      var hasScheduledUpdateInCurrentCommit = false;
      var hasScheduledUpdateInCurrentPhase = false;
      var commitCountInCurrentWorkLoop = 0;
      var effectCountInCurrentCommit = 0;
      var isWaitingForCallback = false;
      var labelsInCurrentCommit = new Set();

      var formatMarkName = function formatMarkName(markName) {
        return reactEmoji + " " + markName;
      };

      var formatLabel = function formatLabel(label, warning$$1) {
        var prefix = warning$$1 ? warningEmoji + " " : reactEmoji + " ";
        var suffix = warning$$1 ? " Warning: " + warning$$1 : "";
        return "" + prefix + label + suffix;
      };

      var beginMark = function beginMark(markName) {
        performance.mark(formatMarkName(markName));
      };

      var clearMark = function clearMark(markName) {
        performance.clearMarks(formatMarkName(markName));
      };

      var endMark = function endMark(label, markName, warning$$1) {
        var formattedMarkName = formatMarkName(markName);
        var formattedLabel = formatLabel(label, warning$$1);

        try {
          performance.measure(formattedLabel, formattedMarkName);
        } catch (err) {}

        performance.clearMarks(formattedMarkName);
        performance.clearMeasures(formattedLabel);
      };

      var getFiberMarkName = function getFiberMarkName(label, debugID) {
        return label + " (#" + debugID + ")";
      };

      var getFiberLabel = function getFiberLabel(componentName, isMounted, phase) {
        if (phase === null) {
          return componentName + " [" + (isMounted ? "update" : "mount") + "]";
        } else {
          return componentName + "." + phase;
        }
      };

      var beginFiberMark = function beginFiberMark(fiber, phase) {
        var componentName = getComponentName(fiber) || "Unknown";
        var debugID = fiber._debugID;
        var isMounted = fiber.alternate !== null;
        var label = getFiberLabel(componentName, isMounted, phase);

        if (isCommitting && labelsInCurrentCommit.has(label)) {
          return false;
        }

        labelsInCurrentCommit.add(label);
        var markName = getFiberMarkName(label, debugID);
        beginMark(markName);
        return true;
      };

      var clearFiberMark = function clearFiberMark(fiber, phase) {
        var componentName = getComponentName(fiber) || "Unknown";
        var debugID = fiber._debugID;
        var isMounted = fiber.alternate !== null;
        var label = getFiberLabel(componentName, isMounted, phase);
        var markName = getFiberMarkName(label, debugID);
        clearMark(markName);
      };

      var endFiberMark = function endFiberMark(fiber, phase, warning$$1) {
        var componentName = getComponentName(fiber) || "Unknown";
        var debugID = fiber._debugID;
        var isMounted = fiber.alternate !== null;
        var label = getFiberLabel(componentName, isMounted, phase);
        var markName = getFiberMarkName(label, debugID);
        endMark(label, markName, warning$$1);
      };

      var shouldIgnoreFiber = function shouldIgnoreFiber(fiber) {
        switch (fiber.tag) {
          case HostRoot:
          case HostComponent:
          case HostText:
          case HostPortal:
          case CallComponent:
          case ReturnComponent:
          case Fragment:
          case ContextProvider:
          case ContextConsumer:
            return true;

          default:
            return false;
        }
      };

      var clearPendingPhaseMeasurement = function clearPendingPhaseMeasurement() {
        if (currentPhase !== null && currentPhaseFiber !== null) {
          clearFiberMark(currentPhaseFiber, currentPhase);
        }

        currentPhaseFiber = null;
        currentPhase = null;
        hasScheduledUpdateInCurrentPhase = false;
      };

      var pauseTimers = function pauseTimers() {
        var fiber = currentFiber;

        while (fiber) {
          if (fiber._debugIsCurrentlyTiming) {
            endFiberMark(fiber, null, null);
          }

          fiber = fiber["return"];
        }
      };

      var resumeTimersRecursively = function resumeTimersRecursively(fiber) {
        if (fiber["return"] !== null) {
          resumeTimersRecursively(fiber["return"]);
        }

        if (fiber._debugIsCurrentlyTiming) {
          beginFiberMark(fiber, null);
        }
      };

      var resumeTimers = function resumeTimers() {
        if (currentFiber !== null) {
          resumeTimersRecursively(currentFiber);
        }
      };

      function recordEffect() {
        if (enableUserTimingAPI) {
          effectCountInCurrentCommit++;
        }
      }

      function recordScheduleUpdate() {
        if (enableUserTimingAPI) {
          if (isCommitting) {
            hasScheduledUpdateInCurrentCommit = true;
          }

          if (currentPhase !== null && currentPhase !== "componentWillMount" && currentPhase !== "componentWillReceiveProps") {
            hasScheduledUpdateInCurrentPhase = true;
          }
        }
      }

      function startRequestCallbackTimer() {
        if (enableUserTimingAPI) {
          if (supportsUserTiming && !isWaitingForCallback) {
            isWaitingForCallback = true;
            beginMark("(Waiting for async callback...)");
          }
        }
      }

      function stopRequestCallbackTimer(didExpire, expirationTime) {
        if (enableUserTimingAPI) {
          if (supportsUserTiming) {
            isWaitingForCallback = false;
            var warning$$1 = didExpire ? "React was blocked by main thread" : null;
            endMark("(Waiting for async callback... will force flush in " + expirationTime + " ms)", "(Waiting for async callback...)", warning$$1);
          }
        }
      }

      function startWorkTimer(fiber) {
        if (enableUserTimingAPI) {
          if (!supportsUserTiming || shouldIgnoreFiber(fiber)) {
            return;
          }

          currentFiber = fiber;

          if (!beginFiberMark(fiber, null)) {
            return;
          }

          fiber._debugIsCurrentlyTiming = true;
        }
      }

      function cancelWorkTimer(fiber) {
        if (enableUserTimingAPI) {
          if (!supportsUserTiming || shouldIgnoreFiber(fiber)) {
            return;
          }

          fiber._debugIsCurrentlyTiming = false;
          clearFiberMark(fiber, null);
        }
      }

      function stopWorkTimer(fiber) {
        if (enableUserTimingAPI) {
          if (!supportsUserTiming || shouldIgnoreFiber(fiber)) {
            return;
          }

          currentFiber = fiber["return"];

          if (!fiber._debugIsCurrentlyTiming) {
            return;
          }

          fiber._debugIsCurrentlyTiming = false;
          endFiberMark(fiber, null, null);
        }
      }

      function stopFailedWorkTimer(fiber) {
        if (enableUserTimingAPI) {
          if (!supportsUserTiming || shouldIgnoreFiber(fiber)) {
            return;
          }

          currentFiber = fiber["return"];

          if (!fiber._debugIsCurrentlyTiming) {
            return;
          }

          fiber._debugIsCurrentlyTiming = false;
          var warning$$1 = "An error was thrown inside this error boundary";
          endFiberMark(fiber, null, warning$$1);
        }
      }

      function startPhaseTimer(fiber, phase) {
        if (enableUserTimingAPI) {
          if (!supportsUserTiming) {
            return;
          }

          clearPendingPhaseMeasurement();

          if (!beginFiberMark(fiber, phase)) {
            return;
          }

          currentPhaseFiber = fiber;
          currentPhase = phase;
        }
      }

      function stopPhaseTimer() {
        if (enableUserTimingAPI) {
          if (!supportsUserTiming) {
            return;
          }

          if (currentPhase !== null && currentPhaseFiber !== null) {
            var warning$$1 = hasScheduledUpdateInCurrentPhase ? "Scheduled a cascading update" : null;
            endFiberMark(currentPhaseFiber, currentPhase, warning$$1);
          }

          currentPhase = null;
          currentPhaseFiber = null;
        }
      }

      function startWorkLoopTimer(nextUnitOfWork) {
        if (enableUserTimingAPI) {
          currentFiber = nextUnitOfWork;

          if (!supportsUserTiming) {
            return;
          }

          commitCountInCurrentWorkLoop = 0;
          beginMark("(React Tree Reconciliation)");
          resumeTimers();
        }
      }

      function stopWorkLoopTimer(interruptedBy, didCompleteRoot) {
        if (enableUserTimingAPI) {
          if (!supportsUserTiming) {
            return;
          }

          var warning$$1 = null;

          if (interruptedBy !== null) {
            if (interruptedBy.tag === HostRoot) {
              warning$$1 = "A top-level update interrupted the previous render";
            } else {
              var componentName = getComponentName(interruptedBy) || "Unknown";
              warning$$1 = "An update to " + componentName + " interrupted the previous render";
            }
          } else if (commitCountInCurrentWorkLoop > 1) {
            warning$$1 = "There were cascading updates";
          }

          commitCountInCurrentWorkLoop = 0;
          var label = didCompleteRoot ? "(React Tree Reconciliation: Completed Root)" : "(React Tree Reconciliation: Yielded)";
          pauseTimers();
          endMark(label, "(React Tree Reconciliation)", warning$$1);
        }
      }

      function startCommitTimer() {
        if (enableUserTimingAPI) {
          if (!supportsUserTiming) {
            return;
          }

          isCommitting = true;
          hasScheduledUpdateInCurrentCommit = false;
          labelsInCurrentCommit.clear();
          beginMark("(Committing Changes)");
        }
      }

      function stopCommitTimer() {
        if (enableUserTimingAPI) {
          if (!supportsUserTiming) {
            return;
          }

          var warning$$1 = null;

          if (hasScheduledUpdateInCurrentCommit) {
            warning$$1 = "Lifecycle hook scheduled a cascading update";
          } else if (commitCountInCurrentWorkLoop > 0) {
            warning$$1 = "Caused by a cascading update in earlier commit";
          }

          hasScheduledUpdateInCurrentCommit = false;
          commitCountInCurrentWorkLoop++;
          isCommitting = false;
          labelsInCurrentCommit.clear();
          endMark("(Committing Changes)", "(Committing Changes)", warning$$1);
        }
      }

      function startCommitSnapshotEffectsTimer() {
        if (enableUserTimingAPI) {
          if (!supportsUserTiming) {
            return;
          }

          effectCountInCurrentCommit = 0;
          beginMark("(Committing Snapshot Effects)");
        }
      }

      function stopCommitSnapshotEffectsTimer() {
        if (enableUserTimingAPI) {
          if (!supportsUserTiming) {
            return;
          }

          var count = effectCountInCurrentCommit;
          effectCountInCurrentCommit = 0;
          endMark("(Committing Snapshot Effects: " + count + " Total)", "(Committing Snapshot Effects)", null);
        }
      }

      function startCommitHostEffectsTimer() {
        if (enableUserTimingAPI) {
          if (!supportsUserTiming) {
            return;
          }

          effectCountInCurrentCommit = 0;
          beginMark("(Committing Host Effects)");
        }
      }

      function stopCommitHostEffectsTimer() {
        if (enableUserTimingAPI) {
          if (!supportsUserTiming) {
            return;
          }

          var count = effectCountInCurrentCommit;
          effectCountInCurrentCommit = 0;
          endMark("(Committing Host Effects: " + count + " Total)", "(Committing Host Effects)", null);
        }
      }

      function startCommitLifeCyclesTimer() {
        if (enableUserTimingAPI) {
          if (!supportsUserTiming) {
            return;
          }

          effectCountInCurrentCommit = 0;
          beginMark("(Calling Lifecycle Methods)");
        }
      }

      function stopCommitLifeCyclesTimer() {
        if (enableUserTimingAPI) {
          if (!supportsUserTiming) {
            return;
          }

          var count = effectCountInCurrentCommit;
          effectCountInCurrentCommit = 0;
          endMark("(Calling Lifecycle Methods: " + count + " Total)", "(Calling Lifecycle Methods)", null);
        }
      }

      var didWarnUpdateInsideUpdate = void 0;
      {
        didWarnUpdateInsideUpdate = false;
      }

      function createUpdateQueue(baseState) {
        var queue = {
          baseState: baseState,
          expirationTime: NoWork,
          first: null,
          last: null,
          callbackList: null,
          hasForceUpdate: false,
          isInitialized: false,
          capturedValues: null
        };
        {
          queue.isProcessing = false;
        }
        return queue;
      }

      function insertUpdateIntoQueue(queue, update) {
        if (queue.last === null) {
          queue.first = queue.last = update;
        } else {
          queue.last.next = update;
          queue.last = update;
        }

        if (queue.expirationTime === NoWork || queue.expirationTime > update.expirationTime) {
          queue.expirationTime = update.expirationTime;
        }
      }

      var q1 = void 0;
      var q2 = void 0;

      function ensureUpdateQueues(fiber) {
        q1 = q2 = null;
        var alternateFiber = fiber.alternate;
        var queue1 = fiber.updateQueue;

        if (queue1 === null) {
          queue1 = fiber.updateQueue = createUpdateQueue(null);
        }

        var queue2 = void 0;

        if (alternateFiber !== null) {
          queue2 = alternateFiber.updateQueue;

          if (queue2 === null) {
            queue2 = alternateFiber.updateQueue = createUpdateQueue(null);
          }
        } else {
          queue2 = null;
        }

        queue2 = queue2 !== queue1 ? queue2 : null;
        q1 = queue1;
        q2 = queue2;
      }

      function insertUpdateIntoFiber(fiber, update) {
        ensureUpdateQueues(fiber);
        var queue1 = q1;
        var queue2 = q2;
        {
          if ((queue1.isProcessing || queue2 !== null && queue2.isProcessing) && !didWarnUpdateInsideUpdate) {
            warning(false, "An update (setState, replaceState, or forceUpdate) was scheduled " + "from inside an update function. Update functions should be pure, " + "with zero side-effects. Consider using componentDidUpdate or a " + "callback.");
            didWarnUpdateInsideUpdate = true;
          }
        }

        if (queue2 === null) {
          insertUpdateIntoQueue(queue1, update);
          return;
        }

        if (queue1.last === null || queue2.last === null) {
          insertUpdateIntoQueue(queue1, update);
          insertUpdateIntoQueue(queue2, update);
          return;
        }

        insertUpdateIntoQueue(queue1, update);
        queue2.last = update;
      }

      function getUpdateExpirationTime(fiber) {
        switch (fiber.tag) {
          case HostRoot:
          case ClassComponent:
            var updateQueue = fiber.updateQueue;

            if (updateQueue === null) {
              return NoWork;
            }

            return updateQueue.expirationTime;

          default:
            return NoWork;
        }
      }

      function getStateFromUpdate(update, instance, prevState, props) {
        var partialState = update.partialState;

        if (typeof partialState === "function") {
          return partialState.call(instance, prevState, props);
        } else {
          return partialState;
        }
      }

      function processUpdateQueue(current, workInProgress, queue, instance, props, renderExpirationTime) {
        if (current !== null && current.updateQueue === queue) {
          var currentQueue = queue;
          queue = workInProgress.updateQueue = {
            baseState: currentQueue.baseState,
            expirationTime: currentQueue.expirationTime,
            first: currentQueue.first,
            last: currentQueue.last,
            isInitialized: currentQueue.isInitialized,
            capturedValues: currentQueue.capturedValues,
            callbackList: null,
            hasForceUpdate: false
          };
        }

        {
          queue.isProcessing = true;
        }
        queue.expirationTime = NoWork;
        var state = void 0;

        if (queue.isInitialized) {
          state = queue.baseState;
        } else {
          state = queue.baseState = workInProgress.memoizedState;
          queue.isInitialized = true;
        }

        var dontMutatePrevState = true;
        var update = queue.first;
        var didSkip = false;

        while (update !== null) {
          var updateExpirationTime = update.expirationTime;

          if (updateExpirationTime > renderExpirationTime) {
            var remainingExpirationTime = queue.expirationTime;

            if (remainingExpirationTime === NoWork || remainingExpirationTime > updateExpirationTime) {
              queue.expirationTime = updateExpirationTime;
            }

            if (!didSkip) {
              didSkip = true;
              queue.baseState = state;
            }

            update = update.next;
            continue;
          }

          if (!didSkip) {
            queue.first = update.next;

            if (queue.first === null) {
              queue.last = null;
            }
          }

          if (debugRenderPhaseSideEffects || debugRenderPhaseSideEffectsForStrictMode && workInProgress.mode & StrictMode) {
            getStateFromUpdate(update, instance, state, props);
          }

          var _partialState = void 0;

          if (update.isReplace) {
            state = getStateFromUpdate(update, instance, state, props);
            dontMutatePrevState = true;
          } else {
            _partialState = getStateFromUpdate(update, instance, state, props);

            if (_partialState) {
              if (dontMutatePrevState) {
                state = babelHelpers.extends({}, state, _partialState);
              } else {
                state = babelHelpers.extends(state, _partialState);
              }

              dontMutatePrevState = false;
            }
          }

          if (update.isForced) {
            queue.hasForceUpdate = true;
          }

          if (update.callback !== null) {
            var _callbackList = queue.callbackList;

            if (_callbackList === null) {
              _callbackList = queue.callbackList = [];
            }

            _callbackList.push(update);
          }

          if (update.capturedValue !== null) {
            var _capturedValues = queue.capturedValues;

            if (_capturedValues === null) {
              queue.capturedValues = [update.capturedValue];
            } else {
              _capturedValues.push(update.capturedValue);
            }
          }

          update = update.next;
        }

        if (queue.callbackList !== null) {
          workInProgress.effectTag |= Callback;
        } else if (queue.first === null && !queue.hasForceUpdate && queue.capturedValues === null) {
          workInProgress.updateQueue = null;
        }

        if (!didSkip) {
          didSkip = true;
          queue.baseState = state;
        }

        {
          queue.isProcessing = false;
        }
        return state;
      }

      function commitCallbacks(queue, context) {
        var callbackList = queue.callbackList;

        if (callbackList === null) {
          return;
        }

        queue.callbackList = null;

        for (var i = 0; i < callbackList.length; i++) {
          var update = callbackList[i];
          var _callback = update.callback;
          update.callback = null;
          invariant(typeof _callback === "function", "Invalid argument passed as callback. Expected a function. Instead " + "received: %s", _callback);

          _callback.call(context);
        }
      }

      var fakeInternalInstance = {};
      var isArray = Array.isArray;
      var didWarnAboutStateAssignmentForComponent = void 0;
      var didWarnAboutUndefinedDerivedState = void 0;
      var didWarnAboutUninitializedState = void 0;
      var didWarnAboutGetSnapshotBeforeUpdateWithoutDidUpdate = void 0;
      var didWarnAboutLegacyLifecyclesAndDerivedState = void 0;
      var warnOnInvalidCallback = void 0;
      {
        didWarnAboutStateAssignmentForComponent = new Set();
        didWarnAboutUndefinedDerivedState = new Set();
        didWarnAboutUninitializedState = new Set();
        didWarnAboutGetSnapshotBeforeUpdateWithoutDidUpdate = new Set();
        didWarnAboutLegacyLifecyclesAndDerivedState = new Set();
        var didWarnOnInvalidCallback = new Set();

        warnOnInvalidCallback = function warnOnInvalidCallback(callback, callerName) {
          if (callback === null || typeof callback === "function") {
            return;
          }

          var key = callerName + "_" + callback;

          if (!didWarnOnInvalidCallback.has(key)) {
            didWarnOnInvalidCallback.add(key);
            warning(false, "%s(...): Expected the last optional `callback` argument to be a " + "function. Instead received: %s.", callerName, callback);
          }
        };

        Object.defineProperty(fakeInternalInstance, "_processChildContext", {
          enumerable: false,
          value: function value() {
            invariant(false, "_processChildContext is not available in React 16+. This likely " + "means you have multiple copies of React and are attempting to nest " + "a React 15 tree inside a React 16 tree using " + "unstable_renderSubtreeIntoContainer, which isn't supported. Try " + "to make sure you have only one copy of React (and ideally, switch " + "to ReactDOM.createPortal).");
          }
        });
        Object.freeze(fakeInternalInstance);
      }

      function callGetDerivedStateFromCatch(ctor, capturedValues) {
        var resultState = {};

        for (var i = 0; i < capturedValues.length; i++) {
          var capturedValue = capturedValues[i];
          var error = capturedValue.value;
          var partialState = ctor.getDerivedStateFromCatch.call(null, error);

          if (partialState !== null && partialState !== undefined) {
            babelHelpers.extends(resultState, partialState);
          }
        }

        return resultState;
      }

      var ReactFiberClassComponent = function ReactFiberClassComponent(legacyContext, scheduleWork, computeExpirationForFiber, memoizeProps, memoizeState) {
        var cacheContext = legacyContext.cacheContext,
            getMaskedContext = legacyContext.getMaskedContext,
            getUnmaskedContext = legacyContext.getUnmaskedContext,
            isContextConsumer = legacyContext.isContextConsumer,
            hasContextChanged = legacyContext.hasContextChanged;
        var updater = {
          isMounted: isMounted,
          enqueueSetState: function enqueueSetState(instance, partialState, callback) {
            var fiber = get(instance);
            callback = callback === undefined ? null : callback;
            {
              warnOnInvalidCallback(callback, "setState");
            }
            var expirationTime = computeExpirationForFiber(fiber);
            var update = {
              expirationTime: expirationTime,
              partialState: partialState,
              callback: callback,
              isReplace: false,
              isForced: false,
              capturedValue: null,
              next: null
            };
            insertUpdateIntoFiber(fiber, update);
            scheduleWork(fiber, expirationTime);
          },
          enqueueReplaceState: function enqueueReplaceState(instance, state, callback) {
            var fiber = get(instance);
            callback = callback === undefined ? null : callback;
            {
              warnOnInvalidCallback(callback, "replaceState");
            }
            var expirationTime = computeExpirationForFiber(fiber);
            var update = {
              expirationTime: expirationTime,
              partialState: state,
              callback: callback,
              isReplace: true,
              isForced: false,
              capturedValue: null,
              next: null
            };
            insertUpdateIntoFiber(fiber, update);
            scheduleWork(fiber, expirationTime);
          },
          enqueueForceUpdate: function enqueueForceUpdate(instance, callback) {
            var fiber = get(instance);
            callback = callback === undefined ? null : callback;
            {
              warnOnInvalidCallback(callback, "forceUpdate");
            }
            var expirationTime = computeExpirationForFiber(fiber);
            var update = {
              expirationTime: expirationTime,
              partialState: null,
              callback: callback,
              isReplace: false,
              isForced: true,
              capturedValue: null,
              next: null
            };
            insertUpdateIntoFiber(fiber, update);
            scheduleWork(fiber, expirationTime);
          }
        };

        function checkShouldComponentUpdate(workInProgress, oldProps, newProps, oldState, newState, newContext) {
          if (oldProps === null || workInProgress.updateQueue !== null && workInProgress.updateQueue.hasForceUpdate) {
            return true;
          }

          var instance = workInProgress.stateNode;
          var ctor = workInProgress.type;

          if (typeof instance.shouldComponentUpdate === "function") {
            startPhaseTimer(workInProgress, "shouldComponentUpdate");
            var shouldUpdate = instance.shouldComponentUpdate(newProps, newState, newContext);
            stopPhaseTimer();
            {
              !(shouldUpdate !== undefined) ? warning(false, "%s.shouldComponentUpdate(): Returned undefined instead of a " + "boolean value. Make sure to return true or false.", getComponentName(workInProgress) || "Component") : void 0;
            }
            return shouldUpdate;
          }

          if (ctor.prototype && ctor.prototype.isPureReactComponent) {
            return !shallowEqual(oldProps, newProps) || !shallowEqual(oldState, newState);
          }

          return true;
        }

        function checkClassInstance(workInProgress) {
          var instance = workInProgress.stateNode;
          var type = workInProgress.type;
          {
            var name = getComponentName(workInProgress) || "Component";
            var renderPresent = instance.render;

            if (!renderPresent) {
              if (type.prototype && typeof type.prototype.render === "function") {
                warning(false, "%s(...): No `render` method found on the returned component " + "instance: did you accidentally return an object from the constructor?", name);
              } else {
                warning(false, "%s(...): No `render` method found on the returned component " + "instance: you may have forgotten to define `render`.", name);
              }
            }

            var noGetInitialStateOnES6 = !instance.getInitialState || instance.getInitialState.isReactClassApproved || instance.state;
            !noGetInitialStateOnES6 ? warning(false, "getInitialState was defined on %s, a plain JavaScript class. " + "This is only supported for classes created using React.createClass. " + "Did you mean to define a state property instead?", name) : void 0;
            var noGetDefaultPropsOnES6 = !instance.getDefaultProps || instance.getDefaultProps.isReactClassApproved;
            !noGetDefaultPropsOnES6 ? warning(false, "getDefaultProps was defined on %s, a plain JavaScript class. " + "This is only supported for classes created using React.createClass. " + "Use a static property to define defaultProps instead.", name) : void 0;
            var noInstancePropTypes = !instance.propTypes;
            !noInstancePropTypes ? warning(false, "propTypes was defined as an instance property on %s. Use a static " + "property to define propTypes instead.", name) : void 0;
            var noInstanceContextTypes = !instance.contextTypes;
            !noInstanceContextTypes ? warning(false, "contextTypes was defined as an instance property on %s. Use a static " + "property to define contextTypes instead.", name) : void 0;
            var noComponentShouldUpdate = typeof instance.componentShouldUpdate !== "function";
            !noComponentShouldUpdate ? warning(false, "%s has a method called " + "componentShouldUpdate(). Did you mean shouldComponentUpdate()? " + "The name is phrased as a question because the function is " + "expected to return a value.", name) : void 0;

            if (type.prototype && type.prototype.isPureReactComponent && typeof instance.shouldComponentUpdate !== "undefined") {
              warning(false, "%s has a method called shouldComponentUpdate(). " + "shouldComponentUpdate should not be used when extending React.PureComponent. " + "Please extend React.Component if shouldComponentUpdate is used.", getComponentName(workInProgress) || "A pure component");
            }

            var noComponentDidUnmount = typeof instance.componentDidUnmount !== "function";
            !noComponentDidUnmount ? warning(false, "%s has a method called " + "componentDidUnmount(). But there is no such lifecycle method. " + "Did you mean componentWillUnmount()?", name) : void 0;
            var noComponentDidReceiveProps = typeof instance.componentDidReceiveProps !== "function";
            !noComponentDidReceiveProps ? warning(false, "%s has a method called " + "componentDidReceiveProps(). But there is no such lifecycle method. " + "If you meant to update the state in response to changing props, " + "use componentWillReceiveProps(). If you meant to fetch data or " + "run side-effects or mutations after React has updated the UI, use componentDidUpdate().", name) : void 0;
            var noComponentWillRecieveProps = typeof instance.componentWillRecieveProps !== "function";
            !noComponentWillRecieveProps ? warning(false, "%s has a method called " + "componentWillRecieveProps(). Did you mean componentWillReceiveProps()?", name) : void 0;
            var noUnsafeComponentWillRecieveProps = typeof instance.UNSAFE_componentWillRecieveProps !== "function";
            !noUnsafeComponentWillRecieveProps ? warning(false, "%s has a method called " + "UNSAFE_componentWillRecieveProps(). Did you mean UNSAFE_componentWillReceiveProps()?", name) : void 0;
            var hasMutatedProps = instance.props !== workInProgress.pendingProps;
            !(instance.props === undefined || !hasMutatedProps) ? warning(false, "%s(...): When calling super() in `%s`, make sure to pass " + "up the same props that your component's constructor was passed.", name, name) : void 0;
            var noInstanceDefaultProps = !instance.defaultProps;
            !noInstanceDefaultProps ? warning(false, "Setting defaultProps as an instance property on %s is not supported and will be ignored." + " Instead, define defaultProps as a static property on %s.", name, name) : void 0;

            if (typeof instance.getSnapshotBeforeUpdate === "function" && typeof instance.componentDidUpdate !== "function" && typeof instance.componentDidUpdate !== "function" && !didWarnAboutGetSnapshotBeforeUpdateWithoutDidUpdate.has(type)) {
              didWarnAboutGetSnapshotBeforeUpdateWithoutDidUpdate.add(type);
              warning(false, "%s: getSnapshotBeforeUpdate() should be used with componentDidUpdate(). " + "This component defines getSnapshotBeforeUpdate() only.", getComponentName(workInProgress));
            }

            var noInstanceGetDerivedStateFromProps = typeof instance.getDerivedStateFromProps !== "function";
            !noInstanceGetDerivedStateFromProps ? warning(false, "%s: getDerivedStateFromProps() is defined as an instance method " + "and will be ignored. Instead, declare it as a static method.", name) : void 0;
            var noInstanceGetDerivedStateFromCatch = typeof instance.getDerivedStateFromCatch !== "function";
            !noInstanceGetDerivedStateFromCatch ? warning(false, "%s: getDerivedStateFromCatch() is defined as an instance method " + "and will be ignored. Instead, declare it as a static method.", name) : void 0;
            var noStaticGetSnapshotBeforeUpdate = typeof type.getSnapshotBeforeUpdate !== "function";
            !noStaticGetSnapshotBeforeUpdate ? warning(false, "%s: getSnapshotBeforeUpdate() is defined as a static method " + "and will be ignored. Instead, declare it as an instance method.", name) : void 0;
            var _state = instance.state;

            if (_state && (typeof _state !== "object" || isArray(_state))) {
              warning(false, "%s.state: must be set to an object or null", name);
            }

            if (typeof instance.getChildContext === "function") {
              !(typeof type.childContextTypes === "object") ? warning(false, "%s.getChildContext(): childContextTypes must be defined in order to " + "use getChildContext().", name) : void 0;
            }
          }
        }

        function resetInputPointers(workInProgress, instance) {
          instance.props = workInProgress.memoizedProps;
          instance.state = workInProgress.memoizedState;
        }

        function adoptClassInstance(workInProgress, instance) {
          instance.updater = updater;
          workInProgress.stateNode = instance;
          set(instance, workInProgress);
          {
            instance._reactInternalInstance = fakeInternalInstance;
          }
        }

        function constructClassInstance(workInProgress, props) {
          var ctor = workInProgress.type;
          var unmaskedContext = getUnmaskedContext(workInProgress);
          var needsContext = isContextConsumer(workInProgress);
          var context = needsContext ? getMaskedContext(workInProgress, unmaskedContext) : emptyObject;

          if (debugRenderPhaseSideEffects || debugRenderPhaseSideEffectsForStrictMode && workInProgress.mode & StrictMode) {
            new ctor(props, context);
          }

          var instance = new ctor(props, context);
          var state = instance.state !== null && instance.state !== undefined ? instance.state : null;
          adoptClassInstance(workInProgress, instance);
          {
            if (typeof ctor.getDerivedStateFromProps === "function" && state === null) {
              var componentName = getComponentName(workInProgress) || "Component";

              if (!didWarnAboutUninitializedState.has(componentName)) {
                didWarnAboutUninitializedState.add(componentName);
                warning(false, "%s: Did not properly initialize state during construction. " + "Expected state to be an object, but it was %s.", componentName, instance.state === null ? "null" : "undefined");
              }
            }

            if (typeof ctor.getDerivedStateFromProps === "function" || typeof instance.getSnapshotBeforeUpdate === "function") {
              var foundWillMountName = null;
              var foundWillReceivePropsName = null;
              var foundWillUpdateName = null;

              if (typeof instance.componentWillMount === "function" && instance.componentWillMount.__suppressDeprecationWarning !== true) {
                foundWillMountName = "componentWillMount";
              } else if (typeof instance.UNSAFE_componentWillMount === "function") {
                foundWillMountName = "UNSAFE_componentWillMount";
              }

              if (typeof instance.componentWillReceiveProps === "function" && instance.componentWillReceiveProps.__suppressDeprecationWarning !== true) {
                foundWillReceivePropsName = "componentWillReceiveProps";
              } else if (typeof instance.UNSAFE_componentWillReceiveProps === "function") {
                foundWillReceivePropsName = "UNSAFE_componentWillReceiveProps";
              }

              if (typeof instance.componentWillUpdate === "function" && instance.componentWillUpdate.__suppressDeprecationWarning !== true) {
                foundWillUpdateName = "componentWillUpdate";
              } else if (typeof instance.UNSAFE_componentWillUpdate === "function") {
                foundWillUpdateName = "UNSAFE_componentWillUpdate";
              }

              if (foundWillMountName !== null || foundWillReceivePropsName !== null || foundWillUpdateName !== null) {
                var _componentName = getComponentName(workInProgress) || "Component";

                var newApiName = typeof ctor.getDerivedStateFromProps === "function" ? "getDerivedStateFromProps()" : "getSnapshotBeforeUpdate()";

                if (!didWarnAboutLegacyLifecyclesAndDerivedState.has(_componentName)) {
                  didWarnAboutLegacyLifecyclesAndDerivedState.add(_componentName);
                  warning(false, "Unsafe legacy lifecycles will not be called for components using new component APIs.\n\n" + "%s uses %s but also contains the following legacy lifecycles:%s%s%s\n\n" + "The above lifecycles should be removed. Learn more about this warning here:\n" + "https://fb.me/react-async-component-lifecycle-hooks", _componentName, newApiName, foundWillMountName !== null ? "\n  " + foundWillMountName : "", foundWillReceivePropsName !== null ? "\n  " + foundWillReceivePropsName : "", foundWillUpdateName !== null ? "\n  " + foundWillUpdateName : "");
                }
              }
            }
          }
          workInProgress.memoizedState = state;
          var partialState = callGetDerivedStateFromProps(workInProgress, instance, props, state);

          if (partialState !== null && partialState !== undefined) {
            workInProgress.memoizedState = babelHelpers.extends({}, workInProgress.memoizedState, partialState);
          }

          if (needsContext) {
            cacheContext(workInProgress, unmaskedContext, context);
          }

          return instance;
        }

        function callComponentWillMount(workInProgress, instance) {
          startPhaseTimer(workInProgress, "componentWillMount");
          var oldState = instance.state;

          if (typeof instance.componentWillMount === "function") {
            instance.componentWillMount();
          }

          if (typeof instance.UNSAFE_componentWillMount === "function") {
            instance.UNSAFE_componentWillMount();
          }

          stopPhaseTimer();

          if (oldState !== instance.state) {
            {
              warning(false, "%s.componentWillMount(): Assigning directly to this.state is " + "deprecated (except inside a component's " + "constructor). Use setState instead.", getComponentName(workInProgress) || "Component");
            }
            updater.enqueueReplaceState(instance, instance.state, null);
          }
        }

        function callComponentWillReceiveProps(workInProgress, instance, newProps, newContext) {
          var oldState = instance.state;
          startPhaseTimer(workInProgress, "componentWillReceiveProps");

          if (typeof instance.componentWillReceiveProps === "function") {
            instance.componentWillReceiveProps(newProps, newContext);
          }

          if (typeof instance.UNSAFE_componentWillReceiveProps === "function") {
            instance.UNSAFE_componentWillReceiveProps(newProps, newContext);
          }

          stopPhaseTimer();

          if (instance.state !== oldState) {
            {
              var componentName = getComponentName(workInProgress) || "Component";

              if (!didWarnAboutStateAssignmentForComponent.has(componentName)) {
                didWarnAboutStateAssignmentForComponent.add(componentName);
                warning(false, "%s.componentWillReceiveProps(): Assigning directly to " + "this.state is deprecated (except inside a component's " + "constructor). Use setState instead.", componentName);
              }
            }
            updater.enqueueReplaceState(instance, instance.state, null);
          }
        }

        function callGetDerivedStateFromProps(workInProgress, instance, nextProps, prevState) {
          var type = workInProgress.type;

          if (typeof type.getDerivedStateFromProps === "function") {
            if (debugRenderPhaseSideEffects || debugRenderPhaseSideEffectsForStrictMode && workInProgress.mode & StrictMode) {
              type.getDerivedStateFromProps.call(null, nextProps, prevState);
            }

            var partialState = type.getDerivedStateFromProps.call(null, nextProps, prevState);
            {
              if (partialState === undefined) {
                var componentName = getComponentName(workInProgress) || "Component";

                if (!didWarnAboutUndefinedDerivedState.has(componentName)) {
                  didWarnAboutUndefinedDerivedState.add(componentName);
                  warning(false, "%s.getDerivedStateFromProps(): A valid state object (or null) must be returned. " + "You have returned undefined.", componentName);
                }
              }
            }
            return partialState;
          }
        }

        function mountClassInstance(workInProgress, renderExpirationTime) {
          var ctor = workInProgress.type;
          var current = workInProgress.alternate;
          {
            checkClassInstance(workInProgress);
          }
          var instance = workInProgress.stateNode;
          var props = workInProgress.pendingProps;
          var unmaskedContext = getUnmaskedContext(workInProgress);
          instance.props = props;
          instance.state = workInProgress.memoizedState;
          instance.refs = emptyObject;
          instance.context = getMaskedContext(workInProgress, unmaskedContext);
          {
            if (workInProgress.mode & StrictMode) {
              ReactStrictModeWarnings.recordUnsafeLifecycleWarnings(workInProgress, instance);
            }

            if (warnAboutDeprecatedLifecycles) {
              ReactStrictModeWarnings.recordDeprecationWarnings(workInProgress, instance);
            }
          }

          if (typeof ctor.getDerivedStateFromProps !== "function" && typeof instance.getSnapshotBeforeUpdate !== "function" && (typeof instance.UNSAFE_componentWillMount === "function" || typeof instance.componentWillMount === "function")) {
            callComponentWillMount(workInProgress, instance);
            var updateQueue = workInProgress.updateQueue;

            if (updateQueue !== null) {
              instance.state = processUpdateQueue(current, workInProgress, updateQueue, instance, props, renderExpirationTime);
            }
          }

          if (typeof instance.componentDidMount === "function") {
            workInProgress.effectTag |= Update;
          }
        }

        function resumeMountClassInstance(workInProgress, renderExpirationTime) {
          var ctor = workInProgress.type;
          var instance = workInProgress.stateNode;
          resetInputPointers(workInProgress, instance);
          var oldProps = workInProgress.memoizedProps;
          var newProps = workInProgress.pendingProps;
          var oldContext = instance.context;
          var newUnmaskedContext = getUnmaskedContext(workInProgress);
          var newContext = getMaskedContext(workInProgress, newUnmaskedContext);
          var hasNewLifecycles = typeof ctor.getDerivedStateFromProps === "function" || typeof instance.getSnapshotBeforeUpdate === "function";

          if (!hasNewLifecycles && (typeof instance.UNSAFE_componentWillReceiveProps === "function" || typeof instance.componentWillReceiveProps === "function")) {
            if (oldProps !== newProps || oldContext !== newContext) {
              callComponentWillReceiveProps(workInProgress, instance, newProps, newContext);
            }
          }

          var oldState = workInProgress.memoizedState;
          var newState = void 0;
          var derivedStateFromCatch = void 0;

          if (workInProgress.updateQueue !== null) {
            newState = processUpdateQueue(null, workInProgress, workInProgress.updateQueue, instance, newProps, renderExpirationTime);
            var updateQueue = workInProgress.updateQueue;

            if (updateQueue !== null && updateQueue.capturedValues !== null && enableGetDerivedStateFromCatch && typeof ctor.getDerivedStateFromCatch === "function") {
              var capturedValues = updateQueue.capturedValues;
              derivedStateFromCatch = callGetDerivedStateFromCatch(ctor, capturedValues);
            }
          } else {
            newState = oldState;
          }

          var derivedStateFromProps = void 0;

          if (oldProps !== newProps) {
            derivedStateFromProps = callGetDerivedStateFromProps(workInProgress, instance, newProps, newState);
          }

          if (derivedStateFromProps !== null && derivedStateFromProps !== undefined) {
            newState = newState === null || newState === undefined ? derivedStateFromProps : babelHelpers.extends({}, newState, derivedStateFromProps);
            var _updateQueue = workInProgress.updateQueue;

            if (_updateQueue !== null) {
              _updateQueue.baseState = babelHelpers.extends({}, _updateQueue.baseState, derivedStateFromProps);
            }
          }

          if (derivedStateFromCatch !== null && derivedStateFromCatch !== undefined) {
            newState = newState === null || newState === undefined ? derivedStateFromCatch : babelHelpers.extends({}, newState, derivedStateFromCatch);
            var _updateQueue2 = workInProgress.updateQueue;

            if (_updateQueue2 !== null) {
              _updateQueue2.baseState = babelHelpers.extends({}, _updateQueue2.baseState, derivedStateFromCatch);
            }
          }

          if (oldProps === newProps && oldState === newState && !hasContextChanged() && !(workInProgress.updateQueue !== null && workInProgress.updateQueue.hasForceUpdate)) {
            if (typeof instance.componentDidMount === "function") {
              workInProgress.effectTag |= Update;
            }

            return false;
          }

          var shouldUpdate = checkShouldComponentUpdate(workInProgress, oldProps, newProps, oldState, newState, newContext);

          if (shouldUpdate) {
            if (!hasNewLifecycles && (typeof instance.UNSAFE_componentWillMount === "function" || typeof instance.componentWillMount === "function")) {
              startPhaseTimer(workInProgress, "componentWillMount");

              if (typeof instance.componentWillMount === "function") {
                instance.componentWillMount();
              }

              if (typeof instance.UNSAFE_componentWillMount === "function") {
                instance.UNSAFE_componentWillMount();
              }

              stopPhaseTimer();
            }

            if (typeof instance.componentDidMount === "function") {
              workInProgress.effectTag |= Update;
            }
          } else {
            if (typeof instance.componentDidMount === "function") {
              workInProgress.effectTag |= Update;
            }

            memoizeProps(workInProgress, newProps);
            memoizeState(workInProgress, newState);
          }

          instance.props = newProps;
          instance.state = newState;
          instance.context = newContext;
          return shouldUpdate;
        }

        function updateClassInstance(current, workInProgress, renderExpirationTime) {
          var ctor = workInProgress.type;
          var instance = workInProgress.stateNode;
          resetInputPointers(workInProgress, instance);
          var oldProps = workInProgress.memoizedProps;
          var newProps = workInProgress.pendingProps;
          var oldContext = instance.context;
          var newUnmaskedContext = getUnmaskedContext(workInProgress);
          var newContext = getMaskedContext(workInProgress, newUnmaskedContext);
          var hasNewLifecycles = typeof ctor.getDerivedStateFromProps === "function" || typeof instance.getSnapshotBeforeUpdate === "function";

          if (!hasNewLifecycles && (typeof instance.UNSAFE_componentWillReceiveProps === "function" || typeof instance.componentWillReceiveProps === "function")) {
            if (oldProps !== newProps || oldContext !== newContext) {
              callComponentWillReceiveProps(workInProgress, instance, newProps, newContext);
            }
          }

          var oldState = workInProgress.memoizedState;
          var newState = void 0;
          var derivedStateFromCatch = void 0;

          if (workInProgress.updateQueue !== null) {
            newState = processUpdateQueue(current, workInProgress, workInProgress.updateQueue, instance, newProps, renderExpirationTime);
            var updateQueue = workInProgress.updateQueue;

            if (updateQueue !== null && updateQueue.capturedValues !== null && enableGetDerivedStateFromCatch && typeof ctor.getDerivedStateFromCatch === "function") {
              var capturedValues = updateQueue.capturedValues;
              derivedStateFromCatch = callGetDerivedStateFromCatch(ctor, capturedValues);
            }
          } else {
            newState = oldState;
          }

          var derivedStateFromProps = void 0;

          if (oldProps !== newProps) {
            derivedStateFromProps = callGetDerivedStateFromProps(workInProgress, instance, newProps, newState);
          }

          if (derivedStateFromProps !== null && derivedStateFromProps !== undefined) {
            newState = newState === null || newState === undefined ? derivedStateFromProps : babelHelpers.extends({}, newState, derivedStateFromProps);
            var _updateQueue3 = workInProgress.updateQueue;

            if (_updateQueue3 !== null) {
              _updateQueue3.baseState = babelHelpers.extends({}, _updateQueue3.baseState, derivedStateFromProps);
            }
          }

          if (derivedStateFromCatch !== null && derivedStateFromCatch !== undefined) {
            newState = newState === null || newState === undefined ? derivedStateFromCatch : babelHelpers.extends({}, newState, derivedStateFromCatch);
            var _updateQueue4 = workInProgress.updateQueue;

            if (_updateQueue4 !== null) {
              _updateQueue4.baseState = babelHelpers.extends({}, _updateQueue4.baseState, derivedStateFromCatch);
            }
          }

          if (oldProps === newProps && oldState === newState && !hasContextChanged() && !(workInProgress.updateQueue !== null && workInProgress.updateQueue.hasForceUpdate)) {
            if (typeof instance.componentDidUpdate === "function") {
              if (oldProps !== current.memoizedProps || oldState !== current.memoizedState) {
                workInProgress.effectTag |= Update;
              }
            }

            if (typeof instance.getSnapshotBeforeUpdate === "function") {
              if (oldProps !== current.memoizedProps || oldState !== current.memoizedState) {
                workInProgress.effectTag |= Snapshot;
              }
            }

            return false;
          }

          var shouldUpdate = checkShouldComponentUpdate(workInProgress, oldProps, newProps, oldState, newState, newContext);

          if (shouldUpdate) {
            if (!hasNewLifecycles && (typeof instance.UNSAFE_componentWillUpdate === "function" || typeof instance.componentWillUpdate === "function")) {
              startPhaseTimer(workInProgress, "componentWillUpdate");

              if (typeof instance.componentWillUpdate === "function") {
                instance.componentWillUpdate(newProps, newState, newContext);
              }

              if (typeof instance.UNSAFE_componentWillUpdate === "function") {
                instance.UNSAFE_componentWillUpdate(newProps, newState, newContext);
              }

              stopPhaseTimer();
            }

            if (typeof instance.componentDidUpdate === "function") {
              workInProgress.effectTag |= Update;
            }

            if (typeof instance.getSnapshotBeforeUpdate === "function") {
              workInProgress.effectTag |= Snapshot;
            }
          } else {
            if (typeof instance.componentDidUpdate === "function") {
              if (oldProps !== current.memoizedProps || oldState !== current.memoizedState) {
                workInProgress.effectTag |= Update;
              }
            }

            if (typeof instance.getSnapshotBeforeUpdate === "function") {
              if (oldProps !== current.memoizedProps || oldState !== current.memoizedState) {
                workInProgress.effectTag |= Snapshot;
              }
            }

            memoizeProps(workInProgress, newProps);
            memoizeState(workInProgress, newState);
          }

          instance.props = newProps;
          instance.state = newState;
          instance.context = newContext;
          return shouldUpdate;
        }

        return {
          adoptClassInstance: adoptClassInstance,
          callGetDerivedStateFromProps: callGetDerivedStateFromProps,
          constructClassInstance: constructClassInstance,
          mountClassInstance: mountClassInstance,
          resumeMountClassInstance: resumeMountClassInstance,
          updateClassInstance: updateClassInstance
        };
      };

      var getCurrentFiberStackAddendum$1 = ReactDebugCurrentFiber.getCurrentFiberStackAddendum;
      var didWarnAboutMaps = void 0;
      var didWarnAboutStringRefInStrictMode = void 0;
      var ownerHasKeyUseWarning = void 0;
      var ownerHasFunctionTypeWarning = void 0;

      var warnForMissingKey = function warnForMissingKey(child) {};

      {
        didWarnAboutMaps = false;
        didWarnAboutStringRefInStrictMode = {};
        ownerHasKeyUseWarning = {};
        ownerHasFunctionTypeWarning = {};

        warnForMissingKey = function warnForMissingKey(child) {
          if (child === null || typeof child !== "object") {
            return;
          }

          if (!child._store || child._store.validated || child.key != null) {
            return;
          }

          invariant(typeof child._store === "object", "React Component in warnForMissingKey should have a _store. " + "This error is likely caused by a bug in React. Please file an issue.");
          child._store.validated = true;
          var currentComponentErrorInfo = "Each child in an array or iterator should have a unique " + '"key" prop. See https://fb.me/react-warning-keys for ' + "more information." + (getCurrentFiberStackAddendum$1() || "");

          if (ownerHasKeyUseWarning[currentComponentErrorInfo]) {
            return;
          }

          ownerHasKeyUseWarning[currentComponentErrorInfo] = true;
          warning(false, "Each child in an array or iterator should have a unique " + '"key" prop. See https://fb.me/react-warning-keys for ' + "more information.%s", getCurrentFiberStackAddendum$1());
        };
      }
      var isArray$1 = Array.isArray;

      function coerceRef(returnFiber, current, element) {
        var mixedRef = element.ref;

        if (mixedRef !== null && typeof mixedRef !== "function" && typeof mixedRef !== "object") {
          {
            if (returnFiber.mode & StrictMode) {
              var componentName = getComponentName(returnFiber) || "Component";

              if (!didWarnAboutStringRefInStrictMode[componentName]) {
                warning(false, 'A string ref, "%s", has been found within a strict mode tree. ' + "String refs are a source of potential bugs and should be avoided. " + "We recommend using createRef() instead." + "\n%s" + "\n\nLearn more about using refs safely here:" + "\nhttps://fb.me/react-strict-mode-string-ref", mixedRef, getStackAddendumByWorkInProgressFiber(returnFiber));
                didWarnAboutStringRefInStrictMode[componentName] = true;
              }
            }
          }

          if (element._owner) {
            var owner = element._owner;
            var inst = void 0;

            if (owner) {
              var ownerFiber = owner;
              invariant(ownerFiber.tag === ClassComponent, "Stateless function components cannot have refs.");
              inst = ownerFiber.stateNode;
            }

            invariant(inst, "Missing owner for string ref %s. This error is likely caused by a " + "bug in React. Please file an issue.", mixedRef);
            var stringRef = "" + mixedRef;

            if (current !== null && current.ref !== null && current.ref._stringRef === stringRef) {
              return current.ref;
            }

            var ref = function ref(value) {
              var refs = inst.refs === emptyObject ? inst.refs = {} : inst.refs;

              if (value === null) {
                delete refs[stringRef];
              } else {
                refs[stringRef] = value;
              }
            };

            ref._stringRef = stringRef;
            return ref;
          } else {
            invariant(typeof mixedRef === "string", "Expected ref to be a function or a string.");
            invariant(element._owner, "Element ref was specified as a string (%s) but no owner was set. This could happen for one of" + " the following reasons:\n" + "1. You may be adding a ref to a functional component\n" + "2. You may be adding a ref to a component that was not created inside a component's render method\n" + "3. You have multiple copies of React loaded\n" + "See https://fb.me/react-refs-must-have-owner for more information.", mixedRef);
          }
        }

        return mixedRef;
      }

      function throwOnInvalidObjectType(returnFiber, newChild) {
        if (returnFiber.type !== "textarea") {
          var addendum = "";
          {
            addendum = " If you meant to render a collection of children, use an array " + "instead." + (getCurrentFiberStackAddendum$1() || "");
          }
          invariant(false, "Objects are not valid as a React child (found: %s).%s", Object.prototype.toString.call(newChild) === "[object Object]" ? "object with keys {" + Object.keys(newChild).join(", ") + "}" : newChild, addendum);
        }
      }

      function warnOnFunctionType() {
        var currentComponentErrorInfo = "Functions are not valid as a React child. This may happen if " + "you return a Component instead of <Component /> from render. " + "Or maybe you meant to call this function rather than return it." + (getCurrentFiberStackAddendum$1() || "");

        if (ownerHasFunctionTypeWarning[currentComponentErrorInfo]) {
          return;
        }

        ownerHasFunctionTypeWarning[currentComponentErrorInfo] = true;
        warning(false, "Functions are not valid as a React child. This may happen if " + "you return a Component instead of <Component /> from render. " + "Or maybe you meant to call this function rather than return it.%s", getCurrentFiberStackAddendum$1() || "");
      }

      function ChildReconciler(shouldTrackSideEffects) {
        function deleteChild(returnFiber, childToDelete) {
          if (!shouldTrackSideEffects) {
            return;
          }

          var last = returnFiber.lastEffect;

          if (last !== null) {
            last.nextEffect = childToDelete;
            returnFiber.lastEffect = childToDelete;
          } else {
            returnFiber.firstEffect = returnFiber.lastEffect = childToDelete;
          }

          childToDelete.nextEffect = null;
          childToDelete.effectTag = Deletion;
        }

        function deleteRemainingChildren(returnFiber, currentFirstChild) {
          if (!shouldTrackSideEffects) {
            return null;
          }

          var childToDelete = currentFirstChild;

          while (childToDelete !== null) {
            deleteChild(returnFiber, childToDelete);
            childToDelete = childToDelete.sibling;
          }

          return null;
        }

        function mapRemainingChildren(returnFiber, currentFirstChild) {
          var existingChildren = new Map();
          var existingChild = currentFirstChild;

          while (existingChild !== null) {
            if (existingChild.key !== null) {
              existingChildren.set(existingChild.key, existingChild);
            } else {
              existingChildren.set(existingChild.index, existingChild);
            }

            existingChild = existingChild.sibling;
          }

          return existingChildren;
        }

        function useFiber(fiber, pendingProps, expirationTime) {
          var clone = createWorkInProgress(fiber, pendingProps, expirationTime);
          clone.index = 0;
          clone.sibling = null;
          return clone;
        }

        function placeChild(newFiber, lastPlacedIndex, newIndex) {
          newFiber.index = newIndex;

          if (!shouldTrackSideEffects) {
            return lastPlacedIndex;
          }

          var current = newFiber.alternate;

          if (current !== null) {
            var oldIndex = current.index;

            if (oldIndex < lastPlacedIndex) {
              newFiber.effectTag = Placement;
              return lastPlacedIndex;
            } else {
              return oldIndex;
            }
          } else {
            newFiber.effectTag = Placement;
            return lastPlacedIndex;
          }
        }

        function placeSingleChild(newFiber) {
          if (shouldTrackSideEffects && newFiber.alternate === null) {
            newFiber.effectTag = Placement;
          }

          return newFiber;
        }

        function updateTextNode(returnFiber, current, textContent, expirationTime) {
          if (current === null || current.tag !== HostText) {
            var created = createFiberFromText(textContent, returnFiber.mode, expirationTime);
            created["return"] = returnFiber;
            return created;
          } else {
            var existing = useFiber(current, textContent, expirationTime);
            existing["return"] = returnFiber;
            return existing;
          }
        }

        function updateElement(returnFiber, current, element, expirationTime) {
          if (current !== null && current.type === element.type) {
            var existing = useFiber(current, element.props, expirationTime);
            existing.ref = coerceRef(returnFiber, current, element);
            existing["return"] = returnFiber;
            {
              existing._debugSource = element._source;
              existing._debugOwner = element._owner;
            }
            return existing;
          } else {
            var created = createFiberFromElement(element, returnFiber.mode, expirationTime);
            created.ref = coerceRef(returnFiber, current, element);
            created["return"] = returnFiber;
            return created;
          }
        }

        function updatePortal(returnFiber, current, portal, expirationTime) {
          if (current === null || current.tag !== HostPortal || current.stateNode.containerInfo !== portal.containerInfo || current.stateNode.implementation !== portal.implementation) {
            var created = createFiberFromPortal(portal, returnFiber.mode, expirationTime);
            created["return"] = returnFiber;
            return created;
          } else {
            var existing = useFiber(current, portal.children || [], expirationTime);
            existing["return"] = returnFiber;
            return existing;
          }
        }

        function updateFragment(returnFiber, current, fragment, expirationTime, key) {
          if (current === null || current.tag !== Fragment) {
            var created = createFiberFromFragment(fragment, returnFiber.mode, expirationTime, key);
            created["return"] = returnFiber;
            return created;
          } else {
            var existing = useFiber(current, fragment, expirationTime);
            existing["return"] = returnFiber;
            return existing;
          }
        }

        function createChild(returnFiber, newChild, expirationTime) {
          if (typeof newChild === "string" || typeof newChild === "number") {
            var created = createFiberFromText("" + newChild, returnFiber.mode, expirationTime);
            created["return"] = returnFiber;
            return created;
          }

          if (typeof newChild === "object" && newChild !== null) {
            switch (newChild.$$typeof) {
              case REACT_ELEMENT_TYPE:
                {
                  var _created = createFiberFromElement(newChild, returnFiber.mode, expirationTime);

                  _created.ref = coerceRef(returnFiber, null, newChild);
                  _created["return"] = returnFiber;
                  return _created;
                }

              case REACT_PORTAL_TYPE:
                {
                  var _created2 = createFiberFromPortal(newChild, returnFiber.mode, expirationTime);

                  _created2["return"] = returnFiber;
                  return _created2;
                }
            }

            if (isArray$1(newChild) || getIteratorFn(newChild)) {
              var _created3 = createFiberFromFragment(newChild, returnFiber.mode, expirationTime, null);

              _created3["return"] = returnFiber;
              return _created3;
            }

            throwOnInvalidObjectType(returnFiber, newChild);
          }

          {
            if (typeof newChild === "function") {
              warnOnFunctionType();
            }
          }
          return null;
        }

        function updateSlot(returnFiber, oldFiber, newChild, expirationTime) {
          var key = oldFiber !== null ? oldFiber.key : null;

          if (typeof newChild === "string" || typeof newChild === "number") {
            if (key !== null) {
              return null;
            }

            return updateTextNode(returnFiber, oldFiber, "" + newChild, expirationTime);
          }

          if (typeof newChild === "object" && newChild !== null) {
            switch (newChild.$$typeof) {
              case REACT_ELEMENT_TYPE:
                {
                  if (newChild.key === key) {
                    if (newChild.type === REACT_FRAGMENT_TYPE) {
                      return updateFragment(returnFiber, oldFiber, newChild.props.children, expirationTime, key);
                    }

                    return updateElement(returnFiber, oldFiber, newChild, expirationTime);
                  } else {
                    return null;
                  }
                }

              case REACT_PORTAL_TYPE:
                {
                  if (newChild.key === key) {
                    return updatePortal(returnFiber, oldFiber, newChild, expirationTime);
                  } else {
                    return null;
                  }
                }
            }

            if (isArray$1(newChild) || getIteratorFn(newChild)) {
              if (key !== null) {
                return null;
              }

              return updateFragment(returnFiber, oldFiber, newChild, expirationTime, null);
            }

            throwOnInvalidObjectType(returnFiber, newChild);
          }

          {
            if (typeof newChild === "function") {
              warnOnFunctionType();
            }
          }
          return null;
        }

        function updateFromMap(existingChildren, returnFiber, newIdx, newChild, expirationTime) {
          if (typeof newChild === "string" || typeof newChild === "number") {
            var matchedFiber = existingChildren.get(newIdx) || null;
            return updateTextNode(returnFiber, matchedFiber, "" + newChild, expirationTime);
          }

          if (typeof newChild === "object" && newChild !== null) {
            switch (newChild.$$typeof) {
              case REACT_ELEMENT_TYPE:
                {
                  var _matchedFiber = existingChildren.get(newChild.key === null ? newIdx : newChild.key) || null;

                  if (newChild.type === REACT_FRAGMENT_TYPE) {
                    return updateFragment(returnFiber, _matchedFiber, newChild.props.children, expirationTime, newChild.key);
                  }

                  return updateElement(returnFiber, _matchedFiber, newChild, expirationTime);
                }

              case REACT_PORTAL_TYPE:
                {
                  var _matchedFiber2 = existingChildren.get(newChild.key === null ? newIdx : newChild.key) || null;

                  return updatePortal(returnFiber, _matchedFiber2, newChild, expirationTime);
                }
            }

            if (isArray$1(newChild) || getIteratorFn(newChild)) {
              var _matchedFiber3 = existingChildren.get(newIdx) || null;

              return updateFragment(returnFiber, _matchedFiber3, newChild, expirationTime, null);
            }

            throwOnInvalidObjectType(returnFiber, newChild);
          }

          {
            if (typeof newChild === "function") {
              warnOnFunctionType();
            }
          }
          return null;
        }

        function warnOnInvalidKey(child, knownKeys) {
          {
            if (typeof child !== "object" || child === null) {
              return knownKeys;
            }

            switch (child.$$typeof) {
              case REACT_ELEMENT_TYPE:
              case REACT_PORTAL_TYPE:
                warnForMissingKey(child);
                var key = child.key;

                if (typeof key !== "string") {
                  break;
                }

                if (knownKeys === null) {
                  knownKeys = new Set();
                  knownKeys.add(key);
                  break;
                }

                if (!knownKeys.has(key)) {
                  knownKeys.add(key);
                  break;
                }

                warning(false, "Encountered two children with the same key, `%s`. " + "Keys should be unique so that components maintain their identity " + "across updates. Non-unique keys may cause children to be " + "duplicated and/or omitted — the behavior is unsupported and " + "could change in a future version.%s", key, getCurrentFiberStackAddendum$1());
                break;

              default:
                break;
            }
          }
          return knownKeys;
        }

        function reconcileChildrenArray(returnFiber, currentFirstChild, newChildren, expirationTime) {
          {
            var knownKeys = null;

            for (var i = 0; i < newChildren.length; i++) {
              var child = newChildren[i];
              knownKeys = warnOnInvalidKey(child, knownKeys);
            }
          }
          var resultingFirstChild = null;
          var previousNewFiber = null;
          var oldFiber = currentFirstChild;
          var lastPlacedIndex = 0;
          var newIdx = 0;
          var nextOldFiber = null;

          for (; oldFiber !== null && newIdx < newChildren.length; newIdx++) {
            if (oldFiber.index > newIdx) {
              nextOldFiber = oldFiber;
              oldFiber = null;
            } else {
              nextOldFiber = oldFiber.sibling;
            }

            var newFiber = updateSlot(returnFiber, oldFiber, newChildren[newIdx], expirationTime);

            if (newFiber === null) {
              if (oldFiber === null) {
                oldFiber = nextOldFiber;
              }

              break;
            }

            if (shouldTrackSideEffects) {
              if (oldFiber && newFiber.alternate === null) {
                deleteChild(returnFiber, oldFiber);
              }
            }

            lastPlacedIndex = placeChild(newFiber, lastPlacedIndex, newIdx);

            if (previousNewFiber === null) {
              resultingFirstChild = newFiber;
            } else {
              previousNewFiber.sibling = newFiber;
            }

            previousNewFiber = newFiber;
            oldFiber = nextOldFiber;
          }

          if (newIdx === newChildren.length) {
            deleteRemainingChildren(returnFiber, oldFiber);
            return resultingFirstChild;
          }

          if (oldFiber === null) {
            for (; newIdx < newChildren.length; newIdx++) {
              var _newFiber = createChild(returnFiber, newChildren[newIdx], expirationTime);

              if (!_newFiber) {
                continue;
              }

              lastPlacedIndex = placeChild(_newFiber, lastPlacedIndex, newIdx);

              if (previousNewFiber === null) {
                resultingFirstChild = _newFiber;
              } else {
                previousNewFiber.sibling = _newFiber;
              }

              previousNewFiber = _newFiber;
            }

            return resultingFirstChild;
          }

          var existingChildren = mapRemainingChildren(returnFiber, oldFiber);

          for (; newIdx < newChildren.length; newIdx++) {
            var _newFiber2 = updateFromMap(existingChildren, returnFiber, newIdx, newChildren[newIdx], expirationTime);

            if (_newFiber2) {
              if (shouldTrackSideEffects) {
                if (_newFiber2.alternate !== null) {
                  existingChildren["delete"](_newFiber2.key === null ? newIdx : _newFiber2.key);
                }
              }

              lastPlacedIndex = placeChild(_newFiber2, lastPlacedIndex, newIdx);

              if (previousNewFiber === null) {
                resultingFirstChild = _newFiber2;
              } else {
                previousNewFiber.sibling = _newFiber2;
              }

              previousNewFiber = _newFiber2;
            }
          }

          if (shouldTrackSideEffects) {
            existingChildren.forEach(function (child) {
              return deleteChild(returnFiber, child);
            });
          }

          return resultingFirstChild;
        }

        function reconcileChildrenIterator(returnFiber, currentFirstChild, newChildrenIterable, expirationTime) {
          var iteratorFn = getIteratorFn(newChildrenIterable);
          invariant(typeof iteratorFn === "function", "An object is not an iterable. This error is likely caused by a bug in " + "React. Please file an issue.");
          {
            if (typeof newChildrenIterable.entries === "function") {
              var possibleMap = newChildrenIterable;

              if (possibleMap.entries === iteratorFn) {
                !didWarnAboutMaps ? warning(false, "Using Maps as children is unsupported and will likely yield " + "unexpected results. Convert it to a sequence/iterable of keyed " + "ReactElements instead.%s", getCurrentFiberStackAddendum$1()) : void 0;
                didWarnAboutMaps = true;
              }
            }

            var _newChildren = iteratorFn.call(newChildrenIterable);

            if (_newChildren) {
              var knownKeys = null;

              var _step = _newChildren.next();

              for (; !_step.done; _step = _newChildren.next()) {
                var child = _step.value;
                knownKeys = warnOnInvalidKey(child, knownKeys);
              }
            }
          }
          var newChildren = iteratorFn.call(newChildrenIterable);
          invariant(newChildren != null, "An iterable object provided no iterator.");
          var resultingFirstChild = null;
          var previousNewFiber = null;
          var oldFiber = currentFirstChild;
          var lastPlacedIndex = 0;
          var newIdx = 0;
          var nextOldFiber = null;
          var step = newChildren.next();

          for (; oldFiber !== null && !step.done; newIdx++, step = newChildren.next()) {
            if (oldFiber.index > newIdx) {
              nextOldFiber = oldFiber;
              oldFiber = null;
            } else {
              nextOldFiber = oldFiber.sibling;
            }

            var newFiber = updateSlot(returnFiber, oldFiber, step.value, expirationTime);

            if (newFiber === null) {
              if (!oldFiber) {
                oldFiber = nextOldFiber;
              }

              break;
            }

            if (shouldTrackSideEffects) {
              if (oldFiber && newFiber.alternate === null) {
                deleteChild(returnFiber, oldFiber);
              }
            }

            lastPlacedIndex = placeChild(newFiber, lastPlacedIndex, newIdx);

            if (previousNewFiber === null) {
              resultingFirstChild = newFiber;
            } else {
              previousNewFiber.sibling = newFiber;
            }

            previousNewFiber = newFiber;
            oldFiber = nextOldFiber;
          }

          if (step.done) {
            deleteRemainingChildren(returnFiber, oldFiber);
            return resultingFirstChild;
          }

          if (oldFiber === null) {
            for (; !step.done; newIdx++, step = newChildren.next()) {
              var _newFiber3 = createChild(returnFiber, step.value, expirationTime);

              if (_newFiber3 === null) {
                continue;
              }

              lastPlacedIndex = placeChild(_newFiber3, lastPlacedIndex, newIdx);

              if (previousNewFiber === null) {
                resultingFirstChild = _newFiber3;
              } else {
                previousNewFiber.sibling = _newFiber3;
              }

              previousNewFiber = _newFiber3;
            }

            return resultingFirstChild;
          }

          var existingChildren = mapRemainingChildren(returnFiber, oldFiber);

          for (; !step.done; newIdx++, step = newChildren.next()) {
            var _newFiber4 = updateFromMap(existingChildren, returnFiber, newIdx, step.value, expirationTime);

            if (_newFiber4 !== null) {
              if (shouldTrackSideEffects) {
                if (_newFiber4.alternate !== null) {
                  existingChildren["delete"](_newFiber4.key === null ? newIdx : _newFiber4.key);
                }
              }

              lastPlacedIndex = placeChild(_newFiber4, lastPlacedIndex, newIdx);

              if (previousNewFiber === null) {
                resultingFirstChild = _newFiber4;
              } else {
                previousNewFiber.sibling = _newFiber4;
              }

              previousNewFiber = _newFiber4;
            }
          }

          if (shouldTrackSideEffects) {
            existingChildren.forEach(function (child) {
              return deleteChild(returnFiber, child);
            });
          }

          return resultingFirstChild;
        }

        function reconcileSingleTextNode(returnFiber, currentFirstChild, textContent, expirationTime) {
          if (currentFirstChild !== null && currentFirstChild.tag === HostText) {
            deleteRemainingChildren(returnFiber, currentFirstChild.sibling);
            var existing = useFiber(currentFirstChild, textContent, expirationTime);
            existing["return"] = returnFiber;
            return existing;
          }

          deleteRemainingChildren(returnFiber, currentFirstChild);
          var created = createFiberFromText(textContent, returnFiber.mode, expirationTime);
          created["return"] = returnFiber;
          return created;
        }

        function reconcileSingleElement(returnFiber, currentFirstChild, element, expirationTime) {
          var key = element.key;
          var child = currentFirstChild;

          while (child !== null) {
            if (child.key === key) {
              if (child.tag === Fragment ? element.type === REACT_FRAGMENT_TYPE : child.type === element.type) {
                deleteRemainingChildren(returnFiber, child.sibling);
                var existing = useFiber(child, element.type === REACT_FRAGMENT_TYPE ? element.props.children : element.props, expirationTime);
                existing.ref = coerceRef(returnFiber, child, element);
                existing["return"] = returnFiber;
                {
                  existing._debugSource = element._source;
                  existing._debugOwner = element._owner;
                }
                return existing;
              } else {
                deleteRemainingChildren(returnFiber, child);
                break;
              }
            } else {
              deleteChild(returnFiber, child);
            }

            child = child.sibling;
          }

          if (element.type === REACT_FRAGMENT_TYPE) {
            var created = createFiberFromFragment(element.props.children, returnFiber.mode, expirationTime, element.key);
            created["return"] = returnFiber;
            return created;
          } else {
            var _created4 = createFiberFromElement(element, returnFiber.mode, expirationTime);

            _created4.ref = coerceRef(returnFiber, currentFirstChild, element);
            _created4["return"] = returnFiber;
            return _created4;
          }
        }

        function reconcileSinglePortal(returnFiber, currentFirstChild, portal, expirationTime) {
          var key = portal.key;
          var child = currentFirstChild;

          while (child !== null) {
            if (child.key === key) {
              if (child.tag === HostPortal && child.stateNode.containerInfo === portal.containerInfo && child.stateNode.implementation === portal.implementation) {
                deleteRemainingChildren(returnFiber, child.sibling);
                var existing = useFiber(child, portal.children || [], expirationTime);
                existing["return"] = returnFiber;
                return existing;
              } else {
                deleteRemainingChildren(returnFiber, child);
                break;
              }
            } else {
              deleteChild(returnFiber, child);
            }

            child = child.sibling;
          }

          var created = createFiberFromPortal(portal, returnFiber.mode, expirationTime);
          created["return"] = returnFiber;
          return created;
        }

        function reconcileChildFibers(returnFiber, currentFirstChild, newChild, expirationTime) {
          if (typeof newChild === "object" && newChild !== null && newChild.type === REACT_FRAGMENT_TYPE && newChild.key === null) {
            newChild = newChild.props.children;
          }

          var isObject = typeof newChild === "object" && newChild !== null;

          if (isObject) {
            switch (newChild.$$typeof) {
              case REACT_ELEMENT_TYPE:
                return placeSingleChild(reconcileSingleElement(returnFiber, currentFirstChild, newChild, expirationTime));

              case REACT_PORTAL_TYPE:
                return placeSingleChild(reconcileSinglePortal(returnFiber, currentFirstChild, newChild, expirationTime));
            }
          }

          if (typeof newChild === "string" || typeof newChild === "number") {
            return placeSingleChild(reconcileSingleTextNode(returnFiber, currentFirstChild, "" + newChild, expirationTime));
          }

          if (isArray$1(newChild)) {
            return reconcileChildrenArray(returnFiber, currentFirstChild, newChild, expirationTime);
          }

          if (getIteratorFn(newChild)) {
            return reconcileChildrenIterator(returnFiber, currentFirstChild, newChild, expirationTime);
          }

          if (isObject) {
            throwOnInvalidObjectType(returnFiber, newChild);
          }

          {
            if (typeof newChild === "function") {
              warnOnFunctionType();
            }
          }

          if (typeof newChild === "undefined") {
            switch (returnFiber.tag) {
              case ClassComponent:
                {
                  {
                    var instance = returnFiber.stateNode;

                    if (instance.render._isMockFunction) {
                      break;
                    }
                  }
                }

              case FunctionalComponent:
                {
                  var Component = returnFiber.type;
                  invariant(false, "%s(...): Nothing was returned from render. This usually means a " + "return statement is missing. Or, to render nothing, " + "return null.", Component.displayName || Component.name || "Component");
                }
            }
          }

          return deleteRemainingChildren(returnFiber, currentFirstChild);
        }

        return reconcileChildFibers;
      }

      var reconcileChildFibers = ChildReconciler(true);
      var mountChildFibers = ChildReconciler(false);

      function cloneChildFibers(current, workInProgress) {
        invariant(current === null || workInProgress.child === current.child, "Resuming work not yet implemented.");

        if (workInProgress.child === null) {
          return;
        }

        var currentChild = workInProgress.child;
        var newChild = createWorkInProgress(currentChild, currentChild.pendingProps, currentChild.expirationTime);
        workInProgress.child = newChild;
        newChild["return"] = workInProgress;

        while (currentChild.sibling !== null) {
          currentChild = currentChild.sibling;
          newChild = newChild.sibling = createWorkInProgress(currentChild, currentChild.pendingProps, currentChild.expirationTime);
          newChild["return"] = workInProgress;
        }

        newChild.sibling = null;
      }

      var didWarnAboutBadClass = void 0;
      var didWarnAboutGetDerivedStateOnFunctionalComponent = void 0;
      var didWarnAboutStatelessRefs = void 0;
      {
        didWarnAboutBadClass = {};
        didWarnAboutGetDerivedStateOnFunctionalComponent = {};
        didWarnAboutStatelessRefs = {};
      }

      var ReactFiberBeginWork = function ReactFiberBeginWork(config, hostContext, legacyContext, newContext, hydrationContext, scheduleWork, computeExpirationForFiber) {
        var shouldSetTextContent = config.shouldSetTextContent,
            shouldDeprioritizeSubtree = config.shouldDeprioritizeSubtree;
        var pushHostContext = hostContext.pushHostContext,
            pushHostContainer = hostContext.pushHostContainer;
        var pushProvider = newContext.pushProvider;
        var getMaskedContext = legacyContext.getMaskedContext,
            getUnmaskedContext = legacyContext.getUnmaskedContext,
            hasLegacyContextChanged = legacyContext.hasContextChanged,
            pushLegacyContextProvider = legacyContext.pushContextProvider,
            pushTopLevelContextObject = legacyContext.pushTopLevelContextObject,
            invalidateContextProvider = legacyContext.invalidateContextProvider;
        var enterHydrationState = hydrationContext.enterHydrationState,
            resetHydrationState = hydrationContext.resetHydrationState,
            tryToClaimNextHydratableInstance = hydrationContext.tryToClaimNextHydratableInstance;

        var _ReactFiberClassCompo = ReactFiberClassComponent(legacyContext, scheduleWork, computeExpirationForFiber, memoizeProps, memoizeState),
            adoptClassInstance = _ReactFiberClassCompo.adoptClassInstance,
            callGetDerivedStateFromProps = _ReactFiberClassCompo.callGetDerivedStateFromProps,
            constructClassInstance = _ReactFiberClassCompo.constructClassInstance,
            mountClassInstance = _ReactFiberClassCompo.mountClassInstance,
            resumeMountClassInstance = _ReactFiberClassCompo.resumeMountClassInstance,
            updateClassInstance = _ReactFiberClassCompo.updateClassInstance;

        function reconcileChildren(current, workInProgress, nextChildren) {
          reconcileChildrenAtExpirationTime(current, workInProgress, nextChildren, workInProgress.expirationTime);
        }

        function reconcileChildrenAtExpirationTime(current, workInProgress, nextChildren, renderExpirationTime) {
          if (current === null) {
            workInProgress.child = mountChildFibers(workInProgress, null, nextChildren, renderExpirationTime);
          } else {
            workInProgress.child = reconcileChildFibers(workInProgress, current.child, nextChildren, renderExpirationTime);
          }
        }

        function updateForwardRef(current, workInProgress) {
          var render = workInProgress.type.render;
          var nextChildren = render(workInProgress.pendingProps, workInProgress.ref);
          reconcileChildren(current, workInProgress, nextChildren);
          memoizeProps(workInProgress, nextChildren);
          return workInProgress.child;
        }

        function updateFragment(current, workInProgress) {
          var nextChildren = workInProgress.pendingProps;

          if (hasLegacyContextChanged()) {} else if (workInProgress.memoizedProps === nextChildren) {
            return bailoutOnAlreadyFinishedWork(current, workInProgress);
          }

          reconcileChildren(current, workInProgress, nextChildren);
          memoizeProps(workInProgress, nextChildren);
          return workInProgress.child;
        }

        function updateMode(current, workInProgress) {
          var nextChildren = workInProgress.pendingProps.children;

          if (hasLegacyContextChanged()) {} else if (nextChildren === null || workInProgress.memoizedProps === nextChildren) {
            return bailoutOnAlreadyFinishedWork(current, workInProgress);
          }

          reconcileChildren(current, workInProgress, nextChildren);
          memoizeProps(workInProgress, nextChildren);
          return workInProgress.child;
        }

        function markRef(current, workInProgress) {
          var ref = workInProgress.ref;

          if (current === null && ref !== null || current !== null && current.ref !== ref) {
            workInProgress.effectTag |= Ref;
          }
        }

        function updateFunctionalComponent(current, workInProgress) {
          var fn = workInProgress.type;
          var nextProps = workInProgress.pendingProps;

          if (hasLegacyContextChanged()) {} else {
            if (workInProgress.memoizedProps === nextProps) {
              return bailoutOnAlreadyFinishedWork(current, workInProgress);
            }
          }

          var unmaskedContext = getUnmaskedContext(workInProgress);
          var context = getMaskedContext(workInProgress, unmaskedContext);
          var nextChildren = void 0;
          {
            ReactCurrentOwner.current = workInProgress;
            ReactDebugCurrentFiber.setCurrentPhase("render");
            nextChildren = fn(nextProps, context);
            ReactDebugCurrentFiber.setCurrentPhase(null);
          }
          workInProgress.effectTag |= PerformedWork;
          reconcileChildren(current, workInProgress, nextChildren);
          memoizeProps(workInProgress, nextProps);
          return workInProgress.child;
        }

        function updateClassComponent(current, workInProgress, renderExpirationTime) {
          var hasContext = pushLegacyContextProvider(workInProgress);
          var shouldUpdate = void 0;

          if (current === null) {
            if (workInProgress.stateNode === null) {
              constructClassInstance(workInProgress, workInProgress.pendingProps);
              mountClassInstance(workInProgress, renderExpirationTime);
              shouldUpdate = true;
            } else {
              shouldUpdate = resumeMountClassInstance(workInProgress, renderExpirationTime);
            }
          } else {
            shouldUpdate = updateClassInstance(current, workInProgress, renderExpirationTime);
          }

          var didCaptureError = false;
          var updateQueue = workInProgress.updateQueue;

          if (updateQueue !== null && updateQueue.capturedValues !== null) {
            shouldUpdate = true;
            didCaptureError = true;
          }

          return finishClassComponent(current, workInProgress, shouldUpdate, hasContext, didCaptureError, renderExpirationTime);
        }

        function finishClassComponent(current, workInProgress, shouldUpdate, hasContext, didCaptureError, renderExpirationTime) {
          markRef(current, workInProgress);

          if (!shouldUpdate && !didCaptureError) {
            if (hasContext) {
              invalidateContextProvider(workInProgress, false);
            }

            return bailoutOnAlreadyFinishedWork(current, workInProgress);
          }

          var ctor = workInProgress.type;
          var instance = workInProgress.stateNode;
          ReactCurrentOwner.current = workInProgress;
          var nextChildren = void 0;

          if (didCaptureError && (!enableGetDerivedStateFromCatch || typeof ctor.getDerivedStateFromCatch !== "function")) {
            nextChildren = null;
          } else {
            {
              ReactDebugCurrentFiber.setCurrentPhase("render");
              nextChildren = instance.render();

              if (debugRenderPhaseSideEffects || debugRenderPhaseSideEffectsForStrictMode && workInProgress.mode & StrictMode) {
                instance.render();
              }

              ReactDebugCurrentFiber.setCurrentPhase(null);
            }
          }

          workInProgress.effectTag |= PerformedWork;

          if (didCaptureError) {
            reconcileChildrenAtExpirationTime(current, workInProgress, null, renderExpirationTime);
            workInProgress.child = null;
          }

          reconcileChildrenAtExpirationTime(current, workInProgress, nextChildren, renderExpirationTime);
          memoizeState(workInProgress, instance.state);
          memoizeProps(workInProgress, instance.props);

          if (hasContext) {
            invalidateContextProvider(workInProgress, true);
          }

          return workInProgress.child;
        }

        function pushHostRootContext(workInProgress) {
          var root = workInProgress.stateNode;

          if (root.pendingContext) {
            pushTopLevelContextObject(workInProgress, root.pendingContext, root.pendingContext !== root.context);
          } else if (root.context) {
            pushTopLevelContextObject(workInProgress, root.context, false);
          }

          pushHostContainer(workInProgress, root.containerInfo);
        }

        function updateHostRoot(current, workInProgress, renderExpirationTime) {
          pushHostRootContext(workInProgress);
          var updateQueue = workInProgress.updateQueue;

          if (updateQueue !== null) {
            var prevState = workInProgress.memoizedState;
            var state = processUpdateQueue(current, workInProgress, updateQueue, null, null, renderExpirationTime);
            memoizeState(workInProgress, state);
            updateQueue = workInProgress.updateQueue;
            var element = void 0;

            if (updateQueue !== null && updateQueue.capturedValues !== null) {
              element = null;
            } else if (prevState === state) {
              resetHydrationState();
              return bailoutOnAlreadyFinishedWork(current, workInProgress);
            } else {
              element = state.element;
            }

            var root = workInProgress.stateNode;

            if ((current === null || current.child === null) && root.hydrate && enterHydrationState(workInProgress)) {
              workInProgress.effectTag |= Placement;
              workInProgress.child = mountChildFibers(workInProgress, null, element, renderExpirationTime);
            } else {
              resetHydrationState();
              reconcileChildren(current, workInProgress, element);
            }

            memoizeState(workInProgress, state);
            return workInProgress.child;
          }

          resetHydrationState();
          return bailoutOnAlreadyFinishedWork(current, workInProgress);
        }

        function updateHostComponent(current, workInProgress, renderExpirationTime) {
          pushHostContext(workInProgress);

          if (current === null) {
            tryToClaimNextHydratableInstance(workInProgress);
          }

          var type = workInProgress.type;
          var memoizedProps = workInProgress.memoizedProps;
          var nextProps = workInProgress.pendingProps;
          var prevProps = current !== null ? current.memoizedProps : null;

          if (hasLegacyContextChanged()) {} else if (memoizedProps === nextProps) {
            var isHidden = workInProgress.mode & AsyncMode && shouldDeprioritizeSubtree(type, nextProps);

            if (isHidden) {
              workInProgress.expirationTime = Never;
            }

            if (!isHidden || renderExpirationTime !== Never) {
              return bailoutOnAlreadyFinishedWork(current, workInProgress);
            }
          }

          var nextChildren = nextProps.children;
          var isDirectTextChild = shouldSetTextContent(type, nextProps);

          if (isDirectTextChild) {
            nextChildren = null;
          } else if (prevProps && shouldSetTextContent(type, prevProps)) {
            workInProgress.effectTag |= ContentReset;
          }

          markRef(current, workInProgress);

          if (renderExpirationTime !== Never && workInProgress.mode & AsyncMode && shouldDeprioritizeSubtree(type, nextProps)) {
            workInProgress.expirationTime = Never;
            workInProgress.memoizedProps = nextProps;
            return null;
          }

          reconcileChildren(current, workInProgress, nextChildren);
          memoizeProps(workInProgress, nextProps);
          return workInProgress.child;
        }

        function updateHostText(current, workInProgress) {
          if (current === null) {
            tryToClaimNextHydratableInstance(workInProgress);
          }

          var nextProps = workInProgress.pendingProps;
          memoizeProps(workInProgress, nextProps);
          return null;
        }

        function mountIndeterminateComponent(current, workInProgress, renderExpirationTime) {
          invariant(current === null, "An indeterminate component should never have mounted. This error is " + "likely caused by a bug in React. Please file an issue.");
          var fn = workInProgress.type;
          var props = workInProgress.pendingProps;
          var unmaskedContext = getUnmaskedContext(workInProgress);
          var context = getMaskedContext(workInProgress, unmaskedContext);
          var value = void 0;
          {
            if (fn.prototype && typeof fn.prototype.render === "function") {
              var componentName = getComponentName(workInProgress) || "Unknown";

              if (!didWarnAboutBadClass[componentName]) {
                warning(false, "The <%s /> component appears to have a render method, but doesn't extend React.Component. " + "This is likely to cause errors. Change %s to extend React.Component instead.", componentName, componentName);
                didWarnAboutBadClass[componentName] = true;
              }
            }

            ReactCurrentOwner.current = workInProgress;
            value = fn(props, context);
          }
          workInProgress.effectTag |= PerformedWork;

          if (typeof value === "object" && value !== null && typeof value.render === "function" && value.$$typeof === undefined) {
            var Component = workInProgress.type;
            workInProgress.tag = ClassComponent;
            workInProgress.memoizedState = value.state !== null && value.state !== undefined ? value.state : null;

            if (typeof Component.getDerivedStateFromProps === "function") {
              var partialState = callGetDerivedStateFromProps(workInProgress, value, props, workInProgress.memoizedState);

              if (partialState !== null && partialState !== undefined) {
                workInProgress.memoizedState = babelHelpers.extends({}, workInProgress.memoizedState, partialState);
              }
            }

            var hasContext = pushLegacyContextProvider(workInProgress);
            adoptClassInstance(workInProgress, value);
            mountClassInstance(workInProgress, renderExpirationTime);
            return finishClassComponent(current, workInProgress, true, hasContext, false, renderExpirationTime);
          } else {
            workInProgress.tag = FunctionalComponent;
            {
              var _Component = workInProgress.type;

              if (_Component) {
                !!_Component.childContextTypes ? warning(false, "%s(...): childContextTypes cannot be defined on a functional component.", _Component.displayName || _Component.name || "Component") : void 0;
              }

              if (workInProgress.ref !== null) {
                var info = "";
                var ownerName = ReactDebugCurrentFiber.getCurrentFiberOwnerName();

                if (ownerName) {
                  info += "\n\nCheck the render method of `" + ownerName + "`.";
                }

                var warningKey = ownerName || workInProgress._debugID || "";
                var debugSource = workInProgress._debugSource;

                if (debugSource) {
                  warningKey = debugSource.fileName + ":" + debugSource.lineNumber;
                }

                if (!didWarnAboutStatelessRefs[warningKey]) {
                  didWarnAboutStatelessRefs[warningKey] = true;
                  warning(false, "Stateless function components cannot be given refs. " + "Attempts to access this ref will fail.%s%s", info, ReactDebugCurrentFiber.getCurrentFiberStackAddendum());
                }
              }

              if (typeof fn.getDerivedStateFromProps === "function") {
                var _componentName = getComponentName(workInProgress) || "Unknown";

                if (!didWarnAboutGetDerivedStateOnFunctionalComponent[_componentName]) {
                  warning(false, "%s: Stateless functional components do not support getDerivedStateFromProps.", _componentName);
                  didWarnAboutGetDerivedStateOnFunctionalComponent[_componentName] = true;
                }
              }
            }
            reconcileChildren(current, workInProgress, value);
            memoizeProps(workInProgress, props);
            return workInProgress.child;
          }
        }

        function updateCallComponent(current, workInProgress, renderExpirationTime) {
          var nextProps = workInProgress.pendingProps;

          if (hasLegacyContextChanged()) {} else if (workInProgress.memoizedProps === nextProps) {
            nextProps = workInProgress.memoizedProps;
          }

          var nextChildren = nextProps.children;

          if (current === null) {
            workInProgress.stateNode = mountChildFibers(workInProgress, workInProgress.stateNode, nextChildren, renderExpirationTime);
          } else {
            workInProgress.stateNode = reconcileChildFibers(workInProgress, current.stateNode, nextChildren, renderExpirationTime);
          }

          memoizeProps(workInProgress, nextProps);
          return workInProgress.stateNode;
        }

        function updatePortalComponent(current, workInProgress, renderExpirationTime) {
          pushHostContainer(workInProgress, workInProgress.stateNode.containerInfo);
          var nextChildren = workInProgress.pendingProps;

          if (hasLegacyContextChanged()) {} else if (workInProgress.memoizedProps === nextChildren) {
            return bailoutOnAlreadyFinishedWork(current, workInProgress);
          }

          if (current === null) {
            workInProgress.child = reconcileChildFibers(workInProgress, null, nextChildren, renderExpirationTime);
            memoizeProps(workInProgress, nextChildren);
          } else {
            reconcileChildren(current, workInProgress, nextChildren);
            memoizeProps(workInProgress, nextChildren);
          }

          return workInProgress.child;
        }

        function propagateContextChange(workInProgress, context, changedBits, renderExpirationTime) {
          var fiber = workInProgress.child;

          if (fiber !== null) {
            fiber["return"] = workInProgress;
          }

          while (fiber !== null) {
            var nextFiber = void 0;

            switch (fiber.tag) {
              case ContextConsumer:
                var observedBits = fiber.stateNode | 0;

                if (fiber.type === context && (observedBits & changedBits) !== 0) {
                  var node = fiber;

                  while (node !== null) {
                    var alternate = node.alternate;

                    if (node.expirationTime === NoWork || node.expirationTime > renderExpirationTime) {
                      node.expirationTime = renderExpirationTime;

                      if (alternate !== null && (alternate.expirationTime === NoWork || alternate.expirationTime > renderExpirationTime)) {
                        alternate.expirationTime = renderExpirationTime;
                      }
                    } else if (alternate !== null && (alternate.expirationTime === NoWork || alternate.expirationTime > renderExpirationTime)) {
                      alternate.expirationTime = renderExpirationTime;
                    } else {
                      break;
                    }

                    node = node["return"];
                  }

                  nextFiber = null;
                } else {
                  nextFiber = fiber.child;
                }

                break;

              case ContextProvider:
                nextFiber = fiber.type === workInProgress.type ? null : fiber.child;
                break;

              default:
                nextFiber = fiber.child;
                break;
            }

            if (nextFiber !== null) {
              nextFiber["return"] = fiber;
            } else {
              nextFiber = fiber;

              while (nextFiber !== null) {
                if (nextFiber === workInProgress) {
                  nextFiber = null;
                  break;
                }

                var sibling = nextFiber.sibling;

                if (sibling !== null) {
                  nextFiber = sibling;
                  break;
                }

                nextFiber = nextFiber["return"];
              }
            }

            fiber = nextFiber;
          }
        }

        function updateContextProvider(current, workInProgress, renderExpirationTime) {
          var providerType = workInProgress.type;
          var context = providerType._context;
          var newProps = workInProgress.pendingProps;
          var oldProps = workInProgress.memoizedProps;

          if (hasLegacyContextChanged()) {} else if (oldProps === newProps) {
            workInProgress.stateNode = 0;
            pushProvider(workInProgress);
            return bailoutOnAlreadyFinishedWork(current, workInProgress);
          }

          var newValue = newProps.value;
          workInProgress.memoizedProps = newProps;
          var changedBits = void 0;

          if (oldProps === null) {
            changedBits = MAX_SIGNED_31_BIT_INT;
          } else {
            if (oldProps.value === newProps.value) {
              if (oldProps.children === newProps.children) {
                workInProgress.stateNode = 0;
                pushProvider(workInProgress);
                return bailoutOnAlreadyFinishedWork(current, workInProgress);
              }

              changedBits = 0;
            } else {
              var oldValue = oldProps.value;

              if (oldValue === newValue && (oldValue !== 0 || 1 / oldValue === 1 / newValue) || oldValue !== oldValue && newValue !== newValue) {
                  if (oldProps.children === newProps.children) {
                    workInProgress.stateNode = 0;
                    pushProvider(workInProgress);
                    return bailoutOnAlreadyFinishedWork(current, workInProgress);
                  }

                  changedBits = 0;
                } else {
                changedBits = typeof context._calculateChangedBits === "function" ? context._calculateChangedBits(oldValue, newValue) : MAX_SIGNED_31_BIT_INT;
                {
                  !((changedBits & MAX_SIGNED_31_BIT_INT) === changedBits) ? warning(false, "calculateChangedBits: Expected the return value to be a " + "31-bit integer. Instead received: %s", changedBits) : void 0;
                }
                changedBits |= 0;

                if (changedBits === 0) {
                  if (oldProps.children === newProps.children) {
                    workInProgress.stateNode = 0;
                    pushProvider(workInProgress);
                    return bailoutOnAlreadyFinishedWork(current, workInProgress);
                  }
                } else {
                  propagateContextChange(workInProgress, context, changedBits, renderExpirationTime);
                }
              }
            }
          }

          workInProgress.stateNode = changedBits;
          pushProvider(workInProgress);
          var newChildren = newProps.children;
          reconcileChildren(current, workInProgress, newChildren);
          return workInProgress.child;
        }

        function updateContextConsumer(current, workInProgress, renderExpirationTime) {
          var context = workInProgress.type;
          var newProps = workInProgress.pendingProps;
          var oldProps = workInProgress.memoizedProps;
          var newValue = context._currentValue;
          var changedBits = context._changedBits;

          if (hasLegacyContextChanged()) {} else if (changedBits === 0 && oldProps === newProps) {
            return bailoutOnAlreadyFinishedWork(current, workInProgress);
          }

          workInProgress.memoizedProps = newProps;
          var observedBits = newProps.unstable_observedBits;

          if (observedBits === undefined || observedBits === null) {
            observedBits = MAX_SIGNED_31_BIT_INT;
          }

          workInProgress.stateNode = observedBits;

          if ((changedBits & observedBits) !== 0) {
            propagateContextChange(workInProgress, context, changedBits, renderExpirationTime);
          }

          var render = newProps.children;
          {
            !(typeof render === "function") ? warning(false, "A context consumer was rendered with multiple children, or a child " + "that isn't a function. A context consumer expects a single child " + "that is a function. If you did pass a function, make sure there " + "is no trailing or leading whitespace around it.") : void 0;
          }
          var newChildren = render(newValue);
          reconcileChildren(current, workInProgress, newChildren);
          return workInProgress.child;
        }

        function bailoutOnAlreadyFinishedWork(current, workInProgress) {
          cancelWorkTimer(workInProgress);
          cloneChildFibers(current, workInProgress);
          return workInProgress.child;
        }

        function bailoutOnLowPriority(current, workInProgress) {
          cancelWorkTimer(workInProgress);

          switch (workInProgress.tag) {
            case HostRoot:
              pushHostRootContext(workInProgress);
              break;

            case ClassComponent:
              pushLegacyContextProvider(workInProgress);
              break;

            case HostPortal:
              pushHostContainer(workInProgress, workInProgress.stateNode.containerInfo);
              break;

            case ContextProvider:
              pushProvider(workInProgress);
              break;
          }

          return null;
        }

        function memoizeProps(workInProgress, nextProps) {
          workInProgress.memoizedProps = nextProps;
        }

        function memoizeState(workInProgress, nextState) {
          workInProgress.memoizedState = nextState;
        }

        function beginWork(current, workInProgress, renderExpirationTime) {
          if (workInProgress.expirationTime === NoWork || workInProgress.expirationTime > renderExpirationTime) {
            return bailoutOnLowPriority(current, workInProgress);
          }

          switch (workInProgress.tag) {
            case IndeterminateComponent:
              return mountIndeterminateComponent(current, workInProgress, renderExpirationTime);

            case FunctionalComponent:
              return updateFunctionalComponent(current, workInProgress);

            case ClassComponent:
              return updateClassComponent(current, workInProgress, renderExpirationTime);

            case HostRoot:
              return updateHostRoot(current, workInProgress, renderExpirationTime);

            case HostComponent:
              return updateHostComponent(current, workInProgress, renderExpirationTime);

            case HostText:
              return updateHostText(current, workInProgress);

            case CallHandlerPhase:
              workInProgress.tag = CallComponent;

            case CallComponent:
              return updateCallComponent(current, workInProgress, renderExpirationTime);

            case ReturnComponent:
              return null;

            case HostPortal:
              return updatePortalComponent(current, workInProgress, renderExpirationTime);

            case ForwardRef:
              return updateForwardRef(current, workInProgress);

            case Fragment:
              return updateFragment(current, workInProgress);

            case Mode:
              return updateMode(current, workInProgress);

            case ContextProvider:
              return updateContextProvider(current, workInProgress, renderExpirationTime);

            case ContextConsumer:
              return updateContextConsumer(current, workInProgress, renderExpirationTime);

            default:
              invariant(false, "Unknown unit of work tag. This error is likely caused by a bug in " + "React. Please file an issue.");
          }
        }

        return {
          beginWork: beginWork
        };
      };

      var ReactFiberCompleteWork = function ReactFiberCompleteWork(config, hostContext, legacyContext, newContext, hydrationContext) {
        var createInstance = config.createInstance,
            createTextInstance = config.createTextInstance,
            appendInitialChild = config.appendInitialChild,
            finalizeInitialChildren = config.finalizeInitialChildren,
            prepareUpdate = config.prepareUpdate,
            mutation = config.mutation,
            persistence = config.persistence;
        var getRootHostContainer = hostContext.getRootHostContainer,
            popHostContext = hostContext.popHostContext,
            getHostContext = hostContext.getHostContext,
            popHostContainer = hostContext.popHostContainer;
        var popLegacyContextProvider = legacyContext.popContextProvider,
            popTopLevelLegacyContextObject = legacyContext.popTopLevelContextObject;
        var popProvider = newContext.popProvider;
        var prepareToHydrateHostInstance = hydrationContext.prepareToHydrateHostInstance,
            prepareToHydrateHostTextInstance = hydrationContext.prepareToHydrateHostTextInstance,
            popHydrationState = hydrationContext.popHydrationState;

        function markUpdate(workInProgress) {
          workInProgress.effectTag |= Update;
        }

        function markRef(workInProgress) {
          workInProgress.effectTag |= Ref;
        }

        function appendAllReturns(returns, workInProgress) {
          var node = workInProgress.stateNode;

          if (node) {
            node["return"] = workInProgress;
          }

          while (node !== null) {
            if (node.tag === HostComponent || node.tag === HostText || node.tag === HostPortal) {
              invariant(false, "A call cannot have host component children.");
            } else if (node.tag === ReturnComponent) {
              returns.push(node.pendingProps.value);
            } else if (node.child !== null) {
              node.child["return"] = node;
              node = node.child;
              continue;
            }

            while (node.sibling === null) {
              if (node["return"] === null || node["return"] === workInProgress) {
                return;
              }

              node = node["return"];
            }

            node.sibling["return"] = node["return"];
            node = node.sibling;
          }
        }

        function moveCallToHandlerPhase(current, workInProgress, renderExpirationTime) {
          var props = workInProgress.memoizedProps;
          invariant(props, "Should be resolved by now. This error is likely caused by a bug in " + "React. Please file an issue.");
          workInProgress.tag = CallHandlerPhase;
          var returns = [];
          appendAllReturns(returns, workInProgress);
          var fn = props.handler;
          var childProps = props.props;
          var nextChildren = fn(childProps, returns);
          var currentFirstChild = current !== null ? current.child : null;
          workInProgress.child = reconcileChildFibers(workInProgress, currentFirstChild, nextChildren, renderExpirationTime);
          return workInProgress.child;
        }

        function appendAllChildren(parent, workInProgress) {
          var node = workInProgress.child;

          while (node !== null) {
            if (node.tag === HostComponent || node.tag === HostText) {
              appendInitialChild(parent, node.stateNode);
            } else if (node.tag === HostPortal) {} else if (node.child !== null) {
              node.child["return"] = node;
              node = node.child;
              continue;
            }

            if (node === workInProgress) {
              return;
            }

            while (node.sibling === null) {
              if (node["return"] === null || node["return"] === workInProgress) {
                return;
              }

              node = node["return"];
            }

            node.sibling["return"] = node["return"];
            node = node.sibling;
          }
        }

        var updateHostContainer = void 0;
        var updateHostComponent = void 0;
        var updateHostText = void 0;

        if (mutation) {
          if (enableMutatingReconciler) {
            updateHostContainer = function updateHostContainer(workInProgress) {};

            updateHostComponent = function updateHostComponent(current, workInProgress, updatePayload, type, oldProps, newProps, rootContainerInstance, currentHostContext) {
              workInProgress.updateQueue = updatePayload;

              if (updatePayload) {
                markUpdate(workInProgress);
              }
            };

            updateHostText = function updateHostText(current, workInProgress, oldText, newText) {
              if (oldText !== newText) {
                markUpdate(workInProgress);
              }
            };
          } else {
            invariant(false, "Mutating reconciler is disabled.");
          }
        } else if (persistence) {
          if (enablePersistentReconciler) {
            var cloneInstance = persistence.cloneInstance,
                createContainerChildSet = persistence.createContainerChildSet,
                appendChildToContainerChildSet = persistence.appendChildToContainerChildSet,
                finalizeContainerChildren = persistence.finalizeContainerChildren;

            var appendAllChildrenToContainer = function appendAllChildrenToContainer(containerChildSet, workInProgress) {
              var node = workInProgress.child;

              while (node !== null) {
                if (node.tag === HostComponent || node.tag === HostText) {
                  appendChildToContainerChildSet(containerChildSet, node.stateNode);
                } else if (node.tag === HostPortal) {} else if (node.child !== null) {
                  node.child["return"] = node;
                  node = node.child;
                  continue;
                }

                if (node === workInProgress) {
                  return;
                }

                while (node.sibling === null) {
                  if (node["return"] === null || node["return"] === workInProgress) {
                    return;
                  }

                  node = node["return"];
                }

                node.sibling["return"] = node["return"];
                node = node.sibling;
              }
            };

            updateHostContainer = function updateHostContainer(workInProgress) {
              var portalOrRoot = workInProgress.stateNode;
              var childrenUnchanged = workInProgress.firstEffect === null;

              if (childrenUnchanged) {} else {
                var container = portalOrRoot.containerInfo;
                var newChildSet = createContainerChildSet(container);
                appendAllChildrenToContainer(newChildSet, workInProgress);
                portalOrRoot.pendingChildren = newChildSet;
                markUpdate(workInProgress);
                finalizeContainerChildren(container, newChildSet);
              }
            };

            updateHostComponent = function updateHostComponent(current, workInProgress, updatePayload, type, oldProps, newProps, rootContainerInstance, currentHostContext) {
              var childrenUnchanged = workInProgress.firstEffect === null;
              var currentInstance = current.stateNode;

              if (childrenUnchanged && updatePayload === null) {
                workInProgress.stateNode = currentInstance;
              } else {
                var recyclableInstance = workInProgress.stateNode;
                var newInstance = cloneInstance(currentInstance, updatePayload, type, oldProps, newProps, workInProgress, childrenUnchanged, recyclableInstance);

                if (finalizeInitialChildren(newInstance, type, newProps, rootContainerInstance, currentHostContext)) {
                  markUpdate(workInProgress);
                }

                workInProgress.stateNode = newInstance;

                if (childrenUnchanged) {
                  markUpdate(workInProgress);
                } else {
                  appendAllChildren(newInstance, workInProgress);
                }
              }
            };

            updateHostText = function updateHostText(current, workInProgress, oldText, newText) {
              if (oldText !== newText) {
                var rootContainerInstance = getRootHostContainer();
                var currentHostContext = getHostContext();
                workInProgress.stateNode = createTextInstance(newText, rootContainerInstance, currentHostContext, workInProgress);
                markUpdate(workInProgress);
              }
            };
          } else {
            invariant(false, "Persistent reconciler is disabled.");
          }
        } else {
          if (enableNoopReconciler) {
            updateHostContainer = function updateHostContainer(workInProgress) {};

            updateHostComponent = function updateHostComponent(current, workInProgress, updatePayload, type, oldProps, newProps, rootContainerInstance, currentHostContext) {};

            updateHostText = function updateHostText(current, workInProgress, oldText, newText) {};
          } else {
            invariant(false, "Noop reconciler is disabled.");
          }
        }

        function completeWork(current, workInProgress, renderExpirationTime) {
          var newProps = workInProgress.pendingProps;

          switch (workInProgress.tag) {
            case FunctionalComponent:
              return null;

            case ClassComponent:
              {
                popLegacyContextProvider(workInProgress);
                var instance = workInProgress.stateNode;
                var updateQueue = workInProgress.updateQueue;

                if (updateQueue !== null && updateQueue.capturedValues !== null) {
                  workInProgress.effectTag &= ~DidCapture;

                  if (typeof instance.componentDidCatch === "function") {
                    workInProgress.effectTag |= ErrLog;
                  } else {
                    updateQueue.capturedValues = null;
                  }
                }

                return null;
              }

            case HostRoot:
              {
                popHostContainer(workInProgress);
                popTopLevelLegacyContextObject(workInProgress);
                var fiberRoot = workInProgress.stateNode;

                if (fiberRoot.pendingContext) {
                  fiberRoot.context = fiberRoot.pendingContext;
                  fiberRoot.pendingContext = null;
                }

                if (current === null || current.child === null) {
                  popHydrationState(workInProgress);
                  workInProgress.effectTag &= ~Placement;
                }

                updateHostContainer(workInProgress);
                var _updateQueue = workInProgress.updateQueue;

                if (_updateQueue !== null && _updateQueue.capturedValues !== null) {
                  workInProgress.effectTag |= ErrLog;
                }

                return null;
              }

            case HostComponent:
              {
                popHostContext(workInProgress);
                var rootContainerInstance = getRootHostContainer();
                var type = workInProgress.type;

                if (current !== null && workInProgress.stateNode != null) {
                  var oldProps = current.memoizedProps;
                  var _instance = workInProgress.stateNode;
                  var currentHostContext = getHostContext();
                  var updatePayload = prepareUpdate(_instance, type, oldProps, newProps, rootContainerInstance, currentHostContext);
                  updateHostComponent(current, workInProgress, updatePayload, type, oldProps, newProps, rootContainerInstance, currentHostContext);

                  if (current.ref !== workInProgress.ref) {
                    markRef(workInProgress);
                  }
                } else {
                  if (!newProps) {
                    invariant(workInProgress.stateNode !== null, "We must have new props for new mounts. This error is likely " + "caused by a bug in React. Please file an issue.");
                    return null;
                  }

                  var _currentHostContext = getHostContext();

                  var wasHydrated = popHydrationState(workInProgress);

                  if (wasHydrated) {
                    if (prepareToHydrateHostInstance(workInProgress, rootContainerInstance, _currentHostContext)) {
                      markUpdate(workInProgress);
                    }
                  } else {
                    var _instance2 = createInstance(type, newProps, rootContainerInstance, _currentHostContext, workInProgress);

                    appendAllChildren(_instance2, workInProgress);

                    if (finalizeInitialChildren(_instance2, type, newProps, rootContainerInstance, _currentHostContext)) {
                      markUpdate(workInProgress);
                    }

                    workInProgress.stateNode = _instance2;
                  }

                  if (workInProgress.ref !== null) {
                    markRef(workInProgress);
                  }
                }

                return null;
              }

            case HostText:
              {
                var newText = newProps;

                if (current && workInProgress.stateNode != null) {
                  var oldText = current.memoizedProps;
                  updateHostText(current, workInProgress, oldText, newText);
                } else {
                  if (typeof newText !== "string") {
                    invariant(workInProgress.stateNode !== null, "We must have new props for new mounts. This error is likely " + "caused by a bug in React. Please file an issue.");
                    return null;
                  }

                  var _rootContainerInstance = getRootHostContainer();

                  var _currentHostContext2 = getHostContext();

                  var _wasHydrated = popHydrationState(workInProgress);

                  if (_wasHydrated) {
                    if (prepareToHydrateHostTextInstance(workInProgress)) {
                      markUpdate(workInProgress);
                    }
                  } else {
                    workInProgress.stateNode = createTextInstance(newText, _rootContainerInstance, _currentHostContext2, workInProgress);
                  }
                }

                return null;
              }

            case CallComponent:
              return moveCallToHandlerPhase(current, workInProgress, renderExpirationTime);

            case CallHandlerPhase:
              workInProgress.tag = CallComponent;
              return null;

            case ReturnComponent:
              return null;

            case ForwardRef:
              return null;

            case Fragment:
              return null;

            case Mode:
              return null;

            case HostPortal:
              popHostContainer(workInProgress);
              updateHostContainer(workInProgress);
              return null;

            case ContextProvider:
              popProvider(workInProgress);
              return null;

            case ContextConsumer:
              return null;

            case IndeterminateComponent:
              invariant(false, "An indeterminate component should have become determinate before " + "completing. This error is likely caused by a bug in React. Please " + "file an issue.");

            default:
              invariant(false, "Unknown unit of work tag. This error is likely caused by a bug in " + "React. Please file an issue.");
          }
        }

        return {
          completeWork: completeWork
        };
      };

      function createCapturedValue(value, source) {
        return {
          value: value,
          source: source,
          stack: getStackAddendumByWorkInProgressFiber(source)
        };
      }

      var ReactFiberUnwindWork = function ReactFiberUnwindWork(hostContext, legacyContext, newContext, scheduleWork, isAlreadyFailedLegacyErrorBoundary) {
        var popHostContainer = hostContext.popHostContainer,
            popHostContext = hostContext.popHostContext;
        var popLegacyContextProvider = legacyContext.popContextProvider,
            popTopLevelLegacyContextObject = legacyContext.popTopLevelContextObject;
        var popProvider = newContext.popProvider;

        function throwException(returnFiber, sourceFiber, rawValue) {
          sourceFiber.effectTag |= Incomplete;
          sourceFiber.firstEffect = sourceFiber.lastEffect = null;
          var value = createCapturedValue(rawValue, sourceFiber);
          var workInProgress = returnFiber;

          do {
            switch (workInProgress.tag) {
              case HostRoot:
                {
                  var errorInfo = value;
                  ensureUpdateQueues(workInProgress);
                  var updateQueue = workInProgress.updateQueue;
                  updateQueue.capturedValues = [errorInfo];
                  workInProgress.effectTag |= ShouldCapture;
                  return;
                }

              case ClassComponent:
                var ctor = workInProgress.type;
                var _instance = workInProgress.stateNode;

                if ((workInProgress.effectTag & DidCapture) === NoEffect && (typeof ctor.getDerivedStateFromCatch === "function" && enableGetDerivedStateFromCatch || _instance !== null && typeof _instance.componentDidCatch === "function" && !isAlreadyFailedLegacyErrorBoundary(_instance))) {
                  ensureUpdateQueues(workInProgress);
                  var _updateQueue = workInProgress.updateQueue;
                  var capturedValues = _updateQueue.capturedValues;

                  if (capturedValues === null) {
                    _updateQueue.capturedValues = [value];
                  } else {
                    capturedValues.push(value);
                  }

                  workInProgress.effectTag |= ShouldCapture;
                  return;
                }

                break;

              default:
                break;
            }

            workInProgress = workInProgress["return"];
          } while (workInProgress !== null);
        }

        function unwindWork(workInProgress) {
          switch (workInProgress.tag) {
            case ClassComponent:
              {
                popLegacyContextProvider(workInProgress);
                var effectTag = workInProgress.effectTag;

                if (effectTag & ShouldCapture) {
                  workInProgress.effectTag = effectTag & ~ShouldCapture | DidCapture;
                  return workInProgress;
                }

                return null;
              }

            case HostRoot:
              {
                popHostContainer(workInProgress);
                popTopLevelLegacyContextObject(workInProgress);
                var _effectTag = workInProgress.effectTag;

                if (_effectTag & ShouldCapture) {
                  workInProgress.effectTag = _effectTag & ~ShouldCapture | DidCapture;
                  return workInProgress;
                }

                return null;
              }

            case HostComponent:
              {
                popHostContext(workInProgress);
                return null;
              }

            case HostPortal:
              popHostContainer(workInProgress);
              return null;

            case ContextProvider:
              popProvider(workInProgress);
              return null;

            default:
              return null;
          }
        }

        function unwindInterruptedWork(interruptedWork) {
          switch (interruptedWork.tag) {
            case ClassComponent:
              {
                popLegacyContextProvider(interruptedWork);
                break;
              }

            case HostRoot:
              {
                popHostContainer(interruptedWork);
                popTopLevelLegacyContextObject(interruptedWork);
                break;
              }

            case HostComponent:
              {
                popHostContext(interruptedWork);
                break;
              }

            case HostPortal:
              popHostContainer(interruptedWork);
              break;

            case ContextProvider:
              popProvider(interruptedWork);
              break;

            default:
              break;
          }
        }

        return {
          throwException: throwException,
          unwindWork: unwindWork,
          unwindInterruptedWork: unwindInterruptedWork
        };
      };

      function showErrorDialog(capturedError) {
        var componentStack = capturedError.componentStack,
            error = capturedError.error;
        var errorToHandle = void 0;

        if (error instanceof Error) {
          var message = error.message,
              name = error.name;
          var summary = message ? name + ": " + message : name;
          errorToHandle = error;

          try {
            errorToHandle.message = summary + "\n\nThis error is located at:" + componentStack;
          } catch (e) {}
        } else if (typeof error === "string") {
          errorToHandle = new Error(error + "\n\nThis error is located at:" + componentStack);
        } else {
          errorToHandle = new Error("Unspecified error at:" + componentStack);
        }

        ExceptionsManager.handleException(errorToHandle, false);
        return false;
      }

      function logCapturedError(capturedError) {
        var logError = showErrorDialog(capturedError);

        if (logError === false) {
          return;
        }

        var error = capturedError.error;
        var suppressLogging = error && error.suppressReactErrorLogging;

        if (suppressLogging) {
          return;
        }

        {
          var componentName = capturedError.componentName,
              componentStack = capturedError.componentStack,
              errorBoundaryName = capturedError.errorBoundaryName,
              errorBoundaryFound = capturedError.errorBoundaryFound,
              willRetry = capturedError.willRetry;
          var componentNameMessage = componentName ? "The above error occurred in the <" + componentName + "> component:" : "The above error occurred in one of your React components:";
          var errorBoundaryMessage = void 0;

          if (errorBoundaryFound && errorBoundaryName) {
            if (willRetry) {
              errorBoundaryMessage = "React will try to recreate this component tree from scratch " + ("using the error boundary you provided, " + errorBoundaryName + ".");
            } else {
              errorBoundaryMessage = "This error was initially handled by the error boundary " + errorBoundaryName + ".\n" + "Recreating the tree from scratch failed so React will unmount the tree.";
            }
          } else {
            errorBoundaryMessage = "Consider adding an error boundary to your tree to customize error handling behavior.\n" + "Visit https://fb.me/react-error-boundaries to learn more about error boundaries.";
          }

          var combinedMessage = "" + componentNameMessage + componentStack + "\n\n" + ("" + errorBoundaryMessage);
          console.error(combinedMessage);
        }
      }

      var invokeGuardedCallback$3 = ReactErrorUtils.invokeGuardedCallback;
      var hasCaughtError$1 = ReactErrorUtils.hasCaughtError;
      var clearCaughtError$1 = ReactErrorUtils.clearCaughtError;
      var didWarnAboutUndefinedSnapshotBeforeUpdate = null;
      {
        didWarnAboutUndefinedSnapshotBeforeUpdate = new Set();
      }

      function logError(boundary, errorInfo) {
        var source = errorInfo.source;
        var stack = errorInfo.stack;

        if (stack === null) {
          stack = getStackAddendumByWorkInProgressFiber(source);
        }

        var capturedError = {
          componentName: source !== null ? getComponentName(source) : null,
          componentStack: stack !== null ? stack : "",
          error: errorInfo.value,
          errorBoundary: null,
          errorBoundaryName: null,
          errorBoundaryFound: false,
          willRetry: false
        };

        if (boundary !== null && boundary.tag === ClassComponent) {
          capturedError.errorBoundary = boundary.stateNode;
          capturedError.errorBoundaryName = getComponentName(boundary);
          capturedError.errorBoundaryFound = true;
          capturedError.willRetry = true;
        }

        try {
          logCapturedError(capturedError);
        } catch (e) {
          var suppressLogging = e && e.suppressReactErrorLogging;

          if (!suppressLogging) {
            console.error(e);
          }
        }
      }

      var ReactFiberCommitWork = function ReactFiberCommitWork(config, captureError, scheduleWork, computeExpirationForFiber, markLegacyErrorBoundaryAsFailed, recalculateCurrentTime) {
        var getPublicInstance = config.getPublicInstance,
            mutation = config.mutation,
            persistence = config.persistence;

        var callComponentWillUnmountWithTimer = function callComponentWillUnmountWithTimer(current, instance) {
          startPhaseTimer(current, "componentWillUnmount");
          instance.props = current.memoizedProps;
          instance.state = current.memoizedState;
          instance.componentWillUnmount();
          stopPhaseTimer();
        };

        function safelyCallComponentWillUnmount(current, instance) {
          {
            invokeGuardedCallback$3(null, callComponentWillUnmountWithTimer, null, current, instance);

            if (hasCaughtError$1()) {
              var unmountError = clearCaughtError$1();
              captureError(current, unmountError);
            }
          }
        }

        function safelyDetachRef(current) {
          var ref = current.ref;

          if (ref !== null) {
            if (typeof ref === "function") {
              {
                invokeGuardedCallback$3(null, ref, null, null);

                if (hasCaughtError$1()) {
                  var refError = clearCaughtError$1();
                  captureError(current, refError);
                }
              }
            } else {
              ref.current = null;
            }
          }
        }

        function commitBeforeMutationLifeCycles(current, finishedWork) {
          switch (finishedWork.tag) {
            case ClassComponent:
              {
                if (finishedWork.effectTag & Snapshot) {
                  if (current !== null) {
                    var prevProps = current.memoizedProps;
                    var prevState = current.memoizedState;
                    startPhaseTimer(finishedWork, "getSnapshotBeforeUpdate");
                    var _instance = finishedWork.stateNode;
                    _instance.props = finishedWork.memoizedProps;
                    _instance.state = finishedWork.memoizedState;

                    var snapshot = _instance.getSnapshotBeforeUpdate(prevProps, prevState);

                    {
                      var didWarnSet = didWarnAboutUndefinedSnapshotBeforeUpdate;

                      if (snapshot === undefined && !didWarnSet.has(finishedWork.type)) {
                        didWarnSet.add(finishedWork.type);
                        warning(false, "%s.getSnapshotBeforeUpdate(): A snapshot value (or null) " + "must be returned. You have returned undefined.", getComponentName(finishedWork));
                      }
                    }
                    _instance.__reactInternalSnapshotBeforeUpdate = snapshot;
                    stopPhaseTimer();
                  }
                }

                return;
              }

            case HostRoot:
            case HostComponent:
            case HostText:
            case HostPortal:
              return;

            default:
              {
                invariant(false, "This unit of work tag should not have side-effects. This error is " + "likely caused by a bug in React. Please file an issue.");
              }
          }
        }

        function commitLifeCycles(finishedRoot, current, finishedWork, currentTime, committedExpirationTime) {
          switch (finishedWork.tag) {
            case ClassComponent:
              {
                var _instance2 = finishedWork.stateNode;

                if (finishedWork.effectTag & Update) {
                  if (current === null) {
                    startPhaseTimer(finishedWork, "componentDidMount");
                    _instance2.props = finishedWork.memoizedProps;
                    _instance2.state = finishedWork.memoizedState;

                    _instance2.componentDidMount();

                    stopPhaseTimer();
                  } else {
                    var prevProps = current.memoizedProps;
                    var prevState = current.memoizedState;
                    startPhaseTimer(finishedWork, "componentDidUpdate");
                    _instance2.props = finishedWork.memoizedProps;
                    _instance2.state = finishedWork.memoizedState;

                    _instance2.componentDidUpdate(prevProps, prevState, _instance2.__reactInternalSnapshotBeforeUpdate);

                    stopPhaseTimer();
                  }
                }

                var updateQueue = finishedWork.updateQueue;

                if (updateQueue !== null) {
                  commitCallbacks(updateQueue, _instance2);
                }

                return;
              }

            case HostRoot:
              {
                var _updateQueue = finishedWork.updateQueue;

                if (_updateQueue !== null) {
                  var _instance3 = null;

                  if (finishedWork.child !== null) {
                    switch (finishedWork.child.tag) {
                      case HostComponent:
                        _instance3 = getPublicInstance(finishedWork.child.stateNode);
                        break;

                      case ClassComponent:
                        _instance3 = finishedWork.child.stateNode;
                        break;
                    }
                  }

                  commitCallbacks(_updateQueue, _instance3);
                }

                return;
              }

            case HostComponent:
              {
                var _instance4 = finishedWork.stateNode;

                if (current === null && finishedWork.effectTag & Update) {
                  var type = finishedWork.type;
                  var props = finishedWork.memoizedProps;
                  commitMount(_instance4, type, props, finishedWork);
                }

                return;
              }

            case HostText:
              {
                return;
              }

            case HostPortal:
              {
                return;
              }

            default:
              {
                invariant(false, "This unit of work tag should not have side-effects. This error is " + "likely caused by a bug in React. Please file an issue.");
              }
          }
        }

        function commitErrorLogging(finishedWork, onUncaughtError) {
          switch (finishedWork.tag) {
            case ClassComponent:
              {
                var ctor = finishedWork.type;
                var _instance5 = finishedWork.stateNode;
                var updateQueue = finishedWork.updateQueue;
                invariant(updateQueue !== null && updateQueue.capturedValues !== null, "An error logging effect should not have been scheduled if no errors " + "were captured. This error is likely caused by a bug in React. " + "Please file an issue.");
                var capturedErrors = updateQueue.capturedValues;
                updateQueue.capturedValues = null;

                if (typeof ctor.getDerivedStateFromCatch !== "function") {
                  markLegacyErrorBoundaryAsFailed(_instance5);
                }

                _instance5.props = finishedWork.memoizedProps;
                _instance5.state = finishedWork.memoizedState;

                for (var i = 0; i < capturedErrors.length; i++) {
                  var errorInfo = capturedErrors[i];
                  var _error = errorInfo.value;
                  var stack = errorInfo.stack;
                  logError(finishedWork, errorInfo);

                  _instance5.componentDidCatch(_error, {
                    componentStack: stack !== null ? stack : ""
                  });
                }
              }
              break;

            case HostRoot:
              {
                var _updateQueue2 = finishedWork.updateQueue;
                invariant(_updateQueue2 !== null && _updateQueue2.capturedValues !== null, "An error logging effect should not have been scheduled if no errors " + "were captured. This error is likely caused by a bug in React. " + "Please file an issue.");
                var _capturedErrors = _updateQueue2.capturedValues;
                _updateQueue2.capturedValues = null;

                for (var _i = 0; _i < _capturedErrors.length; _i++) {
                  var _errorInfo = _capturedErrors[_i];
                  logError(finishedWork, _errorInfo);
                  onUncaughtError(_errorInfo.value);
                }

                break;
              }

            default:
              invariant(false, "This unit of work tag cannot capture errors.  This error is " + "likely caused by a bug in React. Please file an issue.");
          }
        }

        function commitAttachRef(finishedWork) {
          var ref = finishedWork.ref;

          if (ref !== null) {
            var _instance6 = finishedWork.stateNode;
            var instanceToUse = void 0;

            switch (finishedWork.tag) {
              case HostComponent:
                instanceToUse = getPublicInstance(_instance6);
                break;

              default:
                instanceToUse = _instance6;
            }

            if (typeof ref === "function") {
              ref(instanceToUse);
            } else {
              {
                if (!ref.hasOwnProperty("current")) {
                  warning(false, "Unexpected ref object provided for %s. " + "Use either a ref-setter function or React.createRef().%s", getComponentName(finishedWork), getStackAddendumByWorkInProgressFiber(finishedWork));
                }
              }
              ref.current = instanceToUse;
            }
          }
        }

        function commitDetachRef(current) {
          var currentRef = current.ref;

          if (currentRef !== null) {
            if (typeof currentRef === "function") {
              currentRef(null);
            } else {
              currentRef.current = null;
            }
          }
        }

        function commitUnmount(current) {
          if (typeof onCommitUnmount === "function") {
            onCommitUnmount(current);
          }

          switch (current.tag) {
            case ClassComponent:
              {
                safelyDetachRef(current);
                var _instance7 = current.stateNode;

                if (typeof _instance7.componentWillUnmount === "function") {
                  safelyCallComponentWillUnmount(current, _instance7);
                }

                return;
              }

            case HostComponent:
              {
                safelyDetachRef(current);
                return;
              }

            case CallComponent:
              {
                commitNestedUnmounts(current.stateNode);
                return;
              }

            case HostPortal:
              {
                if (enableMutatingReconciler && mutation) {
                  unmountHostComponents(current);
                } else if (enablePersistentReconciler && persistence) {
                  emptyPortalContainer(current);
                }

                return;
              }
          }
        }

        function commitNestedUnmounts(root) {
          var node = root;

          while (true) {
            commitUnmount(node);

            if (node.child !== null && (!mutation || node.tag !== HostPortal)) {
              node.child["return"] = node;
              node = node.child;
              continue;
            }

            if (node === root) {
              return;
            }

            while (node.sibling === null) {
              if (node["return"] === null || node["return"] === root) {
                return;
              }

              node = node["return"];
            }

            node.sibling["return"] = node["return"];
            node = node.sibling;
          }
        }

        function detachFiber(current) {
          current["return"] = null;
          current.child = null;

          if (current.alternate) {
            current.alternate.child = null;
            current.alternate["return"] = null;
          }
        }

        var emptyPortalContainer = void 0;

        if (!mutation) {
          var commitContainer = void 0;

          if (persistence) {
            var replaceContainerChildren = persistence.replaceContainerChildren,
                createContainerChildSet = persistence.createContainerChildSet;

            emptyPortalContainer = function emptyPortalContainer(current) {
              var portal = current.stateNode;
              var containerInfo = portal.containerInfo;
              var emptyChildSet = createContainerChildSet(containerInfo);
              replaceContainerChildren(containerInfo, emptyChildSet);
            };

            commitContainer = function commitContainer(finishedWork) {
              switch (finishedWork.tag) {
                case ClassComponent:
                  {
                    return;
                  }

                case HostComponent:
                  {
                    return;
                  }

                case HostText:
                  {
                    return;
                  }

                case HostRoot:
                case HostPortal:
                  {
                    var portalOrRoot = finishedWork.stateNode;
                    var containerInfo = portalOrRoot.containerInfo,
                        _pendingChildren = portalOrRoot.pendingChildren;
                    replaceContainerChildren(containerInfo, _pendingChildren);
                    return;
                  }

                default:
                  {
                    invariant(false, "This unit of work tag should not have side-effects. This error is " + "likely caused by a bug in React. Please file an issue.");
                  }
              }
            };
          } else {
            commitContainer = function commitContainer(finishedWork) {};
          }

          if (enablePersistentReconciler || enableNoopReconciler) {
            return {
              commitResetTextContent: function commitResetTextContent(finishedWork) {},
              commitPlacement: function commitPlacement(finishedWork) {},
              commitDeletion: function commitDeletion(current) {
                commitNestedUnmounts(current);
                detachFiber(current);
              },
              commitWork: function commitWork(current, finishedWork) {
                commitContainer(finishedWork);
              },
              commitLifeCycles: commitLifeCycles,
              commitBeforeMutationLifeCycles: commitBeforeMutationLifeCycles,
              commitErrorLogging: commitErrorLogging,
              commitAttachRef: commitAttachRef,
              commitDetachRef: commitDetachRef
            };
          } else if (persistence) {
            invariant(false, "Persistent reconciler is disabled.");
          } else {
            invariant(false, "Noop reconciler is disabled.");
          }
        }

        var commitMount = mutation.commitMount,
            commitUpdate = mutation.commitUpdate,
            resetTextContent = mutation.resetTextContent,
            commitTextUpdate = mutation.commitTextUpdate,
            appendChild = mutation.appendChild,
            appendChildToContainer = mutation.appendChildToContainer,
            insertBefore = mutation.insertBefore,
            insertInContainerBefore = mutation.insertInContainerBefore,
            removeChild = mutation.removeChild,
            removeChildFromContainer = mutation.removeChildFromContainer;

        function getHostParentFiber(fiber) {
          var parent = fiber["return"];

          while (parent !== null) {
            if (isHostParent(parent)) {
              return parent;
            }

            parent = parent["return"];
          }

          invariant(false, "Expected to find a host parent. This error is likely caused by a bug " + "in React. Please file an issue.");
        }

        function isHostParent(fiber) {
          return fiber.tag === HostComponent || fiber.tag === HostRoot || fiber.tag === HostPortal;
        }

        function getHostSibling(fiber) {
          var node = fiber;

          siblings: while (true) {
            while (node.sibling === null) {
              if (node["return"] === null || isHostParent(node["return"])) {
                return null;
              }

              node = node["return"];
            }

            node.sibling["return"] = node["return"];
            node = node.sibling;

            while (node.tag !== HostComponent && node.tag !== HostText) {
              if (node.effectTag & Placement) {
                continue siblings;
              }

              if (node.child === null || node.tag === HostPortal) {
                continue siblings;
              } else {
                node.child["return"] = node;
                node = node.child;
              }
            }

            if (!(node.effectTag & Placement)) {
              return node.stateNode;
            }
          }
        }

        function commitPlacement(finishedWork) {
          var parentFiber = getHostParentFiber(finishedWork);
          var parent = void 0;
          var isContainer = void 0;

          switch (parentFiber.tag) {
            case HostComponent:
              parent = parentFiber.stateNode;
              isContainer = false;
              break;

            case HostRoot:
              parent = parentFiber.stateNode.containerInfo;
              isContainer = true;
              break;

            case HostPortal:
              parent = parentFiber.stateNode.containerInfo;
              isContainer = true;
              break;

            default:
              invariant(false, "Invalid host parent fiber. This error is likely caused by a bug " + "in React. Please file an issue.");
          }

          if (parentFiber.effectTag & ContentReset) {
            resetTextContent(parent);
            parentFiber.effectTag &= ~ContentReset;
          }

          var before = getHostSibling(finishedWork);
          var node = finishedWork;

          while (true) {
            if (node.tag === HostComponent || node.tag === HostText) {
              if (before) {
                if (isContainer) {
                  insertInContainerBefore(parent, node.stateNode, before);
                } else {
                  insertBefore(parent, node.stateNode, before);
                }
              } else {
                if (isContainer) {
                  appendChildToContainer(parent, node.stateNode);
                } else {
                  appendChild(parent, node.stateNode);
                }
              }
            } else if (node.tag === HostPortal) {} else if (node.child !== null) {
              node.child["return"] = node;
              node = node.child;
              continue;
            }

            if (node === finishedWork) {
              return;
            }

            while (node.sibling === null) {
              if (node["return"] === null || node["return"] === finishedWork) {
                return;
              }

              node = node["return"];
            }

            node.sibling["return"] = node["return"];
            node = node.sibling;
          }
        }

        function unmountHostComponents(current) {
          var node = current;
          var currentParentIsValid = false;
          var currentParent = void 0;
          var currentParentIsContainer = void 0;

          while (true) {
            if (!currentParentIsValid) {
              var parent = node["return"];

              findParent: while (true) {
                invariant(parent !== null, "Expected to find a host parent. This error is likely caused by " + "a bug in React. Please file an issue.");

                switch (parent.tag) {
                  case HostComponent:
                    currentParent = parent.stateNode;
                    currentParentIsContainer = false;
                    break findParent;

                  case HostRoot:
                    currentParent = parent.stateNode.containerInfo;
                    currentParentIsContainer = true;
                    break findParent;

                  case HostPortal:
                    currentParent = parent.stateNode.containerInfo;
                    currentParentIsContainer = true;
                    break findParent;
                }

                parent = parent["return"];
              }

              currentParentIsValid = true;
            }

            if (node.tag === HostComponent || node.tag === HostText) {
              commitNestedUnmounts(node);

              if (currentParentIsContainer) {
                removeChildFromContainer(currentParent, node.stateNode);
              } else {
                removeChild(currentParent, node.stateNode);
              }
            } else if (node.tag === HostPortal) {
              currentParent = node.stateNode.containerInfo;

              if (node.child !== null) {
                node.child["return"] = node;
                node = node.child;
                continue;
              }
            } else {
              commitUnmount(node);

              if (node.child !== null) {
                node.child["return"] = node;
                node = node.child;
                continue;
              }
            }

            if (node === current) {
              return;
            }

            while (node.sibling === null) {
              if (node["return"] === null || node["return"] === current) {
                return;
              }

              node = node["return"];

              if (node.tag === HostPortal) {
                currentParentIsValid = false;
              }
            }

            node.sibling["return"] = node["return"];
            node = node.sibling;
          }
        }

        function commitDeletion(current) {
          unmountHostComponents(current);
          detachFiber(current);
        }

        function commitWork(current, finishedWork) {
          switch (finishedWork.tag) {
            case ClassComponent:
              {
                return;
              }

            case HostComponent:
              {
                var _instance8 = finishedWork.stateNode;

                if (_instance8 != null) {
                  var newProps = finishedWork.memoizedProps;
                  var oldProps = current !== null ? current.memoizedProps : newProps;
                  var type = finishedWork.type;
                  var updatePayload = finishedWork.updateQueue;
                  finishedWork.updateQueue = null;

                  if (updatePayload !== null) {
                    commitUpdate(_instance8, updatePayload, type, oldProps, newProps, finishedWork);
                  }
                }

                return;
              }

            case HostText:
              {
                invariant(finishedWork.stateNode !== null, "This should have a text node initialized. This error is likely " + "caused by a bug in React. Please file an issue.");
                var textInstance = finishedWork.stateNode;
                var newText = finishedWork.memoizedProps;
                var oldText = current !== null ? current.memoizedProps : newText;
                commitTextUpdate(textInstance, oldText, newText);
                return;
              }

            case HostRoot:
              {
                return;
              }

            default:
              {
                invariant(false, "This unit of work tag should not have side-effects. This error is " + "likely caused by a bug in React. Please file an issue.");
              }
          }
        }

        function commitResetTextContent(current) {
          resetTextContent(current.stateNode);
        }

        if (enableMutatingReconciler) {
          return {
            commitBeforeMutationLifeCycles: commitBeforeMutationLifeCycles,
            commitResetTextContent: commitResetTextContent,
            commitPlacement: commitPlacement,
            commitDeletion: commitDeletion,
            commitWork: commitWork,
            commitLifeCycles: commitLifeCycles,
            commitErrorLogging: commitErrorLogging,
            commitAttachRef: commitAttachRef,
            commitDetachRef: commitDetachRef
          };
        } else {
          invariant(false, "Mutating reconciler is disabled.");
        }
      };

      var NO_CONTEXT = {};

      var ReactFiberHostContext = function ReactFiberHostContext(config, stack) {
        var getChildHostContext = config.getChildHostContext,
            getRootHostContext = config.getRootHostContext;
        var createCursor = stack.createCursor,
            push = stack.push,
            pop = stack.pop;
        var contextStackCursor = createCursor(NO_CONTEXT);
        var contextFiberStackCursor = createCursor(NO_CONTEXT);
        var rootInstanceStackCursor = createCursor(NO_CONTEXT);

        function requiredContext(c) {
          invariant(c !== NO_CONTEXT, "Expected host context to exist. This error is likely caused by a bug " + "in React. Please file an issue.");
          return c;
        }

        function getRootHostContainer() {
          var rootInstance = requiredContext(rootInstanceStackCursor.current);
          return rootInstance;
        }

        function pushHostContainer(fiber, nextRootInstance) {
          push(rootInstanceStackCursor, nextRootInstance, fiber);
          push(contextFiberStackCursor, fiber, fiber);
          push(contextStackCursor, NO_CONTEXT, fiber);
          var nextRootContext = getRootHostContext(nextRootInstance);
          pop(contextStackCursor, fiber);
          push(contextStackCursor, nextRootContext, fiber);
        }

        function popHostContainer(fiber) {
          pop(contextStackCursor, fiber);
          pop(contextFiberStackCursor, fiber);
          pop(rootInstanceStackCursor, fiber);
        }

        function getHostContext() {
          var context = requiredContext(contextStackCursor.current);
          return context;
        }

        function pushHostContext(fiber) {
          var rootInstance = requiredContext(rootInstanceStackCursor.current);
          var context = requiredContext(contextStackCursor.current);
          var nextContext = getChildHostContext(context, fiber.type, rootInstance);

          if (context === nextContext) {
            return;
          }

          push(contextFiberStackCursor, fiber, fiber);
          push(contextStackCursor, nextContext, fiber);
        }

        function popHostContext(fiber) {
          if (contextFiberStackCursor.current !== fiber) {
            return;
          }

          pop(contextStackCursor, fiber);
          pop(contextFiberStackCursor, fiber);
        }

        return {
          getHostContext: getHostContext,
          getRootHostContainer: getRootHostContainer,
          popHostContainer: popHostContainer,
          popHostContext: popHostContext,
          pushHostContainer: pushHostContainer,
          pushHostContext: pushHostContext
        };
      };

      var ReactFiberHydrationContext = function ReactFiberHydrationContext(config) {
        var shouldSetTextContent = config.shouldSetTextContent,
            hydration = config.hydration;

        if (!hydration) {
          return {
            enterHydrationState: function enterHydrationState() {
              return false;
            },
            resetHydrationState: function resetHydrationState() {},
            tryToClaimNextHydratableInstance: function tryToClaimNextHydratableInstance() {},
            prepareToHydrateHostInstance: function prepareToHydrateHostInstance() {
              invariant(false, "Expected prepareToHydrateHostInstance() to never be called. " + "This error is likely caused by a bug in React. Please file an issue.");
            },
            prepareToHydrateHostTextInstance: function prepareToHydrateHostTextInstance() {
              invariant(false, "Expected prepareToHydrateHostTextInstance() to never be called. " + "This error is likely caused by a bug in React. Please file an issue.");
            },
            popHydrationState: function popHydrationState(fiber) {
              return false;
            }
          };
        }

        var canHydrateInstance = hydration.canHydrateInstance,
            canHydrateTextInstance = hydration.canHydrateTextInstance,
            getNextHydratableSibling = hydration.getNextHydratableSibling,
            getFirstHydratableChild = hydration.getFirstHydratableChild,
            hydrateInstance = hydration.hydrateInstance,
            hydrateTextInstance = hydration.hydrateTextInstance,
            didNotMatchHydratedContainerTextInstance = hydration.didNotMatchHydratedContainerTextInstance,
            didNotMatchHydratedTextInstance = hydration.didNotMatchHydratedTextInstance,
            didNotHydrateContainerInstance = hydration.didNotHydrateContainerInstance,
            didNotHydrateInstance = hydration.didNotHydrateInstance,
            didNotFindHydratableContainerInstance = hydration.didNotFindHydratableContainerInstance,
            didNotFindHydratableContainerTextInstance = hydration.didNotFindHydratableContainerTextInstance,
            didNotFindHydratableInstance = hydration.didNotFindHydratableInstance,
            didNotFindHydratableTextInstance = hydration.didNotFindHydratableTextInstance;
        var hydrationParentFiber = null;
        var nextHydratableInstance = null;
        var isHydrating = false;

        function enterHydrationState(fiber) {
          var parentInstance = fiber.stateNode.containerInfo;
          nextHydratableInstance = getFirstHydratableChild(parentInstance);
          hydrationParentFiber = fiber;
          isHydrating = true;
          return true;
        }

        function deleteHydratableInstance(returnFiber, instance) {
          {
            switch (returnFiber.tag) {
              case HostRoot:
                didNotHydrateContainerInstance(returnFiber.stateNode.containerInfo, instance);
                break;

              case HostComponent:
                didNotHydrateInstance(returnFiber.type, returnFiber.memoizedProps, returnFiber.stateNode, instance);
                break;
            }
          }
          var childToDelete = createFiberFromHostInstanceForDeletion();
          childToDelete.stateNode = instance;
          childToDelete["return"] = returnFiber;
          childToDelete.effectTag = Deletion;

          if (returnFiber.lastEffect !== null) {
            returnFiber.lastEffect.nextEffect = childToDelete;
            returnFiber.lastEffect = childToDelete;
          } else {
            returnFiber.firstEffect = returnFiber.lastEffect = childToDelete;
          }
        }

        function insertNonHydratedInstance(returnFiber, fiber) {
          fiber.effectTag |= Placement;
          {
            switch (returnFiber.tag) {
              case HostRoot:
                {
                  var parentContainer = returnFiber.stateNode.containerInfo;

                  switch (fiber.tag) {
                    case HostComponent:
                      var type = fiber.type;
                      var props = fiber.pendingProps;
                      didNotFindHydratableContainerInstance(parentContainer, type, props);
                      break;

                    case HostText:
                      var text = fiber.pendingProps;
                      didNotFindHydratableContainerTextInstance(parentContainer, text);
                      break;
                  }

                  break;
                }

              case HostComponent:
                {
                  var parentType = returnFiber.type;
                  var parentProps = returnFiber.memoizedProps;
                  var parentInstance = returnFiber.stateNode;

                  switch (fiber.tag) {
                    case HostComponent:
                      var _type = fiber.type;
                      var _props = fiber.pendingProps;
                      didNotFindHydratableInstance(parentType, parentProps, parentInstance, _type, _props);
                      break;

                    case HostText:
                      var _text = fiber.pendingProps;
                      didNotFindHydratableTextInstance(parentType, parentProps, parentInstance, _text);
                      break;
                  }

                  break;
                }

              default:
                return;
            }
          }
        }

        function tryHydrate(fiber, nextInstance) {
          switch (fiber.tag) {
            case HostComponent:
              {
                var type = fiber.type;
                var props = fiber.pendingProps;
                var instance = canHydrateInstance(nextInstance, type, props);

                if (instance !== null) {
                  fiber.stateNode = instance;
                  return true;
                }

                return false;
              }

            case HostText:
              {
                var text = fiber.pendingProps;
                var textInstance = canHydrateTextInstance(nextInstance, text);

                if (textInstance !== null) {
                  fiber.stateNode = textInstance;
                  return true;
                }

                return false;
              }

            default:
              return false;
          }
        }

        function tryToClaimNextHydratableInstance(fiber) {
          if (!isHydrating) {
            return;
          }

          var nextInstance = nextHydratableInstance;

          if (!nextInstance) {
            insertNonHydratedInstance(hydrationParentFiber, fiber);
            isHydrating = false;
            hydrationParentFiber = fiber;
            return;
          }

          if (!tryHydrate(fiber, nextInstance)) {
            nextInstance = getNextHydratableSibling(nextInstance);

            if (!nextInstance || !tryHydrate(fiber, nextInstance)) {
              insertNonHydratedInstance(hydrationParentFiber, fiber);
              isHydrating = false;
              hydrationParentFiber = fiber;
              return;
            }

            deleteHydratableInstance(hydrationParentFiber, nextHydratableInstance);
          }

          hydrationParentFiber = fiber;
          nextHydratableInstance = getFirstHydratableChild(nextInstance);
        }

        function prepareToHydrateHostInstance(fiber, rootContainerInstance, hostContext) {
          var instance = fiber.stateNode;
          var updatePayload = hydrateInstance(instance, fiber.type, fiber.memoizedProps, rootContainerInstance, hostContext, fiber);
          fiber.updateQueue = updatePayload;

          if (updatePayload !== null) {
            return true;
          }

          return false;
        }

        function prepareToHydrateHostTextInstance(fiber) {
          var textInstance = fiber.stateNode;
          var textContent = fiber.memoizedProps;
          var shouldUpdate = hydrateTextInstance(textInstance, textContent, fiber);
          {
            if (shouldUpdate) {
              var returnFiber = hydrationParentFiber;

              if (returnFiber !== null) {
                switch (returnFiber.tag) {
                  case HostRoot:
                    {
                      var parentContainer = returnFiber.stateNode.containerInfo;
                      didNotMatchHydratedContainerTextInstance(parentContainer, textInstance, textContent);
                      break;
                    }

                  case HostComponent:
                    {
                      var parentType = returnFiber.type;
                      var parentProps = returnFiber.memoizedProps;
                      var parentInstance = returnFiber.stateNode;
                      didNotMatchHydratedTextInstance(parentType, parentProps, parentInstance, textInstance, textContent);
                      break;
                    }
                }
              }
            }
          }
          return shouldUpdate;
        }

        function popToNextHostParent(fiber) {
          var parent = fiber["return"];

          while (parent !== null && parent.tag !== HostComponent && parent.tag !== HostRoot) {
            parent = parent["return"];
          }

          hydrationParentFiber = parent;
        }

        function popHydrationState(fiber) {
          if (fiber !== hydrationParentFiber) {
            return false;
          }

          if (!isHydrating) {
            popToNextHostParent(fiber);
            isHydrating = true;
            return false;
          }

          var type = fiber.type;

          if (fiber.tag !== HostComponent || type !== "head" && type !== "body" && !shouldSetTextContent(type, fiber.memoizedProps)) {
            var nextInstance = nextHydratableInstance;

            while (nextInstance) {
              deleteHydratableInstance(fiber, nextInstance);
              nextInstance = getNextHydratableSibling(nextInstance);
            }
          }

          popToNextHostParent(fiber);
          nextHydratableInstance = hydrationParentFiber ? getNextHydratableSibling(fiber.stateNode) : null;
          return true;
        }

        function resetHydrationState() {
          hydrationParentFiber = null;
          nextHydratableInstance = null;
          isHydrating = false;
        }

        return {
          enterHydrationState: enterHydrationState,
          resetHydrationState: resetHydrationState,
          tryToClaimNextHydratableInstance: tryToClaimNextHydratableInstance,
          prepareToHydrateHostInstance: prepareToHydrateHostInstance,
          prepareToHydrateHostTextInstance: prepareToHydrateHostTextInstance,
          popHydrationState: popHydrationState
        };
      };

      var ReactFiberInstrumentation = {
        debugTool: null
      };
      var ReactFiberInstrumentation_1 = ReactFiberInstrumentation;
      var warnedAboutMissingGetChildContext = void 0;
      {
        warnedAboutMissingGetChildContext = {};
      }

      var ReactFiberLegacyContext = function ReactFiberLegacyContext(stack) {
        var createCursor = stack.createCursor,
            push = stack.push,
            pop = stack.pop;
        var contextStackCursor = createCursor(emptyObject);
        var didPerformWorkStackCursor = createCursor(false);
        var previousContext = emptyObject;

        function getUnmaskedContext(workInProgress) {
          var hasOwnContext = isContextProvider(workInProgress);

          if (hasOwnContext) {
            return previousContext;
          }

          return contextStackCursor.current;
        }

        function cacheContext(workInProgress, unmaskedContext, maskedContext) {
          var instance = workInProgress.stateNode;
          instance.__reactInternalMemoizedUnmaskedChildContext = unmaskedContext;
          instance.__reactInternalMemoizedMaskedChildContext = maskedContext;
        }

        function getMaskedContext(workInProgress, unmaskedContext) {
          var type = workInProgress.type;
          var contextTypes = type.contextTypes;

          if (!contextTypes) {
            return emptyObject;
          }

          var instance = workInProgress.stateNode;

          if (instance && instance.__reactInternalMemoizedUnmaskedChildContext === unmaskedContext) {
            return instance.__reactInternalMemoizedMaskedChildContext;
          }

          var context = {};

          for (var key in contextTypes) {
            context[key] = unmaskedContext[key];
          }

          {
            var name = getComponentName(workInProgress) || "Unknown";
            checkPropTypes(contextTypes, context, "context", name, ReactDebugCurrentFiber.getCurrentFiberStackAddendum);
          }

          if (instance) {
            cacheContext(workInProgress, unmaskedContext, context);
          }

          return context;
        }

        function hasContextChanged() {
          return didPerformWorkStackCursor.current;
        }

        function isContextConsumer(fiber) {
          return fiber.tag === ClassComponent && fiber.type.contextTypes != null;
        }

        function isContextProvider(fiber) {
          return fiber.tag === ClassComponent && fiber.type.childContextTypes != null;
        }

        function popContextProvider(fiber) {
          if (!isContextProvider(fiber)) {
            return;
          }

          pop(didPerformWorkStackCursor, fiber);
          pop(contextStackCursor, fiber);
        }

        function popTopLevelContextObject(fiber) {
          pop(didPerformWorkStackCursor, fiber);
          pop(contextStackCursor, fiber);
        }

        function pushTopLevelContextObject(fiber, context, didChange) {
          invariant(contextStackCursor.cursor == null, "Unexpected context found on stack. " + "This error is likely caused by a bug in React. Please file an issue.");
          push(contextStackCursor, context, fiber);
          push(didPerformWorkStackCursor, didChange, fiber);
        }

        function processChildContext(fiber, parentContext) {
          var instance = fiber.stateNode;
          var childContextTypes = fiber.type.childContextTypes;

          if (typeof instance.getChildContext !== "function") {
            {
              var componentName = getComponentName(fiber) || "Unknown";

              if (!warnedAboutMissingGetChildContext[componentName]) {
                warnedAboutMissingGetChildContext[componentName] = true;
                warning(false, "%s.childContextTypes is specified but there is no getChildContext() method " + "on the instance. You can either define getChildContext() on %s or remove " + "childContextTypes from it.", componentName, componentName);
              }
            }
            return parentContext;
          }

          var childContext = void 0;
          {
            ReactDebugCurrentFiber.setCurrentPhase("getChildContext");
          }
          startPhaseTimer(fiber, "getChildContext");
          childContext = instance.getChildContext();
          stopPhaseTimer();
          {
            ReactDebugCurrentFiber.setCurrentPhase(null);
          }

          for (var contextKey in childContext) {
            invariant(contextKey in childContextTypes, '%s.getChildContext(): key "%s" is not defined in childContextTypes.', getComponentName(fiber) || "Unknown", contextKey);
          }

          {
            var name = getComponentName(fiber) || "Unknown";
            checkPropTypes(childContextTypes, childContext, "child context", name, ReactDebugCurrentFiber.getCurrentFiberStackAddendum);
          }
          return babelHelpers.extends({}, parentContext, childContext);
        }

        function pushContextProvider(workInProgress) {
          if (!isContextProvider(workInProgress)) {
            return false;
          }

          var instance = workInProgress.stateNode;
          var memoizedMergedChildContext = instance && instance.__reactInternalMemoizedMergedChildContext || emptyObject;
          previousContext = contextStackCursor.current;
          push(contextStackCursor, memoizedMergedChildContext, workInProgress);
          push(didPerformWorkStackCursor, didPerformWorkStackCursor.current, workInProgress);
          return true;
        }

        function invalidateContextProvider(workInProgress, didChange) {
          var instance = workInProgress.stateNode;
          invariant(instance, "Expected to have an instance by this point. " + "This error is likely caused by a bug in React. Please file an issue.");

          if (didChange) {
            var mergedContext = processChildContext(workInProgress, previousContext);
            instance.__reactInternalMemoizedMergedChildContext = mergedContext;
            pop(didPerformWorkStackCursor, workInProgress);
            pop(contextStackCursor, workInProgress);
            push(contextStackCursor, mergedContext, workInProgress);
            push(didPerformWorkStackCursor, didChange, workInProgress);
          } else {
            pop(didPerformWorkStackCursor, workInProgress);
            push(didPerformWorkStackCursor, didChange, workInProgress);
          }
        }

        function findCurrentUnmaskedContext(fiber) {
          invariant(isFiberMounted(fiber) && fiber.tag === ClassComponent, "Expected subtree parent to be a mounted class component. " + "This error is likely caused by a bug in React. Please file an issue.");
          var node = fiber;

          while (node.tag !== HostRoot) {
            if (isContextProvider(node)) {
              return node.stateNode.__reactInternalMemoizedMergedChildContext;
            }

            var parent = node["return"];
            invariant(parent, "Found unexpected detached subtree parent. " + "This error is likely caused by a bug in React. Please file an issue.");
            node = parent;
          }

          return node.stateNode.context;
        }

        return {
          getUnmaskedContext: getUnmaskedContext,
          cacheContext: cacheContext,
          getMaskedContext: getMaskedContext,
          hasContextChanged: hasContextChanged,
          isContextConsumer: isContextConsumer,
          isContextProvider: isContextProvider,
          popContextProvider: popContextProvider,
          popTopLevelContextObject: popTopLevelContextObject,
          pushTopLevelContextObject: pushTopLevelContextObject,
          processChildContext: processChildContext,
          pushContextProvider: pushContextProvider,
          invalidateContextProvider: invalidateContextProvider,
          findCurrentUnmaskedContext: findCurrentUnmaskedContext
        };
      };

      var ReactFiberNewContext = function ReactFiberNewContext(stack) {
        var createCursor = stack.createCursor,
            push = stack.push,
            pop = stack.pop;
        var providerCursor = createCursor(null);
        var valueCursor = createCursor(null);
        var changedBitsCursor = createCursor(0);
        var rendererSigil = void 0;
        {
          rendererSigil = {};
        }

        function pushProvider(providerFiber) {
          var context = providerFiber.type._context;
          push(changedBitsCursor, context._changedBits, providerFiber);
          push(valueCursor, context._currentValue, providerFiber);
          push(providerCursor, providerFiber, providerFiber);
          context._currentValue = providerFiber.pendingProps.value;
          context._changedBits = providerFiber.stateNode;
          {
            !(context._currentRenderer === null || context._currentRenderer === rendererSigil) ? warning(false, "Detected multiple renderers concurrently rendering the " + "same context provider. This is currently unsupported.") : void 0;
            context._currentRenderer = rendererSigil;
          }
        }

        function popProvider(providerFiber) {
          var changedBits = changedBitsCursor.current;
          var currentValue = valueCursor.current;
          pop(providerCursor, providerFiber);
          pop(valueCursor, providerFiber);
          pop(changedBitsCursor, providerFiber);
          var context = providerFiber.type._context;
          context._currentValue = currentValue;
          context._changedBits = changedBits;
        }

        return {
          pushProvider: pushProvider,
          popProvider: popProvider
        };
      };

      var ReactFiberStack = function ReactFiberStack() {
        var valueStack = [];
        var fiberStack = void 0;
        {
          fiberStack = [];
        }
        var index = -1;

        function createCursor(defaultValue) {
          return {
            current: defaultValue
          };
        }

        function isEmpty() {
          return index === -1;
        }

        function pop(cursor, fiber) {
          if (index < 0) {
            {
              warning(false, "Unexpected pop.");
            }
            return;
          }

          {
            if (fiber !== fiberStack[index]) {
              warning(false, "Unexpected Fiber popped.");
            }
          }
          cursor.current = valueStack[index];
          valueStack[index] = null;
          {
            fiberStack[index] = null;
          }
          index--;
        }

        function push(cursor, value, fiber) {
          index++;
          valueStack[index] = cursor.current;
          {
            fiberStack[index] = fiber;
          }
          cursor.current = value;
        }

        function checkThatStackIsEmpty() {
          {
            if (index !== -1) {
              warning(false, "Expected an empty stack. Something was not reset properly.");
            }
          }
        }

        function resetStackAfterFatalErrorInDev() {
          {
            index = -1;
            valueStack.length = 0;
            fiberStack.length = 0;
          }
        }

        return {
          createCursor: createCursor,
          isEmpty: isEmpty,
          pop: pop,
          push: push,
          checkThatStackIsEmpty: checkThatStackIsEmpty,
          resetStackAfterFatalErrorInDev: resetStackAfterFatalErrorInDev
        };
      };

      var invokeGuardedCallback$2 = ReactErrorUtils.invokeGuardedCallback;
      var hasCaughtError = ReactErrorUtils.hasCaughtError;
      var clearCaughtError = ReactErrorUtils.clearCaughtError;
      var didWarnAboutStateTransition = void 0;
      var didWarnSetStateChildContext = void 0;
      var warnAboutUpdateOnUnmounted = void 0;
      var warnAboutInvalidUpdates = void 0;
      {
        didWarnAboutStateTransition = false;
        didWarnSetStateChildContext = false;
        var didWarnStateUpdateForUnmountedComponent = {};

        warnAboutUpdateOnUnmounted = function warnAboutUpdateOnUnmounted(fiber) {
          var componentName = getComponentName(fiber) || "ReactClass";

          if (didWarnStateUpdateForUnmountedComponent[componentName]) {
            return;
          }

          warning(false, "Can't call setState (or forceUpdate) on an unmounted component. This " + "is a no-op, but it indicates a memory leak in your application. To " + "fix, cancel all subscriptions and asynchronous tasks in the " + "componentWillUnmount method.%s", getStackAddendumByWorkInProgressFiber(fiber));
          didWarnStateUpdateForUnmountedComponent[componentName] = true;
        };

        warnAboutInvalidUpdates = function warnAboutInvalidUpdates(instance) {
          switch (ReactDebugCurrentFiber.phase) {
            case "getChildContext":
              if (didWarnSetStateChildContext) {
                return;
              }

              warning(false, "setState(...): Cannot call setState() inside getChildContext()");
              didWarnSetStateChildContext = true;
              break;

            case "render":
              if (didWarnAboutStateTransition) {
                return;
              }

              warning(false, "Cannot update during an existing state transition (such as within " + "`render` or another component's constructor). Render methods should " + "be a pure function of props and state; constructor side-effects are " + "an anti-pattern, but can be moved to `componentWillMount`.");
              didWarnAboutStateTransition = true;
              break;
          }
        };
      }

      var ReactFiberScheduler = function ReactFiberScheduler(config) {
        var stack = ReactFiberStack();
        var hostContext = ReactFiberHostContext(config, stack);
        var legacyContext = ReactFiberLegacyContext(stack);
        var newContext = ReactFiberNewContext(stack);
        var popHostContext = hostContext.popHostContext,
            popHostContainer = hostContext.popHostContainer;
        var popTopLevelLegacyContextObject = legacyContext.popTopLevelContextObject,
            popLegacyContextProvider = legacyContext.popContextProvider;
        var popProvider = newContext.popProvider;
        var hydrationContext = ReactFiberHydrationContext(config);

        var _ReactFiberBeginWork = ReactFiberBeginWork(config, hostContext, legacyContext, newContext, hydrationContext, scheduleWork, computeExpirationForFiber),
            beginWork = _ReactFiberBeginWork.beginWork;

        var _ReactFiberCompleteWo = ReactFiberCompleteWork(config, hostContext, legacyContext, newContext, hydrationContext),
            completeWork = _ReactFiberCompleteWo.completeWork;

        var _ReactFiberUnwindWork = ReactFiberUnwindWork(hostContext, legacyContext, newContext, scheduleWork, isAlreadyFailedLegacyErrorBoundary),
            throwException = _ReactFiberUnwindWork.throwException,
            unwindWork = _ReactFiberUnwindWork.unwindWork,
            unwindInterruptedWork = _ReactFiberUnwindWork.unwindInterruptedWork;

        var _ReactFiberCommitWork = ReactFiberCommitWork(config, onCommitPhaseError, scheduleWork, computeExpirationForFiber, markLegacyErrorBoundaryAsFailed, recalculateCurrentTime),
            commitBeforeMutationLifeCycles = _ReactFiberCommitWork.commitBeforeMutationLifeCycles,
            commitResetTextContent = _ReactFiberCommitWork.commitResetTextContent,
            commitPlacement = _ReactFiberCommitWork.commitPlacement,
            commitDeletion = _ReactFiberCommitWork.commitDeletion,
            commitWork = _ReactFiberCommitWork.commitWork,
            commitLifeCycles = _ReactFiberCommitWork.commitLifeCycles,
            commitErrorLogging = _ReactFiberCommitWork.commitErrorLogging,
            commitAttachRef = _ReactFiberCommitWork.commitAttachRef,
            commitDetachRef = _ReactFiberCommitWork.commitDetachRef;

        var now = config.now,
            scheduleDeferredCallback = config.scheduleDeferredCallback,
            cancelDeferredCallback = config.cancelDeferredCallback,
            prepareForCommit = config.prepareForCommit,
            resetAfterCommit = config.resetAfterCommit;
        var originalStartTimeMs = now();
        var mostRecentCurrentTime = msToExpirationTime(0);
        var mostRecentCurrentTimeMs = originalStartTimeMs;
        var lastUniqueAsyncExpiration = 0;
        var expirationContext = NoWork;
        var isWorking = false;
        var nextUnitOfWork = null;
        var nextRoot = null;
        var nextRenderExpirationTime = NoWork;
        var nextEffect = null;
        var isCommitting = false;
        var isRootReadyForCommit = false;
        var legacyErrorBoundariesThatAlreadyFailed = null;
        var interruptedBy = null;
        var stashedWorkInProgressProperties = void 0;
        var replayUnitOfWork = void 0;
        var isReplayingFailedUnitOfWork = void 0;
        var originalReplayError = void 0;
        var rethrowOriginalError = void 0;

        if (true && replayFailedUnitOfWorkWithInvokeGuardedCallback) {
          stashedWorkInProgressProperties = null;
          isReplayingFailedUnitOfWork = false;
          originalReplayError = null;

          replayUnitOfWork = function replayUnitOfWork(failedUnitOfWork, error, isAsync) {
            assignFiberPropertiesInDEV(failedUnitOfWork, stashedWorkInProgressProperties);

            switch (failedUnitOfWork.tag) {
              case HostRoot:
                popHostContainer(failedUnitOfWork);
                popTopLevelLegacyContextObject(failedUnitOfWork);
                break;

              case HostComponent:
                popHostContext(failedUnitOfWork);
                break;

              case ClassComponent:
                popLegacyContextProvider(failedUnitOfWork);
                break;

              case HostPortal:
                popHostContainer(failedUnitOfWork);
                break;

              case ContextProvider:
                popProvider(failedUnitOfWork);
                break;
            }

            isReplayingFailedUnitOfWork = true;
            originalReplayError = error;
            invokeGuardedCallback$2(null, workLoop, null, isAsync);
            isReplayingFailedUnitOfWork = false;
            originalReplayError = null;

            if (hasCaughtError()) {
              clearCaughtError();
            } else {
              nextUnitOfWork = failedUnitOfWork;
            }
          };

          rethrowOriginalError = function rethrowOriginalError() {
            throw originalReplayError;
          };
        }

        function resetStack() {
          if (nextUnitOfWork !== null) {
            var interruptedWork = nextUnitOfWork["return"];

            while (interruptedWork !== null) {
              unwindInterruptedWork(interruptedWork);
              interruptedWork = interruptedWork["return"];
            }
          }

          {
            ReactStrictModeWarnings.discardPendingWarnings();
            stack.checkThatStackIsEmpty();
          }
          nextRoot = null;
          nextRenderExpirationTime = NoWork;
          nextUnitOfWork = null;
          isRootReadyForCommit = false;
        }

        function commitAllHostEffects() {
          while (nextEffect !== null) {
            {
              ReactDebugCurrentFiber.setCurrentFiber(nextEffect);
            }
            recordEffect();
            var effectTag = nextEffect.effectTag;

            if (effectTag & ContentReset) {
              commitResetTextContent(nextEffect);
            }

            if (effectTag & Ref) {
              var current = nextEffect.alternate;

              if (current !== null) {
                commitDetachRef(current);
              }
            }

            var primaryEffectTag = effectTag & (Placement | Update | Deletion);

            switch (primaryEffectTag) {
              case Placement:
                {
                  commitPlacement(nextEffect);
                  nextEffect.effectTag &= ~Placement;
                  break;
                }

              case PlacementAndUpdate:
                {
                  commitPlacement(nextEffect);
                  nextEffect.effectTag &= ~Placement;
                  var _current = nextEffect.alternate;
                  commitWork(_current, nextEffect);
                  break;
                }

              case Update:
                {
                  var _current2 = nextEffect.alternate;
                  commitWork(_current2, nextEffect);
                  break;
                }

              case Deletion:
                {
                  commitDeletion(nextEffect);
                  break;
                }
            }

            nextEffect = nextEffect.nextEffect;
          }

          {
            ReactDebugCurrentFiber.resetCurrentFiber();
          }
        }

        function commitBeforeMutationLifecycles() {
          while (nextEffect !== null) {
            var effectTag = nextEffect.effectTag;

            if (effectTag & Snapshot) {
              recordEffect();
              var current = nextEffect.alternate;
              commitBeforeMutationLifeCycles(current, nextEffect);
            }

            nextEffect = nextEffect.nextEffect;
          }
        }

        function commitAllLifeCycles(finishedRoot, currentTime, committedExpirationTime) {
          {
            ReactStrictModeWarnings.flushPendingUnsafeLifecycleWarnings();

            if (warnAboutDeprecatedLifecycles) {
              ReactStrictModeWarnings.flushPendingDeprecationWarnings();
            }
          }

          while (nextEffect !== null) {
            var effectTag = nextEffect.effectTag;

            if (effectTag & (Update | Callback)) {
              recordEffect();
              var current = nextEffect.alternate;
              commitLifeCycles(finishedRoot, current, nextEffect, currentTime, committedExpirationTime);
            }

            if (effectTag & ErrLog) {
              commitErrorLogging(nextEffect, onUncaughtError);
            }

            if (effectTag & Ref) {
              recordEffect();
              commitAttachRef(nextEffect);
            }

            var next = nextEffect.nextEffect;
            nextEffect.nextEffect = null;
            nextEffect = next;
          }
        }

        function isAlreadyFailedLegacyErrorBoundary(instance) {
          return legacyErrorBoundariesThatAlreadyFailed !== null && legacyErrorBoundariesThatAlreadyFailed.has(instance);
        }

        function markLegacyErrorBoundaryAsFailed(instance) {
          if (legacyErrorBoundariesThatAlreadyFailed === null) {
            legacyErrorBoundariesThatAlreadyFailed = new Set([instance]);
          } else {
            legacyErrorBoundariesThatAlreadyFailed.add(instance);
          }
        }

        function commitRoot(finishedWork) {
          isWorking = true;
          isCommitting = true;
          startCommitTimer();
          var root = finishedWork.stateNode;
          invariant(root.current !== finishedWork, "Cannot commit the same tree as before. This is probably a bug " + "related to the return field. This error is likely caused by a bug " + "in React. Please file an issue.");
          var committedExpirationTime = root.pendingCommitExpirationTime;
          invariant(committedExpirationTime !== NoWork, "Cannot commit an incomplete root. This error is likely caused by a " + "bug in React. Please file an issue.");
          root.pendingCommitExpirationTime = NoWork;
          var currentTime = recalculateCurrentTime();
          ReactCurrentOwner.current = null;
          var firstEffect = void 0;

          if (finishedWork.effectTag > PerformedWork) {
            if (finishedWork.lastEffect !== null) {
              finishedWork.lastEffect.nextEffect = finishedWork;
              firstEffect = finishedWork.firstEffect;
            } else {
              firstEffect = finishedWork;
            }
          } else {
            firstEffect = finishedWork.firstEffect;
          }

          prepareForCommit(root.containerInfo);
          nextEffect = firstEffect;
          startCommitSnapshotEffectsTimer();

          while (nextEffect !== null) {
            var didError = false;
            var error = void 0;
            {
              invokeGuardedCallback$2(null, commitBeforeMutationLifecycles, null);

              if (hasCaughtError()) {
                didError = true;
                error = clearCaughtError();
              }
            }

            if (didError) {
              invariant(nextEffect !== null, "Should have next effect. This error is likely caused by a bug " + "in React. Please file an issue.");
              onCommitPhaseError(nextEffect, error);

              if (nextEffect !== null) {
                nextEffect = nextEffect.nextEffect;
              }
            }
          }

          stopCommitSnapshotEffectsTimer();
          nextEffect = firstEffect;
          startCommitHostEffectsTimer();

          while (nextEffect !== null) {
            var _didError = false;

            var _error = void 0;

            {
              invokeGuardedCallback$2(null, commitAllHostEffects, null);

              if (hasCaughtError()) {
                _didError = true;
                _error = clearCaughtError();
              }
            }

            if (_didError) {
              invariant(nextEffect !== null, "Should have next effect. This error is likely caused by a bug " + "in React. Please file an issue.");
              onCommitPhaseError(nextEffect, _error);

              if (nextEffect !== null) {
                nextEffect = nextEffect.nextEffect;
              }
            }
          }

          stopCommitHostEffectsTimer();
          resetAfterCommit(root.containerInfo);
          root.current = finishedWork;
          nextEffect = firstEffect;
          startCommitLifeCyclesTimer();

          while (nextEffect !== null) {
            var _didError2 = false;

            var _error2 = void 0;

            {
              invokeGuardedCallback$2(null, commitAllLifeCycles, null, root, currentTime, committedExpirationTime);

              if (hasCaughtError()) {
                _didError2 = true;
                _error2 = clearCaughtError();
              }
            }

            if (_didError2) {
              invariant(nextEffect !== null, "Should have next effect. This error is likely caused by a bug " + "in React. Please file an issue.");
              onCommitPhaseError(nextEffect, _error2);

              if (nextEffect !== null) {
                nextEffect = nextEffect.nextEffect;
              }
            }
          }

          isCommitting = false;
          isWorking = false;
          stopCommitLifeCyclesTimer();
          stopCommitTimer();

          if (typeof onCommitRoot === "function") {
            onCommitRoot(finishedWork.stateNode);
          }

          if (true && ReactFiberInstrumentation_1.debugTool) {
            ReactFiberInstrumentation_1.debugTool.onCommitWork(finishedWork);
          }

          var remainingTime = root.current.expirationTime;

          if (remainingTime === NoWork) {
            legacyErrorBoundariesThatAlreadyFailed = null;
          }

          return remainingTime;
        }

        function resetExpirationTime(workInProgress, renderTime) {
          if (renderTime !== Never && workInProgress.expirationTime === Never) {
            return;
          }

          var newExpirationTime = getUpdateExpirationTime(workInProgress);
          var child = workInProgress.child;

          while (child !== null) {
            if (child.expirationTime !== NoWork && (newExpirationTime === NoWork || newExpirationTime > child.expirationTime)) {
              newExpirationTime = child.expirationTime;
            }

            child = child.sibling;
          }

          workInProgress.expirationTime = newExpirationTime;
        }

        function completeUnitOfWork(workInProgress) {
          while (true) {
            var current = workInProgress.alternate;
            {
              ReactDebugCurrentFiber.setCurrentFiber(workInProgress);
            }
            var returnFiber = workInProgress["return"];
            var siblingFiber = workInProgress.sibling;

            if ((workInProgress.effectTag & Incomplete) === NoEffect) {
              var next = completeWork(current, workInProgress, nextRenderExpirationTime);
              stopWorkTimer(workInProgress);
              resetExpirationTime(workInProgress, nextRenderExpirationTime);
              {
                ReactDebugCurrentFiber.resetCurrentFiber();
              }

              if (next !== null) {
                stopWorkTimer(workInProgress);

                if (true && ReactFiberInstrumentation_1.debugTool) {
                  ReactFiberInstrumentation_1.debugTool.onCompleteWork(workInProgress);
                }

                return next;
              }

              if (returnFiber !== null && (returnFiber.effectTag & Incomplete) === NoEffect) {
                if (returnFiber.firstEffect === null) {
                  returnFiber.firstEffect = workInProgress.firstEffect;
                }

                if (workInProgress.lastEffect !== null) {
                  if (returnFiber.lastEffect !== null) {
                    returnFiber.lastEffect.nextEffect = workInProgress.firstEffect;
                  }

                  returnFiber.lastEffect = workInProgress.lastEffect;
                }

                var effectTag = workInProgress.effectTag;

                if (effectTag > PerformedWork) {
                  if (returnFiber.lastEffect !== null) {
                    returnFiber.lastEffect.nextEffect = workInProgress;
                  } else {
                    returnFiber.firstEffect = workInProgress;
                  }

                  returnFiber.lastEffect = workInProgress;
                }
              }

              if (true && ReactFiberInstrumentation_1.debugTool) {
                ReactFiberInstrumentation_1.debugTool.onCompleteWork(workInProgress);
              }

              if (siblingFiber !== null) {
                return siblingFiber;
              } else if (returnFiber !== null) {
                workInProgress = returnFiber;
                continue;
              } else {
                isRootReadyForCommit = true;
                return null;
              }
            } else {
              var _next = unwindWork(workInProgress);

              if (workInProgress.effectTag & DidCapture) {
                stopFailedWorkTimer(workInProgress);
              } else {
                stopWorkTimer(workInProgress);
              }

              {
                ReactDebugCurrentFiber.resetCurrentFiber();
              }

              if (_next !== null) {
                stopWorkTimer(workInProgress);

                if (true && ReactFiberInstrumentation_1.debugTool) {
                  ReactFiberInstrumentation_1.debugTool.onCompleteWork(workInProgress);
                }

                _next.effectTag &= HostEffectMask;
                return _next;
              }

              if (returnFiber !== null) {
                returnFiber.firstEffect = returnFiber.lastEffect = null;
                returnFiber.effectTag |= Incomplete;
              }

              if (true && ReactFiberInstrumentation_1.debugTool) {
                ReactFiberInstrumentation_1.debugTool.onCompleteWork(workInProgress);
              }

              if (siblingFiber !== null) {
                return siblingFiber;
              } else if (returnFiber !== null) {
                workInProgress = returnFiber;
                continue;
              } else {
                return null;
              }
            }
          }

          return null;
        }

        function performUnitOfWork(workInProgress) {
          var current = workInProgress.alternate;
          startWorkTimer(workInProgress);
          {
            ReactDebugCurrentFiber.setCurrentFiber(workInProgress);
          }

          if (true && replayFailedUnitOfWorkWithInvokeGuardedCallback) {
            stashedWorkInProgressProperties = assignFiberPropertiesInDEV(stashedWorkInProgressProperties, workInProgress);
          }

          var next = beginWork(current, workInProgress, nextRenderExpirationTime);
          {
            ReactDebugCurrentFiber.resetCurrentFiber();

            if (isReplayingFailedUnitOfWork) {
              rethrowOriginalError();
            }
          }

          if (true && ReactFiberInstrumentation_1.debugTool) {
            ReactFiberInstrumentation_1.debugTool.onBeginWork(workInProgress);
          }

          if (next === null) {
            next = completeUnitOfWork(workInProgress);
          }

          ReactCurrentOwner.current = null;
          return next;
        }

        function workLoop(isAsync) {
          if (!isAsync) {
            while (nextUnitOfWork !== null) {
              nextUnitOfWork = performUnitOfWork(nextUnitOfWork);
            }
          } else {
            while (nextUnitOfWork !== null && !shouldYield()) {
              nextUnitOfWork = performUnitOfWork(nextUnitOfWork);
            }
          }
        }

        function renderRoot(root, expirationTime, isAsync) {
          invariant(!isWorking, "renderRoot was called recursively. This error is likely caused " + "by a bug in React. Please file an issue.");
          isWorking = true;

          if (expirationTime !== nextRenderExpirationTime || root !== nextRoot || nextUnitOfWork === null) {
            resetStack();
            nextRoot = root;
            nextRenderExpirationTime = expirationTime;
            nextUnitOfWork = createWorkInProgress(nextRoot.current, null, nextRenderExpirationTime);
            root.pendingCommitExpirationTime = NoWork;
          }

          var didFatal = false;
          startWorkLoopTimer(nextUnitOfWork);

          do {
            try {
              workLoop(isAsync);
            } catch (thrownValue) {
              if (nextUnitOfWork === null) {
                didFatal = true;
                onUncaughtError(thrownValue);
                break;
              }

              if (true && replayFailedUnitOfWorkWithInvokeGuardedCallback) {
                var failedUnitOfWork = nextUnitOfWork;
                replayUnitOfWork(failedUnitOfWork, thrownValue, isAsync);
              }

              var sourceFiber = nextUnitOfWork;
              var returnFiber = sourceFiber["return"];

              if (returnFiber === null) {
                didFatal = true;
                onUncaughtError(thrownValue);
                break;
              }

              throwException(returnFiber, sourceFiber, thrownValue);
              nextUnitOfWork = completeUnitOfWork(sourceFiber);
            }

            break;
          } while (true);

          var didCompleteRoot = false;
          isWorking = false;

          if (didFatal) {
            stopWorkLoopTimer(interruptedBy, didCompleteRoot);
            interruptedBy = null;
            {
              stack.resetStackAfterFatalErrorInDev();
            }
            return null;
          } else if (nextUnitOfWork === null) {
            if (isRootReadyForCommit) {
              didCompleteRoot = true;
              stopWorkLoopTimer(interruptedBy, didCompleteRoot);
              interruptedBy = null;
              root.pendingCommitExpirationTime = expirationTime;
              var finishedWork = root.current.alternate;
              return finishedWork;
            } else {
              stopWorkLoopTimer(interruptedBy, didCompleteRoot);
              interruptedBy = null;
              invariant(false, "Expired work should have completed. This error is likely caused " + "by a bug in React. Please file an issue.");
            }
          } else {
            stopWorkLoopTimer(interruptedBy, didCompleteRoot);
            interruptedBy = null;
            return null;
          }
        }

        function scheduleCapture(sourceFiber, boundaryFiber, value, expirationTime) {
          var capturedValue = createCapturedValue(value, sourceFiber);
          var update = {
            expirationTime: expirationTime,
            partialState: null,
            callback: null,
            isReplace: false,
            isForced: false,
            capturedValue: capturedValue,
            next: null
          };
          insertUpdateIntoFiber(boundaryFiber, update);
          scheduleWork(boundaryFiber, expirationTime);
        }

        function dispatch(sourceFiber, value, expirationTime) {
          invariant(!isWorking || isCommitting, "dispatch: Cannot dispatch during the render phase.");
          var fiber = sourceFiber["return"];

          while (fiber !== null) {
            switch (fiber.tag) {
              case ClassComponent:
                var ctor = fiber.type;
                var instance = fiber.stateNode;

                if (typeof ctor.getDerivedStateFromCatch === "function" || typeof instance.componentDidCatch === "function" && !isAlreadyFailedLegacyErrorBoundary(instance)) {
                  scheduleCapture(sourceFiber, fiber, value, expirationTime);
                  return;
                }

                break;

              case HostRoot:
                scheduleCapture(sourceFiber, fiber, value, expirationTime);
                return;
            }

            fiber = fiber["return"];
          }

          if (sourceFiber.tag === HostRoot) {
            scheduleCapture(sourceFiber, sourceFiber, value, expirationTime);
          }
        }

        function onCommitPhaseError(fiber, error) {
          return dispatch(fiber, error, Sync);
        }

        function computeAsyncExpiration(currentTime) {
          var expirationMs = 5000;
          var bucketSizeMs = 250;
          return computeExpirationBucket(currentTime, expirationMs, bucketSizeMs);
        }

        function computeInteractiveExpiration(currentTime) {
          var expirationMs = 500;
          var bucketSizeMs = 100;
          return computeExpirationBucket(currentTime, expirationMs, bucketSizeMs);
        }

        function computeUniqueAsyncExpiration() {
          var currentTime = recalculateCurrentTime();
          var result = computeAsyncExpiration(currentTime);

          if (result <= lastUniqueAsyncExpiration) {
            result = lastUniqueAsyncExpiration + 1;
          }

          lastUniqueAsyncExpiration = result;
          return lastUniqueAsyncExpiration;
        }

        function computeExpirationForFiber(fiber) {
          var expirationTime = void 0;

          if (expirationContext !== NoWork) {
            expirationTime = expirationContext;
          } else if (isWorking) {
            if (isCommitting) {
              expirationTime = Sync;
            } else {
              expirationTime = nextRenderExpirationTime;
            }
          } else {
            if (fiber.mode & AsyncMode) {
              if (isBatchingInteractiveUpdates) {
                var currentTime = recalculateCurrentTime();
                expirationTime = computeInteractiveExpiration(currentTime);
              } else {
                var _currentTime = recalculateCurrentTime();

                expirationTime = computeAsyncExpiration(_currentTime);
              }
            } else {
              expirationTime = Sync;
            }
          }

          if (isBatchingInteractiveUpdates) {
            if (lowestPendingInteractiveExpirationTime === NoWork || expirationTime > lowestPendingInteractiveExpirationTime) {
              lowestPendingInteractiveExpirationTime = expirationTime;
            }
          }

          return expirationTime;
        }

        function scheduleWork(fiber, expirationTime) {
          return scheduleWorkImpl(fiber, expirationTime, false);
        }

        function scheduleWorkImpl(fiber, expirationTime, isErrorRecovery) {
          recordScheduleUpdate();
          {
            if (!isErrorRecovery && fiber.tag === ClassComponent) {
              var instance = fiber.stateNode;
              warnAboutInvalidUpdates(instance);
            }
          }
          var node = fiber;

          while (node !== null) {
            if (node.expirationTime === NoWork || node.expirationTime > expirationTime) {
              node.expirationTime = expirationTime;
            }

            if (node.alternate !== null) {
              if (node.alternate.expirationTime === NoWork || node.alternate.expirationTime > expirationTime) {
                node.alternate.expirationTime = expirationTime;
              }
            }

            if (node["return"] === null) {
              if (node.tag === HostRoot) {
                var root = node.stateNode;

                if (!isWorking && nextRenderExpirationTime !== NoWork && expirationTime < nextRenderExpirationTime) {
                  interruptedBy = fiber;
                  resetStack();
                }

                if (!isWorking || isCommitting || nextRoot !== root) {
                  requestWork(root, expirationTime);
                }

                if (nestedUpdateCount > NESTED_UPDATE_LIMIT) {
                  invariant(false, "Maximum update depth exceeded. This can happen when a " + "component repeatedly calls setState inside " + "componentWillUpdate or componentDidUpdate. React limits " + "the number of nested updates to prevent infinite loops.");
                }
              } else {
                {
                  if (!isErrorRecovery && fiber.tag === ClassComponent) {
                    warnAboutUpdateOnUnmounted(fiber);
                  }
                }
                return;
              }
            }

            node = node["return"];
          }
        }

        function recalculateCurrentTime() {
          mostRecentCurrentTimeMs = now() - originalStartTimeMs;
          mostRecentCurrentTime = msToExpirationTime(mostRecentCurrentTimeMs);
          return mostRecentCurrentTime;
        }

        function deferredUpdates(fn) {
          var previousExpirationContext = expirationContext;
          var currentTime = recalculateCurrentTime();
          expirationContext = computeAsyncExpiration(currentTime);

          try {
            return fn();
          } finally {
            expirationContext = previousExpirationContext;
          }
        }

        function syncUpdates(fn, a, b, c, d) {
          var previousExpirationContext = expirationContext;
          expirationContext = Sync;

          try {
            return fn(a, b, c, d);
          } finally {
            expirationContext = previousExpirationContext;
          }
        }

        var firstScheduledRoot = null;
        var lastScheduledRoot = null;
        var callbackExpirationTime = NoWork;
        var callbackID = -1;
        var isRendering = false;
        var nextFlushedRoot = null;
        var nextFlushedExpirationTime = NoWork;
        var lowestPendingInteractiveExpirationTime = NoWork;
        var deadlineDidExpire = false;
        var hasUnhandledError = false;
        var unhandledError = null;
        var deadline = null;
        var isBatchingUpdates = false;
        var isUnbatchingUpdates = false;
        var isBatchingInteractiveUpdates = false;
        var completedBatches = null;
        var NESTED_UPDATE_LIMIT = 1000;
        var nestedUpdateCount = 0;
        var timeHeuristicForUnitOfWork = 1;

        function scheduleCallbackWithExpiration(expirationTime) {
          if (callbackExpirationTime !== NoWork) {
            if (expirationTime > callbackExpirationTime) {
              return;
            } else {
              cancelDeferredCallback(callbackID);
            }
          } else {
            startRequestCallbackTimer();
          }

          var currentMs = now() - originalStartTimeMs;
          var expirationMs = expirationTimeToMs(expirationTime);
          var timeout = expirationMs - currentMs;
          callbackExpirationTime = expirationTime;
          callbackID = scheduleDeferredCallback(performAsyncWork, {
            timeout: timeout
          });
        }

        function requestWork(root, expirationTime) {
          addRootToSchedule(root, expirationTime);

          if (isRendering) {
            return;
          }

          if (isBatchingUpdates) {
            if (isUnbatchingUpdates) {
              nextFlushedRoot = root;
              nextFlushedExpirationTime = Sync;
              performWorkOnRoot(root, Sync, false);
            }

            return;
          }

          if (expirationTime === Sync) {
            performSyncWork();
          } else {
            scheduleCallbackWithExpiration(expirationTime);
          }
        }

        function addRootToSchedule(root, expirationTime) {
          if (root.nextScheduledRoot === null) {
            root.remainingExpirationTime = expirationTime;

            if (lastScheduledRoot === null) {
              firstScheduledRoot = lastScheduledRoot = root;
              root.nextScheduledRoot = root;
            } else {
              lastScheduledRoot.nextScheduledRoot = root;
              lastScheduledRoot = root;
              lastScheduledRoot.nextScheduledRoot = firstScheduledRoot;
            }
          } else {
            var remainingExpirationTime = root.remainingExpirationTime;

            if (remainingExpirationTime === NoWork || expirationTime < remainingExpirationTime) {
              root.remainingExpirationTime = expirationTime;
            }
          }
        }

        function findHighestPriorityRoot() {
          var highestPriorityWork = NoWork;
          var highestPriorityRoot = null;

          if (lastScheduledRoot !== null) {
            var previousScheduledRoot = lastScheduledRoot;
            var root = firstScheduledRoot;

            while (root !== null) {
              var remainingExpirationTime = root.remainingExpirationTime;

              if (remainingExpirationTime === NoWork) {
                invariant(previousScheduledRoot !== null && lastScheduledRoot !== null, "Should have a previous and last root. This error is likely " + "caused by a bug in React. Please file an issue.");

                if (root === root.nextScheduledRoot) {
                  root.nextScheduledRoot = null;
                  firstScheduledRoot = lastScheduledRoot = null;
                  break;
                } else if (root === firstScheduledRoot) {
                  var next = root.nextScheduledRoot;
                  firstScheduledRoot = next;
                  lastScheduledRoot.nextScheduledRoot = next;
                  root.nextScheduledRoot = null;
                } else if (root === lastScheduledRoot) {
                  lastScheduledRoot = previousScheduledRoot;
                  lastScheduledRoot.nextScheduledRoot = firstScheduledRoot;
                  root.nextScheduledRoot = null;
                  break;
                } else {
                  previousScheduledRoot.nextScheduledRoot = root.nextScheduledRoot;
                  root.nextScheduledRoot = null;
                }

                root = previousScheduledRoot.nextScheduledRoot;
              } else {
                if (highestPriorityWork === NoWork || remainingExpirationTime < highestPriorityWork) {
                  highestPriorityWork = remainingExpirationTime;
                  highestPriorityRoot = root;
                }

                if (root === lastScheduledRoot) {
                  break;
                }

                previousScheduledRoot = root;
                root = root.nextScheduledRoot;
              }
            }
          }

          var previousFlushedRoot = nextFlushedRoot;

          if (previousFlushedRoot !== null && previousFlushedRoot === highestPriorityRoot && highestPriorityWork === Sync) {
            nestedUpdateCount++;
          } else {
            nestedUpdateCount = 0;
          }

          nextFlushedRoot = highestPriorityRoot;
          nextFlushedExpirationTime = highestPriorityWork;
        }

        function performAsyncWork(dl) {
          performWork(NoWork, true, dl);
        }

        function performSyncWork() {
          performWork(Sync, false, null);
        }

        function performWork(minExpirationTime, isAsync, dl) {
          deadline = dl;
          findHighestPriorityRoot();

          if (enableUserTimingAPI && deadline !== null) {
            var didExpire = nextFlushedExpirationTime < recalculateCurrentTime();
            var timeout = expirationTimeToMs(nextFlushedExpirationTime);
            stopRequestCallbackTimer(didExpire, timeout);
          }

          if (isAsync) {
            while (nextFlushedRoot !== null && nextFlushedExpirationTime !== NoWork && (minExpirationTime === NoWork || minExpirationTime >= nextFlushedExpirationTime) && (!deadlineDidExpire || recalculateCurrentTime() >= nextFlushedExpirationTime)) {
              performWorkOnRoot(nextFlushedRoot, nextFlushedExpirationTime, !deadlineDidExpire);
              findHighestPriorityRoot();
            }
          } else {
            while (nextFlushedRoot !== null && nextFlushedExpirationTime !== NoWork && (minExpirationTime === NoWork || minExpirationTime >= nextFlushedExpirationTime)) {
              performWorkOnRoot(nextFlushedRoot, nextFlushedExpirationTime, false);
              findHighestPriorityRoot();
            }
          }

          if (deadline !== null) {
            callbackExpirationTime = NoWork;
            callbackID = -1;
          }

          if (nextFlushedExpirationTime !== NoWork) {
            scheduleCallbackWithExpiration(nextFlushedExpirationTime);
          }

          deadline = null;
          deadlineDidExpire = false;
          finishRendering();
        }

        function flushRoot(root, expirationTime) {
          invariant(!isRendering, "work.commit(): Cannot commit while already rendering. This likely " + "means you attempted to commit from inside a lifecycle method.");
          nextFlushedRoot = root;
          nextFlushedExpirationTime = expirationTime;
          performWorkOnRoot(root, expirationTime, false);
          performSyncWork();
          finishRendering();
        }

        function finishRendering() {
          nestedUpdateCount = 0;

          if (completedBatches !== null) {
            var batches = completedBatches;
            completedBatches = null;

            for (var i = 0; i < batches.length; i++) {
              var batch = batches[i];

              try {
                batch._onComplete();
              } catch (error) {
                if (!hasUnhandledError) {
                  hasUnhandledError = true;
                  unhandledError = error;
                }
              }
            }
          }

          if (hasUnhandledError) {
            var error = unhandledError;
            unhandledError = null;
            hasUnhandledError = false;
            throw error;
          }
        }

        function performWorkOnRoot(root, expirationTime, isAsync) {
          invariant(!isRendering, "performWorkOnRoot was called recursively. This error is likely caused " + "by a bug in React. Please file an issue.");
          isRendering = true;

          if (!isAsync) {
            var finishedWork = root.finishedWork;

            if (finishedWork !== null) {
              completeRoot(root, finishedWork, expirationTime);
            } else {
              root.finishedWork = null;
              finishedWork = renderRoot(root, expirationTime, false);

              if (finishedWork !== null) {
                completeRoot(root, finishedWork, expirationTime);
              }
            }
          } else {
            var _finishedWork = root.finishedWork;

            if (_finishedWork !== null) {
              completeRoot(root, _finishedWork, expirationTime);
            } else {
              root.finishedWork = null;
              _finishedWork = renderRoot(root, expirationTime, true);

              if (_finishedWork !== null) {
                if (!shouldYield()) {
                  completeRoot(root, _finishedWork, expirationTime);
                } else {
                  root.finishedWork = _finishedWork;
                }
              }
            }
          }

          isRendering = false;
        }

        function completeRoot(root, finishedWork, expirationTime) {
          var firstBatch = root.firstBatch;

          if (firstBatch !== null && firstBatch._expirationTime <= expirationTime) {
            if (completedBatches === null) {
              completedBatches = [firstBatch];
            } else {
              completedBatches.push(firstBatch);
            }

            if (firstBatch._defer) {
              root.finishedWork = finishedWork;
              root.remainingExpirationTime = NoWork;
              return;
            }
          }

          root.finishedWork = null;
          root.remainingExpirationTime = commitRoot(finishedWork);
        }

        function shouldYield() {
          if (deadline === null) {
            return false;
          }

          if (deadline.timeRemaining() > timeHeuristicForUnitOfWork) {
            return false;
          }

          deadlineDidExpire = true;
          return true;
        }

        function onUncaughtError(error) {
          invariant(nextFlushedRoot !== null, "Should be working on a root. This error is likely caused by a bug in " + "React. Please file an issue.");
          nextFlushedRoot.remainingExpirationTime = NoWork;

          if (!hasUnhandledError) {
            hasUnhandledError = true;
            unhandledError = error;
          }
        }

        function batchedUpdates(fn, a) {
          var previousIsBatchingUpdates = isBatchingUpdates;
          isBatchingUpdates = true;

          try {
            return fn(a);
          } finally {
            isBatchingUpdates = previousIsBatchingUpdates;

            if (!isBatchingUpdates && !isRendering) {
              performSyncWork();
            }
          }
        }

        function unbatchedUpdates(fn, a) {
          if (isBatchingUpdates && !isUnbatchingUpdates) {
            isUnbatchingUpdates = true;

            try {
              return fn(a);
            } finally {
              isUnbatchingUpdates = false;
            }
          }

          return fn(a);
        }

        function flushSync(fn, a) {
          invariant(!isRendering, "flushSync was called from inside a lifecycle method. It cannot be " + "called when React is already rendering.");
          var previousIsBatchingUpdates = isBatchingUpdates;
          isBatchingUpdates = true;

          try {
            return syncUpdates(fn, a);
          } finally {
            isBatchingUpdates = previousIsBatchingUpdates;
            performSyncWork();
          }
        }

        function interactiveUpdates(fn, a, b) {
          if (isBatchingInteractiveUpdates) {
            return fn(a, b);
          }

          if (!isBatchingUpdates && !isRendering && lowestPendingInteractiveExpirationTime !== NoWork) {
            performWork(lowestPendingInteractiveExpirationTime, false, null);
            lowestPendingInteractiveExpirationTime = NoWork;
          }

          var previousIsBatchingInteractiveUpdates = isBatchingInteractiveUpdates;
          var previousIsBatchingUpdates = isBatchingUpdates;
          isBatchingInteractiveUpdates = true;
          isBatchingUpdates = true;

          try {
            return fn(a, b);
          } finally {
            isBatchingInteractiveUpdates = previousIsBatchingInteractiveUpdates;
            isBatchingUpdates = previousIsBatchingUpdates;

            if (!isBatchingUpdates && !isRendering) {
              performSyncWork();
            }
          }
        }

        function flushInteractiveUpdates() {
          if (!isRendering && lowestPendingInteractiveExpirationTime !== NoWork) {
            performWork(lowestPendingInteractiveExpirationTime, false, null);
            lowestPendingInteractiveExpirationTime = NoWork;
          }
        }

        function flushControlled(fn) {
          var previousIsBatchingUpdates = isBatchingUpdates;
          isBatchingUpdates = true;

          try {
            syncUpdates(fn);
          } finally {
            isBatchingUpdates = previousIsBatchingUpdates;

            if (!isBatchingUpdates && !isRendering) {
              performWork(Sync, false, null);
            }
          }
        }

        return {
          recalculateCurrentTime: recalculateCurrentTime,
          computeExpirationForFiber: computeExpirationForFiber,
          scheduleWork: scheduleWork,
          requestWork: requestWork,
          flushRoot: flushRoot,
          batchedUpdates: batchedUpdates,
          unbatchedUpdates: unbatchedUpdates,
          flushSync: flushSync,
          flushControlled: flushControlled,
          deferredUpdates: deferredUpdates,
          syncUpdates: syncUpdates,
          interactiveUpdates: interactiveUpdates,
          flushInteractiveUpdates: flushInteractiveUpdates,
          computeUniqueAsyncExpiration: computeUniqueAsyncExpiration,
          legacyContext: legacyContext
        };
      };

      var didWarnAboutNestedUpdates = void 0;
      {
        didWarnAboutNestedUpdates = false;
      }

      var ReactFiberReconciler$1 = function ReactFiberReconciler$1(config) {
        var getPublicInstance = config.getPublicInstance;

        var _ReactFiberScheduler = ReactFiberScheduler(config),
            computeUniqueAsyncExpiration = _ReactFiberScheduler.computeUniqueAsyncExpiration,
            recalculateCurrentTime = _ReactFiberScheduler.recalculateCurrentTime,
            computeExpirationForFiber = _ReactFiberScheduler.computeExpirationForFiber,
            scheduleWork = _ReactFiberScheduler.scheduleWork,
            requestWork = _ReactFiberScheduler.requestWork,
            flushRoot = _ReactFiberScheduler.flushRoot,
            batchedUpdates = _ReactFiberScheduler.batchedUpdates,
            unbatchedUpdates = _ReactFiberScheduler.unbatchedUpdates,
            flushSync = _ReactFiberScheduler.flushSync,
            flushControlled = _ReactFiberScheduler.flushControlled,
            deferredUpdates = _ReactFiberScheduler.deferredUpdates,
            syncUpdates = _ReactFiberScheduler.syncUpdates,
            interactiveUpdates = _ReactFiberScheduler.interactiveUpdates,
            flushInteractiveUpdates = _ReactFiberScheduler.flushInteractiveUpdates,
            legacyContext = _ReactFiberScheduler.legacyContext;

        var findCurrentUnmaskedContext = legacyContext.findCurrentUnmaskedContext,
            isContextProvider = legacyContext.isContextProvider,
            processChildContext = legacyContext.processChildContext;

        function getContextForSubtree(parentComponent) {
          if (!parentComponent) {
            return emptyObject;
          }

          var fiber = get(parentComponent);
          var parentContext = findCurrentUnmaskedContext(fiber);
          return isContextProvider(fiber) ? processChildContext(fiber, parentContext) : parentContext;
        }

        function scheduleRootUpdate(current, element, currentTime, expirationTime, callback) {
          {
            if (ReactDebugCurrentFiber.phase === "render" && ReactDebugCurrentFiber.current !== null && !didWarnAboutNestedUpdates) {
              didWarnAboutNestedUpdates = true;
              warning(false, "Render methods should be a pure function of props and state; " + "triggering nested component updates from render is not allowed. " + "If necessary, trigger nested updates in componentDidUpdate.\n\n" + "Check the render method of %s.", getComponentName(ReactDebugCurrentFiber.current) || "Unknown");
            }
          }
          callback = callback === undefined ? null : callback;
          {
            !(callback === null || typeof callback === "function") ? warning(false, "render(...): Expected the last optional `callback` argument to be a " + "function. Instead received: %s.", callback) : void 0;
          }
          var update = {
            expirationTime: expirationTime,
            partialState: {
              element: element
            },
            callback: callback,
            isReplace: false,
            isForced: false,
            capturedValue: null,
            next: null
          };
          insertUpdateIntoFiber(current, update);
          scheduleWork(current, expirationTime);
          return expirationTime;
        }

        function _updateContainerAtExpirationTime(element, container, parentComponent, currentTime, expirationTime, callback) {
          var current = container.current;
          {
            if (ReactFiberInstrumentation_1.debugTool) {
              if (current.alternate === null) {
                ReactFiberInstrumentation_1.debugTool.onMountContainer(container);
              } else if (element === null) {
                ReactFiberInstrumentation_1.debugTool.onUnmountContainer(container);
              } else {
                ReactFiberInstrumentation_1.debugTool.onUpdateContainer(container);
              }
            }
          }
          var context = getContextForSubtree(parentComponent);

          if (container.context === null) {
            container.context = context;
          } else {
            container.pendingContext = context;
          }

          return scheduleRootUpdate(current, element, currentTime, expirationTime, callback);
        }

        function findHostInstance(fiber) {
          var hostFiber = findCurrentHostFiber(fiber);

          if (hostFiber === null) {
            return null;
          }

          return hostFiber.stateNode;
        }

        return {
          createContainer: function createContainer(containerInfo, isAsync, hydrate) {
            return createFiberRoot(containerInfo, isAsync, hydrate);
          },
          updateContainer: function updateContainer(element, container, parentComponent, callback) {
            var current = container.current;
            var currentTime = recalculateCurrentTime();
            var expirationTime = computeExpirationForFiber(current);
            return _updateContainerAtExpirationTime(element, container, parentComponent, currentTime, expirationTime, callback);
          },
          updateContainerAtExpirationTime: function updateContainerAtExpirationTime(element, container, parentComponent, expirationTime, callback) {
            var currentTime = recalculateCurrentTime();
            return _updateContainerAtExpirationTime(element, container, parentComponent, currentTime, expirationTime, callback);
          },
          flushRoot: flushRoot,
          requestWork: requestWork,
          computeUniqueAsyncExpiration: computeUniqueAsyncExpiration,
          batchedUpdates: batchedUpdates,
          unbatchedUpdates: unbatchedUpdates,
          deferredUpdates: deferredUpdates,
          syncUpdates: syncUpdates,
          interactiveUpdates: interactiveUpdates,
          flushInteractiveUpdates: flushInteractiveUpdates,
          flushControlled: flushControlled,
          flushSync: flushSync,
          getPublicRootInstance: function getPublicRootInstance(container) {
            var containerFiber = container.current;

            if (!containerFiber.child) {
              return null;
            }

            switch (containerFiber.child.tag) {
              case HostComponent:
                return getPublicInstance(containerFiber.child.stateNode);

              default:
                return containerFiber.child.stateNode;
            }
          },
          findHostInstance: findHostInstance,
          findHostInstanceWithNoPortals: function findHostInstanceWithNoPortals(fiber) {
            var hostFiber = findCurrentHostFiberWithNoPortals(fiber);

            if (hostFiber === null) {
              return null;
            }

            return hostFiber.stateNode;
          },
          injectIntoDevTools: function injectIntoDevTools(devToolsConfig) {
            var _findFiberByHostInstance = devToolsConfig.findFiberByHostInstance;
            return injectInternals(babelHelpers.extends({}, devToolsConfig, {
              findHostInstanceByFiber: function findHostInstanceByFiber(fiber) {
                return findHostInstance(fiber);
              },
              findFiberByHostInstance: function findFiberByHostInstance(instance) {
                if (!_findFiberByHostInstance) {
                  return null;
                }

                return _findFiberByHostInstance(instance);
              }
            }));
          }
        };
      };

      var ReactFiberReconciler$2 = Object.freeze({
        default: ReactFiberReconciler$1
      });
      var ReactFiberReconciler$3 = ReactFiberReconciler$2 && ReactFiberReconciler$1 || ReactFiberReconciler$2;
      var reactReconciler = ReactFiberReconciler$3["default"] ? ReactFiberReconciler$3["default"] : ReactFiberReconciler$3;
      var viewConfigCallbacks = new Map();
      var viewConfigs = new Map();

      function register(name, callback) {
        invariant(!viewConfigCallbacks.has(name), "Tried to register two views with the same name %s", name);
        viewConfigCallbacks.set(name, callback);
        return name;
      }

      function get$1(name) {
        var viewConfig = void 0;

        if (!viewConfigs.has(name)) {
          var callback = viewConfigCallbacks.get(name);
          invariant(typeof callback === "function", "View config not found for name %s", name);
          viewConfigCallbacks.set(name, null);
          viewConfig = callback();
          viewConfigs.set(name, viewConfig);
        } else {
          viewConfig = viewConfigs.get(name);
        }

        invariant(viewConfig, "View config not found for name %s", name);
        return viewConfig;
      }

      function _classCallCheck$2(instance, Constructor) {
        if (!(instance instanceof Constructor)) {
          throw new TypeError("Cannot call a class as a function");
        }
      }

      var ReactNativeFiberHostComponent = function () {
        function ReactNativeFiberHostComponent(tag, viewConfig) {
          _classCallCheck$2(this, ReactNativeFiberHostComponent);

          this._nativeTag = tag;
          this._children = [];
          this.viewConfig = viewConfig;
        }

        ReactNativeFiberHostComponent.prototype.blur = function blur() {
          TextInputState.blurTextInput(this._nativeTag);
        };

        ReactNativeFiberHostComponent.prototype.focus = function focus() {
          TextInputState.focusTextInput(this._nativeTag);
        };

        ReactNativeFiberHostComponent.prototype.measure = function measure(callback) {
          UIManager.measure(this._nativeTag, mountSafeCallback(this, callback));
        };

        ReactNativeFiberHostComponent.prototype.measureInWindow = function measureInWindow(callback) {
          UIManager.measureInWindow(this._nativeTag, mountSafeCallback(this, callback));
        };

        ReactNativeFiberHostComponent.prototype.measureLayout = function measureLayout(relativeToNativeNode, onSuccess, onFail) {
          UIManager.measureLayout(this._nativeTag, relativeToNativeNode, mountSafeCallback(this, onFail), mountSafeCallback(this, onSuccess));
        };

        ReactNativeFiberHostComponent.prototype.setNativeProps = function setNativeProps(nativeProps) {
          {
            warnForStyleProps(nativeProps, this.viewConfig.validAttributes);
          }
          var updatePayload = create(nativeProps, this.viewConfig.validAttributes);

          if (updatePayload != null) {
            UIManager.updateView(this._nativeTag, this.viewConfig.uiViewClassName, updatePayload);
          }
        };

        return ReactNativeFiberHostComponent;
      }();

      var hasNativePerformanceNow = typeof performance === "object" && typeof performance.now === "function";
      var now = hasNativePerformanceNow ? function () {
        return performance.now();
      } : function () {
        return Date.now();
      };
      var scheduledCallback = null;
      var frameDeadline = 0;
      var frameDeadlineObject = {
        timeRemaining: function timeRemaining() {
          return frameDeadline - now();
        },
        didTimeout: false
      };

      function setTimeoutCallback() {
        frameDeadline = now() + 5;
        var callback = scheduledCallback;
        scheduledCallback = null;

        if (callback !== null) {
          callback(frameDeadlineObject);
        }
      }

      function scheduleDeferredCallback(callback) {
        scheduledCallback = callback;
        return setTimeout(setTimeoutCallback, 1);
      }

      function cancelDeferredCallback(callbackID) {
        scheduledCallback = null;
        clearTimeout(callbackID);
      }

      function recursivelyUncacheFiberNode(node) {
        if (typeof node === "number") {
          uncacheFiberNode(node);
        } else {
          uncacheFiberNode(node._nativeTag);

          node._children.forEach(recursivelyUncacheFiberNode);
        }
      }

      var NativeRenderer = reactReconciler({
        appendInitialChild: function appendInitialChild(parentInstance, child) {
          parentInstance._children.push(child);
        },
        createInstance: function createInstance(type, props, rootContainerInstance, hostContext, internalInstanceHandle) {
          var tag = ReactNativeTagHandles.allocateTag();
          var viewConfig = get$1(type);
          {
            for (var key in viewConfig.validAttributes) {
              if (props.hasOwnProperty(key)) {
                deepFreezeAndThrowOnMutationInDev(props[key]);
              }
            }
          }
          var updatePayload = create(props, viewConfig.validAttributes);
          UIManager.createView(tag, viewConfig.uiViewClassName, rootContainerInstance, updatePayload);
          var component = new ReactNativeFiberHostComponent(tag, viewConfig);
          precacheFiberNode(internalInstanceHandle, tag);
          updateFiberProps(tag, props);
          return component;
        },
        createTextInstance: function createTextInstance(text, rootContainerInstance, hostContext, internalInstanceHandle) {
          var tag = ReactNativeTagHandles.allocateTag();
          UIManager.createView(tag, "RCTRawText", rootContainerInstance, {
            text: text
          });
          precacheFiberNode(internalInstanceHandle, tag);
          return tag;
        },
        finalizeInitialChildren: function finalizeInitialChildren(parentInstance, type, props, rootContainerInstance) {
          if (parentInstance._children.length === 0) {
            return false;
          }

          var nativeTags = parentInstance._children.map(function (child) {
            return typeof child === "number" ? child : child._nativeTag;
          });

          UIManager.setChildren(parentInstance._nativeTag, nativeTags);
          return false;
        },
        getRootHostContext: function getRootHostContext() {
          return emptyObject;
        },
        getChildHostContext: function getChildHostContext() {
          return emptyObject;
        },
        getPublicInstance: function getPublicInstance(instance) {
          return instance;
        },
        now: now,
        prepareForCommit: function prepareForCommit() {},
        prepareUpdate: function prepareUpdate(instance, type, oldProps, newProps, rootContainerInstance, hostContext) {
          return emptyObject;
        },
        resetAfterCommit: function resetAfterCommit() {},
        scheduleDeferredCallback: scheduleDeferredCallback,
        cancelDeferredCallback: cancelDeferredCallback,
        shouldDeprioritizeSubtree: function shouldDeprioritizeSubtree(type, props) {
          return false;
        },
        shouldSetTextContent: function shouldSetTextContent(type, props) {
          return false;
        },
        mutation: {
          appendChild: function appendChild(parentInstance, child) {
            var childTag = typeof child === "number" ? child : child._nativeTag;
            var children = parentInstance._children;
            var index = children.indexOf(child);

            if (index >= 0) {
              children.splice(index, 1);
              children.push(child);
              UIManager.manageChildren(parentInstance._nativeTag, [index], [children.length - 1], [], [], []);
            } else {
              children.push(child);
              UIManager.manageChildren(parentInstance._nativeTag, [], [], [childTag], [children.length - 1], []);
            }
          },
          appendChildToContainer: function appendChildToContainer(parentInstance, child) {
            var childTag = typeof child === "number" ? child : child._nativeTag;
            UIManager.setChildren(parentInstance, [childTag]);
          },
          commitTextUpdate: function commitTextUpdate(textInstance, oldText, newText) {
            UIManager.updateView(textInstance, "RCTRawText", {
              text: newText
            });
          },
          commitMount: function commitMount(instance, type, newProps, internalInstanceHandle) {},
          commitUpdate: function commitUpdate(instance, updatePayloadTODO, type, oldProps, newProps, internalInstanceHandle) {
            var viewConfig = instance.viewConfig;
            updateFiberProps(instance._nativeTag, newProps);
            var updatePayload = diff(oldProps, newProps, viewConfig.validAttributes);

            if (updatePayload != null) {
              UIManager.updateView(instance._nativeTag, viewConfig.uiViewClassName, updatePayload);
            }
          },
          insertBefore: function insertBefore(parentInstance, child, beforeChild) {
            var children = parentInstance._children;
            var index = children.indexOf(child);

            if (index >= 0) {
              children.splice(index, 1);
              var beforeChildIndex = children.indexOf(beforeChild);
              children.splice(beforeChildIndex, 0, child);
              UIManager.manageChildren(parentInstance._nativeTag, [index], [beforeChildIndex], [], [], []);
            } else {
              var _beforeChildIndex = children.indexOf(beforeChild);

              children.splice(_beforeChildIndex, 0, child);
              var childTag = typeof child === "number" ? child : child._nativeTag;
              UIManager.manageChildren(parentInstance._nativeTag, [], [], [childTag], [_beforeChildIndex], []);
            }
          },
          insertInContainerBefore: function insertInContainerBefore(parentInstance, child, beforeChild) {
            invariant(typeof parentInstance !== "number", "Container does not support insertBefore operation");
          },
          removeChild: function removeChild(parentInstance, child) {
            recursivelyUncacheFiberNode(child);
            var children = parentInstance._children;
            var index = children.indexOf(child);
            children.splice(index, 1);
            UIManager.manageChildren(parentInstance._nativeTag, [], [], [], [], [index]);
          },
          removeChildFromContainer: function removeChildFromContainer(parentInstance, child) {
            recursivelyUncacheFiberNode(child);
            UIManager.manageChildren(parentInstance, [], [], [], [], [0]);
          },
          resetTextContent: function resetTextContent(instance) {}
        }
      });
      var getInspectorDataForViewTag = void 0;
      {
        var traverseOwnerTreeUp = function traverseOwnerTreeUp(hierarchy, instance) {
          if (instance) {
            hierarchy.unshift(instance);
            traverseOwnerTreeUp(hierarchy, instance._debugOwner);
          }
        };

        var getOwnerHierarchy = function getOwnerHierarchy(instance) {
          var hierarchy = [];
          traverseOwnerTreeUp(hierarchy, instance);
          return hierarchy;
        };

        var lastNonHostInstance = function lastNonHostInstance(hierarchy) {
          for (var i = hierarchy.length - 1; i > 1; i--) {
            var instance = hierarchy[i];

            if (instance.tag !== HostComponent) {
              return instance;
            }
          }

          return hierarchy[0];
        };

        var getHostProps = function getHostProps(fiber) {
          var host = findCurrentHostFiber(fiber);

          if (host) {
            return host.memoizedProps || emptyObject;
          }

          return emptyObject;
        };

        var getHostNode = function getHostNode(fiber, findNodeHandle) {
          var hostNode = void 0;

          while (fiber) {
            if (fiber.stateNode !== null && fiber.tag === HostComponent) {
              hostNode = findNodeHandle(fiber.stateNode);
            }

            if (hostNode) {
              return hostNode;
            }

            fiber = fiber.child;
          }

          return null;
        };

        var createHierarchy = function createHierarchy(fiberHierarchy) {
          return fiberHierarchy.map(function (fiber) {
            return {
              name: getComponentName(fiber),
              getInspectorData: function getInspectorData(findNodeHandle) {
                return {
                  measure: function measure(callback) {
                    return UIManager.measure(getHostNode(fiber, findNodeHandle), callback);
                  },
                  props: getHostProps(fiber),
                  source: fiber._debugSource
                };
              }
            };
          });
        };

        getInspectorDataForViewTag = function getInspectorDataForViewTag(viewTag) {
          var closestInstance = getInstanceFromTag(viewTag);

          if (!closestInstance) {
            return {
              hierarchy: [],
              props: emptyObject,
              selection: null,
              source: null
            };
          }

          var fiber = findCurrentFiberUsingSlowPath(closestInstance);
          var fiberHierarchy = getOwnerHierarchy(fiber);
          var instance = lastNonHostInstance(fiberHierarchy);
          var hierarchy = createHierarchy(fiberHierarchy);
          var props = getHostProps(instance);
          var source = instance._debugSource;
          var selection = fiberHierarchy.indexOf(instance);
          return {
            hierarchy: hierarchy,
            props: props,
            selection: selection,
            source: source
          };
        };
      }

      var createReactNativeComponentClass = function createReactNativeComponentClass(name, callback) {
        return register(name, callback);
      };

      function takeSnapshot(view, options) {
        if (typeof view !== "number" && view !== "window") {
          view = findNumericNodeHandleFiber(view) || "window";
        }

        return UIManager.__takeSnapshot(view, options);
      }

      injectFindHostInstance(NativeRenderer.findHostInstance);
      injection$2.injectRenderer(NativeRenderer);
      var roots = new Map();
      var ReactNativeRenderer = {
        NativeComponent: ReactNativeComponent,
        findNodeHandle: findNumericNodeHandleFiber,
        render: function render(element, containerTag, callback) {
          var root = roots.get(containerTag);

          if (!root) {
            root = NativeRenderer.createContainer(containerTag, false, false);
            roots.set(containerTag, root);
          }

          NativeRenderer.updateContainer(element, root, null, callback);
          return NativeRenderer.getPublicRootInstance(root);
        },
        unmountComponentAtNode: function unmountComponentAtNode(containerTag) {
          var root = roots.get(containerTag);

          if (root) {
            NativeRenderer.updateContainer(null, root, null, function () {
              roots["delete"](containerTag);
            });
          }
        },
        unmountComponentAtNodeAndRemoveContainer: function unmountComponentAtNodeAndRemoveContainer(containerTag) {
          ReactNativeRenderer.unmountComponentAtNode(containerTag);
          UIManager.removeRootView(containerTag);
        },
        createPortal: function createPortal(children, containerTag) {
          var key = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : null;
          return _createPortal(children, containerTag, null, key);
        },
        unstable_batchedUpdates: batchedUpdates,
        flushSync: NativeRenderer.flushSync,
        __SECRET_INTERNALS_DO_NOT_USE_OR_YOU_WILL_BE_FIRED: {
          NativeMethodsMixin: NativeMethodsMixin,
          ReactNativeBridgeEventPlugin: ReactNativeBridgeEventPlugin,
          ReactNativeComponentTree: ReactNativeComponentTree,
          ReactNativePropRegistry: ReactNativePropRegistry,
          TouchHistoryMath: TouchHistoryMath,
          createReactNativeComponentClass: createReactNativeComponentClass,
          takeSnapshot: takeSnapshot
        }
      };
      {
        babelHelpers.extends(ReactNativeRenderer.__SECRET_INTERNALS_DO_NOT_USE_OR_YOU_WILL_BE_FIRED, {
          ReactDebugTool: {
            addHook: function addHook() {},
            removeHook: function removeHook() {}
          },
          ReactPerf: {
            start: function start() {},
            stop: function stop() {},
            printInclusive: function printInclusive() {},
            printWasted: function printWasted() {}
          }
        });
      }
      NativeRenderer.injectIntoDevTools({
        findFiberByHostInstance: getInstanceFromTag,
        getInspectorDataForViewTag: getInspectorDataForViewTag,
        bundleType: 1,
        version: ReactVersion,
        rendererPackageName: "react-native-renderer"
      });
      var ReactNativeRenderer$2 = Object.freeze({
        default: ReactNativeRenderer
      });
      var ReactNativeRenderer$3 = ReactNativeRenderer$2 && ReactNativeRenderer || ReactNativeRenderer$2;
      var reactNativeRenderer = ReactNativeRenderer$3["default"] ? ReactNativeRenderer$3["default"] : ReactNativeRenderer$3;
      module.exports = reactNativeRenderer;
    })();
  }
},50,[51,18,19,16,121,122,123,124,116,12,15,125,59,20,29,126],"ReactNativeRenderer-dev");