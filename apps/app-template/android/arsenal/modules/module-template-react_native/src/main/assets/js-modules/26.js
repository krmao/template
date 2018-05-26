__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var ErrorUtils = _require(_dependencyMap[0], 'ErrorUtils');

  var Systrace = _require(_dependencyMap[1], 'Systrace');

  var deepFreezeAndThrowOnMutationInDev = _require(_dependencyMap[2], 'deepFreezeAndThrowOnMutationInDev');

  var invariant = _require(_dependencyMap[3], 'fbjs/lib/invariant');

  var stringifySafe = _require(_dependencyMap[4], 'stringifySafe');

  var TO_JS = 0;
  var TO_NATIVE = 1;
  var MODULE_IDS = 0;
  var METHOD_IDS = 1;
  var PARAMS = 2;
  var MIN_TIME_BETWEEN_FLUSHES_MS = 5;
  var TRACE_TAG_REACT_APPS = 1 << 17;
  var DEBUG_INFO_LIMIT = 32;
  var JSTimers = null;

  var MessageQueue = function () {
    function MessageQueue() {
      var shouldUninstallGlobalErrorHandler = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : false;
      babelHelpers.classCallCheck(this, MessageQueue);
      this._lazyCallableModules = {};
      this._queue = [[], [], [], 0];
      this._successCallbacks = [];
      this._failureCallbacks = [];
      this._callID = 0;
      this._lastFlush = 0;
      this._eventLoopStartTime = new Date().getTime();

      if (shouldUninstallGlobalErrorHandler) {
        this.uninstallGlobalErrorHandler();
      } else {
        this.installGlobalErrorHandler();
      }

      if (__DEV__) {
        this._debugInfo = {};
        this._remoteModuleTable = {};
        this._remoteMethodTable = {};
      }

      this.callFunctionReturnFlushedQueue = this.callFunctionReturnFlushedQueue.bind(this);
      this.callFunctionReturnResultAndFlushedQueue = this.callFunctionReturnResultAndFlushedQueue.bind(this);
      this.flushedQueue = this.flushedQueue.bind(this);
      this.invokeCallbackAndReturnFlushedQueue = this.invokeCallbackAndReturnFlushedQueue.bind(this);
    }

    babelHelpers.createClass(MessageQueue, [{
      key: "callFunctionReturnFlushedQueue",
      value: function callFunctionReturnFlushedQueue(module, method, args) {
        var _this = this;

        this.__guard(function () {
          _this.__callFunction(module, method, args);
        });

        return this.flushedQueue();
      }
    }, {
      key: "callFunctionReturnResultAndFlushedQueue",
      value: function callFunctionReturnResultAndFlushedQueue(module, method, args) {
        var _this2 = this;

        var result = void 0;

        this.__guard(function () {
          result = _this2.__callFunction(module, method, args);
        });

        return [result, this.flushedQueue()];
      }
    }, {
      key: "invokeCallbackAndReturnFlushedQueue",
      value: function invokeCallbackAndReturnFlushedQueue(cbID, args) {
        var _this3 = this;

        this.__guard(function () {
          _this3.__invokeCallback(cbID, args);
        });

        return this.flushedQueue();
      }
    }, {
      key: "flushedQueue",
      value: function flushedQueue() {
        var _this4 = this;

        this.__guard(function () {
          _this4.__callImmediates();
        });

        var queue = this._queue;
        this._queue = [[], [], [], this._callID];
        return queue[0].length ? queue : null;
      }
    }, {
      key: "getEventLoopRunningTime",
      value: function getEventLoopRunningTime() {
        return new Date().getTime() - this._eventLoopStartTime;
      }
    }, {
      key: "registerCallableModule",
      value: function registerCallableModule(name, module) {
        this._lazyCallableModules[name] = function () {
          return module;
        };
      }
    }, {
      key: "registerLazyCallableModule",
      value: function registerLazyCallableModule(name, factory) {
        var module = void 0;
        var getValue = factory;

        this._lazyCallableModules[name] = function () {
          if (getValue) {
            module = getValue();
            getValue = null;
          }

          return module;
        };
      }
    }, {
      key: "getCallableModule",
      value: function getCallableModule(name) {
        var getValue = this._lazyCallableModules[name];
        return getValue ? getValue() : null;
      }
    }, {
      key: "enqueueNativeCall",
      value: function enqueueNativeCall(moduleID, methodID, params, onFail, onSucc) {
        if (onFail || onSucc) {
          if (__DEV__) {
            this._debugInfo[this._callID] = [moduleID, methodID];

            if (this._callID > DEBUG_INFO_LIMIT) {
              delete this._debugInfo[this._callID - DEBUG_INFO_LIMIT];
            }
          }

          onFail && params.push(this._callID << 1);
          onSucc && params.push(this._callID << 1 | 1);
          this._successCallbacks[this._callID] = onSucc;
          this._failureCallbacks[this._callID] = onFail;
        }

        if (__DEV__) {
          global.nativeTraceBeginAsyncFlow && global.nativeTraceBeginAsyncFlow(TRACE_TAG_REACT_APPS, 'native', this._callID);
        }

        this._callID++;

        this._queue[MODULE_IDS].push(moduleID);

        this._queue[METHOD_IDS].push(methodID);

        if (__DEV__) {
          var isValidArgument = function isValidArgument(val) {
            var t = typeof val;

            if (t === 'undefined' || t === 'null' || t === 'boolean' || t === 'number' || t === 'string') {
              return true;
            }

            if (t === 'function' || t !== 'object') {
              return false;
            }

            if (Array.isArray(val)) {
              return val.every(isValidArgument);
            }

            for (var k in val) {
              if (typeof val[k] !== 'function' && !isValidArgument(val[k])) {
                return false;
              }
            }

            return true;
          };

          invariant(isValidArgument(params), '%s is not usable as a native method argument', params);
          deepFreezeAndThrowOnMutationInDev(params);
        }

        this._queue[PARAMS].push(params);

        var now = new Date().getTime();

        if (global.nativeFlushQueueImmediate && (now - this._lastFlush >= MIN_TIME_BETWEEN_FLUSHES_MS || this._inCall === 0)) {
          var queue = this._queue;
          this._queue = [[], [], [], this._callID];
          this._lastFlush = now;
          global.nativeFlushQueueImmediate(queue);
        }

        Systrace.counterEvent('pending_js_to_native_queue', this._queue[0].length);

        if (__DEV__ && this.__spy && isFinite(moduleID)) {
          this.__spy({
            type: TO_NATIVE,
            module: this._remoteModuleTable[moduleID],
            method: this._remoteMethodTable[moduleID][methodID],
            args: params
          });
        } else if (this.__spy) {
          this.__spy({
            type: TO_NATIVE,
            module: moduleID + '',
            method: methodID,
            args: params
          });
        }
      }
    }, {
      key: "createDebugLookup",
      value: function createDebugLookup(moduleID, name, methods) {
        if (__DEV__) {
          this._remoteModuleTable[moduleID] = name;
          this._remoteMethodTable[moduleID] = methods;
        }
      }
    }, {
      key: "uninstallGlobalErrorHandler",
      value: function uninstallGlobalErrorHandler() {
        this.__guard = this.__guardUnsafe;
      }
    }, {
      key: "installGlobalErrorHandler",
      value: function installGlobalErrorHandler() {
        this.__guard = this.__guardSafe;
      }
    }, {
      key: "__guardUnsafe",
      value: function __guardUnsafe(fn) {
        this._inCall++;
        fn();
        this._inCall--;
      }
    }, {
      key: "__guardSafe",
      value: function __guardSafe(fn) {
        this._inCall++;

        try {
          fn();
        } catch (error) {
          ErrorUtils.reportFatalError(error);
        } finally {
          this._inCall--;
        }
      }
    }, {
      key: "__callImmediates",
      value: function __callImmediates() {
        Systrace.beginEvent('JSTimers.callImmediates()');

        if (!JSTimers) {
          JSTimers = _require(_dependencyMap[5], 'JSTimers');
        }

        JSTimers.callImmediates();
        Systrace.endEvent();
      }
    }, {
      key: "__callFunction",
      value: function __callFunction(module, method, args) {
        this._lastFlush = new Date().getTime();
        this._eventLoopStartTime = this._lastFlush;
        Systrace.beginEvent(module + "." + method + "()");

        if (this.__spy) {
          this.__spy({
            type: TO_JS,
            module: module,
            method: method,
            args: args
          });
        }

        var moduleMethods = this.getCallableModule(module);
        invariant(!!moduleMethods, 'Module %s is not a registered callable module (calling %s)', module, method);
        invariant(!!moduleMethods[method], 'Method %s does not exist on module %s', method, module);
        var result = moduleMethods[method].apply(moduleMethods, args);
        Systrace.endEvent();
        return result;
      }
    }, {
      key: "__invokeCallback",
      value: function __invokeCallback(cbID, args) {
        this._lastFlush = new Date().getTime();
        this._eventLoopStartTime = this._lastFlush;
        var callID = cbID >>> 1;
        var isSuccess = cbID & 1;
        var callback = isSuccess ? this._successCallbacks[callID] : this._failureCallbacks[callID];

        if (__DEV__) {
          var debug = this._debugInfo[callID];

          var _module = debug && this._remoteModuleTable[debug[0]];

          var _method = debug && this._remoteMethodTable[debug[0]][debug[1]];

          if (!callback) {
            var errorMessage = "Callback with id " + cbID + ": " + _module + "." + _method + "() not found";

            if (_method) {
              errorMessage = "The callback " + _method + "() exists in module " + _module + ", " + 'but only one callback may be registered to a function in a native module.';
            }

            invariant(callback, errorMessage);
          }

          var profileName = debug ? '<callback for ' + _module + '.' + _method + '>' : cbID;

          if (callback && this.__spy) {
            this.__spy({
              type: TO_JS,
              module: null,
              method: profileName,
              args: args
            });
          }

          Systrace.beginEvent("MessageQueue.invokeCallback(" + profileName + ", " + stringifySafe(args) + ")");
        }

        if (!callback) {
          return;
        }

        this._successCallbacks[callID] = this._failureCallbacks[callID] = null;
        callback.apply(undefined, babelHelpers.toConsumableArray(args));

        if (__DEV__) {
          Systrace.endEvent();
        }
      }
    }], [{
      key: "spy",
      value: function spy(spyOrToggle) {
        if (spyOrToggle === true) {
          MessageQueue.prototype.__spy = function (info) {
            console.log((info.type === TO_JS ? 'N->JS' : 'JS->N') + " : " + ("" + (info.module ? info.module + '.' : '') + info.method) + ("(" + JSON.stringify(info.args) + ")"));
          };
        } else if (spyOrToggle === false) {
          MessageQueue.prototype.__spy = null;
        } else {
          MessageQueue.prototype.__spy = spyOrToggle;
        }
      }
    }]);
    return MessageQueue;
  }();

  module.exports = MessageQueue;
},26,[27,28,29,18,30,31],"MessageQueue");