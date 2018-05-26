__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var BatchedBridge = _require(_dependencyMap[0], 'BatchedBridge');

  var invariant = _require(_dependencyMap[1], 'fbjs/lib/invariant');

  function genModule(config, moduleID) {
    if (!config) {
      return null;
    }

    var _config = babelHelpers.slicedToArray(config, 5),
        moduleName = _config[0],
        constants = _config[1],
        methods = _config[2],
        promiseMethods = _config[3],
        syncMethods = _config[4];

    invariant(!moduleName.startsWith('RCT') && !moduleName.startsWith('RK'), 'Module name prefixes should\'ve been stripped by the native side ' + 'but wasn\'t for ' + moduleName);

    if (!constants && !methods) {
      return {
        name: moduleName
      };
    }

    var module = {};
    methods && methods.forEach(function (methodName, methodID) {
      var isPromise = promiseMethods && arrayContains(promiseMethods, methodID);
      var isSync = syncMethods && arrayContains(syncMethods, methodID);
      invariant(!isPromise || !isSync, 'Cannot have a method that is both async and a sync hook');
      var methodType = isPromise ? 'promise' : isSync ? 'sync' : 'async';
      module[methodName] = genMethod(moduleID, methodID, methodType);
    });
    babelHelpers.extends(module, constants);

    if (__DEV__) {
      BatchedBridge.createDebugLookup(moduleID, moduleName, methods);
    }

    return {
      name: moduleName,
      module: module
    };
  }

  global.__fbGenNativeModule = genModule;

  function loadModule(name, moduleID) {
    invariant(global.nativeRequireModuleConfig, 'Can\'t lazily create module without nativeRequireModuleConfig');
    var config = global.nativeRequireModuleConfig(name);
    var info = genModule(config, moduleID);
    return info && info.module;
  }

  function genMethod(moduleID, methodID, type) {
    var fn = null;

    if (type === 'promise') {
      fn = function fn() {
        for (var _len = arguments.length, args = Array(_len), _key = 0; _key < _len; _key++) {
          args[_key] = arguments[_key];
        }

        return new Promise(function (resolve, reject) {
          BatchedBridge.enqueueNativeCall(moduleID, methodID, args, function (data) {
            return resolve(data);
          }, function (errorData) {
            return reject(createErrorFromErrorData(errorData));
          });
        });
      };
    } else if (type === 'sync') {
      fn = function fn() {
        if (__DEV__) {
          invariant(global.nativeCallSyncHook, 'Calling synchronous methods on native ' + 'modules is not supported in Chrome.\n\n Consider providing alternative ' + 'methods to expose this method in debug mode, e.g. by exposing constants ' + 'ahead-of-time.');
        }

        for (var _len2 = arguments.length, args = Array(_len2), _key2 = 0; _key2 < _len2; _key2++) {
          args[_key2] = arguments[_key2];
        }

        return global.nativeCallSyncHook(moduleID, methodID, args);
      };
    } else {
      fn = function fn() {
        for (var _len3 = arguments.length, args = Array(_len3), _key3 = 0; _key3 < _len3; _key3++) {
          args[_key3] = arguments[_key3];
        }

        var lastArg = args.length > 0 ? args[args.length - 1] : null;
        var secondLastArg = args.length > 1 ? args[args.length - 2] : null;
        var hasSuccessCallback = typeof lastArg === 'function';
        var hasErrorCallback = typeof secondLastArg === 'function';
        hasErrorCallback && invariant(hasSuccessCallback, 'Cannot have a non-function arg after a function arg.');
        var onSuccess = hasSuccessCallback ? lastArg : null;
        var onFail = hasErrorCallback ? secondLastArg : null;
        var callbackCount = hasSuccessCallback + hasErrorCallback;
        args = args.slice(0, args.length - callbackCount);
        BatchedBridge.enqueueNativeCall(moduleID, methodID, args, onFail, onSuccess);
      };
    }

    fn.type = type;
    return fn;
  }

  function arrayContains(array, value) {
    return array.indexOf(value) !== -1;
  }

  function createErrorFromErrorData(errorData) {
    var _ref = errorData || {},
        message = _ref.message,
        extraErrorInfo = babelHelpers.objectWithoutProperties(_ref, ["message"]);

    var error = new Error(message);
    error.framesToPop = 1;
    return babelHelpers.extends(error, extraErrorInfo);
  }

  var NativeModules = {};

  if (global.nativeModuleProxy) {
    NativeModules = global.nativeModuleProxy;
  } else {
    var bridgeConfig = global.__fbBatchedBridgeConfig;
    invariant(bridgeConfig, '__fbBatchedBridgeConfig is not set, cannot invoke native modules');

    var defineLazyObjectProperty = _require(_dependencyMap[2], 'defineLazyObjectProperty');

    (bridgeConfig.remoteModuleConfig || []).forEach(function (config, moduleID) {
      var info = genModule(config, moduleID);

      if (!info) {
        return;
      }

      if (info.module) {
        NativeModules[info.name] = info.module;
      } else {
          defineLazyObjectProperty(NativeModules, info.name, {
            get: function get() {
              return loadModule(info.name, moduleID);
            }
          });
        }
    });
  }

  module.exports = NativeModules;
},24,[25,18,39],"NativeModules");