__d(function (global, _require3, module, exports, _dependencyMap) {
  'use strict';

  var _require = _require3(_dependencyMap[0], 'PolyfillFunctions'),
      polyfillObjectProperty = _require.polyfillObjectProperty,
      polyfillGlobal = _require.polyfillGlobal;

  if (global.GLOBAL === undefined) {
    global.GLOBAL = global;
  }

  if (global.window === undefined) {
    global.window = global;
  }

  var _shouldPolyfillCollection = _require3(_dependencyMap[1], '_shouldPolyfillES6Collection');

  if (_shouldPolyfillCollection('Map')) {
    polyfillGlobal('Map', function () {
      return _require3(_dependencyMap[2], 'Map');
    });
  }

  if (_shouldPolyfillCollection('Set')) {
    polyfillGlobal('Set', function () {
      return _require3(_dependencyMap[3], 'Set');
    });
  }

  global.process = global.process || {};
  global.process.env = global.process.env || {};

  if (!global.process.env.NODE_ENV) {
    global.process.env.NODE_ENV = __DEV__ ? 'development' : 'production';
  }

  if (global.__RCTProfileIsProfiling) {
    var Systrace = _require3(_dependencyMap[4], 'Systrace');

    Systrace.installReactHook();
    Systrace.setEnabled(true);
  }

  var ExceptionsManager = _require3(_dependencyMap[5], 'ExceptionsManager');

  ExceptionsManager.installConsoleErrorReporter();

  if (!global.__fbDisableExceptionsManager) {
    var handleError = function handleError(e, isFatal) {
      try {
        ExceptionsManager.handleException(e, isFatal);
      } catch (ee) {
        console.log('Failed to print error: ', ee.message);
        throw e;
      }
    };

    var ErrorUtils = _require3(_dependencyMap[6], 'ErrorUtils');

    ErrorUtils.setGlobalHandler(handleError);
  }

  var ReactNativeVersionCheck = _require3(_dependencyMap[7], 'ReactNativeVersionCheck');

  ReactNativeVersionCheck.checkVersions();
  polyfillGlobal('Promise', function () {
    return _require3(_dependencyMap[8], 'Promise');
  });
  polyfillGlobal('regeneratorRuntime', function () {
    delete global.regeneratorRuntime;

    _require3(_dependencyMap[9], 'regenerator-runtime/runtime');

    return global.regeneratorRuntime;
  });

  var defineLazyTimer = function defineLazyTimer(name) {
    polyfillGlobal(name, function () {
      return _require3(_dependencyMap[10], 'JSTimers')[name];
    });
  };

  defineLazyTimer('setTimeout');
  defineLazyTimer('setInterval');
  defineLazyTimer('setImmediate');
  defineLazyTimer('clearTimeout');
  defineLazyTimer('clearInterval');
  defineLazyTimer('clearImmediate');
  defineLazyTimer('requestAnimationFrame');
  defineLazyTimer('cancelAnimationFrame');
  defineLazyTimer('requestIdleCallback');
  defineLazyTimer('cancelIdleCallback');
  polyfillGlobal('XMLHttpRequest', function () {
    return _require3(_dependencyMap[11], 'XMLHttpRequest');
  });
  polyfillGlobal('FormData', function () {
    return _require3(_dependencyMap[12], 'FormData');
  });
  polyfillGlobal('fetch', function () {
    return _require3(_dependencyMap[13], 'fetch').fetch;
  });
  polyfillGlobal('Headers', function () {
    return _require3(_dependencyMap[13], 'fetch').Headers;
  });
  polyfillGlobal('Request', function () {
    return _require3(_dependencyMap[13], 'fetch').Request;
  });
  polyfillGlobal('Response', function () {
    return _require3(_dependencyMap[13], 'fetch').Response;
  });
  polyfillGlobal('WebSocket', function () {
    return _require3(_dependencyMap[14], 'WebSocket');
  });
  polyfillGlobal('Blob', function () {
    return _require3(_dependencyMap[15], 'Blob');
  });
  polyfillGlobal('File', function () {
    return _require3(_dependencyMap[16], 'File');
  });
  polyfillGlobal('FileReader', function () {
    return _require3(_dependencyMap[17], 'FileReader');
  });
  polyfillGlobal('URL', function () {
    return _require3(_dependencyMap[18], 'URL');
  });

  if (!global.alert) {
    global.alert = function (text) {
      _require3(_dependencyMap[19], 'Alert').alert('Alert', '' + text);
    };
  }

  var navigator = global.navigator;

  if (navigator === undefined) {
    global.navigator = navigator = {};
  }

  polyfillObjectProperty(navigator, 'product', function () {
    return 'ReactNative';
  });
  polyfillObjectProperty(navigator, 'geolocation', function () {
    return _require3(_dependencyMap[20], 'Geolocation');
  });

  var BatchedBridge = _require3(_dependencyMap[21], 'BatchedBridge');

  BatchedBridge.registerLazyCallableModule('Systrace', function () {
    return _require3(_dependencyMap[4], 'Systrace');
  });
  BatchedBridge.registerLazyCallableModule('JSTimers', function () {
    return _require3(_dependencyMap[10], 'JSTimers');
  });
  BatchedBridge.registerLazyCallableModule('HeapCapture', function () {
    return _require3(_dependencyMap[22], 'HeapCapture');
  });
  BatchedBridge.registerLazyCallableModule('SamplingProfiler', function () {
    return _require3(_dependencyMap[23], 'SamplingProfiler');
  });
  BatchedBridge.registerLazyCallableModule('RCTLog', function () {
    return _require3(_dependencyMap[24], 'RCTLog');
  });
  BatchedBridge.registerLazyCallableModule('RCTDeviceEventEmitter', function () {
    return _require3(_dependencyMap[25], 'RCTDeviceEventEmitter');
  });
  BatchedBridge.registerLazyCallableModule('RCTNativeAppEventEmitter', function () {
    return _require3(_dependencyMap[26], 'RCTNativeAppEventEmitter');
  });
  BatchedBridge.registerLazyCallableModule('PerformanceLogger', function () {
    return _require3(_dependencyMap[27], 'PerformanceLogger');
  });
  BatchedBridge.registerLazyCallableModule('JSDevSupportModule', function () {
    return _require3(_dependencyMap[28], 'JSDevSupportModule');
  });

  global.__fetchSegment = function (segmentId, callback) {
    var _require2 = _require3(_dependencyMap[29], 'NativeModules'),
        SegmentFetcher = _require2.SegmentFetcher;

    if (!SegmentFetcher) {
      throw new Error('SegmentFetcher is missing. Please ensure that it is ' + 'included as a NativeModule.');
    }

    SegmentFetcher.fetchSegment(segmentId, function (errorObject) {
      if (errorObject) {
        var error = new Error(errorObject.message);
        error.code = errorObject.code;
        callback(error);
      }

      callback(null);
    });
  };

  if (__DEV__) {
    if (!global.__RCTProfileIsProfiling) {
      BatchedBridge.registerCallableModule('HMRClient', _require3(_dependencyMap[30], 'HMRClient'));

      if (!window.document) {
        _require3(_dependencyMap[31], 'setupDevtools');
      }

      var JSInspector = _require3(_dependencyMap[32], 'JSInspector');

      JSInspector.registerAgent(_require3(_dependencyMap[33], 'NetworkAgent'));
    }
  }
},51,[52,53,54,58,28,59,27,64,66,74,31,75,89,62,90,86,92,93,94,95,97,25,100,101,102,40,103,104,106,24,107,113,118,119],"InitializeCore");