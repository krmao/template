__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var Promise = _require(_dependencyMap[0], './core');

  var DEFAULT_WHITELIST = [ReferenceError, TypeError, RangeError];
  var enabled = false;
  exports.disable = disable;

  function disable() {
    enabled = false;
    Promise._37 = null;
    Promise._87 = null;
  }

  exports.enable = enable;

  function enable(options) {
    options = options || {};
    if (enabled) disable();
    enabled = true;
    var id = 0;
    var displayId = 0;
    var rejections = {};

    Promise._37 = function (promise) {
      if (promise._65 === 2 && rejections[promise._51]) {
        if (rejections[promise._51].logged) {
          onHandled(promise._51);
        } else {
          clearTimeout(rejections[promise._51].timeout);
        }

        delete rejections[promise._51];
      }
    };

    Promise._87 = function (promise, err) {
      if (promise._40 === 0) {
        promise._51 = id++;
        rejections[promise._51] = {
          displayId: null,
          error: err,
          timeout: setTimeout(onUnhandled.bind(null, promise._51), matchWhitelist(err, DEFAULT_WHITELIST) ? 100 : 2000),
          logged: false
        };
      }
    };

    function onUnhandled(id) {
      if (options.allRejections || matchWhitelist(rejections[id].error, options.whitelist || DEFAULT_WHITELIST)) {
        rejections[id].displayId = displayId++;

        if (options.onUnhandled) {
          rejections[id].logged = true;
          options.onUnhandled(rejections[id].displayId, rejections[id].error);
        } else {
          rejections[id].logged = true;
          logError(rejections[id].displayId, rejections[id].error);
        }
      }
    }

    function onHandled(id) {
      if (rejections[id].logged) {
        if (options.onHandled) {
          options.onHandled(rejections[id].displayId, rejections[id].error);
        } else if (!rejections[id].onUnhandled) {
          console.warn('Promise Rejection Handled (id: ' + rejections[id].displayId + '):');
          console.warn('  This means you can ignore any previous messages of the form "Possible Unhandled Promise Rejection" with id ' + rejections[id].displayId + '.');
        }
      }
    }
  }

  function logError(id, error) {
    console.warn('Possible Unhandled Promise Rejection (id: ' + id + '):');
    var errStr = (error && (error.stack || error)) + '';
    errStr.split('\n').forEach(function (line) {
      console.warn('  ' + line);
    });
  }

  function matchWhitelist(error, list) {
    return list.some(function (cls) {
      return error instanceof cls;
    });
  }
},"a746aeac0009f0e5dc560b0d81b2ff44",["004be1e3b93c14310ddbd367a1c079df"],"node_modules/promise/setimmediate/rejection-tracking.js");