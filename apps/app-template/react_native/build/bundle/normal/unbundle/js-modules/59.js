__d(function (global, _require2, module, exports, _dependencyMap) {
  'use strict';

  var exceptionID = 0;

  function reportException(e, isFatal) {
    var _require = _require2(_dependencyMap[0], 'NativeModules'),
        ExceptionsManager = _require.ExceptionsManager;

    if (ExceptionsManager) {
      var parseErrorStack = _require2(_dependencyMap[1], 'parseErrorStack');

      var stack = parseErrorStack(e);
      var currentExceptionID = ++exceptionID;

      if (isFatal) {
        ExceptionsManager.reportFatalException(e.message, stack, currentExceptionID);
      } else {
        ExceptionsManager.reportSoftException(e.message, stack, currentExceptionID);
      }

      if (__DEV__) {
        var symbolicateStackTrace = _require2(_dependencyMap[2], 'symbolicateStackTrace');

        symbolicateStackTrace(stack).then(function (prettyStack) {
          if (prettyStack) {
            ExceptionsManager.updateExceptionMessage(e.message, prettyStack, currentExceptionID);
          } else {
            throw new Error('The stack is null');
          }
        }).catch(function (error) {
          return console.warn('Unable to symbolicate stack trace: ' + error.message);
        });
      }
    }
  }

  function handleException(e, isFatal) {
    if (!e.message) {
      e = new Error(e);
    }

    if (console._errorOriginal) {
      console._errorOriginal(e.message);
    } else {
      console.error(e.message);
    }

    reportException(e, isFatal);
  }

  function reactConsoleErrorHandler() {
    console._errorOriginal.apply(console, arguments);

    if (!console.reportErrorsAsExceptions) {
      return;
    }

    if (arguments[0] && arguments[0].stack) {
      reportException(arguments[0], false);
    } else {
      var stringifySafe = _require2(_dependencyMap[3], 'stringifySafe');

      var str = Array.prototype.map.call(arguments, stringifySafe).join(', ');

      if (str.slice(0, 10) === '"Warning: ') {
        return;
      }

      var error = new Error('console.error: ' + str);
      error.framesToPop = 1;
      reportException(error, false);
    }
  }

  function installConsoleErrorReporter() {
    if (console._errorOriginal) {
      return;
    }

    console._errorOriginal = console.error.bind(console);
    console.error = reactConsoleErrorHandler;

    if (console.reportErrorsAsExceptions === undefined) {
      console.reportErrorsAsExceptions = true;
    }
  }

  module.exports = {
    handleException: handleException,
    installConsoleErrorReporter: installConsoleErrorReporter
  };
},59,[24,36,60,30],"ExceptionsManager");