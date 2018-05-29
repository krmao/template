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
},"36707ef8c51a3d4168c7191597117671",["7d7d78f5a0c234251af47713b1e4b4cc","ee4b9bb0852a41afb06d2c68334f0f19","3ca231031f859f6a9deeb308a64495f7","a140e1090b5f286a36e432887e992109","f2c19e474cad2bfe5e78b9a682b019c8","884bc352f97b270ddc347d577771af2d","e8f159f7acd84ab33d81898fe639be0f","d03068cfc94974673502036011366fe2","671e0d3f7a033da96d1450e1878fc16f","0a19f3ab228a275f5b22079b817bcfca","d58aebb81f3200b441491ee71f343dd5","79c7f38aacaf813c729418c91402b91c","b090ac9e0b10c45b80ebd8c19c20504a","bd82c605a16f8c2f5185d959b02a54dc","b54f8d6a8fec03e3197a9d378e32fbd2","096007123c886a641b5b7e629326a442","fa51b6ea7707c76b9c6fd243fa47e907","3be364ddfbdd844dd00f981b5bc4da3e","a9f6e522b9deca7a3cff0ca4420ffce4","0c8048c84a9b8e4bf9556e3d67e355e7","f01475cdda1d6eb3c4ae0a2b485aaba1","fb53f04427490b2bd8fd29ce5c52844e","9339c066c5d7ca261099796dfd4c05ea","d4716000500953e0fb2e4fe61254eaf0","e33cf0ee3b102c64134b83b27856d5d6","1060a7fdd4114915bad6b6943cf86a21","ac06206498d0d7a7483261a1a9746d1d","f933a225f808397a1fa97f6bdd87af23","b33721b03d3ba9dbccc1a2744ae23d68","ce21807d4d291be64fa852393519f6c8","87dfc7fd955cf83a76561aa8b152e5c4","9ce443f0435b534a4b0cfc7f0a97c5e0","2bf7838d360efae9f2a55682481f7cb7","6c76943e9c4343ad8e7106a115144a15"],"InitializeCore");